package org.astrogrid.applications.commandline;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.manager.BaseConfiguration;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.config.SimpleConfig;

/**
 * An implementation of 
 * {@link org.astrogrid.applications.commandline.CommandLineConfiguration}
 * that works from the JNDI/properties-file configuration in
 * {@link org.astrogrid.config.SimpleConfig}.
 *
 * @author Guy Rixon
 */
public class BasicCommandLineConfiguration 
    extends BaseConfiguration
    implements CommandLineConfiguration, ComponentDescriptor {
  
  static private Log logger = LogFactory.getLog(BasicCommandLineConfiguration.class);
  
  protected URL applicationDescriptionUrl;
  
  /**
   * Constructs a new BasicCommandLineConfiguration, creating templates
   * for configuration files as needed. This kind of conifiguration includes
   * two configuration files: one application description and one registration
   * template.
   */
  public BasicCommandLineConfiguration() throws IOException {
    super();  // Creates the directory structure.
    this.createApplicationDescription();
  }
  
  /**
   * @see org.astrogrid.applications.commandline.CommandLineConfiguration.getApplicationDescriptionUrl}.
   */
  public URL getApplicationDescriptionUrl() throws IOException {
    return this.nameApplicationDescriptionFile().toURL();
  }
  
  /**
   * @see org.astrogrid.applications.commandline.CommandLineConfiguration.getWorkingDirectory}.
   */
  public File getWorkingDirectory() {
    return this.getTemporaryFilesDirectory();
    // @TODO refactor the clients to remove this delegation.
  }
  
  /**
   * Sets the application-description file. Internally, it
   * copys the application description from the 
   * given URL to the regular location. Therefore,
   * this class should only be used with temporary
   * configurations; don't let it wreck a permanant
   * configuration.
   */
  public void setApplicationDescription(URL url) throws IOException {
    this.copyUrlToFile(url, this.nameApplicationDescriptionFile());
  }

  /**
   * Reveals the name of the component.
   */
  public String getName() {
    return "Configuration for command-line-application server";
  }
  
  /**
   * Gets the duration, in milliseconds after which an executing job is aborted.
   */
  public long getRunTimeLimit() {
    int def = 1000*365*24*60*60*1000; // 1000 years in milliseconds
    return SimpleConfig.getSingleton().getInt("cea.run.time.limit", def);
  }
  
  /**
   * Gets the greatest number of jobs that may execute at the same time.
   */
  public int getParallelExecutionlimit() {
    return SimpleConfig.getSingleton().getInt("cea.parallel.execution.limit", 5);
  }
  
  /**
   * Describes the component and its state.
   */
  public String getDescription() {
    StringBuffer sb = new StringBuffer(super.getDescription());
    
    sb.append("Application-description file: ");
    sb.append(this.applicationDescriptionUrl.toString());
    sb.append("\n");
    
    sb.append("Run-time limit (ms): ");
    sb.append(getRunTimeLimit());
    sb.append('\n');
    
    sb.append("Parallel-execution limit: ");
    sb.append(getParallelExecutionlimit());
    sb.append('\n');
    return sb.toString();
  }
  
  /**
   * Adds a template application-description to the configuration.
   * The template is a minimal XML file that is copied into the configuration
   * directory from a source in the web-application code. If there is
   * already an application description, then the existing one is left
   * untouched. Note that the name of the application-description file -
   * app-description.xml - is fixed.
   */
  protected void createApplicationDescription() throws IOException {
    File appDescFile = this.nameApplicationDescriptionFile();
    this.applicationDescriptionUrl = appDescFile.toURL();
    if (!appDescFile.exists()) {
      logger.info("The application-description file " +
                  appDescFile.getAbsoluteFile() +
                  " has been initialized.");
      appDescFile.createNewFile();
      URL dummy = this.getClass().getResource("/dummy-app-description.xml");
      assert(dummy != null);
      this.copyUrlToFile(dummy, this.nameApplicationDescriptionFile());
    }
  }
  
  /**
   * Generates the name for the application-description file.
   * The last part of the name is fixed by this class. The last part
   * of the directory path is fixed by the superclass. The rest of the
   * name is configurable, and the configuration is handled by the
   * superclass.
   *
   * @return The name.
   */
  protected File nameApplicationDescriptionFile() {
    return new File(this.getConfigurationDirectory(), "app-description.xml");
  }

}
