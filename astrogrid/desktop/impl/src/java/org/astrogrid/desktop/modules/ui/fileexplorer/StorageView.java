/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.actions.BulkCopyWorker;
import org.astrogrid.desktop.modules.ui.actions.CopyCommand;
import org.astrogrid.desktop.modules.ui.comp.ActionComboBox;
import org.astrogrid.desktop.modules.ui.comp.BiStateButton;
import org.astrogrid.desktop.modules.ui.comp.EventListDropDownButton;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.comp.SearchField;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.BookmarkNavigationEvent;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationEvent;
import org.astrogrid.desktop.modules.ui.folders.StorageFolder;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.RangeList;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.matchers.Matchers;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;
import com.l2fprod.common.swing.JDirectoryChooser;
/** View for storage.
 * 
 * based on ubuntu nautilus, mostly.
 * a 'places' sidebar - which later we'll add in options for 'history' and maybe even 'tree'.
 * Alternately, keep it as 'places', but add in some recent history under a divider, 
 * and provide a 'tree' view in hte main window.
 * 
 *@future - add tree to 'list' view - as in OSX. also could add OSX's 'columns' view - which 
 * is nice to navigate through.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 3, 200712:17:50 AM
 */
public class StorageView  implements  ListSelectionListener, FileNavigator.NavigationListener, UserLoginListener{
	private final UIComponent parent;
	protected final UIComponent getParent() {
		return parent;
	}
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(StorageView.class);
	
