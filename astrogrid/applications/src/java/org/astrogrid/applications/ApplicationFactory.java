/*
 * $Id: ApplicationFactory.java,v 1.4 2003/12/31 00:56:17 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.community.User;

/**
 * A factory for creating application objects
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @stereotype factory 
 */
public abstract class ApplicationFactory {
   
   // TODO I do not think that this is a very good pattern..
   public abstract AbstractApplication createApplication(String applicationId, User user) throws ApplicationDescriptionNotFoundException;

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
