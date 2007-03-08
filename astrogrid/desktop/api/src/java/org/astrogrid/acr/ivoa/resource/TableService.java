/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.astrogrid.TableBean;

/**  A service that returns or otherwise interacts with one or
            more specified tables.  
            
        Unclear how this differs from CatalogService, really.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 20074:42:01 PM
 */
public interface TableService extends Service, HasTables {
	/** the observatories or facilities used to collect the data contained or managed by this resource */
	ResourceName[] getFacilities();
	
	/** the instruments used to collect the data contained or managed by this resource */
	ResourceName[] getInstruments();
	
	/**  a description of a table and its columns. */
	TableBean[] getTables();
}
