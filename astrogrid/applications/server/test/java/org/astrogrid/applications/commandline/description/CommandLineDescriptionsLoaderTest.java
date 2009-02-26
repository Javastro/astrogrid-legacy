/*
 * $Id: CommandLineDescriptionsLoaderTest.java,v 1.5 2009/02/26 12:47:04 pah Exp $
 * 
 * Created on 26-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.commandline.description;

import static org.junit.Assert.*;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.commandline.AbstractCmdLineAppTestCase;
import org.astrogrid.applications.commandline.CommandLineApplication;
import org.astrogrid.applications.commandline.CommandLineApplicationDescription;
import org.astrogrid.applications.commandline.Toolbuilder;
import org.astrogrid.applications.description.ApplicationDefinition;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.Cardinality;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.OptionList;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotLoadedException;
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.impl.CommandLineParameterDefinition;
import org.astrogrid.applications.description.impl.SwitchTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Tests the description loader. Expects to find a a file
 * TestApplicationConfig.xml in the Classpath
 * 
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
@RunWith(SpringJUnit4ClassRunner.class) 
public class CommandLineDescriptionsLoaderTest extends AbstractCmdLineAppTestCase  {

    protected ApplicationDescriptionLibrary dl;
 
  
    @Override
    public void setUp() throws Exception {
        super.setUp();
        dl =  manager.getApplicationDescriptionLibrary();
        assertNotNull("cannot create the DescriptionLoader", dl);

    }

    @Test
    final public void testLoadDescription()
            throws ApplicationDescriptionNotLoadedException,
            ParameterNotInInterfaceException, ParameterDescriptionNotFoundException {

        try {
            assertEquals("Wrong numbr of apps defined", 14 , dl
                    .getApplicationNames().length);
            System.out.println("Test application is called " + TESTAPPNAME);
            ApplicationDefinition x = dl.getDescription(TESTAPPNAME);
            assertTrue(x instanceof CommandLineApplicationDescription);
            CommandLineApplicationDescription ad = (CommandLineApplicationDescription)x;
            assertEquals("instance class", ad.getInstanceClass(), null);
            assertTrue("the application description not correct", ad.getDescription().equals("testapp is just for testing"));

            assertTrue("execution path", ad.getExecutionPath().endsWith(
                    "testapp.sh")); //this is dependent on the actual location
            ParameterDescription[] params = ad.getParameterDescriptions();
            assertNotNull("no parameters returned", params);
            assertEquals("there should be 14 parameters ", 14, params.length);
            //now look at the parameters in detail
            ParameterDescription p1 = params[0];

            try {
                ParameterDescription p2 = ad.getParameterDescription(params[0]
                        .getId());
                assertSame(
                        "parameters should be same via two different retrieval methods",
                        p1, p2);
                p1 = ad.getParameterDescription("P1"); // get p1 ready for later
            }
            catch (ParameterDescriptionNotFoundException e1) {
                fail("did not find a parameter that it should");
            }
            // try getting a parameter that should not be there
            try {
                ParameterDescription p3 = ad.getParameterDescription("silly");
                fail("getting non existant parameter should throw exception");
            }
            catch (ParameterDescriptionNotFoundException e2) {
                // do nothing this should get here
            }
            assertTrue(p1 instanceof CommandLineParameterDefinition);
            CommandLineParameterDefinition clpd = (CommandLineParameterDefinition)p1;

            // lets look at a some of the properties
            assertEquals("P1 commandposition", 1, clpd.getCommandPosition());
            assertEquals("P1 ui name unexpected", "Pause Time", clpd
                    .getName());
            assertEquals("P1 should not be a fileRef parameter",false, clpd.isFileRef());
            assertTrue("P1 description missing", clpd.getDescription()
                    .length() > 0);
            assertEquals("ucd", "UCD", clpd.getUcd());
            assertEquals("default value", "0", clpd.getDefaultValue().get(0));
            assertEquals("UNITS value", "seconds", clpd.getUnit());
            System.out.println("name:" + p1.getId());
            System.out.println("desc:" + p1.getDescription());
            
            clpd = (CommandLineParameterDefinition)ad.getParameterDescription("P3");
            assertEquals("P3 should be a fileRef parameter",true, clpd.isFileRef());
                       
            ApplicationInterface intf = null;
            try {
                intf = ad.getInterface("I1");
            }
            catch (InterfaceDescriptionNotFoundException e3) {
                fail("could not find the I1 interface");
            }
            assertNotNull("the inferface object is null", intf);

            String[] pds = intf.getArrayofInputs();
            assertEquals("interfaace -wrong number of input parametes", 5,
                    pds.length);
            assertEquals("input parameter name", "P1", pds[0]);
            try {
                CommandLineParameterDefinition inp1 = (CommandLineParameterDefinition)intf
                        .getInputParameter("P4");
                assertEquals("P4 should be KEYWORD", SwitchTypes.KEYWORD, inp1
                        .getSwitchType());
                OptionList optionList = inp1.getOptionList();
                assertNotNull("P4 should have an options list", optionList);
                assertTrue("P4 should have 3 OptionVal.", optionList.getOptionVal().size() == 3);
                assertEquals("HTML", optionList.getOptionVal().get(0).getContent());
                assertEquals("optionDisplay", optionList.getOptionVal().get(0).getDisplayValue());
                
                assertEquals("VOTable", optionList.getOptionVal().get(1).getContent());
                assertEquals("ASCII", optionList.getOptionVal().get(2).getContent());
            }
            catch (ParameterNotInInterfaceException e4) {
                fail("paramter not found");
            }
            String[] pd2s = intf.getArrayofOutputs();
            assertEquals("wrong number of output parametes", 3, pd2s.length);
            try {
                ParameterDescription inp1 = intf.getOutputParameter("P3");
                assertTrue(inp1 instanceof CommandLineParameterDefinition);
                assertTrue(((CommandLineParameterDefinition)inp1).isFileRef());
            }
            catch (ParameterNotInInterfaceException e4) {
                fail("paramter not found");
            }
            ParameterDescription p6 = intf.getInputParameter("P6");
            Cardinality c6 = intf.getParameterCardinality("P6");
            assertTrue("P6 should be optional", c6.getMinOccurs() == 0);
            assertTrue("P6 should have maxoccurs 1", c6.getMaxOccurs() == 1);
            Cardinality c1 = intf.getParameterCardinality("P1");
            assertTrue("P1 should be mandatory", c1.getMinOccurs() == 1);
            assertTrue("p1 should occur only once", c1.getMaxOccurs() == 1);
            CommandLineParameterDefinition p14 = (CommandLineParameterDefinition)intf.getOutputParameter("P14");
            assertTrue("P14 should have a localfile reference", p14.getLocalFileName().equals("testlocalfile"));
        }
        catch (ApplicationDescriptionNotFoundException e) {
            fail("expected test application " + TESTAPPNAME + "not found");
        }
    }
    
     @Test
    public void testCreateDefaultApplication() throws Exception {
        Application app = testAppDescr.initializeApplication("foo",secGuard,buildTool("1"));
        assertNotNull(app);
        assertEquals(CommandLineApplication.class,app.getClass()); 
    }

    @Override
    protected Tool buildTool(String delay) throws Exception {
	      return Toolbuilder.buildTool(delay, testAppDescr);
    }

    @Override
    protected TestAppInfo setupApplication() {
	    return appInfo; // do nothing - the default that is setup is correct for the "test application"
    }

}