/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Capability;

/** unified interface to testing a capability.
 * implementation is expected to test endpoint for validity, and run
 * any provided test queries.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 19, 20083:53:01 PM
 */
public interface CapabilityTester {

    /**
     * test a capability.
     * @param cap the capability to test.
     * @throws Exception throws an exception if the capability is not functioning in some way.
     * @return true if the capability was tested successfully. Returns false if the capability could not be tested
     * (if the capability was tested, but failed, an exception is thrown).
     */
    public boolean testCapability(Capability cap) throws ServiceException;
    
    
}
