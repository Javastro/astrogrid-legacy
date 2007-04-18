/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** abstract class for when either some kind of resource, or a file, is acceptable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 16, 20078:21:37 PM
 */
public  abstract class AbstractFileOrResourceActivity extends AbstractSingleActivity{
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory
			.getLog(AbstractFileOrResourceActivity.class);

	public void noneSelected() {
		setEnabled(false);
	}

	public abstract void oneSelected(FileObject fo) ;
	public abstract void oneSelected(Resource resource);
	public abstract void manyResourcesSelected(Resource[] l);
	public abstract void manyFilesSelected(FileObject[] l);
	public abstract void somethingElseSelected();
	public void selected(Transferable r) {
		try {
		if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE)) {
			oneSelected((Resource)r.getTransferData(VoDataFlavour.RESOURCE));
		} else if (r.isDataFlavorSupported(VoDataFlavour.RESOURCE)) {
			oneSelected((Resource)r.getTransferData(VoDataFlavour.RESOURCE));
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE_LIST)) {
			manyResourcesSelected((Resource[])r.getTransferData(VoDataFlavour.LOCAL_RESOURCE_LIST));
		} else if (r.isDataFlavorSupported(VoDataFlavour.RESOURCE_LIST)) {
			manyResourcesSelected((Resource[])r.getTransferData(VoDataFlavour.RESOURCE_LIST));
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT)) {
			oneSelected((FileObject)r.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT));
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_LIST)) {
			manyFilesSelected((FileObject[])r.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_LIST));
		} else {
			somethingElseSelected();
		}
		} catch (IOException x) {
			logger.error("UnsupportedFlavorException",x);			
		} catch (UnsupportedFlavorException x) {
			logger.error("UnsupportedFlavorException",x);
		}
	}
}
