/*$Id: JesUtilsTest.java,v 1.1 2004/03/05 16:16:55 nw Exp $
 * Created on 04-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.util;

import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Mar-2004
 *
 */
public class JesUtilsTest extends TestCase {
    /**
     * Constructor for JesUtilsTest.
     * @param arg0
     */
    public JesUtilsTest(String arg0) {
        super(arg0);
    }
    
    private static final String JOB_URN = "jes:test:urn:-3434534:4343";
    private static final String XPATH = "/sequence/step[3]";
    /** check we can round-trip from a urn/ path pair, to id, and back again. */
    public void testJobIdentifierToJobURL() {
        JobURN urn = new JobURN();
        urn.setContent(JOB_URN);
        
        JobIdentifierType id = JesUtil.createJobId(urn,XPATH);
        assertNotNull(id);
        
        JobURN urn1 = JesUtil.extractURN(id);
        assertNotNull(urn1);
        assertEquals(urn.getContent(),urn1.getContent());
        
        String xpath = JesUtil.extractXPath(id);
        assertNotNull(xpath);
        assertEquals(XPATH,xpath);
        
    }
}


/* 
$Log: JesUtilsTest.java,v $
Revision 1.1  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/