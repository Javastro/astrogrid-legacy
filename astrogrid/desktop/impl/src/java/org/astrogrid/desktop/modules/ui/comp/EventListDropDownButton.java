/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.Icon;
import javax.swing.JButton;

import ca.odell.glazedlists.EventList;


/** A Drop Down button . and providing access to 
 * an eventlist of operations. Each item in the eventlist must be a java.awt.Component
 * (and preferably a JMenuItem)
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 2, 200712:43:12 AM
 */
public class EventListDropDownButton extends DropDownButton {

	public EventListDropDownButton(JButton main,EventList el, boolean reverse) {
		super(main);
		setRunFirstItem(true);	
		new EventListPopupMenuManager(el,this.getArrowButton(),getPopupMenu(),reverse);

	}
	
	
	   public EventListDropDownButton(String title, Icon icon, EventList el, boolean reverse) {
	        super(title,icon);
	        setRunFirstItem(true);  
	        new EventListPopupMenuManager(el,this.getArrowButton(),getPopupMenu(),reverse);

	    }


}
