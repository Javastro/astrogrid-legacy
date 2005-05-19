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
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class HostEditorPanel extends JPanel implements Editor {

	private MarsModel model;
	private Host host;

	private JTextField nameField;
	private JTextField addrField;
	private String editorTitle;

	public HostEditorPanel(MarsModel model) {
		// hold on to the model
		this.model = model;
		this.host = null;

		// default to new host
		this.editorTitle = "Add Host";

		// create editable fields
		nameField = new JTextField(25);
		addrField = new JTextField(25);

		// set up the layout manager
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = c.NORTHWEST; c.fill = c.BOTH;
		c.weightx = 1.0; c.weighty = 0.0;
		c.insets = new Insets(4,4,6,4);

		// lay out the panel
		c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
		add(new JLabel("Name"),c);
		c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
		add(nameField,c);
		c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
		add(new JLabel("Address"),c);
		c.gridx = 1; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
		add(addrField,c);
		c.gridx = 1; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
		JLabel addrHelp = new JLabel("Enter DNS hostname or IP address.");
		addrHelp.setFont(addrHelp.getFont().deriveFont(Font.PLAIN,9));
		add(addrHelp,c);
	}
	
	public HostEditorPanel(Host host) {
		// create the UI first
		this(host.getModel());
		
		// capture the host to edit
		this.host = host;

		// change the header label
		this.editorTitle = "Edit Host";

		// change the text field contents
		nameField.setText(host.getName());
		addrField.setText(host.getAddress().getHostName());
	}

	public void commit() throws UnknownHostException {
		if (host == null) {
			host = new Host(model, nameField.getText(),
				InetAddress.getByName(addrField.getText()));
		} else {
			host.setName(nameField.getText());
			host.setAddress(InetAddress.getByName(addrField.getText()));
			host.fireHostChanged();
		}
	}

	public String getEditorTitle() {
		return editorTitle;
	}
}
