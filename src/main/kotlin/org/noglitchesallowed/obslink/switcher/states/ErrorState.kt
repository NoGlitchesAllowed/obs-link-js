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

package org.noglitchesallowed.obslink.switcher.states

import org.java_websocket.WebSocket
import org.noglitchesallowed.obslink.utils.WebSocketLogAttachment
import org.noglitchesallowed.obslink.utils.log

class ErrorState(val error: String, val lastState: ConnectionState) : ConnectionState, WebSocketLogAttachment {
    override val conn: WebSocket
        get() = lastState.conn

    override fun handle(rawMessage: String): ConnectionState {
        conn.log("Received message from dead connection: $rawMessage")
        return this
    }

    override fun logPrefix(): String = "ERROR/${lastState.logPrefix()}"
}