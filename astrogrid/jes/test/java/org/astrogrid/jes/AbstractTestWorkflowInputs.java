/*$Id: AbstractTestWorkflowInputs.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 13-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * Abstract base class for unit testing - lists all workflow input files. 
 * <p />
 * Extend the abstract method to perform test against all inputs. New inputs should be added to this class.
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Feb-2004
 *
 */
public abstract class AbstractTestWorkflowInputs extends TestCase{
    /** Construct a new WorkflowInputs
     * 
     */
    public AbstractTestWorkflowInputs(String arg) {
        super(arg);
    }

    public void testBuildJob1() throws Exception {
        testIt(loadResource("/workflow1.xml"),0);
        
    }

    public void testBuildJob2() throws Exception {
        testIt(loadResource("/workflow2.xml"),1);
        
    }

    public void testBuildJob3() throws Exception {
        testIt(loadResource("/workflow3.xml"),2);
        
    }

    public void testBuildJob4() throws Exception {
        testIt(loadResource("/workflow4.xml"),3);
        
    }

    public void testBuildJob5() throws Exception {
        testIt(loadResource("/workflow5.xml"),4);
        
    }

    public void testBuildJob6() throws Exception {
        testIt(loadResource("/workflow6.xml"),5);
        
    }

    protected InputStream loadResource(String resource) throws IOException {
        InputStream is = this.getClass().getResourceAsStream(resource);
        assertNotNull("Could not load resource " + resource,is);
        return is;
    }
    
    /** define this to do a test for each resource in turn */
    protected abstract  void testIt(InputStream is,int resourceNum) throws Exception;
}


/* 
$Log: AbstractTestWorkflowInputs.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.1  2004/02/17 12:57:11  nw
improved documentation
 
*/