/*
 * $Id: Dft.java,v 1.1 2004/01/20 12:03:49 pah Exp $
 * 
 * Created on 20-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.dft;

import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.community.User;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * @TODO should not really need this class - better to build adding standard parameters to the interface definition so that the standard commandline application could deal with it
 */
public class Dft extends CmdLineApplication {

   /**
    * 
    */
   public Dft() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param controller
    * @param user
    */
   public Dft(AbstractApplicationController controller, User user) {
      super(controller, user);
      // TODO Auto-generated constructor stub
   }
   
   

   /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preRunHook()
    */
   protected void preRunHook() {
      argvals.add("output=VOTable");
      
   }

}
