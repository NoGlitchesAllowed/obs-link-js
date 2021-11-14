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

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.noglitchesallowed.obslink.utils.log
import java.net.ConnectException
import java.net.URI
import kotlin.concurrent.thread

abstract class Connection(uri: String) : WebSocketClient(URI(uri)) {
    abstract fun logPrefix(): Any

    lateinit var other: Connection

    override fun run() {
        setAttachment(logPrefix())
        log("Attempting to connect")
        super.run()
    }

    override fun onOpen(handshakedata: ServerHandshake?) = log("Connected")

    override fun onMessage(message: String) = Unit

    override fun onError(ex: Exception) {
        if (ex is ConnectException)
            return log("Failed to connect, attempting again in 10 seconds")
        log("Error: $ex")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        if (reason != "Connection refused: connect")
            log("Closed - attempting reconnect in 10 seconds")

        thread {
            Thread.sleep(10000)
            reconnect()
        }
    }
}