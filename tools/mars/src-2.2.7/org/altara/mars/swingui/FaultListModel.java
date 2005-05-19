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

/** FaultListModel contains a list of service faults. It does this by
	watching list state changes (it is a StatusChangeListener) and adding
	each service in transition to a bad state to a list. This provides the
	user with an at-a-glance list of problems on her network.
*/

public class FaultListModel extends AbstractListModel
		implements StatusChangeListener, MarsModelListener {

	private TreeSet faultedServices;
	private HashSet hiddenFaults;
	private ArrayList listCache;
	private MarsView view;
	
	public FaultListModel(MarsView view) {
		this.faultedServices = new TreeSet(new HostServiceOrderComparator());
		this.hiddenFaults = new HashSet();
		this.listCache = new ArrayList();
		this.view = view;
		updateListCache();
	}

	public int getSize() {
		return listCache.size();
	}

	public Object getElementAt(int index) {
		return listCache.get(index);
	}

	public void statusChanged(StatusChangeEvent sce) {
		if (sce.getNewStatus().isFault()) {
			faultedServices.add(sce.getService());
		} else {
			faultedServices.remove(sce.getService());
			hiddenFaults.remove(sce.getService());
		}
		updateListCache();
	}

	void hideFault(Service service) {
		if (faultedServices.contains(service)) {
			faultedServices.remove(service);
			hiddenFaults.add(service);
		}
		updateListCache();
	}

	void showAllFaults() {
		faultedServices.addAll(hiddenFaults);
		hiddenFaults.clear();
		updateListCache();
	}

	int countHiddenFaults() {
		return hiddenFaults.size();
	}

	void updateListCache() {
		int oldsz = listCache.size();
		listCache = new ArrayList();
		if (oldsz > 0) fireIntervalRemoved(this,0,oldsz-1);
		if (Main.getMain().getController().isActive()) {
			listCache.addAll(faultedServices);
			if (listCache.size() == 0) {
				if (countHiddenFaults() == 0) {
					listCache.add("No faults detected.");
				} else {
					listCache.add("No faults shown ("+countHiddenFaults()+
						" hidden).");
				}
			}
		} else {
			listCache.add("Not currently monitoring.");
		}
		fireIntervalAdded(this,0,listCache.size()-1);
		// now update the title bar
		view.updateTitleBar();
	}

	int getFaultCount() {
		return faultedServices.size();
	}

	public void hostChanged(Host host) {
		updateListCache();
	}

	public void serviceChanged(Service service) {
		updateListCache();
	}
	
	public void hostListChanged() {
		pruneDefunctServices();
	}

	public void serviceListChanged(Host host) {
		pruneDefunctServices();
	}

	private void pruneDefunctServices() {
		// retain only faulted services in the all service list
		MarsModel model = Main.getMain().getController().getModel();
		Set services = model.getServiceSet();
		faultedServices.retainAll(services);
		hiddenFaults.retainAll(services);
		// rebuild the list.
		updateListCache();
	}

	private static class HostServiceOrderComparator implements Comparator {
		public int compare(Object a, Object b) {
			Service sa = (Service)a;
			Service sb = (Service)b;

			if (sa.getHost() == sb.getHost()) {
				return sa.getName().compareTo(sb.getName());
			} else {
				return sa.getHost().getName().compareTo(sb.getHost().getName());
			}
		}
	}
}
