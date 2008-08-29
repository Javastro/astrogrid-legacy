/*
 * $Id: DftCommandLineFormationTest.java,v 1.1 2008/08/29 07:28:27 pah Exp $
 * 
 * Created on 23-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 


package org.astrogrid.applications.commandline;

import static org.junit.Assert.assertNotNull;

import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class DftCommandLineFormationTest extends AbstractCmdLineRealAppTestCase {

    /* (non-Javadoc)
     * @see org.astrogrid.applications.commandline.AbstractCmdLineAppTestCase#buildTool()
     * 
     */
    protected Tool buildTool(String blah) throws Exception {
 
        ApplicationInterface interf = testAppDescr.getInterface("simple");
        assertNotNull(interf);
        // from this 'meta data' populat a tool..
        Tool t = new Tool();
        t.setId(TESTAPPNAME);
        t.setInterface(interf.getId());
        
        ListOfParameterValues input = new ListOfParameterValues();
	t.setInput(input );
        
        ListOfParameterValues output = new ListOfParameterValues();
	t.setOutput(output );
        ParameterValue param = new ParameterValue();
        input.addParameter(param);
        param.setId("targets");

        
        String myspaceBaseRef="file:///tmp/";
        
        param.setValue(myspaceBaseRef + "sexout_z");
        param.setIndirect(false);

        param = new ParameterValue();
        param.setId("matches");
        input.addParameter(param);
        param.setValue(myspaceBaseRef + "sexout_b");
        param.setIndirect(false);

        param = new ParameterValue();
        input.addParameter(param);
        param.setId("matches");

        param.setIndirect(false);
        param.setValue(myspaceBaseRef + "sexout_v");

        param = new ParameterValue();
        input.addParameter(param);
        param.setId("matches");

        param.setIndirect(false);
        param.setValue(myspaceBaseRef + "sexout_i");

        param = new ParameterValue();
        param.setId("merged_output");
        output.addParameter(param);
        param.setIndirect(true);
        param.setValue(myspaceBaseRef + "merged");
       

        return t;

    
    }
    @Override
    protected TestAppInfo setupApplication() {
	return new TestAppInfo() {

            public String getAppName() {
               return "ivo://org.astrogrid/CrossMatcher";
            }

	    public String getInterfaceName() {
		return "simple";
	    }            
        };
    }

}
