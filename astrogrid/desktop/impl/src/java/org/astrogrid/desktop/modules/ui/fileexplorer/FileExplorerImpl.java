/*$Id: FileExplorerImpl.java,v 1.18 2009/03/26 18:04:11 nw Exp $
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
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.ActivityFactory;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.FileManagerInternal;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.actions.DeleteFilesActivity;
import org.astrogrid.desktop.modules.ui.actions.DuplicateActivity;
import org.astrogrid.desktop.modules.ui.actions.InfoActivity;
import org.astrogrid.desktop.modules.ui.actions.MessagingScavenger;
import org.astrogrid.desktop.modules.ui.actions.RenameActivity;
import org.astrogrid.desktop.modules.ui.actions.SimpleDownloadActivity;
import org.astrogrid.desktop.modules.ui.actions.TaskRunnerActivity;
import org.astrogrid.desktop.modules.ui.actions.ViewInBrowserActivity;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationEvent;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationListener;

/** File Explorer implementation.
 *  - assembles together the contributions.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Mar-2005
 *
 */
public class FileExplorerImpl extends UIComponentImpl implements FileManagerInternal, NavigationListener{

	public FileExplorerImpl( final UIContext context,  final ActivityFactory activityBuilder
			,final TypesafeObjectBuilder uiBuilder) {
        super(context,"File Explorer","window.fileexplorer");
 
        this.setSize(800, 600);    
        	
	    final JPanel pane = getMainPanel();

		    // build the actions menu / pane
		    acts = activityBuilder.create(this,new Class[]{
		        //    BuildQueryActivity.class - non functional
		            TaskRunnerActivity.class
		            ,ViewInBrowserActivity.class
		            ,SimpleDownloadActivity.class
		            ,RenameActivity.class
		            ,DuplicateActivity.class
		            ,DeleteFilesActivity.class
		            ,MessagingScavenger.class
		            ,InfoActivity.class
		    });
		    view = uiBuilder.createStorageView(this,acts);

	// build the menus
		    final UIComponentMenuBar menuBar = new UIComponentMenuBar(this) {
		        @Override
                protected void populateFileMenu(final FileMenuBuilder fmb) {
		            fmb.windowOperation(view.getNewFolder())
		                .windowOperation(view.getFoldersList().getCreate())
		                .windowOperation(acts.getActivity(ViewInBrowserActivity.class))
		                //.windowOperation(acts.getActivity(BuildQueryActivity.class)) non functional
		                .windowOperation(acts.getActivity(TaskRunnerActivity.class))
		                .windowOperation(acts.getActivity(SimpleDownloadActivity.class))
		                .windowOperation(view.getUpload());
		            acts.getActivity(MessagingScavenger.class).addTo(fmb.getMenu());
		                fmb.closeWindow()
		                .separator()
		                .windowOperation(acts.getActivity(RenameActivity.class))
		                .windowOperation(acts.getActivity(DuplicateActivity.class))
		                .windowOperation(acts.getActivity(DeleteFilesActivity.class))
		                .windowOperation(view.getBookmark())
		                .windowOperation(view.getFoldersList().getEdit())
		                .windowOperation(view.getFoldersList().getDelete());
		        }
		        @Override
                protected void populateEditMenu(final EditMenuBuilder emb) { 
		            emb.cut()
		                .copy()
		                .paste()
		                .selectAll()
		                .clearSelection();
		               // .invertSelection();
		        }
		        @Override
                protected void constructAdditionalMenus() {
		            final MenuBuilder vmb = new MenuBuilder("View",KeyEvent.VK_V)
		                .windowOperation(view.getRefresh())
		                .windowOperation(view.getStop())
		                .separator()
		                .radiobox(view.getIcons())
		                .radiobox(view.getList());
		            add(vmb.create());
		            
		            final MenuBuilder gmb = new MenuBuilder("Go",KeyEvent.VK_G);
		            gmb.windowOperation(view.getNavigator().getBackAction())
		                .windowOperation(view.getNavigator().getForwardAction())
		                .windowOperation(view.getNavigator().getUpAction())
		                .separator()
		                .windowOperationWithIcon(view.getNavigator().getGoHomeAction())
		                .windowOperationWithIcon(view.getNavigator().getGoWorkspaceAcgtion())
		                .separator()
		                .windowOperation(view.getOpenLocation())
		                .windowOperation(view.getOpenFolder());
		            add(gmb.create());
		        }
		    };

	        setJMenuBar(menuBar);		

            final JComponent mainPanel = view.getMainPanel();
		    final JComponent mainButtons = view.getMainButtons();
		 
		    // assemble all into main window.
		    final JScrollPane activitiesScroller = new JScrollPane(acts.getTaskPane(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    activitiesScroller.setBorder(null);
			// assemble folders and tasks into LHS 
		    final JScrollPane foldersScroller = new JScrollPane(view.getFoldersList(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    foldersScroller.setBorder(null);
			final JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, foldersScroller
					,activitiesScroller);
		    leftPane.setDividerLocation(200);
		    leftPane.setDividerSize(6);
		    leftPane.setResizeWeight(0.5);
		    leftPane.setBorder(null);

		    // combine LHS and RSH
		    final JSplitPane lrPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPane,mainPanel);
		    lrPane.setDividerLocation(200);
		    lrPane.setResizeWeight(0.1);
		    lrPane.setDividerSize(6);
		    lrPane.setBorder(null);
		    pane.add(lrPane,BorderLayout.CENTER); 
		    
		    // add buttonbar on top.
		    pane.add(mainButtons,BorderLayout.NORTH);
		    
		    // finish it all off..
			this.setTitle("File Explorer");
			view.getNavigator().addNavigationListener(this);
		  //@todo find a new icon  setIconImage(IconHelper.loadIcon("search16.png").getImage());  
    }
	    
// event listener.
  
	

	final ActivitiesManager acts;
    StorageView view;
	
	public void show(final FileObject fileToShow) {
	        view.getNavigator().move(fileToShow);
	}

    public void moved(final NavigationEvent e) {
        setTitle("File Explorer - " + view.getNavigator().current().getName());
    }

    public void moving() {
    }



}

