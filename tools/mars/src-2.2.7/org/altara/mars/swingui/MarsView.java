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
import org.jdom.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;

/** MarsView is the root of the MARS Swing user interface.
*/

public class MarsView extends JFrame implements StatusView {

	// Menu bar actions
	private Action newAction;
	private Action openAction;
	private Action saveAction;
	private Action startAction;
	private Action stopAction;
	private Action viewAllAction;
	private Action viewNoneAction;
	private boolean lastSaveActionState;

	// List and tree models
	private FaultListModel flm;
	private DefaultTreeModel stm;
	private ServiceTreeChangeAdapter stca;
	private DetailListModel dlm;

    // Plugins menu
    private PluginMenu pluginMenu;

	// Panels
	private ChangeListPanel clp;

    // Status bar
    private Queue statusQ;
    private JTextField statusbar;
    private javax.swing.Timer statusQTimer;

    private static final int SB_THROTTLE = 2;
    private static final int SB_DELAY = 2000;

	// Context menu support classes
	private ServiceTreeContextMenuSupport stcms;
	private FaultListContextMenuSupport flcms;

    // Key action support classes
    private ServiceTreeKeyActionSupport stkas;

	// Lists and trees
	private JTree serviceTree;

    // Main tabbed pane, for plugin display management
    private JTabbedPane tabPane;

	// Save/Open files
	private File curDir;
	private File curSaveFile;

