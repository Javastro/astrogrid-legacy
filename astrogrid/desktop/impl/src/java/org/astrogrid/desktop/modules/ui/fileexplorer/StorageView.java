/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ActionComboBox;
import org.astrogrid.desktop.modules.ui.comp.BiStateButton;
import org.astrogrid.desktop.modules.ui.comp.EventListDropDownButton;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.comp.SearchField;
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
 * meanwhile, at the top of the main view, we'll show a breadcrumb trail, and an option 
 * to alter display between 'icons' and 'list' view.
 * also - a filter field, backwards, up and forwards buttons. Also refresh button?
 * 
 * maybe - add tree to 'list' view - as in OSX. also could add OSX's 'columns' view - which 
 * is nice to navigate through.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 3, 200712:17:50 AM
 */
public class StorageView implements ListSelectionListener, HistoryListener{
	private final UIComponent parent;
	public UIComponent getParent() {
		return parent;
	}
	/** background worker that opens a directory */
	private class OpenDirectoryWorker extends BackgroundWorker {

		public OpenDirectoryWorker( Location loc) {
			super(StorageView.this.getParent(),"Opening " + loc.getURI(),Thread.MAX_PRIORITY);
			this.loc = loc;
		}
		private Location loc;
		protected Object construct() throws Exception {
				FileObject fo = loc.retrieveFileObject();
				FileObject[] children = fo.getChildren();
				try {
					files.getReadWriteLock().writeLock().lock();
					files.clear();
					files.addAll(Arrays.asList(children));
				} finally {
					files.getReadWriteLock().writeLock().unlock();
				}
				return loc;
		}
		protected void doFinished(Object result) {
			location.setText(loc.getURI());
			try {// @todo place all this on background thread?
				FileObject fo = loc.retrieveFileObject();
				FileObject root = fo.getFileSystem().getRoot();						
				up.setEnabled(! fo.equals(root));
			} catch (FileSystemException x) {
				up.setEnabled(false);
			}
			// resynchronize the folder view.
			if (loc.getFolder() == null) {
				folders.clearSelection();
			} else {
				StorageFolder f = loc.getFolder();
				// if this item isn't already selected, select it.
				if (! f.equals(folders.getSelectedValue())) {
					folders.setSelectedValue(f,true); 
					// although this fires another event, this is blocked at history
					// as it's idempotent.
				}
			}
		}
		protected void doAlways() {
			goButton.enableA();
		}
		protected void doError(Throwable ex) {
			up.setEnabled(false);
			super.doError(ex);
			// move back in the history.
			// not a good idea.
			//history.movePrevious();
		}
	}
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(StorageView.class);
	
	private class UpAction extends AbstractAction {
		public UpAction() {
			super("Up",IconHelper.loadIcon("up22.png"));
			putValue(Action.SHORT_DESCRIPTION,"Up: Move to parent folder");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			Location loc = (Location)history.current();
			// the locatio will already have a file object - don't need to fetch it.
			try {
				FileObject fo = loc.retrieveFileObject();
				FileObject p= fo.getParent();
				history.move(new Location(p));
			} catch (FileSystemException x) {
				getParent().showError("Failed to access parent",x); // unlikely.
			}
			
		}
	}
	
	private class RefreshAction extends AbstractAction {
		public RefreshAction() {
			super("Refresh",IconHelper.loadIcon("reload22.png"));
			putValue(Action.SHORT_DESCRIPTION,"Refresh: reload information about the current folder");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			Location loc = (Location)history.current();
			goButton.enableB();			
			(new OpenDirectoryWorker(loc)).start();
		
		}
	}
	
	private class GoAction extends AbstractAction {
		public GoAction() {
			super("Go",IconHelper.loadIcon("go22.png"));
			putValue(Action.SHORT_DESCRIPTION,"Go: load the specfied location");
		}
		public void actionPerformed(ActionEvent e) {
			String s = location.getText();
			history.move(new Location(s));
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
			Location loc = (Location)history.current();
			StorageFolder f = new StorageFolder();
			try {
				//@todo work out a more sensible icon in some cases.
				FileObject fo = loc.retrieveFileObject();
				f.setFile(fo);
				f.setUriString(fo.getName().getURI());
				f.setName(fo.getName().getFriendlyURI());
				foldersList.add(f); // and this is automatically persisted.
			} catch (Exception ex) {
				//@todo report and recover
			}
		}
	}
	
