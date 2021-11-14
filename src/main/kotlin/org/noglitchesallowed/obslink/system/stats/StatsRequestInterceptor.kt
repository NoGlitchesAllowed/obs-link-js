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
import org.noglitchesallowed.obslink.utils.GZip
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
        val baos = ByteArrayOutputStream()
        val bo = BitOutput(BitOutput.StreamOutput(baos))
        model.write(bo)
        val rawBytes = baos.toByteArray()
        statsMap["system"] = GZip.zip(rawBytes)
        jsonMap["stats"] = statsMap
        return gson.toJson(jsonMap)
    }

    fun interceptSwitcher(message: String): String {
        val json = gson.fromJson(message, MutableMap::class.java).toMutableMap()
        val stats = (json["stats"] as? Map<*, *>)?.toMutableMap() ?: return message

        // Decode: Base64 -> Decompress -> ByteArray -> Bitstream -> System Stats

        try {
            stats["system"] = stats["system"]
                .let { it as String }
                .let { GZip.unzip(it) }
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
            MemoryStats(available)
        }, powerSources.map { ps ->
            PowerSourceStats(
                ps.currentCapacity,
                ps.isCharging, ps.isDischarging, ps.isPowerOnLine, ps.powerUsageRate,
                ps.remainingCapacityPercent, ps.temperature, ps.timeRemainingEstimated,
                ps.timeRemainingInstant, ps.voltage
            )
        }, processor.run {
            ProcessorStats(
                currentFreq.toList(), processorCpuLoadTicks.map { it.toList() }, systemCpuLoadTicks.toList()
            )
        }, sensors.run {
            SensorStats(cpuTemperature, cpuTemperature, fanSpeeds.toList())
        })
    }, systemInfo.operatingSystem.run { OperatingSystemStats(systemUptime) })
}