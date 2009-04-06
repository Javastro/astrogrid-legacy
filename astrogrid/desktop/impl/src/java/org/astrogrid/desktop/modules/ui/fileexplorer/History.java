package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.EmptyStackException;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

/** The file browsing history 
*/
public class History<T>  {
	
	/** an event fired when current location changes. */
	public static class HistoryEvent<T> extends EventObject {

        /**
		 * @param source
		 */
		public HistoryEvent(final Object source, final T current, final T previous) {
			super(source);
			this.current = current;
            this.previous = previous;
		}
		private final T current;
		private final T previous;
		public T current() {
			return current;
		}
		public T previous() {
		    return previous;
		}
		@Override
        public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((this.current == null) ? 0 : this.current.hashCode());
			return result;
		}
		@Override
        public boolean equals(final Object obj) {
			if (this == obj) {
                return true;
            }
			if (obj == null) {
                return false;
            }
			if (getClass() != obj.getClass()) {
                return false;
            }
			final HistoryEvent other = (HistoryEvent) obj;
			if (this.current == null) {
				if (other.current != null) {
                    return false;
                }
			} else if (!this.current.equals(other.current)) {
                return false;
            }
			return true;
		}

	}
	/** interface to listen to location changes */
	public static interface HistoryListener<T> extends EventListener {
		public void currentChanged(HistoryEvent<T> current);
	}
	
	private final Set<HistoryListener<T>> listeners = new HashSet<HistoryListener<T>>();
	public void addHistoryListener(final HistoryListener<T> l) {
		listeners.add(l);
	}
	public void removeHistoryListener(final HistoryListener<T> l) {
		listeners.remove(l);
	}
	
	protected void fireCurrentChanged(final T current, final T previous) {
		if (listeners.size() > 0) {
			final HistoryEvent<T> he = new HistoryEvent<T>(this,current,previous);
			for (final HistoryListener<T> l : listeners) {
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
	public static class BoundedUniqueEventStack<E> {

		public BoundedUniqueEventStack(final int maxSize) {
			this.sz = maxSize;
		}
		private final int sz;
		public int getMaxSize() {
			return sz;
		}
		private final EventList<E> l = new BasicEventList<E>();
		/** useful for providing views over the data */
		public EventList<E> getEventList() {
			return l;
		}
		
	
		
		public E peek() throws EmptyStackException {
			if (l.isEmpty()) {
				throw new EmptyStackException();
			}
			return l.get(l.size()-1);
		}

		public E pop() throws EmptyStackException {
			if (l.isEmpty()) {
				throw new EmptyStackException();
			}			
			return l.remove(l.size()-1);
		}

		public void push(final E arg0) {
			final int i = l.size();
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
	
	public History(final int sz) {
		previousStack = new BoundedUniqueEventStack<T>(sz);
		nextStack = new BoundedUniqueEventStack<T>(sz);
	}
	
	/** return how many items may be stored in the previous history before 
	 * locations start to get dropped off.
	 */
	public int getMaxHistorySize() {
		return previousStack.sz;
	}
	
	/** default number of items to keep in the previous and the next stack. */
	public static final int DEFAULT_HISTORY_LENGTH = 30;
	private final  History.BoundedUniqueEventStack<T> previousStack;
	private final History.BoundedUniqueEventStack<T> nextStack;
	
	/** acceess the event list used to store the previous locations
	 * can be used for event listening, and also for listing options.
	 * last item of this list will be the current location.
	 * Shouldn't be used to manipulate the history.
	 */
	public EventList<T> getPreviousList() {
		return previousStack.getEventList();
	}
	/** access the event list used to store the next locations.
	 * can be used for event listening, and also for displaying values.
	 * Shooudn't be used to manipulate the history.
	 */
	public EventList<T> getNextList() {	
		return nextStack.getEventList();
	}
	/** returns true if there's a previousl location in the hisotry */
	public final boolean hasPrevious() {
	    return previousStack.getEventList().size() > 1;
	}
	
	/** take a peek at the previous entry */
	public T peekPrevious() {
	    final EventList<T> list = previousStack.getEventList();
	    return list.get(list.size()-2);
	}
	
	/** returns true if there's a next location in the history */
	public boolean hasNext() {
	    return !nextStack.isEmpty();
	}
	
	/** move to the prevous location in history */
	public T movePrevious() throws IllegalStateException{
		if (logger.isDebugEnabled()) {
			logger.debug("Previous:" + previousStack.getEventList() + "\n" + nextStack.getEventList());
		}
		if (! hasPrevious()) {
			throw new IllegalStateException("No previous in history");
		}
		final T previous = previousStack.pop();
		nextStack.push(previous);		
		final T current =  previousStack.peek();
		fireCurrentChanged(current,previous);
		return current;
	}
	
	/** move to the next item in the list */
	public T moveNext() throws IllegalStateException{
		if (logger.isDebugEnabled()) {
			logger.debug("Next:" + previousStack.getEventList() + "\n" + nextStack.getEventList());
		}			
		if (! hasNext()) {
			throw new IllegalStateException("No next in history");
		}
		final T previous = previousStack.peek();
		final T current = nextStack.pop();
		previousStack.push(current);
		fireCurrentChanged(current,previous);
		return current; 
	}	
	
	public T current() {		
		if (previousStack.isEmpty()) {
			return null;
		}
		return previousStack.peek();
	}
	
	public void reset() {
	    nextStack.clear();
	    previousStack.clear();
	}
	
	public void move(final T location) {
		if (location.equals(current())) {
			return; // ignore
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Move:" + previousStack.getEventList() + "\n" + nextStack.getEventList());
		}	
		final T previous;
		if (previousStack.isEmpty()) {
		    previous = null;
		} else {
		    previous = previousStack.peek();
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
		fireCurrentChanged(location,previous);
	}
    /** replace the current location with a new location, without firing any event updates
     * use to resolve a location to a more concrete equivalent, replace a child with it's parent, etc.
     * @param location
     */
    public void replace(final T location) {
        if (location.equals(current())) {
            return; // ignore
        }
        previousStack.pop();
        previousStack.push(location);
    }
	
}