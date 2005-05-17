/*
 * $Id: CommandLineVOSpaceIndirectExecutionTest.java,v 1.2 2005/05/17 23:07:54 jdt Exp $
 * 
 * Created on 11-May-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.integration.commandlinewindows;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.AbstractRunTestForCEA;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URISyntaxException;

/**
 * test for cea server, fetching parameters from vospace
 * 
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-May-2004
 * @version $Name:  $
 * @since iteration5
 * 
 * @see org.astrogrid.applications.integration.AbstractRunTestForCEA
 * @see org.astrogrid.applications.integration.commandline.CommandLineProviderServerInfo
 */
public class CommandLineVOSpaceIndirectExecutionTest extends AbstractRunTestForCEA {

    private Ivorn inputIvorn;

    private Ivorn targetIvorn;

    private FileManagerClient client;


    /**
     * Constructor for ApplicationRunTest.
     * 
     * @param arg0
     */
    public CommandLineVOSpaceIndirectExecutionTest(String arg0) {
        super(new CommandLineProviderServerInfo(), arg0);
    }

    protected void populateTool(Tool tool) throws Exception {
        serverInfo.populateIndirectTool(tool, inputIvorn.toString(), targetIvorn.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        targetIvorn = createIVORN("/ApplicationRunWithVOSpaceTest-output");
        inputIvorn = createIVORN("/ApplicationRunWithVOSpaceTest-input");

        // write to myspace...
        client = (new FileManagerClientFactory()).login();
        assertNotNull(client);

        /*
         * try { //TODO this should not really be here/needed - the user should
         * have been set up properly, but sometimes it is not.....
         * client.createUser(homeSpaceIvorn, userIvorn); } catch (Exception e) { //
         * ignore }
         */

        FileManagerNode node;
        if (client.exists(inputIvorn) == null) {
            node = client.createFile(inputIvorn);
        } else {
            node = client.node(inputIvorn);
        }
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(node.writeContent()));
        pw.println(CommandLineProviderServerInfo.TEST_INPUT);
        pw.close();
    }

    /**
     * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#checkResults(org.astrogrid.applications.beans.v1.cea.castor.ResultListType)
     */
    protected void checkResults(ResultListType results) throws Exception {
        assertNotNull(results);
        softAssertEquals("there should be 1 result", 1, results.getResultCount());
        ParameterValue result = (ParameterValue) results.findXPathValue("result[name='output']");
        assertNotNull(result);
        softAssertTrue(result.getIndirect());
        String filePath = result.getValue();
        softAssertEquals(filePath, targetIvorn.toString());
        client = (new FileManagerClientFactory()).login();
        FileManagerNode node = client.node(new Ivorn(filePath));
        Reader in = new InputStreamReader(node.readContent());
        assertNotNull(in);
        StringWriter out = new StringWriter();
        Piper.pipe(in, out);
        out.close();
        assertNotNull(out.toString()); // unlikely.
        assertTrue("expected message not found in execution output\n+" + out.toString(), out.toString().indexOf(
                CommandLineProviderServerInfo.TEST_OUTPUT) != -1);

    }

}
