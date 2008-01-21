/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.astrogrid.TableBean;

/**            A service that returns or otherwise interacts with one or
            more specified tables.  
            <p/>
                        This type differs from a CatalogService in that the
            table or tables do not have any inherent coverage of the
            sky.  In particular, one cannot include Coverage
            information.  
            @see DataService
            @see CatalogService
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 19, 20082:44:32 PM
 */
public interface TableService extends Service {
    /** the observatories or facilities used to collect the data contained or managed by this resource */
    ResourceName[] getFacilities();
    
    /** the instruments used to collect the data contained or managed by this resource */
    ResourceName[] getInstruments();
    
    /** description of the tables provided by this service */
    TableBean[] getTables();
}
