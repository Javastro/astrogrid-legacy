/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;

/** The capability to performa an IVOA TAP Query.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since 1.2.1
 * @see TapService 
 */
public class TapCapability extends Capability {
    /** @exclude */
    public static final URI CAPABILITY_ID = URI.create("ivo://ivoa.net/std/TAP");
    
    /** @exclude */
    public TapCapability() {
        setStandardID(CAPABILITY_ID);
    }
}

