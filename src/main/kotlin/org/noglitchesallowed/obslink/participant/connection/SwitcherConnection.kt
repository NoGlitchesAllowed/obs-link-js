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

package org.noglitchesallowed.obslink.participant.connection

import org.java_websocket.handshake.ServerHandshake
import org.noglitchesallowed.obslink.system.info.SystemInfoSerializer
import org.noglitchesallowed.obslink.utils.GZip
import org.noglitchesallowed.obslink.utils.gson
import oshi.SystemInfo
import java.util.*

class SwitcherConnection(
    uri: String,
    private val tunnelId: String,
    private val systemInfo: SystemInfo
) : Connection(uri) {
    private val queue = LinkedList<String?>()
    private var ready = false

    override fun logPrefix() = "Switcher  Connection"

    override fun send(text: String?) {
        synchronized(queue) {
            queue.add(text)
        }
    }

    override fun onMessage(message: String) {
        other.send(message)
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        super.onOpen(handshakedata)
        val map = mapOf(
            "participant-tunnel-id" to tunnelId,
            "system-info" to SystemInfoSerializer.toMap(systemInfo)
        )
        val message = gson.toJson(map)
        super.send(message)
        synchronized(queue) {
            ready = true
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        synchronized(queue) {
            queue.clear()
            ready = false
        }

        super.onClose(code, reason, remote)
    }

    fun drainQueue() {
        val toWrite = synchronized(queue) {
            if (!ready) return
            generateSequence { queue.poll() }.toList()
        }

        if (toWrite.isEmpty()) return

        val builder = StringBuilder()
        toWrite.forEach { builder.appendLine(it) }
        val message = GZip.zipString(builder.toString())
        super.send(message)
    }
}