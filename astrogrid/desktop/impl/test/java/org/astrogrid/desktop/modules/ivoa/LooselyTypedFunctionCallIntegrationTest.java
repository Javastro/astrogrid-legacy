/*$Id: LooselyTypedFunctionCallIntegrationTest.java,v 1.5 2009/03/26 18:01:21 nw Exp $
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
import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** test the function call part of the apihelp interface.
 * 
 * @TEST reconsider what this is doing, and why it's in ApiHelp. re-implement to use a method that doesn't call external services.
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class LooselyTypedFunctionCallIntegrationTest extends InARTestCase {

    /*
     * @see TestCase#setUp()
     */
    @Override
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
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        help = null;
        uri = null;
    }

    public void testCallFunctionSimpleOriginal() throws InvalidArgumentException, NotFoundException, ACRException{

    	Object result = help.callFunction(
    			"votech.vomon.checkAvailability"
    			,ApiHelp.ORIGINAL
    			, new Object[]{uri}
    			);
    	assertNull(result);
    	// coul;d do with finding a test method that actually returns a value..
    	

    }
    
    public void testCallFunctionSimpleOriginalWithStringParam() throws InvalidArgumentException, NotFoundException, ACRException {
    	Object result = help.callFunction(
    	        "votech.vomon.checkAvailability"
    			,ApiHelp.ORIGINAL
    			, new Object[]{uri.toString()} // will have to convert to correct type.
    			);
        assertNull(result);
    }
//    
//    public void testCallFunctionSimpleDatastrucrtre() throws  InvalidArgumentException, NotFoundException, ACRException {
//    	Object result = help.callFunction(
//    			"ivoa.adql.s2x"
//    			,ApiHelp.DATASTRUCTURE
//    			, new Object[]{"select * from a as a"}
//    			);
//    	assertNotNull(result);
//    	System.err.println(result);
//    	assertTrue(result instanceof Map);
//    	assertEquals(uri.toString(),((Map)result).get("id"));    	
//    }
    
    public void testCallFunctionSimpleString() throws  InvalidArgumentException, NotFoundException, ACRException {
    	Object result = help.callFunction(
                "ivoa.adql.s2x"
    			,ApiHelp.STRING
                , new Object[]{"select * from a as a"}
    			);
    	assertNotNull(result);
    	assertTrue(result instanceof String);
    	assertTrue(((String)result).trim().length() > 0);  	
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
Revision 1.5  2009/03/26 18:01:21  nw
added override annotations

Revision 1.4  2008/05/28 12:26:17  nw
improved tsts

Revision 1.3  2008/04/23 11:32:43  nw
marked as needing tests.

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