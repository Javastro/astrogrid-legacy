/*
 * $Id: AbstractApplicationController.java,v 1.4 2003/11/25 12:25:26 pah Exp $
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
import org.astrogrid.applications.ApplicationDescription;
import org.astrogrid.applications.Parameter;
import javax.sql.DataSource;

abstract public class AbstractApplicationController implements ApplicationController {

   /**
    * The place where the application controller stores local execution status. 
    */
   private DataSource db;
}
