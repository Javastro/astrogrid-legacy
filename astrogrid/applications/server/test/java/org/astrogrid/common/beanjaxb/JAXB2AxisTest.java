/*
 * $Id: JAXB2AxisTest.java,v 1.1 2009/07/15 13:37:07 pah Exp $
 * 
 * Created on 15 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.common.beanjaxb;

import static org.junit.Assert.*;

import net.ivoa.uws.ExecutionPhase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JAXB2AxisTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testConvertExecutionPhase() {
       org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase p1 = JAXB2Axis.convert(ExecutionPhase.ABORTED);
       assertEquals(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.ERROR, p1);
       p1 = JAXB2Axis.convert(ExecutionPhase.COMPLETED);
       p1 =JAXB2Axis.convert(ExecutionPhase.ERROR);
       p1 =JAXB2Axis.convert(ExecutionPhase.EXECUTING);
       p1 =JAXB2Axis.convert(ExecutionPhase.HELD);
       assertEquals(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.PENDING, p1);
      p1 =JAXB2Axis.convert(ExecutionPhase.PENDING);
       p1 =JAXB2Axis.convert(ExecutionPhase.QUEUED);
       assertEquals(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.PENDING, p1);
       p1 =JAXB2Axis.convert(ExecutionPhase.SUSPENDED);
       assertEquals(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.PENDING, p1);
       p1 =JAXB2Axis.convert(ExecutionPhase.UNKNOWN);
    }

}


/*
 * $Log: JAXB2AxisTest.java,v $
 * Revision 1.1  2009/07/15 13:37:07  pah
 * ASSIGNED - bug 2949: status not reported back properly in CEC interface
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2949
 *
 */
