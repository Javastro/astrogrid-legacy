/* MARS Apple MRJ Integration Plugin
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

package org.altara.mars.plugin.mac;

import org.jdom.*;
import org.altara.util.*;
import org.altara.mars.*;
import org.altara.mars.plugin.*;
import org.altara.mars.swingui.*;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.apple.eawt.*;

public class MacIntegrationPlugin extends ApplicationAdapter 
    implements Plugin, Enableable {

    public MacIntegrationPlugin() {

        // Snag the application
        Application app = Application.getApplication();

        // Set up us the menu bar
        app.removePreferencesMenuItem();
        app.setEnabledAboutMenu(true);
        System.setProperty("apple.laf.useScreenMenuBar","true");

        // Add self as listener
        app.addApplicationListener(this);

        // Tell the rest of Mars we're on a Mac
        System.setProperty("org.altara.mars.macintosh","true");
    }

    public String getElementName() {
        return "macintegration";
    }

    public String getDisplayName() {
        return "Mac OS X Integration";
    }

    public boolean isEnabled() {
        return true;
    }

    public Element getConfig() {
        Element out = new Element(getElementName(),MarsModel.NAMESPACE);
        return out;
    }

    public void setConfig(Element in) {
    }

    public Editor getEditor() {
        return new MacIntegrationEditor();
    }

    private class MacIntegrationEditor extends JPanel implements Editor {   
        private MacIntegrationEditor() {
            // set up the layout manager
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = c.NORTHWEST; c.fill = c.BOTH;
			c.weightx = 1.0; c.weighty = 0.0;
			c.insets = new Insets(4,4,6,4);
	
			// lay out the panel
			c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("Mac OS X Integration needs no configuration."),c);
        }

        public void commit() {
        }

        public String getEditorTitle() {
            return getDisplayName();
        }
    }

    public void handleAbout(ApplicationEvent ae) {
        ae.setHandled(true);
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new MarsView.SplashScreen(false);
                }
            });
    }

    public void handleQuit(ApplicationEvent ae) {
        Main.getMain().maybeQuit();
        ae.setHandled(false);
    }

    public void handleOpenFile(ApplicationEvent ae) {
        Main main = Main.getMain();
        MarsView view = main.getView();
        if (main == null || view == null) {
            // drop off an openModel request for Main
            // hoo boy is this some hacky stuff or what
            System.setProperty("org.altara.mars.macintosh.OpenFile",
                               ae.getFilename());
        } else {
            // view is active; confirm discard of changes
            if (!view.confirmDiscardChanges()) {
                ae.setHandled(false);
            } else {
                ae.setHandled(view.openModel(new File(ae.getFilename())));
            }
        }
    }
}