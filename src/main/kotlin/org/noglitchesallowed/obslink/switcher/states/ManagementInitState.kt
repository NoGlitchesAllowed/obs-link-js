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

import com.google.gson.JsonParser
import org.java_websocket.WebSocket
import org.noglitchesallowed.obslink.switcher.Switcher
import org.noglitchesallowed.obslink.utils.OBSAuth

class ManagementInitState(
    override val conn: WebSocket, val challenge: String, val salt: String, val switcher: Switcher
) : ConnectionState {
    override fun handle(rawMessage: String): ConnectionState {
        val json = JsonParser.parseString(rawMessage).asJsonObject
        val response = json["management-auth-response"]?.asString
            ?: return ErrorState("Expected auth", this)

        val expected = OBSAuth.hash(switcher.secret, challenge, salt)
        if (response != expected) {
            return ErrorState("Invalid auth", this)
        }

        return ManagementReadyState(conn, switcher)
    }

    override fun logPrefix(): String = "manager/init"
}
