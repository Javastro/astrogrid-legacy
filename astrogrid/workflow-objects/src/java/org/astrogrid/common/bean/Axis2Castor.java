/*
 * $Id: Axis2Castor.java,v 1.6 2004/04/30 17:57:42 pah Exp $
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
import org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase;
import org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase_Parameters;
import org.astrogrid.applications.beans.v1.axis.ceabase.Interface_input;
import org.astrogrid.applications.beans.v1.axis.ceabase.Interface_output;
import org.astrogrid.applications.beans.v1.axis.ceabase.InterfacesType;
import org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef;
import org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList;
import org.astrogrid.applications.beans.v1.axis.ceabase._interface;
import org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.axis._Account;

import java.util.Calendar;

import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.LogLevel;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.axis._input;
import org.astrogrid.workflow.beans.v1.axis._output;

import sun.misc.Regexp;

/**
 * Class of static methods to convert axis beans to castor beans. Copied from the util package in the jes project and put in common.
 * @author Noel Winstanley
 * @author Paul Harrison (pah@jb.man.ac.uk) 18-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class Axis2Castor {

    /** convert between castor and axis representations of the same schema object */
    public static org.astrogrid.applications.beans.v1.cea.castor.MessageType convert(MessageType mt) {
        if (mt == null) {
            return null;
        }
        org.astrogrid.applications.beans.v1.cea.castor.MessageType result = new org.astrogrid.applications.beans.v1.cea.castor.MessageType();
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
    public static org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase convert(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase phase) {
        if (phase == null) {
            return null;
        } else {
            return org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase.valueOf(phase.getValue());
        }
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
      org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] params = _output.getParameter();
      for (int i = 0; i < params.length; i++) {
         result.addParameter(convert(params[i]));
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
         result.setType(convert(value.getType()));
         result.setValue(value.getValue());
         return result;
      }

      /**
    * @param types
    * @return
    */
   public static ParameterTypes convert(org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterTypes types) {
      ParameterTypes result = ParameterTypes.valueOf(types.toString());
      return result;
   }

   /**
    * @param _input
    * @return
    */
   public static Input convert(_input _input) {
      Input result = new Input();
      org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] params = _input.getParameter();
      for (int i = 0; i < params.length; i++) {
         result.addParameter(convert(params[i]));
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
    
    public static ApplicationList convert (_ApplicationList in)
    {
      ApplicationList result = new ApplicationList();
      ApplicationBase[] apps = in.getApplicationDefn();
      for (int i = 0; i < apps.length; i++) {
         result.addApplicationDefn(convert(apps[i]));
      }
      return result;
       
    }
    
    public static org.astrogrid.applications.beans.v1.ApplicationBase convert(ApplicationBase in)
    {
       
       org.astrogrid.applications.beans.v1.ApplicationBase result = new org.astrogrid.applications.beans.v1.ApplicationBase();
       result.setName(in.getName().toString());
       InterfacesType ininterfaces = in.getInterfaces();
       ApplicationBase_Parameters inparams = in.getParameters();
       result.setInterfaces(convert(ininterfaces));
       result.setParameters(convert(inparams));
       return result;
    }

   /**
    * @param inparams
    * @return
    */
   private static Parameters convert(ApplicationBase_Parameters inparams) {
      Parameters retval = new Parameters();
      BaseParameterDefinition[] inp = inparams.getParameter();
      for (int i = 0; i < inp.length; i++) {
         retval.addParameter(convert(inp[i]));
      }
      
      return retval;
   }
   
   private static org.astrogrid.applications.beans.v1.InterfacesType convert(InterfacesType interf)
   {
      org.astrogrid.applications.beans.v1.InterfacesType retval = new org.astrogrid.applications.beans.v1.InterfacesType();
      _interface[] in = interf.get_interface();
      for (int i = 0; i < in.length; i++) {
         retval.add_interface(convert(in[i]));
      }
      return retval;
   }
   
   /**
    * @param _interface
    * @return
    */
   private static Interface convert(_interface _interface) {
      Interface result = new Interface();
      result.setName(_interface.getName());
      result.setInput(convert(_interface.getInput()));
      result.setOutput(convert(_interface.getOutput()));
      
      return result;
   }

   /**
    * @param interface_output
    * @return
    */
   private static org.astrogrid.applications.beans.v1.Output convert(Interface_output interface_output) {
      org.astrogrid.applications.beans.v1.Output result = new org.astrogrid.applications.beans.v1.Output();
     ParameterRef[] refs = interface_output.getPref();
     for (int i = 0; i < refs.length; i++) {
      result.addPref(convert(refs[i]));
   }
     return result;
   }

   /**
    * @param ref
    * @return
    */
   private static org.astrogrid.applications.beans.v1.ParameterRef convert(ParameterRef ref) {
      org.astrogrid.applications.beans.v1.ParameterRef result = new org.astrogrid.applications.beans.v1.ParameterRef();
      result.setMaxoccurs(ref.getMaxoccurs());
      result.setMinoccurs(ref.getMinoccurs());
      result.setRef(ref.getRef());
      return result;
   }

   /**
    * @param interface_input
    * @return
    */
   private static org.astrogrid.applications.beans.v1.Input convert(Interface_input interface_input) {
      org.astrogrid.applications.beans.v1.Input result = new org.astrogrid.applications.beans.v1.Input();
     ParameterRef[] refs = interface_input.getPref();
     for (int i = 0; i < refs.length; i++) {
      result.addPref(convert(refs[i]));
   }
     return result;
   }

   private static org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition convert(BaseParameterDefinition inpar)
   {
      org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition  result=  new org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition();
      result.setDefaultValue(inpar.getDefaultValue());
      result.setName(inpar.getName());
      result.setType(convert(inpar.getType()));
      result.setUCD(inpar.getUCD());
      
      result.setUI_Description(convert(inpar.getUI_Description()));
      result.setUI_Name(inpar.getUI_Name());
      result.setUnits(inpar.getUnits());
      return result;
   }

   /**
    * @param documentation
    * @return
    */
   private static XhtmlDocumentation convert(org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation documentation) {
      XhtmlDocumentation result = new XhtmlDocumentation();
      result.setContent(documentation.getValue());
      return result;
   }
}