	private static final String STORAGE_VIEW = "Storage";
    private final ActivitiesManager actsManager;

	public StorageView(UIComponent parent, ActivitiesManager actsManager,EventList foldersList, FileSystemManager vfs, IconFinder iconFinder) {
		this.parent = parent;
        this.actsManager = actsManager;
		
		this.vfs = vfs;
		this.foldersList = foldersList;
	// hierarchies.
		folders = new StorageFoldersList(foldersList,parent,vfs);
		folders.addListSelectionListener(this);
		folders.setName(STORAGE_VIEW);
   // toolbar
	    FormLayout layout = new FormLayout(
	    		//      back fore      up            label             location             stop, refresh bookmark views        filter
	    		"2dlu,pref,pref,4dlu,pref,4dlu,right:pref,2dlu 80dlu:grow,1dlu,pref,pref,pref, 4dlu, pref,1dlu,50dlu,1dlu" // cols
	    		,"pref"); // rows
	    PanelBuilder builder = new PanelBuilder(layout);
	    CellConstraints cc = new CellConstraints();
	    int c = 1;
	    int r = 2;
	    // previous button
	    RangeList historyRange = new RangeList(history.getPreviousList());
	    historyRange.setTailRange(history.getMaxHistorySize(),1); // not including the current.
	    back = new EventListDropDownButton(new JButton(IconHelper.loadIcon("previous22.png")),historyRange,true);
	 //   configureButton(back);
	    back.setToolTipText("Back: See folders you viewed previously");
	    builder.add(back,cc.xy(r++,c));
	    // next button.
	    forward = new EventListDropDownButton(new JButton(IconHelper.loadIcon("next22.png")),history.getNextList(),true);
	//    configureButton(forward);
	    forward.setToolTipText("Forward: See folders you viewed previously");
	    builder.add(forward,cc.xy(r++,c));
	    r++;
	    builder.add(createMainButton(up),cc.xy(r++,c));
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
	    r++;
	    views = new BasicEventList();
	    views.add(icons);
	    views.add(list);
	    builder.add(new ActionComboBox(views),cc.xy(r++,c));
	    r++;
	    SearchField filter = new SearchField("Filter files");
	    builder.add(filter,cc.xy(r++,c));
	    mainButtons = builder.getPanel();
	     
	    final KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
		mainButtons.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,"go");
		mainButtons.getActionMap().put("go",go);
//		 main window datastructures.
	    files = new BasicEventList();
	    // filter out hidden files. Later, add an option to show them - which will 
	    // cause this matcherEditor to emit a change event and use a 'pass all' matcher.
	    EventList noHiddenFiles = new FilterList(files,new AbstractMatcherEditor() {
	    	public Matcher getMatcher() {
	    		return new Matcher() {
					public boolean matches(Object arg0) {
						FileObject fo = (FileObject)arg0;
						try {
							return !(fo.isHidden() || fo.getName().getBaseName().charAt(0) == '.') ;
						} catch (FileSystemException x) {
							return true;
						}
					}
	    		};
	    	}
	    });
	    EventList filteredFiles = new FilterList(noHiddenFiles,
	    		new TextComponentMatcherEditor(filter.getWrappedDocument(),new FileObjectFilterator()));
	    SortedList sortedFiles= new SortedList(filteredFiles, FileObjectComparator.getInstance());
	
		// listen to movement through the history list.
		history.addHistoryListener(this);

