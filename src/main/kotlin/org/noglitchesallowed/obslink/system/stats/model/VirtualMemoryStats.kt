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

data class VirtualMemoryStats(
    val swapPagesIn: Long,
    val swapPagesOut: Long,
    val swapTotal: Long,
    val swapUsed: Long,
    val virtualInUse: Long,
    val virtualMax: Long
) {
    fun write(bitOutput: BitOutput) {
        bitOutput.writeLong(64, swapPagesIn)
        bitOutput.writeLong(64, swapPagesOut)
        bitOutput.writeLong(64, swapTotal)
        bitOutput.writeLong(64, swapUsed)
        bitOutput.writeLong(64, virtualInUse)
        bitOutput.writeLong(64, virtualMax)
    }

    companion object {
        fun read(bitInput: BitInput) = VirtualMemoryStats(
            bitInput.readLong(64),
            bitInput.readLong(64),
            bitInput.readLong(64),
            bitInput.readLong(64),
            bitInput.readLong(64),
            bitInput.readLong(64),
        )
    }
}