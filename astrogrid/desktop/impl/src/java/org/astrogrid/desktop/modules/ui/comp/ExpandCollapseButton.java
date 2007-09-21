package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.Action;
import javax.swing.JToggleButton;

import org.astrogrid.desktop.icons.IconHelper;

import com.l2fprod.common.swing.JCollapsiblePane;

/** button that controls a jCollapsiblepane */
public class ExpandCollapseButton extends JToggleButton {
	/**
	 * construct a new expand-collapse button
	 * @param pane the pane to expand or collapse on button click.
	 */
	public ExpandCollapseButton(JCollapsiblePane pane) {
		final Action toggleAction = pane.getActionMap().get(JCollapsiblePane.TOGGLE_ACTION);
		toggleAction.putValue(JCollapsiblePane.EXPAND_ICON, IconHelper.loadIcon("expand22.png"));
		toggleAction.putValue(JCollapsiblePane.COLLAPSE_ICON, IconHelper.loadIcon("contract22.png"));
		this.setAction(toggleAction);		
		setText("");
		putClientProperty("is3DEnabled", Boolean.FALSE);
		setBorderPainted(false);			
	}
}