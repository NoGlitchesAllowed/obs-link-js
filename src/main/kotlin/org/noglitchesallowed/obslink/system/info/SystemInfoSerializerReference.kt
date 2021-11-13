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

import io.kaitai.struct.ByteBufferKaitaiStream
import oshi.SystemInfo
import oshi.hardware.*
import oshi.software.os.*

// NOT USED - REFERENCE ONLY
object SystemInfoSerializerReference {
    fun toMap(info: SystemInfo): Map<*, *> = mapOf(
        "hardware" to info.hardware?.toMap(),
        "operatingSystem" to info.operatingSystem?.toMap()
    )

    private fun OperatingSystem.toMap(): Map<*, *> = mapOf(
        "bitness" to this.bitness,
        "family" to this.family,
        "fileSystem" to this.fileSystem?.toMap(),
        "internetProtocolStats" to this.internetProtocolStats?.toMap(),
        "isElevated" to this.isElevated,
        "manufacturer" to this.manufacturer,
        "networkParams" to this.networkParams?.toMap(),
        "processCount" to this.processCount,
        "processId" to this.processId,
        "processes" to this.processes?.map { it.toMap() },
        "services" to this.services?.map { it.toMap() },
        "sessions" to this.sessions?.map { it.toMap() },
        "systemBootTime" to this.systemBootTime,
        "systemUptime" to this.systemUptime,
        "threadCount" to this.threadCount,
        "versionInfo" to this.versionInfo.toMap()
    )

    private fun OperatingSystem.OSVersionInfo.toMap(): Map<*, *> = mapOf(
        "buildNumber" to this.buildNumber,
        "codeName" to this.codeName,
        "version" to this.version
    )

    private fun OSSession.toMap() = mapOf(
        "host" to this.host,
        "terminalDevice" to this.terminalDevice,
        "userName" to this.userName,
        "loginTime" to this.loginTime,
    )

    private fun OSService.toMap(): Map<*, *> = mapOf(
        "name" to this.name,
        "processID" to this.processID,
        "state" to this.state
    )

    private fun OSProcess.toMap(): Map<*, *> = mapOf(
        "affinityMask" to this.affinityMask,
        "arguments" to this.arguments,
        "bitness" to this.bitness,
        "bytesRead" to this.bytesRead,
        "bytesWritten" to this.bytesWritten,
        "commandLine" to this.commandLine,
        "contextSwitches" to this.contextSwitches,
        "currentWorkingDirectory" to this.currentWorkingDirectory,
        "environmentVariables" to this.environmentVariables,
        "group" to this.group,
        "groupID" to this.groupID,
        "kernelTime" to this.kernelTime,
        "majorFaults" to this.majorFaults,
        "minorFaults" to this.minorFaults,
        "name" to this.name,
        "openFiles" to this.openFiles,
        "parentProcessID" to this.parentProcessID,
        "path" to this.path,
        "priority" to this.priority,
        "processCpuLoadCumulative" to this.processCpuLoadCumulative,
        "processID" to this.processID,
        "residentSetSize" to this.residentSetSize,
        "startTime" to this.startTime,
        "state" to this.state,
        "threadCount" to this.threadCount,
        "threadDetails" to this.threadDetails?.map { it.toMap() },
        "upTime" to this.upTime,
        "user" to this.user,
        "userID" to this.userID,
        "userTime" to this.userTime,
        "virtualSize" to this.virtualSize
    )

    private fun OSThread.toMap(): Map<*, *> = mapOf(
        "name" to this.name,
        "contextSwitches" to this.contextSwitches,
        "kernelTime" to this.kernelTime,
        "state" to this.state,
        "majorFaults" to this.majorFaults,
        "minorFaults" to this.minorFaults,
        "owningProcessId" to this.owningProcessId,
        "priority" to this.priority,
        "startMemoryAddress" to this.startMemoryAddress,
        "startTime" to this.startTime,
        "threadCpuLoadCumulative" to this.threadCpuLoadCumulative,
        "threadId" to this.threadId,
        "upTime" to this.upTime,
        "userTime" to this.userTime
    )

    private fun NetworkParams.toMap(): Map<*, *> = mapOf(
        "dnsServers" to this.dnsServers,
        "domainName" to this.domainName,
        "hostName" to this.hostName,
        "ipv4DefaultGateway" to this.ipv4DefaultGateway,
        "ipv6DefaultGateway" to this.ipv6DefaultGateway
    )

    private fun InternetProtocolStats.toMap(): Map<*, *> = mapOf(
        "connections" to this.connections?.map { it.toMap() },
        "tcPv4Stats" to this.tcPv4Stats?.toMap(),
        "tcPv6Stats" to this.tcPv6Stats?.toMap(),
        "udPv4Stats" to this.udPv4Stats?.toMap(),
        "udPv6Stats" to this.udPv6Stats?.toMap()
    )

