/*$Id: DataCenterVOSpaceIndirectExecutionTest.java,v 1.6 2005/03/14 22:03:53 clq2 Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.datacenter;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.AbstractRunTestForCEA;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;


/** test datacenter cea against indirect parameters that reference resources in myspace.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 * 
 * @see org.astrogrid.applications.integration.AbstractRunTestForCEA
 * @see org.astrogrid.applications.integration.datacenter.DataCenterProviderServerInfo
 *
 */
public class DataCenterVOSpaceIndirectExecutionTest extends AbstractRunTestForCEA{
    
    /** Construct a new DataCenterVOSpaceIndirectExecutionTest
     * @param info
     * @param arg0
     */
    public DataCenterVOSpaceIndirectExecutionTest( String arg0) {
        super(new DataCenterProviderServerInfo(), arg0);
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
           InputStream is = node.readContent();
           assertNotNull(is);
           AstrogridAssert.assertVotable(is);                
       }
       /**
        * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#populateTool(org.astrogrid.workflow.beans.v1.Tool)
        */
       protected void populateTool(Tool tool) throws Exception {
           serverInfo.populateIndirectTool(tool,inputIvorn.toString(),targetIvorn.toString());
       }
    
       protected void setUp() throws Exception {
          super.setUp();
          targetIvorn = createIVORN("/DataCenterVOSpaceIndirectExecutionTest-output");
          inputIvorn = createIVORN("/DataCenterVOSpaceIndirectExecutionTest-input");

          // write to myspace...
          client = (new FileManagerClientFactory()).login();
          assertNotNull(client);
          FileManagerNode node;
          if (client.exists(inputIvorn) == null) {
              node = client.createFile(inputIvorn);
          } else {
              node = client.node(inputIvorn);
          }
          OutputStream os  =  node.writeContent();
          Piper.pipe(this.getClass().getResourceAsStream(DataCenterProviderServerInfo.SAMPLE_QUERY_RESOURCE),os);
          os.close();

       }
       private Ivorn inputIvorn;
       private Ivorn targetIvorn;
       private  FileManagerClient  client;
}


/* 
$Log: DataCenterVOSpaceIndirectExecutionTest.java,v $
Revision 1.6  2005/03/14 22:03:53  clq2
auto-integration-nww-994

Revision 1.5.34.1  2005/03/11 17:17:17  nw
changed bunch of tests to use FileManagerClient instead of VoSpaceClient

Revision 1.5  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.2.52.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.2  2004/08/27 13:16:52  nw
used AstrogridAssert to check results more thoroughly.

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/