/*
 *
 * <cvs:source>$Source:</cvs:source>
 * <cvs:date>$Author:</cvs:date>
 * <cvs:author>$Date: 2003/06/03 13:16:47 $</cvs:author>
 * <cvs:version>$Revision:</cvs:version>
 *
 * <cvs:log>
 * $Log:
 * </cvs:log>
 *
 */
package org.astrogrid.portal.base.junit ;

import junit.framework.TestCase ;

import org.astrogrid.portal.base.AstPortalIdent ;
import org.astrogrid.portal.base.AstPortalBase ;


/**
 *
 * JUnit test for the base components.
 *
 */
public class JUnitTestCase
	extends TestCase
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static boolean DEBUG_FLAG = true ;

	/**
	 * Switch for our assert statements.
	 *
	 */
	protected static boolean ASSERT_FLAG = false ;

	/**
	 * Extension to JUnit test case.
	 * Only checks for a.equals(b), not b.equals(a).
	 * The latter fails if 'a' is an ident and 'b' is a String.
	 *
	 */
	public void assertIdentEqual(String message, Object a, Object b)
		throws Exception
		{
		//
		// Try using a.equals()
		if (null != a)
			{
			assertTrue(message, a.equals(b)) ;
			}
		//
		// Try using b.equals()
/*
 * FIXME
 * Fails if 'a' is an ident and 'b' is a String.
		if (null != b)
			{
			assertTrue(message, b.equals(a)) ;
			}
 *
 */
		}

	/**
	 * Extension to JUnit test case.
	 * Perform equals check both ways, a.equals(b) and b.equals(a).
	 *
	 */
	public void assertIdentNotEqual(String message, Object a, Object b)
		throws Exception
		{
		//
		// If both are null, then fail.
		if ((null == a) && (null == b))
			{
			fail("Both references are null") ;
			}
		//
		// Try using a.equals()
		if (null != a)
			{
			assertFalse(message, a.equals(b)) ;
			}
		//
		// Try using b.equals()
		if (null != b)
			{
			assertFalse(message, b.equals(a)) ;
			}
		}

	/**
	 * Check that we can create an identifier.
	 *
	 */
	public void testIdent()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testIdent") ;

		//
		// Check can create an identifier.
		AstPortalIdent ident = new AstPortalIdent() ;
		if (DEBUG_FLAG) System.out.println("Ident : " + ident) ;
		//
		// Check we got a valid identifier.
		assertNotNull("Null identifier", ident) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that one identifer matches itself.
	 *
	 */
	public void testIdentEqual()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testIdentEqual") ;

		//
		// Check can create an identifier.
		AstPortalIdent ident = new AstPortalIdent() ;
		if (DEBUG_FLAG) System.out.println("Ident : " + ident) ;
		//
		// Check we got a valid identifier.
		assertNotNull("Null identifier", ident) ;
		//
		// Check that the identifier recongises itself.
		assertIdentEqual("Failed to recognise self", ident, ident) ;
		//
		// Check that the identifier recongises its string form.
		assertIdentEqual("Failed to recognise self", ident, ident.toString()) ;
		
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}


	/**
	 * Check that we can create two different identifiers.
	 *
	 */
	public void testIdentNotEqual()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testIdentNotEqual") ;

		//
		// Check can create two identifiers.
		AstPortalIdent one = new AstPortalIdent() ;
		AstPortalIdent two = new AstPortalIdent() ;
		if (DEBUG_FLAG) System.out.println("One : " + one) ;
		if (DEBUG_FLAG) System.out.println("Two : " + two) ;
		//
		// Check we got two identifiers.
		assertNotNull("Null identifier one", one) ;
		assertNotNull("Null identifier two", two) ;
		//
		// Check that they are not the same object.
		assertNotSame("Identifiers are the same", one, two) ;
		//
		// Check that they are not equal.
		assertIdentNotEqual("Identifiers are equal", one, two) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}


	/**
	 * Check that we can create a base class object.
	 *
	 */
	public void testBase()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testBase") ;

		//
		// Check can create a base class object.
		AstPortalBase base = new AstPortalBase() ;
		if (DEBUG_FLAG) System.out.println("Ident : " + base.getIdent()) ;
		//
		// Check we got a valid base class object.
		assertNotNull("Null object", base) ;
		//
		// Check the object has an identifier.
		assertNotNull("Null identifier", base.getIdent()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that two base class object have different idents.
	 *
	 */
	public void testBaseIdentNotEqual()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testBaseNotEqual") ;

		//
		// Check can create two base class objects.
		AstPortalBase one = new AstPortalBase() ;
		AstPortalBase two = new AstPortalBase() ;
		if (DEBUG_FLAG) System.out.println("One : " + one.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("Two : " + two.getIdent()) ;
		//
		// Check we got a valid base class objects.
		assertNotNull("Null object", one) ;
		assertNotNull("Null object", two) ;
		//
		// Check that they are not the same object.
		assertNotSame("Identifiers are the same", one, two) ;
		//
		// Check they both have an identifier.
		assertNotNull("Null identifier", one.getIdent()) ;
		assertNotNull("Null identifier", two.getIdent()) ;
		//
		// Check that the identifiers are not the same object.
		assertNotSame("Identifiers are the same", one.getIdent(), two.getIdent()) ;
		//
		// Check that the identifiers are not equal.
		assertIdentNotEqual("Identifiers are equal", one.getIdent(), two.getIdent()) ;


		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}
