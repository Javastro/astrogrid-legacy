/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileChangeEvent;
import org.apache.commons.vfs.FileListener;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.fileexplorer.History.HistoryEvent;
import org.astrogrid.desktop.modules.ui.fileexplorer.History.HistoryListener;
import org.astrogrid.desktop.modules.ui.folders.StorageFolder;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.MatcherEditor;

/** 'controller' object that allows navigation around a filesystem; manages changes to a file model and a history.
 * 
 * this object is observable, and notifies observers when the current location changes.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 10, 20074:45:27 PM
 * @TEST this model
 */
public class FileNavigator implements HistoryListener<FileNavigator.Location>, VFSOperationsImpl.Current, FileListener{
    
    /** event listener interface for navigation events */
    public static interface NavigationListener extends EventListener {
        /** called while the navigator is in the process of moving */
        public void moving();
        /** called then the navigator has finished moving */
        public void moved(NavigationEvent e);
    }
    
    /** event notifying weve navigated to a position */
    public static class NavigationEvent extends EventObject {

        private final boolean root;
        public NavigationEvent(final Object source, final boolean root) {
            super(source);
            this.root = root;
        }
        /** returns true of this position is a filesystem root */
        public boolean isRoot() {
            return root;
        }
    }
    
    /** event fired when navigating to a bookmarked position */
    public static class BookmarkNavigationEvent extends NavigationEvent {

        private final StorageFolder sf;

        public BookmarkNavigationEvent(final Object source, final boolean root,final StorageFolder sf) {
            super(source,root);
            this.sf = sf;
        }
        public StorageFolder getBookmark() {
            return sf;
        }
    }
    
    private final EventListenerList listeners = new EventListenerList();
    public void addNavigationListener(final NavigationListener l) {
        listeners.add(NavigationListener.class,l);
    }
    public void removeNavigationListener(final NavigationListener l) {
        listeners.remove(NavigationListener.class,l);
    }
    
    private void fireMoving() {
        final EventListener[] lits = listeners.getListeners(NavigationListener.class);
        if (lits != null) {
            for (int i = 0; i < lits.length; i++) {
                ((NavigationListener)lits[i]).moving();
            }
        }
    }
    
    private void fireMoved(final boolean isRoot) {
        final EventListener[] lits = listeners.getListeners(NavigationListener.class);
        if (lits != null) {
            final NavigationEvent ev = new NavigationEvent(this,isRoot);
            for (int i = 0; i < lits.length; i++) {
                ((NavigationListener)lits[i]).moved(ev);
            }
        }        
    }
    
    private void fireMovedToBookmark(final boolean isRoot, final StorageFolder storageFolder) {
        final EventListener[] lits = listeners.getListeners(NavigationListener.class);
        if (lits != null) {
            final NavigationEvent ev = new BookmarkNavigationEvent(this,isRoot,storageFolder);
            for (int i = 0; i < lits.length; i++) {
                ((NavigationListener)lits[i]).moved(ev);
            }
        }         
    }
    
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(FileNavigator.class);

    private final Filemodel model;
    private final History<Location> history;
    private final UIComponent parent;
    private final EventList<UpMenuItem> upList;

    private final FileSystemManager vfs;

    private final IconFinder icons;
    public FileNavigator(final UIComponent parent,final FileSystemManager vfs, final MatcherEditor ed, final ActivitiesManager activities, final IconFinder icons) {
        super();
        this.parent = parent;
        this.vfs = vfs;
        this.icons = icons;
        this.model = Filemodel.newInstance(ed,activities,icons,new VFSOperationsImpl(parent,this,vfs));
        this.history = new History<Location>();
        history.addHistoryListener(this);
        this.upList = new BasicEventList<UpMenuItem>();
    }

    public Filemodel getModel() {
        return model;
    }

    //vfs operations interface.
    public FileObject get() {
        return current();
    }
    /**
     * @return the icons
     */
    public final IconFinder getIcons() {
        return this.icons;
    }        
    
// methods delegated from the history
    public int getMaxHistorySize() {
        return this.history.getMaxHistorySize();
    }

