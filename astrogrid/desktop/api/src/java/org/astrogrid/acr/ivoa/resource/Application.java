/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

/** Abstract concept of an application
 * schema is fluid at the moment - and poorly thought out IMHO - so only 
 * partially implemented at present. Will wait for standard to settle and see what parts of it are used in practice
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 20, 200811:44:39 AM
 */
public interface Application extends Resource {

    /**enumerate which standards this application is
                        compliant with*/
    public URI[] getApplicationCapabilities();

}
