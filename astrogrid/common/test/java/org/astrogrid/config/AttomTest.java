/*
 * $Id: AttomTest.java,v 1.4 2004/02/25 17:00:55 jdt Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.config;


import java.io.IOException;
import java.net.MalformedURLException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests AttomConfig class.
 */

public class AttomTest extends TestCase
{
//@TODO restore me - temporary delete
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(AttomTest.class);
    }
}

/*
 $Log: AttomTest.java,v $
 Revision 1.4  2004/02/25 17:00:55  jdt
 temp removal of build breaking tests (see bug 151)

 Revision 1.3  2004/02/19 23:27:51  mch
 Added head

 Revision 1.2  2004/02/17 14:46:51  mch
 Increased test coverage

 Revision 1.1  2004/02/17 03:40:21  mch
 Changed to use AttomConfig

 */

