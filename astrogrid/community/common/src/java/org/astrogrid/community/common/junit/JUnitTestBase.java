/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/junit/JUnitTestBase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitTestBase.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 *   Revision 1.3  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.1  2004/01/27 05:43:10  dave
 *   Moved the JUnit tests around a bit
 *
 *   Revision 1.1.2.1  2004/01/13 14:29:41  dave
 *   Added initial JUnit tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.junit ;

import junit.framework.TestCase ;

/**
 * Base class for our data object JUnit tests.
 *
 */
public class JUnitTestBase
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Test that two objects are equal.
     *
     */
    public void checkEqual(String message, Object a, Object b)
        {
        //
        // Check one way.
        assertEquals(message, a, b) ;
        //
        // Check the other way.
        assertEquals(message, b, a) ;
        }

    /**
     * Test that two objects are not equal.
     * Throws 
     *
     */
    public void checkNotEqual(String message, Object a, Object b)
        {
        //
        // Check that they are not the same object.
        //assertNotSame(message, a, b) ;
        //
        // Check that they are not both null.
        assertFalse(message, (a == b)) ;
        //
        // If a is not null.
        if (null != a)
            {
            //
            // Check that a does not equal b.
            assertFalse(message, a.equals(b)) ;
            }
        //
        // If b is not null.
        if (null != b)
            {
            //
            // Check that b does not equal a.
            assertFalse(message, b.equals(a)) ;
            }
        }
    }
