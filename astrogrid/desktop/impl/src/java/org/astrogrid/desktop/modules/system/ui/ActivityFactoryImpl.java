/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.datatransfer.Transferable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPopupMenu;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.hivemind.ApplicationRuntimeException;
import org.astrogrid.desktop.hivemind.ClassKeyObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.actions.Activity;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

/** Implementation of an {@code ActivityFactory}
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 26, 20073:18:32 PM
 * @TEST how things are getting assembled.
 */
public class ActivityFactoryImpl implements ActivityFactory {
	/** task pane with a few additional functions */
	private static class MyTaskPaneGroup extends JTaskPaneGroup{

		public void setHelpId(final String s) {
			CSH.setHelpIDString(this,s);		
		}
		
		public void setIconName(final String s) {
			setIcon(IconHelper.loadIcon(s));
		}

	}

	private final ClassKeyObjectBuilder activityBuilder;

    public ActivitiesManager create(final UIComponent parent, final Class[] acts) {
        for (int i = 0; i < acts.length; i++) {
            if (! Activity.class.isAssignableFrom(acts[i])) {
                throw new ApplicationRuntimeException(acts[i].getName() + " does not implement Activity");
            }
        }
        final JTaskPane tasks = new JTaskPane();
        final JPopupMenu popup = new JPopupMenu();
        CSH.setHelpIDString(tasks, "activity.intro");
        final MyTaskPaneGroup infoPane= new MyTaskPaneGroup() {{
            setTitle("About");
            setIconName("info16.png");
            setSpecial(true);
        }};
        final MyTaskPaneGroup usePane = new MyTaskPaneGroup() {{
            setTitle("Actions");
            setIconName("run16.png");
            //setSpecial(true);
        }};
        tasks.add(usePane);
        tasks.add(infoPane);
        tasks.setBackground(infoPane.getBackground()); // fix for windows, where it appears white - not very nice.
        
        final Map actsMap = new ListOrderedMap();
        for (int i = 0; i < acts.length; i++) {
            final Activity a = (Activity)activityBuilder.create(acts[i]);
            a.setUIParent(parent);
            actsMap.put(acts[i],a);
            if (! (a instanceof Activity.NoContext)) {
                a.addTo(popup);
            }
            if (! (a instanceof Activity.NoTask)) {
                if (a instanceof Activity.Info) {
                    a.addTo(infoPane);
                } else {
                    a.addTo(usePane);
                }
            }
        }
        // cause tasks panel to update
        tasks.revalidate();
        tasks.repaint();         
        return new ActivitiesManagerImpl(actsMap,tasks,popup);
    }


	public ActivityFactoryImpl(final ClassKeyObjectBuilder activityBuilder) {
		super();
		this.activityBuilder = activityBuilder;
	}


    /** implementation of {@code ActivitiesManager}. */
    private static class ActivitiesManagerImpl implements ActivitiesManager {

        private final JPopupMenu popup;
        private final JTaskPane tasks;
        private final Map acts;
        private final Activity[] actsArray;

        public JPopupMenu getPopupMenu() {
            return popup;
        }

        public JTaskPane getTaskPane() {
            return tasks;
        }

        public void setSelection(final Transferable tran) {
            trans = tran;
            for (int i = 0; i < actsArray.length; i++) {
                actsArray[i].selected(tran);
            }               
        }
        
        private Transferable trans;
        public Transferable getCurrentSelection() {
            return trans;
        }
        
        public void clearSelection() {
            trans = null;
            for (int i = 0; i < actsArray.length; i++) {
                actsArray[i].noneSelected();
            }            
        }

        public ActivitiesManagerImpl(final Map acts, final JTaskPane tasks,final JPopupMenu popup) {
            super();
            this.acts = acts;
            this.actsArray = (Activity[])acts.values().toArray(new Activity[acts.size()]);
            this.popup = popup;
            this.tasks = tasks;
        }

        public Activity getActivity(final Class actClass) {
            return (Activity)acts.get(actClass);
        }

        public Iterator iterator() {
            return acts.values().iterator();
        }
    }


}
