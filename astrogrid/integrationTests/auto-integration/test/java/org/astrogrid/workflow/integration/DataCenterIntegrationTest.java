/*$Id: DataCenterIntegrationTest.java,v 1.5 2004/04/15 23:11:20 nw Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.scripting.Service;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

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

    /**Override this, as querying a data center requires different params.
     * @see org.astrogrid.workflow.integration.ApplicationsIntegrationTest#testExecute()
     */

    public void testExecute() throws Exception {
        ApplicationRegistry reg = ag.getWorkflowManager().getToolRegistry();
        ApplicationDescription descr = reg.getDescriptionFor("org.astrogrid.localhost/testdsa");
        assertNotNull("could not get application description for testdsa",descr);
        Tool tool = descr.createToolFromDefaultInterface();
        assertNotNull("tool is null",tool);
        /* todo - fill in parameters*/
        ParameterValue query= (ParameterValue)tool.findXPathValue("input/parameter[name='Query']");
        assertNotNull(query);
        /* @todo add in a query document here */

        descr.validate(tool); 
        
        ParameterValue target = (ParameterValue)tool.findXPathValue("output/parameter[name='Target']");
        assertNotNull(target);
        Ivorn targetIvorn = new Ivorn("org.astrogrid.localhost/myspace","/" + user.getUserId() + "/test/DatacenterIntegrationTest.votable.xml");
        target.setValue(targetIvorn.toString());
        
        JobIdentifierType id = new JobIdentifierType(); // not too bothered about this.
        id.setValue(this.getClass().getName());
       String returnEndpoint ="http://localhost:8080/astrogrid-jes-SNAPSHOT/services/JobMonitorService";      
      String execId = delegate.execute(tool,id,returnEndpoint);
      assertNotNull(execId);
      
    }    

}


/* 
$Log: DataCenterIntegrationTest.java,v $
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