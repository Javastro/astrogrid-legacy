package org.astrogrid.desktop.modules.ui.comp;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.astrogrid.desktop.icons.IconHelper;

import com.l2fprod.common.swing.JCollapsiblePane;

/** Button that controls a {@code JCollapsiblePane}, or a normal pane. */
public class ExpandCollapseButton extends JToggleButton implements ItemListener {
	/**
     * 
     */
    private static final String CONTRACT_ICON = "contract16.png";
    /**
     * 
     */
    private static final String EXPAND_ICON = "expand16.png";
    private JPanel pane;

    /**
	 * construct a new expand-collapse button
	 * @param pane the pane to expand or collapse on button click.
	 */
	public ExpandCollapseButton(final JCollapsiblePane pane) {
	    this();
		final Action toggleAction = pane.getActionMap().get(JCollapsiblePane.TOGGLE_ACTION);
		toggleAction.putValue(JCollapsiblePane.EXPAND_ICON, IconHelper.loadIcon(EXPAND_ICON));
		toggleAction.putValue(JCollapsiblePane.COLLAPSE_ICON, IconHelper.loadIcon(CONTRACT_ICON));
		this.setAction(toggleAction);
		this.putClientProperty("hideActionText",Boolean.TRUE);
		this.setText(null); // as client property isn't always respected.
		setMargin(UIConstants.SMALL_BUTTON_MARGIN);
       		
	}
	
	public ExpandCollapseButton(final JPanel pane) {
	    this.pane = pane;
        setIcon(IconHelper.loadIcon(EXPAND_ICON));
	    setSelectedIcon(IconHelper.loadIcon(CONTRACT_ICON));
	    addItemListener(this);
        setText("");	    
	}
	
	private ExpandCollapseButton() {
		putClientProperty("is3DEnabled", Boolean.FALSE);
		//setBorderPainted(false);	
		//setContentAreaFilled(false);
		//setBorder(null);
		
	}

	// used when working with a normal jPanel.
    public void itemStateChanged(final ItemEvent e) {
            pane.setVisible(isSelected());
    }
}