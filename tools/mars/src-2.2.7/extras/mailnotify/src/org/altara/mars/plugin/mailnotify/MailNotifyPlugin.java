/* MARS Mail Notification Plugin
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

package org.altara.mars.plugin.mailnotify;

import org.jdom.*;
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


public class MailNotifyPlugin implements Plugin, NotificationListener,
                                         Enableable {

	private static final DateFormat df = DateFormat.getTimeInstance();

    private static String getFirstAddress(String addressList) {
        int comPos = addressList.indexOf(',');
        if (comPos == -1)
            return addressList;
        else
            return addressList.substring(0,comPos);
    }

	private InetAddress mailServer;
	private String mailAddress;
	private String fromAddress;
	private boolean enabled;
	private boolean notifyBackUp;

	private SimpleSmtpClient client;

	public MailNotifyPlugin() {
		mailServer = null;
		mailAddress = null;
		enabled = false;
		notifyBackUp = false;
	}

	public String getElementName() {
		return "mailnotify";
	}

	public String getDisplayName() {
		return "Mail Notification";
	}

    public boolean isEnabled() {
        return enabled;
    }

	public void notifyStatusChanged(StatusChangeEvent sce) {
		// do nothing if not enabled
		if (!enabled) return;

		// check one: we just went down.
		if (sce.getOldStatus().getCode() == Status.UP &&
			sce.getNewStatus().isFault()) {
			sendMessage(sce, "Fault");
		} else 
		// check two: we came back up, and we care about such things
		if (notifyBackUp && sce.getOldStatus().isFault() &&
			sce.getNewStatus().getCode() == Status.UP) {
			sendMessage(sce, "Notice");
		}
	}

	private void sendMessage (StatusChangeEvent sce, String subjdesc) {
		Status newStatus = sce.getNewStatus();
		Service service = sce.getService();
		String serviceDesc =
			service.getName()+" on "+service.getHost().getName();
		String subject = "[Mars] "+subjdesc+": "+serviceDesc;
		String message = serviceDesc+" is "+newStatus.toString()+
			" at "+df.format(new Date(newStatus.getTimestamp()))+".";
		Status sendStatus =
            client.sendMessage(fromAddress,mailAddress,subject,message);
        if (sendStatus.isFault()) {
            Main.getMain().showStatus("Mail notification failure: "+sendStatus);
        } else {
            Main.getMain().showStatus(service+": Mail notification sent");
        }
	}

	private void setServer(InetAddress mailServer) {
		this.mailServer = mailServer;
		if (mailServer != null) {
			this.client = new SimpleSmtpClient(mailServer);
		}
	}

	/* ------------------------------------------------------
		XML config serialization
	------------------------------------------------------ */

	public Element getConfig() {
		Element out = new Element(getElementName(),MarsModel.NAMESPACE);
		out.setAttribute("enabled",String.valueOf(enabled));
		out.setAttribute("notifyBackUp",String.valueOf(notifyBackUp));
		if (mailServer != null)
			out.setAttribute("server",mailServer.getHostName());
		if (mailAddress != null)
			out.setAttribute("address",mailAddress);
      if (fromAddress != null)
			out.setAttribute("fromAddress", fromAddress);
		return out;
	}

	public void setConfig(Element in) throws UnknownHostException,
			InvalidDocumentException {
		boolean enabled, notifyBackUp;
		InetAddress mailServer = null;
		String mailAddress = null;
		String fromAddress = null;
		String backUpStr = null;

		String enabledStr = in.getAttributeValue("enabled");
		if (enabledStr == null)
			throw new InvalidDocumentException("Missing mail enabled");
		enabled = Boolean.valueOf(enabledStr).booleanValue();

		String mailServerStr = in.getAttributeValue("server");
		if (mailServerStr == null && enabled == true)
			throw new InvalidDocumentException("Missing mail server");
		if (mailServerStr != null)
			mailServer = InetAddress.getByName(mailServerStr);
		
		mailAddress = in.getAttributeValue("address");
		if (mailAddress == null && enabled == true)
			throw new InvalidDocumentException("Missing mail address");

        // don't fail here - default to recipient
		fromAddress = in.getAttributeValue("fromAddress");
		if (fromAddress == null && enabled == true)
            fromAddress = getFirstAddress(mailAddress);

		// don't fail here - for backwards compatibility, default to no
		backUpStr = in.getAttributeValue("notifyBackUp");
		if (backUpStr == null) {
			notifyBackUp = false;
		} else {
			notifyBackUp = Boolean.valueOf(backUpStr).booleanValue();
		}

		// everything's here, set config
		this.enabled = enabled;
		this.notifyBackUp = notifyBackUp;
		this.mailAddress = mailAddress;
		this.fromAddress = fromAddress;
		setServer(mailServer);
	}

	/* ------------------------------------------------------
		Editor
	------------------------------------------------------ */

	public Editor getEditor() {
		return new MailNotifyEditor();
	}

	private class MailNotifyEditor extends JPanel implements Editor {

		private JCheckBox enabledBox;
		private JTextField serverField;
		private JTextField addressField;
		private JTextField fromAddressField;
		private JCheckBox backUpBox;

		private MailNotifyEditor() {
			// create editable fields
			enabledBox = new JCheckBox("Enabled",enabled);
			String initServerField = "";
			String initAddressField = "";
			String initFromAddressField = "";
			if (mailServer != null) initServerField = mailServer.getHostName();
			if (mailAddress != null) initAddressField = mailAddress;
			if (fromAddress != null) initFromAddressField = fromAddress;

			serverField = new JTextField(initServerField,25);
			addressField = new JTextField(initAddressField,25);
			fromAddressField = new JTextField(initFromAddressField,25);

			backUpBox = new JCheckBox("Notify when a service comes back up",
				notifyBackUp);

			// set default field enable states
			serverField.setEnabled(enabled);
			addressField.setEnabled(enabled);
			backUpBox.setEnabled(enabled);
	
			// set up an action listener on the checkbox
			enabledBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					serverField.setEnabled(enabledBox.isSelected());
					addressField.setEnabled(enabledBox.isSelected());
					fromAddressField.setEnabled(enabledBox.isSelected());
					backUpBox.setEnabled(enabledBox.isSelected());
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
			add(new JLabel("SMTP Server"),c);
			c.gridx = 1; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			add(serverField,c);
			c.gridx = 1; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
			JLabel serverHelp =
				new JLabel("Must be configured to relay for this machine.");
			serverHelp.setFont(serverHelp.getFont().deriveFont(Font.PLAIN,9));
			add(serverHelp,c);

			c.gridx = 0; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("Sender"),c);
			c.gridx = 1; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
			add(fromAddressField,c);
			c.gridx = 1; c.gridy = 4; c.gridwidth = 1; c.gridheight = 1;
			JLabel fromAddressHelp = new JLabel("Optional, defaults to first recipient.");
			fromAddressHelp.setFont(fromAddressHelp.getFont().deriveFont(Font.PLAIN,9));
			add(fromAddressHelp,c);

			c.gridx = 0; c.gridy = 5; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("Recipient(s)"),c);
			c.gridx = 1; c.gridy = 5; c.gridwidth = 1; c.gridheight = 1;
			add(addressField,c);
			c.gridx = 1; c.gridy = 6; c.gridwidth = 1; c.gridheight = 1;
			JLabel addressHelp =
                new JLabel("Separate multiple addresses with commas.");
			addressHelp.setFont(addressHelp.getFont().deriveFont(Font.PLAIN,9));
			add(addressHelp,c);


			c.gridx = 1; c.gridy = 7; c.gridwidth = 1; c.gridheight = 1;
			add(backUpBox,c);
		}

		public void commit() throws UnknownHostException {
			setServer(InetAddress.getByName(serverField.getText()));
			mailAddress = addressField.getText();
            fromAddress = fromAddressField.getText();
			if (fromAddress.length() == 0) fromAddress =
                getFirstAddress(mailAddress);
			enabled = enabledBox.isSelected();
			notifyBackUp = backUpBox.isSelected();
		}

		public String getEditorTitle() {
			return "Configure Mail Notification";
		}
	}
}
