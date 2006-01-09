package org.astrogrid.applications.commandline;

import java.io.File;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;

/**
 * An implementation of 
 * {@link org.astrogrid.applications.commandline.CommandLineConfiguration}
 * that works from the JNDI/properties-file configuration in
 * {@link org.astrogrid.config.SimpleConfig}.
 *
 * @author Guy Rixon
 */
public class BasicCommandLineConfiguration implements CommandLineConfiguration {
  
  static private Log logger = LogFactory.getLog(BasicCommandLineConfiguration.class);
  
  /**
   * @see org.astrogrid.applications.commandline.CommandLineConfiguration.getApplicationDescriptionUrl}.
   */
  public URL getApplicationDescriptionUrl() {
    try {
      // This will throw an exception if the property is not configured.
      URL url = SimpleConfig.getSingleton().getUrl("cea.local.apps.config");
      logger.info("The application configuration is at " + url.toString());
      return url;
    }
    catch (Exception e) {
      logger.warn("No application description is configured. " +
                  "Set cea.local.apps.config to point to your description.");
      URL url = this.getClass().getResource("/EmptyCommandLineApplicationDescription.xml");
      logger.info("Using " + url + "as a dummy description.");
      return url;
    }
  }
  
  /**
   * @see org.astrogrid.applications.commandline.CommandLineConfiguration.getWorkingDirectory}.
   */
  public File getWorkingDirectory() {
    try {
      // This will throw an exception if the property is not configured.
      String dirName = SimpleConfig.getSingleton().getString("cea.job.temp.files");
      logger.info("The working directory is " + dirName);
      return new File(dirName);
    }
    catch (Exception e) {
      String dirName = System.getProperty("java.io.tmpdir");
      logger.warn("No working directory is configured; using " + dirName); 
      return new File(dirName);
    }
  }
}
