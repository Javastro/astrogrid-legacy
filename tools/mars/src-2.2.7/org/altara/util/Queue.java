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

import java.io.*;
import java.util.*;

/** Queue is a wrapper around a LinkedList providing a queue-oriented
	view.
*/

public class Queue {

	LinkedList list;
	boolean isLocked;

	public Queue() {
		list = new LinkedList();
	}

	/** Lock the queue. A locked queue may not be dequeued from - i.e. it
		will block as if it is empty even when it is full. Locking has
		no effect on nb_dequeue().
	*/

	public synchronized void lock() {
		this.isLocked = true;
	}

	/** Unlock the queue. Reverses the effect of a previous lock().
	*/

	public synchronized void unlock() {
		this.isLocked = false;
		notify();
	}

	/** Flush the queue. Removes all elements from the queue.
	*/

	public synchronized void flush() {
		list.clear();
	}

	/** Enqueue an object. This will place the object into the queue for
		later in-order dequeueing.

		@param obj the object to enqueue.
	*/

	public synchronized void enqueue(Object obj) {
		list.addFirst(obj);
		notify();
	}

	/** Retrieve the next object from the queue. This method will 
		block if the queue is empty.

		@throws InterruptedException if the method blocked on an empty queue
									 and was subsequently interrupted.
		@return the next object from the queue.
	*/

	public synchronized Object dequeue() throws InterruptedException {
		while (list.size() == 0 || isLocked)
			wait();
		return nb_dequeue();
	}

	/** Retrieve the next object from the queue. This method will return null
		if the queue is empty.
	
		@return the next object from the queue, or null if none exists.
	*/

	public synchronized Object nb_dequeue() {
		if (list.size() == 0) return null;
		return list.removeLast();
	}

	/** Return this queue's size. 
		
		@return this queue's size.
	*/

	public int size() {
		return list.size();
	}
}
