/*$Id: VOExplorerImpl.java,v 1.5 2007/07/23 11:47:17 nw Exp $
 * Created on 30-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ui.voexplorer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.ActionContributionBuilder;
import org.astrogrid.desktop.modules.system.ui.ArMainWindow;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.UIComponentImpl.CloseAction;
import org.astrogrid.desktop.modules.ui.actions.Activity;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.SmartList;
import org.astrogrid.desktop.modules.ui.folders.StaticList;
import org.astrogrid.desktop.modules.ui.folders.XQueryList;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel.LoadEvent;

import ca.odell.glazedlists.EventList;

import com.l2fprod.common.swing.JTaskPane;

/** Main window of voexplorer
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Mar-2005
 *
 */
public class VOExplorerImpl extends UIComponentImpl 
	implements ListSelectionListener, ActionListener, RegistryGooglePanel.LoadListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(VOExplorerImpl.class);

	public VOExplorerImpl( final UIContext context, final ActionContributionBuilder activityBuilder
			,final UIContributionBuilder menuBuilder, EventList folders, RegistryGooglePanel google, QuerySizer sizer) {
		super(context);
		logger.info("Constructing new VOExplorer");
		this.setSize(800, 600);    
		getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.voexplorer");        
		JPanel pane = getMainPanel();
		pane.setBorder(BorderFactory.createEmptyBorder());

		// build the actions menu / pane / popup.
		JPopupMenu popup = new JPopupMenu();
		actionsPanel = new JTaskPane();
		JMenu actions = new JMenu("Actions");
		activities = activityBuilder.buildActions(this,popup,actionsPanel,actions);

		// build the rest of the menuing system.
		JMenuBar menuBar = new JMenuBar();
	      JMenu fileMenu = new JMenu();
	        fileMenu.setText("File");
	        fileMenu.setMnemonic(KeyEvent.VK_F);
	        fileMenu.add(new JSeparator());
	        fileMenu.add( new CloseAction());
	        menuBar.add(fileMenu);
	        menuBar.add(actions); 
		menuBuilder.populateWidget(menuBar,this,ArMainWindow.MENUBAR_NAME);
		int sz = menuBar.getComponentCount();
		
		// splice additional things into help menu.
		JMenu help = menuBar.getMenu(sz-1);
		help.insertSeparator(0);
		JMenuItem sci = new JMenuItem("VOExplorer: Introduction");
		getContext().getHelpServer().enableHelpOnButton(sci,"voexplorer.intro");
			//	"userInterface.voexplorer");
		help.insert(sci,0);		
		
		// add other menus.
		menuBar.add(getContext().createWindowMenu(this),sz-1); // insert before the help menu.
		setJMenuBar(menuBar);

		// main pane.	    
		this.mainPanel = new FlipPanel();
		this.mainButtons = new FlipPanel();

		// main resource view.
		resourcesFolders = new ResourceLists(folders,this);
		resourcesFolders.addListSelectionListener(this);
		resourcesFolders.setName(RESOURCES_VIEW);

		// main view.
		this.google = google;
		google.parent.set(this);
		// attach ourself to this reg chooser, to listen for selection changes.
		google.getCurrentResourceModel().addListSelectionListener(this); // listen to currently selected resource
		google.setPopup(popup);
		google.getNewSearchButton().addActionListener(this);
		google.addLoadListener(this);
		mainPanel.add(google,RESOURCES_VIEW);
		mainButtons.add(google.getToolbar(),RESOURCES_VIEW);

		// build the various editing panels.
		smartEditPanel = new SmartListEditingPanel(this,sizer);
		wireUpEditor(smartEditPanel,EDIT_SMART_VIEW);
		staticEditPanel = new StaticListEditingPanel();
		wireUpEditor(staticEditPanel,EDIT_STATIC_VIEW);
		xqueryEditPanel = new XQueryListEditingPanel(this,sizer);
		wireUpEditor(xqueryEditPanel,EDIT_XQUERY_VIEW);
		
		// assemble all into main window.
		final JScrollPane actionsScroll = new JScrollPane(actionsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		actionsScroll.setBorder(BorderFactory.createEmptyBorder());
		actionsScroll.setMinimumSize(new Dimension(200,200));		
		JScrollPane foldersScroll = new JScrollPane(resourcesFolders,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		foldersScroll.setBorder(BorderFactory.createEmptyBorder());
		foldersScroll.setMinimumSize(new Dimension(200,100));
		// assemble folders and tasks into LHS 
		JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, foldersScroll	,actionsScroll);
		leftPane.setDividerLocation(200);
		leftPane.setDividerSize(7);
		leftPane.setResizeWeight(0.5); // even allocate new space to folders and actions.
	//	leftPane.setBorder(BorderFactory.createMatteBorder(0,0,0,1,Color.GRAY));
		leftPane.setBorder(BorderFactory.createEmptyBorder());
		// combine LHS and RSH
		JSplitPane lrPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPane,mainPanel);
		lrPane.setDividerLocation(200);
		lrPane.setResizeWeight(0.1); //most to the right
		lrPane.setBorder(BorderFactory.createEmptyBorder());
		lrPane.setDividerSize(7);
		pane.add(lrPane,BorderLayout.CENTER); 

		// add buttonbar on top.
		pane.add(mainButtons,BorderLayout.NORTH);

		// finish it all off..
		this.setContentPane(pane);
		this.setTitle("VO Explorer");
		setIconImage(IconHelper.loadIcon("search16.png").getImage());  
		logger.info("New VOExplorer - Completed");
		// finally, display first folder.
		resourcesFolders.setSelectedIndex(0);
		((ResourceFolder)folders.get(0)).display(google);
	}

	/**
	 *integrate an editor into  voexplorer.
	 */
	private void wireUpEditor(EditingPanel editor,String viewName) {
		mainPanel.add(editor,viewName);
		mainButtons.add(new JPanel(),viewName);// just want the toolbar to vanish in the new view.
		editor.getOkButton().addActionListener(this);
		editor.getCancelButton().addActionListener(this);		
	}

	private final FlipPanel mainPanel;
	private final FlipPanel mainButtons;
	private final Activity[] activities;	
	private final JTaskPane actionsPanel;
	public static final String RESOURCES_VIEW = "resources";
	public static final String EDIT_SMART_VIEW = "edit-smart";
	public static final String EDIT_STATIC_VIEW = "edit-static";
	public static final String EDIT_XQUERY_VIEW = "edit-xquery";
	private final ResourceLists resourcesFolders;
	private final RegistryGooglePanel google;
	private final EditingPanel smartEditPanel;
	private final EditingPanel staticEditPanel;
	private final EditingPanel xqueryEditPanel;

	/** override:  create a help menu with additional entries */
	protected JMenu createHelpMenu() {
		System.out.println("called");
		JMenu menu = super.createHelpMenu();
		menu.insertSeparator(0);
		/*
		JMenuItem ref = new JMenuItem("Reference");
		getHelpServer().enableHelpOnButton(ref, "astroscope.menu.reference");
		menu.insert(ref,0);
		 */
		JMenuItem sci = new JMenuItem("VOExplorer: Introduction");
		getContext().getHelpServer().enableHelpOnButton(sci,"voexplorer.intro");
			//	"userInterface.voexplorer");
		menu.insert(sci,0);
		return menu;
	}

	private void clearSelection() {
		for (int i = 0; i < activities.length; i++) {
			activities[i].noneSelected();
		}		
	}

	private void notifyResourceTasks() {
		Transferable tran = google.getSelectionTransferable();
		if (tran == null) {
			clearSelection();
		} else {
				for (int i = 0; i < activities.length ; i++) {
					activities[i].selected(tran);
				}	
		}
	}
	// listens to clicks on resource Folders and registry google.
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return; // ignore
		}
		if (e.getSource() == resourcesFolders) {
			ResourceFolder f =  (ResourceFolder)resourcesFolders.getSelectedValue();
			if (f != null) { 
				clearSelection();
				f.display(google);
				setTitle("VO Explorer - " + f.getName());
			}
		} else {		// from the table 
			notifyResourceTasks();
		}
	}
