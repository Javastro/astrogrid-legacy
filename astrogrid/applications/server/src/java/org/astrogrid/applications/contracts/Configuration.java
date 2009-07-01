package org.astrogrid.applications.contracts;

import java.io.File;
import java.net.URL;

/**
 *
 * @author Guy Rixon
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 18 Sep 2008 got rid of concept of base directory.
 * 
 * @TODO  this interface should probably be used less often - it supplies simple values, and they are not always all used by all components that have constructors includig this interface. The constructors should be altered to explicitly require only what they use.
 */
public interface Configuration {
 

    /**
     * @param applicationDescriptionUrl the applicationDescriptionUrl to set
     * @TODO is this really needed?
     */
    public void setApplicationDescriptionUrl(URL applicationDescriptionUrl);
  
  public File getRecordsDirectory();
  public File getTemporaryFilesDirectory();
  public URL  getServiceEndpoint();
  /**
   * Gets the URL for the file of application descriptions. If no such file 
   * is configured, gets a URL to a dummy description-file that is syntactically 
   * valid but contains no applications. This method never returns null.
   * 
   * @return The URL.
   */
  public URL getApplicationDescriptionUrl();

  
}
