/*$Id: VOExplorerImpl.java,v 1.34 2010/01/04 12:55:27 nw Exp $

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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.ActivityFactory;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.actions.BuildQueryActivity;
import org.astrogrid.desktop.modules.ui.actions.ContactActivity;
import org.astrogrid.desktop.modules.ui.actions.FurtherInfoActivity;
import org.astrogrid.desktop.modules.ui.actions.GetPaperActivity;
import org.astrogrid.desktop.modules.ui.actions.InfoActivity;
import org.astrogrid.desktop.modules.ui.actions.MessagingScavenger;
import org.astrogrid.desktop.modules.ui.actions.MultiConeActivity;
import org.astrogrid.desktop.modules.ui.actions.QueryScopeActivity;
import org.astrogrid.desktop.modules.ui.actions.SaveResourceActivity;
import org.astrogrid.desktop.modules.ui.actions.SaveXoXoListActivity;
import org.astrogrid.desktop.modules.ui.actions.SimpleDownloadActivity;
import org.astrogrid.desktop.modules.ui.actions.TapQueryActivity;
import org.astrogrid.desktop.modules.ui.actions.TaskRunnerActivity;
import org.astrogrid.desktop.modules.ui.actions.VospaceActivity;
import org.astrogrid.desktop.modules.ui.actions.WebInterfaceActivity;
import org.astrogrid.desktop.modules.ui.comp.BiStateButton;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.folders.ResourceBranch;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.ResourceTreeModel;
import org.astrogrid.desktop.modules.ui.folders.SmartList;
import org.astrogrid.desktop.modules.ui.folders.StaticList;
import org.astrogrid.desktop.modules.ui.folders.XQueryList;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel.LoadEvent;

import com.l2fprod.common.swing.BaseDialog;

/** Main window of voexplorer.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Mar-2005
 *
 */
