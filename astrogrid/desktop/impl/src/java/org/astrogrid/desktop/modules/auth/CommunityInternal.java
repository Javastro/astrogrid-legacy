/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */

package org.astrogrid.desktop.modules.auth;

import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.security.SecurityGuard;

/** Internal interface to community. 
 * <p />
 * Extension of the AR Community interface which allows access to credentials, and also to change password.
 *
 * @author Guy Rixon
 */
public interface CommunityInternal extends Community {
  
  /**
   * Reveals the credentials and principals that were cached when the user 
   * logged in. Changes to the external copy affect the cached copy. If the
   * user is not logged in, then the returned object will contain no
   * credentials or principals; it will never be null.
   *
   * @return - The credentials and principals.
   */
  public SecurityGuard getSecurityGuard();
  
  /** change the password for the currenlty logged in account */
  public void changePassword(final String newPassword) throws ServiceException, SecurityException;
}
