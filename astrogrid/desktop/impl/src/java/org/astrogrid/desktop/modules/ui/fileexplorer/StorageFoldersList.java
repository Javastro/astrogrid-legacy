/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.folders.Folder;
import org.astrogrid.desktop.modules.ui.folders.StorageFolder;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;

/** View and Controller for a set of storage 'roots'. - either roots of mounted filesystems,
 * or shortcuts to favorite locations within these mounts. 
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 200710:24:52 PM
 */
public class StorageFoldersList extends JList implements  ListSelectionListener,MouseListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(StorageFoldersList.class);
    private final FileSystemManager vfs;

	public StorageFoldersList(final EventList folderList, final UIComponent parent,final FileSystemManager vfs) {
		this.parent = parent;
        this.vfs = vfs;
		CSH.setHelpIDString(this,"files.bookmarks");
		this.folderList = folderList;
		this.delete = new DeleteBookmarkAction();
		this.edit = new EditBookmarkAction();
		this.create = new NewBookmarkAction();
		
		setTransferHandler(new StorageFoldersListTransferHandler());
		setDragEnabled(true);
		
		setBorder(BorderFactory.createEmptyBorder());
		setModel(new EventListModel(folderList));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectedIndex(0);
		addListSelectionListener(this);
		setCellRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
				final JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
				final StorageFolder f = (StorageFolder)value;				
				l.setText(f == null ? "" : f.getName());
				l.setIcon(f == null ? null : f.getIcon());
				if (f.getDescription() != null) {
				    l.setToolTipText("<html><b>" + f.getUriString() + "</b><br>" + f.getDescription());
				} else {
				    l.setToolTipText(f.getUriString());
				}
				return l;
			}
		});	
		
		// build a popup menu
		popup = new JPopupMenu();
		popup.add(create);
		popup.add(delete);
		popup.add(edit);
		addMouseListener(this);
		
	}
	
	private class NewBookmarkAction extends AbstractAction {

        public NewBookmarkAction() {
            super("New Bookmark" + UIComponentMenuBar.ELLIPSIS,IconHelper.loadIcon("editadd16.png"));
            putValue(SHORT_DESCRIPTION,"Add a new bookmark");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_N,UIComponentMenuBar.SHIFT_MENU_KEYMASK));
        }
        public void actionPerformed(final ActionEvent e) {
            final StorageFolder f = new StorageFolder();
            final Component pc = parent.getComponent();
            final Window w = pc instanceof Window
                     ? (Window) pc
                     : SwingUtilities.getWindowAncestor(pc);
            final BaseDialog d;
            if (w instanceof Frame) {
                d = new CreateBookmarkDialog((Frame)w, f);
            } else if (w instanceof Dialog) {
                d = new CreateBookmarkDialog((Dialog)w, f);          
            } else {
                d = new CreateBookmarkDialog(f);         
            }
            d.setVisible(true);     
        }
        /** dialog for creating a new bookmark */
        private final class CreateBookmarkDialog extends BookmarkDialog {
        
            public CreateBookmarkDialog(final Dialog w, final StorageFolder f2) {
                super(w, f2);
            }
        
            public CreateBookmarkDialog(final Frame owner, final StorageFolder f)
                    throws HeadlessException {
                super(owner, f);
            }
        
            public CreateBookmarkDialog(final StorageFolder f2) {
                super(f2);
            }
            
            protected void init() {
                super.init();
                setTitle("Create Bookmark");
                getBanner().setTitle("Enter the new bookmark details");
                name.setText("untitled");
                uri.setText(SystemUtils.getUserHome().toURI().toString());
                
            }
            
            protected void saveChanges(){
                folderList.add(f);  // add the new folder to the dialog            
            }
        }

	}
	
	private class DeleteBookmarkAction extends AbstractAction {

        public DeleteBookmarkAction() {
            super("Delete Bookmark",IconHelper.loadIcon("editremove16.png"));
            putValue(SHORT_DESCRIPTION,"Remove a bookmark");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,UIComponentMenuBar.SHIFT_MENU_KEYMASK));
            setEnabled(false);
        }

        public void actionPerformed(final ActionEvent e) {
            final Folder f = (Folder)getSelectedValue();
            if (f != null) {
                folderList.remove(f);
                setSelectedIndex(0);
            }            
        }
	}
	private class EditBookmarkAction extends AbstractAction {

        public EditBookmarkAction() {
            super("Edit Bookmark" + UIComponentMenuBar.ELLIPSIS,IconHelper.loadIcon("edit16.png"));
            putValue(SHORT_DESCRIPTION,"Edit a bookmark");
            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_E,UIComponentMenuBar.MENU_KEYMASK));
            setEnabled(false);
        }
        public void actionPerformed(final ActionEvent e) {
            final StorageFolder f = (StorageFolder)getSelectedValue();
            final int ix = getSelectedIndex();
            if (f != null) {
                final Component pc = parent.getComponent();
                final Window w = pc instanceof Window
                         ? (Window) pc
                         : SwingUtilities.getWindowAncestor(pc);
                final BaseDialog d;
                if (w instanceof Frame) {
                    d = new EditBookmarkDialog((Frame)w, f, ix);
                } else if (w instanceof Dialog) {
                    d = new EditBookmarkDialog((Dialog)w, f, ix);          
                } else {
                    d = new EditBookmarkDialog(f,ix);         
                }
                d.setVisible(true);     

            }
        }
        /** dialog for editing an existing bookmark */
        private final class EditBookmarkDialog extends BookmarkDialog {
        
            private final int ix;
            public EditBookmarkDialog(final Frame owner, final StorageFolder f, final int ix)
                    throws HeadlessException {
                super(owner, f);
                this.ix = ix;
            }
        
            public EditBookmarkDialog(final Dialog w, final StorageFolder f2, final int ix2) {
                super(w, f2);
                this.ix = ix2;
            }
        
            public EditBookmarkDialog(final StorageFolder f2, final int ix2) {
                super(f2);
                this.ix = ix2;
            }
            protected void init() {
                super.init();
                setTitle("Edit Bookmark");
                getBanner().setTitle("Editing " + this.f.getName());
                name.setText(this.f.getName());
                uri.setText(this.f.getUriString());            
            }
            
            protected void saveChanges() {
                folderList.set(this.ix,this.f);
            }
        }
	}
	
	
	
	private final Action edit;
	private final Action create;
	private final Action delete;
	private final JPopupMenu popup;
	private final EventList folderList;
	private final UIComponent parent;

	// listening to myself.
	public void valueChanged(final ListSelectionEvent e) {
		if (e.getSource() != this || e.getValueIsAdjusting()) {
			return; // ignore
		}
		final Folder f = (Folder) getSelectedValue();
		delete.setEnabled(f != null);
		edit.setEnabled(f != null);		
	}

