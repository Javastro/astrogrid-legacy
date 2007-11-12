/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Component;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

/** Utility class that displays contents of an event list in a popup menu.
 * Each item in the eventlist must be a java.awt.Component (and 
 * preferably a JMenuItem)
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 11, 20071:24:22 PM
 */
public class EventListPopupMenuManager implements ListEventListener {
	private final EventList el;
	private final JPopupMenu popup;
	private final boolean reverse;
	private final AbstractButton triggerButton;
    private final int startingSize;


	
	/**
	 * 
	 * @param el the list to produce a view for.
	 * @param triggerButton  the button that triggers this menu (will handle
	 * enabled / disabled as menu shrinks and grows) - may be null
	 * @param popup the popup menu to display items in
	 * @param reverse if true, display the items in reverse order. If false,
	 * additional components may be added to the menu before and after the 
	 * creation of the menu manager. If true, no other components should be added.
	 */
	public EventListPopupMenuManager(final EventList el, final AbstractButton triggerButton, final JPopupMenu popup, boolean reverse) {
		super();
		this.el = el;
		this.popup = popup;
		this.reverse = reverse;
		this.triggerButton = triggerButton;
		startingSize = popup.getComponentCount(); 
		if (triggerButton != null) {
			triggerButton.setEnabled(startingSize + el.size() > 0);
		}
		int ix = startingSize;
		for (Iterator i = el.iterator(); i.hasNext(); ) {
			Component o = (Component)i.next();
			popup.insert(o,mbReverseInsert(ix++));
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
					popup.insert(o,mbReverseInsert(i));
					if (triggerButton != null && popup.getComponentCount() ==1) { // first insert
						triggerButton.setEnabled(true);
					}
					break;
				case ListEvent.UPDATE:
					o = (Component)el.get(i);
					popup.remove(mbReverse(i));
					popup.insert(o,mbReverseInsert(i));
					break;
				case ListEvent.DELETE:
					popup.remove(mbReverse(i));
					if (triggerButton != null && popup.getComponentCount() ==0) {
						triggerButton.setEnabled(false);
					}
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
			return i + startingSize;
		} else {
			int sz = popup.getComponentCount();
			return (sz-1)-i;
		}
	}
	
	/** as insert adds before an item, the calculation is slightly different */
	private int mbReverseInsert(int i) {
		if (! reverse) {
			return i + startingSize;
		} else {
			int sz = popup.getComponentCount();
			return sz -i;
		}
	}

}
