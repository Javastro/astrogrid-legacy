/*
 * $Id: ParameterCreationFactory.java,v 1.5 2004/03/03 18:33:04 pah Exp $
 * 
 * Created on 28-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
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

import org
   .astrogrid
   .applications
   .description
   .exception
   .ParameterTypeNotDefinedException;

/**
 * This is the factory where all of the different parameter definition types are created.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
class ParameterCreationFactory extends AbstractObjectCreationFactory {
   /**
    * @param DescriptionLoader
    */
   ParameterCreationFactory() {
   }

   /* (non-Javadoc)
    * @see org.apache.commons.digester.ObjectCreationFactory#createObject(org.xml.sax.Attributes)
    */
   private static final String TYPE_ATTR = "type";
   public Object createObject(Attributes attr) throws Exception {
      //IMPL new types must be added here....
      if (attr.getValue(TYPE_ATTR) != null) {
         String type = attr.getValue(TYPE_ATTR);
         if (type.equals("double")) {
            return new DoubleParameterDescription();

         }
         else
            if (type.equals("integer")) {
               return new IntParameterDescription();
            }
         else
            if (type.equals("string")) {
               return new StringParameterDescription();
            }
         else
            if (type.equals("MySpace_FileReference")) {
               return new MySpaceReferenceParameterDescription();

            }
         else
            if (type.equals("FileReference")) {
               return new FileReferenceParameterDescription();

            }
         else
            if (type.equals("MySpace_VOTableReference")) {
               return new VOTableReferenceParameterDescription();

            }
         else
             if (type.equals("RA")) {
                return new RAParameterDescription();

             }
         else
             if (type.equals("Dec")) {
                return new DecParameterDescription();

             }
         else
             if (type.equals("ADQL")) {
                return new ADQLParameterDescription();

             }
         else
             if (type.equals("boolean")) {
                return new BooleanParameterDescription();

             }
         else
             if (type.equals("anyURI")) {
                //TODO this is not really necessary? Should make the myspace file reference cope with http: and myspace: protocols
                return new URIParameterDescription();

             }
                  
                  else {
                     throw new ParameterTypeNotDefinedException(type);
                  }
      }
      else {
         throw new ParameterTypeNotDefinedException("");
      }
   }

}