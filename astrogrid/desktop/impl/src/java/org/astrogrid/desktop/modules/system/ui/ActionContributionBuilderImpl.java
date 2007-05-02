/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.actions.Activity;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 26, 20073:18:32 PM
 */
public class ActionContributionBuilderImpl implements ActionContributionBuilder {

	private static class MyTaskPaneGroup extends JTaskPaneGroup{
		protected Activity[] activities = new Activity[0];
		
		public void setHelpId(String s) {
			CSH.setHelpIDString(this,s);
		}
		
		public void setIconName(String s) {
			setIcon(IconHelper.loadIcon(s));
		}

		/** subclasses should call this to cause component to redraw after 
		 * alterations have been completed.
		 *
		 */
		protected void done() {
			revalidate();
			repaint();
		}	
	}
	
	/** listens to children, and adjusts it's own enable / disable status accordingly */
	public static class SelfEnablingMenu extends JMenu implements PropertyChangeListener {

		public SelfEnablingMenu() {
			super();
		}

		public SelfEnablingMenu(Action a) {
			super(a);
		}

		public SelfEnablingMenu(String s, boolean b) {
			super(s, b);
		}

		public SelfEnablingMenu(String s) {
			super(s);
		}

		public void propertyChange(PropertyChangeEvent evt) {
			// someone's visibility has changed. loop through all children, and see 
			// if anything is visible.
			Component[] components = getMenuComponents();
			for (int i = 0; i < components.length; i++) {
				if (components[i].isEnabled()) {
					setEnabled(true);
					return;
				}
			}
			setEnabled(false);
		}
		
		public JMenuItem add(JMenuItem menuItem) {
			 JMenuItem item = super.add(menuItem);
			 item.addPropertyChangeListener("enabled",this);
			 return item;
		}
		public JMenuItem add(Action a) {
			 JMenuItem item = super.add(a);
			 item.addPropertyChangeListener("enabled",this);
			 return item;
		}
	}
	
	private final IterableObjectBuilder activityBuilder;

	public Activity[] buildActions(UIComponent parent, JPopupMenu popup, JTaskPane actionsPanel, JMenu actions) {
		Map actsMap = new ListOrderedMap();
		
		CSH.setHelpIDString(actionsPanel, "voexplorer.actions");
	    // create the groups for this pane.
		actsMap.put(Activity.USE_SECTION,new MyTaskPaneGroup() {{
			setTitle("Utilize");
			setIconName("run16.png");
			setHelpId("resourceActions.invoke");
			setSpecial(true);
		}});
		actsMap.put(Activity.INFO_SECTION, new MyTaskPaneGroup() {{
			setTitle("Details");
			setIconName("info16.png");
			setHelpId("resourceActions.info");
			setSpecial(true);
		}});
		actsMap.put(Activity.EXPORT_SECTION, new MyTaskPaneGroup() {{//@todo move additional stuff 'export' does into activity.
			setTitle("Export");
			setExpanded(false);
		}});
		// compose these tasks.
		for (Iterator i = actsMap.values().iterator(); i.hasNext();) {
			MyTaskPaneGroup t = (MyTaskPaneGroup) i.next();
			actionsPanel.add(t);
		}
		// assemble the menus.
		JMenu popupNew = new SelfEnablingMenu("New");
		JMenu actionsNew = new SelfEnablingMenu("New");
		// no new menus fro VOExplorer - bit of a hack, while we're prototyping.
		if (! (parent instanceof VOExplorerImpl)) {
			popup.add(popupNew);
			popup.addSeparator();
			actions.add(actionsNew);
			actions.addSeparator();
		}
		JMenu popupInfo = new SelfEnablingMenu("About") ;
		JMenu actionsInfo = new SelfEnablingMenu("About");
		JMenu popupExport = new SelfEnablingMenu("Export");
		JMenu actionsExport = new SelfEnablingMenu("Export");
		JMenu popupAdvanced = new SelfEnablingMenu("Advanced");
		JMenu actionsAdvanced =new SelfEnablingMenu("Advanced");
		// now build the activities.
		Activity[] activities = (Activity[]) IteratorUtils.toArray(activityBuilder.creationIterator(),Activity.class);
	    for (int i = 0; i < activities.length; i++) {
	    	final Activity a = activities[i];
			a.setUIParent(parent);
			final String sectname = a.getSection();
			MyTaskPaneGroup t = (MyTaskPaneGroup)actsMap.get(sectname);
			if (t != null) {
				a.addTo(t);
			}
			// assemble the menus.
			if (sectname.equals(Activity.ADVANCED_SECTION)) {
				a.addTo(popupAdvanced);
				a.addTo(actionsAdvanced);
			} else if (sectname.equals(Activity.NEW_SECTION)) {
				a.addTo(popupNew);
				a.addTo(actionsNew);
			} else if (sectname.equals(Activity.INFO_SECTION)) {
				a.addTo(popupInfo);
				a.addTo(actionsInfo);
			} else if (sectname.equals(Activity.EXPORT_SECTION)) {
				a.addTo(actionsExport);
				a.addTo(popupExport);
			} else {
				a.addTo(popup);
				a.addTo(actions);
			}
		}
	    // add hte advanced submenu.
	    popup.addSeparator();
	    popup.add(popupExport);
	    popup.add(popupAdvanced);
	    popup.add(popupInfo);
	    actions.addSeparator();
	    actions.add(actionsExport);
	    actions.add(actionsAdvanced);
	    actions.add(actionsInfo);
	    
	    actionsPanel.revalidate();
	    actionsPanel.repaint();	
	    
	    return activities;
	}

	public ActionContributionBuilderImpl(final IterableObjectBuilder activityBuilder) {
		super();
		this.activityBuilder = activityBuilder;
	}
}
