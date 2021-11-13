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

data class SensorStats(
    val cpuTemperature: Double,
    val cpuVoltage: Double,
    val fanSpeeds: List<Int>
) {
    companion object {
        fun read(bitInput: BitInput) = SensorStats(
            bitInput.readDouble(),
            bitInput.readDouble(),
            bitInput.readList { it.readInt(32) }
        )
    }

    fun write(bitOutput: BitOutput) {
        bitOutput.writeDouble(cpuTemperature)
        bitOutput.writeDouble(cpuVoltage)
        bitOutput.writeList(fanSpeeds) { it, value -> it.writeInt(32, value) }
    }
}