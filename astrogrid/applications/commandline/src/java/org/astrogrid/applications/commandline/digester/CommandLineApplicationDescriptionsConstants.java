/*
 * $Id: CommandLineApplicationDescriptionsConstants.java,v 1.3 2004/08/27 19:39:54 pah Exp $
 * 
 * Created on 04-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.digester;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
final class CommandLineApplicationDescriptionsConstants {
   public static final String NAME_ATTR = "name";
   public static final String APPLICATIONCONTROLLER_ELEMENT =
      "CommandLineExecutionControllerConfig";
   public static final String APPLICATION_ELEMENT =
      APPLICATIONCONTROLLER_ELEMENT+ "/Application";
      public static final String EXPATH_ELEMENT = APPLICATION_ELEMENT + "/ExecutionPath";
   public static final String PARAMETER_ELEMENT =
      APPLICATION_ELEMENT+"/Parameters/CmdLineParameterDefn";
   public static final String UI_NAME_ELEMENT = PARAMETER_ELEMENT + "/*:UI_Name";
   public static final String UI_DESC_ELEMENT = PARAMETER_ELEMENT + "/*:UI_Description";
   public static final String UI_DESC_CHILDREN = PARAMETER_ELEMENT + "/*:UI_Description/*";
   public static final String UCD_ELEMENT = PARAMETER_ELEMENT + "/*:UCD";
   public static final String DEFVAL_ELEMENT = PARAMETER_ELEMENT + "/*:DefaultValue";
   public static final String UNITSL_ELEMENT = PARAMETER_ELEMENT + "/*:Units";
   public static final String INTERFACE_ELEMENT= APPLICATION_ELEMENT + "/Interfaces/Interface";
   public static final String INPUT_PREFS = INTERFACE_ELEMENT + "/input/pref";
   public static final String OUTPUT_PREFS = INTERFACE_ELEMENT + "/output/pref";

}
