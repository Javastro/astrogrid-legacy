/*
 * $Id: FileReferenceParameterDescription.java,v 1.3 2004/01/18 12:28:00 pah Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.description;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.FileReferenceParameter;
import org.astrogrid.applications.Parameter;

/**
 * This represents a true local file reference - can be used directly....
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class FileReferenceParameterDescription extends ParameterDescription {
   
   /* (non-Javadoc)
    * @see org.astrogrid.applications.description.ParameterDescription#createValueObject(org.astrogrid.applications.AbstractApplication)
    */
   public Parameter createValueObject(AbstractApplication app) {
      return new FileReferenceParameter(app, this);
   }

}
