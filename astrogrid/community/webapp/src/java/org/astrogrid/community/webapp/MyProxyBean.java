package org.astrogrid.community.webapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

/**
 * A Java bean giving access to credentials stored by MyProxy.
 * This bean allows direct manipulation of the credential files via the
 * file system shared by the parent web-application and MyProxy. The bean
 * does not connect with MyProxy's network interface to retrieve credentials.
 *
 * @author Guy Rixon
 */
public class MyProxyBean {
  
  /**
   * Constructs a MyProxyBean.
   * Reads the AstroGrid configuration to find out where MyProxy
   * keeps its files.
   */
  public MyProxyBean() {
    this.findMyProxyFilesFromConfiguration();
  }
  
  /**
   * The directory where MyProxy stores credentials.
   */
  protected File myProxyDirectory;
  
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
      this.changePasswordInMyProxy(this.userLoginName,
                                   this.userOldPassword, 
                                   this.userNewPassword);
      return context + "succeeded.";
    }
    catch (Exception e) {
      e.printStackTrace();
      return context + "failed. The MyProxy service reports: " + e.getMessage();
    }
  }
  
  /**
   * Changes the password protecting the user's credentials.
   */
  protected void changePasswordInMyProxy(String loginName,
                                      String oldPassword,
                                      String newPassword) throws Exception {
    String mp = this.myProxyDirectory.getAbsolutePath();
    String[] command = {
        "myproxy-admin-change-pass", // Invoke MyProxy tools
        "-s",                        // Use the MyProxy storage...
        mp,                          // ...here
        "-l",                        // Alter the credential for...
        loginName,                   // ...this user
        "-S"                         // Read the password from standard input
    };
    
    // The command takes the current password (to unlock the private key)
    // plus the new password, on two sucessive lines of input.
    String passwords = oldPassword + "\n" + newPassword;
    this.runCommandWithStdinPassword(command, passwords);
  }
  
  /**
   * Runs a command in a sub-process and writes the password to
   * that process' standard input.
   *
   * The command is given as an array of words. This approach is required 
   * because some of the arguments may contain white space; forming the
   * command as a single string doesn't work. Adding quotes around the
   * arguments with white space doesn't work either, as there is no shell
   * to process the quotes; Java tokenization can't cope.
   * 
   * @param command - The command with argument list; zeroth element is command name.
   * @param password - The passphrase.
   * @throws Exception - If OpenSSL goes wrong.
   */
  protected void runCommandWithStdinPassword(String[] command, 
                                             String password) throws Exception {
    // Start the command in a sub-process.
    Process p = Runtime.getRuntime().exec(command);
    
    // Send the password to the standard input of the sub-process.
    OutputStream os = p.getOutputStream();
    os.write(password.getBytes());
    os.flush();
    os.close();
    
    // Wait for the OpenSSL command to finish.
    p.waitFor();
    
    // Check for failure.
    if (p.exitValue() != 0) {
      InputStream is = p.getErrorStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      StringBuffer whinge = new StringBuffer();
      whinge.append(command[0]);
      whinge.append(" failed:");
      while(true) {
        String line = br.readLine();
        if (line == null) {
          break;
        }
        whinge.append("\n");
        whinge.append(line);
      }
      throw new Exception(whinge.toString());
    }
  }
  
  /**
   * Looks up in the AstroGrid configuration the files needed to use MyProxy.
   */
  protected void findMyProxyFilesFromConfiguration() {
    
    // The CA consists in three files and MyProxy has a storage directory.
    // Find them with the system configuration. If any key is not set,
    // then the corresponding File property in this bean remains set to null.
    Config config = SimpleConfig.getSingleton();
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
