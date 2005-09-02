package org.astrogrid.security.sample;

import javax.security.auth.Subject;
import org.astrogrid.security.AxisServiceSecurityGuard;


public class SampleImpl implements SamplePortType {

  public String whoAmI () {
    System.out.println("whoAmI()");
    AxisServiceSecurityGuard sg = AxisServiceSecurityGuard.getInstanceFromContext();
    Subject s = sg.getGridSubject();
    System.out.println(s.getPrincipals().size() + " Principals in gridSubject");
    System.out.println(s.getPrivateCredentials().size() + " private credentials in gridSubject");
    if (sg.isAnonymous()) {
      System.out.println("Service: caller is anonymous");
      return "anonymous";
    }
    else {
      String name = sg.getGridPrincipal().getName();
      System.out.println("Service: caller is " + name);
      return name;
    }
  }

}
