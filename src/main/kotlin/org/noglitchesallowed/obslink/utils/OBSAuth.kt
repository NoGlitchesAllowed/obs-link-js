/*
 *     noga-obs-link
 *     Copyright (C) 2021, Paul Schwandes
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published
 *     by the Free Software Foundation, either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.noglitchesallowed.obslink.utils

import java.security.MessageDigest
import java.util.*

object OBSAuth {
    private val sha256 get() = MessageDigest.getInstance("SHA-256")
    private fun binary_sha256(input: String) = sha256.digest(input.toByteArray())
    private fun base64_encode(input: ByteArray) = Base64.getEncoder().encode(input).let { String(it) }

    fun hash(password: String, challenge: String, salt: String): String {
        // https://github.com/Palakis/obs-websocket/blob/4.x-current/docs/generated/protocol.md#authentication
        // Licensed under GPLv2, see COPYING-OTHER
        val secret_string = password + salt
        val secret_hash = binary_sha256(secret_string)
        val secret = base64_encode(secret_hash)

        val auth_response_string = secret + challenge
        val auth_response_hash = binary_sha256(auth_response_string)
        val auth_response = base64_encode(auth_response_hash)

        return auth_response
    }

    fun matches(a: String, b: String) = MessageDigest.isEqual(a.toByteArray(), b.toByteArray())

    @JvmStatic
    fun main(args: Array<String>) {
        fun read(s: String) = println(s).let { readLine()!! }
        fun readAll(vararg s: String) = s.map { read(it) }
        while (true) {
            val (password, challenge, salt) = readAll("password", "challenge", "salt")
            println(hash(password, challenge, salt))
        }
    }
}