		VFSOperationsImpl.Current curr = new VFSOperationsImpl.Current() {

			public FileObject get() {
				try {
					return ((Location)history.current()).retrieveFileObject();
				} catch (FileSystemException x) {
					// unlikely.
					logger.error("FileSystemException",x);
					return null;
				}
			}
		};
		VFSOperations ops = new VFSOperationsImpl(parent,curr,vfs);
		dnd = new FileModel(sortedFiles, actsManager, iconFinder,ops);
		fileList =  new NavigableFilesList( this,iconFinder,dnd);
	    fileTable = new NavigableFilesTable( this,iconFinder,dnd); 
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
	final FileSystemManager vfs;
	// list of FileObjects currently being displayed in main window.
	final EventList files;
	// list of folders being displayed in LHS
	final EventList foldersList;
	// list of Actions for selecting between different views.
	final EventList views;
	final StorageFoldersList folders;
	private final NavigableFilesList fileList;
	private final JTable fileTable;
	final FlipPanel mainPanel;
	private final JComponent mainButtons;
	
	private final 	    EventListDropDownButton back;
	private final EventListDropDownButton forward;
	final Action up = new UpAction();
	private final Action refresh = new RefreshAction();
	private final Action stop = new StopAction();
	private final Action go = new GoAction();
	private final Action icons = new IconsAction();
	private final Action list = new ListAction();
	private final Action bookmark = new BookmarkAction();
		
	final History history = new History();
	final JTextField location;
	final BiStateButton goButton;
	private  final FileModel dnd;

	
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

	public JComponent getHierarchiesPanel() {
		return folders;
	}

	public String getName() {
		return STORAGE_VIEW;
	}

	public JComponent getMainPanel() {
		return mainPanel;
	}


	public JComponent getMainButtons() {
		return mainButtons;
	}

		// calle when visibility of this view changes.
	public void setVisible(boolean b) {
		folders.setEnabled(b);
		fileList.setEnabled(b);
		mainButtons.setEnabled(b);
	}

	//we've moved.
	public void currentChanged(HistoryEvent e) {
		back.setEnabled( history.getPreviousList().size() > 1);
		forward.setEnabled(! history.getNextList().isEmpty());
		final Location current = (Location)e.current();
		refresh.setEnabled(current != null);
		bookmark.setEnabled(current != null && current.getFolder() == null);
		if (current != null) {
			goButton.enableB();
			(new OpenDirectoryWorker(current)).start();
		}		
	//	notifyStorageTasks();
	}

	// listens to clicks on storage folders and file list
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		if (e.getSource() == folders) {
			StorageFolder f =  (StorageFolder)folders.getSelectedValue();
			if (f != null) { 
				//causes eveything else to be triggered.
					history.move(new Location(f));
			}
		}
	}	
	// data structure used to manage the different ways we might provide a location to navigate to.
	// also extends JMenu item, which means it can be displayed ina  menu, and if clicked
	// knows how to navigate to this location.
	private class Location extends JMenuItem implements ActionListener{
		/**
		 * create a location from a uri string
		 * @param s
		 */
		public Location(String s) {
			setText(s);
			addActionListener(this); // listen to clicks on ourselves.
		}
		/** use an existing file object as a location */
		public Location(FileObject o) {
			this( o.getName().getURI());			
			this.o = o;
		}
		/** use a storage folder as a location */
		public Location(StorageFolder f) {
			this(f.getUriString());
			this.f = f;
			this.o = f.getFile();		
		}
		private StorageFolder f = null;
		private FileObject o = null;

		public void actionPerformed(ActionEvent e) {
			history.move(this);
		}		
		public FileObject retrieveFileObject() throws FileSystemException {
			if (o == null) {
				o = vfs.resolveFile(getURI());
				if (f != null) {
					f.setFile(o);
				}
			}
			return o;
		}

		public String getURI() {
			return getText();
		}
		
		public StorageFolder getFolder() {
			return f;
		}
		
		public boolean equals(Object obj) {
			if (! (obj instanceof Location)) {
				return false;
			}
			Location other = (Location)obj;
			if (other == null) {
				return false;
			}
			return getText().equals(other.getText());
		}		
	}
	/** move this view to the specified uri */
	public void move(String uri) {
		history.move(new Location(uri));
	}
	
	/** move ths view to the specified file object */
	public void move(FileObject obj) {
		history.move(new Location(obj));
	}
	
}
