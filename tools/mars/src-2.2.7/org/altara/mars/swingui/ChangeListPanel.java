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
import org.altara.mars.plugin.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;

/** Contains the change list and its control(s).
*/

public class ChangeListPanel extends JPanel {

	public ChangeListPanel(Controller controller) {
		// create the change list
		final ChangeListModel clm = new ChangeListModel();
		controller.addStatusChangeListener(
			new StatusChangeThreadAdapter(clm));
		JList changeList = new JList(clm);
		JScrollPane changeListSP = new JScrollPane(changeList);
		// set up various list options
		changeListSP.setBorder(new TitledBorder("Recent Changes"));
		// set up the renderer for the change list
		ChangeListRenderer clr = new ChangeListRenderer();
		changeList.setCellRenderer(clr);

		// create a button to clear the history
		JButton clearBtn = new JButton("Clear History");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				clm.clear();
			}
		});

		// set up the layout manager
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = c.NORTHWEST; c.fill = c.BOTH;
		c.weightx = 1.0; c.weighty = 0.0;
		c.insets = new Insets(4,4,6,4);
 		c.gridwidth = 1; c.gridheight = 1;

		// lay out the panel
		c.gridx = 0; c.gridy = 0; c.weighty = 1.0;
		add(changeListSP,c);
		c.gridx = 0; c.gridy = 1; c.weighty = 0.0;
		c.fill = c.VERTICAL; c.anchor = c.SOUTHEAST;
		add(clearBtn,c);
	}
}

