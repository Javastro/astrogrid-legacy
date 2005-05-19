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

import java.io.*;
import java.net.*;
import java.util.*;
import org.altara.mars.*;

/** Probe is the abstract superclass of all MARS service probes.
*/

public abstract class Probe implements Runnable, Serializable {
	
	protected Service service;

	protected Probe() {
		this(null);
	}

	protected Probe(Service service) {
		this.service = service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Service getService() {
		return service;
	}

	public boolean canRun() {
		if (service == null) return false;
		return service.isDue();
	}

	public final void run() {
		try {
			// check for runnability first
			if (!canRun()) {
				return;
			}

			// get the current controller
			Controller controller = Main.getMain().getController();
		
			// runnable, so run the probe
			Status oldStatus = service.getStatus();
			service.setStatus(doProbe());
			Status newStatus = service.getStatus();

			// first notify the probe was run
			controller.probeRun(service,newStatus);

			// then check for status change
			if (oldStatus.getCode() != newStatus.getCode()) {
				controller.statusChanged(service,oldStatus,newStatus);
			} 

		} catch (Exception ex) {
			// if the probe fails unexpectedly, let the user know
			// (don't know why this would happen, but we need to
			// distinguish it from a known-down state).
			Status stat = new Status(Status.PROBEFAIL);
			stat.setProperty("exception",ex.toString());
			service.setStatus(stat);
            ex.printStackTrace();
		}
	}

	public String toString() {
		return "Probe on "+service.toString();
	}

	protected abstract Status doProbe() throws Exception;
}
