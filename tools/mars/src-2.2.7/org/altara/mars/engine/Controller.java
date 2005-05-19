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
import org.altara.mars.*;
import java.util.*;

/** This class controls the MARS monitoring engine. It manages the monitoring
	work queue, the monitoring threads, and the service status change list.
*/

public class Controller implements MarsModelListener {

	private MarsModel model;
	private Queue runQueue;
	private HashSet statListeners;
	private HashSet probeListeners;
	private HashSet notifyListeners;
	private ProbeWorker[] workers;
	private int checkouts;
	private boolean active;

	public Controller() {
		model = null;
		runQueue = new Queue();
		checkouts = 0;
		statListeners = new HashSet();
		probeListeners = new HashSet();
		notifyListeners = new HashSet();
		active = false;

        // initialize engine debugger framework
        new Debug();

        // enable periodic re-resolution of names, for dynamic DNS
        System.setProperty("networkaddress.cache.ttl","300");

        // create a new Notifier to handle notification
        new Notifier(this);
	}

	public synchronized void start() {
		if (active) {
			// we're already started, so fail silently.
			return;
		}

		if (model == null) {
			// no model means don't start
			throw new RuntimeException("Can't start with null model");
		}

		// build the run queue (is this necessary?)
		runQueue = new Queue();
		int workerCount = buildRunQueue();

		// create and start a static thread pool
		workers = new ProbeWorker[workerCount];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new ProbeWorker(this);
			workers[i].start();
		}

		// signify successful start
		this.checkouts = 0;
		this.active = true;

        Main.getMain().showStatus("Started monitoring...");
	}

	public synchronized void stop() {
		if (!active) {
			// we're already stopped, so fail silently.
			return;
		}

		// shut down each thread
		for (int i = 0; i < workers.length; i++) {
			workers[i].kill();
		}

		// signify successful stop
		this.checkouts = 0;
		this.active = false;

        Main.getMain().showStatus("Stopped monitoring.");
	}

	public boolean isActive() {
		return active;
	}

	public MarsModel getModel() {
		return model;
	}

	public synchronized void setModel(MarsModel model) {
		if (active) {
			// programming error - must be stopped before setModel.
			throw new RuntimeException("Can't change model while running");
		}
		this.model = model;
		model.addMarsModelListener(this);
	}

	public void addNotificationListener(NotificationListener listener) {
		notifyListeners.add(listener);
	}

	public void removeNotificationListener(NotificationListener listener) {
		notifyListeners.remove(listener);
	}

	public void addStatusChangeListener(StatusChangeListener listener) {
		statListeners.add(listener);
	}

	public void removeStatusChangeListener(StatusChangeListener listener) {
		statListeners.remove(listener);
	}

	public void addProbeListener(ProbeListener listener) {
		probeListeners.add(listener);
	}

	public void removeProbeListener(ProbeListener listener) {
		probeListeners.remove(listener);
	}

	public void notifyStatusChanged(StatusChangeEvent sce) {
		if (sce.getService().getHost().getModel() != model) {
			// ignore events not attached to our current model
			return;
		}

		// resend the event
		Iterator listeners = notifyListeners.iterator();
		while (listeners.hasNext()) {
			((NotificationListener)listeners.next()).notifyStatusChanged(sce);
		}
	}

	public void statusChanged(Service service,
			Status oldStatus, Status newStatus) {
		if (service.getHost().getModel() != model) {
			// ignore services not attached to our current model
			return;
		}

		// construct a StatusChangedEvent to send to all our listeners
		StatusChangeEvent ev =
			new StatusChangeEvent(service,oldStatus,newStatus);
		// send the event
		Iterator listeners = statListeners.iterator();
		while (listeners.hasNext()) {
			((StatusChangeListener)listeners.next()).statusChanged(ev);
		}
	}

	public void probeRun(Service service, Status newStatus) {
		if (service.getHost().getModel() != model) {
			// ignore services not attached to our current model
			return;
		}

		// construct a ProbeEvent to sent to all our listeners
		ProbeEvent ev = new ProbeEvent(service, newStatus);
		// send the event
		Iterator listeners = probeListeners.iterator();
		while (listeners.hasNext()) {
			((ProbeListener)listeners.next()).probeRun(ev);
		}
	}

	private int buildRunQueue() {
		// run through the tree, depth first.
		long totalTimeout = 0;
		long totalPeriod = 0;
		Iterator hostNames = model.getHostNames();
		while (hostNames.hasNext()) {
			Host host = model.getHost(((String)hostNames.next()));
			Iterator serviceNames = host.getServiceNames();
			while (serviceNames.hasNext()) {
				Service service =
					host.getService(((String)serviceNames.next()));
				runQueue.enqueue(service.getProbe());
				totalTimeout += service.getTimeout();
				totalPeriod += service.getPeriod();
			}
		}

		// calculate the number of threads needed to service this queue
		if (runQueue.size() == 0) {
			return 1;
		} else {
			double avgPeriod = (double)totalPeriod / runQueue.size();
			return (int)Math.ceil(totalTimeout/avgPeriod);
		}
	}

	public void rebuildRunQueue() {
		// first, flush the queue and block all the workers
		// lock the queue
		synchronized (runQueue) {
			runQueue.lock();
			// wait for checkouts to fall to zero
			while (checkouts > 0) {
				try {
					runQueue.wait();
				} catch (InterruptedException ign) {};
			}
		}

		// all running threads should be stopped. first, flush the queue
		runQueue.flush();

		// build the queue (don't change the threadcount)
		buildRunQueue();

		// ... and unlock it
		runQueue.unlock();
	}

	Probe getNextProbe() throws InterruptedException {
		synchronized (runQueue) {
			Probe out = (Probe)runQueue.dequeue();
			checkouts++;
			return out;
		}
	}

	void returnProbe(Probe probe) {
		synchronized (runQueue) {
			checkouts--;
			runQueue.enqueue(probe);
		}
	}

	int queueSize() {
		return runQueue.size();
	}

	public void hostChanged(Host host) {
		rebuildRunQueue();
	}

	public void serviceChanged(Service service) {
		rebuildRunQueue();
	}

	public void hostListChanged() {
		rebuildRunQueue();
	}

	public void serviceListChanged(Host host) {
		rebuildRunQueue();
	}
}
