package org.astrogrid.security.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.security.AxisServiceSecurityGuard;


public class SampleImpl implements SamplePortType {
  
  private static Log log = LogFactory.getLog(SampleImpl.class);

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
