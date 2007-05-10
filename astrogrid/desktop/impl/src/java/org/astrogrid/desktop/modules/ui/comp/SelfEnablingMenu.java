package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/** listens to children, and adjusts it's own enable / disable status accordingly */
public class SelfEnablingMenu extends JMenu implements PropertyChangeListener {

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
		 if (item.isEnabled()) {
			 setEnabled(true);
		 }
		 return item;
	}
	public JMenuItem add(Action a) {
		 JMenuItem item = super.add(a);
		 item.addPropertyChangeListener("enabled",this);
		 if (item.isEnabled()) {
			 setEnabled(true);
		 }		 
		 return item;
	}

	public Component add(Component c, int index) {
		Component comp =  super.add(c, index);
		comp.addPropertyChangeListener("enabled",this);
		if (comp.isEnabled()) {
			setEnabled(true);
		}
		return comp;
	}

	public Component add(Component c) {
		Component comp =  super.add(c);
		comp.addPropertyChangeListener("enabled",this);
		if (comp.isEnabled()) {
			setEnabled(true);
		}		
		return comp;
	}

	public JMenuItem add(String s) {
		JMenuItem mi =  super.add(s);
		mi.addPropertyChangeListener("enabled",this);
		if (mi.isEnabled()) {
			setEnabled(true);
		}
		return mi;
	}

}