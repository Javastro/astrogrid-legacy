/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** abstract class for activitirs that just operate of file objects
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 200710:31:27 AM
 */
public abstract class AbstractFileActivity extends AbstractActivity implements Predicate {
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory
			.getLog(AbstractFileActivity.class);

	public void noneSelected() {
		setEnabled(false);
		current = null;
	}
	public void selected(Transferable r) {
		try {
		if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT)) {
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
	protected FileObject[] current;
	public boolean evaluate(Object arg0) {
		if (! (arg0 instanceof FileObject)) {
			return false;
		}
		return invokable((FileObject)arg0);
	}
	
	/** to be implemented by subclasses - as a file filter which selects just
	 * those files this activity can operate on.
	 * @param f
	 * @return
	 */
	protected abstract boolean invokable(FileObject f);
	
	protected List computeInvokable() {
		List r = new ArrayList();
		for (int i = 0; i < current.length; i++) {
			if (evaluate(current[i])) {
				r.add(current[i]);
			}
		}
		return r;
	}

	public void oneSelected(FileObject fo) {
		if (invokable(fo)) {
			setEnabled(true);
			current =new FileObject[]{fo};
		} else {
			setEnabled( false);
			current = null;
		}
	}

	public void manySelected(FileObject[] list) {
		for (int i = 0; i < list.length; i++) {
			if(evaluate(list[i])) { // lazy - only goes as far as the first 'yes'
				setEnabled(true);
				current = list;
				return;
			}
		}
			setEnabled(false);
			current = null;
	}	
	


}
