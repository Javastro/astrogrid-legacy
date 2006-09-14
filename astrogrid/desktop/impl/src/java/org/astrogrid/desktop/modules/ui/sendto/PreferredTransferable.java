/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Map;

/** Extension of pthe default transferable interface, that lets
 * you find out the preferred (i.e. native) data flavour of the underlying data object.
 * @author Noel Winstanley
 * @since Jun 19, 200611:02:53 AM
 */
public interface PreferredTransferable extends Transferable {
	public DataFlavor getPreferredDataFlavor();
	/** returns a map of additional metadata about this object
	 * contents are deliberately vague. it's probably ucds-values.
	 * @return
	 */
	Map getMetaData();
}
