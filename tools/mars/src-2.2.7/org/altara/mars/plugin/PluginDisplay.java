/* Mars Extension Framework
   Copyright (C) 2002-2004 Leapfrog Research & Development, LLC

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

package org.altara.mars.plugin;

import javax.swing.*;

public class PluginDisplay {
    
    JTabbedPane pane;
    JComponent  component;
    String      title;
    boolean     shown;
    int         index;

    PluginDisplay(JTabbedPane pane) {
        this.pane = pane;
        this.shown = false;
    }

    public synchronized void show() {
        if (shown) return;
        if (title == null || component == null)
            throw new RuntimeException("Attempt to show() "+
                                       "uninitialized plugin display");
        pane.addTab(title,component);
        index = pane.getTabCount() - 1;
        shown = true;
    }

    public synchronized void hide() {
        if (!shown) return;
        pane.removeTabAt(index);
        shown = false;
    }

    public synchronized void setComponent(JComponent component) {
        this.component = component;
        if (shown) {
            pane.setComponentAt(index,component);
        }
    }

    public synchronized void setTitle(String title) {
        this.title = title;
        if (shown) {
            pane.setTitleAt(index,title);
        }
    }
}