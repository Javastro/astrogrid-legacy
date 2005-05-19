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

/** Delays the delivery of StatusChangeEvents to notification plugins. Used to
    smooth out notifications on rough networks.
*/

package org.altara.mars.engine;

import java.util.*;
import org.altara.mars.*;

public class Notifier implements StatusChangeListener, ProbeListener {

    Map pendingTable;
    Controller controller;

    public Notifier(Controller controller) {
        this.pendingTable = Collections.synchronizedMap(new HashMap());
        this.controller = controller;
        controller.addStatusChangeListener(this);
        controller.addProbeListener(this);
    }

    public void statusChanged(StatusChangeEvent sce) {
        Service svc = sce.getService();
        int notac = svc.getNotac();
        if (notac > 1) {
            pendingTable.put(svc,new NotificationEntry(sce,notac));
        } else if (notac == 1) {
            controller.notifyStatusChanged(sce);
        }
    }

    public void probeRun(ProbeEvent pe) {
        Service svc = pe.getService();
        NotificationEntry entry = (NotificationEntry)pendingTable.get(svc);
        if (entry != null && --entry.notac <= 1) {
            pendingTable.remove(svc);
            controller.notifyStatusChanged(entry.sce);
        }
    }

    private static class NotificationEntry {
        private StatusChangeEvent sce;
        private int notac;

        private NotificationEntry(StatusChangeEvent sce, int notac) {
            this.sce = sce;
            this.notac = notac;
        }
    }
}
