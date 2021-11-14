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

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.java_websocket.WebSocket
import org.noglitchesallowed.obslink.switcher.Switcher
import org.noglitchesallowed.obslink.utils.sendAndLog

class ManagementReadyState(override val conn: WebSocket, val switcher: Switcher) : ConnectionState {
    override fun handle(rawMessage: String): ConnectionState {
        val json = JsonParser.parseString(rawMessage).asJsonObject

        val request = json["request-type"]?.asString?.lowercase()?.replace("-", "")
        if (request == null) {
            managementError(conn, "Expected request-type", json)
            return this
        }

        val response = JsonObject()

        json["message-id"]?.asString?.let { response.addProperty("message-id", it) }
        when (request) {
            "listclients" -> {
                val array = JsonArray()
                switcher.getParticipants().forEach { array.add(it.participantTunnelId) }
                response.add("clients", array)
            }
            "getsysteminfo" -> {
                val targetTunnel = json["tunnel-id"]?.asString
                    ?: return managementError(conn, "No tunnel id specified", json)
                val info = switcher.getParticipants().find { it.participantTunnelId == targetTunnel }?.systemInfo
                    ?: return managementError(conn, "Tunnel id does not match to an active connection", json)
                response.add("info", info)
            }

            else -> return managementError(conn, "Invalid request type", json)
        }

        conn.sendAndLog(response.toString())

        return this
    }

    private fun managementError(conn: WebSocket, error: String, json: JsonObject): ConnectionState {
        val response = JsonObject()
        response.addProperty("error", error)
        response.add("request", json)
        conn.sendAndLog(response.toString())
        return this
    }

    override fun logPrefix(): String = "manager"
}
