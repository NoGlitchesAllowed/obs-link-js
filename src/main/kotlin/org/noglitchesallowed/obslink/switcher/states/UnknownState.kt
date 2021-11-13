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
import org.noglitchesallowed.obslink.utils.sendAndLog
import java.security.SecureRandom
import java.util.*

class UnknownState(override val conn: WebSocket, val switcher: Switcher) : ConnectionState {
    private val random = SecureRandom()

    override fun handle(rawMessage: String): ConnectionState {
        val json = JsonParser.parseString(rawMessage).asJsonObject

        // First message determines the type of the connection
        // Participant connections send a tunnel id
        val participantTunnelId = json["participant-tunnel-id"]?.asString
        val systemInfo = json["system-info"]?.asJsonObject
        if (participantTunnelId != null && systemInfo != null) {
            if (switcher.getParticipants().any { it.participantTunnelId == participantTunnelId }) {
                return ErrorState("Duplicate connection detected with $participantTunnelId", this)
            }

            return ParticipantState(conn, participantTunnelId, systemInfo, switcher)
        }

        // obs-websocket-js on NodeCG sends {"request-type:"GetAuthRequired"}
        val requestType = json["request-type"]?.asString?.lowercase()
        val messageId = json["message-id"]?.asString
        if (requestType == "getauthrequired") {
            // Simulate the OBS authentication process
            val challenge: String = getRandomString()
            val salt: String = getRandomString()
            val msg = JsonObject()
            msg.addProperty("message-id", messageId)
            msg.addProperty("authRequired", true)
            msg.addProperty("challenge", challenge)
            msg.addProperty("salt", salt)
            conn.sendAndLog(msg.toString())
            return ObsWebSocketInitState(conn, challenge, salt, switcher)
        }

        // Standalone connection for NodeCG management
        val listClientsPassword = json["management-auth-request"]
        if (listClientsPassword != null) {
            // Simulate the OBS authentication process
            val challenge: String = getRandomString()
            val salt: String = getRandomString()
            val msg = JsonObject()
            msg.addProperty("management-auth-challenge", challenge)
            msg.addProperty("management-auth-salt", salt)
            conn.sendAndLog(msg.toString())
            return ManagementInitState(conn, challenge, salt, switcher)
        }

        return ErrorState("Invalid first message", this)
    }

    private fun getRandomString() = ByteArray(32).also { random.nextBytes(it) }
        .let { Base64.getEncoder().encodeToString(it) }

    override fun logPrefix(): String = "Unknown"
}