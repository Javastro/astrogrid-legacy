/*
 * $Id TestWorkspace.java $
 *
 */

package org.astrogrid.util;


/**
 * Unit tests for the DomLoader class
 *
 * @author M Hill
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class DomLoaderTest extends TestCase
{
   
   public void testLoadFile() throws IOException, ParserConfigurationException, SAXException
   {
      InputStream in = this.getClass().getResourceAsStream("xmlDocument.xml");
      Document doc = DomHelper.newDocument(in);
      assertNotNull(doc);
   }

   public void testReadString() throws IOException, ParserConfigurationException, SAXException
   {
      String xml = "<Root><Parent><Child>Arthuer</Child><Child>Beatrice</Child></Parent></Root>";
      Document doc = DomHelper.newDocument(xml);
      assertNotNull(doc);
   }

   public void testReadUrl() throws IOException, ParserConfigurationException, SAXException
   {
      URL xml = this.getClass().getResource("xmlDocument.xml");
      Document doc = DomHelper.newDocument(xml);
      assertNotNull(doc);
   }

   /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(DomLoaderTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }


}

/*
$Log: DomLoaderTest.java,v $
Revision 1.3  2004/03/09 17:06:36  mch
Switched to test DomHelper

Revision 1.2  2004/03/01 22:46:29  mch
Increased test coverage

Revision 1.1  2003/12/02 19:59:17  mch
Implementation-neutral DOM loader

Revision 1.1  2003/11/18 11:12:33  mch
Moved Workspace to common

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.12  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.11  2003/09/24 21:13:16  nw
moved setup fromo constructor to setUp() - then can call from other tests.

Revision 1.10  2003/09/17 14:53:02  nw
tidied imports

Revision 1.9  2003/09/15 18:01:45  mch
Better test coverage

Revision 1.8  2003/09/10 14:48:35  nw
fixed breaking tests

Revision 1.7  2003/09/08 19:39:55  mch
More bugfixes and temporary file locations

Revision 1.6  2003/09/08 18:35:54  mch
Fixes for bugs raised by WorkspaceTest

Revision 1.5  2003/09/05 13:24:53  nw
added forgotten constructor (is this still needed for unit tests?)

Revision 1.4  2003/09/05 01:03:01  nw
extended to test workspace thoroughly

Revision 1.3  2003/09/04 10:49:16  nw
fixed typo

Revision 1.2  2003/09/04 09:24:32  nw
added martin's changes

Revision 1.1  2003/08/29 15:27:20  mch
Renamed TestXxxx to XxxxxTest so Maven runs them

Revision 1.1  2003/08/27 18:12:35  mch
Workspace tester

*/
