/* MARS Network Monitor
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2002-2003 Leapfrog Research & Development, LLC

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

package org.altara.mars;

import java.text.*;
import java.util.*;
import java.net.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import gnu.getopt.*;

import org.altara.util.*;
import org.altara.mars.engine.*;
import org.altara.mars.swingui.*;
import org.altara.mars.plugin.*;

public class Main {

	/* Application-wide constaints */
	public static final String VERSION = Version.VERSION;
	public static final String BUILD_NUMBER = Version.BUILD_NUMBER;
	public static final String RELEASE_DATE = Version.RELEASE_DATE;

    /* Date format constant */
    public static final DateFormat df = 
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	private static Main current;

	public static final void main (String[] args) throws Exception {
		// default options
		String homeDirName = ".";
		String configName = null;
        String geomStr = null;
		boolean startUI = true;
		// parse command line options
		int c;
		LongOpt[] longopts = {
			new LongOpt("nogui", LongOpt.NO_ARGUMENT, null, 'n'),
			new LongOpt("home", LongOpt.REQUIRED_ARGUMENT, null, 'h'),
            new LongOpt("geom", LongOpt.REQUIRED_ARGUMENT, null, 'g')
		};
		Getopt g = new Getopt("mars",args,"h:g:n",longopts);
		while ((c = g.getopt()) != -1) {
			switch (c) {
            case 'h':
                homeDirName = g.getOptarg();
                break;
            case 'n':
                startUI = false;
                break;
            case 'g':
                geomStr = g.getOptarg();
            default:
                break;
			}
		}
		if (g.getOptind() < args.length) {
			configName = args[g.getOptind()];
		}

		// check for silly argument combinations
		if (startUI == false && configName == null) {
			System.err.println(
				"It would be useless to start Mars with neither "+
				"a configuration file\nnor a user interface. Exiting.");
			System.exit(1);
		}

        if (startUI == false && geomStr != null) {
			System.err.println(
				"Ignoring geometry "+geomStr+" without user interface.");
		}

		// check for invalid home directory
		File homeDir = new File(homeDirName);
		if (!homeDir.isDirectory()) {
			System.err.println(
				"Invalid Mars home directory "+homeDirName+
				"\nExiting.");
			System.exit(1);
		}

		// create a new Main and make it current
		new Main(homeDir,configName,geomStr,startUI);
	}

	// singleton children
	private Controller controller;
	private PluginRegistry pluginRegistry;
    private MarsView view;

    // current status view (status bar, etc.)
    private StatusView statusView;

	private Main(File homeDir, String configName, String geomStr,
                 final boolean startUI)
		throws Exception {
		// Make myself current immediately (initialization will need to
		// access singletons through this instance)
		current = this;

		// Display splash screen if UI desired
		if (startUI) {
			// initialize icon service - we'll need it later, too
			IconService.initialize(MarsView.class,"icons");
			displaySplashScreen();
		} else {
			displaySplashText();
		}

		// Initialize the Controller
		controller = new Controller();

        // Hook up the status view to the controller
        controller.addStatusChangeListener(new StatusChangeListener() {
            public void statusChanged(StatusChangeEvent ev) {
                showStatus(ev.getService()+": "+ev.getNewStatus());
            }
        });

		// Attach XML-defined probes
		XmlProbeFactory.registerAll(homeDir);

		// Dynamically load probes in the home directory
		ProbeFactory.loadDynamic(homeDir);

		// Initialize the PluginRegistry
		pluginRegistry = new PluginRegistry();

		// Dynamically load plugins in the home directory
		pluginRegistry.loadDynamic(homeDir, startUI);

		// Set up a model - either from a file, or from scratch
        File configFile = null;

		if (configName == null) {
			newModel();
		} else {
            configFile = new File(configName);
			loadConfig(configFile);
		}

		// Initialize the Swing UI, if desired
		if (startUI) {
            int frameWidth, frameHeight;
			// Parse geometry
            if (geomStr == null) {
                frameWidth = 500;
                frameHeight = 400;
            } else {
                StringTokenizer tok = new StringTokenizer(geomStr,"x");
                frameWidth = Integer.parseInt(tok.nextToken());
                frameHeight = Integer.parseInt(tok.nextToken());
            }
            
            // set default config file for save dialog
			if (configFile == null) {
				configFile = new File(System.getProperty("user.home"),
                                      "mars-config.xml");
			}

            // create window
            view = new MarsView(configFile, frameWidth, frameHeight);
            
            // hook the view up to the status mechanism
            setStatusView(view);
            showStatus("Welcome to Mars "+VERSION+" build "+
                       BUILD_NUMBER+" ("+RELEASE_DATE+")");
		} else {
			// Otherwise, start the controller
            showStatus("Welcome to Mars "+VERSION+" build "+
                       BUILD_NUMBER+" ("+RELEASE_DATE+")");
			controller.start();
		}
	}

    /* Shutdown management */

    public void maybeQuit() {
        if (view != null) {
            if (!view.confirmDiscardChanges()) return;
            view.setVisible(false);
        }
        controller.stop();
        System.exit(0);
    }            

    /* Status management */

    public void showStatus(String status) {
        if (statusView != null) statusView.showStatus(status);
    }

    private void setStatusView(StatusView statusView) {
        this.statusView = statusView;
    }

	/* Splash screens */

	private void displaySplashScreen() {
		// show the splash screen
		final MarsView.SplashScreen splashScreen =
			new MarsView.SplashScreen(true);
        setStatusView(splashScreen);
		// set up a timer to kill it
		javax.swing.Timer splashTimer =
			new javax.swing.Timer(5000,new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				splashScreen.dispose();
			}
		});
		splashTimer.setCoalesce(false);
		splashTimer.setRepeats(false);
		splashTimer.start();
	}

	private void displaySplashText() {
		System.out.print(
			"Mars "+VERSION+" build "+BUILD_NUMBER+
            " starting without GUI...\n"+
			"Mars is (c) 2002-2004 Leapfrog R&D, LLC (http://www.lfrd.com).\n"+
			"        (c) 1999 Brian H. Trammell.\n"+
			"Mars is released with NO WARRANTY, and is distributed under\n"+
			"the terms of the GNU General Public License, \n"+
			"available at http://www.gnu.org/copyleft/gpl.html\n\n");
        setStatusView(new StatusView() {
                public void showStatus(String status) {
                    System.out.println("["+df.format(new Date())+"] "+status);
                }
        });
	}

	/* Configuration management */

	public void newModel() {
        MarsModel oldModel = controller.getModel();
        if (oldModel != null) oldModel.clearMarsModelListeners();
		controller.setModel(new MarsModel());
	}

	public void loadConfig(File file) throws Exception {
        // store the old model so we can disconnect it from its listeners
        MarsModel oldModel = controller.getModel();
		// load the file into an XML document
		Element config = new SAXBuilder().build(file).getRootElement();
		// create a new model
		MarsModel newModel = MarsModel.fromJDOMElem(config);
        // disconnect the old model
        if (oldModel != null) oldModel.clearMarsModelListeners();
		// attach it to the controller
		controller.setModel(newModel);
		// configure plugins
		pluginRegistry.configAll(config);
	}

	public void saveConfig(File file) throws IOException {
		// get the model configuration
		Element config = controller.getModel().toJDOMElem(false);
        Document configDoc = new Document(config);
		// merge plugin configs into the element
		pluginRegistry.mergeConfig(config);
		// write it out
        Writer configWriter = new OutputStreamWriter(
            new FileOutputStream(file),"UTF8");
		XMLOutputter out = new XMLOutputter(
            org.jdom.output.Format.getPrettyFormat());
        out.output(configDoc, configWriter);
        configWriter.close();
	}

	/* Singleton access */
	public static Main getMain() {
		return current;
	}

    public MarsView getView() {
        return view;
    }

	public Controller getController() {
		return controller;
	}

	public PluginRegistry getPluginRegistry() {
		return pluginRegistry;
	}
}
