/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/test/org/astrogrid/filemanager/common/Attic/FileManagerPropertiesTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:44:04 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerPropertiesTestCase.java,v $
 *   Revision 1.4  2005/01/28 10:44:04  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.3.2.1  2005/01/18 14:52:48  dave
 *   Added node create and modify dates ..
 *
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.1  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;

/**
 * A JUnit test case for the FileManagerProperties.
 *
 */
public class FileManagerPropertiesTestCase
    extends TestCase
    {

    /**
     * Check we get the right Exception for a null set.
     *
     */
    public void testDifferenceNull()
        throws Exception
        {
        FileManagerProperties frog = new FileManagerProperties();
        try {
            frog.difference(
                null
                );
            }
        catch(IllegalArgumentException ouch)
            {
            return ;
            }
        fail("Expected IllegalArgumentException");
        }

    /**
     * Check that we get the right result for an empty set.
     *
     */
    public void testDifferenceEmpty()
        throws Exception
        {
        FileManagerProperties frog = new FileManagerProperties();
        FileManagerProperties toad = new FileManagerProperties();
        FileManagerProperties newt = frog.difference(toad);
        assertEquals(
            0,
            newt.toArray().length
            );
        }

    /**
     * Check that we get the right result for an extra entry.
     *
     */
    public void testDifferenceExtra()
        throws Exception
        {
        FileManagerProperties frog = new FileManagerProperties();
        FileManagerProperties toad = new FileManagerProperties();
        toad.setManagerResourceName("toad");
        FileManagerProperties newt = frog.difference(toad);
        assertEquals(
            2,
            newt.toArray().length
            );
        assertEquals(
            "toad",
            newt.getManagerResourceName()
            );
        }

    /**
     * Check that we get the right result for a changed entry.
     *
     */
    public void testDifferenceChanged()
        throws Exception
        {
        FileManagerProperties frog = new FileManagerProperties();
        frog.setManagerResourceName("frog");
        FileManagerProperties toad = new FileManagerProperties();
        toad.setManagerResourceName("toad");
        FileManagerProperties newt = frog.difference(toad);
        assertEquals(
            2,
            newt.toArray().length
            );
        assertEquals(
            "toad",
            newt.getManagerResourceName()
            );
        }

    /**
     * Check that we get the right result for the same entry.
     *
     */
    public void testDifferenceSame()
        throws Exception
        {
        FileManagerProperties frog = new FileManagerProperties();
        frog.setManagerResourceName("frog");
        FileManagerProperties toad = new FileManagerProperties();
        toad.setManagerResourceName("frog");
        FileManagerProperties newt = frog.difference(toad);
        assertEquals(
            0,
            newt.toArray().length
            );
        assertNull(
            newt.getManagerResourceName()
            );
        }

    }
