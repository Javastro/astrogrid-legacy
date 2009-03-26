/*$Id: ApiHelpIntegrationTest.java,v 1.7 2009/03/26 18:01:21 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.ivoa.LooselyTypedFunctionCallIntegrationTest;

/** test the apihelp interface
 * @see LooselyTypedFunctionCallIntegrationTest for additional tests of this component.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class ApiHelpIntegrationTest extends InARTestCase {

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final ACR reg =getACR();
        assertNotNull(reg);
        help = (ApiHelp) reg.getService(ApiHelp.class);
        assertNotNull(help);
    }
    protected ApiHelp help;
    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        help = null;
    }


    public void testListMethods() {
        final List l = Arrays.asList(help.listMethods());
        assertNotNull(l);
        assertTrue(l.size() > 20);
        assertTrue(l.contains("system.configuration.getKey"));
    }

    public void testListModules() {
        final List l = Arrays.asList(help.listModules());
        assertNotNull(l);
        assertTrue(l.contains("system"));
        assertTrue(l.size() > 3);
    }

    public void testListModuleDescriptors() {
    	final List l = Arrays.asList(help.listModuleDescriptors());
    	assertNotNull(l);
    	assertTrue(l.size() > 3);
    	assertNotNull(l.get(0));
    }
    	
    public void testListComponentsOfModule() throws Exception {
        List l = Arrays.asList(help.listComponentsOfModule("builtin"));
        assertTrue(l.contains("builtin.shutdown"));
        l = Arrays.asList(help.listComponentsOfModule("system"));
        assertTrue(l.contains("system.configuration"));
        assertFalse(l.contains("builtin.shutdown"));
    }
    
    public void testListComponentsOfModuleUnknown() {
    	try {
    		help.listComponentsOfModule("unknown");
    		fail("Exp[ected to barf");
    	} catch (final NotFoundException e) {
    		// ok
    	}
    }
    
    public void testListComponentsOfModuleNull() {
    	try {
    		help.listComponentsOfModule(null);
    		fail("Expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}
    	try {
    		help.listComponentsOfModule("");
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    	}
    }
    public void testInterfaceClassName() throws NotFoundException {
    	assertEquals(Configuration.class.getName(),help.interfaceClassName("system.configuration"));
    	
    }

    public void testInterfaceClassNameUnknown() {
    	try {
    		help.interfaceClassName("foo.bar");
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		// ok
    	}
    }
    
    public void testInterfaceClassNameNull() {
    	try {
    		help.interfaceClassName(null);
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		// ok
    	}
    }    	
    
    public void testInterfaceClassNameMalformed() {
    	try {
    		help.interfaceClassName("wibble");
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		// ok
    	}
    }  

    public void testListMethodsOfComponent() throws Exception {
        List l = Arrays.asList(help.listMethodsOfComponent("builtin.shutdown"));
        assertTrue(l.contains("builtin.shutdown.halt"));
        assertFalse(l.contains("system.configuration.list"));        
        l = Arrays.asList(help.listMethodsOfComponent("system.configuration"));
        assertTrue(l.contains("system.configuration.list"));
        assertFalse(l.contains("builtin.shutdown.halt"));        
    }
    
    public void testListMethodsOfComponentNull()  {
    	try {
    		help.listComponentsOfModule(null);
    		fail("Expected to barf");
    	} catch (final NotFoundException e) {
    		//ok
    	}
    	try {
    		help.listComponentsOfModule("");
    		fail("Expected to barf");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	
    	
    }

    public void testListMethodsOfComponentUnknown() {
    	try {
    		help.listComponentsOfModule("wibble.foo");
    		fail("Expected to barf");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	
    
    }
    
    public void testListMethodsOfComponentMalformed() {
    	try {
    		help.listComponentsOfModule("wibb");
    		fail("Expected to barf");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	
    }
    
    
    
    public void testMethodSignature() throws Exception {
        final List result = Arrays.asList(help.methodSignature("system.configuration.list"));
        assertEquals(1,result.size());
        final String[] sig1 = (String[])result.get(0);
        assertEquals(1,sig1.length);
        assertEquals("Map",sig1[0]); 
    }
    
    public void testMethodSignatureParameters() throws NotFoundException {
        final List result = Arrays.asList(help.methodSignature("system.configuration.setKey"));
        assertEquals(1,result.size());
        final String[] sig1 = (String[])result.get(0);
        assertEquals(3,sig1.length);
        System.out.println(Arrays.asList(sig1));
        assertEquals("boolean",sig1[0]); 
    }
    
    public void testMethodSignatureNull() {
    	try {
    		help.methodSignature(null);
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}
    }
    
    public void testMethodSignatureUnknown() {
    	try {
    		help.methodSignature("foo.bar");
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	
    }
    
    public void testMethodSignatureMalformed() {
    	try {
    		help.methodSignature("foo");
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	
    }
    
    public void testMethodSignatureInternal() throws NotFoundException {
    	final String[][] r = help.methodSignature("system.listMethods");
    	assertNotNull(r);
    	assertEquals(1,r.length);
    	assertEquals(2,r[0].length);
    }

    public void testModuleHelp() throws Exception {
       assertNotNull(help.moduleHelp("builtin"));
       assertNotNull(help.moduleHelp("system"));
       assertFalse(help.moduleHelp("builtin").equals(help.moduleHelp("system")));
    }

    public void testModuleHelpNull() {
    	try {
    		help.moduleHelp(null);
    		fail("Expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}
    	try {
    		help.moduleHelp("");
    		fail("Expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	
    }
    
    public void testModuleHelpUnknown() {
    	try {
    		help.moduleHelp("unknown");
    		fail("Expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	
    }
    
    public void testComponentHelp() throws Exception {
        assertNotNull(help.componentHelp("builtin.shutdown"));
        assertNotNull(help.componentHelp("system.configuration"));
        assertFalse(help.componentHelp("builtin.shutdown").equals(help.componentHelp("system.configuration")));
    }
    
    public void testComponentHelpNull() {
    	try {
    		help.componentHelp(null);
    		fail("Expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	
    	try {
    		help.componentHelp("");
    		fail("Expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	    	
    }
    
    public void testComponentHelpUnknown() {
    	try {
    		help.componentHelp("unknown.unknown");
    		fail("Expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	    	
    }
    
    public void testComponentHelpMalformed() {
    	try {
    		help.componentHelp("unknown");
    		fail("Expected to fail");
    	} catch (final NotFoundException e) {
    		//ok
    	}    	    	
    }

    public void testMethodHelp() throws Exception {
        assertNotNull(help.methodHelp("system.configuration.getKey"));
    }
    
    public void testMethodHelpNull() {
    	try {
    		help.methodHelp(null);
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		//ok.
    	}
    	try {
    		help.methodHelp("");
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		//ok.
    	}    	
    }
    
    public void testMethodHelpUnknown() {
    	try {
    		help.methodHelp("foo.bar.choo");
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		//ok.
    	}    	
    }
    
    public void testMethodHelpMalformed() {
    	try {
    		help.methodHelp("foo");
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		//ok.
    	}   
    	try {
    		help.methodHelp("foo.bar");
    		fail("expected to fail");
    	} catch (final NotFoundException e) {
    		//ok.
    	}    	
    }
   
                

    public static Test suite() {
        return new ARTestSetup(new TestSuite(ApiHelpIntegrationTest.class));
    }
}


/* 
$Log: ApiHelpIntegrationTest.java,v $
Revision 1.7  2009/03/26 18:01:21  nw
added override annotations

Revision 1.6  2008/12/22 18:18:18  nw
improved in-program API help.

Revision 1.5  2008/05/28 12:26:18  nw
improved tsts

Revision 1.4  2007/01/29 10:42:48  nw
tidied.

Revision 1.3  2007/01/23 11:53:37  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.2  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/