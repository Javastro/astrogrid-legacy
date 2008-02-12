package org.astrogrid.applications.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.Test;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

/**
 *
 * @author Guy Rixon
 */
public class BaseConfiguration 
    implements Configuration, ComponentDescriptor {
  
  protected File baseDirectory;
  protected File configurationDirectory;
  protected File recordsDirectory;
  protected File temporaryFilesDirectory;
  protected URL  cecEndpoint;
  protected File registrationTemplate;
  protected URL registrationTemplateUrl;
  
  /**
   * Constructs a BaseConfiguration, establishing the directory
   * structure on which the configuration is based.
   *
   * @throws IOException If one of the directories cannot be created.
   */
  public BaseConfiguration() throws IOException {
    this.chooseBaseDirectory();
    this.chooseConfigurationDirectory();
    this.chooseRecordsDirectory();
    this.chooseTemporaryFilesDirectory();
    this.initializeRegistrationTemplate();
    this.initializeCecEndpoint();
  }
  
  /**
   * Determines the base directory of the configuration structure.
   * This directory is the only part of the structure that can be
   * chosen by the deployer.
   *
   * @return The directory.
   */
  public File getBaseDirectory() {
    return this.baseDirectory;
  }
  
  /**
   * Determines the directory in which the CEC can find its 
   * configuration files.
   *
   * @return The directory.
   */
  public File getConfigurationDirectory() {
    return this.configurationDirectory;
  }  
  
  /**
   * Determines the directory in which the CEC can write its execution histories.
   *
   * @return The directory.
   */
  public File getRecordsDirectory() {
     //TODO needs to be individually settable again
    return this.recordsDirectory;
  }
  
  /**
   * Determines the directory in which the CEC can write the temporary
   * files for each execution.
   *
   * @return The directory.
   */
  public File getTemporaryFilesDirectory() {
    return this.temporaryFilesDirectory;
  }
 
  /**
   * Establishes the base directory, creating it if necessary.
   * Normally, the directory is put at the location stated in
   * the JNDI variable cea.base.dir. If this is not set, or
   * if a directory cannot be created there, then a directory
   * with a random name is created in the default temporary-files
   * directory.
   *
   * @throws IOException If the directory cannot be created.
   */ 
  private void chooseBaseDirectory() throws IOException {
    
    // For preference, base the directory structure at the point
    // configured in the application properties.
    try {
      String property = SimpleConfig.getSingleton().getString("cea.base.dir");
      this.baseDirectory = new File(property);
      if (!this.baseDirectory.exists()) {
        if (!this.baseDirectory.mkdirs()) {
          throw new Exception("Can't create the base directory " + 
                              this.baseDirectory.getAbsolutePath());
        }
      }
    }
    
    // If the configured location isn't available, make a temporary
    // directory as the base.
    catch (Exception e) {
      this.baseDirectory = this.makeTemporaryDirectory();
    }
  }
  
  private void chooseConfigurationDirectory() {
    this.configurationDirectory = new File(this.baseDirectory, "config");
    this.configurationDirectory.mkdir();
  }
  
  private void chooseRecordsDirectory() {
    this.recordsDirectory = new File(this.baseDirectory, "records");
    this.recordsDirectory.mkdir();
  }
  
  private void chooseTemporaryFilesDirectory() {
    this.temporaryFilesDirectory = new File(this.baseDirectory, "temp");
    this.temporaryFilesDirectory.mkdir();
  }
  
  /**
   * Makes sure that there is a registration-template file at the configured
   * location, creating one if necessary.
   */
  private void initializeRegistrationTemplate() 
      throws IOException, MalformedURLException {
    
    // Work out and remember where the template is kept.
    this.registrationTemplate = new File(this.configurationDirectory, 
                                         "registration-template.xml");
    this.registrationTemplateUrl = this.registrationTemplate.toURL();
    
    // If there is no template yet, take a copy of a dummy one in the JAR.
    if (!this.registrationTemplate.exists()) {
      URL source = this.getClass().getResource("/CEARegistryTemplate.xml");
      this.copyUrlToFile(source, this.registrationTemplate);
    }
  }
  
  /**
   * Records the endpoint URL for the CEC.
   */
  private void initializeCecEndpoint() throws MalformedURLException {
    try {
      String urlText = SimpleConfig.getSingleton().getString("cea.webapp.url") +
                       "/services/CommonExecutionConnectorService";
      this.cecEndpoint = new URL(urlText);
    }
    catch (Exception e) {
      this.cecEndpoint 
          = new URL("http://localhost:8080/cec/services/CommonExecutionConnectorService");
    }
  }
  
  /** 
   * Sets the registration template, by copying an XML document
   * from the given URL.
   *
   * @param source The source of the template document.
   */
  public void setRegistrationTemplate(URL source) throws IOException {
    this.copyUrlToFile(source, this.registrationTemplate);
  }

  /**
   * Copies the content of a URL to a file. This is useful for transcribing
   * template files in a jar to writable storage.
   *
   * @param source The URL from which to copy.
   * @param sink The file to which to write.
   * @throws IOException If anything goes wrong.
   */
  public void copyUrlToFile(URL source, File sink) throws IOException {
    InputStream is = source.openStream();
    OutputStream os = new FileOutputStream(sink);
    byte[] b = new byte[32768];
    while(true) {
      int nBytes = is.read(b);
      if (nBytes > 0) {
        os.write(b, 0, nBytes);
      }
      else {
        break;
      }
    }
  }

  public void copyUrlToUrl(URL source, URL sink) throws IOException {
    InputStream is = source.openStream();
    OutputStream os = sink.openConnection().getOutputStream();
    byte[] b = new byte[32768];
    while(true) {
      int nBytes = is.read(b);
      if (nBytes > 0) {
        os.write(b, 0, nBytes);
      }
      else {
        break;
      }
    }
  }
  /**
   * Locates the registration template.
   */
  public URL getRegistryTemplate() {
    return this.registrationTemplateUrl;
  }
  
  /**
   * Locates the registration template.
   */
  public URL getServiceEndpoint() {
    return this.cecEndpoint;
  }
  
  /**
   * Reveals the name of the component.
   */
  public String getName() {
    return "Configuration for a Generic CEC.";
  }
  
  /**
   * Describes the component and its current state.
   */
  public String getDescription() {
    StringBuffer sb = new StringBuffer();
    sb.append("Provides configuration for the CEC in a type-safe form.\n");
    sb.append("Manages directories where the CEC keeps its files.\n");
    sb.append("Base directory: ");
    sb.append(this.getBaseDirectory().getAbsolutePath());
    sb.append("\n");
    sb.append("Configuration directory: ");
    sb.append(this.getConfigurationDirectory().getAbsolutePath());
    sb.append("\n");
    sb.append("Temporary-files directory: ");
    sb.append(this.getTemporaryFilesDirectory().getAbsolutePath());
    sb.append("\n");
    sb.append("Execution-records directory: ");
    sb.append(this.getRecordsDirectory().getAbsolutePath());
    sb.append("\n");
    sb.append("Registration-template file: ");
    sb.append(this.getRegistryTemplate().toString());
    sb.append("\n");
    sb.append("CEC endpoint: ");
    sb.append(this.getServiceEndpoint().toString());
    sb.append("\n");
    return sb.toString();
  }
  
  /**
   * Generates a test-suite (not implemented for this component).
   */
  public Test getInstallationTest() {
    return null;
  }
  
  /**
   * Creates a temporary directory.
   * Java has an API to create a temporary file, with safeguards against
   * duplication, but doesn't have an API for a temporary directory.
   * This method uses the temporary file (name ending in .tmp) as a lock
   * and creates a directory based on that name. The simpler technique
   * of creating the file, deleting it from the file system and replacing
   * with a directory of the same name doesn't work reliably; it defeats
   * the locking in the JRE.
   *
   * @return The directory.
   * @throw IOException If the lock file cannot be created (should never happen). 
   */
  private File makeTemporaryDirectory() throws IOException {
    File lockFile = File.createTempFile("CecBaseConfiguration", ".tmp");
    lockFile.deleteOnExit();
    String fileName = lockFile.getAbsolutePath();
    String directoryName 
        = fileName.substring(0, fileName.lastIndexOf(".tmp")-1).toString();
    File directory = new File(directoryName);
    directory.mkdir();
    directory.deleteOnExit();
    return directory;
  }
}
