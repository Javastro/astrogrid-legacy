package org.astrogrid.ogsa.echo;

import java.rmi.RemoteException;
import javax.security.auth.Subject;
import org.globus.gsi.jaas.JaasGssUtil;
import org.globus.gsi.jaas.JaasSubject;
import org.globus.ogsa.impl.ogsi.GridServiceImpl;
import org.globus.ogsa.impl.security.SecurityManager;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSName;

/**
 * Implements the operations of the echo port-type.
 */
public class EchoPortImpl extends GridServiceImpl implements EchoPortType {

   public EchoPortImpl() {
      super("AstroGrid echo service");
   }
   
   
   /**
    * Returns the given string to the caller.
    */    
   public String echo(String s) {
      return s;
   }
   
   /**
    * Returns the caller's identity, teken from the security credentials.
    */
   public String whoAmI () {
     System.out.println("Entering whoAmI");
     String identity = SecurityManager.getManager().getCaller();
     if (identity == null) identity = "null";
     return identity;
   }
   
  /**
   * Tests the presence of a delegate seciroty-cerdential.
   * Such a credential should be present if the caller uses
   * message-level security in the call to this operation
   * with delegation of privileges turned on.
   *
   * @return the name from the credential
   */
  public String testDelegatedCredentials () throws RemoteException {
    System.out.println("Entering testDelegatedCredentials");
    
    String name = "???";

    try {
      Subject subject = JaasSubject.getCurrentSubject();
      if(subject == null) throw new Exception("Subject is null.");

      GSSCredential gc = JaasGssUtil.getCredential(subject);
      if (gc == null) throw new Exception("Delegated credential is null");
       
      GSSName gn = gc.getName();
      if (gn == null) throw new Exception("Name in credential is null");
  
      String url = "http://astrogrid.ast.cam.ac.uk:9090"
                 + "/ogsa/services/astrogrid/echo/EchoFactory";     
      WhoAmIDelegate wai = new WhoAmIDelegate(url);
      name = wai.whoAmI();
    }
    catch (Exception e) {
      throw new RemoteException("Delegation failed.", e);
    }

    return name;
  }
  
}
