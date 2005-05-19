/* Altara Utility Classes
   Copyright (c) 2001,2002 Brian H. Trammell

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.
	
	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.
	
	You should have received a copy of the GNU Lesser General Public
	License along with this library; if not, it is available at
	http://www.gnu.org/copyleft/lesser.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place, Suite 330,
	Boston, MA  02111-1307  USA
*/

package org.altara.util;

public abstract class Worker extends Thread {

	private boolean running;
	private boolean awake;

	protected Worker() {
		super();
	}

	protected Worker(String name) {
		super(name);
	}

	// thread control methods

	public void start() {
		this.running = true;
		this.awake = true;
		super.start();
	}

	public void startAsleep() {
		this.running = true;
		this.awake = false;
		super.start();
	}

	public synchronized void lull() {
		this.awake = false;
	}

	public synchronized void wake() {
		this.awake = true;
		this.notify();
	}
	
	public synchronized void kill() {
		this.awake = true;
		this.running = false;
		this.interrupt();
	}

	// thread body

	public final void run() {
		while (running) {
			// Check to see if I should sleep
			if (!awake) {
				synchronized(this) {
					try {
						this.wait();
					} catch (InterruptedException ex) {
						// We must skip over our action, since
						// this interrupt may be a kill() interrupt.
						continue;
					}
				}
			}
		
			// this can block too
			try {
				work();
			} catch (Exception ex) {
				workException(ex);
			}
		}
		workExit();
	}

	protected abstract void work() throws Exception;

	protected void workException(Exception ex) {
		// do nothing by default
	}

	protected void workExit() {
		// do nothing by default
	}
}
