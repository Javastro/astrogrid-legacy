package org.astrogrid.desktop.modules.ui.comp;

import java.awt.CardLayout;

import javax.swing.JPanel;

/** a jpanel with integrated card layout  */
public class FlipPanel extends JPanel{
	public FlipPanel() {
		card = new CardLayout();
		this.setLayout(card);
	}
	private final CardLayout card;
	public void show(String viewName) {
		card.show(this,viewName);
	}
}