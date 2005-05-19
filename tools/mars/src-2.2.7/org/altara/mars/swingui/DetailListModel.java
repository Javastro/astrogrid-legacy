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
import java.text.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

/** Presents details about a service's status in a list view.
*/

public class DetailListModel extends AbstractListModel
		implements ProbeListener, TreeSelectionListener {

	private DateFormat df;
	private Service service;
	private ArrayList listCache;

	public DetailListModel() {
		this.listCache = new ArrayList();
		this.service = null;
		updateListCache();
		this.df = new SimpleDateFormat("HH:mm:ss.SSS");
	}

	public int getSize() {
		return listCache.size();
	}

	public Object getElementAt(int index) {
		return listCache.get(index);
	}

	public void probeRun(ProbeEvent pe) {
		// rebuild the list cache if our service is the 
		// one that was probed
		if (pe.getService() == service) {
			updateListCache();
		}
	}

	public void valueChanged(TreeSelectionEvent tse) {
		TreePath leadPath = tse.getNewLeadSelectionPath();
		if (leadPath == null) {
			setService(null);
		} else {
			Object lead = leadPath.getLastPathComponent();
			if (lead == null || !(lead instanceof Service)) {
				setService(null);
			} else {
				setService((Service)lead);
			}
		}
	}

	private void setService(Service service) {
		this.service = service;
		updateListCache();
	}

	Service getService() {
		return this.service;
	}

	private void updateListCache() {
		// remove the previous list contents
		int oldsz = listCache.size();
		if (oldsz > 0) fireIntervalRemoved(this,0,oldsz-1);
	
		// create a new list
		listCache = new ArrayList();
		
		// say very little if no service selected
		if (service == null) {
			listCache.add("No service selected");
			return;
		}

		// grab the service's host and status
		Host host = service.getHost();
		Status status = service.getStatus();

		// present a full service report
		listCache.add("Service "+service.getName()+" on "+host.getName()+
		":"+service.getPort());
		listCache.add("Type: "+service.getSvcType()+"  Status: "+
            status.toString());

		// display last checked and due time, if it's been checked.
		if (status.getTimestamp() > -1) {
			listCache.add("Last checked "+
                          df.format(new Date(status.getTimestamp())));
            listCache.add("Next probe at "+
                          df.format(new Date(status.getTimestamp() +
                                             service.getPeriod())));
		}
        
		// display response time, if available
		if (status.getResponseTime() > -1) {
			listCache.add("Response time was "+status.getResponseTime()+" ms");
		}
		
		// show all properties, too
		Iterator propNames = status.getPropertyNames();
		while (propNames.hasNext()) {
			String propName = (String)propNames.next();
			String propValue = (String)status.getProperty(propName);
			// special multiline treatment for received:
			if (propName.equals("received")) {
				listCache.add(propName+":");
				StringTokenizer lineSplitter =
					new StringTokenizer(propValue,"\015\012");
				while (lineSplitter.hasMoreTokens()) {
					listCache.add(lineSplitter.nextToken());
				}
			} else {
				listCache.add(propName+": "+propValue);
			}
		}

		// signal the re-add of list elements
		if (listCache.size() > 0) fireIntervalAdded(this,0,listCache.size()-1);
	}
}
