/*$Id: DataCenterIntegrationTest.java,v 1.3 2004/04/15 10:28:40 nw Exp $
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

import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.scripting.Service;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import java.util.Iterator;

/** Test integration between workflow and datacenter.
 * <p>
 * extends ApplicationsIntegrationTest - as there's a lot of commonality, as its the same interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class DataCenterIntegrationTest extends ApplicationsIntegrationTest {
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
        Input input = new Input();
        Output output = new Output();
        Tool tool = new Tool();
        tool.setName("datacenter");
        tool.setInterface("not used");
        tool.setInput(input);
        tool.setOutput(output);
        JobIdentifierType id = new JobIdentifierType(); // not too bothered about this.
        id.setValue(this.getClass().getName());
       String returnEndpoint ="http://www.dont.care.for.now";      
      String execId = delegate.execute(tool,id,returnEndpoint);
    }

}


/* 
$Log: DataCenterIntegrationTest.java,v $
Revision 1.3  2004/04/15 10:28:40  nw
improving testing

Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/