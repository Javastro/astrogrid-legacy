/*
 * $Id: ApplicationDescription.java,v 1.2 2003/11/18 17:58:49 pah Exp $
 * 
 * Created on 14-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

/**
 * This class represents the description of the application. This will be in an extension of wsdl.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @TODO this is only a placeholder until proper implementation is sorted.
 */
public class ApplicationDescription {
   private String name;
   private String parameter;
   
   public ApplicationDescription()
   {
      name="defaultname";
   }
   
   public ApplicationDescription(String applicationID)
   {
      name = applicationID;
   }
   /**
    * @return
    */
   public String getName() {
      return name;
   }

   /**
    * @return
    */
   public String getParameter() {
      return parameter;
   }

   /**
    * @param string
    */
   public void setName(String string) {
      name = string;
   }

   /**
    * @param string
    */
   public void setParameter(String string) {
      parameter = string;
   }

}