    public EventList<Location> getNextList() {
        return this.history.getNextList();
    }

    public EventList<Location> getPreviousList() {
        return this.history.getPreviousList();
    }
    
    public EventList<UpMenuItem> getUpList() {
        return upList;
    }

// current state
    /** access the current fiile - will throw an illeage state exception if not yet resolved */
    public FileObject current()  {
        final Location loc = history.current();
        if (loc == null) {
            return null;
        }
        return loc.getFileObject();
    }
    
   //navigation functions
    public void up()  {
        (new UpWorker()).start();
    }
     
    public void previous() {
        history.movePrevious();
    }
    
    public void next() {
        history.moveNext();
    }
    
    public void home() {
        move(SystemUtils.getUserHome().toString());
    }
   
    public void refresh() {
        (new RefreshWorker()).start();
    }
    
    public boolean hasPrevious() {
        return history.hasPrevious();
    }
    
    public boolean hasNext() {
        return history.hasNext();
    }
    public void move(final String s) {       
        history.move(new Location(s));
    }
    
    public void move(final StorageFolder f) {
        history.move(new Location(f));
    }  
        
    /** move ths view to the specified file object */
    public void move(final FileObject fileToShow) {
        history.move(new Location(fileToShow));        
    }

    // history event listener interface - whenever we hear a position change in history, we reload.
    public void currentChanged(final HistoryEvent<Location> e) {        
        (new OpenDirectoryWorker(e.current(),e.previous())).start();                
    }
    
    // UI component used to  represent an item in the 'up' menu
    private class UpMenuItem extends JMenuItem implements ActionListener {
        private final FileName fn;

        public UpMenuItem(final FileName fn) {
            this.fn = fn;
            final String baseName = fn.getBaseName();
            setText(StringUtils.isEmpty(baseName) ? fn.getURI() : baseName);
            setIcon(icons.defaultFolderIcon());
            addActionListener(this);
        }

        public void actionPerformed(final ActionEvent e) {
            FileNavigator.this.move(fn.getURI());
        }
    }
    // data structure used to manage the different ways we might provide a location to navigate to.
    // also extends JMenu item, which means it can be displayed ina  menu, and if clicked
    // knows how to navigate to this location.
    public class Location extends JMenuItem implements ActionListener{

        /**
         * create a location from a uri string
         * @param s
         */
        public Location(final String s) {
            setText(s);
            addActionListener(this); // listen to clicks on ourselves.
        }
        /** use an existing file object as a location */
        public Location(final FileObject o) {
            this( o.getName().getURI());            
            this.o = o;
        }
        /** use a storage folder as a location */
        public Location(final StorageFolder f) {
            this(f.getUriString());
            this.f = f;
            this.o = f.getFile();       
        }
        private StorageFolder f = null;
        private volatile FileObject o = null;

        public void actionPerformed(final ActionEvent e) {
            history.move(this);
        }       
        public boolean hasResolved() {
            return o != null;
        }
        public synchronized void resolveFileObject() throws FileSystemException {
            if (o == null) {
                logger.debug("retriving file object - RESOLVING");
                o = vfs.resolveFile(getURI());
                if (f != null) {
                    f.setFile(o);
                }
            } 
        }
        /** access the file object. must have called resolveFleObject previously on a bg thread,
         * else will throw.
         * @return
         */
        public FileObject getFileObject() {
            if (o == null) {
                throw new IllegalStateException(getText() + " - Has not yet been resolved");
            } else {
                return o;
            }
        }

        public String getURI() {
            return getText();
        }
        
        public StorageFolder getBookmark() {
            return f;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (! (obj instanceof Location)) {
                return false;
            }
            final Location other = (Location)obj;

            return getText().equals(other.getText());
        }      
        @Override
        public int hashCode() {
            return getText().hashCode();
        }
    } // end of location inner class

// actions     

