/*
 * @(#)Flow.java   1.0
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
import org.astrogrid.portal.workflow.design.activity.*;
import org.w3c.dom.*;

/**
 * The <code>Flow</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 21-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class Flow extends ActivityContainer {

  /** Compile-time switch used to turn tracing on/off. 
    * Set this to false to eliminate all trace statements within the byte code.*/
  private static final boolean TRACE_ENABLED = true;

  private static Logger logger = Logger.getLogger(Flow.class);

  public Flow(Activity parent) {
    super(parent);
    if (TRACE_ENABLED)
      trace("Flow() entry/exit");
  }

  public Flow(String communitySnippet, Element element, Activity parent) {
    super(communitySnippet, element, parent);
    if (TRACE_ENABLED)
      trace("Flow(Element) entry/exit");
  }

  public String toXMLString() {
    if (TRACE_ENABLED)
      trace("Flow.toXMLString() entry");

    String response = null;

    try {

      Object[] inserts = new Object[1];
      inserts[0] = super.toXMLString();

      response = MessageFormat.format(WorkflowDD.FLOW_TEMPLATE, inserts);

    } finally {
      if (TRACE_ENABLED)
        trace("Flow.toXMLString() exit");
    }

    return response;

  } // end of toXMLString()

  private static void trace(String traceString) {
    System.out.println(traceString);
    // logger.debug( traceString ) ;
  }

  private static void debug(String logString) {
    System.out.println(logString);
    // logger.debug( logString ) ;
  }

} // end of class Flow
