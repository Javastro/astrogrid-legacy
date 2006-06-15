/*$Id: ApiHelpIntegrationTest.java,v 1.1 2006/06/15 09:18:24 nw Exp $
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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.ACRTestSetup;

import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** test the apihelp interface
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class ApiHelpIntegrationTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg =getACR();
        assertNotNull(reg);
        help = (ApiHelp) reg.getService(ApiHelp.class);
        assertNotNull(help);
    	uri = new URI("ivo://org.astrogrid/Galaxev");
    }
    protected ApiHelp help;
    protected URI uri;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    /**
     * @return
     */
    protected ACR getACR() throws Exception{
        return (ACR)ACRTestSetup.acrFactory.getACR();
    }    

    public void testListMethods() {
        List l = Arrays.asList(help.listMethods());
        assertNotNull(l);
        assertTrue(l.size() > 20);
        assertTrue(l.contains("system.configuration.getKey"));
    }

    public void testListModules() {
        List l = Arrays.asList(help.listModules());
        assertNotNull(l);
        assertTrue(l.contains("system"));
        assertTrue(l.size() > 3);
    }

    public void testListModuleDescriptors() {
    	List l = Arrays.asList(help.listModuleDescriptors());
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
    	} catch (NotFoundException e) {
    		// ok
    	}
    }
    
    public void testListComponentsOfModuleNull() {
    	try {
    		help.listComponentsOfModule(null);
    		fail("Expected to fail");
    	} catch (NotFoundException e) {
    		//ok
    	}
    	try {
    		help.listComponentsOfModule("");
    		fail("expected to fail");
    	} catch (NotFoundException e) {
    	}
    }
    public void testInterfaceClassName() throws NotFoundException {
    	assertEquals(Configuration.class.getName(),help.interfaceClassName("system.configuration"));
    	
    }

    public void testInterfaceClassNameUnknown() {
    	try {
    		help.interfaceClassName("foo.bar");
    		fail("expected to fail");
    	} catch (NotFoundException e) {
    		// ok
    	}
    }
    
    public void testInterfaceClassNameNull() {
    	try {
    		help.interfaceClassName(null);
    		fail("expected to fail");
    	} catch (NotFoundException e) {
    		// ok
    	}
    }    	
    
    public void testInterfaceClassNameMalformed() {
    	try {
    		help.interfaceClassName("wibble");
    		fail("expected to fail");
    	} catch (NotFoundException e) {
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
    	} catch (NotFoundException e) {
    		//ok
    	}
    	try {
    		help.listComponentsOfModule("");
    		fail("Expected to barf");
    	} catch (NotFoundException e) {
    		//ok
    	}    	
    	
    }

    public void testListMethodsOfComponentUnknown() {
    	try {
    		help.listComponentsOfModule("wibble.foo");
    		fail("Expected to barf");
    	} catch (NotFoundException e) {
    		//ok
    	}    	
    
    }
    
    public void testListMethodsOfComponentMalformed() {
    	try {
    		help.listComponentsOfModule("wibb");
    		fail("Expected to barf");
    	} catch (NotFoundException e) {
    		//ok
    	}    	
    }
    
    
    
    public void testMethodSignature() throws Exception {
        List result = Arrays.asList(help.methodSignature("system.configuration.list"));
        assertEquals(1,result.size());
        String[] sig1 = (String[])result.get(0);
        assertEquals(1,sig1.length);
        assertEquals("key-value map",sig1[0]); 
    }
    
    public void testMethodSignatureParameters() throws NotFoundException {
        List result = Arrays.asList(help.methodSignature("system.configuration.setKey"));
        assertEquals(1,result.size());
        String[] sig1 = (String[])result.get(0);
        assertEquals(3,sig1.length);
        System.out.println(Arrays.asList(sig1));
        assertEquals("boolean",sig1[0]); 
    }
    
    public void testMethodSignatureNull() {
    	try {
    		help.methodSignature(null);
    		fail("expected to fail");
    	} catch (NotFoundException e) {
    		//ok
    	}
    }
    
    public void testMethodSignatureUnknown() {
    	try {
    		help.methodSignature("foo.bar");
    		fail("expected to fail");
    	} catch (NotFoundException e) {
    		//ok
    	}    	
    }
    
    public void testMethodSignatureMalformed() {
    	try {
    		help.methodSignature("foo");
    		fail("expected to fail");
    	} catch (NotFoundException e) {
    		//ok
    	}    	
    }
    
    public void testMethodSignatureInternal() throws NotFoundException {
    	String[][] r = help.methodSignature("system.listMethods");
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
    	} catch (NotFoundException e) {
    		//ok
    	}
    	try {
    		help.moduleHelp("");
    		fail("Expected to fail");
    	} catch (NotFoundException e) {
    		//ok
    	}    	
    }
    
    public void testModuleHelpUnknown() {
    	try {
    		help.moduleHelp("unknown");
    		fail("Expected to fail");
    	} catch (NotFoundException e) {
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
    	} catch (NotFoundException e) {
    		//ok
    	}    	
    	try {
    		help.componentHelp("");
    		fail("Expected to fail");
    	} catch (NotFoundException e) {
    		//ok
    	}    	    	
    }
    
    public void testComponentHelpUnknown() {
    	try {
    		help.componentHelp("unknown.unknown");
    		fail("Expected to fail");
    	} catch (NotFoundException e) {
    		//ok
    	}    	    	
    }
    
    public void testComponentHelpMalformed() {
    	try {
    		help.componentHelp("unknown");
    		fail("Expected to fail");
    	} catch (NotFoundException e) {
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
    	} catch (NotFoundException e) {
    		//ok.
    	}
    	try {
    		help.methodHelp("");
    		fail("expected to fail");
    	} catch (NotFoundException e) {
    		//ok.
    	}    	
    }
    
    public void testMethodHelpUnknown() {
    	try {
    		help.methodHelp("foo.bar.choo");
    		fail("expected to fail");
    	} catch (NotFoundException e) {
    		//ok.
    	}    	
    }
    
    public void testMethodHelpMalformed() {
    	try {
    		help.methodHelp("foo");
    		fail("expected to fail");
    	} catch (NotFoundException e) {
    		//ok.
    	}   
    	try {
    		help.methodHelp("foo.bar");
    		fail("expected to fail");
    	} catch (NotFoundException e) {
    		//ok.
    	}    	
    }
    
    public void testCallFunctionSimpleOriginal() throws InvalidArgumentException, NotFoundException, ACRException{

    	Object result = help.callFunction(
    			"astrogrid.registry.getResourceInformation"
    			,ApiHelp.ORIGINAL
    			, new Object[]{uri}
    			);
    	assertNotNull(result);
    	if (result instanceof ResourceInformation ) {
    		assertEquals(uri,((ResourceInformation)result).getId());
    	} else if (result instanceof Map) {
    		assertEquals(uri.toString(),((Map)result).get("id"));
    	} else {
    		fail("wrong type");
    	}
    }
    
    public void testCallFunctionSimpleOriginalWithStringParam() throws InvalidArgumentException, NotFoundException, ACRException {
    	Object result = help.callFunction(
    			"astrogrid.registry.getResourceInformation"
    			,ApiHelp.ORIGINAL
    			, new Object[]{uri.toString()} // will have to convert to correct type.
    			);
    	assertNotNull(result);
    	if (result instanceof ResourceInformation ) {
    		assertEquals(uri,((ResourceInformation)result).getId());
    	} else if (result instanceof Map) {
    		assertEquals(uri.toString(),((Map)result).get("id"));
    	} else {
    		fail("wrong type");
    	}
    }
    
    public void testCallFunctionSimpleDatastrucrtre() throws  InvalidArgumentException, NotFoundException, ACRException {
    	Object result = help.callFunction(
    			"astrogrid.registry.getResourceInformation"
    			,ApiHelp.DATASTRUCTURE
    			, new Object[]{uri}
    			);
    	assertNotNull(result);
    	assertTrue(result instanceof Map);
    	assertEquals(uri.toString(),((Map)result).get("id"));    	
    }
    
    public void testCallFunctionSimpleString() throws  InvalidArgumentException, NotFoundException, ACRException {
    	Object result = help.callFunction(
    			"astrogrid.registry.getResourceInformation"
    			,ApiHelp.STRING
    			, new Object[]{uri}
    			);
    	assertNotNull(result);
    	assertTrue(result instanceof String);
    	assertTrue(((String)result).trim().length() > 0);  	
    	assertTrue(((String)result).indexOf(uri.toString()) != -1);
    }
    
    

    
    public void testCallFunctionWrongNumberOfParameters() throws InvalidArgumentException, NotFoundException, ACRException{
    	try {
    	Object result = help.callFunction(
    			"astrogrid.registry.getResourceInformation"
    			,ApiHelp.ORIGINAL
    			, new Object[]{}
    			);
    	fail("expected to fail");
    	} catch (InvalidArgumentException e) {
    		// ok
    	}
    }    
    /* will fail in function-specific ways if wrng parameters are passed in.
     * all attempt is made to convert to correct type - but usually produces a meaningless
     * value - garbage in, garbage out.
     */
