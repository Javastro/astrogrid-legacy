/*$Id: VOSpaceIndirectExecutionTest.java,v 1.6 2005/03/14 22:03:53 clq2 Exp $
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
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/** Test execution of application, using indirect parameters that point into vospace..
 * @see org.astrogrid.applications.integration.AbstractRunTestForCEA
 * @see org.astrogrid.applications.integration.JavaProviderServerInfo
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
        client = (new FileManagerClientFactory()).login();
        FileManagerNode node = client.node(new Ivorn(filePath));
        
        BufferedReader br = new BufferedReader(new InputStreamReader(node.readContent()));
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
       client = (new FileManagerClientFactory()).login();
       assertNotNull(client);
       FileManagerNode node;
       if (client.exists(inputIvorn) == null) {
           node = client.createFile(inputIvorn);
       } else {
           node = client.node(inputIvorn);
       }
       PrintWriter pw  = new PrintWriter(new OutputStreamWriter( node.writeContent()));
       pw.print(40);
       pw.close();

    }
    private Ivorn inputIvorn;
    private Ivorn targetIvorn;
    private FileManagerClient client;
}


/* 
$Log: VOSpaceIndirectExecutionTest.java,v $
Revision 1.6  2005/03/14 22:03:53  clq2
auto-integration-nww-994

Revision 1.5.34.1  2005/03/11 17:17:17  nw
changed bunch of tests to use FileManagerClient instead of VoSpaceClient

Revision 1.5  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.2.92.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.2  2004/07/23 08:44:30  nw
fix to parameter population

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/