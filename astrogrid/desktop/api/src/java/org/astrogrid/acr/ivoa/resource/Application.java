/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

import org.astrogrid.acr.astrogrid.CeaApplication;

/** An abstract notion of 'Application'.
 * 
 * The IVOA schema is still in flux at the moment, so only partially 
 * implemented at present. Will wait for standard to settle and see what elements of it are used in practice
 * @see CeaApplication CeaApplication - useful subtype of Application.
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface Application extends Resource {

    /**Enumerate which standards this application is compliant with
     * 
     * @return an array of standard IDs.
     */
                        
    public URI[] getApplicationCapabilities();

}
