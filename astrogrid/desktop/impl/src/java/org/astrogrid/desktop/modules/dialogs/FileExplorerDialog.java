/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIDialogueComponentImpl;
import org.astrogrid.desktop.modules.ui.actions.Activity;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.fileexplorer.StorageView;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationEvent;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationListener;
import org.astrogrid.desktop.modules.ui.folders.StorageFolder;

import ca.odell.glazedlists.matchers.Matcher;

import com.l2fprod.common.swing.JTaskPane;

/** Implementation of a file resource chooser that uses the filemanaer (storage view) code.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 30, 20079:49:42 AM
 */
public class FileExplorerDialog extends UIDialogueComponentImpl implements DocumentListener, Matcher, NavigationListener, ActionListener{

    private final StorageView view;
    private URI uri;
    private final JButton okButton;
    private boolean selectDirectories = false;
    private boolean localEnabled = true;
    private boolean urlEnabled = true;
    private boolean vospaceEnabled= true;
    private final Timer validationTimer;
    private final JTextField filename; 

    /**
     * @param foldersList 
     * @param vfs 
     * @param iconFinder 
     * @param comm 
     * 
     */
    public FileExplorerDialog(final UIContext context, final TypesafeObjectBuilder builder) {
        super(context,"File Chooser","dialog.file");
        // based on code reading...
        this.okButton = getOkButton();
        final ActivitiesManager acts = new NullActivitiesManager();
        this.view = builder.createStorageView(this,acts);
        view.setSingleSelectionMode(true);
        view.setShowViewComboInMenuBar(true);
        view.getNavigator().addNavigationListener(this);
        final JPanel mainPanel = getMainPanel();

        // assemble new UI.
        final JComponent foldersPanel = view.getFoldersList();
        final JComponent mainViewPanel = view.getMainPanel();
        final JComponent mainButtons = view.getMainButtons();
        // combine LHS and RSH
        final JSplitPane lrPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,foldersPanel,mainViewPanel);
        lrPane.setDividerLocation(200);
        lrPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(lrPane,BorderLayout.CENTER); 
        // add buttonbar on top.
        mainPanel.add(mainButtons,BorderLayout.NORTH);
        
        // Create a new panel for a filename text field.
        this.filename = new JTextField(40);
        filename.getDocument().addDocumentListener(this);
        final JPanel p = new JPanel(new FlowLayout());
        nameFieldLabel = new JLabel("File name:");
        p.add(nameFieldLabel);
        p.add(filename);
        // stack both panels together.
        final Box b = new Box(BoxLayout.Y_AXIS);
        b.add(mainPanel);
        b.add(p);
        
        this.getContentPane().add(b);                
        this.setSize(600,400);
        
