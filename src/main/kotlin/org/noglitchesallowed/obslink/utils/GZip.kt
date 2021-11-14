package org.noglitchesallowed.obslink.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object GZip {
    fun zip(data: ByteArray): String {
        val baos = ByteArrayOutputStream()
        val gzip = GZIPOutputStream(baos)
        gzip.write(data)
        gzip.close()
        return Base64.getEncoder().encodeToString(baos.toByteArray())
    }

    fun zipString(string: String): String = zip(string.toByteArray())

    @JvmName("decompress_bytes")
    fun unzip(string: String) = Base64.getDecoder().decode(string)
        .let { ByteArrayInputStream(it) }
        .let { GZIPInputStream(it) }
        .readAllBytes()!!

    @JvmName("decompress_string")
    fun unzipString(string: String): String = String(unzip(string))
}