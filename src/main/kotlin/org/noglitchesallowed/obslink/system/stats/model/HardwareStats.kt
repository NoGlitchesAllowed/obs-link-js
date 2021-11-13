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

package org.noglitchesallowed.obslink.system.stats.model

import com.googlecode.jinahya.io.BitInput
import com.googlecode.jinahya.io.BitOutput
import org.noglitchesallowed.obslink.system.stats.readList
import org.noglitchesallowed.obslink.system.stats.writeList

data class HardwareStats(
    val memory: MemoryStats,
    val powerSources: List<PowerSourceStats>,
    val processor: ProcessorStats,
    val sensors: SensorStats
) {
    fun write(bitOutput: BitOutput) {
        memory.write(bitOutput)
        bitOutput.writeList(powerSources) { it, value -> value.write(it) }
        processor.write(bitOutput)
        sensors.write(bitOutput)
    }

    companion object {
        fun read(bitInput: BitInput) = HardwareStats(
            MemoryStats.read(bitInput),
            bitInput.readList { PowerSourceStats.read(it) },
            ProcessorStats.read(bitInput),
            SensorStats.read(bitInput)
        )
    }
}