    private fun InternetProtocolStats.UdpStats.toMap(): Map<*, *> = mapOf(
        "datagramsNoPort" to this.datagramsNoPort,
        "datagramsReceived" to this.datagramsReceived,
        "datagramsReceivedErrors" to this.datagramsReceivedErrors,
        "datagramsSent" to this.datagramsSent
    )

    private fun InternetProtocolStats.TcpStats.toMap(): Map<*, *> = mapOf(
        "connectionFailures" to this.connectionFailures,
        "connectionsActive" to this.connectionsActive,
        "connectionsEstablished" to this.connectionsEstablished,
        "connectionsPassive" to this.connectionsPassive,
        "connectionsReset" to this.connectionsReset,
        "inErrors" to this.inErrors,
        "outResets" to this.outResets,
        "segmentsReceived" to this.segmentsReceived,
        "segmentsRetransmitted" to this.segmentsRetransmitted,
        "segmentsSent" to this.segmentsSent
    )

    private fun InternetProtocolStats.IPConnection.toMap(): Map<*, *> = mapOf(
        "foreignAddress" to this.foreignAddress.joinToString(),
        "foreignPort" to this.foreignPort,
        "localAddress" to this.localAddress.joinToString(),
        "localPort" to this.localPort,
        "receiveQueue" to this.receiveQueue,
        "state" to this.state,
        "transmitQueue" to this.transmitQueue,
        "type" to this.type,
        "getowningProcessId()" to this.getowningProcessId()
    )

    private fun FileSystem.toMap(): Map<*, *> = mapOf(
        "fileStores" to this.fileStores?.map { it.toMap() },
        "maxFileDescriptors" to this.maxFileDescriptors,
        "openFileDescriptors" to this.openFileDescriptors
    )

    private fun OSFileStore.toMap(): Map<*, *> = mapOf(
        "description" to this.description,
        "freeInodes" to this.freeInodes,
        "freeSpace" to this.freeSpace,
        "label" to this.label,
        "logicalVolume" to this.logicalVolume,
        "mount" to this.mount,
        "name" to this.name,
        "options" to this.options,
        "totalInodes" to this.totalInodes,
        "totalSpace" to this.totalSpace,
        "type" to this.type,
        "usableSpace" to this.usableSpace,
        "uuid" to this.uuid,
        "volume" to this.volume
    )

    private fun HardwareAbstractionLayer.toMap(): Map<*, *> = mapOf(
        "computerSystem" to this.computerSystem?.toMap(),
        "diskStores" to this.diskStores?.map { it.toMap() },
        "displays" to this.displays?.map { it.toMap() },
        "logicalVolumeGroups" to this.logicalVolumeGroups?.map { it.toMap() },
        "memory" to this.memory?.toMap(),
        "getNetworkIFs(true)" to this.getNetworkIFs(true)?.map { it.toMap() },
        "powerSources" to this.powerSources?.map { it.toMap() },
        "processor" to this.processor?.toMap(),
        "sensors" to this.sensors?.toMap(),
        "soundCards" to this.soundCards?.map { it.toMap() },
        "getUsbDevices(false)" to this.getUsbDevices(false)?.map { it.toMap() },
    )

    private fun UsbDevice.toMap(): Map<*, *> = mapOf(
        "connectedDevices" to this.connectedDevices?.map { it.toMap() },
        "name" to this.name,
        "productId" to this.productId,
        "serialNumber" to this.serialNumber,
        "uniqueDeviceId" to this.uniqueDeviceId,
        "vendor" to this.vendor,
        "vendorId" to this.vendorId
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
        "contextSwitches" to this.contextSwitches,
        "currentFreq" to this.currentFreq.toList(),
        "interrupts" to this.interrupts,
        "logicalProcessorCount" to this.logicalProcessorCount,
        "logicalProcessors" to this.logicalProcessors?.map { it.toMap() },
        "maxFreq" to this.maxFreq,
        "physicalPackageCount" to this.physicalPackageCount,
        "physicalProcessorCount" to this.physicalProcessorCount,
        "processorCpuLoadTicks" to this.processorCpuLoadTicks.map { it.toList() },
        "processorIdentifier" to this.processorIdentifier?.toMap(),
        "systemCpuLoadTicks" to this.systemCpuLoadTicks.toList()
    )

    private fun CentralProcessor.ProcessorIdentifier.toMap(): Map<*, *> = mapOf(
        "family" to this.family,
        "identifier" to this.identifier,
        "isCpu64bit" to this.isCpu64bit,
        "microarchitecture" to this.microarchitecture,
        "model" to this.model,
        "name" to this.name,
        "processorID" to this.processorID,
        "stepping" to this.stepping,
        "vendor" to this.vendor
    )

