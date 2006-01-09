package org.astrogrid.applications.commandline;

import java.io.File;
import java.net.URL;

/**
 *
 * @author Guy Rixon
 */
public class TestCommandLineConfiguration implements CommandLineConfiguration {

  private URL applicationDescriptionUrl;
  
  private File workingDirectory;
  
  public URL getApplicationDescriptionUrl() {
    return this.applicationDescriptionUrl;
  }
  
  public void setApplicationDescriptionUrl(URL url) {
    this.applicationDescriptionUrl = url;
  }
  
  public File getWorkingDirectory() {
    return this.workingDirectory;
  }
  
  public void setWorkingDirectory(File directory) {
    this.workingDirectory = directory;
  }
  
}
