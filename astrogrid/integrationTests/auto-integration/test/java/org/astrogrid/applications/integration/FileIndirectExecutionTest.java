/*$Id: FileIndirectExecutionTest.java,v 1.3 2004/11/19 10:27:29 clq2 Exp $
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
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;

/** Simplest CEA test that checks the fetching of indirect methods. uses the file:/ protocol for convenience, although same should apply to all protocols
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Jun-2004
 * 
 * @see org.astrogrid.applications.integration.AbstractRunTestForCEA
 * @see org.astrogrid.applications.integration.JavaProviderServerInfo
 *
 */
public class FileIndirectExecutionTest extends AbstractRunTestForCEA {
    /** Construct a new FileIndirectExecutionTest
     * @param name
     */
    public FileIndirectExecutionTest(String name) {
        super(new JavaProviderServerInfo(), name);
    }
    /**
     * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#populateTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    protected void populateTool(Tool tool) throws Exception {
        serverInfo.populateIndirectTool(tool, inputFile.toURI().toString(),outputFile.toURI().toString());
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
        softAssertEquals(filePath,outputFile.toURI().toString());
        BufferedReader br = new BufferedReader(new FileReader(new File(new URI(filePath))));
        assertNotNull(br);
        String value = br.readLine();
        assertNotNull(value);
        assertEquals(42, Integer.parseInt(value));           
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        inputFile = File.createTempFile("FileIndirectExecutionTest-input",null);
        outputFile = File.createTempFile("FileIndirectExecutionTest-output",null);
        inputFile.createNewFile();
        outputFile.createNewFile();
        inputFile.deleteOnExit();
        outputFile.deleteOnExit();
        PrintWriter pw = new PrintWriter(new FileWriter(inputFile));
        pw.print(40);
        pw.close();        
    }
    
    protected File inputFile;
    protected File outputFile;

}


/* 
$Log: FileIndirectExecutionTest.java,v $
Revision 1.3  2004/11/19 10:27:29  clq2
nww-itn07-659

Revision 1.2.92.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.2  2004/07/23 08:44:30  nw
fix to parameter population

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/