/* MARS Network Monitor Swing User Interface
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2002 Leapfrog Research & Development, LLC

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, it is available at 
	http:///www.gnu.org/copyleft/gpl.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place - Suite 330,
	Boston, MA  02111-1307, USA.
*/

package org.altara.mars.swingui;

import org.altara.util.*;
import org.altara.mars.*;
import org.altara.mars.engine.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;

/** Renders a list cell or tree cell using a JLabel. Most of the look and
	feel of the renderer components is controlled here.
*/

public abstract class MarsAbstractRenderer extends JLabel
		implements ListCellRenderer, TreeCellRenderer {

	protected static final Color nominalFg = Color.black;
	protected static final Color nominalBg = Color.white;
	protected static final Color selectedFg = Color.black;
	protected static final Color selectedBg = new Color(0xcc,0xcc,0xff);
	protected static final Border focusBorder = new EtchedBorder();
	protected static final Border nominalBorder = new EmptyBorder(2,2,2,2);
	public static final Font cellFont = new Font("SansSerif",Font.PLAIN,11);

	protected static Icon downIcon;
	protected static Icon timeoutIcon;
	protected static Icon upIcon;
	protected static Icon unknownIcon;
	protected static Icon serviceIcon;
	protected static Icon okHostIcon;
	protected static Icon faultHostIcon;

	protected MarsAbstractRenderer() {
		// load icons if necessary
		if (downIcon == null) {
			downIcon = IconService.getIcon("mi2_ssdn.gif");
			timeoutIcon = IconService.getIcon("mi2_ssto.gif");
			upIcon = IconService.getIcon("mi2_ssup.gif");
			unknownIcon = IconService.getIcon("mi2_ssunk.gif");
			serviceIcon = IconService.getIcon("mi2_stsvc.gif");
			okHostIcon = IconService.getIcon("mi2_sthost.gif");
			faultHostIcon = IconService.getIcon("mi2_sthfault.gif");
		}
			
		// initialize default label state
		setOpaque(true);
		setFont(cellFont);

	}

	protected Component getCellRendererComponent(Object value,
			boolean isSelected, boolean cellHasFocus) {
		// decide which selection decoration to use
		if (isSelected) {
			setBackground(selectedBg);
			setForeground(selectedFg);
		} else {
			setBackground(nominalBg);
			setForeground(nominalFg);
		}

		// decide which focus decoration to use
		if (cellHasFocus) {
			setBorder(focusBorder);
		} else {
			setBorder(nominalBorder);
		}

		// set the label's icon
		setIcon(getIconForValue(value));

		// and its label text
		setText(getStringForValue(value));

		// return the label
		return this;
	}

	protected abstract Icon getIconForValue(Object value);
	protected abstract String getStringForValue(Object value);

	/* ------------------------------------------------------------
		Status-handling utility methods
	--------------------------------------------------------------*/

	protected Icon getIconForStatus(Status status) {
	// Select an icon based on status code
		if (status.getCode().intValue() <= Status.MAX_HARDFAULTCODE)
			return downIcon;
		if (status.getCode().intValue() <= Status.MAX_SOFTFAULTCODE)
			return timeoutIcon;
		if (status.getCode() == Status.UP)
			return upIcon;
		return unknownIcon;
	}

    protected Icon getIconForHost(Host host) {
        // are any of the host's services faulted?
        if (host.isOK()) {
            return okHostIcon;
        } else {
            return faultHostIcon;
        }
    }

	/* ------------------------------------------------------------
		ListCellRenderer / TreeCellRenderer implementation
	--------------------------------------------------------------*/

	public Component getListCellRendererComponent(
			JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		return getCellRendererComponent(value,isSelected,cellHasFocus);
	}

	public Component getTreeCellRendererComponent(
			JTree tree, Object value, boolean isSelected, 
			boolean isExpanded, boolean isLeaf, int row,
			boolean cellHasFocus) {
		if (value instanceof DefaultMutableTreeNode) {
			value = ((DefaultMutableTreeNode)value).getUserObject();
		}
		return getCellRendererComponent(value,isSelected,cellHasFocus);
	}
	
}
	
