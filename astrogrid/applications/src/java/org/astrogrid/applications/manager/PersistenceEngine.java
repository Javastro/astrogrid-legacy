/*
 * $Id: PersistenceEngine.java,v 1.1 2003/12/05 22:52:16 pah Exp $
 * 
 * Created on 05-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import javax.sql.DataSource;

import com.sun.enterprise.deployment.Application;

import org.astrogrid.applications.common.config.ApplicationControllerConfig;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class PersistenceEngine {

   /**
    * get a new executionID.
    */
   public static int getNewID() {
      
      DataSource ds = ApplicationControllerConfig.getInstance().getDataSource();
      throw new UnsupportedOperationException("persistence engine not implemented");
      
     
   }
   

}
