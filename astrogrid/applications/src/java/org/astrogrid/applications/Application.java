/*
 * $Id: Application.java,v 1.4 2004/03/23 12:51:25 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;


/**
 * @stereotype entity
 * @robustness Entity 
 */
public interface Application {
   boolean execute() throws CeaException;
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