//    public void testCallFunctionWrongParameterTypes() throws InvalidArgumentException, NotFoundException, ACRException, MalformedURLException{
//    	try {
//        	Object result = help.callFunction(
//        			"astrogrid.registry.getResourceInformation"
//        			,ApiHelp.ORIGINAL
//        			, new Object[]{new Integer(42)}
//        			);
//        	fail("expected to fail");
//        	} catch (InvalidArgumentException e) {
//        		// ok
//        	}
//        }    
    
    public void testCallFunctionWrongNumberOfParametersNull() throws InvalidArgumentException, NotFoundException, ACRException{
    	try {
        	Object result = help.callFunction(
        			"astrogrid.registry.getResourceInformation"
        			,ApiHelp.ORIGINAL
        			, null
        			);
        	fail("expected to fail");
        	} catch (InvalidArgumentException e) {
        		// ok
        	}
        }    

    // when called with unknown kind, falls back to default.
    public void testCallFunctionUnknownKind() throws InvalidArgumentException, NotFoundException, ACRException {
    	assertEquals(
    			help.callFunction("system.webserver.getPort",ApiHelp.ORIGINAL,new String[]{})
    			, help.callFunction("system.webserver.getPort",42,new String[]{})
    			);
    }
    
    public void testCallFunctionNullParameter() throws InvalidArgumentException, NotFoundException, ACRException {
    	Object result = help.callFunction("system.webserver.getPort",ApiHelp.ORIGINAL, new Object[]{});
    	assertNotNull(result);    	
    	assertEquals(Integer.class,result.getClass());
    }
    
    public void testCallFunctionNull() throws ACRException {
    	try {
    		help.callFunction(null,ApiHelp.DATASTRUCTURE,null);
    		fail("Expected to barf");
    	} catch (InvalidArgumentException e) {
    		//ok.
    	}
    }
    
    public void testCallFunctionUnknown() throws InvalidArgumentException, ACRException {
    	try {
    		help.callFunction("wibble.foo.bar",ApiHelp.DATASTRUCTURE,new Object[]{});
    		fail("Expected to barf");
    	} catch (NotFoundException e) {
    		//ok.
    	}    	    	
    }
    
    public void testCallFunctionMalformed() throws  ACRException {
    	try {
    		help.callFunction("wibble",ApiHelp.DATASTRUCTURE,new Object[]{});
    		fail("Expected to barf");
    	} catch (InvalidArgumentException e) {
    		//ok.
    	}    	
    }
                                            

    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ApiHelpIntegrationTest.class));
    }
}


/* 
$Log: ApiHelpIntegrationTest.java,v $
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