public class VOExplorerImpl extends UIComponentImpl 
	implements ListSelectionListener, ActionListener, RegistryGooglePanel.LoadListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(VOExplorerImpl.class);
    private final Action stopAction = new StopAction();
    private final BiStateButton foldersButton;
    private final Action refreshAction= new RefreshAction();
    
    public static final String EXPORT = "export";

	public VOExplorerImpl( final UIContext context, final ActivityFactory activityBuilder
	        , final TypesafeObjectBuilder builder
			, final ResourceTreeModel folderModel, final QuerySizer sizer
            ,final ResourceChooserInternal chooser,  final XmlPersist persister) {
		super(context,"VO Explorer","window.voexplorer");
        this.chooser = chooser;
        this.persister = persister;
		logger.info("Constructing new VOExplorer");
        this.google = builder.createGooglePanel(this);
		this.setSize(800, 650);    
		final JPanel pane = getMainPanel();
		pane.setBorder(BorderFactory.createEmptyBorder());

		// build the actions menu / pane
		acts = activityBuilder.create(this,new Class[]{
		        QueryScopeActivity.class
		        , MultiConeActivity.class
		       ,BuildQueryActivity.class
		       ,TapQueryActivity.class
		       ,VospaceActivity.class
		       ,TaskRunnerActivity.class
		       ,WebInterfaceActivity.class
		       ,MessagingScavenger.class
		       ,SimpleDownloadActivity.class
		       ,InfoActivity.class
		       ,FurtherInfoActivity.class
		       ,GetPaperActivity.class
		       ,ContactActivity.class
		       ,SaveXoXoListActivity.class
		       ,SaveResourceActivity.class
		});
		
        // main resource view.		
        resourceLists = new ResourceTree(folderModel, this, chooser,persister,refreshAction); 
        resourceLists.setName(RESOURCES_VIEW);
        resourceLists.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(final TreeSelectionEvent evt) {
                if (! resourceLists.isDragging()) {
                    final ResourceFolder folder = resourceLists.getSelectedFolder();
                    if (folder != null) {
                        acts.clearSelection();
                        folder.display(google);
                        setTitle("VO Explorer - " + folder.getName());
                    }
                }
            }
        });	
        
		// build the menus.		
		final UIComponentMenuBar menuBar = new UIComponentMenuBar(this) {
		    @Override
            protected void populateFileMenu(final FileMenuBuilder fmb) {
		        fmb
		                .windowOperation(resourceLists.getAddSmart())
		                .windowOperation(resourceLists.getAddStatic())
		                .windowOperation(new NewFolderFromSelectionAction())
		                .windowOperation(resourceLists.getAddXQuery())
		                .windowOperation(resourceLists.getAddSubscription())
		                .windowOperation(resourceLists.getAddBranch())
		                .separator()
		                .windowOperation(resourceLists.getProperties())
		                .windowOperation(resourceLists.getRename())
		                .windowOperation(resourceLists.getDuplicate())
		                .windowOperation(resourceLists.getRemove())
		                .separator();
		                fmb.closeWindow()
		                .windowOperation(resourceLists.getImport())
		                .windowOperation(resourceLists.getExport());
		                
		       }
		    @Override
            protected void populateEditMenu(final EditMenuBuilder emb) {
		        //@todo attach.
		        emb
		            .cut()
		            .copy()
		            .paste()
		            .selectAll()
		            .clearSelection()
		           . invertSelection();
		    }
		    @Override
            protected void constructAdditionalMenus() {
                final MenuBuilder vmb = new MenuBuilder("View",KeyEvent.VK_V);
                vmb
                    .windowOperationWithIcon(google.getExpandAction())
                    .submenu(google.createColumnsMenu("Columns"))
                    .checkbox(google.getSystemToggleButton())
                    .separator()
                    .windowOperation(refreshAction)              
                    .windowOperation(stopAction);
                add(vmb.create());
                                                    
                final MenuBuilder rmb = new MenuBuilder("Resource",KeyEvent.VK_R)
                    .windowOperation(acts.getActivity(QueryScopeActivity.class))
                    .windowOperation(acts.getActivity(BuildQueryActivity.class))
                    .windowOperation(acts.getActivity(TaskRunnerActivity.class))
                    .windowOperation(acts.getActivity(WebInterfaceActivity.class));
                    
                acts.getActivity(MessagingScavenger.class).addTo(rmb.getMenu());
                 
                    rmb.windowOperation(acts.getActivity(SimpleDownloadActivity.class))
                    .separator()
                    .windowOperation(acts.getActivity(FurtherInfoActivity.class))
                    .windowOperation(acts.getActivity(GetPaperActivity.class))
                    .windowOperation(acts.getActivity(ContactActivity.class))
                    .separator()
                    .windowOperation(acts.getActivity(SaveXoXoListActivity.class))
                    .windowOperation(acts.getActivity(SaveResourceActivity.class));
                add(rmb.create());                        
		    }
		};
		setJMenuBar(menuBar);

		// main pane.	    
		this.mainPanel = new FlipPanel();

		// main view.

		// attach ourself to this reg chooser, to listen for selection changes.
		google.getCurrentResourceModel().addListSelectionListener(this); // listen to currently selected resource
		google.setPopup(acts.getPopupMenu());			
		google.addLoadListener(this);
		mainPanel.add(google,RESOURCES_VIEW);


		// build the various editing panels.
		smartEditPanel = new SmartListEditingPanel(this,sizer);
		wireUpEditor(smartEditPanel,EDIT_SMART_VIEW);
		staticEditPanel = new StaticListEditingPanel();
		wireUpEditor(staticEditPanel,EDIT_STATIC_VIEW);
		xqueryEditPanel = new XQueryListEditingPanel(this,sizer);
		wireUpEditor(xqueryEditPanel,EDIT_XQUERY_VIEW);
        subscriptionEditPanel = new SubscriptionEditingPanel(chooser);
        wireUpEditor(subscriptionEditPanel,EDIT_SUBSCRIPTION_VIEW);
		
		// assemble all into main window.
		final JScrollPane actionsScroll = new JScrollPane(acts.getTaskPane(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		actionsScroll.setBorder(null);
		actionsScroll.setMinimumSize(new Dimension(200,200));		

		
		final JPanel foldersPanel = new JPanel(new BorderLayout());
        final JScrollPane foldersScroll = new JScrollPane(resourceLists,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        foldersScroll.setBorder(null);
        foldersScroll.setMinimumSize(new Dimension(200,100));
        foldersPanel.add(foldersScroll, BorderLayout.CENTER);
		foldersPanel.setBorder(null);
		foldersPanel.setMinimumSize(new Dimension(200,100));
		
            
        this.foldersButton = new BiStateButton(resourceLists.getAddSmart(),stopAction);
        CSH.setHelpIDString(foldersButton,"reg.button");
        foldersPanel.add(foldersButton,BorderLayout.SOUTH);

		// assemble folders and tasks into LHS 
		final JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, foldersPanel	,actionsScroll);
		leftPane.setDividerLocation(300);
		leftPane.setDividerSize(6);
		leftPane.setResizeWeight(0.5);
		leftPane.setBorder(null);
		// combine LHS and RSH
		final JSplitPane lrPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPane,mainPanel);
		lrPane.setDividerLocation(200);
		lrPane.setResizeWeight(0.1); //most to the right
		lrPane.setBorder(null);
		lrPane.setDividerSize(6);
		pane.add(lrPane,BorderLayout.CENTER); 

		// add buttonbar on top.

		// finish it all off..
		setIconImage(IconHelper.loadIcon("search16.png").getImage());  
		logger.info("New VOExplorer - Completed");
		// finally, display first folder.
		resourceLists.initialiseViewAndSelection();
	}

	/**
	 *integrate an editor into  voexplorer.
	 */
	private void wireUpEditor(final EditingPanel editor,final String viewName) {
		mainPanel.add(editor,viewName);
		editor.getOkButton().addActionListener(this);
		editor.getCancelButton().addActionListener(this);		
	}

	private final FlipPanel mainPanel;
	final ActivitiesManager acts;
	public static final String RESOURCES_VIEW = "resources";
	public static final String EDIT_SMART_VIEW = "edit-smart";
	public static final String EDIT_STATIC_VIEW = "edit-static";
	public static final String EDIT_XQUERY_VIEW = "edit-xquery";
    public static final String EDIT_SUBSCRIPTION_VIEW = "edit-subscription";
    private final ResourceChooserInternal chooser;
    private final XmlPersist persister;
    private final ResourceTree resourceLists;
	private final RegistryGooglePanel google;
	private final EditingPanel smartEditPanel;
	private final EditingPanel staticEditPanel;
	private final XQueryListEditingPanel xqueryEditPanel;
    private final SubscriptionEditingPanel subscriptionEditPanel;


	private void notifyResourceTasks() {
		final Transferable tran = google.getSelectionTransferable();
		if (tran == null) {
			acts.clearSelection();
		} else {
		    acts.setSelection(tran);
		}
	}
	// listens to clicks on registry google.
	public void valueChanged(final ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return; // ignore
		}
		// from the table 
		notifyResourceTasks();
	}
