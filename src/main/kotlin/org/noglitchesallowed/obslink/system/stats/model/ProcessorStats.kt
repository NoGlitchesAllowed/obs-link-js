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

data class ProcessorStats(
    val currentFreq: List<Long>,
    val processorCpuLoadTicks: List<List<Long>>,
    val systemCpuLoadTicks: List<Long>
) {
    companion object {
        fun read(bitInput: BitInput) = ProcessorStats(
            bitInput.readList { it.readLong(64) },
            bitInput.readList { it.readList { it.readLong(64) } },
            bitInput.readList { it.readLong(64) }
        )
    }

    fun write(bitOutput: BitOutput) {
        bitOutput.writeList(currentFreq) { it, value -> it.writeLong(64, value) }
        bitOutput.writeList(processorCpuLoadTicks) { it, value ->
            it.writeList(value) { it2, value2 ->
                it2.writeLong(
                    64,
                    value2
                )
            }
        }
        bitOutput.writeList(systemCpuLoadTicks) { it, value -> it.writeLong(64, value) }
    }
}