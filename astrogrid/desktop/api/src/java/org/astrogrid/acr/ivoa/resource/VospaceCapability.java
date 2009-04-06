/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.net.URI;
import java.net.URISyntaxException;

/** The capability to access VOSpace
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 6, 20091:37:54 PM
 */
public class VospaceCapability extends Capability {

    /** @exclude */
    public static final URI CAPABILITY_ID = URI.create("ivo://ivoa.net/vospace/core#vospace-1.1");
    private final URI root;

    /** @exclude */
    public VospaceCapability(final URI ivoid) {        
        setStandardID(CAPABILITY_ID);
        URI u = null;
        try {
            u = new URI("vos","//" + ivoid.getAuthority() 
                        + ivoid.getPath().replace('/','!') 
                        + "/"
                        ,null);
        } catch (final URISyntaxException x) {
           // ignore.
        }
        root = u;
    }
    
    /** Returns the vospace root URI for this vospace service
     * @return a {@code vos://...} URI */
    public URI getVospaceRoot() {
        return root;
    }
}
