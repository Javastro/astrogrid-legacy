/*
 * $Id: Application.java,v 1.6 2004/04/20 09:03:22 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import org.astrogrid.applications.manager.ApplicationExitMonitor;


/**
 * The basic definition of what an application can do.
 * @author Paul Harrison (pah@jb.man.ac.uk) 20-Apr-2004
 * @stereotype entity
 * @robustness Entity 
 * @TODO this needs to be refactored to represent what an application is better.
 */
public interface Application {
   /**
    * Execute an application. This is the main entry point for the application, and will cause asynchronous execution once the parameter values have been set up for the application.
    * When the application finishes it should signal via the given {@link org.astrogrid.applications.manager.ApplicationExitMonitor}.
    * @param mon This is called when the application exits.
    * @return
    * @throws CeaException
    */
   boolean execute(ApplicationExitMonitor mon) throws CeaException;
   /**
    * Add a parameter value to the application. This is called before the application is executed to set up the input and output parameters.
    * @param p
    */
   void addParameter(Parameter p);
   /**
    * Retrieve the result. This is not used in the current implementations
    * @return
    * @deprecated not currently used.
    */
   Result[] retrieveResult();
   /**
    * Obtain the integer completion status. 
    * @TODO This is only really applicable to command line applications - need to refactor.
    * @return
    */
   int completionStatus();

   /**
    * @link
    * @shapeType PatternLink
    * @pattern Factory Method
    * @supplierRole Creator
    * @hidden 
    */
   /*# private ApplicationFactory _applicationFactory; */

   /** @link dependency 
    * @stereotype access
    * @clientRole returns*/
   /*# Result lnkResult; */
}
