/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** abstract class for when either some kind of resource, or a file, is acceptable.
 * 
 * a merge of the funtionality provided vby the AbstractFileActivity and AbstractResourceActivity classes
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 16, 20078:21:37 PM
 */
public  abstract class AbstractFileOrResourceActivity extends AbstractActivity{
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory
			.getLog(AbstractFileOrResourceActivity.class);

	private FileObject[] currentFiles = null;
	private Resource[] currentResources = null;
	
// machinery for files
	public void oneSelected(FileObject fo){
        if (invokable(fo)) {
            setEnabled(true);
            currentFiles =new FileObject[]{fo};
            currentResources = null;
        } else {
            setEnabled( false);
            currentFiles = null;
            currentResources = null;
        }	    
	}
	public void manySelected(FileObject[] list) {
        for (int i = 0; i < list.length; i++) {
            if(invokable(list[i])) { // lazy - only goes as far as the first 'yes'
                setEnabled(true);
                currentFiles = list;
                currentResources = null;
                return;
            }
        }
            setEnabled(false);
            currentResources = null;
            currentFiles = null;
	}
	
    /** to be implemented by subclasses - as a file filter which selects just
     * those files this activity can operate on.
     * @param f
     */
    protected abstract boolean invokable(FileObject f);
    
    /** helper method, to be used in 'action performed' method of subclass
     * 
     * will return the empty list if there's no invokable files at the moment - which suggests
     * that there's invokable resources instead.
     * */
    protected final List computeInvokableFiles() {
        if (currentFiles == null || currentFiles.length == 0) {
            return Collections.EMPTY_LIST;
        }
        List r = new ArrayList();
        for (int i = 0; i < currentFiles.length; i++) {
            if (invokable(currentFiles[i])) {
                r.add(currentFiles[i]);
            }
        }
        return r;
    }	

// machinery for resouces	
	public void oneSelected(Resource resource) {
        if (invokable(resource)) {
            setEnabled(true);
            currentResources =new Resource[]{resource};
            currentFiles = null;
        } else {
            setEnabled( false);
            currentResources = null;
            currentFiles = null;
        }
    }
    /**
     * @param list
     */
    public void someSelected(Resource[] list) {
        for (int i = 0; i < list.length; i++) {
            if(invokable(list[i])) {
                setEnabled(true);
                currentResources = list;
                currentFiles = null;
                return;
            }
        }
            setEnabled(false);
            currentResources = null;
            currentFiles = null;
    }
    /** helper method, to be used in 'action performed' method of subclass
     * 
     *      * will return the empty list if there's no invokable files at the moment - which suggests
     * that there's invokable resources instead.
     * never returns null
     * */
    protected final List computeInvokableResources() {
        if (currentResources == null || currentResources.length == 0) {
            return Collections.EMPTY_LIST;
        }
        List r = new ArrayList();
        for (int i = 0; i < currentResources.length; i++) {
            if (invokable(currentResources[i])) {
                r.add(currentResources[i]);
            }
        }
        return r;
    }
    /** to be implemented by subclasses - a filter
     * that selects just those resources this activity can operate on.
     * @param r
     */
    protected  abstract boolean invokable(Resource r);

	
	/** equivalent of none-selected */
	public void noneSelected() {
	    setEnabled(false);
	    currentFiles = null;
	    currentResources = null;
	}
	public final void selected(Transferable r) {
	    if (r == null) {
	        return;
	    }
		try {
		if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE)) {
			oneSelected((Resource)r.getTransferData(VoDataFlavour.RESOURCE));
		} else if (r.isDataFlavorSupported(VoDataFlavour.RESOURCE)) {
			oneSelected((Resource)r.getTransferData(VoDataFlavour.RESOURCE));
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE_ARRAY)) {
			someSelected((Resource[])r.getTransferData(VoDataFlavour.LOCAL_RESOURCE_ARRAY));
		} else if (r.isDataFlavorSupported(VoDataFlavour.RESOURCE_ARRAY)) {
		    someSelected((Resource[])r.getTransferData(VoDataFlavour.RESOURCE_ARRAY));
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT)) {
			oneSelected((FileObject)r.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT));
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_LIST)) {
			manySelected((FileObject[])r.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_LIST));
		} else {
			noneSelected();
		}
		} catch (IOException x) {
			logger.error("UnsupportedFlavorException",x);			
		} catch (UnsupportedFlavorException x) {
			logger.error("UnsupportedFlavorException",x);
		}
	}
}
