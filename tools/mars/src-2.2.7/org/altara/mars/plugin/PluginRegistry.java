/* MARS Extension Framework
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

package org.altara.mars.plugin;

import org.altara.util.*;
import org.altara.mars.*;
import org.altara.mars.engine.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import org.jdom.*;

/** Manages the extension framework. PluginRegistry handles loading
	and configuration of plugins dynamically.
*/

public class PluginRegistry {

    public static final String PLUGIN_JAR_PREFIX = 
        "plugin_";
    public static final String PLUGIN_MANIFEST_KEY = 
        "MarsPluginClass";
	
	private LinkedList plugins;

	public PluginRegistry() {
		plugins = new LinkedList();
	}

	public void registerPlugin(Plugin plugin, boolean withUI) {
		Controller controller = Main.getMain().getController();
        // reject Displayable plugins if there's no UI
        if (!withUI && plugin instanceof Displayable) {
            Main.getMain().showStatus("Plugin "+plugin.getClass()+
                                      " not registered because it needs the UI.");
            return;
        }

		// connect the plugin to the controller
		if (plugin instanceof NotificationListener) {
			controller.addNotificationListener(
				(NotificationListener)plugin);
		}
		if (plugin instanceof StatusChangeListener) {
			controller.addStatusChangeListener(
				(StatusChangeListener)plugin);
		}
		if (plugin instanceof ProbeListener) {
			controller.addProbeListener((ProbeListener)plugin);
		}

		// add it to the registry
		plugins.add(plugin);
        Main.getMain().showStatus("Registered plugin "+plugin.getClass());
	}

	public void configAll(Element rootElem) throws Exception {
		Iterator plugins = this.plugins.iterator();
		while (plugins.hasNext()) {
			/* retrieve the configuration element */
			Plugin thisPlugin = (Plugin)plugins.next();
			Element configElem = rootElem.getChild(
				thisPlugin.getElementName(),MarsModel.NAMESPACE);
			/* config the plugin if it's there */
			if (configElem != null) thisPlugin.setConfig(configElem);
		}
	}

	public void mergeConfig(Element rootElem) {
		Iterator plugins = this.plugins.iterator();
		while (plugins.hasNext()) {
			Plugin thisPlugin = (Plugin)plugins.next();
			rootElem.addContent(thisPlugin.getConfig());
		}
	}

	public Plugin[] getRegisteredPlugins() {
		return (Plugin[])plugins.toArray(new Plugin[plugins.size()]);
	}

    public void initPluginDisplay(JTabbedPane pane) {
		Iterator plugins = this.plugins.iterator();
		while (plugins.hasNext()) {
			Object nextPlugin = plugins.next();
            if (nextPlugin instanceof Displayable) {
                ((Displayable)nextPlugin).setPluginDisplay(
                    new PluginDisplay(pane));
            }
        }
    }

	public void loadDynamic(File homeDir, boolean withUI) {
        PluginLoadExceptionHandler pleh = new PluginLoadExceptionHandler();
        PluginFilenameFilter pff = new PluginFilenameFilter();
        Main.getMain().showStatus("Scanning for plugins in "+homeDir.getAbsolutePath()+"...");
        Iterator plugins = ExtensionLoader.scanExtensions(homeDir,
            pff, PLUGIN_MANIFEST_KEY, pleh).iterator();
        while (plugins.hasNext()) {
            try {
                Class nextPluginClass = (Class)plugins.next();
                Plugin nextPlugin = (Plugin)nextPluginClass.newInstance();
                registerPlugin(nextPlugin, withUI);
            } catch (Exception ex) {
                pleh.handleLoadException(null,ex);
            }
        }
    }

    private static class PluginLoadExceptionHandler
            implements LoadExceptionHandler {
        public void handleLoadException(File file, Exception ex) {
            if (file == null) {
                Main.getMain().showStatus("Error loading plugin: "+ex.getMessage());
            } else {
                Main.getMain().showStatus("Error loading plugin from"+
                    file.getAbsolutePath()+": "+ex.getMessage());
            }
            ex.printStackTrace();
        }
    }

    private static class PluginFilenameFilter
            implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.startsWith(PLUGIN_JAR_PREFIX) && name.endsWith(".jar"));
        }
    }
}
