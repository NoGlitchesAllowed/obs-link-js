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

package org.noglitchesallowed.obslink.system.info

import oshi.SystemInfo
import oshi.hardware.*
import oshi.software.os.OperatingSystem

object SystemInfoSerializer {
    fun toMap(info: SystemInfo): Map<*, *> = mapOf(
        "hardware" to info.hardware?.toMap(),
        "operatingSystem" to info.operatingSystem?.toMap()
    )

    private fun OperatingSystem.toMap(): Map<*, *> = mapOf(
        "bitness" to this.bitness,
        "family" to this.family,
        "isElevated" to this.isElevated,
        "manufacturer" to this.manufacturer,
        "versionInfo" to this.versionInfo.toMap()
    )

    private fun OperatingSystem.OSVersionInfo.toMap(): Map<*, *> = mapOf(
        "buildNumber" to this.buildNumber,
    )

    private fun HardwareAbstractionLayer.toMap(): Map<*, *> = mapOf(
        "graphicsCards" to this.graphicsCards?.map { it.toMap() },
        "memory" to this.memory?.toMap(),
        "processor" to this.processor?.toMap(),
        "powerSources" to this.powerSources.map { it.toMap() },
        "sensors" to this.sensors?.toMap(),
        "soundCards" to this.soundCards?.map { it.toMap() }
    )

    private fun PowerSource.toMap(): Map<*, *> = mapOf(
        "capacityUnits" to this.capacityUnits,
        "chemistry" to this.chemistry,
        "cycleCount" to this.cycleCount,
        "designCapacity" to this.designCapacity,
        "maxCapacity" to this.maxCapacity
    )

    private fun GraphicsCard.toMap(): Map<*, *> = mapOf(
        "name" to this.name,
        "vendor" to this.vendor,
        "vRam" to this.vRam
    )

    private fun SoundCard.toMap(): Map<*, *> = mapOf(
        "name" to this.name
    )

    private fun Sensors.toMap(): Map<*, *> = mapOf(
        "cpuTemperature" to this.cpuTemperature,
    )

    private fun CentralProcessor.toMap(): Map<*, *> = mapOf(
        "processorIdentifier" to this.processorIdentifier?.toMap(),
    )

    private fun CentralProcessor.ProcessorIdentifier.toMap(): Map<*, *> = mapOf(
        "name" to this.name,
    )

    private fun GlobalMemory.toMap(): Map<*, *> = mapOf(
        "total" to this.total,
        "physicalMemory" to this.physicalMemory?.map { it.toMap() },
    )

    private fun PhysicalMemory.toMap() = mapOf(
        "bankLabel" to this.bankLabel,
        "capacity" to this.capacity,
        "clockSpeed" to this.clockSpeed,
        "manufacturer" to this.manufacturer,
        "memoryType" to this.memoryType
    )

}

