/*
 * $Id: WebServiceApplication.java,v 1.5 2004/04/19 17:34:08 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.webservice;

import java.io.File;
import java.net.URL;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Result;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.applications.manager.ApplicationExitMonitor;
import org.astrogrid.community.User;

public class WebServiceApplication extends AbstractApplication {


   

   /**
    * @param controller
    * @param user
    */
   public WebServiceApplication(AbstractApplicationController controller, User user) {
      super(controller, user);
      // TODO Auto-generated constructor stub
   }



   private URL url;
   
   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#completionStatus()
    */
   public int completionStatus() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("WebServiceApplication.completionStatus() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.AbstractApplication#createLocalTempFile()
    */
   public File createLocalTempFile() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("WebServiceApplication.createLocalTempFile() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#retrieveResult()
    */
   public Result[] retrieveResult() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("WebServiceApplication.retrieveResult() not implemented");
   }

   /** 
    * @see org.astrogrid.applications.Application#execute(org.astrogrid.applications.manager.ApplicationExitMonitor)
    */
   public boolean execute(ApplicationExitMonitor mon) throws CeaException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("WebServiceApplication.execute() not implemented");
   }

}
