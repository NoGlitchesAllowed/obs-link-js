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

package org.noglitchesallowed.obslink.switcher

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.noglitchesallowed.obslink.switcher.states.*
import org.noglitchesallowed.obslink.utils.log
import java.net.InetSocketAddress

class Switcher(address: InetSocketAddress, val secret: String) : WebSocketServer(address) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val hostname = args[0]
            val port = args[1].toInt()
            val secret = args[2]
            Switcher(InetSocketAddress(hostname, port), secret).run()
        }
    }

    private var WebSocket.state: ConnectionState
        get() = getAttachment()
        set(value) = setAttachment(value)

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        conn.setAttachment(UnknownState(conn, this))
        conn.log("Connected from ${conn.remoteSocketAddress}, ${handshake.resourceDescriptor}")
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        conn.log("Disconnected code=$code reason=$reason remote=$remote")
        val state = conn.state
        if (state is ParticipantState) {
            getObsWebsocketJsListeningTo(state.participantTunnelId).forEach { it.close(1000, "Linked connection has closed") }
        }
    }

    override fun onMessage(conn: WebSocket, rawMessage: String) {
        val message = rawMessage.trim()

        conn.log("Received: $message")
        val oldState = conn.state
        val newState = oldState.handle(rawMessage)
        if (oldState == newState) return

        conn.state = newState

        if (newState is ErrorState) {
            conn.close(1000, newState.error)
            conn.log(newState.error)
        } else {
            conn.log("State switched")
        }
    }

    override fun onError(conn: WebSocket?, ex: Exception) {
        conn?.log(ex.stackTraceToString()) ?: ex.printStackTrace()
    }

    override fun onStart() = println("Server started")

    fun getParticipants(): List<ParticipantState> = connections.mapNotNull { it.state as? ParticipantState }

    fun getObsWebsocketJsListeningTo(tunnelId: String) =
        connections.filter { (it.state as? ObsWebSocketReadyState)?.targetTunnelId == tunnelId }
}