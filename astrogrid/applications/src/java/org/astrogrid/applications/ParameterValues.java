/*
 * $Id: ParameterValues.java,v 1.3 2003/12/05 22:52:16 pah Exp $
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
 * This is a container for passing the parameter values to the application. It is essentially a container for the xml fragment that describes a jobstep parameters.
 * @author pah 
 */
public class ParameterValues {
   public String getMethodName(){ return methodName; }

   public void setMethodName(String methodName){ this.methodName = methodName; }

   public String getParameterSpec(){ return parameterSpec; }

   public void setParameterSpec(String parameterSpec){ this.parameterSpec = parameterSpec; }

/**
 * The name of the method (or interface as it is called in the applicationDescription schema) that this set of paramters is intended for
 * 
 * @TODO rename this to interfaceName
 */
   private String methodName;
 
   /**
    * The list of input/output parameters as a simple XML fragment. This is of the form
    * <pre>
    *&lt;input&gt;<br>
&lt;parameter name="query" type="agpd:File_Reference" &gt;<br>
myspace://image1<br>
&lt;/parameter&gt;<br>
&lt;parameter name="integation_time" type="xs:integer"&gt;<br>
60<br>
&lt;/parameter&gt;<br>
&lt;/input&gt;<br>
&lt;output&gt;<br>
&lt;parameter name="result" type="agpd:VOTable_Reference"&gt;<br>
myspace://votable1<br>
&lt;/parameter&gt;<br>
&lt;/output&gt;
<pre>
    * 
    */
      private String parameterSpec;
}
