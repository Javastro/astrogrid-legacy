/*
 * $Id: CommandLineParameter.java,v 1.3 2003/12/08 17:06:35 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import org.astrogrid.applications.description.ParameterDescription;

public class CommandLineParameter extends Parameter {
   private int position;
   /**
    * @param parameterDescription
    */
   public CommandLineParameter(ParameterDescription parameterDescription) {
      super(parameterDescription);
      // TODO Auto-generated constructor stub
   }

}
