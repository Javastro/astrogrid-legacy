/*
 * $Id: RegistryEntryBuilder.java,v 1.2 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 22-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.registry.beans.resource.VODescription;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryEntryBuilder {

   private ApplicationBase desc;

   /**
    * 
    */
   public RegistryEntryBuilder(ApplicationBase baseDesciption) {
      
      this.desc = baseDesciption;
   }
   
   public VODescription getEntry()
   {
      VODescription vodesc = new VODescription();
      
      return vodesc;
      
      
   }

}
