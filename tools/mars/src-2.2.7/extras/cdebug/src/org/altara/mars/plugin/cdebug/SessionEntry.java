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
            
public class SessionEntry {
        
    public static final int MSG = 0;
    public static final int SEND = 1;
    public static final int RECV = 2;

    private static Icon[] icons;

    static {
        icons = new Icon[3];
        icons[0] = IconService.getIcon("mi2_dbgmsg.gif");
        icons[1] = IconService.getIcon("mi2_dbgsend.gif");
        icons[2] = IconService.getIcon("mi2_dbgrecv.gif");
    }

    int type;
    StringBuffer text;
    Date stamp;

    SessionEntry(int type, String text) {
        this.type = type;
        this.text = new StringBuffer(text);
        this.stamp = new Date();
    }

    void append(String text) {
        this.text.append(text);
        this.stamp = new Date();
    }

    int getType() { return type; }

    Icon getIcon() { return icons[type]; }

    Date getStamp() { return stamp; }

    String getText() { return text.toString(); }

}
