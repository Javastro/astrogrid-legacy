/*
 * $Id: Castor2Axis.java,v 1.1 2004/03/18 21:24:03 pah Exp $
 * 
 * Created on 11-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.common.bean;

import javax.xml.namespace.QName;

import org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterTypes;
import org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.axis._input;
import org.astrogrid.workflow.beans.v1.axis._output;
import org.astrogrid.workflow.beans.v1.axis._tool;

/**
 * Static methods to convert Castor to Axis objects.
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Mar-2004
 * @version $Name:  $
 * @since iteration5
 * @TODO - this is rather fragile to changes in the schema - would be better perhaps to use castor serialization in axis directly...or get the axis object marshalling
 */
public class Castor2Axis {

   /**
    * 
    */
   private Castor2Axis() {
   }

   public static _tool convert(Tool ctool) {
      _tool result = new _tool();
      result.set_interface(ctool.getInterface());
      result.setName(ctool.getName());
      result.setInput(convert(ctool.getInput()));
      result.setOutput(convert(ctool.getOutput()));

      return result;
   }

   /**
    * @param output
    * @return
    */
   public static _output convert(Output output) {
      
      org.astrogrid.applications.beans.v1.parameters.ParameterValue[] inpars = output.getParameter();
      _output result = new _output();
      result.setParameter(transformParameters(inpars));
      return result;
   }

   /**
    * @param input
    * @return
    */
   public static _input convert(Input input) {
      _input result = new _input();
      org.astrogrid.applications.beans.v1.parameters.ParameterValue[] inpars = input.getParameter();
      result.setParameter(transformParameters(inpars));
      return result;
   }
   
   private static ParameterValue[] transformParameters(org.astrogrid.applications.beans.v1.parameters.ParameterValue[] inpars)
   {
      ParameterValue[] result = new ParameterValue[inpars.length];
      
      for (int i = 0; i < inpars.length; i++) {
         // this is a bit inefficient as we are thowing away the objects created in the new above..
         result[i] = convert(inpars[i]);
      }
      
      return result;
   }
   
   public static ParameterValue convert(org.astrogrid.applications.beans.v1.parameters.ParameterValue parameterValue)
   {
      ParameterValue result = new ParameterValue();
      result.setName(parameterValue.getName());
      result.setType(convert(parameterValue.getType()));
      result.setValue(parameterValue.getValue());
     
      return result;
   }

   /**
    * @param types
    * @return
    */
   private static ParameterTypes convert(org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes type) {
      ParameterTypes result = ParameterTypes.fromValue(new QName(type.toString()));
      return result;
   }

}
