/*
 * $Id: ParameterFactory.java,v 1.1 2003/12/08 17:06:35 pah Exp $
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

import org.astrogrid.applications.Parameter;

/**
 * a factory to create {@link org.astrogrid.applications.Parameter} instances;
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ParameterFactory extends AbstractObjectCreationFactory {
   
   private ApplicationDescription applicationDescription;
   
   ParameterFactory(ApplicationDescription applicationDescription)
   {
      this.applicationDescription = applicationDescription;
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
         param = pd.createValueObject();
      }
      
      return param;

   }

}
