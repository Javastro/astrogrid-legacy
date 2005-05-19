/* MARS XMPP Notification Plugin
   Copyright (C) 2004 Leapfrog Research & Development, LLC

   Written by Robert Fuller, borrowing from the Mail Notification 
   Plugin included with Mars.

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License as
	published by the Free Software Foundation; either version 2 of the
	License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, it is available at
	http:///www.gnu.org/copyleft/gpl.html, or by writing to the Free
	Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
	02111-1307, USA.
*/

package org.altara.mars.plugin.xmppnotify;

import org.jdom.*;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.altara.mars.*;
import org.altara.mars.engine.*;
import org.altara.mars.plugin.*;
import org.altara.mars.swingui.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class XmppNotifyPlugin implements Plugin, NotificationListener,
                                         Enableable {

	private static final DateFormat df = DateFormat.getTimeInstance();

	private String xmppServer;
	private String xmppAddress;
	private String xmppUsername;
	private String xmppPassword;
	private String messagePrefix;
	int xmppPort = 5222;
	private boolean enabled;
	private boolean notifyBackUp;
	private XMPPConnection chatClient = null;


	public XmppNotifyPlugin() {
		xmppServer = null;
		xmppAddress = null;
		xmppPort = 5222;
		enabled = false;
		notifyBackUp = false;
	}

	public String getElementName() {
		return "xmppnotify";
	}

	public String getDisplayName() {
		return "XMPP Notification";
	}

    public boolean isEnabled() {
        return enabled;
    }

	public void notifyStatusChanged(StatusChangeEvent sce) {
		// do nothing if not enabled
		if (!enabled) return;

		// check one: we just went down.
		if ((sce.getOldStatus().getCode() == Status.UNKNOWN 
		    || sce.getOldStatus().getCode() == Status.UP) &&
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
		String message = serviceDesc+" is "+newStatus.toString()+
			" at "+df.format(new Date(newStatus.getTimestamp()))+".";
		try{
			sendMessage(message);
			Main.getMain().showStatus(service+": XMPP notification sent");
		} catch (XMPPException e) {
			Main.getMain().showStatus("XMPP notification failure: "+e);
			e.printStackTrace();
		}
	}
	
	void sendMessage(String msg) throws XMPPException{
		connect();
		String message = messagePrefix+" [Mars] "+msg;
        StringTokenizer addrTok = new StringTokenizer(xmppAddress,",");
        while (addrTok.hasMoreTokens()) {
            String thisAddr = addrTok.nextToken();
            chatClient.createChat(thisAddr).sendMessage(message);
        }
	}   
	
	void connect() throws XMPPException{
		if(chatClient == null || !chatClient.isConnected()){
            /*
            System.err.println("XMPP connect to "+
                               xmppServer+":"+xmppPort+
                               "\n as "+xmppUsername+":"+xmppPassword+
                               "\n sending to "+xmppAddress);
            */
			chatClient = new XMPPConnection(xmppServer, xmppPort);
			chatClient.login(xmppUsername, xmppPassword);
		}
	}


	/* ------------------------------------------------------
		XML config serialization
	------------------------------------------------------ */

	public Element getConfig() {
		Element out = new Element(getElementName(),MarsModel.NAMESPACE);
		out.setAttribute("enabled",String.valueOf(enabled));
		out.setAttribute("notifyBackUp",String.valueOf(notifyBackUp));
		if (xmppServer != null)
			out.setAttribute("server",xmppServer);
			out.setAttribute("port",""+xmppPort);
		if (xmppAddress != null)
			out.setAttribute("address",xmppAddress);
      if (xmppUsername != null)
			out.setAttribute("username", xmppUsername);
		if (xmppPassword != null)
			  out.setAttribute("password", xmppPassword);
		if (messagePrefix != null)
			  out.setAttribute("msgPrefix", messagePrefix);
		return out;
	}

	public void setConfig(Element in) throws
			InvalidDocumentException, XMPPException {
		boolean enabled, notifyBackUp;
		String server = null;
		String recipients = null;
		String username = null;
		String password = null;
		String msgPrefix = null;
		String sport = null;
		
		String backUpStr = null;

		String enabledStr = in.getAttributeValue("enabled");
		if (enabledStr == null)
			throw new InvalidDocumentException("Missing xmpp enabled");
		enabled = Boolean.valueOf(enabledStr).booleanValue();

		String xmppServerStr = in.getAttributeValue("server");
		if (xmppServerStr == null && enabled == true)
			throw new InvalidDocumentException("Missing xmpp server");
		if (xmppServerStr != null)
			server = xmppServerStr;

	    sport = in.getAttributeValue("port");
	    if(sport == null && enabled == true)
			throw new InvalidDocumentException("Missing xmpp port");
	    try{
	    	xmppPort = Integer.parseInt(sport);
	    }catch(NumberFormatException e){
			throw new InvalidDocumentException("Invalid xmpp port ["+sport+"]");
	    }

		recipients = in.getAttributeValue("address");
		if (recipients == null && enabled == true)
			throw new InvalidDocumentException("Missing xmpp address");

		username = in.getAttributeValue("username");
		if (username == null && enabled == true)
				throw new InvalidDocumentException("Missing xmpp username");

		password = in.getAttributeValue("password");
		if (password == null && enabled == true)
				throw new InvalidDocumentException("Missing xmpp password");

        msgPrefix = in.getAttributeValue("msgPrefix");
        if (msgPrefix == null){
            msgPrefix = "";
        }

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
		this.xmppAddress = recipients;
		this.xmppUsername = username;
		this.xmppPassword = password;
		this.messagePrefix = msgPrefix;
		this.xmppServer = server;
        if (enabled)
            this.testConnection();
       
	}
	
	void testConnection() throws XMPPException{
		sendMessage("Mars "+Main.VERSION+" build "+
                    Main.BUILD_NUMBER+ " ("+Main.RELEASE_DATE+
                    ") XMPP Notification Plugin starting.");
	}

	/* ------------------------------------------------------
		Editor
	------------------------------------------------------ */

	public Editor getEditor() {
		return new XmppNotifyEditor();
	}

	private class XmppNotifyEditor extends JPanel implements Editor {

		private JCheckBox enabledBox;
		private JTextField serverField;
		private JTextField addressField;
		private JTextField usernameField;
		private JTextField passwordField;
		private JTextField prefixField;
		private JTextField portField;
		private JCheckBox backUpBox;

		private XmppNotifyEditor() {
			// create editable fields
			enabledBox = new JCheckBox("Enabled",enabled);
			String initServerField = "";
			String initAddressField = "";
			String initUsernameField = "";
			String initPasswordField = "";
			String initMsgPrefixField = "";
			String initPortField = ""+xmppPort;
			
			if (xmppServer != null) initServerField = xmppServer;
			if (xmppAddress != null) initAddressField = xmppAddress;
			if (xmppUsername != null) initUsernameField = xmppUsername;
			if (xmppPassword != null) initPasswordField = xmppPassword;
			if (messagePrefix != null) initMsgPrefixField = messagePrefix;

			serverField = new JTextField(initServerField,25);
			addressField = new JTextField(initAddressField,25);
			usernameField = new JTextField(initUsernameField,25);
			passwordField = new JTextField(initPasswordField,25);
			prefixField = new JTextField(initMsgPrefixField,25);
			portField = new JTextField(initPortField,5);
			

			backUpBox = new JCheckBox("Notify when a service comes back up",
				notifyBackUp);

			// set default field enable states
			serverField.setEnabled(enabled);
			addressField.setEnabled(enabled);
			usernameField.setEnabled(enabled);
			passwordField.setEnabled(enabled);
			prefixField.setEnabled(enabled);
			backUpBox.setEnabled(enabled);
			portField.setEnabled(enabled);
	
			// set up an action listener on the checkbox
			enabledBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					serverField.setEnabled(enabledBox.isSelected());
					addressField.setEnabled(enabledBox.isSelected());
					usernameField.setEnabled(enabledBox.isSelected());
					passwordField.setEnabled(enabledBox.isSelected());
					prefixField.setEnabled(enabledBox.isSelected());
					backUpBox.setEnabled(enabledBox.isSelected());
					portField.setEnabled(enabledBox.isSelected());
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
			add(new JLabel("XMPP Server"),c);
			c.gridx = 1; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			add(serverField,c);

			c.gridx = 0; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("XMPP Port"),c);
			c.gridx = 1; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
			add(portField,c);
			c.gridx = 1; c.gridy = 4; c.gridwidth = 1; c.gridheight = 1;
			JLabel portHelp =
				new JLabel("XMPP Server port number; usually 5222.");
			portHelp.setFont(portHelp.getFont().deriveFont(Font.PLAIN,9));
			add(portHelp,c);

			c.gridx = 0; c.gridy = 5; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("Username"),c);
			c.gridx = 1; c.gridy = 5; c.gridwidth = 1; c.gridheight = 1;
			add(usernameField,c);

			c.gridx = 0; c.gridy = 7; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("Password"),c);
			c.gridx = 1; c.gridy = 7; c.gridwidth = 1; c.gridheight = 1;
			add(passwordField,c);

			c.gridx = 0; c.gridy = 9; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("Prefix"),c);
			c.gridx = 1; c.gridy = 9; c.gridwidth = 1; c.gridheight = 1;
			add(prefixField,c);
			c.gridx = 1; c.gridy = 10; c.gridwidth = 1; c.gridheight = 1;
			JLabel prefixHelp = new JLabel("String to be prefixed to each message.");
			prefixHelp.setFont(prefixHelp.getFont().deriveFont(Font.PLAIN,9));
			add(prefixHelp,c);

			c.gridx = 0; c.gridy = 11; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("Recipient(s)"),c);
			c.gridx = 1; c.gridy = 11; c.gridwidth = 1; c.gridheight = 1;
			add(addressField,c);
			c.gridx = 1; c.gridy = 12; c.gridwidth = 1; c.gridheight = 1;
			JLabel addressHelp =
                new JLabel("Separate multiple addresses with commas.");
			addressHelp.setFont(addressHelp.getFont().deriveFont(Font.PLAIN,9));
			add(addressHelp,c);

			c.gridx = 1; c.gridy = 13; c.gridwidth = 1; c.gridheight = 1;
			add(backUpBox,c);
		}

		public void commit() throws XMPPException {
			xmppServer = serverField.getText();
			xmppAddress = addressField.getText();
            xmppUsername = usernameField.getText();
            xmppPassword = passwordField.getText();
            xmppPort = Integer.parseInt(portField.getText());
            messagePrefix = prefixField.getText();
			enabled = enabledBox.isSelected();
			notifyBackUp = backUpBox.isSelected();

            /* restart connection */
            if (chatClient != null && chatClient.isConnected()) 
                chatClient.close();
            if (enabled) 
                testConnection();
		}

		public String getEditorTitle() {
			return "Configure XMPP Notification";
		}
	}

}