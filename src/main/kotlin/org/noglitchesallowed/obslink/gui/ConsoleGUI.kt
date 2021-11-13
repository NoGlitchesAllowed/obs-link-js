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
import java.awt.GraphicsEnvironment
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.JFrame
import javax.swing.JScrollPane

class ConsoleGUI(consoleTextArea: ConsoleTextArea) : JFrame() {
    init {
        contentPane = JScrollPane(consoleTextArea,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS)
        title = "noga-obs-link"
        defaultCloseOperation = EXIT_ON_CLOSE
        pack()
        isVisible = true
    }

    companion object {
        @Synchronized
        fun create(): Boolean {
            if (GraphicsEnvironment.isHeadless()) {
                return false
            }

            val consoleTextArea = ConsoleTextArea()
            val textAreaOutput = TextAreaOutput(consoleTextArea)

            fun createCombined(old: PrintStream, color: Color): PrintStream {
                return PrintStream(object : OutputStream() {
                    override fun write(b: Int) {
                        old.write(b)
                        textAreaOutput.write(b, color)
                    }
                })
            }


            System.setOut(createCombined(System.out, Color.WHITE))
            System.setErr(createCombined(System.err, Color.RED))
            ConsoleGUI(consoleTextArea)
            return true
        }
    }
}