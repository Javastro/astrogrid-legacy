/*$Id: FileExplorerImpl.java,v 1.1 2007/05/02 15:38:28 nw Exp $
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
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.ui.ActionContributionBuilder;
import org.astrogrid.desktop.modules.system.ui.ArMainWindow;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.actions.Activity;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;

import ca.odell.glazedlists.EventList;

import com.l2fprod.common.swing.JOutlookBar;
import com.l2fprod.common.swing.JTaskPane;

/** Main window of fileexplorer - assembles together the contributions.
 * a copy of the old window of voexplorer - to allow me to refactor that in freedom.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Mar-2005
 *
 */
public class FileExplorerImpl extends UIComponentImpl implements  ExplorerWindow
{

	public FileExplorerImpl( final UIContext context,  final ActionContributionBuilder activityBuilder
			,final UIContributionBuilder menuBuilder, EventList folders, FileSystemManager vfs) {
        super(context);
 
        this.setSize(800, 800);    
        	
		    getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.fileExplorer");        
		    JPanel pane = getMainPanel();
		    pane.setBorder(BorderFactory.createEmptyBorder());

		    // build the actions menu / pane / popup.
		    popup = new JPopupMenu();
			actionsPanel = new JTaskPane();
			JMenu actions = new JMenu("Actions");
		    activities = activityBuilder.buildActions(this,popup,actionsPanel,actions);

	// build the rest of the menuing system.
		    JMenuBar menuBar = new JMenuBar();
		    menuBuilder.populateWidget(menuBar,this,ArMainWindow.MENUBAR_NAME);
		//	menuBar.add(createHelpMenu());
			int sz = menuBar.getComponentCount();
			menuBar.add(actions,sz-2); // insert before help.
			menuBar.add(getContext().createWindowMenu(this),sz-1); // insert before the help menu.
		    setJMenuBar(menuBar);		

		    // main pane.	    
		    StorageView view = new StorageView(this,folders,vfs);
		    JComponent foldersPanel = view.getHierarchiesPanel();
		    JComponent mainPanel = view.getMainPanel();
		    JComponent mainButtons = view.getMainButtons();
		 
		    // assemble all into main window.
		    final JScrollPane scrollPane = new JScrollPane(actionsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
		  //@todo find a new icon  setIconImage(IconHelper.loadIcon("search16.png").getImage());  
    }

	private final JTaskPane actionsPanel;
	    
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
		// listens to clicks on resource Folders and registry google.
//		public void valueChanged(ListSelectionEvent e) {
//			if (e.getSource() == resourcesFolders) {
//				ResourceFolder f =  (ResourceFolder)resourcesFolders.getSelectedValue();
//				if (f != null) { 
//					clearSelection();
//					setTitle("VO Explorer - " + f.getName());
//					f.display(google);
//				}
//			} else if (! e.getValueIsAdjusting()) {
//				// from the table - ignore if it's adjusting.
//				notifyResourceTasks();
//			}
//		}
	public void clearSelection() {
		for (int i = 0; i < activities.length; i++) {
			activities[i].noneSelected();
		}		
	}

	private final Activity[] activities;
	private final JPopupMenu popup;
	public JPopupMenu getPopupMenu() {
		return popup;
	}

	public void setSelection(Transferable tran) {
		if (tran == null) {
			for (int i = 0; i < activities.length; i++) {
				activities[i].noneSelected();
			}
		} else {
			for (int i = 0; i < activities.length ; i++) {
				activities[i].selected(tran);
			}
		}		
	}
}

