/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.astrogrid.TableBean;

/**     A service that interact with tables.
            <p/>
                        This type differs from a {@link CatalogService} in that the
            table or tables do not have any inherent coverage of the
            sky. 
            @see DataService
            @see CatalogService
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface TableService extends Service {
    /** the observatories or facilities used to collect the data contained or managed by this resource */
    ResourceName[] getFacilities();
    
    /** the instruments used to collect the data contained or managed by this resource */
    ResourceName[] getInstruments();
    
    /** description of the tables provided by this service */
    TableBean[] getTables();
}
