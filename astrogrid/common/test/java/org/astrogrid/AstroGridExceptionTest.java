/* $Id: AstroGridExceptionTest.java,v 1.1 2004/01/19 17:57:05 jdt Exp $
 * Created on 19-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid;

import org.astrogrid.i18n.AstroGridMessage;

import junit.framework.TestCase;

/**
 * @author jdt
 *
 * A few quick tests to get the clover numbers up
 */
public final class AstroGridExceptionTest extends TestCase {

  /**
   * Constructor for AstrogridExceptionTest.
   * @param arg0 test name
   */
  public AstroGridExceptionTest(final String arg0) {
    super(arg0);
  }

  /** 
   * fire up the text ui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(AstroGridExceptionTest.class);
  }
  
  /**
   *  what else can you do?
   */
  public void testConstructors() {
    AstroGridMessage message = new AstroGridMessage("message");
    AstroGridException ae1 = new AstroGridException(message);
    assertEquals(ae1.getAstroGridMessage(), message);
    Exception foo = new Exception();
    AstroGridException ae2 = new AstroGridException(foo);
    assertEquals(ae2.getCause(), foo);
    AstroGridException ae3 = new AstroGridException(message,foo);
    assertEquals(ae3.getAstroGridMessage(), message);
    assertEquals(ae3.getCause(), foo);    
  }

}

/*
*$Log: AstroGridExceptionTest.java,v $
*Revision 1.1  2004/01/19 17:57:05  jdt
*I think I must be losing my marbles.
*
*/