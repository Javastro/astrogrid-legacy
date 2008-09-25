/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.astrogrid.TableBean;

/**  A service that interacts with tables, and posesses coverage information.
 * <p/>
 * The definition reads "A service that interacts one or more specified tables
            having some coverage of the sky, time, and/or frequency."
           
           @note Was previously named TabularSkyService
           @see TableService
           
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface CatalogService extends DataService {
	/** description of the tables that this service provides */
 TableBean[] getTables();
}
