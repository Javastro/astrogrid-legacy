/*
 * $Id: Status.java,v 1.2 2004/04/19 17:34:08 pah Exp $
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

import java.util.HashMap;
import java.util.Map;

/**
 * The status values that the application can have. Follows a typesafe enum pattern.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class Status {

   private final String value;
   private static Map allmap = new HashMap();
   /**
    * 
    */
   private Status(String value) {
      this.value = value;
      allmap.put(value, this);
   }
   

  
   public String toString() {
     return value;
   }
   
   public static Status valueOf(String val)
   {
      Status retval;
      retval = (Status)allmap.get(val);
      return retval;
   }
   
   public static final Status NEW = new Status("New");
   public static final Status INITIALIZED = new Status("Initialized");
   public static final Status RUNNING = new Status("Running");
   public static final Status COMPLETED = new Status("Completed");
   public static final Status WRITINGBACK = new Status("writing parameters back");
   public static final Status ERROR = new Status("Error");
   public static final Status UNKNOWN = new Status("Unknown");
}
