/*
 * $Id: ServiceFromConfig.java,v 1.2 2004/03/23 12:51:26 pah Exp $
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

package org.astrogrid.applications.manager.externalservices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.common.config.CeaControllerConfig;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class ServiceFromConfig {

   /**
    * @param config
    */
   public ServiceFromConfig(CeaControllerConfig config) {
      
      this.config = config;
   }

   /**
    * 
    */
   public ServiceFromConfig() {
      super();
      // TODO Auto-generated constructor stub
   }

   protected CeaControllerConfig config;

   protected static org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(MySpaceFromConfig.class);

}
