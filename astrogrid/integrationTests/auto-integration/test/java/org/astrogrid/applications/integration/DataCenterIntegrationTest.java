/*$Id: DataCenterIntegrationTest.java,v 1.2 2004/04/26 12:16:07 nw Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.io.Piper;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.scripting.Service;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/** Test  CEA functionality of datacenter.
 * <p>
 * extends ApplicationsIntegrationTest - as there's a lot of commonality, as its the same interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class DataCenterIntegrationTest extends ApplicationsInstallationTest {
    /**
     * Constructor for DataCenterIntegrationTest.
     * @param arg0
     */
    public DataCenterIntegrationTest(String arg0) {
        super(arg0);
    }
    /**
     * @see org.astrogrid.workflow.integration.ApplicationsIntegrationTest#findRequiredService(java.util.Iterator)
     */
    protected Service findRequiredService(Iterator apps) {
        while( apps.hasNext() ) { // find the correct one
            Service s = (Service)apps.next();
            if (s.getDescription().indexOf("datacenter") != -1) {
                return s;
            }           
        }
        fail("failed to find command-line service");
        // never reached.
        return null;        
    }
    /**
     * @see org.astrogrid.workflow.integration.ApplicationsInstallationTest#applicationName()
     */
    protected String applicationName() {
        return TESTDSA;
    }


    protected void populateTool(Tool tool) throws Exception {
          ParameterValue query= (ParameterValue)tool.findXPathValue("input/parameter[name='Query']");
            assertNotNull(query);
            InputStream is = this.getClass().getResourceAsStream("DataCenterIntegrationTest-sample-query.xml");
            assertNotNull(is);
            StringWriter out = new StringWriter();
            Piper.pipe(new InputStreamReader(is),out); 
            query.setValue(out.toString());       
        ParameterValue format= (ParameterValue)tool.findXPathValue("input/parameter[name='Format']");
          assertNotNull(format);
           assertEquals("format should be preset","VOTABLE", format.getValue());            
            ParameterValue target = (ParameterValue)tool.findXPathValue("output/parameter[name='Target']");
            assertNotNull(target);
           Ivorn targetIvorn = new Ivorn(MYSPACE,user.getUserId() + "/DatacenterIntegrationTest-votable.xml");
          // Agsl targetAgsl = new Agsl("myspace:http://localhost:8080/astrogrid-mySpace-SNAPSHOT/services/Manager","frog/DatacenterIntegrationTest.votable");
            target.setValue(targetIvorn.toString());
            System.out.println(targetIvorn.toString());
    }    



}


/* 
$Log: DataCenterIntegrationTest.java,v $
Revision 1.2  2004/04/26 12:16:07  nw
got applications int test working.
dsa works, but suspect its failing under the hood.

Revision 1.1  2004/04/21 13:41:34  nw
set up applications integration tests

Revision 1.8  2004/04/21 10:44:05  nw
tidied to check applicatrions are resolvable.

Revision 1.7  2004/04/20 14:47:41  nw
changed to use an agsl for now

Revision 1.6  2004/04/19 09:35:24  nw
added constants for ivorns of services.
added test query

Revision 1.5  2004/04/15 23:11:20  nw
tweaks

Revision 1.4  2004/04/15 12:18:25  nw
updating tests

Revision 1.3  2004/04/15 10:28:40  nw
improving testing

Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/