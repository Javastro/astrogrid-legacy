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

/** Implements the serviceTree's context menus.
*/

public class ServiceTreeContextMenuSupport
		extends TreeContextMenuSupport {

	private Action newHostAction;
	private Action editHostAction;
	private Action duplicateHostAction;
	private Action deleteHostAction;
	private Action newServiceAction;
	private Action editServiceAction;
	private Action deleteServiceAction;

	private MarsView marsview;

	public ServiceTreeContextMenuSupport(MarsView marsview, JTree serviceTree) {
		super(serviceTree);
		
		// track the mars view so we can display ourselves in the
		// right place.
		this.marsview = marsview;
		
		// Initialize actions
		initializeActions();
	}

	public void setEditActionsEnabled(boolean enabled) {
		// FIXME temp. hack
		if (!enabled) return;
		newHostAction.setEnabled(enabled);
		editHostAction.setEnabled(enabled);
		deleteHostAction.setEnabled(enabled);
		newServiceAction.setEnabled(enabled);
		editServiceAction.setEnabled(enabled);
		deleteServiceAction.setEnabled(enabled);
	}


	private void initializeActions() {
		// first, create each action
		newHostAction = new AbstractAction("New Host...") {
			public void actionPerformed(ActionEvent ae) {
				new EditorDialog(marsview,
					new HostEditorPanel(findModel(getLastInvoked())));
			}
		};

		editHostAction = new AbstractAction("Edit Host...") {
			public void actionPerformed(ActionEvent ae) {
				new EditorDialog(marsview,
					new HostEditorPanel(findHost(getLastInvoked())));
			}
		};

		duplicateHostAction = new AbstractAction ("Duplicate Host...") {
			public void actionPerformed(ActionEvent ae) {
				Host newHost = findHost(getLastInvoked()).duplicate();
				new EditorDialog(marsview,new HostEditorPanel(newHost));
			}
		};

		deleteHostAction = new AbstractAction("Delete Host") {
			public void actionPerformed(ActionEvent ae) {
				Host host = findHost(getLastInvoked());
				if (confirmDelete(host)) {
					host.getModel().removeHost(host);
                    marsview.postCommitUpdate();
				}
			}
		};

		newServiceAction = new AbstractAction("New Service...") {
			public void actionPerformed(ActionEvent ae) {
				new EditorDialog(marsview,
					new ServiceEditorPanel(findHost(getLastInvoked())));
			}
		};

		editServiceAction = new AbstractAction("Edit Service...") {
			public void actionPerformed(ActionEvent ae) {
				new EditorDialog(marsview,
					new ServiceEditorPanel(findService(getLastInvoked())));
			}
		};

		deleteServiceAction = new AbstractAction("Delete Service") {
			public void actionPerformed(ActionEvent ae) {
				Service service = findService(getLastInvoked());
				if (confirmDelete(service)) {
					service.getHost().removeService(service);
                    marsview.postCommitUpdate();
				}
			}
		};

		// then bind each action to the proper invoked class
		addClassAction(MarsModel.class,newHostAction);
		addClassAction(Host.class,editHostAction);
		addClassAction(Host.class,newServiceAction);
		addClassAction(Host.class,duplicateHostAction);
		addClassAction(Host.class,deleteHostAction);
		addDefaultClassAction(Service.class,editServiceAction);
		addClassAction(Service.class,deleteServiceAction);
	}

	private MarsModel findModel(Object obj) {
		if (obj instanceof MarsModel) return (MarsModel)obj;
		if (obj instanceof Host) return ((Host)obj).getModel();
		if (obj instanceof Service) return ((Service)obj).getHost().getModel();
		return null;
	}

	private Host findHost(Object obj) {
		if (obj instanceof Host) return (Host)obj;
		if (obj instanceof Service) return ((Service)obj).getHost();
		return null;
	}

	private Service findService(Object obj) {
		if (obj instanceof Service) return (Service)obj;
		return null;
	}

	private boolean confirmDelete(Object obj) {
		String deleteMsg = "Are you sure you want to delete ";
		if (obj instanceof Host) {
			deleteMsg += "host "+((Host)obj).getName();
		} else {
			deleteMsg += "service "+((Service)obj).getName();
		}
		deleteMsg += "?";

		int confirmed =
			JOptionPane.showConfirmDialog(marsview,deleteMsg,"Confirm Delete",
			JOptionPane.YES_NO_OPTION);
		return confirmed == JOptionPane.YES_OPTION;
	}
}
