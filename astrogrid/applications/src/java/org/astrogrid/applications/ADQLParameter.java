/*
 * $Id: ADQLParameter.java,v 1.4 2004/04/20 09:03:22 pah Exp $
 *
 * Created on 21 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import org.astrogrid.applications.description.ParameterDescription;


/**
 * A parameter that consists of ADQL. ADQL is....
 * @author Paul Harrison (pah@jb.man.ac.uk) 20-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class ADQLParameter extends Parameter {
  
   /**
    * @param application
    * @param parameterDescription
    */
   public ADQLParameter(AbstractApplication application,
   ParameterDescription parameterDescription) {
      super(application, parameterDescription);
   }

}
