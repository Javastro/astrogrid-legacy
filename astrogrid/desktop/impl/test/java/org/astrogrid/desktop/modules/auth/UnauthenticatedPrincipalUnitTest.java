/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import java.security.Principal;

import junit.framework.TestCase;

/** unit test for unauthenticated principle.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 22, 20072:58:09 PM
 */
public class UnauthenticatedPrincipalUnitTest extends TestCase {


	public void testCreateUnique() throws Exception {
		UnauthenticatedPrincipal p = new UnauthenticatedPrincipal();
		UnauthenticatedPrincipal p1 = new UnauthenticatedPrincipal();
		assertNotSame(p,p1); // doh.
		assertEquals(p.getName(),p.getLogin());
		assertEquals(p1.getName(),p1.getLogin());
		assertFalse(p1.equals(p));
		assertFalse(p1.getName().equals(p.getName()));
		assertEquals(p.getRoles(),p1.getRoles());
	}
}
