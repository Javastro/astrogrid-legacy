/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.EventListPopupMenuManager;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.l2fprod.common.swing.JTaskPaneGroup;

/** base class for components that manage a dynamic list of activities.
 *  - a 'scavenger' (inspired by taverna terminology)
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

	public AbstractActivityScavenger(final String name, final Icon icon) {
		super(name, icon);
	}

	public AbstractActivityScavenger(final String name) {
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

	@Override
    public void addTo(final JTaskPaneGroup grp) {
		if (! loaded) {
			loadChildren();
			loaded =true;
		}
		new EventListTaskPaneGroupManager(grp,getChildren());
	}

	@Override
    public void addTo(final JTaskPaneGroup grp,final int pos) {
		if (! loaded) {
			loadChildren();
			loaded =true;
		}
		new EventListTaskPaneGroupManager(grp,getChildren(),pos);
	}
	
	@Override
    public void addTo(final JMenu menu) {
		if (! loaded) {
			loadChildren();
			loaded = true;
		}
        new EventListMenuManager(createNewMenuItemList(false),menu,false); 		
	}
	
	@Override
    public void addTo(final JPopupMenu menu) {
		if (! loaded) {
			loadChildren();
			loaded = true;
		}			
		new EventListPopupMenuManager(createNewMenuItemList(true),null,menu,false);	
	}

	protected EventList<JMenuItem> createNewMenuItemList(final boolean hiding) {
		return new FunctionList(getChildren(), new FunctionList.Function() {
			public Object evaluate(final Object arg0) {
				final AbstractActivity a = (AbstractActivity)arg0;
				if (hiding) {
				    return a.createHidingMenuItem();
				} else {
				    return a.createMenuItem();
				}
			}
		});
	}
	

	private boolean loaded = false;
	
	@Override
    public void noneSelected() {
		latest= null;
		for (final Iterator i = getChildren().iterator(); i.hasNext();) {
			final Activity a = (Activity) i.next();
			a.noneSelected();
		}
	}

	@Override
    public void selected(final Transferable t) {
		latest = t;
		for (final Iterator i = getChildren().iterator(); i.hasNext();) {
			final Activity a = (Activity) i.next();
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
			public EventListTaskPaneGroupManager(final JTaskPaneGroup pane, final EventList list,final int startPos) {
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
			for (final Iterator i = list.iterator(); i.hasNext();) {
				final AbstractActivity a = (AbstractActivity) i.next();
				a.addTo(pane,startIndex++);
			}
		}
	
		public void listChanged(final ListEvent arg0) {
			// find pstarting position
			final int startPos = findStartIndex();
			while (arg0.hasNext()) {
				arg0.next();
				final int i = arg0.getIndex();
				switch(arg0.getType()) {
				case ListEvent.INSERT:			
					final AbstractActivity a = (AbstractActivity)list.get(i);
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
			final Component[] contents = pane.getContentPane().getComponents();
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
