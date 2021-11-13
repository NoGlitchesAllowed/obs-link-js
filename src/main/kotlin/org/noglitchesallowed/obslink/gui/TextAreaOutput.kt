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

package org.noglitchesallowed.obslink.gui

import java.awt.Color
import javax.swing.JTextArea
import javax.swing.SwingUtilities

class TextAreaOutput(private val textArea: JTextArea) {
    private val sb = StringBuilder()

    fun write(b: Int, color: Color) {
        if (b == '\r'.code) return
        if (b == '\n'.code) {
            return flush(color)
        }

        sb.append(b.toChar())
    }

    @Synchronized
    private fun flush(color: Color) {
        sb.appendLine()
        val text = sb.toString()
        sb.setLength(0)
        SwingUtilities.invokeLater {
            textArea.foreground = color
            textArea.append(text)
        }
    }
}