/// load listener interface.
	public void loadCompleted(final LoadEvent e) {
	    foldersButton.enableA();
		resourceLists.setEnabled(true);
		stopAction.setEnabled(false);
		//unused final Boolean previous = (Boolean)refreshAction.getValue("previous");		
		refreshAction.setEnabled(true);
	}

	public void loadStarted(final LoadEvent e) {
	    foldersButton.enableB();
		resourceLists.setEnabled(false);
		stopAction.setEnabled(true);
		refreshAction.setEnabled(false);
		
	}	
//	 action Listener interface. handles ok/cancel from each of the editor panels.
	public void actionPerformed(final ActionEvent e) {
	    if (e.getSource() == smartEditPanel.getOkButton()) {
			acceptEdit(smartEditPanel);
		} else if (e.getSource() == staticEditPanel.getOkButton()) {
			acceptEdit(staticEditPanel);
		} else if (e.getSource() == xqueryEditPanel.getOkButton()) {
			acceptEdit(xqueryEditPanel);			
        } else if (e.getSource() == subscriptionEditPanel.getOkButton()) {
		    resourceLists.setEnabled(true);
            acceptEdit(subscriptionEditPanel);
		} else if (e.getSource() == smartEditPanel.getCancelButton()
				|| e.getSource() == staticEditPanel.getCancelButton()
				|| e.getSource() == xqueryEditPanel.getCancelButton()
                || e.getSource() == subscriptionEditPanel.getCancelButton()) {
			showResourceView();
		}
	}	
	// accept an edit from an editing panel, updade list and resources, etc.
	private void acceptEdit(final EditingPanel p) {
		p.loadEdits();
		final ResourceFolder r = p.getCurrentlyEditing();
		resourceLists.store(r);
		//display updated folder contents.
		
        mainPanel.setShowing(RESOURCES_VIEW);
        // clearing selection and then setting hte selection will trigger a query reload in all circumstances.
        resourceLists.clearSelection();
        resourceLists.setSelectedFolder(r);

        // make sure the edited folder is the selected one (necessary when we've just created a new one..
       // if (resourcesFolders.getSelectedValue() != r) {
       //     resourcesFolders.setSelectedValue(r,true);
       // } else {
    //    activities.clearSelection();
	//	r.display(google);
	//	setTitle("VO Explorer - " + r.getName());		
	}

	public void editNewSmartList(final SmartList f) {
		smartEditPanel.getOkButton().setText("Create");
		smartEditPanel.setCurrentlyEditing(f);
		mainPanel.setShowing(EDIT_SMART_VIEW);
		resourceLists.setEnabled(false);
		acts.clearSelection(); // removes list of actions.
	}

	public void editExistingSmartList(final SmartList f) {
		smartEditPanel.getOkButton().setText("Update");
		smartEditPanel.setCurrentlyEditing(f);
		mainPanel.setShowing(EDIT_SMART_VIEW);
		resourceLists.setEnabled(false);
		acts.clearSelection(); // removes list of actions.
	}
	
	public void editNewStaticList(final StaticList f) {
		staticEditPanel.getOkButton().setText("Create");
		staticEditPanel.setCurrentlyEditing(f);
		mainPanel.setShowing(EDIT_STATIC_VIEW);
		resourceLists.setEnabled(false);
		acts.clearSelection(); // removes list of actions.
	}

	public void editExistingStaticList(final StaticList f) {
		staticEditPanel.getOkButton().setText("Update");
		staticEditPanel.setCurrentlyEditing(f);
		mainPanel.setShowing(EDIT_STATIC_VIEW);
		resourceLists.setEnabled(false);
		acts.clearSelection(); // removes list of actions.
	}
	
	public void editNewQueryList(final XQueryList f) {
		xqueryEditPanel.getOkButton().setText("Create");
		xqueryEditPanel.setCurrentlyEditing(f);
		mainPanel.setShowing(EDIT_XQUERY_VIEW);
		resourceLists.setEnabled(false);
		acts.clearSelection(); // removes list of actions.
	}

	public void editExistingQueryList(final XQueryList f) {
		xqueryEditPanel.getOkButton().setText("Update");
		xqueryEditPanel.setCurrentlyEditing(f);
		mainPanel.setShowing(EDIT_XQUERY_VIEW);
		resourceLists.setEnabled(false);
		acts.clearSelection(); // removes list of actions.
	}

    public void editNewResourceBranch(final ResourceBranch f) {
        final Component c = getComponent();
        final Window w = c instanceof Window
                 ? (Window) c
                 : SwingUtilities.getWindowAncestor(c);
        
        final BaseDialog d;
        if (w instanceof Frame) {
            d = new RenameDialog((Frame)w, f);
        } else if (w instanceof Dialog) {
            d = new RenameDialog((Dialog)w, f);          
        } else {
            d = new RenameDialog(f);         
        }
        d.setVisible(true);
    }
    private class RenameDialog extends BaseDialog {
        private final ResourceFolder folder;

        public RenameDialog(final ResourceFolder folder) throws HeadlessException {
            super();
            this.folder = folder;
            init();
        }
        public RenameDialog(final Frame f,final ResourceFolder folder) throws HeadlessException {
            super(f);
            this.folder = folder;
            init();
        }
        public RenameDialog(final Dialog f,final ResourceFolder folder) throws HeadlessException {
            super(f);
            this.folder = folder;
            init();
        }            
        private final JTextField tf = new JTextField(20);
        private void init() {
            setModal( false);
            setDialogMode(BaseDialog.OK_CANCEL_DIALOG);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("New Container");
            getBanner().setTitle("Enter a name for the new container");
            getBanner().setSubtitleVisible(false);
            tf.setText("New Container");
            
            final Container cp = getContentPane();
            cp.setLayout(new java.awt.FlowLayout());
            cp.add(new JLabel("Name :"));
            cp.add(tf);                
            pack();
            tf.selectAll();
            tf.requestFocusInWindow();
            setLocationRelativeTo(VOExplorerImpl.this.getComponent());
        }
        
        @Override
        public void ok() {
            super.ok();
            final String nuName =tf.getText();
            if (StringUtils.isNotEmpty(nuName)) {
                folder.setName(nuName);
                resourceLists.appendFolder(folder);            
            }           
        }
    }


    public void editNewSubscription(final ResourceFolder f) {
        subscriptionEditPanel.getOkButton().setText("Create");
        subscriptionEditPanel.setCurrentlyEditing(f);
        mainPanel.setShowing(EDIT_SUBSCRIPTION_VIEW);
        resourceLists.setEnabled(false);
        acts.clearSelection();
    }

    public void editExistingSubscription(final ResourceFolder f) {
        subscriptionEditPanel.getOkButton().setText("Update");
        subscriptionEditPanel.setCurrentlyEditing(f);        
        mainPanel.setShowing(EDIT_SUBSCRIPTION_VIEW);
        resourceLists.setEnabled(false);
        acts.clearSelection();
        
    }

	public void showResourceView() {
		mainPanel.setShowing(RESOURCES_VIEW);
		resourceLists.setEnabled(true);
	}	

