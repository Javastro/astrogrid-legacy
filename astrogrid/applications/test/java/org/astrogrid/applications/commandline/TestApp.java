/*
 * $Id: TestApp.java,v 1.4 2003/12/31 00:56:17 pah Exp $
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

package org.astrogrid.applications.commandline;

import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.community.User;

/**
 * This application does not do anything special - just here to test that the class loading is working...
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class TestApp extends CmdLineApplication {
   
   
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(TestApp.class);
   /**
    * constructor - just so that we can see that this is the class that is being instantiated....
    */
   public TestApp(AbstractApplicationController controller, User user) {
      super(controller, user);
      logger.info("constructing test application");
   }

}
