/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.astrogrid.TableBean;

/**  A service that interacts with one or more specified tables
            having some coverage of the sky, time, and/or frequency.
           
           Previously Named TabularSkyService
           @see TableService
           
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 20075:00:10 PM
 */
public interface CatalogService extends DataService {
	/** description of the tables provided by this service */
 TableBean[] getTables();
}
