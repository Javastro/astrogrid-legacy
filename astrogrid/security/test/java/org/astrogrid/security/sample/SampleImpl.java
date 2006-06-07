package org.astrogrid.security.sample;

import org.apache.log4j.Logger;
import org.astrogrid.security.AxisServiceSecurityGuard;


public class SampleImpl implements SamplePortType {
  
  private static Logger log 
      = Logger.getLogger("org.astrogrid.security.sample.SampleImpl");

  public String whoAmI () {
    AxisServiceSecurityGuard sg = AxisServiceSecurityGuard.getInstanceFromContext();
    if (sg.isAnonymous()) {
      log.info("Caller is anonymous");
      return "anonymous";
    }
    else {
      String name = sg.getX500Principal().getName();
      log.info("Caller is " + name);
      return name;
    }
  }

}
