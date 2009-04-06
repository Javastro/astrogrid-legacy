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
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

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
		if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_VIEW)) {
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
	protected FileObjectView[] current;
	public boolean evaluate(final Object arg0) {
		if (! (arg0 instanceof FileObjectView)) {
			return false;
		}
		return invokable((FileObjectView)arg0);
	}
	
	/** to be implemented by subclasses - as a file filter which selects just
	 * those files this activity can operate on.
	 * @param f
	 */
	protected abstract boolean invokable(FileObjectView f);
	
	protected List<FileObjectView> computeInvokable() {
		final List<FileObjectView> r = new ArrayList<FileObjectView>();
		for (int i = 0; i < current.length; i++) {
			if (invokable(current[i])) {
				r.add(current[i]);
			}
		}
		return r;
	}

	public void oneSelected(final FileObjectView fo) {
		if (invokable(fo)) {
			setEnabled(true);
			current =new FileObjectView[]{fo};
		} else {
			setEnabled( false);
			current = null;
		}
	}

	public void manySelected(final FileObjectView[] list) {
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
