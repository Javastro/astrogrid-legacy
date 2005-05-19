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

public class ServiceEditorPanel extends JPanel implements Editor {

    private final static String[] notacNames = {
        "never",
        "immediately",
        "on second attempt",
        "on third attempt"
    };

	private Host host;
	private Service service;

	private JTextField nameField;
	private ServiceTypeComboBox typeBox;
    private JComboBox notacBox;
	private ServiceParamEditor paramEditor;
	private JTextField portField;
	private JTextField timeoutField;
	private JTextField periodField;
	private String editorTitle;

	private GridBagConstraints c;
	
	public ServiceEditorPanel(Host host) {
		// hold on to the host
		this.host = host;
		this.service = null;

		// default editor title to creation
		this.editorTitle = "Add Service";

		// create editable fields
		nameField = new JTextField(25);
		timeoutField = new JTextField(5);
		periodField = new JTextField(5);
		portField = new JTextField(5);
		typeBox = new ServiceTypeComboBox(portField);
        notacBox = new JComboBox(notacNames);

		// null out the params editor for now
		paramEditor = null;

		// fill in default timeout/period
		double defTimeout = Service.DEFAULT_TIMEOUT / 1000.0;
		timeoutField.setText(String.valueOf(defTimeout));
		double defPeriod = Service.DEFAULT_PERIOD / 1000.0;
		periodField.setText(String.valueOf(defPeriod));

        // fill in default notac
        notacBox.setSelectedIndex(Service.DEFAULT_NOTAC);

		// set up the layout manager
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.anchor = c.NORTHWEST; c.fill = c.BOTH;
		c.weightx = 1.0; c.weighty = 0.0;
		c.insets = new Insets(4,4,6,4);

		// lay out the panel
		c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
		add(new JLabel("Name",JLabel.RIGHT),c);
		c.gridx = 1; c.gridy = 0; c.gridwidth = 5; c.gridheight = 1;
		add(nameField,c);
		c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
		add(new JLabel("Type",JLabel.RIGHT),c);
		c.gridx = 1; c.gridy = 1; c.gridwidth = 2; c.gridheight = 1;
		add(typeBox,c);
		c.gridx = 3; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
		add(new JLabel("Port",JLabel.RIGHT),c);
		c.gridx = 4; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
		add(portField,c);
		c.gridx = 0; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
		add(new JLabel("Timeout",JLabel.RIGHT),c);
		c.gridx = 1; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
		add(timeoutField,c);
		c.gridx = 2; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
		add(new JLabel("sec."),c);
		c.gridx = 3; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
		add(new JLabel("Check ea.",JLabel.RIGHT),c);
		c.gridx = 4; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
		add(periodField,c);
		c.gridx = 5; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
		add(new JLabel("sec."),c);
		c.gridx = 0; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
		add(new JLabel("Notify",JLabel.RIGHT),c);
		c.gridx = 1; c.gridy = 3; c.gridwidth = 5; c.gridheight = 1;
		add(notacBox,c);

		// add trigger for service parameters panel
		typeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				createParamEditor();
			}
		});

		// trigger an action event on the type box to fill in default port
		// and param panel
		typeBox.fireActionEvent();
	}

	public ServiceEditorPanel(Service service) {
		// create the UI first
		this(service.getHost());

		// capture the service to edit
		this.service = service;

		// change the editor title
		this.editorTitle = "Edit Service";

		// fill in the text field values
		nameField.setText(service.getName());
		typeBox.setSvcType(service.getSvcType());
		portField.setText(String.valueOf(service.getPort()));
		double timeoutDbl = service.getTimeout() / 1000.0;
		timeoutField.setText(String.valueOf(timeoutDbl));
		double periodDbl = service.getPeriod() / 1000.0;
		periodField.setText(String.valueOf(periodDbl));
        notacBox.setSelectedIndex(service.getNotac());

		// create the editor panel and fill in values
		createParamEditor();
		paramEditor.fillParams(service);
	}

	public void commit() throws NumberFormatException,
			InvalidServiceTypeException {
		// try validating the fields first
		int port = Integer.parseInt(portField.getText());
		if (port < 0 || port > 65535)
			throw new NumberFormatException("Port out of range");

		double timeoutDbl = Double.parseDouble(timeoutField.getText());
		if (timeoutDbl < 0 || timeoutDbl > 65535)
			throw new NumberFormatException("Timeout out of range");
		long timeout = (long)(timeoutDbl*1000);

		double periodDbl = Double.parseDouble(periodField.getText());
		if (periodDbl < 0 || periodDbl > 65535)
			throw new NumberFormatException("Check period out of range");
		long period = (long)(periodDbl*1000);

		if (service == null) {
			// then create a new service if necessary
			service = new Service(host,nameField.getText(),
				typeBox.getSvcType(),port,timeout,period,
                notacBox.getSelectedIndex(),
				paramEditor.getParamMap());
		} else {
			// otherwise, just save the changes into this service
			service.setName(nameField.getText());
			service.setSvcType(typeBox.getSvcType());
			service.setPort(port);
			service.setTimeout(timeout);
			service.setPeriod(period);
            service.setNotac(notacBox.getSelectedIndex());
			paramEditor.commitParams(service);
			service.fireServiceChanged();
		}
	}

	private void createParamEditor() {
		// remove old param editor if necessary
		if (paramEditor != null) remove(paramEditor);
		
		// create a new editor panel of the appropriate type
		String svcname = (String)typeBox.getSelectedItem();
		paramEditor =
			new ServiceParamEditor(ProbeFactory.getFactory(svcname));
		paramEditor.setBorder(
			new TitledBorder("Parameters for "+svcname));

		// add it to the main panel
		c.gridx = 0; c.gridy = 4; c.gridwidth = 6; c.gridheight = 6;
		add(paramEditor,c);
		
		// revalidate the container and fix up window size
		validate();
        packParentWindow();
	}

	public String getEditorTitle() {
		return editorTitle;
	}

    private void packParentWindow() {
        Component curComponent = this;
        while (curComponent != null && !(curComponent instanceof Window)) {
            curComponent = curComponent.getParent();
        }
        if (curComponent != null) ((Window)curComponent).pack();
    }
}
