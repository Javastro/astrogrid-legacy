/* $Id: TrivialTest.java,v 1.2 2003/10/20 15:02:56 jdt Exp $ 
 * Created on 20/10/03 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes;

import junit.framework.TestCase;

/**
 * @author jdt
 *
 * Trivial test simply to force CVS to create the test directory structure so I 
 * can run Maven.
 */
public class TrivialTest extends TestCase {

  /**
   * Constructor for TrivialTest.
   * @param arg0
   */
  public TrivialTest(String arg0) {
    super(arg0);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(TrivialTest.class);
  }
  
  /**
   * Does nothing.
   *
   */
  public void testTrivial() {
  }
  

}
/*
 * $log$
 */
