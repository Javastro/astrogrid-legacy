/*
 * $Id: CredentialHolder.java,v 1.1 2004/03/15 12:32:00 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.warehouse.ogsadai;

import org.apache.log4j.Logger;

import org.ietf.jgss.GSSCredential;
import org.gridforum.jgss.ExtendedGSSManager;
import org.globus.ogsa.utils.QueryHelper;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;


/**
 *
 * @author K Andrews
 */
/**
 * A utility class for creating an X-509-style credential. 
 *
 * If a user credential is present, this is used;  otherwise, an attempt
 * is made to create a host credential using the host certificate.
 * 
 */
public class CredentialHolder
{
  /**
   * Default empty constructor.
   * 
   * @throws Exception
   * @throws IOException
   * @throws SAXException
   */
  
  static Logger logger = Logger.getLogger("CredentialHolder");

  /* The created credential */
  private GSSCredential credential = null;
  

  public CredentialHolder(String hostCertLocation, String hostKeyLocation) 
      throws Exception {

    // First try to use end-user's proxy credential, if one exists
    Exception e1 = null, e2 = null;
    try {
      ExtendedGSSManager manager =
        (ExtendedGSSManager)ExtendedGSSManager.getInstance();
      credential = manager.createCredential(GSSCredential.INITIATE_AND_ACCEPT);
    }   
    catch (Exception e) {
      e1 = e;
    }
    if ( credential == null ) { // No user credential

      // Check that host keys are valid
      if (hostCertLocation == null || hostKeyLocation == null) {
        String errMess = "No identity credentials could be obtained. "
            + "The user's proxy is not available ("
            + e1.getMessage()
            + ") and a host certificate/key was not supplied.";
        logger.error(errMess);
        throw new Exception(errMess);
      }
      GlobusCredential gc = null;
      // Try to make a new proxy from the host's certificate and key.
      try {
        gc = new GlobusCredential(hostCertLocation, hostKeyLocation);
        credential = new GlobusGSSCredentialImpl(
             gc, GSSCredential.INITIATE_AND_ACCEPT);
        //logger.debug(credential.getName().toString());
        //logger.debug(credential.getRemainingLifetime());
        //logger.debug(credential.getUsage());
      }
      catch (Exception e) {
        String errMess = ("No identity credentials could be obtained. "
            + "The user's proxy is not available ("
            + e1.getMessage()
            + ") and no proxy could be made from the "
            + "host certificate and private key ("
            + e.getMessage()
            +").");
        logger.error(errMess);
        throw new Exception(errMess);
      }
    }
  }

  public GSSCredential getCredential() {
    return credential;
  }
}
