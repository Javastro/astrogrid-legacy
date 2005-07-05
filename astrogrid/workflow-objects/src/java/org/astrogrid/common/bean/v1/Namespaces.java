/*
 * $Id: Namespaces.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 * 
 * Created on 02-Apr-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.common.bean.v1;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 02-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public final class Namespaces {

   /**
    * 
    */
   private Namespaces() {
   }

   public final static String VOCEA = "http://www.ivoa.net/xml/CEAService/v0.2";
   public final static String CEAPD = "http://www.astrogrid.org/schema/AGParameterDefinition/v1";
   public final static String CEAB="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1" ;
   public final static String CEATYPES="http://www.astrogrid.org/schema/CEATypes/v1";
   public final static String CEAIMPL="http://www.astrogrid.org/schema/CEAImplementation/v1";
   public final static String WORKFLOW="http://www.astrogrid.org/schema/AGWorkflow/v1";
   public final static String CREDENTIALS = "http://www.astrogrid.org/schema/Credentials/v1";
   public final static String EXECUTIONRECORD = "http://www.astrogrid.org/schema/ExecutionRecord/v1";

   //plus some stqndard ivoa ones...
   
   public final static String VORESOURCE= "http://www.ivoa.net/xml/VOResource/v0.10";
   public final static String VODATASERVICE = "http://www.ivoa.net/xml/VODataService/v0.5";
   public static final String REGINTERFACE = "http://www.ivoa.net/xml/RegistryInterface/v0.1";
}