    /** background worker that finds the parent of a file,
     * then moves the history - which in turn fires events to trigger a view reload
     * calld by the up() api function, but not used in the up menu.
     */
    private class UpWorker extends BackgroundWorker {

        public UpWorker() {
            super(FileNavigator.this.parent,"Finding Parent",Thread.MAX_PRIORITY);
        }
        @Override
        protected Object construct() throws Exception {
            final FileObject p= current().getParent();
            if (p != null) {
                logger.debug("Got parent");
                history.move(new Location(p));
            } else {
                logger.debug("parent was null");
            }
            return null; 
        }
    }
    
    /** backgound worker that performs a refresh */
    private class RefreshWorker extends OpenDirectoryWorker {
        public RefreshWorker() {           
            super(history.current(),history.current());            
        }
        @Override
        protected Object construct() throws Exception {
            // refresh the object first, then just list the directory.
            current().refresh();
            return super.construct();
        }
        
    }
    
    /** background worker that opens a directory */
    private class OpenDirectoryWorker extends BackgroundWorker {

        public OpenDirectoryWorker(final Location current, final Location previous) {
            super(FileNavigator.this.parent,"Listing contents",BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY);
            this.loc = current;
            this.previous = previous;
            if (SwingUtilities.isEventDispatchThread()) {
                fireMoving();
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        fireMoving();
                    }                    
                });
            }
        }
        protected FileObject requested;
        protected FileObject shown;
        protected final Location loc;
        private final Location previous;
        protected boolean isRoot;
        @Override
        protected Object construct() throws Exception {
            // stop listening to whatever was previously displayed.
            if (previous != null && previous.hasResolved()) {
                final FileObject prev = previous.getFileObject();
                prev.getFileSystem().removeListener(prev,FileNavigator.this);                
            }

            // now load the new one.
                reportProgress("Resolving directory");
                loc.resolveFileObject();                                
                requested = loc.getFileObject();
                reportProgress("Listing children");
                if (requested.getType().hasChildren()) {
                    shown = requested;
                } else {
                    shown = requested.getParent();
                    history.replace(new Location(shown)); // replace in the history what was requested with what we're actually sowing
                }
                if (shown == null) {
                    return null;
                }
                // populate the children.
                final EventList files = model.getChildrenList();
                // special case - if we're looking for a particular file, and it's not in the folder, do a refresh.
                if (shown != requested && null == shown.getChild(requested.getName().getBaseName())) {
                    shown.refresh();
                }
                final FileObject[] children = shown.getChildren();
                
                try {
                    files.getReadWriteLock().writeLock().lock();
                    files.clear();
                    if (! ArrayUtils.isEmpty(children)) {
                        files.addAll(Arrays.asList(children));
                    }
                } finally {
                    files.getReadWriteLock().writeLock().unlock();
                }
                // populate the parents.
                final java.util.List parents = new java.util.ArrayList();                    
                upList.clear();
                FileName fn = shown.getName().getParent();
                while(fn != null) {
                    parents.add(fn);
                    fn = fn.getParent();
                }
                // listen for changes.
                final FileSystem fileSystem = shown.getFileSystem();
                isRoot = shown == fileSystem.getRoot();
                // Dave points out a bug in default impl of FileSystem
                // adding a listener twice means you receive two notifications.
                // so would like to check whether we're already listening to this,
                // but can't - no api method for this.
                // instead, remove the listener, and then add back again.
                // only occurs on 'refresh' - when navigating to same dir.
                // and when clicking the 'back' button.
               // fileSystem.removeListener(shown,FileNavigator.this);
                fileSystem.addListener(shown,FileNavigator.this);
                reportProgress("Completed");
                return parents;
        }
        // update the ui.
        @Override
        protected void doFinished(final Object result) {
            final List<FileName> parents = (java.util.List<FileName>) result;
            upList.clear();
            for (final FileName name : parents) {
                upList.add(new UpMenuItem(name));                
            }
            upAction.setEnabled(!upList.isEmpty());
            backAction.setEnabled(history.hasPrevious());
            forwardAction.setEnabled(history.hasNext());
                        
            loc.setText(loc.getURI());
            if (requested != shown) { // we're meant to be showing a child of this folder.
                final int ix = model.getChildrenList().indexOf(requested);
                model.getSelection().setSelectionInterval(ix,ix);
            }  
            if (loc.getBookmark() != null) {
                fireMovedToBookmark(isRoot,loc.getBookmark());
            } else {
                fireMoved(isRoot);
            }
        }

    }

 // file listener interface - call refresh on a bg thread.
    public void fileChanged(final FileChangeEvent event) throws Exception {
        refresh();
    }
    public void fileCreated(final FileChangeEvent event) throws Exception {
        refresh();
    }
    public void fileDeleted(final FileChangeEvent event) throws Exception {
        refresh();
    }

    /** rest history, and go home */
    public void reset() {
//        if (current().getFileSystem() instanceof MyspaceFileSystem) { //do all views - whether currently showing mysopace or not - as might have some previous state..
            history.reset(); // necessary, as it holds onto references to file objects - and some might be myspace related.
            home();
  //      }
    }

    private final Action goHomeAction = new GoHomeAction();
    private final Action goWorkspaceAcgtion = new GoWorkspaceAction();
    private final Action upAction = new UpAction();
    private final Action backAction = new BackAction();
    private final Action forwardAction = new ForwardAction();

    private class GoHomeAction extends AbstractAction {

        public GoHomeAction() {
            super("Home",IconHelper.loadIcon("home16.png"));
            putValue(SHORT_DESCRIPTION,"Go to home folder");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_H,UIComponentMenuBar.SHIFT_MENU_KEYMASK));
        }
        
        public void actionPerformed(final ActionEvent e) {
            home();
        }
    }
    
    private class GoWorkspaceAction extends AbstractAction {
    
        public GoWorkspaceAction() {
            super("VO Workspace",IconHelper.loadIcon("networkdisk16.png"));
            putValue(SHORT_DESCRIPTION,"Go to VO Workspace");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_W,UIComponentMenuBar.SHIFT_MENU_KEYMASK));
             
        }
        public void actionPerformed(final ActionEvent e) {
            move("workspace:///");
        }                
    }
    
    private class UpAction extends AbstractAction {

        public UpAction() {
            super("Enclosing Folder");
            putValue(SHORT_DESCRIPTION,"Move up to the enclosing folder");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_UP,UIComponentMenuBar.MENU_KEYMASK));
            setEnabled(false);
        }
        public void actionPerformed(final ActionEvent e) {
            up();
        }
    }
    private class BackAction extends AbstractAction {

        public BackAction() {
            super("Back");
            putValue(SHORT_DESCRIPTION,"Move up to previous location");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET,UIComponentMenuBar.MENU_KEYMASK));
            setEnabled(false);
        }
        public void actionPerformed(final ActionEvent e) {
            previous();
        }
    }
    private class ForwardAction extends AbstractAction {

        public ForwardAction() {
            super("Forward");
            putValue(SHORT_DESCRIPTION,"Move up to the next location");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET,UIComponentMenuBar.MENU_KEYMASK));
            setEnabled(false);
            
        }
        public void actionPerformed(final ActionEvent e) {
            next();
        }
    }

    /**
     * @return the goHomeAction
     */
    public final Action getGoHomeAction() {
        return this.goHomeAction;
    }
    /**
     * @return the goWorkspaceAcgtion
     */
    public final Action getGoWorkspaceAcgtion() {
        return this.goWorkspaceAcgtion;
    }
    /**
     * @return the backAction
     */
    public final Action getBackAction() {
        return this.backAction;
    }
    /**
     * @return the forwardAction
     */
    public final Action getForwardAction() {
        return this.forwardAction;
    }
    /**
     * @return the upAction
     */
    public final Action getUpAction() {
        return this.upAction;
    }
    

}
