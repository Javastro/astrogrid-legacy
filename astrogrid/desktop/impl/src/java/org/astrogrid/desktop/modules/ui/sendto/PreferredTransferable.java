/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/** Extension of pthe default transferable interface, that lets
 * you find out the preferred (i.e. native) data flavour of the underlying data object.
 * @author Noel Winstanley
 * @since Jun 19, 200611:02:53 AM
 */
public interface PreferredTransferable extends Transferable {
	public DataFlavor getPreferredDataFlavor();
}
