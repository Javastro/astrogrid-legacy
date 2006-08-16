package org.astrogrid.community.server.ca;

import java.io.File;
import java.io.IOException;

/**
 * The collection of files used by the CA to certify each user.
 *
 * @author Guy Rixon
 */
public class UserFiles {
  
  /**
   * Constructs a UserFiles.
   */
  public UserFiles(File   parentDirectory,
                   String userLoginName) throws IOException {
    assert parentDirectory != null;
    assert userLoginName != null;
    assert parentDirectory.exists();
    
    // Generate the locations of all the files.
    // This does not create anything in the file system.
    this.userDirectory = new File(parentDirectory, userLoginName);
    this.key           = new File(userDirectory,   "key.pem");
    this.certificate   = new File(userDirectory,   "certificate.pem");
    this.request       = new File(userDirectory,   "request.pem");
    
    // Create the user directory.
    this.userDirectory.mkdirs();
  }
  
  /**
   * The directory in which are kept all the files for this user.
   */
  protected File userDirectory;
  
  /**
   * Reveals the user directory.
   */
  public File getUserDirectory() {
    return this.userDirectory;
  }
  
  /** 
   * The file holding the user's private key.
   */
  protected File key;
  
  /**
   * Reveals the key file.
   */
  public File getKeyFile() {
    return this.key;
  }
  
  /**
   * The file holding the user's certificate.
   */
  protected File certificate;
  
  /**
   * Reveals the certificate file.
   */
  public File getCertificateFile() {
    return this.certificate;
  }
  
  /**
   * The file holding the user's certificate request.
   */
  protected File request;
  
  /**
   * Reveals the certificate-request file.
   */
  public File getCertificateRequestFile() {
    return this.request;
  }
  
  /**
   * Deletes the files in the user directory.
   */
  public void deleteUserFiles() {
    this.key.delete();
    this.certificate.delete();
    this.request.delete();
  }
  
  /**
   * Deletes the user directory and the files in it.
   */
  public void deleteUserDirectory() {
    this.deleteUserFiles();
    this.userDirectory.delete();
  }
 
}
