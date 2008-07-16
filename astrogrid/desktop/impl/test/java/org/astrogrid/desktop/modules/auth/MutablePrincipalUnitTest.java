/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import java.security.Principal;

import junit.framework.TestCase;
import org.astrogrid.security.HomespaceLocation;

/** unit test for mutable principal
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 22, 20073:02:33 PM
 */
public class MutablePrincipalUnitTest extends TestCase {


	public void testIntitial() throws Exception {
		MutablePrincipal p = new MutablePrincipal();
		Principal actualPrincipal = p.getActualPrincipal();
		
		// test it comes pre-initialized with something inside.
		assertNotNull(actualPrincipal);
		assertTrue(actualPrincipal instanceof UnauthenticatedPrincipal);
		
		// test that name delegates to this.
		assertEquals(p.getName(),actualPrincipal.getName());
		assertEquals(p.toString(),actualPrincipal.toString());
		
		// test that equality doesn't delegate to this.
		assertFalse(p.equals(actualPrincipal));
		assertFalse(p.hashCode() == actualPrincipal.hashCode());
		
		// test an object is equl to itself.
		// caught a bug in my previous implementation.
		assertEquals(p,p);
		
		
		// finally, test changing the actual Principal works, without changing the identity.
                Principal r = new HomespaceLocation("ivo://foobar");
		int hc = p.hashCode();
		p.setActualPrincipal(r);
		assertEquals(r,p.getActualPrincipal());
		assertEquals(r.getName(),p.getName());
		assertEquals(r.toString(),p.toString());
		assertEquals(hc,p.hashCode());
	}

}
