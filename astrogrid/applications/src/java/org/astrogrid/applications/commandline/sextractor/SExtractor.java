/*
 * $Id: SExtractor.java,v 1.2 2004/01/18 12:28:00 pah Exp $
 *
 * Created on 24 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline.sextractor;

import java.io.IOException;

import com.sun.corba.se.internal.corba.EncapsInputStream;

import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.community.User;

public class SExtractor extends CmdLineApplication {

   /**
    * @param controller
    * @param user
    */
   public SExtractor(AbstractApplicationController controller, User user) {
      super(controller, user);
   }
   public SExtractor()
   {
   }
 
   /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preWritebackHook()
    */
   protected void preWritebackHook() {
      
      // need to convert the output to a VOTable
      
     try {
      ASCII2VOTableConverter conv = new ASCII2VOTableConverter(findParameter("CATALOG_NAME"), findParameter("PARAMETERS_NAME"), applicationEnvironment);
      conv.writeVOTable();
   }
   catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
   }     
   }
   
   
  
   

   /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preRunHook()
    */
   protected void preRunHook() {
      argvals.add("-CATALOG_TYPE");
      argvals.add("ASCII_HEAD");
      // FIXME temp kludge to work for AVO demo
      argvals.add("-FILTER_NAME");
      argvals.add("/home/applications/demo/h_goods_r1.0z_detect_conv.txt");
      argvals.add("-CHECKIMAGE_TYPE");
      argvals.add("NONE");
      argvals.add("-WEIGHT_TYPE");
      argvals.add("BACKGROUND");
      argvals.add("-DETECT_THRESH");
      argvals.add("4.0");
      args = (String[])argvals.toArray(new String[0]);
   }

}
