/*$Id: FileExplorerImpl.java,v 1.8 2007/09/11 12:12:15 nw Exp $
 * Created on 30-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.ActivityFactory;
import org.astrogrid.desktop.modules.system.ui.ArMainWindow;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.FileManagerInternal;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.UIComponentImpl.CloseAction;
import org.astrogrid.desktop.modules.ui.actions.Activity;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationEvent;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationListener;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;

import ca.odell.glazedlists.EventList;

import com.l2fprod.common.swing.JOutlookBar;
import com.l2fprod.common.swing.JTaskPane;

/** Main window of fileexplorer - assembles together the contributions.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Mar-2005
 *
 */
public class FileExplorerImpl extends UIComponentImpl implements FileManagerInternal, NavigationListener{

	public FileExplorerImpl( final UIContext context,  final ActivityFactory activityBuilder
			,final UIContributionBuilder menuBuilder
			, TypesafeObjectBuilder uiBuilder) {
        super(context);
 
        this.setSize(800, 600);    
        	
		    getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.fileExplorer");        
		    JPanel pane = getMainPanel();
		    pane.setBorder(BorderFactory.createEmptyBorder());

		    // build the actions menu / pane / popup.
		    activities = activityBuilder.create(this);

	// build the rest of the menuing system.
		    JMenuBar menuBar = new JMenuBar();
	          JMenu fileMenu = new JMenu();
	            fileMenu.setText("File");
	            fileMenu.setMnemonic(KeyEvent.VK_F);
	            fileMenu.add(new JSeparator());
	            fileMenu.add( new CloseAction());
	            menuBar.add(fileMenu);
	            
	        menuBar.add(activities.getMenu()); 
	        
		    menuBuilder.populateWidget(menuBar,this,ArMainWindow.MENUBAR_NAME);
		    int sz = menuBar.getComponentCount();
			
	        JMenu help = menuBar.getMenu(sz-1);
	        help.insertSeparator(0);
	        JMenuItem sci = new JMenuItem("FileExplorer: Introduction");
	        getContext().getHelpServer().enableHelpOnButton(sci,"fileexplorer.intro");
	        help.insert(sci,0); 			
			
			menuBar.add(getContext().createWindowMenu(this),sz-1); // insert before the help menu.
		    setJMenuBar(menuBar);		

		    view = uiBuilder.createStorageView(this,activities);
            JComponent foldersPanel = view.getHierarchiesPanel();
		    JComponent mainPanel = view.getMainPanel();
		    JComponent mainButtons = view.getMainButtons();
		 
		    // assemble all into main window.
		    final JScrollPane scrollPane = new JScrollPane(activities.getTaskPane(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    scrollPane.setBorder(BorderFactory.createEmptyBorder());
			// assemble folders and tasks into LHS 
		    
			JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, foldersPanel
					,scrollPane);
		    leftPane.setDividerLocation(400);
		    leftPane.setBorder(BorderFactory.createMatteBorder(0,0,0,1,Color.GRAY));

		    // combine LHS and RSH
		    JSplitPane lrPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPane,mainPanel);
		    lrPane.setDividerLocation(200);
		    lrPane.setBorder(BorderFactory.createEmptyBorder());
		    pane.add(lrPane,BorderLayout.CENTER); 
		    
		    // add buttonbar on top.
		    pane.add(mainButtons,BorderLayout.NORTH);
		    
		    // finish it all off..
			this.setContentPane(pane);
			this.setTitle("File Explorer");
			view.getNavigator().addNavigationListener(this);
		  //@todo find a new icon  setIconImage(IconHelper.loadIcon("search16.png").getImage());  
    }
	    
// event listener.
  
	
	   /** override:  create a help menu with additional entries */
	   protected JMenu createHelpMenu() {
		JMenu menu = super.createHelpMenu();
		menu.insertSeparator(0);
	/*
		JMenuItem ref = new JMenuItem("Reference");
		getHelpServer().enableHelpOnButton(ref, "astroscope.menu.reference");
		menu.insert(ref,0);
		*/
		JMenuItem sci = new JMenuItem("FileExplorer Help");
		getContext().getHelpServer().enableHelpOnButton(sci, "userInterface.fileExplorer");
		menu.insert(sci,0);
		return menu;
	}

	private final ActivitiesManager activities;
    private StorageView view;
	
	public void show(final FileObject fileToShow) {
	    view.getNavigator().move(fileToShow);
	}

    public void moved(NavigationEvent e) {
        setTitle("File Explorer - " + view.getNavigator().current().getName());
    }

    public void moving() {
    }



}