	private class RefreshAction extends AbstractAction {
		public RefreshAction() {
			super("Refresh",IconHelper.loadIcon("reload22.png"));
			CSH.setHelpIDString(this,"files.refresh");
			putValue(Action.SHORT_DESCRIPTION,"Refresh: reload information about the current folder");
			putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_R,UIComponentMenuBar.MENU_KEYMASK));
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			navigator.refresh();
		}
	}
	
	private class GoAction extends AbstractAction {
		public GoAction() {
			super("Go",IconHelper.loadIcon("go22.png"));
			putValue(Action.SHORT_DESCRIPTION,"Go: load the specfied location");
		}
		public void actionPerformed(ActionEvent e) {
			String s = location.getText();
			//@todo add some input validation here
			navigator.move(s);
		}
	}
	
	private class OpenLocationAction extends AbstractAction {
	    public OpenLocationAction() {
	        super("Go to Location"+UIComponentMenuBar.ELLIPSIS);
            CSH.setHelpIDString(this,"files.openLocation");	        
	        putValue(SHORT_DESCRIPTION,"Navigate to a provided URI");
	        putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_G,UIComponentMenuBar.MENU_KEYMASK));	        
	    }
        public void actionPerformed(ActionEvent e) {
            location.selectAll();
            location.requestFocusInWindow();
        }
	}
	
	private class OpenFolderAction extends AbstractAction {

        public OpenFolderAction() {
            super("Go to Local Folder"+UIComponentMenuBar.ELLIPSIS);
            CSH.setHelpIDString(this,"files.openFolder");            
            putValue(SHORT_DESCRIPTION,"Select and open a local folder");     
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_G,UIComponentMenuBar.SHIFT_MENU_KEYMASK));                        
        }
        public void actionPerformed(ActionEvent e) {
            JDirectoryChooser chooser = new JDirectoryChooser(SystemUtils.getUserHome());
            chooser.setShowingCreateDirectory(false);
            int code = chooser.showOpenDialog(mainPanel);
            if (code == JDirectoryChooser.APPROVE_OPTION) {
                try {
                    URL f = chooser.getSelectedFile().toURL();
                    navigator.move(f.toString());
                } catch (MalformedURLException ex) {
                    parent.showTransientError("Unable to open file",ExceptionFormatter.formatException(ex));
                }
            }
        }
	}
	
	private class StopAction extends AbstractAction {
		public StopAction() {
			super("Stop",IconHelper.loadIcon("stop22.png"));
			putValue(Action.SHORT_DESCRIPTION,"Stop: halt loading of current folder");
		    putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD,UIComponentMenuBar.MENU_KEYMASK));
		       
		}

		public void actionPerformed(ActionEvent e) {
			goButton.enableA();
			StorageView.this.getParent().haltMyTasks();
		}
	}
	
	private class IconsAction extends AbstractAction {
		public IconsAction() {
			super("as Icons");
	         CSH.setHelpIDString(this,"files.icons");
			putValue(Action.SMALL_ICON,IconHelper.loadIcon("iconview22.png"));
			putValue(Action.SHORT_DESCRIPTION,"Icons View: show items as icons");
			putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_1,UIComponentMenuBar.MENU_KEYMASK));
		}

		public void actionPerformed(ActionEvent e) {
			mainPanel.show("list");
			fileList.requestFocusInWindow();
			
		}		
	}
	
	private class ListAction extends AbstractAction {
		public ListAction() {
			super("as List");
	         CSH.setHelpIDString(this,"files.table");
			putValue(Action.SMALL_ICON,IconHelper.loadIcon("listview22.png"));
			putValue(Action.SHORT_DESCRIPTION,"List View: show items as a list");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_2,UIComponentMenuBar.MENU_KEYMASK));

		}

		public void actionPerformed(ActionEvent e) {
			mainPanel.show("table");
			fileTable.requestFocusInWindow();
		}		
	}
	
	private class BookmarkAction extends AbstractAction {
		public BookmarkAction() {
			super("Bookmark this Folder", IconHelper.loadIcon("addbookmark22.png"));
			CSH.setHelpIDString(this,"files.bookmark");
			putValue(Action.SHORT_DESCRIPTION,"Bookmark the current location");
			putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_K,UIComponentMenuBar.MENU_KEYMASK));
			setEnabled(false);
		}
		public void actionPerformed(ActionEvent e) {
		    
			StorageFolder f = new StorageFolder();
			try {
				//@todo work out a more sensible icon in some cases.
				FileObject fo = navigator.current();
				f.setFile(fo);
				f.setUriString(fo.getName().getURI());
				f.setName(fo.getName().getBaseName());
				foldersList.add(f); // and this is automatically persisted.
			} catch (Exception ex) {
				parent.showTransientError("Failed to add bookmark",ExceptionFormatter.formatException(ex));
			}
		}
	}
	
	private class UploadAction extends AbstractAction {
	    /**
         * 
         */
        public UploadAction() {
            super("Upload"+UIComponentMenuBar.ELLIPSIS);
            CSH.setHelpIDString(this,"files.upload");
            putValue(Action.SHORT_DESCRIPTION,"Upload local files to this folder");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_UP,UIComponentMenuBar.SHIFT_MENU_KEYMASK));
            setEnabled(true);
        }
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser(SystemUtils.getUserHome());
            chooser.setDialogTitle("Choose files to upload");
            chooser.setApproveButtonText("Upload");
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setMultiSelectionEnabled(true);
            int code = chooser.showOpenDialog(getParent().getComponent());
            if (code == JFileChooser.APPROVE_OPTION) {
                FileObject base = navigator.current();
                File[] files = chooser.getSelectedFiles();
                CopyCommand[] commands = new CopyCommand[files.length];
                for (int i = 0; i < files.length; i++) {
                    commands[i] = new CopyCommand(files[i]);
                }
                new BulkCopyWorker(vfs,getParent(),base,commands).start();
            }
        }
	}
	
	private class NewFolderAction extends AbstractAction {
	    /**
         * @author Noel.Winstanley@manchester.ac.uk
         * @since Nov 21, 200711:35:08 AM
         */
        private final class NewFolderDialog extends BaseDialog {
            /**
             * 
             */
            private String baseName;
            /**
             * 
             */
            private final FileObject base;
            private JTextField tf = new JTextField(20);
            private void init (){
                baseName = base.getName().getBaseName();
                if (StringUtils.isEmpty(baseName)) {
                    baseName = "/";
                }
                setModal(false);
                setDialogMode(BaseDialog.OK_CANCEL_DIALOG);
                setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                setTitle("Create New Folder");
                getBanner().setTitle("Create new subfolder of " + this.baseName);
                getBanner().setSubtitle("Enter a name for this folder");
                getBanner().setSubtitleVisible(true);
                tf.setText("NewFolder");
                
                final Container cp = getContentPane();
                cp.setLayout(new java.awt.FlowLayout());
                cp.add(new JLabel("Folder Name :"));
                cp.add(tf);
                pack();
                setLocationRelativeTo(StorageView.this.getParent().getComponent());                
            }


            private NewFolderDialog(FileObject base)
            throws HeadlessException {
                this.base = base;
                init();
            }
            private NewFolderDialog(Dialog d,FileObject base)
            throws HeadlessException {
                super(d);
                this.base = base;
                init();
            }
            private NewFolderDialog(Frame f,FileObject base)
            throws HeadlessException {
                super(f);
                this.base = base;
                init();
            }
            public void ok() {
                (new BackgroundWorker(StorageView.this.getParent(),"Creating subfolder of " + this.baseName,BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {
                    
                    protected Object construct() throws Exception {
                        String nuName = tf.getText();
                        if (StringUtils.isEmpty(nuName)) {
                            return null; // bail out.
                        }
                        FileObject f = base.resolveFile(nuName);
                        if (f.exists()) {
                            return nuName + " already exists";
                        }
                        // ok, user has made some input, and it's fairly valid - we can close the dialogue immediately.
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                NewFolderDialog.super.ok();
                            }
                           });	                        
                        f.createFolder();
                        FileSystem fs =base.getFileSystem();
                        if (fs instanceof AbstractFileSystem) {
                            ((AbstractFileSystem)fs).fireFileChanged(base);
                        }
                        return null;
                    }
                    protected void doFinished(Object result) {
                        if (result == null && isVisible()) {
                            NewFolderDialog.super.ok();
                        } else {
                            getBanner().setSubtitleColor(Color.RED);
                            getBanner().setSubtitle(result + " - Choose another name");
                        }
                    }	                    
                }).start();	       
            }
        }
        public  NewFolderAction() {
	        super("New Folder"+UIComponentMenuBar.ELLIPSIS, IconHelper.loadIcon("foldernew16.png"));
            CSH.setHelpIDString(this,"files.newFolder");
	        putValue(Action.SHORT_DESCRIPTION,"Create a new folder in the current location");
	        putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_N,UIComponentMenuBar.MENU_KEYMASK));
	        setEnabled(true);
	    }
	    public void actionPerformed(ActionEvent e) {
	        // work out where we are at the moment.
	        final FileObject base =navigator.current();
	        Component pc = parent.getComponent();
	        Window w = pc instanceof Window
	                 ? (Window) pc
	                 : SwingUtilities.getWindowAncestor(pc);
	        
	        final BaseDialog d;
	        if (w instanceof Frame) {
	            d = new NewFolderDialog((Frame)w, base);
	        } else if (w instanceof Dialog) {
	            d = new NewFolderDialog((Dialog)w, base);          
	        } else {
	            d = new NewFolderDialog(base);         
	        }
	        d.setVisible(true);

	        
	    }
	}
	
	private static final String STORAGE_VIEW = "Storage";
    private final FileSystemManager vfs;


		public StorageView(UIComponent parent, ActivitiesManager actsManager,EventList unfilteredfoldersList, FileSystemManager vfs, IconFinder iconFinder, Community comm) {
    		this.parent = parent;
            this.vfs = vfs;
    		foldersListFilter = new MutableMatcherEditor();
            this.foldersList = new FilterList(unfilteredfoldersList,foldersListFilter);
    		
            ButtonGroup bg = new ButtonGroup();
            bg.add(iconsMenuItem);
            bg.add(listMenuItem);
            iconsMenuItem.setSelected(true);
            
    		// core model.
            SearchField filter = new SearchField("Filter files");	
            CSH.setHelpIDString(filter,"files.filter");
            MatcherEditor ed = new TextComponentMatcherEditor(filter.getWrappedDocument(),new FileObjectFilterator());
            navigator = new FileNavigator(getParent(),vfs,ed,actsManager,iconFinder);		
            comm.addUserLoginListener(this);
            navigator.addNavigationListener(this);
    	// hierarchies.
    		folders = new StorageFoldersList(foldersList,parent,vfs);
    		folders.addListSelectionListener(this);
    		folders.setName(STORAGE_VIEW);
       // toolbar
    	    FormLayout layout = new FormLayout(
    	    		//      back fore      up            label             location             stop, refresh bookmark newFolder views        filter
    	    		"1dlu,d,d,1dlu,d,1dlu,right:d,1dlu,80dlu:grow,0dlu,d,d,d,d,0dlu,d,0dlu,50dlu,1dlu" // cols
    	    		,"pref"); // rows
    	    PanelBuilder builder = new PanelBuilder(layout);
    	    CellConstraints cc = new CellConstraints();
    	    int c = 1;
    	    int r = 2;
    	    // previous button
    	    RangeList historyRange = new RangeList(navigator.getPreviousList());
    	    historyRange.setTailRange(navigator.getMaxHistorySize(),1); // not including the current.
    	    back = new EventListDropDownButton(new JButton(IconHelper.loadIcon("previous22.png")),historyRange,true);
            CSH.setHelpIDString(back,"files.back");
    	    back.getMainButton().setToolTipText("Back: See folders you viewed previously");
    	    builder.add(back,cc.xy(r++,c));
    	    // next button.
    	    forward = new EventListDropDownButton(new JButton(IconHelper.loadIcon("next22.png")),navigator.getNextList(),true);
    	    forward.getMainButton().setToolTipText("Forward: See folders you viewed previously");
            CSH.setHelpIDString(forward,"files.forward");
    	    builder.add(forward,cc.xy(r++,c));
    	    r++;
    	    up = new EventListDropDownButton(new JButton(IconHelper.loadIcon("up22.png")),navigator.getUpList(),false);
    	    up.getMainButton().setToolTipText("Up: navigate to the parent of the current folder");
            CSH.setHelpIDString(up,"files.up");
    	    builder.add(up,cc.xy(r++,c));
    	    r++;
    	    builder.addLabel("Location",cc.xy(r++,c));
    	    r++;
    	    //@todo later - maybe add autocomplete support for this?
    	    location = new JTextField();
    	    CSH.setHelpIDString(location,"files.location");
    	    location.requestFocusInWindow();
    	    location.setToolTipText("<html>Enter a URL or file location. Supported Schemes:"
    	    		+ "<br> [<b>file</b>://] absolute-path"
    	    		+ "<br> <b>workspace</b>://[ absolute-path]"
    	    		// decided not to put an example of myspace in here - looks confusingly like registry key.
    	    		+ "<br> <b>ftp</b>://[ username[: password]@] hostname[: port][ absolute-path]"
    	    		+ "<br> <b>sftp</b>://[ username[: password]@] hostname[: port][ absolute-path]"
    	    		+"<br><b>Examples</b>"
    	    		+"<br>/users/fred/myfile"
    	    		+ "<br>file:///home/someuser/somedir"
    	    		+ "<br>workspace:///myresults"
    	    		+ "<br>ftp://myusername:mypassword@somehost/pub/downloads/somefile.tgz"
    	    		+ "<br>sftp://myusername:mypassword@somehost/pub/downloads/somefile.tgz"
    	    		//@todo add examples of myspace and vospace schemes.
    	    );
           
    	    builder.add(location,cc.xy(r++,c));
    	    r++;
    		goButton = new BiStateButton(go,stop,true);
    		CSH.setHelpIDString(goButton,"files.go");
    		configureButton(goButton);
    	    builder.add(goButton,cc.xy(r++,c));
    	    builder.add(createMainButton(refresh),cc.xy(r++,c));
    	    builder.add(createMainButton(bookmark),cc.xy(r++,c));
    	    builder.add(createMainButton(newFolder),cc.xy(r++,c));
    	    r++;
    	    // can't be bothered making this track the menu entries, so just make it invisible if the menu is available.
    	    BasicEventList views = new BasicEventList();
    	    views.add(icons);
    	    views.add(list);    	   
    	    viewsCombo = new ActionComboBox(views);
            viewsCombo.setToolTipText("Views: alter how the folder contents are displayed");
    	    viewsCombo.setVisible(false);
            builder.add(viewsCombo,cc.xy(r++,c));
            
    	    r++;
    	    // filter was created much earlier.
    	    builder.add(filter,cc.xy(r++,c));
    	    mainButtons = builder.getPanel();
    	     
    	    final KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
    		mainButtons.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,"go");
    		mainButtons.getActionMap().put("go",go);
    		
    		fileList =  new NavigableFilesList(navigator);
    	    fileTable = new NavigableFilesTable( navigator); 
    	    mainPanel = new FlipPanel();
    	    mainPanel.add(new JScrollPane(fileList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
    	    		,"list");
    	    final JScrollPane tableScroll = new JScrollPane(fileTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	    tableScroll.getViewport().setBackground(Color.WHITE);
            mainPanel.add(tableScroll,"table");
    
      // finally, select first item in the views list, to start the whole thing going.
    	    folders.clearSelection();
    	    folders.setSelectedIndex(0);
    	    fileList.requestFocusInWindow();
    	}
	// list of folders being displayed in LHS
	private final EventList foldersList;
	// list of Actions for selecting between different views.
	//private final EventList views;
	private final StorageFoldersList folders;
	private final NavigableFilesList fileList;
	private final JTable fileTable;
	private final FlipPanel mainPanel;
	private final JComponent mainButtons;
	
	private final EventListDropDownButton back;
	private final EventListDropDownButton forward;
	private final EventListDropDownButton up;
	private final Action refresh = new RefreshAction();
	private final Action stop = new StopAction();
	private final Action openFolder = new OpenFolderAction();
	private final Action openLocation = new OpenLocationAction();
	private final Action go = new GoAction();
	private final Action icons = new IconsAction();
	private final Action list = new ListAction();
	private final JRadioButtonMenuItem iconsMenuItem = new JRadioButtonMenuItem(icons);
	private final JRadioButtonMenuItem listMenuItem = new JRadioButtonMenuItem(list);
	{
	    iconsMenuItem.setIcon(null); // don't want icons on these menu items.
	    listMenuItem.setIcon(null);
	}
	private final Action bookmark = new BookmarkAction();
	private final Action newFolder = new NewFolderAction();

	private final FileNavigator navigator;
	private final JTextField location;
	private final BiStateButton goButton;
    private final MutableMatcherEditor foldersListFilter;
    private final Action upload = new UploadAction();
    private final ActionComboBox viewsCombo;

	/** set to true to show a combo-box in menu bar that allows selection of views 
	 * default - not shown.
	 * @param b
	 */
    public void setShowViewComboInMenuBar(boolean b) {
        viewsCombo.setVisible(b);
    }
    
	/** create and configure a button from an action */
	private JButton createMainButton(Action act) {
		JButton b = new JButton(act);
		configureButton(b);
		return b;	
	}

	/** confugure a button - ensures that all have same look */
	private void configureButton(JButton b) {
		b.setBorderPainted(false);
		b.setRolloverEnabled(true);
		//future -  make this a preference, or something.
		b.setText(null);
		b.setMargin(UIConstants.SMALL_BUTTON_MARGIN);
	//	b.setVerticalTextPosition(JButton.BOTTOM);
	//	b.setHorizontalTextPosition(JButton.CENTER);
		//b.putClientProperty("is3DEnabled", Boolean.FALSE);		
	}

	public StorageFoldersList getFoldersList() {
		return folders;
	}


	public JComponent getMainPanel() {
		return mainPanel;
	}


	public JComponent getMainButtons() {
		return mainButtons;
	}


	// listens to clicks on storage folders and file list
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		if (e.getSource() == folders) {
			StorageFolder f =  (StorageFolder)folders.getSelectedValue();
			if (f != null) { 
			    navigator.move(f);
			}
		}
	}

	// listen to navigation 
    public void moved(NavigationEvent e) {
        back.setEnabled( navigator.hasPrevious());
        forward.setEnabled(navigator.hasNext());        
        goButton.enableA();
        refresh.setEnabled(true);
        up.setEnabled(! e.isRoot());
        location.setText(navigator.current().getName().getURI());
        //  notifyStorageTasks();        
        if (e instanceof BookmarkNavigationEvent) {
            bookmark.setEnabled(false);
            StorageFolder bookmark = ((BookmarkNavigationEvent)e).getBookmark();
            // if this item isn't already selected, select it.
            if (! bookmark.equals(folders.getSelectedValue())) {
                folders.setSelectedValue(bookmark,true); 
                // although this fires another event, this is blocked at history
                // as it's idempotent.
            }            
        } else {
            bookmark.setEnabled(true);
            folders.clearSelection();
        }
    }

    public void moving() {
        up.setEnabled(false);
        refresh.setEnabled(false);
        goButton.enableB();      
        
    }

    /**
     * @return the navigator
     */
    public final FileNavigator getNavigator() {
        return this.navigator;
    }

    // listen to login / logout events.
    public void userLogin(UserLoginEvent arg0) {
        // ignored.
    }

    public void userLogout(UserLoginEvent arg0) {
        // clear anything that might have references to myspace, and move home.
        navigator.reset();

    }

    /**
     * @return the location
     */
    public final JTextField getLocation() {
        return this.location;
    }
    
    public void setSingleSelectionMode(boolean singleSelection) {
        navigator.getModel().setSelectionMode(singleSelection ? ListSelectionModel.SINGLE_SELECTION : ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
    }
    
    /** add an additional filter to this view.
     * 
     * @param m a matcher that works on FileObjects and also 'StorageFolders'
     */
    public void installFilter(Matcher m) {
        navigator.getModel().installFilter(m);
        foldersListFilter.setMatcher(m);
    }
    
    /** clear the filter */
    public void clearFilter() {
        navigator.getModel().installFilter(Matchers.trueMatcher());
        foldersListFilter.setMatcher(Matchers.trueMatcher());
    }

    /**
     * @return the vfs
     */
    public final FileSystemManager getVfs() {
        return this.vfs;
    }


    /**
     * @return the refresh action
     */
    public final Action getRefresh() {
        return this.refresh;
    }

    /**
     * @return the stop action
     */
    public final Action getStop() {
        return this.stop;
    }


    /**
     * @return the icons action
     */
    public final JRadioButtonMenuItem getIcons() {
        return this.iconsMenuItem;
    }

    /**
     * @return the list action
     */
    public final JRadioButtonMenuItem getList() {
        return this.listMenuItem;
    }

    /**
     * @return the bookmark action
      */
    public final Action getBookmark() {
        return this.bookmark;
    }

    /**
     * @return the newFolder action
     */
    public final Action getNewFolder() {
        return this.newFolder;
    }

    /**
     * @return the openFolder
     */
    public final Action getOpenFolder() {
        return this.openFolder;
    }

    /**
     * @return the openLocation
     */
    public final Action getOpenLocation() {
        return this.openLocation;
    }

    /**
     * @return
     */
    public Action getUpload() {
        return this.upload;
    }





}
