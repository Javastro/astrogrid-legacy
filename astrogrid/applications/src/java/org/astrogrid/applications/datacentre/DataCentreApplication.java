/*
 * $Id: DataCentreApplication.java,v 1.5 2004/04/28 16:10:11 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.datacentre;

import java.io.File;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Result;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.applications.manager.ApplicationExitMonitor;
import org.astrogrid.community.User;
public class DataCentreApplication extends AbstractApplication {

   /**
    * @param controller
    * @param user
    */
   public DataCentreApplication(AbstractApplicationController controller, User user) {
      super(controller, user);
   }

   
   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#completionStatus()
    */
   public int completionStatus() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("DataCentreApplication.completionStatus() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.AbstractApplication#createLocalTempFile()
    */
   public File createLocalTempFile() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("DataCentreApplication.createLocalTempFile() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.Application#retrieveResult()
    */
   public Result[] retrieveResult() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("DataCentreApplication.retrieveResult() not implemented");
   }


   /** 
    * @see org.astrogrid.applications.AbstractApplication#execute(org.astrogrid.applications.manager.ApplicationExitMonitor)
    */
   public boolean execute(ApplicationExitMonitor mon) throws CeaException {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("DataCentreApplication.execute() not implemented");
   }

}
