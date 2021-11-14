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

package org.noglitchesallowed.obslink.utils

import java.nio.file.Paths
import kotlin.system.exitProcess

object MultiInstanceDetect {
    fun checkAlreadyRunning() {
        val currentCommand = ProcessHandle.current().info().command().orElse(null)
            ?: return println("Could not determine current command and perform an already running check")

        val runningCount = ProcessHandle.allProcesses()
            .filter { it.info().command().orElse(null) == currentCommand }
            .count()

        if (runningCount == 1L) {
            return
        }

        // in the distributed OBS, the JRE is bundled in a subfolder of the running application
        // meaning only one instance of that executable can run at once.
        // this means that if the working directory contains the path to the command,
        // we are in production. otherwise, we're likely running out of an IDE
        val runningPath = Paths.get("").toAbsolutePath().toString()
        if (!currentCommand.startsWith(runningPath)) {
            return println("Detected multiple instances of command but paths not matching. Assuming test/dev mode")
        }

        println("DETECTED MULTIPLE INSTANCES RUNNING! If this is an error, open task manager")
        println("and terminate all 'java' processes. If this persists, contact fgeorjje@noglitchesallowed.org")

        try {
            Thread.sleep(20000)
        } catch (e: InterruptedException) {
        }
        exitProcess(-1)
    }
}