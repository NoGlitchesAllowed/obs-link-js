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

package org.noglitchesallowed.obslink.utils

import com.google.gson.Gson
import org.java_websocket.WebSocket

private fun WebSocket.getLogAttachmentString() = getAttachment<Any?>()
    .let { if (it is WebSocketLogAttachment) it.logPrefix() else it?.toString() }

fun WebSocket.log(s: String) = println("[${getLogAttachmentString()}] $s")
fun WebSocket.sendAndLog(s: String) {
    log("Sending: $s")
    send(s)
}

interface WebSocketLogAttachment {
    fun logPrefix(): String
}

val gson = Gson()