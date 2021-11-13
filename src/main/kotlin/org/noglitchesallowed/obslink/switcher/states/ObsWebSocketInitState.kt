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

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.java_websocket.WebSocket
import org.noglitchesallowed.obslink.switcher.Switcher
import org.noglitchesallowed.obslink.utils.OBSAuth
import org.noglitchesallowed.obslink.utils.sendAndLog

class ObsWebSocketInitState(
    override val conn: WebSocket, val challenge: String, val salt: String, val switcher: Switcher
) : ConnectionState {
    override fun handle(rawMessage: String): ConnectionState {
        val json = JsonParser.parseString(rawMessage).asJsonObject
        val requestType = json["request-type"]?.asString?.lowercase()
        val messageId = json["message-id"]?.asString

        if (requestType != "authenticate" || messageId == null) {
            return ErrorState("Expected Authenticate request, closing connection", this)
        }

        val auth = json["auth"].asString!!

        val tunnelId = switcher.getParticipants()
            .map { it.participantTunnelId }
            .find { OBSAuth.hash("${switcher.secret}:$it", challenge, salt) == auth }
            ?: return ErrorState("Invalid Auth", this)

        val msg = JsonObject()
        msg.addProperty("message-id", messageId)
        conn.sendAndLog(msg.toString())
        return ObsWebSocketReadyState(this.conn, tunnelId, switcher)
    }

    override fun logPrefix(): String = "obs-ws-js/init"
}
