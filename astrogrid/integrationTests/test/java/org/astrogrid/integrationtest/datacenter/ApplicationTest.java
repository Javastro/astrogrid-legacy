/*$Id: ApplicationTest.java,v 1.1 2004/03/04 19:06:04 jdt Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.integrationtest.datacenter;

import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.applications.delegate.ApplicationController;
import org.astrogrid.applications.delegate.beans.ParameterValues;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.integrationtest.store.MySpaceTest;
import org.astrogrid.store.delegate.StoreClientTest;

/** Tests the ApplicationController interface to a datacenter
 *
 * @author mch
 */

public class ApplicationTest extends StoreClientTest {

   public static final String VM07_PAL = "http://vm07.astrogrid.org:8080/pal/services/AxisDataServer";
   
   public void testVm07() throws Exception {
      
         //connect up to the 'dummy' deployed datacenter
         ApplicationController tool = (ApplicationController) DatacenterDelegateFactory.makeFullSearcher(Account.ANONYMOUS, VM07_PAL, DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);

         //set up the parameters
         ParameterValues parameters =  new ParameterValues();
         parameters.setParameterSpec(
            "<tool><input>"+
              "<parameter name='QueryMySpaceReference' type='MySpaceRef'>"+
                  "vospace://http.vm05.astrogrid.org:8080/astrogrid-mySpace/services/MySpaceManager#avodemo@test.astrogrid.org/serv1/query/AVOQuery.xml"+
              "</parameter>"+
              "<parameter name='ResultsMySpaceReference' type='MySpaceRef'>"+
                  "vospace://http.vm05.astrogrid.org:8080/astrogrid-mySpace/services/MySpaceManager#avodemo@test.astrogrid.org/serv1/votable/IntegrationTestResults.vot"+
              "</parameter>"+
            "</input></tool>");
         
         String localId = "MadeUpLocalId:"+new Date().getSeconds();

         //initialise
         String runId = tool.initializeApplication("Datacenter", localId, null, null, parameters);

         //and GO!
         tool.executeApplication(runId);
        
   }
   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(ApplicationTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       path = "avodemo@test.astrogrid.org/serv1/mch/";
       
       junit.textui.TestRunner.run(suite());
    }

}


/*
$Log: ApplicationTest.java,v $
Revision 1.1  2004/03/04 19:06:04  jdt
Package name changed to lower case to satisfy coding standards.  mea culpa - didn't read the Book.  Tx Martin.

Revision 1.1  2004/03/02 01:20:59  mch
Added Integration test to try datacenter as an Application

Revision 1.1  2004/03/01 22:35:09  mch
Tests for StoreClient

Revision 1.4  2004/03/01 16:51:10  mch
Removed test for equality between local filespaces

Revision 1.3  2004/01/23 15:22:27  jdt
added logging

Revision 1.2  2003/12/03 17:39:25  mch
New factory/interface based store delegates

Revision 1.1  2003/09/22 17:36:09  mch
tests for storeDumnmyDelegate

*/

