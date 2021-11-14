package org.noglitchesallowed.obslink.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.Exception
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