/// load listener interface.
	public void loadCompleted(LoadEvent e) {
		resourcesFolders.setEnabled(true);
	}

	public void loadStarted(LoadEvent e) {
		resourcesFolders.setEnabled(false);
	}	
//	 action Listener interface. handles ok/cancel from each of the editor panels.
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == google.getNewSearchButton()) {
			SmartList sl = new SmartList();
			editNewSmartList(sl);
		} else if (e.getSource() == smartEditPanel.getOkButton()) {
			acceptEdit(smartEditPanel);
		} else if (e.getSource() == staticEditPanel.getOkButton()) {
			acceptEdit(staticEditPanel);
		} else if (e.getSource() == xqueryEditPanel.getOkButton()) {
			acceptEdit(xqueryEditPanel);			
		} else if (e.getSource() == smartEditPanel.getCancelButton()
				|| e.getSource() == staticEditPanel.getCancelButton()
				|| e.getSource() == xqueryEditPanel.getCancelButton()) {
			showResourceView();
		}
	}	
	// accept an edit from an editing panel, updade list and resources, etc.
	private void acceptEdit(EditingPanel p) {
		p.loadEdits();
		ResourceFolder r = p.getCurrentlyEditing();
		resourcesFolders.store(r);
		//display updated folder contents.
		clearSelection();
		r.display(google);
		setTitle("VO Explorer - " + r.getName());		
		showResourceView();
	}

	public void editNewSmartList(SmartList f) {
		smartEditPanel.getOkButton().setText("Create");
		smartEditPanel.setCurrentlyEditing(f);
		mainPanel.show(EDIT_SMART_VIEW);
		mainButtons.show(EDIT_SMART_VIEW);
		resourcesFolders.setEnabled(false);
		clearSelection(); // removes list of actions.
	}

	public void editExistingSmartList(SmartList f) {
		smartEditPanel.getOkButton().setText("Update");
		smartEditPanel.setCurrentlyEditing(f);
		mainPanel.show(EDIT_SMART_VIEW);
		mainButtons.show(EDIT_SMART_VIEW);
		resourcesFolders.setEnabled(false);
		clearSelection(); // removes list of actions.
	}
	
	public void editNewStaticList(StaticList f) {
		staticEditPanel.getOkButton().setText("Create");
		staticEditPanel.setCurrentlyEditing(f);
		mainPanel.show(EDIT_STATIC_VIEW);
		mainButtons.show(EDIT_STATIC_VIEW);
		resourcesFolders.setEnabled(false);
		clearSelection(); // removes list of actions.
	}

	public void editExistingStaticList(StaticList f) {
		staticEditPanel.getOkButton().setText("Update");
		staticEditPanel.setCurrentlyEditing(f);
		mainPanel.show(EDIT_STATIC_VIEW);
		mainButtons.show(EDIT_STATIC_VIEW);
		resourcesFolders.setEnabled(false);
		clearSelection(); // removes list of actions.
	}
	
	public void editNewQueryList(XQueryList f) {
		xqueryEditPanel.getOkButton().setText("Create");
		xqueryEditPanel.setCurrentlyEditing(f);
		mainPanel.show(EDIT_XQUERY_VIEW);
		mainButtons.show(EDIT_XQUERY_VIEW);
		resourcesFolders.setEnabled(false);
		clearSelection(); // removes list of actions.
	}

	public void editExistingQueryList(XQueryList f) {
		xqueryEditPanel.getOkButton().setText("Update");
		xqueryEditPanel.setCurrentlyEditing(f);
		mainPanel.show(EDIT_XQUERY_VIEW);
		mainButtons.show(EDIT_XQUERY_VIEW);
		resourcesFolders.setEnabled(false);
		clearSelection(); // removes list of actions.
	}

	public void showResourceView() {
		mainPanel.show(RESOURCES_VIEW);
		mainButtons.show(RESOURCES_VIEW);
		resourcesFolders.setEnabled(true);
	}	

// publlic api.
	/** called to display a specific set of resouces in this view.
	 * @param uriList
	 */
	public void displayResources(List uriList) {
		google.displayIdSet(uriList);
	}
	
	public void doOpen(URI ivorn) {
		google.displayIdSet(Collections.singletonList(ivorn));
		
	}
	
	public void doQuery(String query) {
		google.displayQuery(query);
	}



}

