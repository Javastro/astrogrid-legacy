/*
 * $Id: ParameterCreationFactory.java,v 1.1 2003/11/29 00:50:14 pah Exp $
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
         if (type.equals("xs:double")) {
            return new DoubleParameterDescription();

         }
         else
            if (type.equals("xs:integer")) {
               return new IntParameterDescription();
            }
         else
            if (type.equals("xs:string")) {
               return new StringParameterDescription();
            }
            else
               if (type.equals("agpd:MySpace_FileReference")) {
                  return new FileReferenceParameterDescription();

               }
         else
            if (type.equals("agpd:MySpace_VOTableReference")) {
               return new VOTableReferenceParameterDescription();

            }
         else
             if (type.equals("agpd:RA")) {
                return new RAParameterDescription();

             }
         else
             if (type.equals("agpd:Dec")) {
                return new DecParameterDescription();

             }
         else
             if (type.equals("agpd:ADQL")) {
                return new ADQLParameterDescription();

             }
                  
                  else {
                     throw new ParameterTypeNotDefinedException();
                  }
      }
      else {
         throw new ParameterTypeNotDefinedException();
      }
   }

}