/*$Id: DirectExecutionTest.java,v 1.1 2004/07/01 11:43:33 nw Exp $
 * Created on 22-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Jun-2004
 *
 */
public class DirectExecutionTest extends AbstractRunTestForCEA{
    /** Construct a new DirectExecutionTest
     * 
     */
    public DirectExecutionTest(String name) {
        super(new JavaProviderServerInfo(), name);
    }

    protected void checkResults(ResultListType results) throws Exception {
        softAssertEquals("more than one result returned",1,results.getResultCount());
        ParameterValue result = results.getResult(0);
        assertNotNull(result);
        String value = result.getValue();
        assertNotNull(value);
        assertEquals(42, Integer.parseInt(value));                        
    }

    protected void populateTool(Tool tool) throws Exception {
        serverInfo.populateDirectTool(tool);
    }
}


/* 
$Log: DirectExecutionTest.java,v $
Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/