/*
 * $Id: Castor2Axis.java,v 1.4 2004/03/26 12:07:34 pah Exp $
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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Calendar;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.message.MessageElement;
import org.apache.axis.types.Id;
import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.Parameters;
import org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase_Parameters;
import org.astrogrid.applications.beans.v1.axis.ceabase.Interface_input;
import org.astrogrid.applications.beans.v1.axis.ceabase.Interface_output;
import org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList;
import org.astrogrid.applications.beans.v1.axis.ceabase._interface;
import org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterTypes;
import org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase;
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
   public static ParameterTypes convert(org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes type) {
      ParameterTypes result = null;
      if (type != null) {
         result = ParameterTypes.fromValue(new QName(type.toString()));
      }
      return result;
   }

   /**
    * @param outlist
    * @return
    */
   public static _ApplicationList convert(ApplicationList outlist) {
      _ApplicationList result = new _ApplicationList();
      ApplicationBase[] apps = outlist.getApplicationDefn();
      result.setApplicationDefn(convert(apps));
      
      
      return result;
   }

   /**
    * @param apps
    * @return
    */
   public static org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase[] convert(ApplicationBase[] apps) {
      //REFACTORME - better to use collections and convert to array at end
      org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase[] result = new org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase[apps.length];
      for (int i = 0; i < result.length; i++) {
         result[i]= convert(apps[i]);
      }
 
      return result;
   }

   /**
    * @param base
    * @return
    */
   public static org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase convert(ApplicationBase base) {
      Id id = new Id(base.getName());
      org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase result = new org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase();
      
      result.setName(id);
      result.setInstanceClass(base.getInstanceClass());
      result.setInterfaces(convert(base.getInterfaces()));
      result.setParameters(convert(base.getParameters()));
      return result;
   }

   /**
    * @param parameters
    * @return
    */
   public static ApplicationBase_Parameters convert(Parameters parameters) {
      ApplicationBase_Parameters result = new ApplicationBase_Parameters();
      result.setParameter(convert(parameters.getParameter()));
      return result;
   }

   /**
    * @param definitions
    * @return
    */
   public static org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition[] convert(BaseParameterDefinition[] definitions) {
      org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition[] result = new org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition[definitions.length];
      for (int i = 0; i < result.length; i++) {
         result[i] = convert(definitions[i]);
      }
      return result;
   }

   /**
    * @param definition
    * @return
    */
   public static org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition convert(BaseParameterDefinition definition) {
      org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition result = new org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition();
      result.setName(definition.getName());
      result.setDefaultValue(definition.getDefaultValue());
      result.setType(convert(definition.getType()));
      result.setUCD(definition.getUCD());
      result.setUI_Description(convert(definition.getUI_Description()));
      result.setUI_Name(definition.getUI_Name());
      result.setUnits(definition.getUnits());
      
      return result;
   }

   /**
    * convert between the documentation elements. This is very messy just to get between two "any" elements!
    * @param documentation
    * @return
    * @TODO - this really needs a better implementation.
    */
   public static org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation convert(XhtmlDocumentation documentation) {
      MessageElement[] _an = new MessageElement[1];
      StringWriter out = new StringWriter();
      MessageElement el;
      try {
         documentation.marshal(out);
         StringReader in = new StringReader(out.toString());
         Document doc = XMLUtils.newDocument(new InputSource(in));
         
          el = new MessageElement(doc.getDocumentElement());
        
      }
      catch (Exception e) {
         
         logger.error("problem with the Xdocumentation element conversion", e);
         el = new MessageElement();
       }
       _an[1] = el;
      org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation result = new org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation();
      result.set_any(_an);
      return result;
   }

   /**
    * @param type
    * @return
    */
   public static org.astrogrid.applications.beans.v1.axis.ceabase.InterfacesType convert(InterfacesType type) {
      org.astrogrid.applications.beans.v1.axis.ceabase.InterfacesType result = new org.astrogrid.applications.beans.v1.axis.ceabase.InterfacesType();
      result.set_interface(convert(type.get_interface()));
      
      return result;
   }

   /**
    * @param interfaces
    * @return
    */
   public static _interface[] convert(Interface[] interfaces) {
      _interface[] result = new _interface[interfaces.length];
      for (int i = 0; i < result.length; i++) {
         result[i] = convert(interfaces[i]);
      }
      return result;
   }

   /**
    * @param interface1
    * @return
    */
   public static _interface convert(Interface interface1) {
      _interface result = new _interface();
      result.setName(interface1.getName());
      result.setInput(convert(interface1.getInput()));
      result.setOutput(convert(interface1.getOutput()));
      return result;
   }

   /**
    * @param input
    * @return
    */
   public static Interface_input convert(org.astrogrid.applications.beans.v1.Input input) {
      Interface_input result = new Interface_input();
      result.setPref(convert(input.getPref()));
  
      return result;
   }

   /**
    * @param output
    * @return
    */
   public static Interface_output convert(org.astrogrid.applications.beans.v1.Output output) {
      Interface_output result = new Interface_output();
      result.setPref(convert(output.getPref()));
  
      return result;
   }

   /**
    * @param refs
    * @return
    */
   public static org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef[] convert(ParameterRef[] refs) {
      org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef[] result = new org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef[refs.length];
      for (int i = 0; i < result.length; i++) {
         result[i] = convert(refs[i]);
      }
      return result;
   }

   /**
    * @param ref
    * @return
    */
   public static org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef convert(ParameterRef ref) {
      org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef result = new org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef();
      result.setMaxoccurs(ref.getMaxoccurs());
      result.setMinoccurs(ref.getMinoccurs());
      result.setRef(ref.getRef());
      return result;
   }

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
  
 
}
