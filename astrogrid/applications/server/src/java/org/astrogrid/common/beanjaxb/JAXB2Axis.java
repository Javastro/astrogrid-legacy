/*
 * $Id: JAXB2Axis.java,v 1.3 2009/07/15 13:37:07 pah Exp $
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

package org.astrogrid.common.beanjaxb;

import org.apache.axis.types.NMToken;
import org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue;
import org.astrogrid.applications.description.execution.LogLevel;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionSummaryType;
import org.astrogrid.jes.types.v1.cea.axis.InputListType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;
import org.astrogrid.workflow.beans.v1.axis._input;
import org.astrogrid.workflow.beans.v1.axis._output;
import org.astrogrid.workflow.beans.v1.axis._tool;

/**
 * Static methods to convert JAXB to Axis objects.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 17 Apr 2008
 * @version $Name:  $
 * @since 
 * @TODO - drop this when axis is retired...- anything please!!!
 */
public class JAXB2Axis {
   static public org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(JAXB2Axis.class);

   /**
    * 
    */
   public JAXB2Axis() {
   }

   public static _tool convert(org.astrogrid.applications.description.execution.Tool ctool) {
      _tool result = new _tool();
      result.set_interface(ctool.getInterface());
      result.setName(ctool.getId());
      _input input = new _input();
      input.setParameter(convert(ctool.getInput().getParameter()));
    result.setInput(input );
    _output output = new _output();
    output.setParameter(convert(ctool.getOutput().getParameter()));
      result.setOutput(output);

      return result;
   }
    
   public static ParameterValue[] transformParameters(org.astrogrid.applications.description.execution.ParameterValue[] inpars)
   {
      ParameterValue[] result = new ParameterValue[inpars.length];
      
      for (int i = 0; i < inpars.length; i++) {
         // this is a bit inefficient as we are thowing away the objects created in the new above..
         result[i] = convert(inpars[i]);
      }
      
      return result;
   }
   
   public static ParameterValue convert(org.astrogrid.applications.description.execution.ParameterValue parameterValue)
   {
      ParameterValue result = new ParameterValue();
      result.setName(parameterValue.getId());
      if (parameterValue.getEncoding() != null) {
        NMToken encs= new NMToken(parameterValue.getEncoding().toString());
	result.setEncoding(encs);
      } else {
          result.setEncoding(new NMToken());
      }
      result.setIndirect(parameterValue.isIndirect());
      result.setValue(parameterValue.getValue());
     
      return result;
   }

  

 
 

   /**
    * @param mess
    * @return
    */
   public static org.astrogrid.jes.types.v1.cea.axis.MessageType convert(org.astrogrid.applications.description.execution.MessageType mess) {
      org.astrogrid.jes.types.v1.cea.axis.MessageType result = new org.astrogrid.jes.types.v1.cea.axis.MessageType();
      result.setContent(mess.getContent());
      result.setLevel(convert(mess.getLevel()));
      result.setPhase(convert(mess.getPhase()));
      result.setSource(mess.getSource());
      result.setTimestamp(mess.getTimestamp().toGregorianCalendar());
      return result;
   }
   
   public static org.astrogrid.jes.types.v1.cea.axis.MessageType[] convert(org.astrogrid.applications.description.execution.MessageType[] mess) {
       org.astrogrid.jes.types.v1.cea.axis.MessageType[] axis = new org.astrogrid.jes.types.v1.cea.axis.MessageType[mess.length];
       for (int i = 0; i < mess.length; i++) {
           axis[i] = convert(mess[i]);
       }
       return axis;
   }
   
   
   public static ExecutionPhase convert(net.ivoa.uws.ExecutionPhase ep)
   {
       ExecutionPhase result;
       switch (ep) {
       case EXECUTING:
	   result = ExecutionPhase.RUNNING;
	   break;

       case ABORTED:
           result = ExecutionPhase.ERROR;
           break;
           
       case HELD:
       case QUEUED:
       case SUSPENDED:
           result = ExecutionPhase.PENDING;
           break;
       default:
	   result = ExecutionPhase.fromValue(ep.toString());
       break;
       }

       return result;
   }


  public static org.astrogrid.jes.types.v1.cea.axis.LogLevel convert(LogLevel ll)
  {
     return org.astrogrid.jes.types.v1.cea.axis.LogLevel.fromString(ll.toString().toLowerCase());
  }
  
  public static ExecutionSummaryType convert(org.astrogrid.applications.description.execution.ExecutionSummaryType castor) {
      ExecutionSummaryType axis = new ExecutionSummaryType();
      axis.setApplicationName(castor.getApplicationName());
      axis.setExecutionId(castor.getJobId());
      axis.setResultList(convert(castor.getResultList()));
      axis.setInputList(new InputListType());
      axis.getInputList().setInput(convert(castor.getInputList().getParameter().toArray(new org.astrogrid.applications.description.execution.ParameterValue[]{})));
      axis.setStatus(convert(castor.getPhase()));
      return axis;
  }
  
  public static ResultListType convert(org.astrogrid.applications.description.execution.ResultListType castor) {
      ResultListType axis = new ResultListType();
      axis.setResult(convert(castor.getResult().toArray(new org.astrogrid.applications.description.execution.ParameterValue[]{})));
      return axis;
  }
  
  public static ParameterValue[] convert(org.astrogrid.applications.description.execution.ParameterValue[] castor) {
      ParameterValue[] axis = new ParameterValue[castor.length];
      for (int i = 0; i < axis.length; i++) {
          axis[i] = convert(castor[i]);
      }
      return axis;
  }
  
 
}
