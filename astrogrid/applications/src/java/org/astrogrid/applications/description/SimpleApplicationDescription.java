/*
 * $Id: SimpleApplicationDescription.java,v 1.1 2003/12/01 22:24:59 pah Exp $
 * 
 * Created on 01-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

/**
 * A bean for holding application description information suitable for client-side use. This is really just a placeholder until it is decided what is really needed.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class SimpleApplicationDescription {
   String xmlDescriptor;

   /**
    * @return
    */
   public String getXmlDescriptor() {
      return xmlDescriptor;
   }

   /**
    * @param string
    */
   public void setXmlDescriptor(String string) {
      xmlDescriptor = string;
   }

}
