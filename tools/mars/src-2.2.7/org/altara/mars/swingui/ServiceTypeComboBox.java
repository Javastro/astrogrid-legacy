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

public class ServiceTypeComboBox extends JComboBox {

	public ServiceTypeComboBox (final JTextField portField) {
		// make sure only valid service types are selectable
		setEditable(false);
		// add all known service types into the combo box
		Iterator serviceTypes = ProbeFactory.getRegisteredServiceTypes();
		while (serviceTypes.hasNext()) {
			addItem(serviceTypes.next());
		}

		// change portField when new service types are selected
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				portField.setText(String.valueOf(
					ProbeFactory.getFactory((String)getSelectedItem())
					.getDefaultPort()));
			}
		});
	}

	public String getSvcType() {
		return (String)getSelectedItem();
	}

	public void setSvcType(String svcType) {
		setSelectedItem(svcType);
	}

	public void fireActionEvent() {
		super.fireActionEvent();
	}
}
