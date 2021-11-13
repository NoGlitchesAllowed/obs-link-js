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

data class PowerSourceStats(
    val currentCapacity: Int,
    val isCharging: Boolean,
    val isDischarging: Boolean,
    val isPowerOnLine: Boolean,
    val powerUsageRate: Double,
    val remainingCapacityPercent: Double,
    val temperature: Double,
    val timeRemainingEstimated: Double,
    val timeRemainingInstant: Double,
    val voltage: Double
) {
    fun write(bitOutput: BitOutput) {
        bitOutput.writeInt(32, currentCapacity)
        bitOutput.writeBoolean(isCharging)
        bitOutput.writeBoolean(isDischarging)
        bitOutput.writeBoolean(isPowerOnLine)
        bitOutput.writeDouble(powerUsageRate)
        bitOutput.writeDouble(remainingCapacityPercent)
        bitOutput.writeDouble(temperature)
        bitOutput.writeDouble(timeRemainingEstimated)
        bitOutput.writeDouble(timeRemainingInstant)
        bitOutput.writeDouble(voltage)
    }

    companion object {
        fun read(bitInput: BitInput) = PowerSourceStats(
            bitInput.readInt(32),
            bitInput.readBoolean(), bitInput.readBoolean(), bitInput.readBoolean(),
            bitInput.readDouble(), bitInput.readDouble(), bitInput.readDouble(),
            bitInput.readDouble(), bitInput.readDouble(), bitInput.readDouble()
        )
    }


}