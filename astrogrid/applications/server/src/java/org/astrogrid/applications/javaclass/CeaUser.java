package org.astrogrid.applications.javaclass;

import org.astrogrid.community.User;
import org.astrogrid.security.SecurityGuard;

/**
 *
 * @author gtr
 * @deprecated this is not used anywhere - pah - use {@link SecurityGuard}
 */
public class CeaUser {
  
  public static InheritableThreadLocal store;
  
  public static void  setUser(User u) {
    CeaUser.store.set(u);
  }
  
  public static User getUser() {
    return (User)(CeaUser.store.get());
  } 
  
}
