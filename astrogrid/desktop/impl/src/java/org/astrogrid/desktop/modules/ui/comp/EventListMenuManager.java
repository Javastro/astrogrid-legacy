/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Component;
import java.util.Iterator;

import javax.swing.JMenu;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

/** Utility class that displays contents of an event list in a popup menu.
 * Each item in the eventlist must be a java.awt.Component (and 
 * preferably a JMenuItem)
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 11, 20071:24:22 PM
 */
public class EventListMenuManager implements ListEventListener {
	private final EventList el;
	private final JMenu menu;
	private final boolean reverse;
	
	/**
	 * 
	 * @param el the list to produce a view for.
	 * @param menu the popup menu to display items in
	 * @param reverse if true, display the items in reverse order.
	 */
	public EventListMenuManager(final EventList el, final JMenu menu, boolean reverse) {
		super();
		this.el = el;
		this.menu = menu;
		this.reverse = reverse;
		int ix = 0;
		for (Iterator i = el.iterator(); i.hasNext(); ) {
			Component o = (Component)i.next();
			menu.add(o,mbReverseInsert(ix++));
		}		
		el.addListEventListener(this);
	}
	

	public void listChanged(ListEvent e) {
		while (e.hasNext()) {
			e.next();
			int i = e.getIndex();
			Component o;
			switch(e.getType()) {
				case ListEvent.INSERT:
					o = (Component)el.get(i);
					menu.add(o,mbReverseInsert(i));

					break;
				case ListEvent.UPDATE:
					o = (Component)el.get(i);
					menu.remove(mbReverse(i));
					menu.add(o,mbReverseInsert(i));
					break;
				case ListEvent.DELETE:
					menu.remove(mbReverse(i));
					break;
			}
		}		
	}

	/** may reverse / invert the index
	 * @param i
	 * @return
	 */
	private int mbReverse(int i) {
		if (! reverse) {
			return i;
		} else {
			int sz = menu.getComponentCount();
			return (sz-1)-i;
		}
	}
	
	/** as insert adds before an item, the calculation is slightly different */
	private int mbReverseInsert(int i) {
		if (! reverse) {
			return i;
		} else {
			int sz = menu.getComponentCount();
			return sz -i;
		}
	}

}
