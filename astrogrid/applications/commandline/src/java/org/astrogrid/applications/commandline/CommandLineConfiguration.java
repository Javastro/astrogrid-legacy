package org.astrogrid.applications.commandline;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.astrogrid.applications.contracts.Configuration;

/**
 * The proximate source of configuration parameters for a
 * {@link org.astrogrid.applications.commandline.CommandLineCEAComponentManager}.
 * Implementations should get the information from the general configuration, or
 * from test set-up as appropriate. Note the comments on the accessor methods:
 * this interfaces mandates specific handling of default values.
 *
 * @author Guy Rixon
 */
public interface CommandLineConfiguration extends Configuration {
  
  /**
   * Gets the URL for the file of application descriptions. If no such file 
   * is configured, gets a URL to a dummy description-file that is syntactically 
   * valid but contains no applications. This method never returns null.
   * 
   * @return The URL.
   */
  public URL getApplicationDescriptionUrl() throws IOException;
  
  /**
   * Gets the working directory. The WD is where the parent programme may 
   * keep scratch files. If no WD is configured, returns the system-defined
   * temporary-files directory. This method never returns null.
   *
   * @return The working directory.
   */
  public File getWorkingDirectory();
}
