/*
 * $Id: CommunityException.java,v 1.1 2003/09/11 10:24:21 KevinBenson Exp $
 * 
 * Created on 10-Sep-2003 by pah
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.community.common;

/**
 * General Community Exception Class
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class CommunityException extends Exception {

   /**
    * 
    */
   public CommunityException() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param message
    */
   public CommunityException(String message) {
      super(message);
      // TODO Auto-generated constructor stub
   }

   /**
    * @param cause
    */
   public CommunityException(Throwable cause) {
      super(cause);
      // TODO Auto-generated constructor stub
   }

   /**
    * @param message
    * @param cause
    */
   public CommunityException(String message, Throwable cause) {
      super(message, cause);
      // TODO Auto-generated constructor stub
   }

}
