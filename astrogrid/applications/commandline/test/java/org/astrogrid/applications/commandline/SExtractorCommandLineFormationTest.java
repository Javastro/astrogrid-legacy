/*
 * $Id: SExtractorCommandLineFormationTest.java,v 1.4 2008/09/04 21:20:02 pah Exp $
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

import static org.junit.Assert.*;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.commandline.sextractor.SExtractor;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;
import org.junit.Test;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class SExtractorCommandLineFormationTest extends AbstractCmdLineRealAppTestCase {

    
  
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
        ListOfParameterValues input = t.getInput();
       
         ListOfParameterValues output = t.getOutput();
      

        String myspaceBaseRef="file:///nout/";
        
        ParameterValue param = new ParameterValue();
        input.addParameter(param);
        param.setId("DetectionImage");

        param.setIndirect(false);
        String hemi="s";
      String sector="23";
      param.setValue(		
           "file:///detect");

        param = new ParameterValue();
        input.addParameter(param);
        param.setId("PhotoImage");

        param.setIndirect(false);
        String band="Z";	
      param.setValue(
           "file:///photo");

        param = new ParameterValue();
        input.addParameter(param);
        param.setId("config_file");

        param.setIndirect(false);
        param.setValue(
           "file:///config");

        param = new ParameterValue();
        input.addParameter(param);
        param.setId("PARAMETERS_NAME");

        param.setIndirect(false);
        param.setValue("file:///param");

        param = new ParameterValue();
        output.addParameter(param);
        param.setId("CATALOG_NAME");

        param.setIndirect(false);
        param.setValue(myspaceBaseRef + "catalog");
        
        param = new ParameterValue();
        input.addParameter(param);
        param.setId("FILTER_NAME");
        param.setValue("file:///filter");
        param.setIndirect(false);


      

        return t;

    
    }
    
    @Test
    public void testCreateCustomApplication() throws Exception{
        
	Application app = testAppDescr.initializeApplication("foo",secGuard ,buildTool("1"));
        assertNotNull(app);
        assertTrue("was expecting app to be SExtractor, but actually is "+ app.getClass().getCanonicalName(),app instanceof SExtractor);
        
    }


    @Override
    protected TestAppInfo setupApplication() {
	return new TestAppInfo(){

            public String getAppName() {
               return "ivo://org.astrogrid/SExtractor";
            }

	    public String getInterfaceName() {
		return "simple";
	    }
            
        };
    }

}
