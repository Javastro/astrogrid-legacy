/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.EventListPopupMenuManager;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.l2fprod.common.swing.JTaskPaneGroup;

/** base class for components that manage a dynamic list of activities - a 'scavenger' (inspired by taverna terminology)
 * To the outside, appears as a single activity - this class takes care of delegating
 * to the list of activities it manages.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 20071:41:47 AM
 * @TEST unit test
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
	
	private EventList _children;

	/** access the list of child activities */
	protected EventList getChildren() {
		if (_children == null) {
			_children =   createEventList();
		} 
		return _children;
	}

	/** implement this to control which list implementaiton to use */
	protected abstract EventList createEventList();
	
	/**implement this to populate the 'children' list, accessed via {@link #getChildren()} 
	 * 
	 * this method will be called to populate the chldren list lazily.*/
    protected abstract void loadChildren();

	public void addTo(JTaskPaneGroup grp) {
		if (! loaded) {
			loadChildren();
			loaded =true;
		}
		new EventListTaskPaneGroupManager(grp,getChildren());
	}

	public void addTo(JTaskPaneGroup grp,int pos) {
		if (! loaded) {
			loadChildren();
			loaded =true;
		}
		new EventListTaskPaneGroupManager(grp,getChildren(),pos);
	}
	
	public void addTo(JMenu menu) {
		if (! loaded) {
			loadChildren();
			loaded = true;
		}
        new EventListMenuManager(createNewMenuItemList(false),menu,false); 		
	}
	
	public void addTo(JPopupMenu menu) {
		if (! loaded) {
			loadChildren();
			loaded = true;
		}			
		new EventListPopupMenuManager(createNewMenuItemList(true),null,menu,false);	
	}

	protected EventList createNewMenuItemList(final boolean hiding) {
		return new FunctionList(getChildren(), new FunctionList.Function() {
			public Object evaluate(Object arg0) {
				AbstractActivity a = (AbstractActivity)arg0;
				if (hiding) {
				    return a.createHidingMenuItem();
				} else {
				    return a.createMenuItem();
				}
			}
		});
	}
	

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
	
	/** manages the display of a list of Activities in a taskPane */
	private class EventListTaskPaneGroupManager implements ListEventListener {


		private final JTaskPaneGroup pane;
		private final EventList list;
		private final Component markerComponent;
		public EventListTaskPaneGroupManager(final JTaskPaneGroup pane, final EventList list) {
			this(pane,list,-1);
		}
			public EventListTaskPaneGroupManager(final JTaskPaneGroup pane, final EventList list,int startPos) {
			super();
			this.pane = pane;
			this.list = list;
			logger.debug(pane);
			logger.debug("startpos: "+startPos);
			// add an invisible item..
			markerComponent = new Marker();
			if (startPos != -1) {
				pane.add(markerComponent,startPos);
			} else {
				pane.add(markerComponent);
			}
			list.addListEventListener(this);
			int startIndex = findStartIndex();
			logger.debug("startindex: " + startIndex);
			for (Iterator i = list.iterator(); i.hasNext();) {
				AbstractActivity a = (AbstractActivity) i.next();
				a.addTo(pane,startIndex++);
			}
		}
	
		public void listChanged(ListEvent arg0) {
			// find pstarting position
			int startPos = findStartIndex();
			while (arg0.hasNext()) {
				arg0.next();
				int i = arg0.getIndex();
				switch(arg0.getType()) {
				case ListEvent.INSERT:			
					AbstractActivity a = (AbstractActivity)list.get(i);
					a.addTo(pane,startPos + i);
					// need to adjust according to selection - causes the action to be enabled /disabled
					// which enables / disables related menu items in turn.
					if (latest == null) {
						a.noneSelected();
					} else {
						a.selected(latest);
					}
					break;
				case ListEvent.DELETE:
					pane.remove(startPos + i);
					break;
				case ListEvent.UPDATE: // ever happens?
					break; 
				}
			}
			pane.revalidate();
			pane.repaint();
		}
		private int findStartIndex() {
			//JTaskPane overrides addImpl to add children to it's 'contentPane' - so it's there we need to look.
			Component[] contents = pane.getContentPane().getComponents();
			for (int i = 0; i < contents.length; i++) {
				if (markerComponent.equals(contents[i])) {
					return i + 1; // real components start at position of the marker + 1
				}
			}
			throw new RuntimeException("Programing error, cannot find marker component");
		}
		
				/** an invisible component, that we use to keep track of the insertion position */
			private class Marker extends Component {
				public Marker() {
					setEnabled(false);
					setVisible(false);
				}
			}
	}

}
