/*
 * $Id: HyperZ.java,v 1.2 2004/01/18 12:28:00 pah Exp $
 * 
 * Created on 16-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.hyperz;

import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.community.User;

/**
 * Specialization for HyperZ. This needs to take a VOTable and convert it to suitable form for native input into hyperZ, and then create a 
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * @TODO there are dependencies on the AVO DEMO setup file paths that need to be removed
 */
public class HyperZ extends CmdLineApplication {
   
   

   /**
    * 
    */
   public HyperZ() {
      super();
   }

   /**
    * @param controller
    * @param user
    */
   public HyperZ(AbstractApplicationController controller, User user) {
      super(controller, user);
   }
   
   

   /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preRunHook()
    */
   protected void preRunHook() {
      
      // Add in the missing parameters from the simple interface FIXME - add these to the main interface and remove from here...
      argvals.add("-FILTERS_RES");
      argvals.add("/home/applications/demo/hyperz/FILTER.RES");
      argvals.add("-FILTERS_FILE");
      argvals.add("/home/applications/demo/hyperz/cdfs-bviz.param");
      argvals.add("-TEMPLATES_FILE");
      argvals.add("/home/applications/demo/hyperz/spectra.param");
      args = (String[])argvals.toArray(new String[0]);
    
      
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#preWritebackHook()
    */
   protected void preWritebackHook() {
      // TODO convert the output file 
      
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#postParamSetupHook()
    */
   protected void postParamSetupHook() {
      // TODO Auto-generated method stub
   }

}
