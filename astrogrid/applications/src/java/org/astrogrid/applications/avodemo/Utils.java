/*
 * $Id: Utils.java,v 1.1 2004/01/23 19:20:22 pah Exp $
 * 
 * Created on 23-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.avodemo;

import org.astrogrid.applications.delegate.beans.User;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class Utils {



   /**
    * 
    */
   public Utils() {
      super();
      // TODO Auto-generated constructor stub
   }
   
   public static User createUserBean(String account)
   {
      User retval = new User();
      retval.setAccount(account);
      retval.setGroup("avo");
      retval.setToken("dummy");
      return retval;
   }

}
