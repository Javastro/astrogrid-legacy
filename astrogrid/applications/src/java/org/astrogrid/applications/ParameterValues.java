/*
 * $Id: ParameterValues.java,v 1.1 2003/11/25 12:25:26 pah Exp $
 *
 * Created on 24 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

/**
 * This is a container for passing the parameter values to the appication. It is essentially a container for the xml fragment that describes a jobstep parameters. 
 */
public class ParameterValues {
   public String getMethodName(){ return methodName; }

   public void setMethodName(String methodName){ this.methodName = methodName; }

   public String getParameterSpec(){ return parameterSpec; }

   public void setParameterSpec(String parameterSpec){ this.parameterSpec = parameterSpec; }

   private String methodName;
   private String parameterSpec;
}
