/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

/** Abstract capability type for all Registry capabilities.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 20, 20081:03:01 PM
 */
public abstract class RegistryCapability extends Capability {
    public RegistryCapability() {
        setStandardID(CAPABILITY_ID);
    }
    /** standard identifier for this capability */
    private static final URI CAPABILITY_ID = URI.create("ivo://ivoa.net/std/Registry");

}
