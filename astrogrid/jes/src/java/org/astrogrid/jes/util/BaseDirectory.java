package org.astrogrid.jes.util;

import java.io.File;

/**
 * A working directory for the parts of JES that write records in files.
 * Some major components of JES need to be passed the location of a
 * working directory on construction. A java.io.File would express this
 * well enough, but the components are managed by PicoContainer which
 * relies on constructor arguments being in interface-implementation pairs.
 *
 * @author Guy Rixon
 */
public interface BaseDirectory {
  
  /**
   * Gets the directory.
   * @return The directory.
   */
  public File getDir();
}
