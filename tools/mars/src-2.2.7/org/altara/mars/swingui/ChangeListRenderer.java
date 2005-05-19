/* MARS Network Monitor Swing User Interface
   Copyright (C) 1999 Brian H. Trammell
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

package org.altara.mars.swingui;

import org.altara.mars.*;
import org.altara.mars.engine.*;
import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

/** Renders status change events in the changeList.
*/

public class ChangeListRenderer extends MarsAbstractRenderer {

	private DateFormat df;
	
	public ChangeListRenderer() {
		super();
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}

	protected Icon getIconForValue(Object value) {
		// Change list entries are StatusChangeEvents; for the purposes
		// of finding an icon, we're only interested in the new status.
		return getIconForStatus(((StatusChangeEvent)value).getNewStatus());
	}

	protected String getStringForValue(Object value) {
		// Change list entries are displayed much like fault list entries -
		// but we care about the service and its new status.
		Service service = ((StatusChangeEvent)value).getService();
		Status status = ((StatusChangeEvent)value).getNewStatus();
		Host host = service.getHost();

		return	service.getName()+" on "+host.getName()+
				":"+service.getPort()+": "+status.toString()+
				" at "+df.format(new Date(status.getTimestamp()));
	}
}
