/*
 * $Id: AbstractApplicationController.java,v 1.5 2003/11/26 22:07:24 pah Exp $
 *
 * Created on 13 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptions;

import javax.sql.DataSource;
import java.util.List;

abstract public class AbstractApplicationController implements ApplicationController {

   /**
    * The place where the application controller stores local execution status. 
    */
   private DataSource db;

   /**
    * The store for the descriptions of the applications that this application controller manages.
    */
   private ApplicationDescriptions applicationDescriptions;
   
   
   /**
    * @return
    */
   public ApplicationDescriptions getApplicationDescriptions() {
      return applicationDescriptions;
   }

   /**
    * @param descriptions
    */
   public void setApplicationDescriptions(ApplicationDescriptions descriptions) {
      applicationDescriptions = descriptions;
   }

}