// public api.
	/** called to display a specific set of resouces in this view.
	 * @param uriList
	 */
	public void displayResources(final List<URI> uriList) {
        resourceLists.clearSelection();     
		google.displayIdSet(uriList);
	}
	
	public void displayResources(final String title, final List<URI> uriList) {
        resourceLists.clearSelection();         
	    google.displayIdSet(title,uriList);
	}
	
	public void doOpen(final URI ivorn) {
        resourceLists.clearSelection();         
		google.displayIdSet(Collections.singletonList(ivorn));
		
	}
	
	   public void doOpen(final String title,final URI ivorn) {
           resourceLists.clearSelection();       
	        google.displayIdSet(title,Collections.singletonList(ivorn));
	        
	    }
	
	public void doQuery(final String query) {
        resourceLists.clearSelection();         
		google.displayQuery(query);
	}
	
	   public void doQuery(final String title,final String query) {
	        resourceLists.clearSelection();	       
	        google.displayQuery(title,query);
	    }

	   /**
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 8, 20071:47:58 PM
     */
    private final class StopAction extends AbstractAction {
        public StopAction() {
            super("Halt Search");
            putValue(AbstractAction.SMALL_ICON,IconHelper.loadIcon("loader.gif"));
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD,UIComponentMenuBar.MENU_KEYMASK));
            setEnabled(false);
        }

        /**
         * @param name
         */
        private StopAction(final String name) {
            super(name);
        }

        public void actionPerformed(final ActionEvent e) {
            google.halt();
        }
    }

    /** perform a refresh action.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 8, 200712:09:36 PM
     */
    private final class RefreshAction extends AbstractAction {
        /**
         * 
         */
        public RefreshAction() {
            super("Refresh",IconHelper.loadIcon("reload16.png"));
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_R,UIComponentMenuBar.MENU_KEYMASK));
            putValue(Action.SHORT_DESCRIPTION,"Refresh: reload the contents of the current list from the VO Registry");
            setEnabled(false);
        }

        public void actionPerformed(final ActionEvent e) {
            google.setNextToBypassCache(); 
            final ResourceFolder f =  resourceLists.getSelectedFolder();
            if (f != null) { 
                acts.clearSelection();
                f.display(google);
                setTitle("VO Explorer - " + f.getName());
            } else {
                System.err.println("resouce folder was null");//@todo remove me.
            }
        }
    }
    
  private final class NewFolderFromSelectionAction extends AbstractAction implements ListSelectionListener {
      /**
     * 
     */
    public NewFolderFromSelectionAction() {
        super("New List from Selection");
        setEnabled(false);
        google.resourceTable.getSelectionModel().addListSelectionListener(this);
    }
    public void actionPerformed(final ActionEvent e) {
        final Transferable trans = google.getSelectionTransferable();
        resourceLists.clearSelection();
        resourceLists.getTransferHandler().importData(resourceLists,trans);
    }
    // listens to the selection.
    public void valueChanged(final ListSelectionEvent e) {
        setEnabled(google.resourceTable.getSelectedRowCount() > 0);
    }
  }
}

