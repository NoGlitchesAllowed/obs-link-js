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

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object GZip {
    private const val MAX_MESSAGE_SIZE = 1024 * 1024

    fun zip(data: ByteArray): String {
        val baos = ByteArrayOutputStream()
        val gzip = GZIPOutputStream(baos)
        gzip.write(data)
        gzip.close()
        return Base64.getEncoder().encodeToString(baos.toByteArray())
    }

    fun zipString(string: String): String = zip(string.toByteArray())

    fun unzip(string: String): ByteArray {
        val gzip = Base64.getDecoder().decode(string)
            .let { ByteArrayInputStream(it) }
            .let { GZIPInputStream(it) }
        val result = gzip.readNBytes(MAX_MESSAGE_SIZE)
        if (gzip.available() == 1) {
            throw Exception("Possible zip bomb: $string")
        }
        return result
    }

    fun unzipString(string: String): String = String(unzip(string))
}