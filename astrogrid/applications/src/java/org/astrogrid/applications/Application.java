/*
 * $Id: Application.java,v 1.2 2003/12/07 01:09:48 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import org.astrogrid.applications.commandline.exceptions.ApplicationExecutionException;

/**
 * @stereotype entity
 * @robustness Entity 
 */
public interface Application {
   boolean execute() throws ApplicationExecutionException;
   void setParameter();
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
