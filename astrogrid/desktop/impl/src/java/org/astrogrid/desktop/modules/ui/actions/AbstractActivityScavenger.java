/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.datatransfer.Transferable;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.TitledBorder;

import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import com.l2fprod.common.swing.JTaskPaneGroup;

/** base class for components that manage a dynamic list of activities.
 * To the outside, appears as a single activity - this class takes care of delegating
 * to the components. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 20071:41:47 AM
 */
public  abstract class AbstractActivityScavenger extends AbstractActivity{

	
	
	public AbstractActivityScavenger() {
		super();
	}

	public AbstractActivityScavenger(String name, Icon icon) {
		super(name, icon);
	}

	public AbstractActivityScavenger(String name) {
		super(name);
	}

	public void addTo(JTaskPaneGroup grp) {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder((String)getValue(Action.NAME)));
		grp.add(p);
	}

	public void addTo(JMenu menu) {
		JMenu m = new JMenu(this);
		new EventListMenuManager(children,m,false);		
		menu.add(m);
	}

	public void addTo(JPopupMenu menu) {
		JMenu m = new JMenu(this);
		new EventListMenuManager(children,m,false);
		menu.add(m);		
	}

	private EventList children = new BasicEventList();

	public void noneSelected() {
		for (Iterator i = children.iterator(); i.hasNext();) {
			Activity a = (Activity) i.next();
			a.noneSelected();
		}
	}

	public void selected(Transferable t) {
		for (Iterator i = children.iterator(); i.hasNext();) {
			Activity a = (Activity) i.next();
			a.selected(t);
		}		
	}


}
