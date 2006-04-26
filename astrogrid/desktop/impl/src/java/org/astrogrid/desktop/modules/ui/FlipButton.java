package org.astrogrid.desktop.modules.ui;

import javax.swing.Icon;
import javax.swing.JButton;

import org.astrogrid.desktop.icons.IconHelper;

/**
 * @author Noel Winstanley
 * @since Apr 26, 20063:51:19 PM
 */
public class FlipButton extends JButton {
	private Icon searchIcon = IconHelper.loadIcon("find.png");

	private Icon haltIcon = IconHelper.loadIcon("stop.gif"); //"fileclose.png");

	{
		enableSearch();
	}

	private boolean doingSearch;

	public boolean isDoingSearch() {
		return doingSearch;
	}
		
	
	private void enableSearch() {
		doingSearch = false;
		setText("Search");
		setIcon(searchIcon);
		setToolTipText("Find resources for this position");
	}

	private void enableHalt() {
		doingSearch=true;
		setText("Halt");
		setIcon(haltIcon);
		setToolTipText("Halt the search");
	}

	public void flip() {
		if (doingSearch) {
			enableSearch();
		} else {
			enableHalt();
		}
	}
}