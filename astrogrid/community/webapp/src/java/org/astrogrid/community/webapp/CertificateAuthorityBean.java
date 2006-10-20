package org.astrogrid.community.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.apache.log4j.Logger;
import org.astrogrid.community.server.ca.CertificateAuthority;
import org.astrogrid.community.server.ca.UserFiles;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

/**
 * A Java bean representing a Certificate Authority.
 *
 * The CA is made operational by constructing the bean and setting
 * the caPassword property. This must be done before initiating any new users.
 *
 * "Initiating" a user means generating for that user a private key and 
 * long-term certificate using the CA. The initiation is set up by setting the
 * properties userCommonName, userLoginName and userPassword on this bean.
 * Initiation is triggered by reading the initiationResults properties.
 *
 * @author Guy Rixon
 */
public class CertificateAuthorityBean {
  
  /**
   * Constructs a CertificateAuthorityBean.
   */
  public CertificateAuthorityBean() {
  
    // Determine the details of the CA.
    this.findCaFilesFromConfiguration();
  }
  
  private static Logger log =
      Logger.getLogger("org.astrogrid.community.webapp.CertificateAuthorityBean");
  
  /**
   * The CA itself. This bean wraps the CA to
   * make it easier to use by JSPs.
   */
  protected CertificateAuthority ca;
  
  /**
   * A message describing the result of enabling the CA.
   */
  protected String enablementResult =
      "The certificate authority has not been enabled.";
  
  /**
   * Gets the message describing the enablement of the CA.
   */
  public String getEnablementResult() {
    return this.enablementResult;
  }
  
  /**
   * The state of the certificate authority: true implies enabled.
   */
  protected boolean caEnabled = false;
  
  /**
   * Reveals the state of the CA.
   */
  public String getCaState() {
    return (this.caEnabled)? "enabled" : "disabled";
  }
  
  /**
   * The file holding the CA's private key.
   */
  protected File caKeyFile;
  
  /**
   * The file holding the CA's certificate.
   */
  protected File caCertificateFile;
  
  /**
   * The file holding the CA's record of certificate serial numbers.
   */
  protected File caSerialFile;
  
  /**
   * The root of the DN's for this CA.
   */
  protected String rootDn;
  
  /**
   * Reveals the root DN for the CA.
   */
  public String getRootDn() {
    return this.rootDn;
  }
  
  /**
   * The directory where MyProxy stores credentials.
   */
  protected File myProxyDirectory;
  
  /**
   * Sets the passphrase for the CA's private key.
   */
  public void setCaPassword(String password) throws Exception {
    if (this.caKeyFile == null) {
      this.enablementResult = "The certificate authority could not be enabled" +
                              "the private-key file is not configured.";
      this.caEnabled = false;
    }
    else if (!this.caKeyFile.exists()) {
      this.enablementResult = "The certificate authority could not be enabled: " +
                              "the private-key file is missing.";
    }
    else if (this.caCertificateFile == null) {
      this.enablementResult = "The certificate authority could not be enabled: " +
                              "the certificate file is not configured.";
      this.caEnabled = false;
    }
    else if (!this.caCertificateFile.exists()) {
      this.enablementResult = "The certificate authority could not be enabled: " +
                              "the certificate file is missing.";
    }
    else if (this.caSerialFile == null) {
      this.enablementResult = "The certificate authority could not be enabled: " +
                              "the serial-number file is not configured.";
      this.caEnabled = false;
    }
    else if (!this.caSerialFile.exists()) {
      this.enablementResult = "The certificate authority could not be enabled: " +
                              "the serial-number file is missing.";
    }
    else if (this.myProxyDirectory == null) {
      this.enablementResult = "The certificate authority could not be enabled: " +
                              "the directory for MyProxy files is not configured.";
      this.caEnabled = false;
    }
    else if (!this.myProxyDirectory.exists()) {
      this.enablementResult = "The certificate authority could not be enabled: " +
                              "the directory for MyProxy files is missing.";
    }
    else if (password == null) {
      this.enablementResult = "The certificate authority could not be enabled: " +
                              "no password was given for the private key.";
      this.caEnabled = false;
    }
    else {
      try {
        this.rootDn = "(unknown)";
        this.ca = new CertificateAuthority(password,
                                           this.caKeyFile,
                                           this.caCertificateFile,
                                           this.caSerialFile,
                                           this.myProxyDirectory);
        this.rootDn = this.ca.getRootDn();
        this.enablementResult = "The certificate authority is enabled.";
        this.caEnabled = true;
      }
      catch (Exception e) {
        this.enablementResult = "Enabling the certificate authority failed: " + 
                                e.getMessage();
        this.caEnabled = false;
      }
    }
  }
  
  /**
   * The user's log-in name at the community.
   * This is the name that they use in the MyProxy protocol.
   */
  protected String userLoginName;
  
