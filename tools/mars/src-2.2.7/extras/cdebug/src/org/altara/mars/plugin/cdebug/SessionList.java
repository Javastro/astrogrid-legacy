/* MARS Network Monitoring Engine Debugger
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2004 Leapfrog Research & Development, LLC

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

package org.altara.mars.plugin.cdebug;

import org.jdom.*;
import org.altara.util.*;
import org.altara.mars.*;
import org.altara.mars.engine.*;
import org.altara.mars.plugin.*;
import org.altara.mars.swingui.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class SessionList extends AbstractListModel {

    LinkedList activeSessions;
    LinkedList sessionHistory;
    ArrayList sessionCache;
    int horizon;

    SessionList(int horizon) {
        activeSessions = new LinkedList();
        sessionHistory = new LinkedList();
        sessionCache = new ArrayList();
        this.horizon = horizon;
    }

    Session newSession(String name) {
        Session out = new Session(this,name);
        addActiveSession(out);
        return out;
    }

    void closeSession(Session session) {
        makeSessionInactive(session);
    }

    void setHorizon(int horizon) {
        this.horizon = horizon;
    }

    private void updateSessionCache() {
        sessionCache.clear();
        sessionCache.addAll(activeSessions);
        sessionCache.addAll(sessionHistory);
    }

    private void addActiveSession(final Session session) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    activeSessions.addFirst(session);
                    updateSessionCache();
                    fireIntervalAdded(this,0,0);
                }
            });
    }

    private void makeSessionInactive(final Session session) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    boolean didHorizon = false;
                    int maxHorizonIndex = 0, minHorizonIndex = 0;

                    /* move inactive down the list */
                    int activeIndex = activeSessions.indexOf(session);
                    activeSessions.remove(activeIndex);
                    int inactiveIndex = activeSessions.size();
                    sessionHistory.addFirst(session);

                    /* process horizon (may have multiple over the horizon
                       if the horizon was set lower */
                    if (sessionHistory.size() > horizon) {
                        didHorizon = true;
                        maxHorizonIndex = 
                            activeSessions.size() + 
                            sessionHistory.size() - 1;
                        while (sessionHistory.size() > horizon) {
                            sessionHistory.removeLast();
                            minHorizonIndex = 
                                activeSessions.size() + 
                                sessionHistory.size();
                        }
                    }

                    /* update cache */
                    updateSessionCache();
                    
                    /* fire list events */
                    fireIntervalRemoved(SessionList.this,
                                        activeIndex,activeIndex);
                    fireIntervalAdded(SessionList.this,
                                      inactiveIndex,inactiveIndex);
                    if (didHorizon) {
                        fireIntervalRemoved(SessionList.this,
                                            minHorizonIndex,maxHorizonIndex);
                    }
                }
            });
    }

    private void fireSessionAdded(final int index) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    updateSessionCache();
                    fireIntervalAdded(this,index,index);
                }
            });
    }

    private void fireSessionRemoved(final int index) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    updateSessionCache();
                    fireIntervalRemoved(this,index,index);
                }
            });
    }


    public int getSize() {
        return sessionCache.size();
    }
        
    public Object getElementAt(int row) {
        return sessionCache.get(row);
    }
}
