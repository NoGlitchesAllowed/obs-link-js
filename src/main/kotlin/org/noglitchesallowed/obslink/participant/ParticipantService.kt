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

import org.noglitchesallowed.obslink.participant.connection.LocalOBSConnection
import org.noglitchesallowed.obslink.participant.connection.SwitcherConnection
import org.noglitchesallowed.obslink.utils.MultiInstanceDetect
import oshi.SystemInfo
import java.io.File
import java.util.*
import kotlin.concurrent.thread

object ParticipantService {
    private val systemInfo = SystemInfo()
    private lateinit var tunnelId: String

    @JvmStatic
    fun main(args: Array<String>) {
        MultiInstanceDetect.checkAlreadyRunning()

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
                    admin@noglitchesallowed.org / https://discord.gg/WfcvgPv 
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
        val localObsConnection = LocalOBSConnection(localObsUri, systemInfo)

        val switcherUri = args.getOrNull(1) ?: "wss://obslink.noglitchesallowed.org"
        val switcherConnection = SwitcherConnection(switcherUri, tunnelId, systemInfo)

        localObsConnection.other = switcherConnection
        switcherConnection.other = localObsConnection

        Thread.setDefaultUncaughtExceptionHandler { _, e -> e.printStackTrace() }
        thread { localObsConnection.run() }
        thread { switcherConnection.run() }
        thread {
            while (true) {
                Thread.sleep(2000)
                switcherConnection.drainQueue()
            }
        }
    }
}