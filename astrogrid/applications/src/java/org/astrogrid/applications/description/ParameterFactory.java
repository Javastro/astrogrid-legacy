/*
 * $Id: ParameterFactory.java,v 1.2 2003/12/31 00:56:17 pah Exp $
 * 
 * Created on 08-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.xml.sax.Attributes;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Parameter;

/**
 * a factory to create {@link org.astrogrid.applications.Parameter} instances;
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ParameterFactory extends AbstractObjectCreationFactory {
   
   private ApplicationDescription applicationDescription;
   private AbstractApplication application;
   
   ParameterFactory(AbstractApplication app)
   {
      this.application = app;
      this.applicationDescription = app.getApplicationDescription();
   }

   /* (non-Javadoc)
    * @see org.apache.commons.digester.AbstractObjectCreationFactory#createObject(org.xml.sax.Attributes)
    */
   public Object createObject(Attributes attr) throws Exception {
      
      String name;
      ParameterDescription pd = null;
      Parameter param = null;

      if ((name=attr.getValue(ApplicationDescriptionConstants.NAME_ATTR)) != null) {
         pd = applicationDescription.getParameter(name);
         param = pd.createValueObject(application);
      }
      
      return param;

   }

}
