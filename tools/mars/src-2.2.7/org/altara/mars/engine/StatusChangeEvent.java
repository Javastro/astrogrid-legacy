/* MARS Network Monitoring Engine
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

package org.altara.mars.engine;

import java.util.*;
import org.altara.mars.*;

/** Represents a change of a given service's status. Used to notify user-side
	components (e.g. the user interface and notification engine) that a
	service's status has changed.
*/

public class StatusChangeEvent extends ProbeEvent {

	private Status oldStatus;

	public StatusChangeEvent (Service service, Status oldStatus,
			Status newStatus) {
		super(service, newStatus);
		this.oldStatus = oldStatus;
	}

	public Status getOldStatus() {
		return oldStatus;
	}
}
