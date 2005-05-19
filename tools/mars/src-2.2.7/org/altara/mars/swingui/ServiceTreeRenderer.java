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

/** Renders tree cells in the serviceTree.
*/

public class ServiceTreeRenderer extends MarsAbstractRenderer {

	public ServiceTreeRenderer() {
		super();
	}

	protected Icon getIconForValue(Object value) {
		// return a host icon if it's a MarsModel
		// this should never be displayed.
		if (value instanceof MarsModel) return okHostIcon;

        boolean isActive = Main.getMain().getController().isActive();

		if (value instanceof Host) {
            if (isActive) {
                return getIconForHost((Host)value);
            } else {
                return okHostIcon;
            }
        } else {
            if (isActive) {
			    return getIconForStatus(((Service)value).getStatus());
            } else {
		        return serviceIcon;
            }
		}
	}

	protected String getStringForValue(Object value) {
		// return a root node label if it's a MarsModel
		// this should never be displayed.
		if (value instanceof MarsModel) return "<root>";

		// return hostname if the node is a host
		if (value instanceof Host) return ((Host)value).getName();

		// it's a service. build a general service description no matter what
		Service service = (Service)value;
		// show status if running
		String out = service.getName();
		if (Main.getMain().getController().isActive()) {
			out += ": "+service.getStatus().toString();
		}
		return out;
	}
}
