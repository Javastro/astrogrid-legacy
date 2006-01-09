package org.astrogrid.applications.description.registry;

import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;

/**
 * A wrapper for the factory that makes registry-admin delegeates.
 * This class only exists to maintain compatibility with existing
 * code while eliminating inner classes that break Picocontainer.
 * The consumers of this class could be refactored to use
 * RegistryDelegateFactory directly.
 *
 * @author Guy Rixon
 */
public class RegistryAdminLocatorImpl implements RegistryAdminLocator {
  
  public RegistryAdminService getClient() {                
    return RegistryDelegateFactory.createAdmin();
  }  
}
