/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.datatransfer.Transferable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.actions.Activity;
import org.astrogrid.desktop.modules.ui.comp.SelfEnablingMenu;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 26, 20073:18:32 PM
 */
public class ActivityFactoryImpl implements ActivityFactory {
	/** task pane with a few additional functions */
	private static class MyTaskPaneGroup extends JTaskPaneGroup{

		public void setHelpId(String s) {
			CSH.setHelpIDString(this,s);
		}
		
		public void setIconName(String s) {
			setIcon(IconHelper.loadIcon(s));
		}

	}
	
	private final IterableObjectBuilder activityBuilder;

	protected Activity[] create(UIComponent parent, JPopupMenu popup, JTaskPane actionsPanel, JMenu actions) {
		Map actsMap = new ListOrderedMap();
		
		CSH.setHelpIDString(actionsPanel, "voexplorer.actions");
	    final MyTaskPaneGroup usePane = new MyTaskPaneGroup() {{
					setTitle("Actions");
					setIconName("run16.png");
					setHelpId("resourceActions.invoke");
					setSpecial(true);
				}};
		// create the groups for this pane.
		actsMap.put(Activity.USE_SECTION,usePane);
		/*
		actsMap.put(Activity.PLASTIC_SECTION,new MyTaskPaneGroup() {{
			setTitle("Plastic");
			setIconName("plasticeye.gif");
			setHelpId("resourceActions.plastid");
			setSpecial(true);
		}});
		*/		

		/*
		actsMap.put(Activity.SCRIPT_SECTION, new MyTaskPaneGroup() {{
			setTitle("Automation");
			setExpanded(false);
		}});
		*/		

		actsMap.put(Activity.PLASTIC_SECTION,usePane); // fold plastic in with actions
		actsMap.put(Activity.INFO_SECTION, new MyTaskPaneGroup() {{
			setTitle("About");
			//setTitle("Further Information");
			setIconName("info16.png");
			setHelpId("resourceActions.info");
			//setSpecial(true);
		}});		
		final MyTaskPaneGroup exportPane = new MyTaskPaneGroup() {{
					setTitle("Export");
					setExpanded(false);
				}};
		actsMap.put(Activity.EXPORT_SECTION, exportPane);
		actsMap.put(Activity.SCRIPT_SECTION,exportPane); // combine this with export actions.
		// compose these tasks.
		for (Iterator i = actsMap.values().iterator(); i.hasNext();) {
			MyTaskPaneGroup t = (MyTaskPaneGroup) i.next();
			actionsPanel.add(t);
		}
		// assemble the menus.
		JMenu popupNew = new SelfEnablingMenu("New");
		JMenu actionsNew = new SelfEnablingMenu("New");
		// only add new menu in file explorer
		if (parent instanceof ActivitiesManager) {
			popup.add(popupNew);
			popup.addSeparator();
			actions.add(actionsNew);
			actions.addSeparator();
		}
		JMenu popupInfo = new SelfEnablingMenu("About") ;
		JMenu actionsInfo = new SelfEnablingMenu("About");
		JMenu popupExport = new SelfEnablingMenu("Export");
		JMenu actionsExport = new SelfEnablingMenu("Export");
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
			if (sectname.equals(Activity.NEW_SECTION)) {
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
	    popup.add(popupInfo);
	    actions.addSeparator();
	    actions.add(actionsExport);
	    actions.add(actionsInfo);
	    
	    actionsPanel.revalidate();
	    actionsPanel.repaint();	
	    
	    return activities;
	}

	public ActivityFactoryImpl(final IterableObjectBuilder activityBuilder) {
		super();
		this.activityBuilder = activityBuilder;
	}

    public ActivitiesManager create(UIComponent parent, boolean wantPopup,
            boolean wantMenu, boolean wantTaskPane) {
        // a bit back-to-front -I create all 3 componets by default at the moment,
        // and then discard the ones I don't want. dunno how wasteful this is yet.
        // hopefully the un-needed ones just get gc'd immediately. - they're
        // never realized on screen anyhow.
        //@future - rewrite more efficiently - although it will mean taking account of null 
        // everywhere in the factory, which will be a pain.
       JMenu menu = new JMenu("Actions");
       JTaskPane tasks = new JTaskPane();
       JPopupMenu popup = new JPopupMenu();
       Activity[] acts = create(parent,popup,tasks,menu);
       return new ActivitiesManagerImpl(acts,popup,menu,tasks);
    }

    public ActivitiesManager create(UIComponent parent) {
        return create(parent,true,true,true);
    }
    
    private static class ActivitiesManagerImpl implements ActivitiesManager {

        private final JPopupMenu popup;
        private final JMenu menu;
        private final JTaskPane tasks;
        private final Activity[] acts;

        public JMenu getMenu() {
            return menu;
        }

        public JPopupMenu getPopupMenu() {
            return popup;
        }

        public JTaskPane getTaskPane() {
            return tasks;
        }

        public void setSelection(Transferable tran) {
            for (int i = 0; i < acts.length; i++) {
                acts[i].selected(tran);
            }               
        }
        public void clearSelection() {
            for (int i = 0; i < acts.length; i++) {
                acts[i].noneSelected();
            }            
        }

        public ActivitiesManagerImpl(Activity[] acts,JPopupMenu popup, JMenu menu,
                JTaskPane tasks) {
            super();
            this.acts = acts;
            this.popup = popup;
            this.menu = menu;
            this.tasks = tasks;
        }
    }
}