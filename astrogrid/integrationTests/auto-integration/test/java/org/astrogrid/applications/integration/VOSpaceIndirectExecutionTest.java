/*$Id: VOSpaceIndirectExecutionTest.java,v 1.2 2004/07/23 08:44:30 nw Exp $
 * Created on 23-Jun-2004
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
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/** Test execution of application, using indirect parameters and reuslts in vospace..
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Jun-2004
 *
 */
public class VOSpaceIndirectExecutionTest extends AbstractRunTestForCEA {
    /** Construct a new VOSpaceIndirectExecutionTest
     * @param arg0
     */
    public VOSpaceIndirectExecutionTest(String arg0) {
        super(new JavaProviderServerInfo(),arg0);
    }

    /**
     * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#checkResults(org.astrogrid.applications.beans.v1.cea.castor.ResultListType)
     */
    protected void checkResults(ResultListType results) throws Exception {
        assertNotNull(results);
        softAssertEquals("more than one result returned",1,results.getResultCount());
        ParameterValue result = results.getResult(0);
        assertNotNull(result);
        softAssertTrue(result.getIndirect());
        String filePath = result.getValue();
        softAssertEquals(filePath,targetIvorn.toString());
        client = new VoSpaceClient(user);
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getStream( new Ivorn(filePath))));
        assertNotNull(br);
        String value = br.readLine();
        assertNotNull(value);
        assertEquals(42, Integer.parseInt(value));                 
    }
    /**
     * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#populateTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    protected void populateTool(Tool tool) throws Exception {
        serverInfo.populateIndirectTool(tool,inputIvorn.toString(),targetIvorn.toString());
    }
    
    protected void setUp() throws Exception {
       super.setUp();
       targetIvorn = createIVORN("/VOSpaceIndirectExecutionTest-output");
       inputIvorn = createIVORN("/VOSpaceIndirectExecutionTest-input");

       // write to myspace...
       client = new VoSpaceClient(user);
       assertNotNull(client);
       PrintWriter pw  = new PrintWriter(new OutputStreamWriter( client.putStream(inputIvorn)));
       pw.print(40);
       pw.close();

    }
    private Ivorn inputIvorn;
    private Ivorn targetIvorn;
    private VoSpaceClient client;
}


/* 
$Log: VOSpaceIndirectExecutionTest.java,v $
Revision 1.2  2004/07/23 08:44:30  nw
fix to parameter population

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/