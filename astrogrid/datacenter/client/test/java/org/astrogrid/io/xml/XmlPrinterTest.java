/*
 $Id: XmlPrinterTest.java,v 1.4 2004/07/05 14:06:05 mch Exp $

 (c) Copyright...
 */
package org.astrogrid.io.xml;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;

/**
 * Tests A special XmlTagPrinter used to represent the underlying output stream.
 * <p>
 *
 */

public class XmlPrinterTest extends TestCase {
   
   /** Writes out a simple table
    */
   public void testValid() throws IOException, ParserConfigurationException, SAXException {
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

   /** Tests tag making
    */
   public void testNewTag() throws IOException, ParserConfigurationException, SAXException {
      StringWriter sw = new StringWriter();
      
      XmlPrinter xOut = new XmlPrinter(sw);
      
      //test new tag
      XmlTagPrinter ftag = xOut.newTag("FRUIT");
 
      //test can't make another peer
      try {
         xOut.newTag("Banana");
         fail("Should not be allowed to create a new peer Tag");
      }
      catch (AssertionError ae) { /* ignore - should happen */ }

      //test can't write a peer tag
      try {
         xOut.writeTag("Banana", "Value");
         fail("Should not be allowed to write a peer Tag");
      }
      catch (AssertionError ae) { /* ignore - should happen */ }
         
      //write something to ftag and close
      ftag.writeLine("Some value of fruit");
      ftag.close();

      //test can now create a new tag
      ftag = xOut.newTag("BAT");
      ftag.writeLine("Vampires!!");

      
      //make sure close works all the way up
      xOut.close();
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
 Revision 1.4  2004/07/05 14:06:05  mch
 More tests

 Revision 1.3  2004/07/05 13:49:23  mch
 Fixed static testWrite method

 Revision 1.2  2004/07/03 11:51:53  mch
 Added validation test

 Revision 1.1  2004/07/02 16:53:12  mch
 Added XML Writer tests

 */

