/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

/** Abstract baseclass for all Registry capabilities.
 * @see RegistryService
 * @author Noel.Winstanley@manchester.ac.uk
 */
public abstract class RegistryCapability extends Capability {
    public RegistryCapability() {
        setStandardID(CAPABILITY_ID);
    }
    /** standard identifier for this capability */
    private static final URI CAPABILITY_ID = URI.create("ivo://ivoa.net/std/Registry");

}
