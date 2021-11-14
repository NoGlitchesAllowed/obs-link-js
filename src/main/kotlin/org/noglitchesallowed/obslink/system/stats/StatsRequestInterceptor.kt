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

import org.noglitchesallowed.obslink.system.stats.model.*
import org.noglitchesallowed.obslink.utils.gson
import oshi.SystemInfo

object StatsRequestInterceptor {
    private const val SYSTEM_STATS_KEY = "systemStats"

    fun interceptLocal(message: String, systemInfo: SystemInfo): String {
        fun processInserts(map: MutableMap<Any?, Any?>) {
            val stats = (map["stats"] as? Map<*, *>)?.toMutableMap()
            if (stats != null) {
                stats[SYSTEM_STATS_KEY] = toModel(systemInfo)
            }

            if (map["update-type"] == "StreamStatus") {
                map[SYSTEM_STATS_KEY] = toModel(systemInfo)
            }

            for ((key, value) in map.entries) {
                if (value is Iterable<*>) {
                    map[key] = value.map {
                        if (it !is Map<*, *>) return@map it
                        val copy = it.toMutableMap()
                        processInserts(copy)
                        copy
                    }
                }

                if (value is Map<*, *>) {
                    val copy = value.toMutableMap()
                    processInserts(copy)
                    map[key] = copy
                }
            }
        }

        val json = gson.fromJson(message, Map::class.java).toMutableMap()
        processInserts(json)
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