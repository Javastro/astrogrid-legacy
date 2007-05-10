/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.datatransfer.Transferable;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.SelfEnablingMenu;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

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
	
	/** subclasses should populate this in the constructor.
	 contents of this class must all be AbstractActivity */
	private EventList _children;

	
	protected EventList getChildren() {
		if (_children == null) {
			_children =   createEventList();
		} 
		return _children;
	}

	/** implement this to specifiy which list implementaiton to use */
	protected abstract EventList createEventList();

	public void addTo(JTaskPaneGroup grp) {
		if (! loaded) {
			loadChildren();
			loaded =true;
		}
		new EventListTaskPaneGroupManager(grp,getChildren());
	}

	public void addTo(JMenu menu) {
		if (! loaded) {
			loadChildren();
			loaded = true;
		}		
		JMenu m = new SelfEnablingMenu(this);
		new EventListMenuManager(createNewMenuItemList(),m,false);		
		menu.add(m);
	}
	public void addTo(JPopupMenu menu) {
		if (! loaded) {
			loadChildren();
			loaded = true;
		}			
		JMenu m = new SelfEnablingMenu(this);
		new EventListMenuManager(createNewMenuItemList(),m,false);	
		menu.add(m);		
	}

	private EventList createNewMenuItemList() {
		return new FunctionList(getChildren(), new FunctionList.Function() {
			public Object evaluate(Object arg0) {
				AbstractActivity a = (AbstractActivity)arg0;
				return a.createMenuItem();
			}
		});
	}
	
	/** subclasses should use this method to populate tyhe 'children' list */
	protected abstract void loadChildren();
	private boolean loaded = false;
	
	public void noneSelected() {
		latest= null;
		for (Iterator i = getChildren().iterator(); i.hasNext();) {
			Activity a = (Activity) i.next();
			a.noneSelected();
		}
	}

	public void selected(Transferable t) {
		latest = t;
		for (Iterator i = getChildren().iterator(); i.hasNext();) {
			Activity a = (Activity) i.next();
			a.selected(t);
		}		
	}
	
	private Transferable latest;
	
	private class EventListTaskPaneGroupManager implements ListEventListener {
		private final JTaskPaneGroup pane;
		private final EventList list;
		public void listChanged(ListEvent arg0) {
			while (arg0.hasNext()) {
				arg0.next();
				int i = arg0.getIndex();
				switch(arg0.getType()) {
				case ListEvent.INSERT:
					AbstractActivity a = (AbstractActivity)list.get(i);
					a.addTo(pane);
					// need to adjust according to selection - causes the action to be enabled /disabled
					// which enables / disables related menu items in turn.
					if (latest == null) {
						a.noneSelected();
					} else {
						a.selected(latest);
					}
					break;
				case ListEvent.DELETE:
					pane.remove(i);
				case ListEvent.UPDATE: // ever happens?
					break; 
				}
			}
			pane.revalidate();
			pane.repaint();
		}
		public EventListTaskPaneGroupManager(final JTaskPaneGroup pane, final EventList list) {
			super();
			this.pane = pane;
			this.list = list;
			list.addListEventListener(this);
			for (Iterator i = list.iterator(); i.hasNext();) {
				AbstractActivity a = (AbstractActivity) i.next();
				a.addTo(pane);
			}
		}
	}


}
