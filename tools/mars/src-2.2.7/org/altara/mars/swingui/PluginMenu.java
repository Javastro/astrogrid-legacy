/* MARS Network Monitor Swing User Interface
   Copyright (C) 1999 Brian H. Trammell
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

package org.altara.mars.swingui;

import org.altara.util.*;
import org.altara.mars.*;
import org.altara.mars.engine.*;
import org.altara.mars.plugin.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class PluginMenu extends JMenu {
    
    MarsView view;
    boolean wasEdited;

    public PluginMenu(MarsView view) {
        super("Plugins");

        // stash marsview so we can tell it we've got unsaved changes.
        this.view = view;
        
        // Iterate over plugins to build the menu
        Plugin[] plugins = 
            Main.getMain().getPluginRegistry().getRegisteredPlugins();
        
        for (int i = 0; i < plugins.length; i++) {
            add(new PluginMenuItem(plugins[i]));
        }
    }

    void updateState() {
        Component[] items = getMenuComponents();
        for (int i = 0; i < items.length; i++) {
            ((PluginMenuItem)items[i]).updateState();
        }
    }

    boolean wasEdited() {
        boolean out = wasEdited;
        wasEdited = false;
        return out;
    }

    private class PluginMenuAction extends AbstractAction {   

        Plugin plugin;

        private PluginMenuAction(Plugin plugin) {
            super(plugin.getDisplayName());
            this.plugin = plugin;
        }

        public void actionPerformed(ActionEvent ae) {
            Editor ed = plugin.getEditor();
            if (ed != null) {
                new EditorDialog(view,ed);
                wasEdited = true;
            }
        }
    }

    private class PluginMenuItem extends JCheckBoxMenuItem {
        
        Plugin plugin;

        private PluginMenuItem(Plugin plugin) {
            super(new PluginMenuAction(plugin));
            this.plugin = plugin;
            updateState();
        }

        private void updateState() {
            if (plugin instanceof Enableable) {
                setState(((Enableable)plugin).isEnabled());
            } else {
                setState(false);
            }
        }
    }
}