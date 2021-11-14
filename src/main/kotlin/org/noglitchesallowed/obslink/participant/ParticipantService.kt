/*
 *     noga-obs-link
 *     Copyright (C) 2021, Paul Schwandes
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.noglitchesallowed.obslink.participant

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.noglitchesallowed.obslink.gui.ConsoleGUI
import org.noglitchesallowed.obslink.system.info.SystemInfoSerializer
import org.noglitchesallowed.obslink.system.stats.StatsRequestInterceptor
import org.noglitchesallowed.obslink.utils.*
import oshi.SystemInfo
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintWriter
import java.net.ConnectException
import java.net.URI
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.system.exitProcess

object ParticipantService {
    private val executor = Executors.newScheduledThreadPool(4)
    private val systemInfo = SystemInfo()
    private lateinit var tunnelId: String

    @JvmStatic
    fun main(args: Array<String>) {
        if (!ConsoleGUI.create()) {
            throw AssertionError("Cannot run local service in headless mode")
        }

        checkAlreadyRunning()

        val file = File("tunnel-id")
        if (!file.exists() || !file.isFile) {
            println(
                """
                    FIRST LAUNCH - PLEASE READ

                    This is "noga-obs-link". It is an application made for No Glitches Allowed events.
                    It links the portable OBS to our services for access through a plugin called "obs-websocket".

                    It also sends system-specific information on first connection and monitors your system
                    to make sure that your stream isn't overloading it. (e.g. too high CPU temparature, etc.)

                    The source code for this program can be found at github.com/noglitchesallowed/obs-link
                    and the program is licensed under the AGPLv3. See COPYING for more information.

                    No personal data (like serial numbers/user account name/etc.) is sent. System information & stats
                    are not stored on the server and only used during tech checking & live production.

                    If you have questions/concerns, close this program and contact the No Glitches Allowed Team
                    admin@noglitchesallowed.org / 
                    
                """.trimIndent()
            )
            tunnelId = UUID.randomUUID().toString()
            file.writeText(tunnelId)
        } else if (args.size <= 2) {
            tunnelId = file.readText()
        } else {
            tunnelId = args[3]
        }

        println("Your tunnel ID is $tunnelId")
        val localObsUri = args.getOrNull(0) ?: "ws://127.0.0.1:4444"
        val localObsConnection = Connection(localObsUri, ConnectionTarget.LOCAL_OBS)

        val switcherUri = args.getOrNull(1) ?: "wss://obslink.noglitchesallowed.org"
        val switcherConnection = Connection(switcherUri, ConnectionTarget.SWITCHER)

        localObsConnection.other = switcherConnection
        switcherConnection.other = localObsConnection

        executor.submit(localObsConnection)
        executor.submit(switcherConnection)
        executor.scheduleWithFixedDelay(DrainQueueTask(switcherConnection), 0L, 1L, TimeUnit.SECONDS)
    }

    private fun checkAlreadyRunning() {
        val currentCommand = ProcessHandle.current().info().command().orElse(null)
        if (currentCommand == null) {
            println("Could not determine current command and perform an already running check")
            return
        }

        val runningCount = ProcessHandle.allProcesses()
            .filter { it.info().command().orElse(null) == currentCommand }
            .count()

        if (runningCount == 1L) {
            return
        }

        val runningPath = Paths.get("").toAbsolutePath().toString()
        if (!currentCommand.startsWith(runningPath)) {
            // Running from IDE/test mode
            println("Detected multiple instances of command but paths not matching.")
            println("Assuming test/dev mode")
            return
        }

        println("DETECTED MULTIPLE INSTANCES RUNNING!")
        println("If this is an error, open task manager")
        println("and terminate all 'javaw' processes.")
        println("If this persists, contact fgeorjje@noglitchesallowed.org")

        try {
            Thread.sleep(20000)
        } catch (e: InterruptedException) {
        }
        exitProcess(-1)
    }

    class DrainQueueTask(val connection: Connection) : Runnable {
        override fun run() {
            val queue = connection.queue
            val toWrite = synchronized(queue) {
                generateSequence { queue.poll() }.toList()
            }
            if (toWrite.isEmpty()) return

            val builder = StringBuilder()
            toWrite.forEach { builder.appendLine(it) }
            val message = GZip.zipString(builder.toString())
            connection.sendOriginal(message)
            connection.log("Sending compressed ${toWrite.size} messages: $message")
        }
    }

    class Connection(uri: String, private val target: ConnectionTarget) : WebSocketClient(URI(uri)) {
        var compress = false
        val queue = LinkedList<String?>()
        lateinit var other: Connection

        override fun send(text: String?) {
            if (compress && target == ConnectionTarget.SWITCHER) {
                synchronized(queue) {
                    queue.add(text)
                }
            } else {
                super.send(text)
            }
        }

        fun sendOriginal(text: String?) {
            super.send(text)
        }

        override fun run() {
            setAttachment(target)
            log("Attempting to connect to $uri")
            super.run()
        }

        override fun onOpen(handshakedata: ServerHandshake?) {
            setAttachment(target)
            log("Connected")
            if (target == ConnectionTarget.SWITCHER) {
                mapOf(
                    "participant-tunnel-id" to tunnelId,
                    "system-info" to SystemInfoSerializer.toMap(systemInfo)
                ).let { sendAndLog(gson.toJson(it)) }
                compress = true
            }
        }

        override fun onMessage(message: String) {
            log("Received $message")
            other.sendAndLog(
                when (target) {
                    ConnectionTarget.LOCAL_OBS -> StatsRequestInterceptor.interceptLocal(message, systemInfo)
                    else -> message
                }
            )
        }

        override fun onError(ex: Exception) {
            if (ex is ConnectException)
                return log("Failed to connect to $uri")
            log("Error: $ex")
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            synchronized(queue) {
                queue.clear()
            }

            compress = false
            if (reason != "Connection refused: connect")
                log("Closed - attempting to reconnect. Code=$code, reason $reason, remote $remote")
            executor.schedule({ reconnect() }, 10L, TimeUnit.SECONDS)
        }
    }

    enum class ConnectionTarget(val desc: String) : WebSocketLogAttachment {
        LOCAL_OBS("Local OBS Connection"),
        SWITCHER(" Switcher Connection");

        override fun logPrefix(): String = desc
    }
}