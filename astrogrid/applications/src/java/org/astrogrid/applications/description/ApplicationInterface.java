/*
 * $Id: ApplicationInterface.java,v 1.2 2003/11/27 12:40:48 pah Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.description;

import java.util.List;
public class ApplicationInterface {
   public String getName(){ return name; }

   public void setName(String name){ this.name = name; }

   private String name;

   /**
    * list of references to the {@link ParameterDescription} objects that make up the inputs for this interface.
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ParameterDescription
    */
   private List inputs;

   /**
    * list of references to the {@link ParameterDescription} objects that make up the outputs for this interface.
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ParameterDescription
    */
   private List outputs;
}
