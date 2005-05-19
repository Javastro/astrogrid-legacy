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
import javax.swing.event.*;

/** Implements the fault list's context menus
*/

public class FaultListContextMenuSupport extends ListContextMenuSupport {

	private Action hideFaultAction;
	private Action showFaultsAction;

	private FaultListModel model;

	public FaultListContextMenuSupport (JList faultList) {

		super(faultList);
		this.model = (FaultListModel)faultList.getModel();

		// Initialize actions
		initializeActions();
	}

	private void initializeActions() {
		// first, create each action
		hideFaultAction = new AbstractAction("Hide") {
			public void actionPerformed(ActionEvent ae) {
				model.hideFault((Service)getLastInvoked());
			}
		};

		showFaultsAction = new AbstractAction("Show All") {
			public void actionPerformed(ActionEvent ae) {
				model.showAllFaults();
			}
		};

		// bind them to the appropriate classes
		addClassAction(String.class,showFaultsAction);
		addClassAction(Service.class,hideFaultAction);
		addClassAction(Service.class,showFaultsAction);
	}
}