//	 listen for popup menu trigger.
	public void mouseClicked(final MouseEvent e) {
	}

	public void mouseEntered(final MouseEvent e) {
	}

	public void mouseExited(final MouseEvent e) {
	}

	public void mousePressed(final MouseEvent e) {
		checkForTriggerEvent( e );
	}

	public void mouseReleased(final MouseEvent e) {
		checkForTriggerEvent( e);
	}

//	 determine whether event should trigger popup menu
	private void checkForTriggerEvent( final MouseEvent event ){
	   if ( event.isPopupTrigger() ) {
	      popup.show( event.getComponent(),
	         event.getX(), event.getY() );
	   }
	}	
	
	private final static DataFlavor[] inputFlavors = new DataFlavor[]{
	    VoDataFlavour.LOCAL_FILEOBJECT
	    //	,VoDataFlavour.LOCAL_FILEOBJECT_LIST don't know what to do with a multiple selection at the moment
	    ,VoDataFlavour.LOCAL_URI
	    ,VoDataFlavour.LOCAL_URL
	    ,VoDataFlavour.URL
		,VoDataFlavour.URI_LIST
		,VoDataFlavour.URI_LIST_STRING
		,VoDataFlavour.STRING
	};
	/** transfer handler - accepts drops of file objects, and also allows export of uri of 
	 * a storage folder */
	private class StorageFoldersListTransferHandler extends TransferHandler {

	    public int getSourceActions(final JComponent c) {
	        return COPY;
	    }
	    public boolean canImport(final JComponent comp, final DataFlavor[] transferFlavors) {
	        // don't want to import into self - how do I prevent that?
	        // can't here - but can in thje 'import data' method
	        for (int i = 0; i < transferFlavors.length; i++) {
                if (ArrayUtils.contains(inputFlavors,transferFlavors[i])){
                    return true;
                }
            }
	        return false;	        
	    }
	    
	    public boolean importData(final JComponent comp, final Transferable t) {
	        if (t instanceof StorageFoldersListTransferable || ! canImport(comp,t.getTransferDataFlavors())) {
	            return false; // don't import things that originate here
	        }
	        try {
                StorageFolder sf = null;
                if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT)) {
                    final FileObject fo = (FileObject)t.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT);
                    if (fo.getType().hasChildren()) {
                        sf = build(fo.getName());
                        sf.setFile(fo);
                    } // rejects non-folders.
                } else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI) ){
                    sf = build(t.getTransferData(VoDataFlavour.LOCAL_URI));
                } else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URL)) {
                    sf = build(t.getTransferData(VoDataFlavour.LOCAL_URL));
                } else if (t.isDataFlavorSupported(VoDataFlavour.URL)) {
                    final URL u = (URL) t.getTransferData(VoDataFlavour.URL);
                    // this is a url received from outside. it probably doesn't follow java's
                    // broken conventions for a file url - so we need to 'break it' before passing it on.
                    sf = build(VoDataFlavour.mkJavanese(u));
                } else if (t.isDataFlavorSupported(VoDataFlavour.URI_LIST)) {
                    BufferedReader r = null;
                    try {
                        final InputStream is = (InputStream)t.getTransferData(VoDataFlavour.URI_LIST);
                        r = new BufferedReader(new InputStreamReader(is));
                        String line;
                        // we only parse the first in the list.
                        if ((line = r.readLine()) != null) {
                            // must be a url (as we've registered a url handler for all our types of uri)
                            final URL u = VoDataFlavour.mkJavanese(new URL(line.trim()));
                            sf = build(u);
                        }                        
                    } finally {
                        if (r != null) {
                            try { r.close(); } catch (final IOException e) { /* ignored*/ }
                        }
                    }
                } else if (t.isDataFlavorSupported(VoDataFlavour.URI_LIST_STRING)) {
                        String s= (String)t.getTransferData(VoDataFlavour.URI_LIST_STRING);
                        final StringTokenizer tok = new StringTokenizer(s);
                        s = tok.nextToken();
                        sf = build(s);
                } else if(t.isDataFlavorSupported(VoDataFlavour.STRING)) {
                       sf = build(t.getTransferData(VoDataFlavour.STRING));
                } else {
                    logger.warn("Unknown type of transferable " + t);
                }
                if (sf != null) {
                    folderList.add(sf);     
                    return true;
                } 
            } catch (final IOException e) {
                parent.showTransientError("Failed to import",ExceptionFormatter.formatException(e));
            } catch (final UnsupportedFlavorException x) {
                parent.showTransientError("Failed to import",ExceptionFormatter.formatException(x));     
            }
            return false;
	    }
	    
	    /** attempt to build a storage folder from a string
	     * on parse error, will log error, and reutrn null;
	     * @param s
	     * @return
	     */
	   

	    
	    /** attempt to build a storage folder from an object (via vfs parsing of  toString()) */
	    private StorageFolder build(final Object o) {
            try {
                final FileName fn = vfs.resolveURI(o.toString());                
                return build(fn);
            } catch (final FileSystemException x) {
                
                parent.showTransientError("Failed to parse as filename",ExceptionFormatter.formatException(x));
            }	        
            return null;
	    }
	    
	    private StorageFolder build(final FileName fn) {
	        try {
            final StorageFolder sf = new StorageFolder();
            sf.setName(fn.getBaseName());
            sf.setUriString(fn.getURI());
            return sf;
            } catch (final URISyntaxException e) {
                parent.showTransientError("Failed to parse dropped uri",ExceptionFormatter.formatException(e));
            }
            return null;
	    }
	    
	    protected Transferable createTransferable(final JComponent c) {
	        final StorageFolder s = (StorageFolder)getSelectedValue();
	        
	        return new StorageFoldersListTransferable(s);
	    }
	}
	
	/** transferable for a storage folder.
	 * at the moment this is for export only - no real use in the application itself
	 * so just returns an externalizable 'uri-list'
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Oct 31, 20079:56:45 AM
	 */
	private static class StorageFoldersListTransferable implements Transferable {

        private final URI uri;

        public StorageFoldersListTransferable(final StorageFolder sf) {
            this.uri = sf.getUri();
        }

        public Object getTransferData(final DataFlavor flavor)
                throws UnsupportedFlavorException, IOException {
            if (! isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            if (VoDataFlavour.URI_LIST.equals(flavor)) {
                return new ByteArrayInputStream(uri.toString().getBytes());
            } else if (VoDataFlavour.URI_LIST_STRING.equals(flavor)) {
                return uri.toString();
            } else {
                // can't really happen
                throw new UnsupportedFlavorException(flavor);
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavs;
        }

        public boolean isDataFlavorSupported(final DataFlavor flavor) {
            return ArrayUtils.contains(flavs,flavor);
        }
        
        private static final DataFlavor[] flavs = new DataFlavor[] {
            VoDataFlavour.URI_LIST
            ,VoDataFlavour.URI_LIST_STRING
        };
	}

	/** abstract class for editing a dialog
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 21, 200710:47:30 AM
     */
    private abstract class BookmarkDialog extends BaseDialog {
        /**
         * 
         */
        protected final StorageFolder f;

        protected JTextField name = new JTextField();
        protected JTextField uri = new JTextField();
        protected void init(){
            setModal(false);
            setDialogMode(BaseDialog.OK_CANCEL_DIALOG);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getBanner().setSubtitleVisible(true);
            
            final JPanel cp = (JPanel)getContentPane();
            final CellConstraints cc = new CellConstraints();
            final PanelBuilder pb = new PanelBuilder(new FormLayout("right:d,2dlu,fill:100dlu:grow","d,2dlu,d"),cp);
            pb.addLabel("Name :",cc.xy(1,1));
            pb.add(name,cc.xy(3,1));
            pb.addLabel("Location :",cc.xy(1,3));
            pb.add(uri,cc.xy(3,3));
            pack();
            setLocationRelativeTo(parent.getComponent());
            
        }
    
        public BookmarkDialog(final Frame owner, final StorageFolder f)
                throws HeadlessException {
            super(owner);
            this.f = f;
            init();
        }
    
        public BookmarkDialog(final Dialog w, final StorageFolder f2) {
            super(w);
            f = f2;
            init();
        }
    
        public BookmarkDialog(final StorageFolder f2) {
            f = f2;
            init();
        }
    
        public void ok() {
            if (StringUtils.isEmpty(name.getText()) || StringUtils.isEmpty(uri.getText())) {
                return;
            }
            this.f.setName(name.getText().trim());
            try {
                this.f.setUriString(uri.getText().trim());
            } catch (final URISyntaxException e) {
                getBanner().setSubtitle("Invalid location entered - please try again");
                return;
            }
            // although we're aliasing, do this to notify the folder list that things have changed.
            super.ok(); // closes the dialogue.
            saveChanges();
        }
        /** subclasses should implement this to save changes to folder */
        protected abstract void saveChanges();
    }

    /**
     * @return the edit
     */
    public final Action getEdit() {
        return this.edit;
    }

    /**
     * @return the create
     */
    public final Action getCreate() {
        return this.create;
    }

    /**
     * @return the delete
     */
    public final Action getDelete() {
        return this.delete;
    }

}
