/*
 * $Id: BooleanNormalizerTest.java,v 1.1 2008/08/29 07:28:27 pah Exp $
 * 
 * Created on 28 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BooleanNormalizerTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testNormalize() {
	assertEquals("false", BooleanNormalizer.normalize("f", "true"));
	assertEquals("false", BooleanNormalizer.normalize("f", "false"));
    }
    @Test
    public void testNormalize2() {
	assertEquals("false", BooleanNormalizer.normalize("false", "true"));
    }
    
    @Test
    public void testNormalize3() {
	assertEquals("false", BooleanNormalizer.normalize("0", "true"));
    }
    
    @Test
    public void testNormalize4() {
	assertEquals("silly", BooleanNormalizer.normalize("silly", "true"));
    }
    @Test
    public void testNormalize5() {
	assertEquals("0", BooleanNormalizer.normalize("false", "1"));
    }
     
    @Test
    public void testNormalize6() {
	assertFalse("absurd value should not be recognised", BooleanNormalizer.isRecognised("aill"));
    }

}


/*
 * $Log: BooleanNormalizerTest.java,v $
 * Revision 1.1  2008/08/29 07:28:27  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/03/28 16:44:35  pah
 * RESOLVED - bug 2683: treatment of boolean values
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2683
 *
 */
