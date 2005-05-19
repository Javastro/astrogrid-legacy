/* MARS Swing UI Notification Plugin
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

package org.altara.mars.plugin.swingnotify;

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

public class SwingNotifyPlugin implements Plugin, NotificationListener,
                                          Enableable, Displayable {
	
	private boolean enabled;
	private boolean notifyBackUp;
	private boolean beep;

    private DialogWorker dialogWorker;

	public SwingNotifyPlugin() {
		enabled = false;
		notifyBackUp = false;

        dialogWorker = new DialogWorker();
        dialogWorker.start();
	}

	public String getElementName() {
		return "swingnotify";
	}

	public String getDisplayName() {
		return "UI Notification";
	}

    public boolean isEnabled() {
        return enabled;
    }

    public void setPluginDisplay(PluginDisplay display) {
        // this is a no-op. we only implement Displayable to 
        // tell the plugin registry we should't be loaaded on
        // --nogui.
    }

	public void notifyStatusChanged(StatusChangeEvent sce) {
		// do nothing if not enabled
		if (!enabled) return;

		// check one: we just went down.
		if (sce.getOldStatus().getCode() == Status.UP &&
			sce.getNewStatus().isFault()) {
			dialogWorker.addEvent(sce);
		} else 
		// check two: we came back up, and we care about such things
		if (notifyBackUp && sce.getOldStatus().isFault() &&
			sce.getNewStatus().getCode() == Status.UP) {
			dialogWorker.addEvent(sce);
		}
	}

	private void showNotification (StatusChangeEvent sce) {
		Status newStatus = sce.getNewStatus();
		Service service = sce.getService();
		String svcDesc = service.getName()+" on "+service.getHost().getName();
		String typeDesc = newStatus.isFault() ? "Fault" : "Notice";
		int msgType = newStatus.isFault() ? JOptionPane.WARNING_MESSAGE :
			JOptionPane.INFORMATION_MESSAGE;
		String message = svcDesc + " is " +newStatus.toString();
		String title = typeDesc + ": "+svcDesc;
		if (newStatus.isFault() && beep) Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null,message,title,msgType);
	}

	/* ------------------------------------------------------
		XML config serialization
	------------------------------------------------------ */

	public Element getConfig() {
		Element out = new Element(getElementName(),MarsModel.NAMESPACE);
		out.setAttribute("enabled",String.valueOf(enabled));
		out.setAttribute("notifyBackUp",String.valueOf(notifyBackUp));
		out.setAttribute("beep",String.valueOf(beep));
		return out;
	}

	public void setConfig(Element in) throws UnknownHostException,
			InvalidDocumentException {
		boolean enabled, notifyBackUp, beep;

		String enabledStr = in.getAttributeValue("enabled");
		if (enabledStr == null) 
			throw new InvalidDocumentException("Missing UI enabled");
		enabled = Boolean.valueOf(enabledStr).booleanValue();

		String backUpStr = in.getAttributeValue("notifyBackUp");
		if (backUpStr == null)
			throw new InvalidDocumentException("Missing UI backUp");
		notifyBackUp = Boolean.valueOf(backUpStr).booleanValue();

		String beepStr = in.getAttributeValue("notifyBackUp");
		if (beepStr == null)
			throw new InvalidDocumentException("Missing UI beep");
		beep = Boolean.valueOf(beepStr).booleanValue();

		// everything's here, set config
		this.enabled = enabled;
		this.notifyBackUp = notifyBackUp;
		this.beep = beep;
	}

	/* ------------------------------------------------------
		Editor
	------------------------------------------------------ */

	public Editor getEditor() {
		return new SwingNotifyEditor();
	}

	private class SwingNotifyEditor extends JPanel implements Editor {

		private JCheckBox enabledBox;
		private JCheckBox backUpBox;
		private JCheckBox beepBox;

		private SwingNotifyEditor() {
			// create editable fields
			enabledBox = new JCheckBox("Enabled",enabled);
			backUpBox = new JCheckBox("Notify when a service comes back up",
				notifyBackUp);
			beepBox = new JCheckBox("Beep on fault", beep);

			// set default field enable states
			backUpBox.setEnabled(enabled);
			beepBox.setEnabled(enabled);
	
			// set up an action listener on the checkbox
			enabledBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					backUpBox.setEnabled(enabledBox.isSelected());
					beepBox.setEnabled(enabledBox.isSelected());
				}
			});

			// set up the layout manager
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = c.NORTHWEST; c.fill = c.BOTH;
			c.weightx = 1.0; c.weighty = 0.0;
			c.insets = new Insets(4,4,6,4);
	
			// lay out the panel
			c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
			add(enabledBox,c);
			c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			add(beepBox,c);
			c.gridx = 0; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
			add(backUpBox,c);
		}

		public void commit() throws UnknownHostException {
			enabled = enabledBox.isSelected();
			notifyBackUp = backUpBox.isSelected();
			beep = beepBox.isSelected();
		}

		public String getEditorTitle() {
			return "Configure UI Notification";
		}
	}

    private class DialogWorker extends Worker {

        private Queue sceQ;

        private DialogWorker() {
            super("UI Notification Worker");
            sceQ = new Queue();
        }

        public void work() throws InterruptedException {
            showNotification((StatusChangeEvent)sceQ.dequeue());
        }

        public synchronized void addEvent(StatusChangeEvent sce) {
            sceQ.enqueue(sce);
        }
    }
}
