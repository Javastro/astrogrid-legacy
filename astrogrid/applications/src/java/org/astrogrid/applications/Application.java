/*
 * $Id: Application.java,v 1.5 2004/04/19 17:34:08 pah Exp $
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
 * @stereotype entity
 * @robustness Entity 
 * @TODO this needs to be refactored to represent what an application is better
 */
public interface Application {
   boolean execute(ApplicationExitMonitor mon) throws CeaException;
   void addParameter(Parameter p);
   Result[] retrieveResult();
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
