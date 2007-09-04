/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import java.awt.BorderLayout;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.fileexplorer.StorageView;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationEvent;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationListener;
import org.astrogrid.desktop.modules.ui.folders.StorageFolder;

import ca.odell.glazedlists.matchers.Matcher;

import com.l2fprod.common.swing.BaseDialog;
import com.l2fprod.common.swing.JTaskPane;

/** Implementation of a resource chooser that uses the filemanaer (storage view) code.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 30, 20079:49:42 AM
 */
public class FileExplorerBaseDialog extends BaseDialog implements DocumentListener, Matcher, NavigationListener{

    private final UIComponentImpl parent;
    private final StorageView view;
    private URI uri;
    private final JButton okButton;
    private boolean selectDirectories = false;
    private boolean localEnabled = true;
    private boolean urlEnabled = true;
    private boolean vospaceEnabled= true; 

    /**
     * @param foldersList 
     * @param vfs 
     * @param iconFinder 
     * @param comm 
     * 
     */
    public FileExplorerBaseDialog(UIContext context, TypesafeObjectBuilder builder) {
        super();
        getBanner().setVisible(false);
        // based on code reading...
        this.okButton = getRootPane().getDefaultButton();
        this.parent = new UIComponentImpl(context);
        ActivitiesManager acts = new NullActivitiesManager();
        this.view = builder.createStorageView(parent,acts);
        view.setSingleSelectionMode(true);
        view.getNavigator().addNavigationListener(this);
        view.getLocation().getDocument().addDocumentListener(this);
        JPanel main = parent.getMainPanel();
        parent.remove(main); // remove from this uicomponent - as we want to embed it in the dialog.
        // rest of parent remains, invisible.

        // assemble new UI.
        JComponent foldersPanel = view.getHierarchiesPanel();
        JComponent mainPanel = view.getMainPanel();
        JComponent mainButtons = view.getMainButtons();
        // combine LHS and RSH
        JSplitPane lrPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,foldersPanel,mainPanel);
        lrPane.setDividerLocation(200);
        lrPane.setBorder(BorderFactory.createEmptyBorder());
        main.add(lrPane,BorderLayout.CENTER); 
        // add buttonbar on top.
        main.add(mainButtons,BorderLayout.NORTH);

