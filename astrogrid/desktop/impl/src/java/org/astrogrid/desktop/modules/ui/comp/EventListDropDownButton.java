/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.Icon;
import javax.swing.JButton;

import ca.odell.glazedlists.EventList;


/** A Drop Down button populated by an EventList of operations.
 * Each item in the eventlist must be a java.awt.Component
 * (and preferably a JMenuItem)
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 2, 200712:43:12 AM
 */
public class EventListDropDownButton extends DropDownButton {

	public EventListDropDownButton(final JButton main,final EventList el, final boolean reverse) {
		super(main);
		setRunFirstItem(true);	
		new EventListPopupMenuManager(el,this.getArrowButton(),getPopupMenu(),reverse);

	}
	
	
	   public EventListDropDownButton(final String title, final Icon icon, final EventList el, final boolean reverse) {
	        super(title,icon);
	        setRunFirstItem(true);  
	        new EventListPopupMenuManager(el,this.getArrowButton(),getPopupMenu(),reverse);

	    }


}
