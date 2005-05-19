/* MARS XML Snapshot Plugin
   Copyright (C) 2002-2004 Leapfrog Research & Development, LLC

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

package org.astrogrid.mars.plugin.xmlsnap;

import org.jdom.*;
import org.jdom.output.*;
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
/**
 * This is an almost line-by-line copy of the mars class of the same name,
 * but allows the user to specify a link to a style sheet, allowing one's
 * browser to then render the file in html
 * @author Jon Tayler johndavidtaylor@users.sourceforge.net
 */
public class XMLSnapshotPlugin implements Plugin, ProbeListener, Enableable {

    private boolean enabled;
    private File xmlfile;
    private long period;
    private long lastsnap;
    private String xslthref;

    public XMLSnapshotPlugin() {
        enabled = false;
        xmlfile = null;
        period = 60000L;
        lastsnap = -1L;
        xslthref = "";
    }

    public String getElementName() {
        return "xmlxsltsnap";
    }

    public String getDisplayName() {
        return "XML-XSLT Snapshot";
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void probeRun(ProbeEvent pe) {
        if (enabled && due()) {
            try {
                /* Get model configuration with status information */
                Element config = Main.getMain().getController()
                    .getModel().toJDOMElem(true);
                
                Document configDoc = new Document(config);
                if (!("".equals(xslthref))) {
                    configDoc.addContent(0,new ProcessingInstruction("xml-stylesheet", "type='text/xsl' href='"+xslthref+"'"));
                }
                /* Write it out */
                Writer configWriter = new OutputStreamWriter(
                    new FileOutputStream(xmlfile),"UTF8");
                XMLOutputter out = new XMLOutputter(
                    org.jdom.output.Format.getPrettyFormat());
                out.output(configDoc, configWriter);
                Main.getMain().showStatus("Wrote XML snapshot");
                configWriter.close();
            } catch (Exception ex) {
                Main.getMain().showStatus("XML Snapshot failed: "+ex);
                ex.printStackTrace();
            }
        }
    }

    private boolean due() {
        long now = System.currentTimeMillis();
        if (lastsnap + period < now) {
            lastsnap = now;
            return true;
        } else {
            return false;
        }
    }

	/* ------------------------------------------------------
		XML config serialization
	------------------------------------------------------ */

    public Element getConfig() {
        Element out = new Element(getElementName(),MarsModel.NAMESPACE);
        out.setAttribute("enabled",String.valueOf(enabled));
        out.setAttribute("xmlfile",xmlfile.getPath());
        out.setAttribute("period",String.valueOf(period));
        out.setAttribute("xslthref",xslthref);
        return out;
    }

    public void setConfig(Element in) throws InvalidDocumentException,
            IOException {

		String enabledStr = in.getAttributeValue("enabled");
		if (enabledStr == null) 
			throw new InvalidDocumentException("Missing XMLSnap enabled");

        String xmlfileStr = in.getAttributeValue("xmlfile");
        if (xmlfileStr == null)
            throw new InvalidDocumentException("Missing XMLSnap xmlfile");

        String periodStr = in.getAttributeValue("period");
        if (periodStr == null)
            throw new InvalidDocumentException("Missing XMLSnap period");
        
        xslthref = in.getAttributeValue("xslthref");
        if (xslthref == null)
            throw new InvalidDocumentException("Missing XMLSnap xslthref");

        this.enabled = Boolean.valueOf(enabledStr).booleanValue();;
        this.xmlfile = new File(xmlfileStr).getCanonicalFile();
        this.period = Long.parseLong(periodStr);
    }

	/* ------------------------------------------------------
		Editor
	------------------------------------------------------ */

    public Editor getEditor() {
        return new XMLSnapshotEditor();
    }

    private class XMLSnapshotEditor extends JPanel implements Editor {

		private JCheckBox enabledBox;
        private JTextField periodField;
        private JTextField xmlfileField;
        private JTextField xslthrefField;
        private JButton chooseBtn;

        private XMLSnapshotEditor() {
            // create editable fields
            enabledBox = new JCheckBox("Enabled",enabled);
            periodField = new JTextField(String.valueOf(period/1000.0),5);
            String path;
            if (xmlfile == null) {
                path = "";
            } else {
                path = xmlfile.getPath();
            }
            xmlfileField = new JTextField(path,30);
            xslthrefField = new JTextField(xslthref);

            // create chooser button
            chooseBtn = new JButton(new AbstractAction("Choose...") {
                public void actionPerformed(ActionEvent ae) {
                    selectXmlFile();
                }
            });

            // set field enabled states
            periodField.setEnabled(enabled);
            xmlfileField.setEnabled(enabled);
            chooseBtn.setEnabled(enabled);
            xslthrefField.setEnabled(enabled);

          	// set up an action listener on the checkbox
			enabledBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
                    periodField.setEnabled(enabledBox.isSelected());
                    xmlfileField.setEnabled(enabledBox.isSelected());
                    chooseBtn.setEnabled(enabledBox.isSelected());
                    xslthrefField.setEnabled(enabledBox.isSelected());
				}
			});

            // set up the layout manager
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.NORTHWEST; c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0; c.weighty = 0.0;
			c.insets = new Insets(4,4,6,4);
	
			// lay out the panel
			c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
			add(enabledBox,c);
			c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("Period"),c);
			c.gridx = 1; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			add(periodField,c);
			c.gridx = 2; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("sec."),c);

            c.gridx = 0; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
            add(new JLabel("XSLT href"),c);
            c.gridx = 1; c.gridy = 2; c.gridwidth = 2; c.gridheight = 1;
            add(xslthrefField,c);
            c.gridx = 0; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
            add(new JLabel("XML file"),c);
			c.gridx = 1; c.gridy = 3; c.gridwidth = 2; c.gridheight = 1;
			add(xmlfileField,c);
			c.anchor = GridBagConstraints.SOUTHEAST; c.fill = GridBagConstraints.NONE;
			c.gridx = 2; c.gridy = 4; c.gridwidth = 1; c.gridheight = 1;
            add(chooseBtn,c);

        }

        public void commit() throws NumberFormatException, IOException {
            enabled = enabledBox.isSelected();
            xmlfile = new File(xmlfileField.getText()).getCanonicalFile();
            xslthref = xslthrefField.getText();
            double periodDbl = Double.parseDouble(periodField.getText());
            if (periodDbl < 0 || periodDbl > 65535)
                throw new NumberFormatException("Period out of range");
            period = (long)(periodDbl * 1000);
        }

		public String getEditorTitle() {
			return "Configure XML Snapshot";
		}

    	private void selectXmlFile() {
		    // create a file chooser
		    JFileChooser chooser = new JFileChooser();
		    // ask for a file to open
		    int option = chooser.showSaveDialog(this);
		    if (option == JFileChooser.APPROVE_OPTION) {
			    // get pathname and stick it in the field
			    xmlfileField.setText(
                    chooser.getSelectedFile().getAbsolutePath());
		    } 
        }
	}
}
