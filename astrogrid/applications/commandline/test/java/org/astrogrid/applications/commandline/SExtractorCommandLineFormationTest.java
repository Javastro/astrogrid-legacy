/*
 * $Id: SExtractorCommandLineFormationTest.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
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
public class SExtractorCommandLineFormationTest extends AbstractCmdLineRealAppTestCase {

    
    public SExtractorCommandLineFormationTest(String arg0)
    {
        super(new TestAppInfo(){

            public String getAppName() {
               return "org.astrogrid/SExtractor";
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
    public SExtractorCommandLineFormationTest(TestAppInfo info, String arg0) {
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

        String myspaceBaseRef="file:///nout/";
        
        ParameterValue param = new ParameterValue();
        input.addParameter(param);
        param.setName("DetectionImage");

        param.setIndirect(false);
        String hemi="s";
      String sector="23";
      param.setValue(		
           "file:///detect");

        param = new ParameterValue();
        input.addParameter(param);
        param.setName("PhotoImage");

        param.setIndirect(false);
        String band="Z";	
      param.setValue(
           "file:///photo");

        param = new ParameterValue();
        input.addParameter(param);
        param.setName("config_file");

        param.setIndirect(false);
        param.setValue(
           "file:///config");

        param = new ParameterValue();
        input.addParameter(param);
        param.setName("PARAMETERS_NAME");

        param.setIndirect(false);
        param.setValue("file:///param");

        param = new ParameterValue();
        output.addParameter(param);
        param.setName("CATALOG_NAME");

        param.setIndirect(false);
        param.setValue(myspaceBaseRef + "catalog");
        
        param = new ParameterValue();
        input.addParameter(param);
        param.setName("FILTER_NAME");
        param.setValue("file:///filter");
        param.setIndirect(false);


      

        return t;

    
    }

}
