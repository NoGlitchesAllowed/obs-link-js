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
import org.noglitchesallowed.obslink.switcher.Switcher
import org.noglitchesallowed.obslink.utils.sendAndLog

class ObsWebSocketReadyState(
    override val conn: WebSocket, val targetTunnelId: String, val switcher: Switcher
) : ConnectionState {
    override fun handle(rawMessage: String): ConnectionState {
        val target = switcher.getParticipants().find { it.participantTunnelId == targetTunnelId }
            ?: return ErrorState("Dead tunnel", this)
        target.conn.sendAndLog(rawMessage)
        return this
    }

    override fun logPrefix(): String = "obs-ws-js/ready/$targetTunnelId"
}
