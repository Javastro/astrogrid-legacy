/*
 * @(#)Cardinality.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import org.apache.log4j.Logger;

/**
 * The <code>Cardinality</code> class represents... 
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
public class Cardinality {

  /** Compile-time switch used to turn tracing on/off. 
    * Set this to false to eliminate all trace statements within the byte code.*/
  private static final boolean TRACE_ENABLED = true;

  private static Logger logger = Logger.getLogger(Cardinality.class);

  public static final int UNLIMITED = -1;

  private int minimum, maximum;

  private Cardinality() {
  }

  public Cardinality(int minimum, int maximum) {
    this.minimum = minimum;
    this.maximum = maximum;
  }

  public Cardinality(String minString, String maxString) {
    if (TRACE_ENABLED)
      trace("Cardinality(String,String) entry");

    Integer min, max;

    try {
      try {
        min = new Integer(minString);
      } catch (NumberFormatException nfe) {
        min = new Integer(1);
      }
      try {
        max = new Integer(maxString);
      } catch (NumberFormatException nfe) {
        max = new Integer(1);
      }
      this.minimum = min.intValue();
      this.maximum = max.intValue();
    } finally {
      if (TRACE_ENABLED)
        trace("Cardinality(String,String) exit");
    }

  }

  /**
    */
  public int getMaximum() {
    return maximum;
  }

  /**
    */
  public int getMinimum() {
    return minimum;
  }

  public boolean isUnlimited() {
    return (maximum == Cardinality.UNLIMITED);
  }

  private static void trace(String traceString) {
    System.out.println(traceString);
    // logger.debug( traceString ) ;
  }

  private static void debug(String logString) {
    System.out.println(logString);
    // logger.debug( logString ) ;
  }

} // end of class Cardinality
