/*$Id: CeaTest.java,v 1.1 2004/03/22 17:23:06 mch Exp $
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

/** Tests the CEA web interface at the Vm07 datacenter
 *
 * @author mch
 */

public class CeaTest  {

   public static final String VM07_PAL = "http://vm07.astrogrid.org:8080/pal/services/CeaDataService";
   public static final String GRENDEL_PAL = "http://grendel12.roe.ac.uk:8080/pal-SNAPSHOT/services/CeaDataService";
   
   public void testVm07() throws Exception {
      
      //make up parameters
      
      //make up call

      //make call
      
      //check results
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
Revision 1.1  2004/03/22 17:23:06  mch
moved from old package name

Revision 1.1  2004/03/02 01:20:59  mch
Added Integration test to try datacenter as an Application


*/

