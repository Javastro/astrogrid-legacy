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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class Session extends AbstractTableModel implements ClientDebugger {
    
    SessionList sessionList;
    ArrayList   entries;
    String      name;
    int         lastEntryType;
    boolean     lastEntryEOL;

    static SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss.SSS");

    Session(SessionList sessionList, String name) {
        this.sessionList = sessionList;
        this.lastEntryType = -1;
        this.lastEntryEOL = false;
        this.entries = new ArrayList();
        this.name = "["+fmt.format(new Date())+"] "+ name;
    }

    String getName() {
        return name;
    }
            
    public String toString() {
        return getName();
    }

    private synchronized void append (int type, String text) {
        SessionEntry currentEntry;

        if (type == lastEntryType && type != SessionEntry.MSG) {
            currentEntry = (SessionEntry)entries.get(entries.size() - 1);
        } else {
            lastEntryType = type;
            lastEntryEOL = false;
            currentEntry = new SessionEntry(type,"");
            entries.add(currentEntry);
            fireEntryAdded(entries.size()-1);
        }

        StringTokenizer entryTok = new StringTokenizer(text,"\r\n",true);
        while (entryTok.hasMoreTokens()) {
            String token = entryTok.nextToken();

            if (lastEntryEOL) {
                lastEntryEOL = false;
                currentEntry = new SessionEntry(type,"");
                entries.add(currentEntry);
                fireEntryAdded(entries.size()-1);
            }                

            if (token.equals("\r")) {
                currentEntry.append("<CR>");
            } else if (token.equals("\n")) {
                currentEntry.append("<LF>");
                lastEntryEOL = true;
            } else {
                currentEntry.append(token);
            }
        }
    }

    public void send(String sent) {
        append(SessionEntry.SEND,sent);
    }

    public void receive(String received) {
        append(SessionEntry.RECV,received);
    }

    public void message(String message) {
        append(SessionEntry.MSG,message);
    }

    public void close() {
        sessionList.closeSession(this);
    }

    private void fireEntryAdded(final int index) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    fireTableRowsInserted(index,index);
                }
            });
    }

    private void fireEntryChanged(final int index) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    fireTableRowsUpdated(index,index);
                }
            });
    }


    public int getColumnCount() {
        return 3;
    }

    public String getColumnName(int col) {
        if (col == 0) { return "Time"; }
        else if (col == 1) { return ""; }
        else { return "Message"; }
    }
        
    public Class getColumnClass(int col) {
        if (col == 0) { return String.class; }
        else if (col == 1) { return Icon.class; }
        else { return String.class; }
    }

    public int getRowCount() {
        return entries.size();
    }

    public Object getValueAt(int row, int col) {
        SessionEntry entry = (SessionEntry)entries.get(row);
        if (col == 0) {
            return fmt.format(entry.getStamp());
        } else if (col == 1) {
            return entry.getIcon();
        } else {
            return entry.getText();
        }
    }    
}
