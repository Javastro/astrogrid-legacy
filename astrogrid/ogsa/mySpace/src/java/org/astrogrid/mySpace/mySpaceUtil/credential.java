package org.astrogrid.mySpace.mySpaceUtil;

import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.gridforum.jgss.ExtendedGSSManager;
import org.ietf.jgss.GSSCredential;

public class credential {

  public GSSCredential getCredentials () throws Exception {
    GSSCredential credential = null;

    // try to load the user's existing proxy.
    Exception e1 = null;
    try {

      ExtendedGSSManager m =
                  (ExtendedGSSManager) ExtendedGSSManager.getInstance();
        credential = m.createCredential(GSSCredential.INITIATE_AND_ACCEPT);
    }
    catch (Exception e) {
      e1 = e;
    }
    if (credential != null) return credential;

    // Try to make a new proxy from the host's certficate and key.
    Exception e2 = null;
    try {
      // KLUDGE! hard-coded locations for files!
      GlobusCredential gc =
          new GlobusCredential("/etc/grid-security/hostcert.pem",
                               "/etc/grid-security/hostkey.pem");
        credential =
            new GlobusGSSCredentialImpl(gc, GSSCredential.ACCEPT_ONLY);
      }
    catch (Exception e) {
      e2 = e;
    }
    if (credential != null) return credential;

    // If this point of the method is reached, then the method
    // has failed to raise any credentials.
    throw new Exception("No identity credentials could be obtained. "
                        + "The user's proxy is not available ("
                        + e1.getMessage()
                        + ") and no proxy could be made from the "
                        + "host certficate and private key ("
                        + e2.getMessage()
                        +").");
  }
}
