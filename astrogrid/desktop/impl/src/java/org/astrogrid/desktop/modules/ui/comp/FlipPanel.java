package org.astrogrid.desktop.modules.ui.comp;

import java.awt.CardLayout;

import javax.swing.JPanel;

/** a jpanel with integrated card layout - allows the panel to be 'flipped' to display something different.
 * 
 *  whenever the view is flipped, any property change listeners are notified.. The property is named {@link #SHOWING}
 *  
 *  */
public class FlipPanel extends JPanel{
	public FlipPanel() {
		card = new CardLayout();
		this.setLayout(card);
	}
	private final CardLayout card;
	public void setShowing(final String viewName) {
		card.show(this,viewName);
		final String old = current;
		current = viewName;
		super.firePropertyChange(SHOWING,old,current);
		
	}
	
	/** property name to listen to for changes when the current view changes */
	public static final String SHOWING = "showing";
	
	private String current;
	
	public String getShowing() {
		return current;
	}
}