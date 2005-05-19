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

import org.altara.mars.*;
import org.altara.mars.engine.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

/** Translates StatusChangeEvents and the MarsModelListener interface
	into TreeModelEvents. This allows the serviceTree display to update
	itself in a timely manner without imposing the peculiarities of the
	JTree event model on the UI-independent MARS code.
*/

public class ServiceTreeChangeAdapter
		implements StatusChangeListener, MarsModelListener {

	private Controller controller;
	private MarsModel model;
	private JTree serviceTree;
	private DefaultTreeModel stm;

	public ServiceTreeChangeAdapter(Controller controller, MarsModel model,
			JTree serviceTree) {
		// capture instance vars
		this.controller = controller;
		this.model = model;
		this.serviceTree = serviceTree;
		this.stm = (DefaultTreeModel)serviceTree.getModel();

		// register as a change listener with the controller
		// (dynamic changes)
		controller.addStatusChangeListener(this);

		// register as a change listener with the model
		// (static changes)
		model.addMarsModelListener(this);
	}

	void setModel(MarsModel model) {
		this.model = model;
		model.addMarsModelListener(this);
	}

	public void statusChanged(final StatusChangeEvent sce) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				stm.nodeChanged(sce.getService());
			}
		});
	}

	public void hostChanged(final Host host) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				stm.nodeChanged(host);
			}
		});
	}

	public void serviceChanged(final Service service) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				stm.nodeChanged(service);
			}
		});
	}

	public void hostListChanged() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				stm.nodeStructureChanged(model);
			}
		});
	}

	public void serviceListChanged(final Host host) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				stm.nodeStructureChanged(host);
			}
		});
	}

	public void repaintServiceNodes() {
		Iterator hosts = model.getHosts();
		while (hosts.hasNext()) {
			Iterator services = ((Host)hosts.next()).getServices();
			while (services.hasNext()) {
				stm.nodeChanged((Service)services.next());
			}
		}
	}
}
