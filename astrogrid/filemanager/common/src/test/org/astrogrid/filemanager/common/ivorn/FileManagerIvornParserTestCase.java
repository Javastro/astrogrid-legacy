/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/test/org/astrogrid/filemanager/common/ivorn/Attic/FileManagerIvornParserTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerIvornParserTestCase.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.2  2004/11/26 05:07:17  dave
 *   Fixed node tests for integration ...
 *
 *   Revision 1.1.2.1  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common.ivorn ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;

/**
 * A JUnit test case for the FileManager ivorn parser.
 *
 */
public class FileManagerIvornParserTestCase
    extends TestCase
    {

    /**
     * Check we can create a parser with a String.
     *
     */
    public void testParseString()
        throws Exception
        {
        assertNotNull(
            new FileManagerIvornParser(
                "ivo://org.astro.test/path#fragment"
                )
            );
        }

    /**
     * Check we can create a parser with an Ivorn.
     *
     */
    public void testParseIvorn()
        throws Exception
        {
        assertNotNull(
            new FileManagerIvornParser(
                new Ivorn(
                    "ivo://org.astro.test/path#fragment"
                    )
                )
            );
        }

    /**
     * Check we get the right service ident.
     *
     */
    public void testGetServiceIdent()
        throws Exception
        {
        FileManagerIvornParser parser = 
            new FileManagerIvornParser(
                "ivo://org.astro.test/path#fragment"
                );
        assertEquals(
            "org.astro.test/path",
            parser.getServiceIdent()
            );
        }


    }
