/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;

/** Computes the size (i.e. number of resources returned) of queries
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 27, 20071:19:25 PM
 */
public interface QuerySizer {

	/** compute the size of the registry */
	public Integer regSize();

	/** compute the size of a srql query */
	public Integer size(SRQL query);

	/** compute the size of an xquery */
	public Integer size(String query);

	//Represents an error occurring.
	public final static Integer ERROR = new Integer(-1);
}