	public MarsView (File curSaveFile, int frameWidth, int frameHeight) {
		// set the window's title
		super("Mars "+Main.VERSION);

		// Get the controller (understood to be initialized
		// and to have a bound MarsModel) - we'll use it a lot
		// below
		final Controller controller = Main.getMain().getController();
		MarsModel model = controller.getModel();

        // initialize menu/toolbar actions
        initializeActions();

        // initialize menubar
        setJMenuBar(initializeMenuBar());

        // initialize toolbar
        JToolBar toolbar = initializeToolBar();

		// set file defaults
        setDefaultSaveFile(curSaveFile);

		// create a fault list and bind it to the controller
		flm = new FaultListModel(this);
		controller.addStatusChangeListener(
			new StatusChangeThreadAdapter(flm));
		final JList faultList = new JList(flm);
		JScrollPane faultListSP = new JScrollPane(faultList);
		// set up various list options
		faultListSP.setBorder(new TitledBorder("Current Faults"));
		// set up the renderer for the fault list
		FaultListRenderer flr = new FaultListRenderer(flm);
		faultList.setCellRenderer(flr);
		// make it a change listener
		model.addMarsModelListener(flm);

		// add context menus to the fault list
		flcms = new FaultListContextMenuSupport(faultList);

		// create a change log panel 
		clp = new ChangeListPanel(controller);

		// create a service tree bound to the model
		stm = new DefaultTreeModel(controller.getModel());
		serviceTree = new JTree(stm);
		JScrollPane serviceTreeSP = new JScrollPane(serviceTree);
		// set up various tree options
		serviceTree.setRootVisible(false);
		serviceTreeSP.setBorder(new TitledBorder("Hosts & Services"));
		// add a renderer
		ServiceTreeRenderer str = new ServiceTreeRenderer();
		serviceTree.setCellRenderer(str);
		// use a ServiceTreeChangeAdapter to route change events to the
		// correct places...
		stca = new ServiceTreeChangeAdapter(controller,model,serviceTree);

		// add context menus to the service tree
		stcms = new ServiceTreeContextMenuSupport(this,serviceTree);

        // add key actions to the service tree
		stkas = new ServiceTreeKeyActionSupport(this,serviceTree);

		// bind the service tree selection to the fault list
		faultList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				if (lse.getValueIsAdjusting()) return;
				Object selObj = faultList.getSelectedValue();
				if (!(selObj instanceof Service)) return;
				TreePath selPath = ((Service)selObj).getTreePath();
				serviceTree.setSelectionPath(selPath);
				serviceTree.makeVisible(selPath);
			}
		});

		// create a detail view bound to the controller
		// and the service tree
		dlm = new DetailListModel();
		controller.addProbeListener(new ProbeThreadAdapter(dlm));
		serviceTree.addTreeSelectionListener(dlm);
		JList detailList = new JList(dlm);
		JScrollPane detailListSP = new JScrollPane(detailList);
		// set up various list options
		detailListSP.setBorder(new TitledBorder("Service Detail"));
		// no renderer as of yet - set font to size 10, though.
		detailList.setFont(MarsAbstractRenderer.cellFont);

		// create a configuration panel

		// add a status change listener to modify the titlebar
		controller.addStatusChangeListener(new StatusChangeThreadAdapter(
			new StatusChangeListener() {
			public void statusChanged(StatusChangeEvent sce) {
				updateTitleBar();
			}
		}));

		// ensure the controller is stopped and set appropriate
		// default enable states
		controller.stop();
		stopAction.setEnabled(false);
		saveAction.setEnabled(false);
		stcms.setEditActionsEnabled(true);

		// stuff it all together into a tab pane
		JSplitPane rightPane = new JSplitPane(
			JSplitPane.VERTICAL_SPLIT, faultListSP, detailListSP);
		JSplitPane frontPane = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT, serviceTreeSP, rightPane);
		tabPane = new JTabbedPane(JTabbedPane.BOTTOM);
		tabPane.addTab("Current",frontPane);
		tabPane.addTab("History",clp);

        // add tabs for plugins
        Main.getMain().getPluginRegistry().initPluginDisplay(tabPane);

        // create the status bar
        statusQ = new Queue();
        statusbar = new JTextField("Starting...");
        statusbar.setEditable(false);

        controller.addProbeListener(new ProbeListener() {
            public void probeRun(ProbeEvent ev) {
                showStatus("Checked "+ev.getService());
            }
        });

        statusQTimer =
            new javax.swing.Timer(SB_DELAY,new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                displayNextStatus();
            }
        });
        statusQTimer.setCoalesce(true);
        statusQTimer.setRepeats(true);
        statusQTimer.start();

		// now put it into the window
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolbar,BorderLayout.NORTH);
		getContentPane().add(tabPane,BorderLayout.CENTER);
        getContentPane().add(statusbar,BorderLayout.SOUTH);

		// set up the window close listener
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
                Main.getMain().maybeQuit();
			}
		});

		// rock and roll
        this.pack();
		this.setSize(frameWidth, frameHeight);
        serviceTree.requestFocus();
		this.show();
		rightPane.setDividerLocation(0.5);
		frontPane.setDividerLocation(0.4);

        // check for outstanding open file requests
        String delayedOpenFile = 
            System.getProperty("org.altara.mars.macintosh.OpenFile");
        if (delayedOpenFile != null) openModel(new File(delayedOpenFile));
	}

    public void showStatus(String status) {
        statusQ.enqueue(status);
    }

    private void displayNextStatus() {
        String nextStatus = (String)statusQ.nb_dequeue();
        if (nextStatus == null) {
            if (Main.getMain().getController().isActive()) {
                statusbar.setText("Monitoring.");
            } else {
                statusbar.setText("Idle.");
            }
        } else {
            // check throttle
            int backlog = statusQ.size();
            if (backlog > SB_THROTTLE) {
                statusQTimer.setDelay((SB_THROTTLE * SB_DELAY)/backlog);
            }
            // display next status
            statusbar.setText(nextStatus);
        }
    }

	void updateTitleBar() {
		if (!Main.getMain().getController().isActive())
			setTitle("Mars "+Main.VERSION);
		else if (flm.getFaultCount() == 0) setTitle("Mars [No Faults]");
		else if (flm.getFaultCount() == 1) setTitle("Mars [1 Fault]");
		else setTitle("Mars ["+flm.getFaultCount()+" Faults]");
	}

    void postCommitUpdate() {
        markUnsavedChanges(true);
        if (pluginMenu.wasEdited()) {
            pluginMenu.updateState();
        }
    }

    private void initializeActions() {
		newAction = 
			new AbstractAction("New") {
			public void actionPerformed(ActionEvent ae) {
				newModel();
			}
		};

		openAction = 
			new AbstractAction("Open...") {
			public void actionPerformed(ActionEvent ae) {
				openModel();
			}
		};

		saveAction =
			new AbstractAction("Save As...") {
			public void actionPerformed(ActionEvent ae) {
				saveModel();
			}
		};

		startAction =
			new AbstractAction("Start") {
			public void actionPerformed(ActionEvent ae) {
				startController();
			}
		};

		stopAction = 
			new AbstractAction("Stop") {
			public void actionPerformed(ActionEvent ae) {
				stopController();
			}
		};

		viewAllAction = 
			new AbstractAction("Expand Hosts") {
			public void actionPerformed(ActionEvent ae) {
				viewAllServices();
			}
		};

		viewNoneAction = 
			new AbstractAction("Collapse Hosts") {
			public void actionPerformed(ActionEvent ae) {
				viewNoServices();
			}
		};
    }

    private JMenuBar initializeMenuBar() {
        // determine command mask
        int cmdMask = KeyEvent.CTRL_MASK;
        String isMacString = System.getProperty("org.altara.mars.macintosh");
        if (isMacString != null && new Boolean(isMacString).booleanValue()) {
            cmdMask = KeyEvent.META_MASK;
        } 

        // create the menubar
        JMenuBar menuBar = new JMenuBar();

        // create file menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = fileMenu.add(newAction);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,cmdMask));
        JMenuItem openItem = fileMenu.add(openAction);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,cmdMask));
        JMenuItem saveItem = fileMenu.add(saveAction);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,cmdMask));
        menuBar.add(fileMenu);

        // create control menu
        JMenu controlMenu = new JMenu("Control");
        JMenuItem startItem = controlMenu.add(startAction);
        startItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,cmdMask));
        JMenuItem stopItem = controlMenu.add(stopAction);
        stopItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,cmdMask));
        menuBar.add(controlMenu);

        // create view menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem viewAllItem = viewMenu.add(viewAllAction);
        viewAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,cmdMask));
        JMenuItem viewNoneItem = viewMenu.add(viewNoneAction);
        viewNoneItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,cmdMask));
        menuBar.add(viewMenu);

        // create plugins menu
        pluginMenu = new PluginMenu(this);
        menuBar.add(pluginMenu);

        // return menu bar
        return menuBar;
    }

	private JToolBar initializeToolBar() {
		// create the toolbar
		JToolBar toolbar = new JToolBar();

		// add actions to the toolbar
		JButton newButton = toolbar.add(newAction);
        newButton.setToolTipText("New Configuration");
        newButton.setIcon(IconService.getIcon("mi2_tbnew.gif"));
        newButton.setText(null);

		JButton openButton = toolbar.add(openAction);
        openButton.setToolTipText("Open Configuration File...");
        openButton.setIcon(IconService.getIcon("mi2_tbopen.gif"));
        openButton.setText(null);
        
        JButton saveButton = toolbar.add(saveAction);
        saveButton.setToolTipText("Save Configuration File As...");
        saveButton.setIcon(IconService.getIcon("mi2_tbsave.gif"));
        saveButton.setText(null);

		toolbar.addSeparator();

		JButton startButton = toolbar.add(startAction);
        startButton.setToolTipText("Start Monitoring");
        startButton.setIcon(IconService.getIcon("mi2_tbrun.gif"));
        startButton.setText(null);

		JButton stopButton = toolbar.add(stopAction);
        stopButton.setToolTipText("Stop Monitoring");
        stopButton.setIcon(IconService.getIcon("mi2_tbstop.gif"));
        stopButton.setText(null);

		toolbar.addSeparator();

        JButton viewAllButton = toolbar.add(viewAllAction);
        viewAllButton.setToolTipText("View All Services");
        viewAllButton.setIcon(IconService.getIcon("mi2_tbviewall.gif"));
        viewAllButton.setText(null);

        JButton viewNoneButton = toolbar.add(viewNoneAction);
        viewNoneButton.setToolTipText("Hide All Services");
        viewNoneButton.setIcon(IconService.getIcon("mi2_tbviewnone.gif"));
        viewNoneButton.setText(null);

        // all done
		return toolbar;
	}


	private void newModel() {
		// don't chuck current model without giving
		// the user a chance to save
		if (!confirmDiscardChanges()) return;

		// create the new model 
		Main.getMain().newModel();
		// ...and connect it to the UI
		MarsModel newModel = Main.getMain().getController().getModel();
		stca.setModel(newModel);
		stm.setRoot(newModel);
		newModel.addMarsModelListener(flm);

		// disable the save button
		markUnsavedChanges(false);
	}

	private void openModel() {
		// don't chuck current model without giving
		// the user a chance to save
		if (!confirmDiscardChanges()) return;

		// get the file to open
		File inFile = selectFileOpen();
		// make sure a file was selected
		if (inFile == null) return;

        // delegate to the non-choosing version of openModel
        openModel(inFile);
    }
    
    public boolean openModel(File inFile) { 
		try {
			// try to load the new model
			Main.getMain().loadConfig(inFile);
			// and connect it to the UI
			MarsModel newModel = Main.getMain().getController().getModel();
			stca.setModel(newModel);
			stm.setRoot(newModel);
			newModel.addMarsModelListener(flm);
			// disable the save button
			markUnsavedChanges(false);
		} catch (JDOMException ex) {
			// report load exception to the user.
			JOptionPane.showMessageDialog(this,
				inFile.getName()+" could not be opened.\n"+
				"The file is not a valid XML file.",
				"File Open Error",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
            return false;
		} catch (InvalidDocumentException ex) {
			// report load exception to the user.
			JOptionPane.showMessageDialog(this,
				inFile.getName()+" could not be opened.\n"+
				"The file contains the following error: "+ex.getMessage(),
				"File Open Error",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
            return false;
		} catch (UnknownHostException ex) {
			// report load exception to the user.
			JOptionPane.showMessageDialog(this,
				inFile.getName()+" could not be opened.\n"+
				"The host address "+ex.getMessage()+" could not be resolved.\n"+
				"This may indicate a problem with your domain name servers.",
				"File Open Error",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
            return false;
		} catch (Exception ex) {
			// report catch-all load exception to the user
			JOptionPane.showMessageDialog(this,
				inFile.getName()+" could not be opened.\n"+
				"A plugin could not be properly configured\n"+
				"because an unknown error occurred:\n"+ex.getMessage(),
				"File Open Error",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
            return false;
		}
        return true;
	}

	private boolean saveModel() {
		// select a file to save to, default to last save
		File outFile = selectFileSave();
		if (outFile == null) return false;

		try {
			// write the config out
			Main.getMain().saveConfig(outFile);
			// disable the save button
			markUnsavedChanges(false);
			// signal success
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
				outFile.getName()+" could not be saved.\n"+
				"You may not have permission to write the file, or the file\n"+
				"may be open. Try saving to a different file or directory.",
				"File Save Error",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
	}

	private void startController() {
		startAction.setEnabled(false);
		stopAction.setEnabled(true);
		newAction.setEnabled(false);
		openAction.setEnabled(false);
		lastSaveActionState = saveAction.isEnabled();
		saveAction.setEnabled(false);
		stcms.setEditActionsEnabled(false);
		Main.getMain().getController().start();
		// now do necessary UI updates
		stca.repaintServiceNodes();
		flm.updateListCache();
		updateTitleBar();
	}

	private void stopController() {
		stopAction.setEnabled(false);
		startAction.setEnabled(true);
		newAction.setEnabled(true);
		openAction.setEnabled(true);
		saveAction.setEnabled(lastSaveActionState);
		stcms.setEditActionsEnabled(true);
		Main.getMain().getController().stop();
		// now do necessary UI updates
		stca.repaintServiceNodes();
		flm.updateListCache();
		updateTitleBar();
	}

    private void viewAllServices() {
        int rowCount = serviceTree.getRowCount();
        for (int i = rowCount-1; i >= 0; i--) {
            serviceTree.expandRow(i);
        }
    }

    private void viewNoServices() {
        int rowCount = serviceTree.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            serviceTree.collapseRow(i);
        }
    }

	void markUnsavedChanges(boolean changed) {
		if (Main.getMain().getController().isActive()) {
			lastSaveActionState = changed;
		} else {
			saveAction.setEnabled(changed);
		}
        String isMacString = 
            System.getProperty("org.altara.mars.macintosh");
        if (isMacString != null && 
            new Boolean(isMacString).booleanValue()) {
            getRootPane().putClientProperty("windowModified",
                                            new Boolean(changed));
        }
	}

	boolean hasUnsavedChanges() {
		if (Main.getMain().getController().isActive()) {
			return lastSaveActionState;
		} else {
			return saveAction.isEnabled();
		}
	}

	public boolean confirmDiscardChanges() {
		// if the model hasn't changed, assume tacit approval
		if (!hasUnsavedChanges()) return true;

		// otherwise, ask the user
		String changeMsg = "There are unsaved changes to the configuration.\n"+
							"Do you wish to save them before proceeding?";
		int confirmed = JOptionPane.showConfirmDialog(this,changeMsg,
			"Unsaved Changes",JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.WARNING_MESSAGE);
		switch (confirmed) {
			case JOptionPane.YES_OPTION:
				return saveModel();
			case JOptionPane.NO_OPTION:
				return true;
			case JOptionPane.CANCEL_OPTION:
			default:
				return false;
		}
	}

	private File selectFileOpen() {
		// create a file chooser
		JFileChooser chooser = new JFileChooser(curDir);
		// ask for a file to open
		int option = chooser.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			// remember selected directory
			curDir = chooser.getCurrentDirectory();
			// and return selected file to open
			return chooser.getSelectedFile();
		} else {
			// no file selected, return null
			return null;
		}
	}

	private File selectFileSave() {
		// create a file chooser
		JFileChooser chooser = new JFileChooser(curDir);
		if (curSaveFile != null) {
            chooser.setSelectedFile(curSaveFile);
        }
		// overwrite check loop
		while (true) {
			int option = chooser.showSaveDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				// check for existing selected file
				if (chooser.getSelectedFile().exists()) {
					int confirm = JOptionPane.showConfirmDialog(this,
						"Overwrite existing "+
						chooser.getSelectedFile().getName()+"?",
						"Confirm File Overwrite",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
					if (confirm == JOptionPane.YES_OPTION) {
						// confirmed overwrite, so no repeat
						break;
					}
				} else {
					// file doesn't exist, so don't repeat
					break;
				}
			} else {
				// not approved, so return null
				return null;
			}
		}

		// remember selected directory and savefile
		curDir = chooser.getCurrentDirectory();
		curSaveFile = chooser.getSelectedFile();
		// return file
		return curSaveFile;
	}

	public void setDefaultSaveFile(File file) {
        try {
            this.curSaveFile = file.getCanonicalFile();
            this.curDir = this.curSaveFile.getParentFile();
        } catch (IOException ignored) {}
	}

	public static class SplashScreen extends JWindow implements StatusView {
        JTextArea statusArea;
        JButton okButton;

		public SplashScreen(boolean atStartup) {
			super();

			// get the splash graphic
			JLabel splashLabel = new JLabel(IconService.getIcon("splash.gif"));

			// add it to the frame
			getContentPane().setLayout(new BorderLayout());
            getContentPane().add(splashLabel,BorderLayout.NORTH);

            if (atStartup) {
                // create a status field
                statusArea = new JTextArea();
                statusArea.setFont(new Font("SansSerif",Font.PLAIN,10));
                statusArea.setRows(6);
                statusArea.setLineWrap(true);
                statusArea.setWrapStyleWord(true);
                statusArea.setEditable(false);
                getContentPane().add(new JScrollPane(statusArea),
                                     BorderLayout.SOUTH);
            } else {
                // create an OK button
                okButton = new JButton(new AbstractAction("OK") {
                        public void actionPerformed(ActionEvent ae) {
                            SplashScreen.this.dispose();
                        }
                    });
                getContentPane().add(okButton, BorderLayout.SOUTH);
            }

			// display it
			this.pack();
			this.centerWindow();
			this.show();
		}

        public void showStatus(final String status) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    statusArea.append(status+"\n");
                    statusArea.setCaretPosition(statusArea.getText().length()-1);
                }
            });
        }
	
		private void centerWindow() {
			Rectangle virtualBounds = new Rectangle();
			GraphicsEnvironment ge =
				GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] gs = ge.getScreenDevices();
			
            // only do the main screen...
            GraphicsConfiguration[] gc = gs[0].getConfigurations();
            for (int i=0; i < gc.length; i++) {
                virtualBounds = virtualBounds.union(gc[i].getBounds());
            }
	
			int cx = (int) virtualBounds.getWidth()/2;
			int cy = (int) virtualBounds.getHeight()/2;
			this.setLocation(cx - this.getWidth()/2,
								cy - this.getHeight()/2);
		}
	}
}
