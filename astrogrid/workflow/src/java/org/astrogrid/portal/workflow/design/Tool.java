/*
 * @(#)Tool.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
//import java.util.Collections ;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import java.text.MessageFormat;

/**
 * The <code>Tool</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 20-Nov-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.4
 */
public class Tool {

  /** Compile-time switch used to turn tracing on/off. 
    * Set this to false to eliminate all trace statements within the byte code.*/
  private static final boolean TRACE_ENABLED = true;

  private static Logger logger = Logger.getLogger(Tool.class);

  private static List createParameters(Element element) {
    if (TRACE_ENABLED)
      trace("Tool.createParameters() entry");

    List list = new ArrayList();

    try {

      NodeList nodeList = element.getChildNodes();

      for (int i = 0; i < nodeList.getLength(); i++) {

        if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

          element = (Element) nodeList.item(i);

          if (element.getTagName().equals(WorkflowDD.PAREMETER_ELEMENT)) {
            list.add(new Parameter(element));
          }

        } // end if

      } // end for       

    } finally {
      if (TRACE_ENABLED)
        trace("Tool.createParameters() exit");
    }

    return list;

  } // end of Tool.createParameters()  

  private String name, documentation;

  private List inputParameters = new ArrayList(),
    outputParameters = new ArrayList();

  private Tool() {
  }

  protected Tool(String name) {
    this.name = name;
  }

  protected Tool(Element element) {
    if (TRACE_ENABLED)
      trace("Tool( Element ) entry");

    try {

      name = element.getAttribute(WorkflowDD.TOOL_NAME_ATTR);

      NodeList nodeList = element.getChildNodes();

      for (int i = 0; i < nodeList.getLength(); i++) {

        if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

          element = (Element) nodeList.item(i);

          if (element.getTagName().equals(WorkflowDD.INPUT_ELEMENT)) {
            this.inputParameters = Tool.createParameters(element);
          } else if (element.getTagName().equals(WorkflowDD.OUTPUT_ELEMENT)) {
            this.outputParameters = Tool.createParameters(element);
          } else if (
            element.getTagName().equals(WorkflowDD.DOCUMENTATION_ELEMENT)) {
            this.documentation = element.getNodeValue();
          }

        } // end if

      } // end for        

    } finally {
      if (TRACE_ENABLED)
        trace("Tool(Element) exit");
    }

  }

  /**
    */
  public String getName() {
    return this.name;
  }

  public String getDocumentation() {
    return this.documentation;
  }

  public ListIterator getInputParameters() {
    if (TRACE_ENABLED)
      trace("Tool.getInputParameters() entry");
    return this.inputParameters.listIterator();
  }

  public ListIterator getOutputParameters() {
    if (TRACE_ENABLED)
      trace("Tool.getOutputParameters() entry");
    return this.outputParameters.listIterator();
  }

  public Parameter newInputParameter(String name) {
    if (TRACE_ENABLED)
      trace("Tool.newInputParameter() entry");

    Parameter p = new Parameter(name);
    this.inputParameters.add(p);
    return p;

  }

  public Parameter newInputParameter(Parameter param) {
    if (TRACE_ENABLED)
      trace("Tool.newInputParameter() entry");

    Parameter p = this.newInputParameter(param.getName());
    p.setContents(param.getContents());
    p.setDocumentation(param.getDocumentation());
    p.setLocation(param.getLocation());
    p.setType(param.getType());
    p.setCardinality(param.getCardinality());

    return p;
  }

  public Parameter newOutputParameter(String name) {
    if (TRACE_ENABLED)
      trace("Tool.newInputParameter() entry");

    Parameter p = new Parameter(name);
    this.outputParameters.add(p);
    return p;
  }

  public Parameter newOutputParameter(Parameter param) {
    if (TRACE_ENABLED)
      trace("Tool.newOutputParameter() entry");

    Parameter p = this.newOutputParameter(param.getName());
    p.setContents(param.getContents());
    p.setDocumentation(param.getDocumentation());
    p.setLocation(param.getLocation());
    p.setType(param.getType());
    p.setCardinality(param.getCardinality());

    return p;
  }

  protected String toXMLString() {
    if (TRACE_ENABLED)
      trace("Tool.toXMLString() entry");

    String response = null, inputParams, outputParams;
    StringBuffer buffer = null;

    try {

      buffer = new StringBuffer(128);
      ListIterator it = inputParameters.listIterator();
      while (it.hasNext()) {
        buffer.append(((Parameter) it.next()).toXMLString());
      }
      inputParams = buffer.toString();
      buffer = new StringBuffer(128);
      it = outputParameters.listIterator();
      while (it.hasNext()) {
        buffer.append(((Parameter) it.next()).toXMLString());
      }
      outputParams = buffer.toString();

      Object[] inserts = new Object[4];
      inserts[0] = this.getName();
      inserts[1] = this.getDocumentation();
      inserts[2] = inputParams;
      inserts[3] = outputParams;

      response = MessageFormat.format(WorkflowDD.TOOL_TEMPLATE, inserts);

    } finally {
      if (TRACE_ENABLED)
        trace("Tool.toXMLString() exit");
    }

    return response;

  }

  protected String toJESXMLString() {
    if (TRACE_ENABLED)
      trace("Tool.toJESXMLString() entry");

    String response = null, inputParams, outputParams;
    StringBuffer buffer = null;

    try {

      buffer = new StringBuffer(128);
      ListIterator it = inputParameters.listIterator();
      while (it.hasNext()) {
        buffer.append(((Parameter) it.next()).toJESXMLString());
      }
      inputParams = buffer.toString();
      buffer = new StringBuffer(128);
      it = outputParameters.listIterator();
      while (it.hasNext()) {
        buffer.append(((Parameter) it.next()).toJESXMLString());
      }
      outputParams = buffer.toString();

      Object[] inserts = new Object[3];
      inserts[0] = this.getName();
      inserts[1] = inputParams;
      inserts[2] = outputParams;

      response = MessageFormat.format(WorkflowDD.JOBTOOL_TEMPLATE, inserts);

    } finally {
      if (TRACE_ENABLED)
        trace("Tool.toJESXMLString() exit");
    }

    return response;

  }

  private static void trace(String traceString) {
    System.out.println(traceString);
    // logger.debug( traceString ) ;
  }

  private static void debug(String logString) {
    System.out.println(logString);
    // logger.debug( logString ) ;
  }

  /**
     */
  public void setDocumentation(String string) {
    documentation = string;
  }

  /**
     */
  public void setInputParameters(List list) {
    inputParameters = list;
  }

  /**
     */
  public void setName(String string) {
    name = string;
  }

  /**
     */
  public void setOutputParameters(List list) {
    outputParameters = list;
  }

} // end of class Tool
