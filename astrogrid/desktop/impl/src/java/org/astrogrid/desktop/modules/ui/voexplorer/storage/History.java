package org.astrogrid.desktop.modules.ui.voexplorer.storage;

import java.util.EmptyStackException;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

/** models the browsing history */
public class History  {
	
	/** an event fired when current location changes. */
	public static class HistoryEvent extends EventObject {

		/**
		 * @param source
		 */
		public HistoryEvent(Object source, Object current) {
			super(source);
			this.current = current;
		}
		private final Object current;
		public Object current() {
			return current;
		}
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((this.current == null) ? 0 : this.current.hashCode());
			return result;
		}
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final HistoryEvent other = (HistoryEvent) obj;
			if (this.current == null) {
				if (other.current != null)
					return false;
			} else if (!this.current.equals(other.current))
				return false;
			return true;
		}

	}
	/** interface to listen to location changes */
	public static interface HistoryListener extends EventListener {
		public void currentChanged(HistoryEvent current);
	}
	
	private final Set listeners = new HashSet();
	public void addHistoryListener(HistoryListener l) {
		listeners.add(l);
	}
	public void removeHistoryListener(HistoryListener l) {
		listeners.remove(l);
	}
	
	protected void fireCurrentChanged() {
		if (listeners.size() > 0) {
			HistoryEvent he = new HistoryEvent(this,current());
			for (Iterator i = listeners.iterator(); i.hasNext();) {
				HistoryListener l = (HistoryListener) i.next();
				l.currentChanged(he);
			}
		}
	}
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(History.class);

	/** a custom stack - has a fixed size, as which point oldest
	 * items are deleted. Furthermore, push only
	 * inserts unique items. If the item already exists inthe list, the push
	 * happens, but the earlier item is removed.
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Mar 29, 200712:09:01 AM
	 */
	public static class BoundedUniqueEventStack {

		public BoundedUniqueEventStack(int maxSize) {
			this.sz = maxSize;
		}
		private final int sz;
		public int getMaxSize() {
			return sz;
		}
		private final EventList l = new BasicEventList();
		/** useful for providing views over the data */
		public EventList getEventList() {
			return l;
		}
		
		public Object peek() throws EmptyStackException {
			if (l.isEmpty()) {
				throw new EmptyStackException();
			}
			return l.get(l.size()-1);
		}

		public Object pop() throws EmptyStackException {
			if (l.isEmpty()) {
				throw new EmptyStackException();
			}			
			return l.remove(l.size()-1);
		}

		public void push(Object arg0) {
			int i = l.size();
			if (i > 0 && l.get(i-1).equals(arg0)) {
				// do nothing. already at the top of the stack.
				return;
			} else if (l.contains(arg0)) {
				l.remove(arg0);
			} else if (i >= sz) {
				l.remove(0);
			}
			l.add(arg0);
		}

		public void clear() {
			l.clear();
		}

		public boolean isEmpty() {
			return l.isEmpty();
		}
	}
	
	public History() {
		this(DEFAULT_HISTORY_LENGTH);
	}
	
	public History(int sz) {
		previousStack = new BoundedUniqueEventStack(sz);
		nextStack = new BoundedUniqueEventStack(sz);
	}
	
	/** return how many items may be stored in the previous history before 
	 * locations start to get dropped off.
	 * @return
	 */
	public int getMaxHistorySize() {
		return previousStack.sz;
	}
	
	/** default number of items to keep in the previous and the next stack. */
	public static final int DEFAULT_HISTORY_LENGTH = 30;
	private final  History.BoundedUniqueEventStack previousStack;
	private final History.BoundedUniqueEventStack nextStack;
	
	/** acceess the event list used to store the previous locations
	 * can be used for event listening, and also for listing options.
	 * last item of this list will be the current location.
	 * Shouldn't be used to manipulate the history.
	 * @return
	 */
	public EventList getPreviousList() {
		return previousStack.getEventList();
	}
	/** access the event list used to store the next locations.
	 * can be used for event listening, and also for displaying values.
	 * Shooudn't be used to manipulate the history.
	 * @return
	 */
	public EventList getNextList() {	
		return nextStack.getEventList();
	}
	/** move to the prevous location in history */
	public Object movePrevious() throws IllegalStateException{
		if (logger.isDebugEnabled()) {
			logger.debug("Previous:" + previousStack.getEventList() + "\n" + nextStack.getEventList());
		}
		if (previousStack.getEventList().size() < 2) {
			throw new IllegalStateException("No previous in history");
		}
		nextStack.push(previousStack.pop());
		fireCurrentChanged();
		return previousStack.peek();
	}
	
	/** move to the next item in the list */
	public Object moveNext() throws IllegalStateException{
		if (logger.isDebugEnabled()) {
			logger.debug("Next:" + previousStack.getEventList() + "\n" + nextStack.getEventList());
		}			
		if (nextStack.isEmpty()) {
			throw new IllegalStateException("No next in history");
		}
		previousStack.push(nextStack.pop());
		fireCurrentChanged();
		return previousStack.peek(); 
	}	
	
	public Object current() {		
		if (previousStack.isEmpty()) {
			return null;
		}
		return previousStack.peek();
	}
	
	public void move(Object location) {
		if (location.equals(current())) {
			return; // ignore
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Move:" + previousStack.getEventList() + "\n" + nextStack.getEventList());
		}	
	
		if (previousStack.getEventList().indexOf(location) !=-1) {
			// naieve implementaiton, but good enough for now. maximum of 30 iterations.
			while(! current().equals(location)) {
				nextStack.push(previousStack.pop());
			}
		} else if (nextStack.getEventList().indexOf(location) != -1) {
			// naieve implementaiton, but good enough for now. maximum of 30 iterations.
			while(! current().equals(location)) {
				previousStack.push(nextStack.pop());
			}
		} else { //totally new
			nextStack.clear();
			previousStack.push(location);
		}
		fireCurrentChanged();
	}
	
}