        this.validationTimer = new Timer(700,this); // validate when the user has been silent for 700ms.
        validationTimer.setCoalesce(true);
        validationTimer.setRepeats(false);
    }

    @Override
    public void cancel() {
        super.cancel();
        setUri(null);
        reset();
    }

    @Override
    public void ok() {
        super.ok();
        try {
            setUri(new URI(StringUtils.replace(selected.getName().getURI()," ","%20")));
        } catch (final URISyntaxException x) {
            //very unlikely to happen, as if this was the case, the ok button woudlnt'
            // be enabled, and so we couldn't have got here.
        }
        reset();
    }

    /** background worker that validates a text input.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Oct 1, 200710:22:37 AM
     */
    private final class ValidationWorker extends BackgroundWorker {
        protected volatile boolean cond;
        private final static String MSG =  "Validating text input";
        /**
         * @param parent
         * @param msg
         */
        private ValidationWorker() {
            super(FileExplorerDialog.this, MSG,BackgroundWorker.SHORT_TIMEOUT,Thread.MAX_PRIORITY);
        }

        @Override
        protected Object construct() throws Exception {
            if (StringUtils.isEmpty(filename.getText())) {
                cond = false;
                return null;
            }
            final FileObject fo = view.getVfs().resolveFile(view.getLocation().getText().trim() + "/" + filename.getText().trim());
            cond = isSchemeValid(fo.getName().getScheme()); 
            if (fo.exists()) {
               cond = cond && (selectDirectories == fo.getType().hasChildren());
            } 
            return fo;
        }

        @Override
        protected void doFinished(final Object result) {
            if (this == latest) {
                selected = (FileObject)result;
                okButton.setEnabled(cond);
                parent.setStatusMessage(MSG + " - " + (cond ? "OK" : "INVALID"));
            }
        }

        @Override
        protected void doError(final Throwable ex) { // on error, just disable the button.
            if (this == latest) {
                okButton.setEnabled(false);
                parent.setStatusMessage(MSG + " - INVALID: " + ExceptionFormatter.formatException(ex));
            }
        }
    }

    /** stubbed out class - this functionality is not needed in this usage
     * all it does is listens to current selection, stores the file object, and updates location and filename fields 
     *  */
    private class NullActivitiesManager implements ActivitiesManager {

        public void clearSelection() {
            //does nothing.
        }

        public Transferable getCurrentSelection() {
            return null;
        }


        public JPopupMenu getPopupMenu() {
            return null;
        }

        public JTaskPane getTaskPane() {
            return null;
        }

        // listens to selection events.
        public void setSelection(final Transferable tran) {
            try {
                selected = (FileObject)tran.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT);
                final boolean selectionIsFolder = selected.getType().hasChildren();
                final FileName selectedName = selected.getName();
                view.getLocation().setText(selectedName.getParent().getURI());
                if (selectDirectories == selectionIsFolder) { // either we've got a folder, and we want a folder, or we've got a file and we want a file.
                    filename.setText(selectedName.getBaseName());
                } else { // it's not what we want - set to null;
                    filename.setText(null);
                }
                
                okButton.setEnabled( selectDirectories == selectionIsFolder && isSchemeValid(selectedName.getScheme()));                 
            } catch (final UnsupportedFlavorException x) {
                // unexpected
            } catch (final IOException x) {
                // unexpected, might possibly happen from getType() - though this should have been computed already..
                okButton.setEnabled(false);
                filename.setText(null);
            }
        }

        public Activity getActivity(final Class activityClass) {
            return null;
        }

        public Iterator iterator() {
            return IteratorUtils.emptyIterator();
        }
    }


    public URI getUri() {
        return uri;
    }
    public void setUri(final URI uri) {
        this.uri = uri;
    }

    
    // matcher interface - determines what can be displayed.
    public boolean matches(final Object item) {
        if (item instanceof FileObject) {
            final FileObject fo = (FileObject) item;
            // only remove files when in directory mode - else show both.
            try {
                if (selectDirectories && ! fo.getType().hasChildren()) {
                    return false;
                }
            } catch (final FileSystemException e) {
                return false;
            }
            return isSchemeValid(fo.getName().getScheme());
        } else if (item instanceof StorageFolder ) {
            final StorageFolder f = (StorageFolder)item;
            return isSchemeValid(f.getUri().getScheme());
        } else {
            // unexpected.
            return false;
        }
    }

    //document listener - used to check whether input is valid.
    public void changedUpdate(final DocumentEvent e) {          
        validateLocation();
    }

    public void insertUpdate(final DocumentEvent e) {      
        validateLocation();
    }

    public void removeUpdate(final DocumentEvent e) {
        validateLocation();
    }

    
    private void validateLocation() {
        // interrupt any validation that's currently happening.
        if (latest != null) {
            latest.interrupt();
        }
        if (filename.isFocusOwner()) { // document event came from a user edit.
            validationTimer.restart(); // if no more edits in the timer duration, validate the user input.        
        } else {
            // focus is elsewhere - so this edit we don't need to validate, and it supercedes any pending edits.
            validationTimer.stop();
        }
    }

    /** validate the contents of the location box - called by the validation timer.*/
    public void actionPerformed(final ActionEvent e) {
        // run this in the bg.        
        latest= new ValidationWorker();
        latest.start();
    }
    // keeps track of the latest resolution worker.
    private BackgroundWorker latest;
    
    /**
    // check that current input is acceptable.
     * @param scheme
     * @return
     */
    private boolean isSchemeValid(final String scheme) {
        return (  (localEnabled && "file".equals(scheme))
                || (vospaceEnabled &&( "ivo".equals(scheme) || "workspace".equals(scheme)) )
                || (urlEnabled && URL_SCHEMES.contains(scheme))
             );
    }
    private final static Set URL_SCHEMES = new HashSet();
    static {
        URL_SCHEMES.add("http");
        URL_SCHEMES.add("ftp");
        URL_SCHEMES.add("sftp");
    }
    
    // listen to navigation events.
    public void moved(final NavigationEvent e) {
        selected = view.getNavigator().current();
        // current selection is the folder we've just moved to. do we want to select folders?
        if (selectDirectories) {
            filename.setText(selected.getName().getBaseName());
        } else {
            filename.setText(null);
        }
        okButton.setEnabled(selectDirectories && isSchemeValid(selected.getName().getScheme())); 
    }
    
    private FileObject selected = null;
    private final JLabel nameFieldLabel;

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
       //2422 - don't go home -  view.getNavigator().home();
        filename.setText(null);        
        okButton.setEnabled(false);
        
    }

    public void setChooseDirectories(final boolean enableDirectorySelection) {
        this.selectDirectories = enableDirectorySelection;
        view.installFilter(this); // need to re-call this whenever our filtering conditions change.
        nameFieldLabel.setText(enableDirectorySelection ? "Folder name:" : "File name:");
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
        } catch (final FileSystemException x) {
            okButton.setEnabled(false);
        }    
    }

    public void setLocalEnabled(final boolean enableLocalFilePanel) {
        this.localEnabled = enableLocalFilePanel;
        view.installFilter(this); // need to re-call this whenever our filtering conditions change.
        validateCurrent();        
    }

    public void setUrlEnabled(final boolean enableURIPanel) {
        this.urlEnabled = enableURIPanel;
        view.installFilter(this); // need to re-call this whenever our filtering conditions change.  
        validateCurrent();        
    }

    public void setVospaceEnabled(final boolean enableMySpacePanel) {
        this.vospaceEnabled = enableMySpacePanel;
        view.installFilter(this); // need to re-call this whenever our filtering conditions change.
        validateCurrent();        
    }

    
    public FileSystemManager getVFS() {
        return view.getVfs();
    }

    /** overridden to navigate to first appropriate position, if current position isn't suitable. */
    @Override
    public void show() {
        final FileObject currentPosition = view.getNavigator().current();
        if (! matches(currentPosition)) { // the current position of the dialogue doesn't match the selection criteria.
            // find the first item in the list that does match the selection criteria.
            final ListModel model = view.getFoldersList().getModel();
            for (int i = 0 ; i < model.getSize(); i++) {
                final StorageFolder folder = (StorageFolder) model.getElementAt(i);
                if (matches (folder)) {
                    view.getNavigator().move(folder);
                    break;
                }
                    
            }
        }
        super.show();
    }
    
}
