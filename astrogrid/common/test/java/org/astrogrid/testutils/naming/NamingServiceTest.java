/* $Id: NamingServiceTest.java,v 1.1 2003/12/11 18:19:10 jdt Exp $
 * Created on 11-Nov-2003
 *
 */
package org.astrogrid.testutils.naming;

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
  protected final void setUp() throws Exception {
    super.setUp();
    if (!NamingManager.hasInitialContextFactoryBuilder()) {
      NamingManager.setInitialContextFactoryBuilder(
        new SimpleContextFactoryBuilder());
    }
  }

  /**
   * Store something in the naming service
   * @throws NamingException if object not found
   */
  public final void testLookup() throws NamingException {
    String name = new String("foo");
    Object obj = new String("bar");
    Context ctx = NamingManager.getInitialContext(null);
    ctx.bind(name, obj);
    assertEquals("Object bound OK", obj, ctx.lookup(name));
  }

  /**
   * Fails cleanly if you look for a nonexistent object
   * @throws NamingException problem getting initial context
   */
  public final void testLookupMissing() throws NamingException {
    Context ctx = NamingManager.getInitialContext(null);
    try {
      ctx.lookup("dlkfajfl");
      fail("Should get a NamingException");
    } catch (NamingException ne) {
      return; //expected
    }
  }

  /**
   * Objects can be removed from the naming service
   * @throws NamingException problem getting initial context
   */
  public final void testUnbind() throws NamingException {
    String name = new String("foo");
    Object obj = new String("bar");
    Context ctx = NamingManager.getInitialContext(null);
    ctx.bind(name, obj);
    ctx.lookup(name); //should work OK
    ctx.unbind(name);
    try {
      ctx.lookup(name);
      fail("Expect a Naming Exception");
    } catch (NamingException ne) {
      return; //expected
    }
  }

  /**
   * Fails cleanly if you remove a nonexistent object
   * @throws NamingException problem getting initial context
   */
  public final void testUnbindMissing() throws NamingException {
    Context ctx = NamingManager.getInitialContext(null);
    try {
      ctx.unbind("foo");
      fail("expected NameNotFoundException");
    } catch (NameNotFoundException ne) {
      return; //expected
    }

  }
}
/*
 * $Log: NamingServiceTest.java,v $
 * Revision 1.1  2003/12/11 18:19:10  jdt
 * New files to test the modifications to the Configurator which allow it
 * to load properties files from URLs
 *
 * Revision 1.2  2003/11/12 18:13:14  jdt
 * Minor bits and pieces to satisfy the coding conventions.
 *
 * Revision 1.1  2003/11/11 23:48:27  anoncvs
 * Implemented the unbind method and added some tests
 *
 */
