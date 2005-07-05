/*
 * $Id: DftCommandLineFormationTest.java,v 1.4 2005/07/05 08:26:56 clq2 Exp $
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

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.commandline.DescriptionBaseTestCase.TestAppInfo;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class DftCommandLineFormationTest extends AbstractCmdLineRealAppTestCase {

    
    public DftCommandLineFormationTest(String arg0)
    {
        super(new TestAppInfo(){

            public String getAppName() {
               return "org.astrogrid/CrossMatcher";
            }

            public String getConfigFileName() {
                return TestAppConst.REALAPP_CONFFILE;
            }
            
        },
                arg0);
    }
    /**
     * @param info
     * @param arg0
     */
    public DftCommandLineFormationTest(TestAppInfo info, String arg0) {
        super(info, arg0);
    }

    /* (non-Javadoc)
     * @see org.astrogrid.applications.commandline.AbstractCmdLineAppTestCase#buildTool()
     * 
     */
    protected Tool buildTool(String blah) throws Exception {
 
        ApplicationInterface interf = testAppDescr.getInterface("simple");
        assertNotNull(interf);
        // from this 'meta data' populat a tool..
        Tool t = new Tool();
        t.setName(TESTAPPNAME);
        t.setInterface(interf.getName());
        Input input = new Input();
        t.setInput(input);
        Output output = new Output();
        t.setOutput(output);
        ParameterValue param = new ParameterValue();
        input.addParameter(param);
        param.setName("targets");

        
        String myspaceBaseRef="file:///tmp/";
        
        param.setValue(myspaceBaseRef + "sexout_z");
        param.setIndirect(false);

        param = new ParameterValue();
        param.setName("matches");
        input.addParameter(param);
        param.setValue(myspaceBaseRef + "sexout_b");
        param.setIndirect(false);

        param = new ParameterValue();
        input.addParameter(param);
        param.setName("matches");

        param.setIndirect(false);
        param.setValue(myspaceBaseRef + "sexout_v");

        param = new ParameterValue();
        input.addParameter(param);
        param.setName("matches");

        param.setIndirect(false);
        param.setValue(myspaceBaseRef + "sexout_i");

        param = new ParameterValue();
        param.setName("merged_output");
        output.addParameter(param);
        param.setIndirect(true);
        param.setValue(myspaceBaseRef + "merged");
       

        return t;

    
    }

}
