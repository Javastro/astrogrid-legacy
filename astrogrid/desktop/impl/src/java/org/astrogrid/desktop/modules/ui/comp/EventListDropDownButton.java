/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import org.astrogrid.desktop.icons.IconHelper;

import ca.odell.glazedlists.EventList;


/** A Drop Down button . and providing access to 
 * an eventlist of operations. Each item in the eventlist must be a java.awt.Component
 * (and preferably a JMenuItem)
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 2, 200712:43:12 AM
 */
public class EventListDropDownButton extends DropDownButton {
	/**
	 * cnstruct a drop-down button list, 
	 * @param iconName the icon for the button
	 * @param el the event list to show items for
	 * @param reverse whether to display items in reverse order or not.
	 */
	public EventListDropDownButton(String iconName,EventList el, boolean reverse) {
		super(IconHelper.loadIcon(iconName));
		setRunFirstItem(true);	
		new EventListPopupMenuManager(el,this,getPopupMenu(),reverse);

	}


}