    private fun CentralProcessor.LogicalProcessor.toMap(): Map<*, *> = mapOf(
        "numaNode" to this.numaNode,
        "physicalPackageNumber" to this.physicalPackageNumber,
        "physicalProcessorNumber" to this.physicalProcessorNumber,
        "processorGroup" to this.processorGroup,
        "processorNumber" to this.processorNumber
    )

    private fun PowerSource.toMap(): Map<*, *> = mapOf(
        "amperage" to this.amperage,
        "capacityUnits" to this.capacityUnits,
        "chemistry" to this.chemistry,
        "currentCapacity" to this.currentCapacity,
        "cycleCount" to this.cycleCount,
        "designCapacity" to this.designCapacity,
        "deviceName" to this.deviceName,
        "isCharging" to this.isCharging,
        "isDischarging" to this.isDischarging,
        "isPowerOnLine" to this.isPowerOnLine,
        "manufactureDate" to this.manufactureDate?.toString(),
        "manufacturer" to this.manufacturer,
        "maxCapacity" to this.maxCapacity,
        "name" to this.name,
        "powerUsageRate" to this.powerUsageRate,
        "remainingCapacityPercent" to this.remainingCapacityPercent,
        "serialNumber" to this.serialNumber,
        "temperature" to this.temperature,
        "timeRemainingEstimated" to this.timeRemainingEstimated,
        "timeRemainingInstant" to this.timeRemainingInstant,
        "voltage" to this.voltage
    )

    private fun NetworkIF.toMap() = mapOf(
        "bytesRecv" to this.bytesRecv,
        "bytesSent" to this.bytesSent,
        "collisions" to this.collisions,
        "inDrops" to this.inDrops,
        "inErrors" to this.inErrors,
        "index" to this.index,
        "ifType" to this.ifType,
        "mtu" to this.mtu,
        "ndisPhysicalMediumType" to this.ndisPhysicalMediumType,
        "outErrors" to this.outErrors,
        "packetsRecv" to this.packetsRecv,
        "packetsSent" to this.packetsSent,
        "speed" to this.speed,
        "timeStamp" to this.timeStamp,
        "displayName" to this.displayName,
        "iPv4addr" to this.iPv4addr,
        "iPv6addr" to this.iPv6addr,
        "ifOperStatus" to this.ifOperStatus,
        "isConnectorPresent" to this.isConnectorPresent,
        "isKnownVmMacAddr" to this.isKnownVmMacAddr,
        "macaddr" to this.macaddr,
        "prefixLengths" to this.prefixLengths
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


    private fun LogicalVolumeGroup.toMap() = mapOf(
        "logicalVolumes" to this.logicalVolumes,
        "name" to this.name,
        "physicalVolumes" to this.physicalVolumes
    )

    private fun Edid.toMap() = mapOf(
        "mfgStr" to this.mfgStr(),
        "mfgWeek" to this.mfgWeek(),
        "mfgYear" to this.mfgYear(),
        "productCode" to this.productCode(),
        "screenSizeH" to this.screenSizeH(),
        "screenSizeV" to this.screenSizeV(),
        "serial" to this.serial()
    )

    private fun Display.toMap() = ByteBufferKaitaiStream(this.edid).let { Edid(it) }.toMap()

    private fun HWDiskStore.toMap(): Map<*, *> = mapOf(
        "currentQueueLength" to this.currentQueueLength,
        "readBytes" to this.readBytes,
        "reads" to this.reads,
        "size" to this.size,
        "timeStamp" to this.timeStamp,
        "transferTime" to this.transferTime,
        "writeBytes" to this.writeBytes,
        "writes" to this.writes,
        "model" to this.model,
        "name" to this.name,
        "serial" to this.serial,
        "partitions" to this.partitions?.map { it.toMap() }
    )

    private fun HWPartition.toMap() = mapOf(
        "identification" to this.identification,
        "mountPoint" to this.mountPoint,
        "name" to this.name,
        "type" to this.type,
        "uuid" to this.uuid,
        "major" to this.major,
        "minor" to this.minor
    )

    private fun ComputerSystem.toMap(): Map<*, *> = mapOf(
        "baseboard" to this.baseboard?.toMap(),
        "firmware" to this.firmware?.toMap(),
        "hardwareUUID" to this.hardwareUUID,
        "manufacturer" to this.manufacturer,
        "model" to this.model,
        "serialNumber" to this.serialNumber
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
        "serialNumber" to this.serialNumber,
        "version" to this.version
    )
}
