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

/** Renders faults in the faultList.
*/

public class FaultListRenderer extends MarsAbstractRenderer {

	private FaultListModel model;

	public FaultListRenderer(FaultListModel model) {
		super();
		this.model = model;
	}

	protected Icon getIconForValue(Object value) {
		if (value instanceof String) {
			// string is either "no faults" or "not monitoring"
			// select based upon controller status
			if (Main.getMain().getController().isActive()) {
				if (model.countHiddenFaults() == 0) {
					return upIcon;
				} else {
					return timeoutIcon;
				}
			} else {
				return unknownIcon;
			}
		} else {
			// Fault list entries are faulted services. Cast the service and
			// get its status to determine the icon.
			return getIconForStatus(((Service)value).getStatus());
		}
	}
		
	protected String getStringForValue(Object value) {
		// check for all is well message.
		if (value instanceof String) return (String)value;
		
		// Fault list entries are faulted services. Get the service and
		// its status.
		Service service = (Service)value;
		Status status = service.getStatus();
		Host host = service.getHost();

		return service.getName()+" on "+host.getName()+
			":"+service.getPort()+": "+status.toString();
	}
}	