        this.getContentPane().add(main);
        this.setModal(true);
        this.setSize(600,400);
    }

    public void cancel() {
        super.cancel();
        setUri(null);
        reset();
    }

    public void ok() {
        super.ok();
        try {
            setUri(new URI(StringUtils.replaceChars(selected.getName().getURI(),' ','+')));
        } catch (URISyntaxException x) {
            //very unlikely to happen, as if this was the case, the ok button woudlnt'
            // be enabled, and so we couldn't have got here.
        }
        reset();
    }

    /** stubbed out class - this functionality is not needed in this usage
     * all it does is listens to current selection and updates location field - as this is where we're getting the filename from.
     *  */
    private class NullActivitiesManager implements ActivitiesManager {

        public void clearSelection() {
        }

        public Transferable getCurrentSelection() {
            return null;
        }

        public JMenu getMenu() {
            return null;
        }

        public JPopupMenu getPopupMenu() {
            return null;
        }

        public JTaskPane getTaskPane() {
            return null;
        }

        // listens to selection events.
        public void setSelection(Transferable tran) {
            try {
                URI uri = (URI) tran.getTransferData(VoDataFlavour.LOCAL_URI); // leave it to the transferable code to create a 'good' uri.
                view.getLocation().setText(uri.toString());
                selected = (FileObject)tran.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT);
                okButton.setEnabled( (selectDirectories == selected.getType().hasChildren()) && isSchemeValid(selected.getName().getScheme()));                 
            } catch (UnsupportedFlavorException x) {
                // unexpected
            } catch (IOException x) {
                // unexpected, might possibly happen from getType() - though this should have been computed already..
                okButton.setEnabled(false);
            }
        }
    }


    public URI getUri() {
        return uri;
    }
    public void setUri(URI uri) {
        this.uri = uri;
    }

    
    // matcher interface - determines what can be displayed.
    public boolean matches(Object item) {
        if (item instanceof FileObject) {
            FileObject fo = (FileObject) item;
            // only remove files when in directory mode - else show both.
            try {
                if (selectDirectories && ! fo.getType().hasChildren()) {
                    return false;
                }
            } catch (FileSystemException e) {
                return false;
            }
            return isSchemeValid(fo.getName().getScheme());
        } else if (item instanceof StorageFolder ) {
            StorageFolder f = (StorageFolder)item;
            return isSchemeValid(f.getUri().getScheme());
        } else {
            // unexpected.
            return false;
        }
    }

    //document listener - checks whether input is valid.
    public void changedUpdate(DocumentEvent e) {          
        validateLocation();
    }

    public void insertUpdate(DocumentEvent e) {      
        validateLocation();
    }

    public void removeUpdate(DocumentEvent e) {
        validateLocation();
    }
    /** validate the contents of the location box */
    private void validateLocation() {
        if (! view.getLocation().isFocusOwner()) { // focus is elsewhere - so this event isn't something we need to validate
            return;
        }      
        //@todo probably want a minor timer here - as in query sizer.
        if (latest != null) {
            latest.interrupt();
        }
        // run this in the bg.
        latest= new BackgroundWorker(parent,"Resolving user input") {
            protected boolean cond;
            protected Object construct() throws Exception {
                FileObject fo = view.getVfs().resolveFile(view.getLocation().getText());
                cond = isSchemeValid(fo.getName().getScheme()); 
                if (fo.exists()) {
                   cond = cond && (selectDirectories == fo.getType().hasChildren());
                } 
                return fo;
            }
            protected void doFinished(Object result) {
                if (this == latest) {
                    selected = (FileObject)result;
                    okButton.setEnabled(cond);
                }
            }
            protected void doError(Throwable ex) {
                if (this == latest) {
                    okButton.setEnabled(false);
                }
            }
        };
        latest.start();
    }
    // keeps track of the latest resolution worker.
    private BackgroundWorker latest;
    
    /**
    // check that current input is acceptable.
     * @param scheme
     * @return
     */
    private boolean isSchemeValid(String scheme) {
        return (  (localEnabled && "file".equals(scheme))
                || (vospaceEnabled &&( "ivo".equals(scheme) || "workspace".equals(scheme)) )
                || (urlEnabled && schemes.contains(scheme))
             );
    }
    private final static List schemes = new ArrayList();
    static {
        schemes.add("http");
        schemes.add("ftp");
        schemes.add("sftp");
    }
    
    // listen to navigation events.
    public void moved(NavigationEvent e) {
        selected = view.getNavigator().current();
        // current selection is the folder we've just moved to. do we want to select folders?
        okButton.setEnabled(selectDirectories && isSchemeValid(selected.getName().getScheme())); 
    }
    
    private FileObject selected = null;

    public void moving() {
        // ignored.
    }
   
//  configuration methods
    public void reset() {
        setLocalEnabled(true);
        setVospaceEnabled(true);
        setUrlEnabled(true);
        setChooseDirectories(false);    
        
        // move back home.
        view.getNavigator().home();
        validateCurrent();
        
    }

    public void setChooseDirectories(boolean enableDirectorySelection) {
        this.selectDirectories = enableDirectorySelection;
        view.installFilter(this); // need to re-call this whenever our filtering conditions change.
        validateCurrent();
    }
    
    /**
     * validate the current selection
     */
    private void validateCurrent() {
        if (selected == null) {
            okButton.setEnabled(false);
            return;
        }
        try {
            okButton.setEnabled( (selectDirectories == selected.getType().hasChildren()) && isSchemeValid(selected.getName().getScheme()));
        } catch (FileSystemException x) {
            okButton.setEnabled(false);
        }    
    }

    public void setLocalEnabled(boolean enableLocalFilePanel) {
        this.localEnabled = enableLocalFilePanel;
        view.installFilter(this); // need to re-call this whenever our filtering conditions change.
        validateCurrent();        
    }

    public void setUrlEnabled(boolean enableURIPanel) {
        this.urlEnabled = enableURIPanel;
        view.installFilter(this); // need to re-call this whenever our filtering conditions change.  
        validateCurrent();        
    }

    public void setVospaceEnabled(boolean enableMySpacePanel) {
        this.vospaceEnabled = enableMySpacePanel;
        view.installFilter(this); // need to re-call this whenever our filtering conditions change.
        validateCurrent();        
    }


    
}
