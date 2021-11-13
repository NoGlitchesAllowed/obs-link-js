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

package org.noglitchesallowed.obslink.system.stats

import com.googlecode.jinahya.io.BitInput
import com.googlecode.jinahya.io.BitOutput
import org.noglitchesallowed.obslink.system.stats.model.*
import org.noglitchesallowed.obslink.utils.gson
import oshi.SystemInfo
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object StatsRequestInterceptor {
    fun interceptLocal(message: String, systemInfo: SystemInfo): String {
        val jsonMap = gson.fromJson(message, Map::class.java).toMutableMap()
        val statsMap = (jsonMap["stats"] as? Map<*, *>)?.toMutableMap() ?: return message

        // Encode: System Stats -> Bitstream -> ByteArray -> Compress -> Base64
        val model = toModel(systemInfo)
        //println("DEBUG: Encoding $model")
        var baos = ByteArrayOutputStream()
        val bo = BitOutput(BitOutput.StreamOutput(baos))
        model.write(bo)
        val rawBytes = baos.toByteArray()
        //println("DEBUG: rawBytes ${rawBytes.joinToString(" ")}")

        baos = ByteArrayOutputStream()
        val gzip = GZIPOutputStream(baos)
        gzip.write(rawBytes)
        gzip.flush()
        gzip.close()
        val compressedBytes = baos.toByteArray()
        //println("DEBUG: compressedBytes ${compressedBytes.joinToString(" ")}")

        val base64 = compressedBytes.let { Base64.getEncoder().encodeToString(it) }
        //println("DEBUG: base64 $base64")

        statsMap["system"] = base64
        jsonMap["stats"] = statsMap
        return gson.toJson(jsonMap)
    }

    fun interceptSwitcher(message: String): String {
        val json = gson.fromJson(message, MutableMap::class.java).toMutableMap()
        val stats = (json["stats"] as? Map<*, *>)?.toMutableMap() ?: return message

        // Decode: Base64 -> Decompress -> ByteArray -> Bitstream -> System Stats

        try {
            stats["system"] = stats["system"]
                //.also { println("DEBUG: base64 $it") }
                .let { Base64.getDecoder().decode(it.toString()) }
                //.also { println("DEBUG: compressedBytes ${it.joinToString(" ")}") }
                .let { ByteArrayInputStream(it) }
                .let { GZIPInputStream(it) }
                .readAllBytes()
                //.also { println("DEBUG: rawBytes ${it.joinToString(" ")}") }
                .let { ByteArrayInputStream(it) }
                .let { BitInput(BitInput.StreamInput(it)) }
                .let { SystemStats.read(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            stats.remove("system")
        }

        json["stats"] = stats
        return gson.toJson(json)
    }

    private fun toModel(systemInfo: SystemInfo) = SystemStats(systemInfo.hardware.run {
        HardwareStats(memory.run {
            MemoryStats(available, pageSize, total,
                virtualMemory.run {
                    VirtualMemoryStats(
                        swapPagesIn, swapPagesOut, swapTotal,
                        swapUsed, virtualInUse, virtualMax
                    )
                })
        }, powerSources.map { ps ->
            PowerSourceStats(
                ps.currentCapacity,
                ps.isCharging, ps.isDischarging, ps.isPowerOnLine, ps.powerUsageRate,
                ps.remainingCapacityPercent, ps.temperature, ps.timeRemainingEstimated,
                ps.timeRemainingInstant, ps.voltage
            )
        }, processor.run {
            ProcessorStats(
                contextSwitches, currentFreq.toList(), interrupts,
                processorCpuLoadTicks.map { it.toList() }, systemCpuLoadTicks.toList()
            )
        }, sensors.run {
            SensorStats(cpuTemperature, cpuTemperature, fanSpeeds.toList())
        })
    }, systemInfo.operatingSystem.run { OperatingSystemStats(systemUptime) })
}