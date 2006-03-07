package org.astrogrid.applications.contracts;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Guy Rixon
 */
public interface Configuration {
  
  public File getBaseDirectory();
  public File getRecordsDirectory();
  public File getTemporaryFilesDirectory();
  public URL  getRegistryTemplate();
  public URL  getServiceEndpoint();
  
}
