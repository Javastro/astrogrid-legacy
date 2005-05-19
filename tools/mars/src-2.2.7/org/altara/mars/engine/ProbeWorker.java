/* MARS Network Monitoring Engine
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2002 Leapfrog Research & Development, LLC

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, it is available at 
	http:///www.gnu.org/copyleft/gpl.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place - Suite 330,
	Boston, MA  02111-1307, USA.
*/

package org.altara.mars.engine;

import org.altara.util.*;

/** This worker runs Probes in a circular queue. Each Probe is checked to
	see if it's ready to run and is placed back into the queue. ProbeWorker
	runs at a fairly low priority and yields often, so as not to interfere
	with interactive tasks.
*/

public class ProbeWorker extends Worker {

	private static final int PW_PRIO = 2;
	private static final int SPIN_DELAY = 1000;

	private static int nxid = 0;

	private Controller controller;
	private int spincount;

	public ProbeWorker (Controller controller) {
		super("ProbeWorker "+(nxid++));
		setPriority(PW_PRIO);
		this.controller = controller;
		this.spincount = 0;
	}

	protected void work() throws InterruptedException {
		Probe nextProbe = controller.getNextProbe();
		if (nextProbe.canRun()) {
			nextProbe.run();
			spincount = 0;
			controller.returnProbe(nextProbe);
		} else {
			// return the non-runnable probe immediately
			controller.returnProbe(nextProbe);
			// check for spin
			if (++spincount > controller.queueSize()) {
				Thread.sleep(SPIN_DELAY);
			} 
		}
		Thread.yield();
	}

	protected void workExit() {
		// log here when we get decent logging.
	}
}
