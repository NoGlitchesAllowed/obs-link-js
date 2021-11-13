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

fun <T> BitInput.readList(reader: (BitInput) -> T) =
    List(readInt(32)) { reader(this) }

fun <T> BitOutput.writeList(list: List<T>, writer: (BitOutput, T) -> Unit) {
    writeInt(32, list.size)
    list.forEach { writer(this, it) }
}