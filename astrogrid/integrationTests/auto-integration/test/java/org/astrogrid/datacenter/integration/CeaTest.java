/*$Id: CeaTest.java,v 1.3 2004/05/13 12:17:07 mch Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.integration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.community.User;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.io.Piper;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.delegate.StoreAdminClient;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreException;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

/** Tests the CEA web interface at the Vm07 datacenter
 *
 * @author mch
 */

public class CeaTest  extends TestCase {

   public static final String STD_PAL = "http://localhost:8080/astrogrid-pal-SNAPSHOT/services/CeaDataService";
   public static final String IVO_MYSPACE = "ivo://org.astrogrid.localhost/myspace";
   
   public static final String QUERY_PATH = "CeaTest/query.adql";
   public static final String RESULTS_PATH = "CeaTest/ceaTestResults.vot";

   public static final String RESULTS_LOC = IVO_MYSPACE+"#"+RESULTS_PATH;
   public static final String QUERY_LOC = IVO_MYSPACE+"#"+QUERY_PATH;
   
   //creates frog user, uploads query and makes sure results location is clear
   public void prepareMySpace() throws IOException {

      try {
         StoreAdminClient admin = StoreDelegateFactory.createAdminDelegate(User.ANONYMOUS, new Agsl("myspace:http://localhost:8080/astrogrid-mySpace-SNAPSHOT/services/Manager"));
         admin.createUser(new User("CeaTest@Unknown", "org.astrogrid.localhost", ""));
      }
      catch (StoreException se) {
         se.printStackTrace(); //but otherwise ignore - user mihgt already exist
      }
      
      //upload query
      StoreClient client = StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl("myspace:http://localhost:8080/astrogrid-mySpace-SNAPSHOT/services/Manager"));
      OutputStream out = client.putStream(QUERY_PATH, false);
      InputStream in = this.getClass().getResourceAsStream("Test-sample-adql-0.5.xml");
      assertNotNull(in);
      Piper.pipe(in, out);
      out.close();

      //make sure results file is clear
      try {
         client.delete(RESULTS_PATH);
      }
      catch (StoreException se) {
         se.printStackTrace(); //but otherwise ignore - might not exist
      }
      
   
   }
   
   public void testStdPal() throws Exception {

      prepareMySpace();
      
      //create 'delegate'
      Tool tool = new Tool();
      tool.setName("Std Datacenter");
      
      //make up parameters
      Input inputs = new Input();

      ParameterValue param = new ParameterValue();
      param.setName("Query");
      param.setType(ParameterTypes.ADQL);
//      param.setValue(getSampleQuery());
      param.setValue(QUERY_LOC);
      inputs.addParameter(param);
      
      param = new ParameterValue();
      param.setName("Format");
      param.setType(ParameterTypes.STRING);
      param.setValue(QuerySearcher.VOTABLE);
      inputs.addParameter(param);

      // output
      Output outputs = new Output();

      param = new ParameterValue();
      param.setName("Target");
      param.setType(ParameterTypes.STRING);
      param.setValue(RESULTS_LOC);
      outputs.addParameter(param);
      
      tool.setInput(inputs);
      tool.setOutput(outputs);
      
      //set up delegate
      CommonExecutionConnectorClient delegate = DelegateFactory.createDelegate(STD_PAL);
      
      //do call
      JobIdentifierType jobId = new JobIdentifierType("CeaTest");
      delegate.execute(tool, jobId, "");
      
      //check results
      VoSpaceClient vospace = new VoSpaceClient(User.ANONYMOUS);
      StoreFile resultsFile = vospace.getFile(new Ivorn(RESULTS_LOC));
      
      assertNotNull(resultsFile);
      
      
   }
   
   public String getSampleQuery() throws IOException {
            InputStream is = this.getClass().getResourceAsStream("SimpleStarQuery-adql-0.5.xml");
            assertNotNull(is);
            StringWriter out = new StringWriter();
            Piper.pipe(new InputStreamReader(is),out);
            return out.toString();
   }
   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(CeaTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       
       junit.textui.TestRunner.run(suite());
    }

}


/*
$Log: CeaTest.java,v $
Revision 1.3  2004/05/13 12:17:07  mch
Fixed createUser

Revision 1.2  2004/05/12 09:17:51  mch
Various fixes - forgotten whatfors...

Revision 1.1  2004/04/16 15:17:14  mch
Copied in from integration tests

Revision 1.2  2004/03/22 18:07:00  mch
Added parameter construction

Revision 1.1  2004/03/22 17:23:06  mch
moved from old package name

Revision 1.1  2004/03/02 01:20:59  mch
Added Integration test to try datacenter as an Application


*/

