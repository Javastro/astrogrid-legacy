/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ActionComboBox;
import org.astrogrid.desktop.modules.ui.comp.BiStateButton;
import org.astrogrid.desktop.modules.ui.comp.EventListDropDownButton;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.comp.SearchField;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.BookmarkNavigationEvent;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationEvent;
import org.astrogrid.desktop.modules.ui.fileexplorer.History.HistoryEvent;
import org.astrogrid.desktop.modules.ui.fileexplorer.History.HistoryListener;
import org.astrogrid.desktop.modules.ui.folders.StorageFolder;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.RangeList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.matchers.Matchers;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.JTaskPane;
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
			putValue(Action.SHORT_DESCRIPTION,"Refresh: reload information about the current folder");
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
	
	private class StopAction extends AbstractAction {
		public StopAction() {
			super("Stop",IconHelper.loadIcon("stop22.png"));
			putValue(Action.SHORT_DESCRIPTION,"Stop: halt loading of current folder");
		}

		public void actionPerformed(ActionEvent e) {
			goButton.enableA();
			StorageView.this.getParent().haltMyTasks();
		}
	}
	
	private class IconsAction extends AbstractAction {
		public IconsAction() {
			super("Icons View",IconHelper.loadIcon("iconview22.png"));
			putValue(Action.SHORT_DESCRIPTION,"Icons View: show items as icons");
		}

		public void actionPerformed(ActionEvent e) {
			mainPanel.show("list");
			
		}		
	}
	
	private class ListAction extends AbstractAction {
		public ListAction() {
			super("List View",IconHelper.loadIcon("listview22.png"));
			putValue(Action.SHORT_DESCRIPTION,"List View: show items as a list");
		}

		public void actionPerformed(ActionEvent e) {
			mainPanel.show("table");
		}		
	}
	
	private class BookmarkAction extends AbstractAction {
		public BookmarkAction() {
			super("Add Bookmark", IconHelper.loadIcon("addbookmark22.png"));
			putValue(Action.SHORT_DESCRIPTION,"Bookmark the current location");
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
				//@todo report and recover
			}
		}
	}
	
	private class NewFolderAction extends AbstractAction {
	    public  NewFolderAction() {
	        super("New Folder", IconHelper.loadIcon("foldernew16.png"));
	        putValue(Action.SHORT_DESCRIPTION,"Create a new folder in the current location");
	        setEnabled(true);
	    }
	    public void actionPerformed(ActionEvent e) {
	        // work out where we are at the moment.
	        final FileObject base =navigator.current();
	    
	        
	        (new BackgroundWorker(getParent(),"Creating subfolder of " + base.getName().getBaseName()) {

	            protected Object construct() throws Exception {
	                FileObject f;
	                do {
	                    String nuName = JOptionPane.showInputDialog(getParent().getFrame(),"Enter new folder name","NewFolder");
	                    if (StringUtils.isEmpty(nuName)) {
	                        return null; // user pressed cancel;
	                    }
	                    f = base.resolveFile(nuName);
	                } while (f.exists());
	                f.createFolder();
	                FileSystem fs = base.getFileSystem();
	                if (fs instanceof AbstractFileSystem) {
	                    ((AbstractFileSystem)fs).fireFileChanged(base);
	                }
	                return null;
	            }
	        }).start();	        
	    }
	}
	
	private static final String STORAGE_VIEW = "Storage";
    private final FileSystemManager vfs;


	public StorageView(UIComponent parent, ActivitiesManager actsManager,EventList unfilteredfoldersList, FileSystemManager vfs, IconFinder iconFinder, Community comm) {
		this.parent = parent;
        this.vfs = vfs;
		foldersListFilter = new MutableMatcherEditor();
        this.foldersList = new FilterList(unfilteredfoldersList,foldersListFilter);
		
		// core model.
        SearchField filter = new SearchField("Filter files");		
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
	    		"2dlu,pref,pref,4dlu,pref,4dlu,right:pref,2dlu 80dlu:grow,1dlu,pref,pref,pref, pref,4dlu, pref,1dlu,50dlu,1dlu" // cols
	    		,"pref"); // rows
	    PanelBuilder builder = new PanelBuilder(layout);
	    CellConstraints cc = new CellConstraints();
	    int c = 1;
	    int r = 2;
	    // previous button
	    RangeList historyRange = new RangeList(navigator.getPreviousList());
	    historyRange.setTailRange(navigator.getMaxHistorySize(),1); // not including the current.
	    back = new EventListDropDownButton(new JButton(IconHelper.loadIcon("previous22.png")),historyRange,true);
	    back.setToolTipText("Back: See folders you viewed previously");
	    builder.add(back,cc.xy(r++,c));
	    // next button.
	    forward = new EventListDropDownButton(new JButton(IconHelper.loadIcon("next22.png")),navigator.getNextList(),true);
	    forward.setToolTipText("Forward: See folders you viewed previously");
	    builder.add(forward,cc.xy(r++,c));
	    r++;
	    up = new EventListDropDownButton(new JButton(IconHelper.loadIcon("up22.png")),navigator.getUpList(),false);
	    up.setToolTipText("Up: navigate to the parent of the current folder");
	    builder.add(up,cc.xy(r++,c));
	    r++;
	    builder.addLabel("Location",cc.xy(r++,c));
	    r++;
	    //@todo later - maybe add autocomplete support for this?
	    location = new JTextField();
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
		configureButton(goButton);
	    builder.add(goButton,cc.xy(r++,c));
	    builder.add(createMainButton(refresh),cc.xy(r++,c));
	    builder.add(createMainButton(bookmark),cc.xy(r++,c));
	    builder.add(createMainButton(newFolder),cc.xy(r++,c));
	    r++;
	    views = new BasicEventList();
	    views.add(icons);
	    views.add(list);
	    builder.add(new ActionComboBox(views),cc.xy(r++,c));
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
	    mainPanel.add(new JScrollPane(fileList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
	    		,"list");
	    mainPanel.add(new JScrollPane(fileTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
	    		,"table");
	    //@todo make it easer to add further views to the main panel.

  // finally, select first item in the views list, to start the whole thing going.
	    folders.clearSelection();
	    folders.setSelectedIndex(0);
	}
	// list of folders being displayed in LHS
	private final EventList foldersList;
	// list of Actions for selecting between different views.
	private final EventList views;
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
	private final Action go = new GoAction();
	private final Action icons = new IconsAction();
	private final Action list = new ListAction();
	private final Action bookmark = new BookmarkAction();
	private final Action newFolder = new NewFolderAction();
	private final FileNavigator navigator;
	private final JTextField location;
	private final BiStateButton goButton;
    private final MutableMatcherEditor foldersListFilter;

	
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
		//@todo make this a preference, or something.
		b.setText(null);
	//	b.setVerticalTextPosition(JButton.BOTTOM);
	//	b.setHorizontalTextPosition(JButton.CENTER);
		b.putClientProperty("is3DEnabled", Boolean.FALSE);		
	}

	public StorageFoldersList getHierarchiesPanel() {
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


}
