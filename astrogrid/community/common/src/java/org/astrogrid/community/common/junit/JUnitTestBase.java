/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/junit/JUnitTestBase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitTestBase.java,v $
 *   Revision 1.3  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.54.2  2004/06/17 14:50:01  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.2.54.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
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
