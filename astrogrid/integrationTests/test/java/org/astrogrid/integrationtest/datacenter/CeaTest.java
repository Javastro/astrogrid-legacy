/*$Id: CeaTest.java,v 1.2 2004/03/22 18:07:00 mch Exp $
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

import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

/** Tests the CEA web interface at the Vm07 datacenter
 *
 * @author mch
 */

public class CeaTest  {

   public static final String STD_PAL = "http://vm07.astrogrid.org:8080/pal/services/CeaDataService";
   public static final String GRENDEL_PAL = "http://grendel12.roe.ac.uk:8080/pal-SNAPSHOT/services/CeaDataService";
   
   public void testStdPal() throws Exception {

      //create 'delegate'
      Tool tool = new Tool();
      tool.setName("Std Datacenter");
      
      //make up parameters
      Input inputs = new Input();

      ParameterValue param = new ParameterValue();
      param.setName("Query");
      param.setType(ParameterTypes.ADQL);
      param.setValue("15");
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
      param.setValue("astrogrid:store:myspace:http://Hello World");
      outputs.addParameter(param);
      
      tool.setInput(inputs);
      tool.setOutput(outputs);
      
      //make up call
      
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
Revision 1.2  2004/03/22 18:07:00  mch
Added parameter construction

Revision 1.1  2004/03/22 17:23:06  mch
moved from old package name

Revision 1.1  2004/03/02 01:20:59  mch
Added Integration test to try datacenter as an Application


*/

