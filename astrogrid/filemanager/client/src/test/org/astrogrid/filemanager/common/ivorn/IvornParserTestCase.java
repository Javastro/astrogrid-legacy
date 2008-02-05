/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/common/ivorn/IvornParserTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: pah $</cvs:author>
 * <cvs:date>$Date: 2008/02/05 11:38:00 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: IvornParserTestCase.java,v $
 *   Revision 1.3  2008/02/05 11:38:00  pah
 *   RESOLVED - bug 2545: Problem with IVORN resolution
 *   http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2545
 *
 *   Revision 1.2  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/02/11 17:16:03  nw
 *   knock on effect of renaming and making IvornFactory static
 *
 *   Revision 1.1.2.1  2005/02/10 18:01:06  jdt
 *   Moved common into client.
 *
 *   Revision 1.3.8.1  2005/02/10 16:24:17  nw
 *   formatted code, added AllTests that draw all tests together for easy execution from IDE
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
 *   Revision 1.1.2.2  2004/11/26 05:07:17  dave
 *   Fixed node tests for integration ...
 *
 *   Revision 1.1.2.1  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common.ivorn;

import java.net.URISyntaxException;

import org.astrogrid.store.Ivorn;

import junit.framework.TestCase;

/**
 * A JUnit test case for the FileManager ivorn parser.
 *  
 */
public class IvornParserTestCase extends TestCase {

    /**
     * Check we can create a parser with a String.
     *  
     */
    public void testParseString() throws Exception {
        assertNotNull(new IvornParser(
                "ivo://org.astro.test/path#fragment"));
    }

    /**
     * Check we can create a parser with an Ivorn.
     *  
     */
    public void testParseIvorn() throws Exception {
        assertNotNull(new IvornParser(new Ivorn(
                "ivo://org.astro.test/path#fragment")));
    }
    
    
    public void testParseNewIvorn() throws URISyntaxException {
	new IvornParser(new Ivorn("ivo://KonaAndrews@org.astrogrid.regtest/community#/test_folder/new_file0.vot"));
    }

    /**
     * Check we get the right service ident.
     *  
     */
    public void testGetServiceIdent() throws Exception {
        IvornParser parser = new IvornParser(
                "ivo://org.astro.test/path#fragment");
        assertEquals("org.astro.test/path", parser.getServiceIdent());
    }

}