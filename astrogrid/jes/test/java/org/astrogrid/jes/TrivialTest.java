/* $Id: TrivialTest.java,v 1.5 2003/11/12 22:25:23 anoncvs Exp $ 
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
 * Trivial test simply to force CVS to create the test directory structure so I 
 * can run Maven.
 *  
 *  @author jdt
 *  @TODO this is a task in a class javadoc
 */
public class TrivialTest extends TestCase {

  /**
   * Constructor for TrivialTest.
   * @param arg0 Unused
   */
  public TrivialTest(final String arg0) {
    super(arg0); //@TODO this is a task in an ordinary comment
  }

/**
 * Launch the JUnit Text UI
 * @param args Unused
 */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(TrivialTest.class);
  }
  
  /**
   * Does nothing.
   * @TODO this is a task in a method javadoc
   */
  public void testTrivial() {
  }
  

}
/*
 * $Log: TrivialTest.java,v $
 * Revision 1.5  2003/11/12 22:25:23  anoncvs
 * Added some @TODO tags to see what happens when Maven runs tonight.
 *
 * Revision 1.4  2003/10/21 16:36:37  jdt
 * Updated to comply with our coding standards.
 *
 * Revision 1.3  2003/10/20 15:08:30  jdt
 * trivial change
 *
 */

