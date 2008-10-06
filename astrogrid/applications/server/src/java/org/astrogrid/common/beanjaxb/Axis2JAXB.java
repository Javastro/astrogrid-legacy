/*
 * $Id: Axis2JAXB.java,v 1.4 2008/10/06 12:16:16 pah Exp $
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

package org.astrogrid.common.beanjaxb;

import net.ivoa.uws.ExecutionPhase;

import org.astrogrid.applications.description.execution.BinaryEncodings;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.execution.InputListType;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.ResultListType;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.axis._Account;

import java.util.Calendar;
import org.astrogrid.jes.types.v1.cea.axis.LogLevel;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.joda.time.DateTime;


/**
 * Class of static methods to convert axis beans to JAXB beans. Copied from the util package in the jes project and put in
 common.
 * @author Noel Winstanley
 * @author Paul Harrison (pah@jb.man.ac.uk) 18-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class Axis2JAXB {

    /**
     * @param message
     * @return
     */
    public static org.astrogrid.applications.description.execution.MessageType[] convert(MessageType[] message) {
        if (message == null) {
            return null;
        }
        org.astrogrid.applications.description.execution.MessageType[] castor = new
        org.astrogrid.applications.description.execution.MessageType[message.length];
        for (int i = 0; i < message.length; i++) {
            castor[i] = convert(message[i]);
        }
        return castor;
    }


    /** convert between castor and axis representations of the same schema object */
    public static org.astrogrid.applications.description.execution.MessageType convert(MessageType mt) {
        if (mt == null) {
            return null;
        }
        org.astrogrid.applications.description.execution.MessageType result = 
            new org.astrogrid.applications.description.execution.MessageType();
        result.setContent(mt.getContent());
        result.setPhase(Axis2JAXB.convert(mt.getPhase()));
        result.setLevel(Axis2JAXB.convert(mt.getLevel()));
        result.setSource(mt.getSource());
        Calendar cal =  mt.getTimestamp();
        if (cal != null) {
            result.setTimestamp(new DateTime(cal)); //IMPL this cast is a dodgy assumption?
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
    public static ExecutionPhase convert(org.astrogrid.jes.types.v1.cea.axis.
    ExecutionPhase phase) {
        if (phase == null) {
            return null;
        } else {
            return ExecutionPhase.valueOf(phase.getValue());
        }
    }

 


    public static Tool convert(org.astrogrid.workflow.beans.v1.axis._tool tool)
    {
       Tool result = new Tool();
       result.setInterface(tool.get_interface());
       result.setId(tool.getName());
       result.setInput(convert(tool.getInput().getParameter()));
       result.setOutput(convert(tool.getOutput().getParameter()));
       return result;
    }

 

//--type convertors/////////////////////////////////////////////////////////////////////////////////////////////////////////

 
   /**
       * @param value
       * @return
       */
      public static org.astrogrid.applications.description.execution.ParameterValue convert(org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue value) {
         org.astrogrid.applications.description.execution.ParameterValue result = new ParameterValue();
         result.setId(value.getName());
         if (value.getEncoding() != null) {
           BinaryEncodings encod = BinaryEncodings.valueOf(value.getEncoding().toString());
	result.setEncoding(encod );
         }
         result.setIndirect(value.isIndirect());
         result.setValue(value.getValue());
         return result;
      }

 
 
    /** convert between castor and axis representations of the same schema object */
    public static org.astrogrid.applications.description.execution.LogLevel convert(LogLevel level) {
        if (level == null) {
            return null;
        } else {
            return org.astrogrid.applications.description.execution.LogLevel.valueOf(level.getValue());
        }
    }

    /** rsultListType */
    public static ResultListType convert(org.astrogrid.jes.types.v1.cea.axis.ResultListType axis) {
        if (axis == null) {
            return null;
        } else {
            ResultListType castor = new ResultListType();
            org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] axisRs = axis.getResult();
            org.astrogrid.applications.description.execution.ParameterValue[] castorRs = convert(axisRs);
            castor.setParameters(castorRs);
            return castor;

        }
    }

    public static org.astrogrid.applications.description.execution.ParameterValue[] convert(
        org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] axisRs) {

        final int axisRsLength = (axisRs==null ? 0 : axisRs.length);
        org.astrogrid.applications.description.execution.ParameterValue[] castorRs = new org.astrogrid.applications.description.execution.ParameterValue[axisRsLength];
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
            castor.setJobId(axis.getExecutionId());
            org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] axisArr = axis.getInputList().getInput();
            InputListType inputs = new InputListType();
            inputs.setInputs(convert(axisArr));
	    castor.setInputList(inputs );
            castor.setResultList(convert(axis.getResultList()));
            castor.setPhase(convert(axis.getStatus()));
            return castor;
        }
    }


 
}

