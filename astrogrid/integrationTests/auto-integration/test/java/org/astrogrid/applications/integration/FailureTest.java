/*
 * $Id: FailureTest.java,v 1.1 2004/09/20 15:01:38 pah Exp $
 * 
 * Created on 10-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.integration;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.integration.commandline.CommandLineProviderServerInfo;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * Tests whether known faults are passed back. 
 * @author Paul Harrison (pah@jb.man.ac.uk) 10-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class FailureTest extends AbstractTestForCEA {

   private Tool tool;
private JobIdentifierType jobid;
private static final CommandLineProviderServerInfo serverInfo = new CommandLineProviderServerInfo();
public FailureTest(String arg0)
   {
   
       this(serverInfo.getServerSearchString(), arg0);
   }
  
    /**
     * @param searchString
     * @param arg0
     */
    public FailureTest(String searchString, String arg0) {
        super(searchString, arg0);
        
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        ApplicationRegistry reg = ag.getWorkflowManager().getToolRegistry();
        org.astrogrid.portal.workflow.intf.ApplicationDescription descr = reg.getDescriptionFor(serverInfo.getApplicationName());
        assertNotNull("could not get application description",descr);
        tool = descr.createToolFromDefaultInterface();
        assertNotNull("tool is null",tool);

        serverInfo.populateDirectTool(tool);
        jobid = new JobIdentifierType();
        jobid.setValue("blah");
        
    }
    public void testUnknownToolFailure()
    {
        tool.setName("unknown");
        try {
            String exid = delegate.init(tool, jobid);
            fail("a ApplicationDescriptionNotFoundException: unknown should have occured");
        }
        catch (CEADelegateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void testbadParameter()
    {
        org.astrogrid.applications.beans.v1.parameters.ParameterValue silly = new ParameterValue();
        silly.setName("silly");
        silly.setValue("daft");
        tool.getInput().addParameter(silly);
        try {
            String exid = delegate.init(tool, jobid);
            delegate.execute(exid);
            fail("a ParameterDescriptionNotFoundException: silly should have occured");
        }
        catch (CEADelegateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
