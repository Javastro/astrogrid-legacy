/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.modules.dialogs.ConfirmDialog;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.actions.BulkCopyWorker;
import org.astrogrid.desktop.modules.ui.comp.JPromptingTextField;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;

/**
 * Helper class that manages the file upload when a file:// reference is provided.
 * factored out of task parameters form to keep the code a little more modular.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 6, 200712:38:39 PM
 */
public class LocalFileUploadAssistant implements PropertyChangeListener, FunctionList.AdvancedFunction{
    

    public LocalFileUploadAssistant(UIComponent parent, FileSystemManager vfs, EventList components) {
        super();
        this.parent = parent;
        this.vfs = vfs;
        this.unused = new FunctionList(components,this); // just used for side effects - nice model for adding / removing listeners.
        this.prefs = Preferences.userNodeForPackage(LocalFileUploadAssistant.class);
    }
    private final UIComponent parent;
    private final FunctionList unused;
    private final FileSystemManager vfs;
    // used to record whether this is the 'first run'
    private final Preferences prefs;
    private static final String FIRST_RUN_KEY = "isFirstRun";

// interface to monitor changes to the parameter list, and listen to each component accordingly.
    public void dispose(Object sourceValue, Object transformedValue) {
        ((JPromptingTextField)transformedValue).removePropertyChangeListener(JPromptingTextField.VALUE_PROPERTY,this);
    }

    public Object evaluate(Object sourceValue) {
        JPromptingTextField indirectField = ((AbstractTaskFormElement)sourceValue).getIndirectField();
        indirectField.addPropertyChangeListener(JPromptingTextField.VALUE_PROPERTY,this); 
        return indirectField;
    }
    public Object reevaluate(Object sourceValue, Object transformedValue) {
        ((JPromptingTextField)transformedValue).removePropertyChangeListener(JPromptingTextField.VALUE_PROPERTY,this);
        JPromptingTextField indirectField = ((AbstractTaskFormElement)sourceValue).getIndirectField();
        indirectField.addPropertyChangeListener(JPromptingTextField.VALUE_PROPERTY,this);
        return indirectField;
    }
    
    // triggered when the value of a uri field changes.
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() != null 
                && ! evt.getNewValue().equals(evt.getOldValue()))  {
            try {
                URI u = new URI(evt.getNewValue().toString());
                if (u.getScheme().equals("file")) {
                    offerToRelocateFile(u,(JFormattedTextField) evt.getSource());
                }
            } catch (URISyntaxException x) {
                // don't care.
            }
        }
    }

// the business end.
    /**
     * @param u uri to upload from 
     * @param resultField ui field to update with new location.
     * @return
     */
    private void offerToRelocateFile(final URI u, final JFormattedTextField resultField) {
        final boolean loggedIn = parent.getContext().getLoggedInModel().isEnabled();
        final boolean firstRun = prefs.getBoolean(FIRST_RUN_KEY,true);
        if ( ! loggedIn || firstRun) {
            ConfirmDialog.newConfirmDialog(parent.getComponent(),"Upload this file to VOSpace?"
                    , "<html>You have selected a file on a local disk. Local disks cannot be accessed by remote applications."
                    +"<br>Do you want to have this file uploaded to VOSpace, where it will be accessible?"
                    + (! loggedIn ? "<br>(This will require you to login)" :"")
                    , new Runnable() {
                public void run() {
                    if (firstRun) {
                        prefs.putBoolean(FIRST_RUN_KEY,false);
                    }                    
                    relocateFile(u,resultField);
                }
            }
            ).setVisible(true);
        } else {
            relocateFile(u,resultField);            
        }       
    }
    
    private void relocateFile(final URI u,final JFormattedTextField resultField) {
        resultField.setEnabled(false);
        List upload = new ArrayList();
        upload.add(u);
        parent.showTransientMessage("Uploading","Copying " + u);
        new BulkCopyWorker(vfs,parent,workingDir,upload){
            protected void doFinished(Object result) {
                super.doFinished(result);
                Map outcome = (Map)result;
                // I know very well it's' a single-item map at this point.
                Object outURI = outcome.values().iterator().next();
                if (outURI instanceof String) {
                    resultField.setValue((String)outURI);
                } 
            }
            protected void doAlways() {
                super.doAlways();
                resultField.setEnabled(true);
            }
        }.start();

        return;
    }
    
    private final static URI workingDir = URI.create("workspace:///cea-working/");


}
