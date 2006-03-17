package org.astrogrid.applications.component;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.manager.BaseConfiguration;
import org.astrogrid.config.SimpleConfig;

/**
 * A component manager that contains the default CEA components.
 * This is a minimal, concrete implementation of
 * {@link org.astrogrid.applications.component.EmptyCEAComponentManager}
 * and is used to test the latter.
 *
 * @author Guy Rixon
 */
public class BaseCEAComponentManager extends EmptyCEAComponentManager {
  
  /**
   * Constructs a BaseCEAComponentManager.
   */
  public BaseCEAComponentManager() {
    super();
    super.registerDefaultServices(this.pico);
    this.pico.registerComponentImplementation(Configuration.class,
                                              BaseConfiguration.class);
  }
  
}
