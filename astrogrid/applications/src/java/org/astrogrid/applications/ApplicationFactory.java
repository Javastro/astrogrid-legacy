/*
 * $Id: ApplicationFactory.java,v 1.1 2003/10/30 14:12:25 pah Exp $
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
 * @stereotype factory 
 */
public interface ApplicationFactory {
   Application createApplication();

   /**
    * @link
    * @shapeType PatternLink
    * @pattern Factory Method
    * @clientRole Creator
    * @supplierRole Product 
    */
   /*# private Application _application; */

   /**
    * @link
    * @shapeType PatternLink
    * @pattern Factory Method
    * @supplierRole Concrete creators 
    */
   /*# private ApplicationCreator _dataCentreApplicationCreator; */
}