  /**
   * Sets the user's log-in name for the next initiation.
   */
  public void setUserLoginName(String name) {
    this.userLoginName = name;
  }
  
  /**
   * Reveals the user's log-in name for the current initiation.
   */
  public String getUserLoginName() {
    return this.userLoginName;
  }
  
  /**
   * The user's common name.
   * This is the name encoded into the user's certificate
   * as part of the subject.
   */
  protected String userCommonName;
  
  /**
   * Sets the user's common name for the next initiation.
   */
  public void setUserCommonName(String name) {
    this.userCommonName = name;
  }
  
  /**
   * Reveals the user's common name for the next initiation.
   */
  public String getUserCommonName() {
    return this.userCommonName;
  }
  
  /**
   * The user's log-in password.
   * This is the credential that they use to log in to MyProxy.
   * "New" means that it is to be set when creating credentials or when
   * changing the password on existing credentials.
   */
  protected String userNewPassword;
  
  /**
   * Sets the user's log-in password.
   */
  public void setUserNewPassword(String password) {
    this.userNewPassword = password;
  }
  
  /**
   * The user's log-in password, when changing the password.
   * This is the credential that they use to log in to MyProxy.
   * "Old" means that this is the password currently set (recovered
   * from community records) that must be replaced when changing
   * password on existing credentials.
   */
  protected String userOldPassword;
  
  /**
   * Sets the user's log-in password.
   */
  public void setUserOldPassword(String password) {
    this.userOldPassword = password;
  }
  
  /**
   * Get the message describing the results of initiation.
   * Getting this property triggers the initiation.
   */
  public String getInitiationResult() {
    String context = "Initiation of " +
                     this.userCommonName +
                     " (" +
                     this.userLoginName +
                     ") ";
    
    if (!this.caEnabled) {
      return context + "failed: the CA has not been enabled with the CA passphrase.";
    }
    
    try {
      String workingDirectoryName = System.getProperty("java.io.tmpdir");
      UserFiles userFiles = new UserFiles(new File(workingDirectoryName),
                                          this.userLoginName);
      log.debug("Initiating the user " + 
                this.userCommonName +
                " (" +
                this.userLoginName +
                ").");
      log.debug("User files are in " + 
                userFiles.getUserDirectory().getAbsolutePath());
      this.ca.setUserCredentialsInMyProxy(this.userLoginName,
                                          this.userCommonName,
                                          this.userNewPassword,
                                          userFiles);
      return context + "succeeded.";
    }
    catch (Exception e) {
      e.printStackTrace();
      return context + "failed. The CA reports: " + e.getMessage();
    }
  }
  
  /**
   * Get the message describing the results of initiation.
   * Getting this property triggers the initiation.
   */
  public String getPasswordChangeResult() throws Exception {
    if (this.userLoginName == null 
    ||  this.userOldPassword == null
    ||  this.userNewPassword == null) {
      return "Password change failed. Set properties userLoginName, " +
             "userOldPassword and userNewPassword before reading property " +
             "passwordChangeResult.";
    }
    String context = "Password change for " + this.userLoginName + " ";
    try {
      this.ca = new CertificateAuthority("",
                                         this.caKeyFile,
                                         this.caCertificateFile,
                                         this.caSerialFile,
                                         this.myProxyDirectory);
      this.ca.changePasswordInMyProxy(this.userLoginName,
                                      this.userOldPassword, 
                                      this.userNewPassword);
      return context + "succeeded.";
    }
    catch (Exception e) {
      e.printStackTrace();
      return context + "failed. The CA reports: " + e.getMessage();
    }
  }
  
  /**
   * Looks up in the AstroGrid configuration the files needed to use the
   * CA and Myproxy.
   */
  protected void findCaFilesFromConfiguration() {
    
    // The CA consists in three files and MyProxy has a storage directory.
    // Find them with the system configuration. If any key is not set,
    // then the corresponding File property in this bean remains set to null.
    Config config = SimpleConfig.getSingleton();
    try {
      String caKeyFileName = 
          config.getString("org.astrogrid.community.cakey");
      this.caKeyFile = new File(caKeyFileName);
    }
    catch (Exception e) {
      // Ignore it.
    }
    try {
      String caCertificateFileName 
          = config.getString("org.astrogrid.community.cacert");
      this.caCertificateFile = new File(caCertificateFileName);
    }
    catch (Exception e) {
      // Ignore it.
    }
    try {
      String caSerialFileName 
          = config.getString("org.astrogrid.community.caserial");
      this.caSerialFile      = new File(caSerialFileName);
    }
    catch (Exception e) {
      // Ignore it.
    }
    try {
      String myProxyDirectoryName 
          = config.getString("org.astrogrid.community.myproxy");
      this.myProxyDirectory  = new File(myProxyDirectoryName);
    }
    catch (Exception e) {
      // Ignore it.
    }
  }
  
}
