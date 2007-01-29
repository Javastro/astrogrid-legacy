/*$Id: LooselyTypedFunctionCallIntegrationTest.java,v 1.2 2007/01/29 10:42:28 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** test the function call part of the apihelp interface.
 * 
 * in this package, as requires a variant of the ar which has 
 * a component which takes a complex type as an argument.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class LooselyTypedFunctionCallIntegrationTest extends InARTestCase {

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
        help = null;
        uri = null;
    }

    public void testCallFunctionSimpleOriginal() throws InvalidArgumentException, NotFoundException, ACRException{

    	Object result = help.callFunction(
    			"ivoa.registry.getResource"
    			,ApiHelp.ORIGINAL
    			, new Object[]{uri}
    			);
    	assertNotNull(result);
    	assertTrue(result instanceof Resource);
    		assertEquals(uri,((Resource)result).getId());

    }
    
    public void testCallFunctionSimpleOriginalWithStringParam() throws InvalidArgumentException, NotFoundException, ACRException {
    	Object result = help.callFunction(
    			"ivoa.registry.getResource"
    			,ApiHelp.ORIGINAL
    			, new Object[]{uri.toString()} // will have to convert to correct type.
    			);
    	assertNotNull(result);
    	assertTrue(result instanceof Resource);
    		assertEquals(uri,((Resource)result).getId());
    }
    
    public void testCallFunctionSimpleDatastrucrtre() throws  InvalidArgumentException, NotFoundException, ACRException {
    	Object result = help.callFunction(
    			"ivoa.registry.getResource"
    			,ApiHelp.DATASTRUCTURE
    			, new Object[]{uri}
    			);
    	assertNotNull(result);
    	assertTrue(result instanceof Map);
    	assertEquals(uri.toString(),((Map)result).get("id"));    	
    }
    
    public void testCallFunctionSimpleString() throws  InvalidArgumentException, NotFoundException, ACRException {
    	Object result = help.callFunction(
    			"ivoa.registry.getResource"
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
    			"ivoa.registry.getResource"
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
        			"ivoa.registry.getResource"
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
        return new ARTestSetup(new TestSuite(LooselyTypedFunctionCallIntegrationTest.class));
    }
}


/* 
$Log: LooselyTypedFunctionCallIntegrationTest.java,v $
Revision 1.2  2007/01/29 10:42:28  nw
tidied.

Revision 1.1  2007/01/23 11:53:36  nw
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