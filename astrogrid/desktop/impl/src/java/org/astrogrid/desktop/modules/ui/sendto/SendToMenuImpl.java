/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.Component;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** Implementation of a shared 'send-to' menu component - used to pass data to other 
 * uitilies for vieing.
 * @author Noel Winstanley
 * @since Jun 17, 20062:10:55 AM
 */
public class SendToMenuImpl extends JPopupMenu implements SendToMenu {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(SendToMenuImpl.class);

	public SendToMenuImpl(List actions) {
		super("Send To...");
		setBorderPainted(true);
		for (Iterator i = actions.iterator(); i.hasNext(); ) {
			Object o = i.next();
			if (o instanceof SendToAction) {
				add((Action)o);
			} else if (o instanceof SendToActionFactory) {
				SendToActionFactory fac = (SendToActionFactory)o;
				JSeparator sep = new JSeparator();
				add(sep);
				fac.setMenu(this,sep);
				add(new JSeparator());// add another one afterwards
			} else {
				logger.warn("Ignoring unknown object type:" + o);
			}
		}
		
		
	}
	
	/** overrode this, to create something I can get the original action
	 * object back from.
	 */
	protected JMenuItem createActionComponent(Action a) {
		return new ActionJMenuItem(a);
	}
	
	/** menu item which lets you get back to the item */
	public static class ActionJMenuItem extends JMenuItem {

		public ActionJMenuItem(Action a) {
			super(a);
			this.a = a;
		}
		private final Action a;
		public Action getAction() {
			return a;
		}
	}
	
	/** iterate through teh list of actions, applying the atom to eacch */
	private void applyToActions(PreferredTransferable atom, UIComponent invoker) {
		for (int i = 0; i < getComponentCount(); i++) {
			Object o = getComponent(i);
			applyTo(o, atom, invoker);
		}
	}

	/**
	 * @param o
	 * @param atom
	 * @param invoker
	 */
	private void applyTo(Object o, PreferredTransferable atom, UIComponent invoker) {
		if (o instanceof ActionJMenuItem) {
			Action a = ((ActionJMenuItem)o).getAction();
			if (a instanceof SendToAction) {
				SendToAction act = (SendToAction)a;
				act.applyTo(atom,invoker);
			}
		} else if (o instanceof JMenu) { // sub menu - recursion.
			JMenu m = (JMenu)o;
			for (int i = 0; i < m.getItemCount(); i++) {
				applyTo(m.getItem(i),atom,invoker); // arg! why is this item, not compoennt!!
			}
		}		
	}
	
	/** display the popup menu, customized to one particular atom of data, and process user requests
	 * 
	 * @param atom the obejct the menu is going to operate upon.
	 * @param parent the UI component to report / create tasks in.
	 * @param invoker the conponent the menu has been invoke on. is expected that this
	 * will be contained witin <tt>parent</tt>
	 * @param x the x position to display the menu
	 * @param y the y position to display the menu
	 */
	public void show(PreferredTransferable atom,UIComponent parent,Component invoker, int x, int y) {
		applyToActions(atom,parent);
		super.show(invoker, x, y);
	}
	


	
}
