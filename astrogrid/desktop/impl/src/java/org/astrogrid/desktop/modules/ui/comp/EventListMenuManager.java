/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.JMenu;

import ca.odell.glazedlists.EventList;

/** Displays contents of an event list in a <b>drop-down</b> menu.
 * Each item in the eventlist must be a java.awt.Component (and 
 * preferably a JMenuItem)
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 11, 20071:24:22 PM
 */
public class EventListMenuManager extends EventListPopupMenuManager {

	
	/**
	 * create an event list based on the contents of the list 
	 * @param el the list to produce a view for.
	 * @param menu the popup menu to display items in
	 * @param reverse if true, display the items in reverse order.
	 */
	public EventListMenuManager(final EventList el, final JMenu menu, final boolean reverse) {
		super(el,menu,menu.getPopupMenu(),reverse);
	}


}
