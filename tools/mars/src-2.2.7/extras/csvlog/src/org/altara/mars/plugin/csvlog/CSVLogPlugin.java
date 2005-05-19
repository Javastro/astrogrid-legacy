/* MARS CSV Logger Plugin
   Copyright (C) 2002-2003 Leapfrog Research & Development, LLC

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

package org.altara.mars.plugin.csvlog;

import org.jdom.*;
import org.altara.util.*;
import org.altara.mars.*;
import org.altara.mars.engine.*;
import org.altara.mars.plugin.*;
import org.altara.mars.swingui.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class CSVLogPlugin implements Plugin, ProbeListener, Enableable {

    private boolean enabled;
    private String logfile;

    private SimpleDateFormat format;

    private PrintWriter writer;

    public CSVLogPlugin() {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        enabled = false;
        logfile = "";
        writer = null;
    }

    public String getElementName() {
        return "csvlog";
    }

    public String getDisplayName() {
        return "CSV Logger";
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void probeRun(ProbeEvent pe) {
        if (writer != null) {
            Service service = pe.getService();
            Status status = pe.getNewStatus();
            Host host = service.getHost();
            String stamp = format.format(new Date());
            writer.println( stamp+", "+
                            host.getName()+", "+
                            service.getName()+", "+
                            host.getAddress().getHostName()+", "+
                            service.getPort()+", "+
                            service.getSvcType()+", "+
                            status.toString()+", "+
                            status.getResponseTime());
        }
    }

    private void startWriter() throws IOException {
        writer = new PrintWriter(new FileWriter(logfile,true),true);
        enabled = true;
    }

    private void stopWriter() {
        if (writer != null) writer.close();
        writer = null;
        enabled = false;
    }

	/* ------------------------------------------------------
		XML config serialization
	------------------------------------------------------ */

    public Element getConfig() {
        Element out = new Element(getElementName(),MarsModel.NAMESPACE);
        out.setAttribute("enabled",String.valueOf(enabled));
        out.setAttribute("logfile",logfile);
        return out;
    }

    public void setConfig(Element in) throws InvalidDocumentException,
            IOException {
       	boolean enabled;
        String logfile;

		String enabledStr = in.getAttributeValue("enabled");
		if (enabledStr == null) 
			throw new InvalidDocumentException("Missing CSV enabled");
		enabled = Boolean.valueOf(enabledStr).booleanValue();

        logfile = in.getAttributeValue("logfile");
        if (logfile == null)
            throw new InvalidDocumentException("Missing CSV logfile");

        this.logfile = logfile;
        if (enabled) startWriter();
    }

	/* ------------------------------------------------------
		Editor
	------------------------------------------------------ */

    public Editor getEditor() {
        return new CSVLogEditor();
    }

    private class CSVLogEditor extends JPanel implements Editor {

		private JCheckBox enabledBox;
        private JTextField logfileField;
        private JButton chooseBtn;

        private CSVLogEditor() {
            // create editable fields
            enabledBox = new JCheckBox("Enabled",enabled);
            logfileField = new JTextField(logfile,30);

            // create chooser button
            chooseBtn = new JButton(new AbstractAction("Choose...") {
                public void actionPerformed(ActionEvent ae) {
                    selectLogfile();
                }
            });

            // set field enabled states
            logfileField.setEnabled(enabled);
            chooseBtn.setEnabled(enabled);

          	// set up an action listener on the checkbox
			enabledBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
                    logfileField.setEnabled(enabledBox.isSelected());
                    chooseBtn.setEnabled(enabledBox.isSelected());
				}
			});

            // set up the layout manager
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = c.NORTHWEST; c.fill = c.BOTH;
			c.weightx = 1.0; c.weighty = 0.0;
			c.insets = new Insets(4,4,6,4);
	
			// lay out the panel
			c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
			add(enabledBox,c);
			c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("Log file"),c);
			c.gridx = 1; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			add(logfileField,c);
			c.anchor = c.SOUTHEAST; c.fill = c.NONE;
			c.gridx = 1; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
            add(chooseBtn,c);
        }

        public void commit() throws IOException {
            logfile = logfileField.getText();
            if (enabledBox.isSelected()) { 
                startWriter();
            } else {
                stopWriter();
            }
        }

		public String getEditorTitle() {
			return "Configure CSV Logger";
		}
    	private void selectLogfile() {
		    // create a file chooser
		    JFileChooser chooser = new JFileChooser();
		    // ask for a file to open
		    int option = chooser.showSaveDialog(this);
		    if (option == JFileChooser.APPROVE_OPTION) {
			    // get pathname and stick it in the field
			    logfileField.setText(
                    chooser.getSelectedFile().getAbsolutePath());
		    } 
        }
	}
}
