/*
 * $Id: Castor2Axis.java,v 1.10 2004/08/30 17:36:48 jdt Exp $
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

import java.util.Calendar;

import org.apache.axis.types.NMToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionSummaryType;
import org.astrogrid.jes.types.v1.cea.axis.InputListType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;
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
 * @TODO - this is rather fragile to changes in the schema - would be better perhaps to use castor serialization in axis directly...or get the axis object marshalling - anything please!!!
 */
public class Castor2Axis {
   static public org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(Castor2Axis.class);

   /**
    * 
    */
   public Castor2Axis() {
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
   
   public static ParameterValue[] transformParameters(org.astrogrid.applications.beans.v1.parameters.ParameterValue[] inpars)
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
	if (logger.isTraceEnabled()) {
		logger.trace("convert(org.astrogrid.applications.beans.v1.parameters.ParameterValue) - start");
	}

      ParameterValue result = new ParameterValue();
      result.setName(parameterValue.getName());
      if (parameterValue.getEncoding() != null) {
        result.setEncoding(new NMToken(parameterValue.getEncoding()));
      } else {
          result.setEncoding(new NMToken());
      }
      result.setIndirect(parameterValue.getIndirect());
      result.setValue(parameterValue.getValue());
     
      return result;
   }

   /**
    * @param types
    * @return
    */
   /* not used
   public static ParameterTypes convert(org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes type) {
      ParameterTypes result = null;
      if (type != null) {
         result = ParameterTypes.fromValue(new QName(type.toString()));
      }
      return result;
   }
   */



   /**
    * @param definitions
    * @return
    */
   /* not used 
   public static org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition[] convert(BaseParameterDefinition[] definitions) {
      org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition[] result = new org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition[definitions.length];
      for (int i = 0; i < result.length; i++) {
         result[i] = convert(definitions[i]);
      }
      return result;
   }
   */

   /**
    * @param definition
    * @return
    */
   /* not used
   public static org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition convert(BaseParameterDefinition definition) {
      org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition result = new org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition();
      String nothing="";
      String val;
      result.setName(definition.getName());
      
      result.setDefaultValue(definition.getDefaultValue());
      result.setType(convert(definition.getType()));
      result.setSubType(definition.getSubType());
      String[] encodings = definition.getAcceptEncodings();
      StringBuffer buff = new StringBuffer();
      for (int i = 0; i < encodings.length; i++) {
          buff.append(encodings[i]);
          buff.append(" ");
      }
      result.setAcceptEncodings(new NMTokens(buff.toString()));
      result.setUCD(definition.getUCD());
      result.setUI_Description(convert(definition.getUI_Description()));
      result.setUI_Name(definition.getUI_Name());
      result.setUnits(definition.getUnits());
      
      return result;
   }
*/
   /**
    * convert between the documentation elements. This is very messy just to get between two "any" elements!
    * @param documentation
    * @return
    * @TODO FIXME this really needs a better implementation - it does not work - do not really understand how castor/axis deal with any - the schema has been converted to a type derived from string for now to make things easier...
    * 
    */
   /* not used
   public static org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation convert(XhtmlDocumentation documentation) {
//      MessageElement[] _an = new MessageElement[1];
//      StringWriter out = new StringWriter();
//      MessageElement el;
//      try {
//         documentation.marshal(out);
//         StringReader in = new StringReader(out.toString());
//         Document doc = XMLUtils.newDocument(new InputSource(in));
//         
//          el = new MessageElement(doc.getDocumentElement());
//        
//      }
//      catch (Exception e) {
//         
//         logger.error("problem with the Xdocumentation element conversion", e);
//         el = new MessageElement();
//       }
//       _an[0] = el;
      org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation result = new org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation();
      if (documentation != null) {
         result.setValue(documentation.getContent());
      }
      return result;
   }

*/


   /**
    * @param mess
    * @return
    */
   public static org.astrogrid.jes.types.v1.cea.axis.MessageType convert(MessageType mess) {
      org.astrogrid.jes.types.v1.cea.axis.MessageType result = new org.astrogrid.jes.types.v1.cea.axis.MessageType();
      result.setContent(mess.getContent());
      result.setLevel(convert(mess.getLevel()));
      result.setPhase(convert(mess.getPhase()));
      result.setSource(mess.getSource());
      Calendar cal = Calendar.getInstance();
      cal.setTime(mess.getTimestamp());
      result.setTimestamp(cal);
      return result;
   }
   
  public static ExecutionPhase convert(org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase ep)
  {
     ExecutionPhase result = ExecutionPhase.fromValue(ep.toString());
     return result;
  }


  public static org.astrogrid.jes.types.v1.cea.axis.LogLevel convert(org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel ll)
  {
     return org.astrogrid.jes.types.v1.cea.axis.LogLevel.fromString(ll.toString());
  }
  
  public static ExecutionSummaryType convert(org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType castor) {
      ExecutionSummaryType axis = new ExecutionSummaryType();
      axis.setApplicationName(castor.getApplicationName());
      axis.setExecutionId(castor.getExecutionId());
      axis.setResultList(convert(castor.getResultList()));
      axis.setInputList(new InputListType());
      axis.getInputList().setInput(convert(castor.getInputList().getInput()));
      axis.setStatus(convert(castor.getStatus()));
      return axis;
  }
  
  public static ResultListType convert(org.astrogrid.applications.beans.v1.cea.castor.ResultListType castor) {
      ResultListType axis = new ResultListType();
      axis.setResult(convert(castor.getResult()));
      return axis;
  }
  
  public static ParameterValue[] convert(org.astrogrid.applications.beans.v1.parameters.ParameterValue[] castor) {
      ParameterValue[] axis = new ParameterValue[castor.length];
      for (int i = 0; i < axis.length; i++) {
          axis[i] = convert(castor[i]);
      }
      return axis;
  }
  
 
}
