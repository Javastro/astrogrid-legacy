/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import ca.odell.glazedlists.EventList;

/** Interface to something that provides an eventlist.
 * 
 * implementations probably provides a way of loading the list contents
 * from a persistent store, and then watching for updates - but this is 
 * not specified.
 * @author Noel.Winstanley@manchester.ac.uk
 * @param <E> item type for list
 * @since Mar 5, 200712:36:10 PM
 */
public interface ListProvider<E> {

	public EventList<E> getList();

}