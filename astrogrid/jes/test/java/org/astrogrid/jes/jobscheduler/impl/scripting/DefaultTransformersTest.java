/*$Id: DefaultTransformersTest.java,v 1.1 2004/07/09 09:32:12 nw Exp $
 * Created on 11-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;

import junit.framework.TestCase;

/** Simplest test - just checks object can be created correctly (which means it can load all its resources, etc).
 * @author Noel Winstanley nw@jb.man.ac.uk 11-May-2004
 *
 */
public class DefaultTransformersTest extends TestCase {
    /**
     * Constructor for DefaultTransformersTest.
     * @param arg0
     */
    public DefaultTransformersTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        trans = new DefaultTransformers();
    }
    protected DefaultTransformers trans;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testCompiler() throws Exception{
        assertNotNull(trans.getCompiler());
    }
    
    public void testTranslator()  throws Exception{
        assertNotNull(trans.getTranslator());
    }
    
    public void testAnnotator() throws Exception {
        assertNotNull(trans.getWorkflowAnnotator());
    }
}


/* 
$Log: DefaultTransformersTest.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:42  nw
first checkin of prototype scrpting workflow interpreter
 
*/