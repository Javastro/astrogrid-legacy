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

public class ServiceParamEditor extends JPanel {

	private String[] paramNames;
	private JTextField[] paramValFields;

	public ServiceParamEditor(ProbeFactory factory) {

		// set up the layout manager
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = c.NORTHWEST; c.fill = c.BOTH;
		c.weightx = 1.0; c.weighty = 0.0;
		c.insets = new Insets(4,4,6,4);

		// Get lists of variables and values
		paramNames = factory.getServiceParamNames();
		String[] paramLabels = factory.getServiceParamLabels();

		// check for null
		if (paramNames == null) {
			c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("This service has no parameters"),c);
			return;
		}

		// layout labels and param values in panel
		paramValFields = new JTextField[paramNames.length];
		c.gridwidth = 1; c.gridheight = 1;
		for (int i = 0; i < paramNames.length; i++) {
			paramValFields[i] = new JTextField(15);
			c.gridy = i;
			c.gridx = 0;
			add(new JLabel(paramLabels[i]),c);
			c.gridy = i;
			c.gridx = 1;
			add(paramValFields[i],c);
		}
	}

	public void fillParams(Service service) {
		// check for no params
		if (paramNames == null) return;

		// otherwise fill in required parameters from the service
		for (int i = 0; i < paramNames.length; i++) {
			String val = service.getParameter(paramNames[i]);
			if (val == null) val = "";
			paramValFields[i].setText(val);
		}
	}

	public void commitParams(Service service) {
		// check for no params
		if (paramNames == null) return;

		// otherwise fill in the required parameters to the service
		for (int i = 0; i < paramNames.length; i++) {
			String val = paramValFields[i].getText();
			if (val.length() == 0) val = null;
			service.setParameter(paramNames[i],val);
		}
	}

	public HashMap getParamMap() {
		HashMap out = new HashMap();
		if (paramNames == null) {
			return out;
		}

		for (int i = 0; i < paramNames.length; i++) {
			String val = paramValFields[i].getText();
			if (val.length() == 0) val = null;
			out.put(paramNames[i],val);
		}
		return out;
	}
}
