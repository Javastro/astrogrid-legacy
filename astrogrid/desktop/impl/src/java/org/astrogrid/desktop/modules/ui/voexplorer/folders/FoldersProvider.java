/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import ca.odell.glazedlists.EventList;

/** interface to something that provides an eventlist.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 5, 200712:36:10 PM
 */
public interface FoldersProvider {

	public EventList getList();

}