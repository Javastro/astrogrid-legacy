/* $Id: NamingServiceTest.java,v 1.1 2003/11/11 23:48:27 anoncvs Exp $
 * Created on 11-Nov-2003
 *
 */
package org.astrogrid.jes.testutils.naming;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

import junit.framework.TestCase;

/**
 * Test the noddy naming service.
 * @author john
 */
public class NamingServiceTest extends TestCase {
    /**
     * Constructor for NamingServiceTest.
     * @param arg0 testname
     */
    public NamingServiceTest(final String arg0) {
        super(arg0);
    }
    /**
     * Fire up the JUnit textui
     * @param args ignored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(NamingServiceTest.class);
    }
    /**
     * Set up the SimpleContextFactoryBuilder
     * @throws Exception 
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
		if (!NamingManager.hasInitialContextFactoryBuilder()) {
			NamingManager.setInitialContextFactoryBuilder(new SimpleContextFactoryBuilder());
		} 
    }
    
    /**
     * Store something in the naming service
     * @throws NamingException
     */
    public final void testLookup() throws NamingException {
		String name = new String("foo");
		Object obj = new String("bar");
		Context ctx = NamingManager.getInitialContext(null);
		ctx.bind(name,obj);
		assertEquals("Object bound OK", obj, ctx.lookup(name));
    }
    
    /**
     * Fails cleanly if you look for a nonexistent object
     * @throws NamingException
     */
    public final void testLookupMissing() throws NamingException {
    	Context ctx = NamingManager.getInitialContext(null);
    	try {
    		ctx.lookup("dlkfajfl");
    		fail("Should get a NamingException");
    	} catch (NamingException ne) {
    		//expected
    	}
    }
    
    /**
     * Objects can be removed from the naming service
     * @throws NamingException
     */
    public final void testUnbind() throws NamingException {
		String name = new String("foo");
		Object obj = new String("bar");
		Context ctx = NamingManager.getInitialContext(null);
		ctx.bind(name,obj);
		ctx.lookup(name); //should work OK
		ctx.unbind(name);
		try {
			ctx.lookup(name);
			fail("Expect a Naming Exception");
		} catch (NamingException ne) {
			//expected
		}
    }
    
    /**
     * Fails cleanly if you remove a nonexistent object
     * @throws NamingException
     */
    public final void testUnbindMissing() throws NamingException {
		Context ctx = NamingManager.getInitialContext(null);
		try {
			ctx.unbind("foo");
			fail("expected NameNotFoundException");
		} catch (NameNotFoundException ne) {
			//expected
		}
    	
    }
}
/*
 * $Log: NamingServiceTest.java,v $
 * Revision 1.1  2003/11/11 23:48:27  anoncvs
 * Implemented the unbind method and added some tests
 *
 */
