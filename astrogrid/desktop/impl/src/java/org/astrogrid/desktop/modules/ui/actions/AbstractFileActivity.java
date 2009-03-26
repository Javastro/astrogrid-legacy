/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** base class for activities only applicable to file objects.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 200710:31:27 AM
 */
public abstract class AbstractFileActivity extends AbstractActivity implements Predicate {

	@Override
    public void noneSelected() {
		setEnabled(false);
		current = null;
	}
	@Override
    public final void selected(final Transferable r) {
	    if (r == null) {
	        return;
	    }
		try {
		if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT)) {
			oneSelected((FileObject)r.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT));
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_ARRAY)) {
			manySelected((FileObject[])r.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_ARRAY));
		} else {
			noneSelected();
		}
		} catch (final IOException x) {
			logger.error("UnsupportedFlavorException",x);			
		} catch (final UnsupportedFlavorException x) {
			logger.error("UnsupportedFlavorException",x);
		}
	}
	protected FileObject[] current;
	public boolean evaluate(final Object arg0) {
		if (! (arg0 instanceof FileObject)) {
			return false;
		}
		return invokable((FileObject)arg0);
	}
	
	/** to be implemented by subclasses - as a file filter which selects just
	 * those files this activity can operate on.
	 * @param f
	 */
	protected abstract boolean invokable(FileObject f);
	
	protected List<FileObject> computeInvokable() {
		final List<FileObject> r = new ArrayList<FileObject>();
		for (int i = 0; i < current.length; i++) {
			if (invokable(current[i])) {
				r.add(current[i]);
			}
		}
		return r;
	}

	public void oneSelected(final FileObject fo) {
		if (invokable(fo)) {
			setEnabled(true);
			current =new FileObject[]{fo};
		} else {
			setEnabled( false);
			current = null;
		}
	}

	public void manySelected(final FileObject[] list) {
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
