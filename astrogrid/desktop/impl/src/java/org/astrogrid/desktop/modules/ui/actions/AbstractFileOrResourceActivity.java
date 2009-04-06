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

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/** base class for activities can are applicable to file objects and registry resources.
 * 
 * a merge of the funtionality provided vby the AbstractFileActivity and AbstractResourceActivity classes
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 16, 20078:21:37 PM
 */
public  abstract class AbstractFileOrResourceActivity extends AbstractActivity{

	private FileObjectView[] currentFiles = null;
	private Resource[] currentResources = null;
	
// machinery for files
	public void oneSelected(final FileObjectView fo){
        if (invokable(fo)) {
            setEnabled(true);
            currentFiles =new FileObjectView[]{fo};
            currentResources = null;
        } else {
            setEnabled( false);
            currentFiles = null;
            currentResources = null;
        }	    
	}
	public void manySelected(final FileObjectView[] list) {
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
    protected abstract boolean invokable(FileObjectView f);
    
    /** helper method, to be used in 'action performed' method of subclass
     * 
     * will return the empty list if there's no invokable files at the moment - which suggests
     * that there's invokable resources instead.
     * */
    protected final List<FileObjectView> computeInvokableFiles() {
        if (currentFiles == null || currentFiles.length == 0) {
            return Collections.EMPTY_LIST;
        }
        final List<FileObjectView> r = new ArrayList();
        for (int i = 0; i < currentFiles.length; i++) {
            if (invokable(currentFiles[i])) {
                r.add(currentFiles[i]);
            }
        }
        return r;
    }	

// machinery for resouces	
	public void oneSelected(final Resource resource) {
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
    public void someSelected(final Resource[] list) {
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
    protected final List<Resource> computeInvokableResources() {
        if (currentResources == null || currentResources.length == 0) {
            return Collections.EMPTY_LIST;
        }
        final List<Resource> r = new ArrayList();
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
	@Override
    public void noneSelected() {
	    setEnabled(false);
	    currentFiles = null;
	    currentResources = null;
	}
	@Override
    public final void selected(final Transferable r) {
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
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_VIEW)) {
			oneSelected((FileObjectView)r.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_VIEW));
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_VIEW_ARRAY)) {
			manySelected((FileObjectView[])r.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_VIEW_ARRAY));
		} else {
			noneSelected();
		}
		} catch (final IOException x) {
			logger.error("UnsupportedFlavorException",x);			
		} catch (final UnsupportedFlavorException x) {
			logger.error("UnsupportedFlavorException",x);
		}
	}
}
