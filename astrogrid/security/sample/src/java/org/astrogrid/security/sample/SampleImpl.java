package org.astrogrid.security.sample;

import org.astrogrid.security.ServiceSecurityGuard;


public class SampleImpl implements SamplePortType {

  public String whoAmI () {
    ServiceSecurityGuard sg = ServiceSecurityGuard.getInstanceFromContext();
    if (sg.isAnonymous()) {
      return "anonymous";
    }
    else {
      return sg.getUsername();
    }
  }

}
