package org.astrogrid.security.sample;

import javax.security.auth.Subject;
import org.astrogrid.security.ServiceSecurityGuard;


public class SampleImpl implements SamplePortType {

  public String whoAmI () {
    ServiceSecurityGuard sg = ServiceSecurityGuard.getInstanceFromContext();
    Subject s = sg.getGridSubject();
    System.out.println(s.getPrincipals().size() + " Principals in gridSubject");
    System.out.println(s.getPrivateCredentials().size() + " private credentials in gridSubject");
    if (sg.isAnonymous()) {
      return "anonymous";
    }
    else {
      return sg.getUsername();
    }
  }

}
