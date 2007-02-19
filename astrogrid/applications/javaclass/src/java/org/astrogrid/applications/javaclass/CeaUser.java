package org.astrogrid.applications.javaclass;

import org.astrogrid.community.User;

/**
 *
 * @author gtr
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
