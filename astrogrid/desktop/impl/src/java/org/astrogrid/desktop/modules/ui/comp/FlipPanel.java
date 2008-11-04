package org.astrogrid.desktop.modules.ui.comp;

import java.awt.CardLayout;

import javax.swing.JPanel;

/** a jpanel with integrated card layout - allows the panel to be 'flipped' to display something different. */
public class FlipPanel extends JPanel{
	public FlipPanel() {
		card = new CardLayout();
		this.setLayout(card);
	}
	private final CardLayout card;
	public void show(final String viewName) {
		card.show(this,viewName);
		current = viewName;
	}
	
	private String current;
	
	public String currentlyShowing() {
		return current;
	}
}