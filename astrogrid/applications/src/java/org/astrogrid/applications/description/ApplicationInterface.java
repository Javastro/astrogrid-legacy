/*
 * $Id: ApplicationInterface.java,v 1.1 2003/11/26 22:07:24 pah Exp $
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
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ParameterDescription
    */
   private List inputs;

   /**
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ParameterDescription
    */
   private List outputs;
}
