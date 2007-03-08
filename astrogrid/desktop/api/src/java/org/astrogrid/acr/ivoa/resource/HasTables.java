/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.astrogrid.TableBean;

/** marker interface for all resources that provide table metadata.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 8, 20075:30:44 PM
 */
public interface HasTables {
	TableBean[] getTables();

}
