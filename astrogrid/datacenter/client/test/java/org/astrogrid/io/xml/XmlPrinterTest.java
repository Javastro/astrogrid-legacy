/*
 $Id: XmlPrinterTest.java,v 1.2 2004/07/03 11:51:53 mch Exp $

 (c) Copyright...
 */
package org.astrogrid.io.xml;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;

/**
 * Tests A special XmlTagPrinter used to represent the underlying output stream.
 * <p>
 *
 */

public class XmlPrinterTest {
   
   /** Test harness method - writes out a simple table
    */
   public static void testWrite() throws IOException, ParserConfigurationException, SAXException {
      StringWriter sw = new StringWriter();
      
      XmlPrinter xOut = new XmlPrinter(sw);
      
      //test new tag
      XmlTagPrinter ftag = xOut.newTag("FRUIT","");
      
      //test convenience routine
      ftag.writeTag("DESCRIPTION","Sort of fruity things");
      
      XmlTagPrinter atag = ftag.newTag("APPLE","");
      atag.writeTag("SKIN","Rosy");
      atag.writeTag("FLESH","White & Powdery");
      atag.writeTag("STALK","Yes unless it's fallen off");
      atag.close();
      
      XmlTagPrinter otag = ftag.newTag("ORANGE","");
      otag.writeTag("SKIN","Orange");
      otag.writeTag("FLESH","Really Orange. Or Red.");
      otag.writeTag("STALK","Would be orange but I've eaten it");
      //         otag.close();
      
      xOut.close();
      
      //check it's valid
      DomHelper.newDocument(sw.toString());
   }
   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(XmlPrinterTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

/*
 $Log: XmlPrinterTest.java,v $
 Revision 1.2  2004/07/03 11:51:53  mch
 Added validation test

 Revision 1.1  2004/07/02 16:53:12  mch
 Added XML Writer tests

 */
