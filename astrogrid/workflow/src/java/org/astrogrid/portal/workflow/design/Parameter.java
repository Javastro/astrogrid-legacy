/*
 * @(#)Parameter.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <code>Parameter</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 18-Nov-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.4
 */
public class Parameter {

  /** Compile-time switch used to turn tracing on/off. 
    * Set this to false to eliminate all trace statements within the byte code.*/
  private static final boolean TRACE_ENABLED = true;

  private static Logger logger = Logger.getLogger(Parameter.class);

  private String name = null, type = null, documentation = null;

  private String location = null;

  private String contents = null;

  private Cardinality cardinality = null;

  private Parameter() {
  }

  protected Parameter(String name) {
    this.name = name;
  }

  public Parameter(Element element) {
    if (TRACE_ENABLED)
      trace("Parameter(Element) exit");

    try {

      this.name = element.getAttribute(WorkflowDD.PAREMETER_NAME_ATTR);
      this.type = element.getAttribute(WorkflowDD.PAREMETER_TYPE_ATTR);
      this.location = element.getAttribute(WorkflowDD.PAREMETER_LOCATION_ATTR);
      this.cardinality =
        new Cardinality(
          element.getAttribute(WorkflowDD.PARAMETER_MIN_CARDINALITY),
          element.getAttribute(WorkflowDD.PARAMETER_MAX_CARDINALITY));

      this.contents = element.getNodeValue();

      NodeList nodeList = element.getChildNodes();

      for (int i = 0; i < nodeList.getLength(); i++) {

        if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

          element = (Element) nodeList.item(i);

          if (element.getTagName().equals(WorkflowDD.DOCUMENTATION_ELEMENT)) {
            this.documentation = element.getNodeValue();
          }

        } // end if

      } // end for       

    } finally {
      if (TRACE_ENABLED)
        trace("Parameter(Element)() exit");
    }

  } // end of Parameter(Element)

  public String getName() {
    return this.name;
  }

  public String getDocumentation() {
    return this.documentation;
  }

  public String getType() {
    return this.type;
  }

  public Cardinality getCardinality() {
    return this.cardinality;
  }

  public String getContents() {
    return contents;
  }

  public String getLocation() {
    return location;
  }

  public void setContents(String string) {
    contents = string;
  }

  public void setLocation(String url) {
    location = url;
  }

  protected String toXMLString() {
    if (TRACE_ENABLED)
      trace("Parameter.toXMLString() entry");

    String response = null;

    try {

      Object[] inserts = new Object[7];
      inserts[0] = this.getName();
      inserts[1] = this.getType();
      inserts[2] = this.getLocation();
      inserts[3] = new Integer(this.getCardinality().getMinimum()).toString();
      inserts[4] = new Integer(this.getCardinality().getMaximum()).toString();
      ;
      inserts[5] = this.getDocumentation();
      inserts[6] = (this.getContents() == null) ? " " : this.getContents();

      response = MessageFormat.format(WorkflowDD.PARAMETER_TEMPLATE, inserts);

    } finally {
      if (TRACE_ENABLED)
        trace("Parameter.toXMLString() exit");
    }

    return response;
  }

  protected String toJESXMLString() {
    if (TRACE_ENABLED)
      trace("Parameter.toJESXMLString() entry");
    String response = null;

    try {

      Object[] inserts = new Object[4];
      inserts[0] = this.getName();
      inserts[1] = this.getType();
      inserts[2] =
        (this.getLocation() == null)
          ? " "
          : "location=\"" + this.getLocation() + "\"";
      inserts[3] = (this.getContents() == null) ? " " : this.getContents();

      response =
        MessageFormat.format(WorkflowDD.JOBPARAMETER_TEMPLATE, inserts);

    } finally {
      if (TRACE_ENABLED)
        trace("Parameter.toJESXMLString() exit");
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
  public void setCardinality(Cardinality cardinality) {
    this.cardinality = cardinality;
  }

  /**
     */
  public void setDocumentation(String string) {
    documentation = string;
  }

  /**
     */
  public void setType(String string) {
    type = string;
  }

} // end of class Parameter
