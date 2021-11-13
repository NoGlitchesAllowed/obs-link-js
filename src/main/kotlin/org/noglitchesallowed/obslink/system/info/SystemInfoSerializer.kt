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
        "codeName" to this.codeName,
        "version" to this.version
    )

    private fun HardwareAbstractionLayer.toMap(): Map<*, *> = mapOf(
        "computerSystem" to this.computerSystem?.toMap(),
        "memory" to this.memory?.toMap(),
        "powerSources" to this.powerSources?.map { it.toMap() },
        "processor" to this.processor?.toMap(),
        "sensors" to this.sensors?.toMap(),
        "soundCards" to this.soundCards?.map { it.toMap() },
    )

    private fun SoundCard.toMap(): Map<*, *> = mapOf(
        "codec" to this.codec,
        "driverVersion" to this.driverVersion,
        "name" to this.name
    )

    private fun Sensors.toMap(): Map<*, *> = mapOf(
        "cpuTemperature" to this.cpuTemperature,
        "cpuVoltage" to this.cpuVoltage,
        "fanSpeeds" to this.fanSpeeds.toList()
    )

    private fun CentralProcessor.toMap(): Map<*, *> = mapOf(
        "logicalProcessorCount" to this.logicalProcessorCount,
        "maxFreq" to this.maxFreq,
        "physicalPackageCount" to this.physicalPackageCount,
        "physicalProcessorCount" to this.physicalProcessorCount,
        "processorIdentifier" to this.processorIdentifier?.toMap(),
    )

    private fun CentralProcessor.ProcessorIdentifier.toMap(): Map<*, *> = mapOf(
        "family" to this.family,
        "identifier" to this.identifier,
        "isCpu64bit" to this.isCpu64bit,
        "microarchitecture" to this.microarchitecture,
        "model" to this.model,
        "name" to this.name,
        "stepping" to this.stepping,
        "vendor" to this.vendor
    )

    private fun PowerSource.toMap(): Map<*, *> = mapOf(
        "amperage" to this.amperage,
        "capacityUnits" to this.capacityUnits,
        "chemistry" to this.chemistry,
        "cycleCount" to this.cycleCount,
        "designCapacity" to this.designCapacity,
        "deviceName" to this.deviceName,
        "manufactureDate" to this.manufactureDate?.toString(),
        "manufacturer" to this.manufacturer,
        "maxCapacity" to this.maxCapacity,
        "name" to this.name
    )

    private fun GlobalMemory.toMap(): Map<*, *> = mapOf(
        "available" to this.available,
        "pageSize" to this.pageSize,
        "total" to this.total,
        "physicalMemory" to this.physicalMemory?.map { it.toMap() },
        "virtualMemory" to this.virtualMemory?.toMap()
    )

    private fun VirtualMemory.toMap(): Map<*, *> = mapOf(
        "swapPagesIn" to this.swapPagesIn,
        "swapPagesOut" to this.swapPagesOut,
        "swapTotal" to this.swapTotal,
        "swapUsed" to this.swapUsed,
        "virtualInUse" to this.virtualInUse,
        "virtualMax" to this.virtualMax
    )

    private fun PhysicalMemory.toMap() = mapOf(
        "bankLabel" to this.bankLabel,
        "capacity" to this.capacity,
        "clockSpeed" to this.clockSpeed,
        "manufacturer" to this.manufacturer,
        "memoryType" to this.memoryType
    )

    private fun ComputerSystem.toMap(): Map<*, *> = mapOf(
        "baseboard" to this.baseboard?.toMap(),
        "firmware" to this.firmware?.toMap(),
        "manufacturer" to this.manufacturer,
        "model" to this.model,
    )

    private fun Firmware.toMap(): Map<*, *> = mapOf(
        "description" to this.description,
        "manufacturer" to this.manufacturer,
        "name" to this.name,
        "releaseDate" to this.releaseDate,
        "version" to this.version
    )

    private fun Baseboard.toMap(): Map<*, *> = mapOf(
        "manufacturer" to this.manufacturer,
        "model" to this.model,
        "version" to this.version
    )
}