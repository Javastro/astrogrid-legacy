/*
 * $Id: Status.java,v 1.1 2004/01/04 14:51:22 pah Exp $
 * 
 * Created on 02-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

/**
 * The status values that the application can have. Follows a typesafe enum pattern.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class Status {

   private final String value;
   /**
    * 
    */
   private Status(String value) {
      this.value = value;
   }
   

  
   public String toString() {
     return value;
   }
   public static final Status NEW = new Status("New");
   public static final Status INITIALIZED = new Status("Initialized");
   public static final Status RUNNING = new Status("Running");
   public static final Status COMPLETED = new Status("Completed");
   public static final Status ERROR = new Status("Error");
   public static final Status UNKNOWN = new Status("Unknown");
}
