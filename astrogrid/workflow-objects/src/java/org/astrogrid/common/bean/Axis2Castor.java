/*
 * $Id: Axis2Castor.java,v 1.12 2005/11/04 17:31:05 clq2 Exp $
 *
 * Created on 18-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.common.bean;

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.Parameters;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.InputListType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.axis._Account;

import java.util.Calendar;

import org.astrogrid.jes.beans.v1.axis.executionrecord.JobURN;
import org.astrogrid.jes.beans.v1.axis.executionrecord.WorkflowSummaryType;
import org.astrogrid.jes.beans.v1.axis.executionrecord._extension;
import org.astrogrid.jes.types.v1.cea.axis.LogLevel;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.axis._input;
import org.astrogrid.workflow.beans.v1.axis._output;
import org.astrogrid.workflow.beans.v1.execution.Extension;

import org.apache.axis.types.NMToken;

import sun.misc.Regexp;

/**
 * Class of static methods to convert axis beans to castor beans. Copied from the util package in the jes project and put in
 common.
 * @author Noel Winstanley
 * @author Paul Harrison (pah@jb.man.ac.uk) 18-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class Axis2Castor {

    /**
     * @param message
     * @return
     */
    public static org.astrogrid.applications.beans.v1.cea.castor.MessageType[] convert(MessageType[] message) {
        if (message == null) {
            return null;
        }
        org.astrogrid.applications.beans.v1.cea.castor.MessageType[] castor = new
        org.astrogrid.applications.beans.v1.cea.castor.MessageType[message.length];
        for (int i = 0; i < message.length; i++) {
            castor[i] = convert(message[i]);
        }
        return castor;
    }


    /** convert between castor and axis representations of the same schema object */
    public static org.astrogrid.applications.beans.v1.cea.castor.MessageType convert(MessageType mt) {
        if (mt == null) {
            return null;
        }
        org.astrogrid.applications.beans.v1.cea.castor.MessageType result = new org.astrogrid.applications.beans.v1.cea.castor.
        MessageType();
        result.setContent(mt.getContent());
        result.setPhase(Axis2Castor.convert(mt.getPhase()));
        result.setLevel(Axis2Castor.convert(mt.getLevel()));
        result.setSource(mt.getSource());
        Calendar cal =  mt.getTimestamp();
        if (cal != null) {
            result.setTimestamp(cal.getTime());
        }
        return result;
    }

    /** convert between castor and axis representations of the same schema object */
    public static Account convert(_Account arg0) {
        Account result = new Account();
        result.setCommunity(arg0.getCommunity().getValue());
        result.setName(arg0.getName().getValue());
        return result;
    }

    /** convert between castor and axis representations of the same schema object */
    public static org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase convert(org.astrogrid.jes.types.v1.cea.axis.
    ExecutionPhase phase) {
        if (phase == null) {
            return null;
        } else {
            return org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase.valueOf(phase.getValue());
        }
    }

    /**
     * @param extension
     * @return
     */
    public static Extension[] convert(_extension[] extension) {
        if (extension == null) {
            return null;
        }
        Extension[] castor = new Extension[extension.length];
        for (int i = 0; i < extension.length; i++) {
            castor[i] = convert(extension[i]);
        }
        return castor;
    }


    public static Extension convert(_extension extension) {
        Extension castor = new Extension();
        castor.setContent(extension.getValue());
        castor.setKey(extension.getKey());
        return castor;
    }



    public static Tool convert(org.astrogrid.workflow.beans.v1.axis._tool tool)
    {
       Tool result = new Tool();
       result.setInterface(tool.get_interface());
       result.setName(tool.getName());
       result.setInput(convert(tool.getInput()));
       result.setOutput(convert(tool.getOutput()));
       return result;
    }

   //--type convertors/////////////////////////////////////////////////////////////////////////////////////////////////////////

       /**
    * @param _output
    * @return
    */
   public static Output convert(_output _output) {
      Output result = new Output();
      if ( _output != null) {
          org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] params = _output.getParameter();
          if (params != null) { // bit more code armour
              for (int i = 0; i < params.length; i++) {
                  result.addParameter(convert(params[i]));
              }
          }
      }
      return result;
   }

   /**
       * @param value
       * @return
       */
      public static ParameterValue convert(org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue value) {
         ParameterValue result = new ParameterValue();
         result.setName(value.getName());
         if (value.getEncoding() != null) {
           result.setEncoding(value.getEncoding().toString());
         }
         result.setIndirect(value.isIndirect());
         result.setValue(value.getValue());
         return result;
      }

   /**
    * @param _input
    * @return
    */
   public static Input convert(_input _input) {
      Input result = new Input();
      if (_input != null) {
          org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] params = _input.getParameter();
          if (params != null) {
              for (int i = 0; i < params.length; i++) {
                  result.addParameter(convert(params[i]));
              }
          }
      }
      return result;
   }

   /** convert between castor and axis representations of the same schema object */
       public static org.astrogrid.workflow.beans.v1.execution.JobURN convert(JobURN jobURN) {
           if (jobURN == null ) {
               return null;
           }
           org.astrogrid.workflow.beans.v1.execution.JobURN result = new org.astrogrid.workflow.beans.v1.execution.JobURN();
           result.setContent(jobURN.toString());
           return result;
       }

    /** convert between castor and axis representations of the same schema object */
    public static org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel convert(LogLevel level) {
        if (level == null) {
            return null;
        } else {
            return org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel.valueOf(level.getValue());
        }
    }

    /** rsultListType */
    public static ResultListType convert(org.astrogrid.jes.types.v1.cea.axis.ResultListType axis) {
        if (axis == null) {
            return null;
        } else {
            ResultListType castor = new ResultListType();
            org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] axisRs = axis.getResult();
            ParameterValue[] castorRs = convert(axisRs);
            castor.setResult(castorRs);
            return castor;

        }
    }

    public static ParameterValue[] convert(
        org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] axisRs) {

        final int axisRsLength = (axisRs==null ? 0 : axisRs.length);
        ParameterValue[] castorRs = new ParameterValue[axisRsLength];
    for (int i = 0; i < axisRsLength; i++){
            castorRs[i] = convert(axisRs[i]);
        }
        return castorRs;
    }



    /** execution summary type */
    public static ExecutionSummaryType convert(org.astrogrid.jes.types.v1.cea.axis.ExecutionSummaryType axis) {
        if (axis == null) {
            return null;
        } else {
            ExecutionSummaryType castor = new ExecutionSummaryType();
            castor.setApplicationName(axis.getApplicationName());
            castor.setExecutionId(axis.getExecutionId());
            org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] axisArr = axis.getInputList().getInput();
            castor.setInputList(new InputListType());
            castor.getInputList().setInput(convert(axisArr));
            castor.setResultList(convert(axis.getResultList()));
            castor.setStatus(convert(axis.getStatus()));
            return castor;
        }
    }


    /** convert a workflow summary between libraries.
     * @param type
     * @return
     */
    public static org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType convert(WorkflowSummaryType type) {
        if (type == null) {
            return null;
        }
        org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType castor = new org.astrogrid.workflow.beans.v1.execution.
        WorkflowSummaryType();
        if (type.getDescription() != null) {
            castor.setDescription(type.getDescription());
        }
        if (type.getExtension() != null) {
            castor.setExtension(convert(type.getExtension()));
        }
        castor.setFinishTime(type.getFinishTime() == null ? null : type.getFinishTime().getTime());
        if (type.getMessage() != null) {
            castor.setMessage(convert(type.getMessage()));
        }
        castor.setJobId(Axis2Castor.convert(type.getJobId()));
        castor.setStartTime(type.getStartTime() == null ? null : type.getStartTime().getTime());
        if (type.getStatus() != null) {
        castor.setStatus(Axis2Castor.convert(type.getStatus()));
        }
        castor.setWorkflowName(type.getWorkflowName());
        return castor;
    }

}

