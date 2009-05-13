/*
 * $Id: AdqlTestHelper.java,v 1.1 2009/05/13 13:21:01 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.adql;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.InputStreamReader;
import org.astrogrid.io.Piper;

import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Some helper routines for loading test queries, etc
 */

public class AdqlTestHelper    {
   
   private static String DEFAULT_VERSION = "v074";    // Defaults to 0.7.4
   String version;

   public AdqlTestHelper() 
   {
     this.version = DEFAULT_VERSION;
   }
   public AdqlTestHelper(String version) 
   {
     this.version = version;
   }

   public Document getTestAdql(String queryFile) throws ParserConfigurationException, SAXException, IOException  {
      assert (queryFile != null);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      assert (is != null) : "Could not open query file: " + queryFile;

      Document adqlDom = DomHelper.newDocument(is);
      adqlDom.normalize(); //so we can compare Adql DOMs
      //return adqlDom.getDocumentElement();
      return adqlDom;
   }
   public InputStream getTestAdqlStream(String queryFile) throws ParserConfigurationException, SAXException, IOException  {
      assert (queryFile != null);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      assert (is != null) : "Could not open query file: " + queryFile;
      return is;
   }
  /*
   public Element getTestAdql(String queryFile) throws ParserConfigurationException, SAXException, IOException  {
      assert (queryFile != null);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      assert (is != null) : "Could not open query file: " + queryFile;

      Document adqlDom = DomHelper.newDocument(is);
      adqlDom.normalize(); //so we can compare Adql DOMs
      return adqlDom.getDocumentElement();
   }
   */


   /** Returns true if each element has the same attributes with the same values,
    * the same children elements and each child element is also equal using this
    * method.
    * Can be used to compare docs to see if they are effectively equal.
    * Throws an assertion exception with some indication of where the difference lies
    */
   public void assertElementsEqualOUTOFDATE(Element e1, Element e2) {
      e1.normalize();
      e2.normalize();
      
      assert e1.getTagName().equals(e2.getTagName()) : "Tag names "+e1.getTagName()+" and "+ e2.getTagName()+" not equal";

      //check attributes - order not important, compare both ways
      NamedNodeMap e1Atts = e1.getAttributes();
      for (int i = 0; i < e1Atts.getLength(); i++) {
         Node e1Att = e1Atts.item(i);
         String e2AttValue = e2.getAttribute(e1Att.getNodeName());
         assert e2AttValue != null : "Attribute "+e1Att.getNodeName()+" not in both elements "+e1.getTagName();
         assert e2AttValue.equals(e1Att.getNodeValue()) : "Attribute "+e1Att.getNodeName()+" has different values "+e1Att.getNodeValue()+" and "+e2AttValue+" in "+e1.getTagName();
      }
      NamedNodeMap e2Atts = e2.getAttributes();
      for (int i = 0; i < e2Atts.getLength(); i++) {
         Node e2Att = e2Atts.item(i);
         String e1AttValue = e1.getAttribute(e2Att.getNodeName());
         assert e1AttValue != null : "Attribute "+e2Att.getNodeName()+" not in both elements";
         assert e1AttValue.equals(e2Att.getNodeValue()) : "Attribute "+e2Att.getNodeName()+" has different values "+e2Att.getNodeValue()+" and "+e1AttValue+" in "+e1.getTagName();
      }

      /*
       * KEA COMMENT: This won't work because some nodes may have multiple
       * children with the same name.  We need to find the actual matching 
       * nodes!
      //check children *elements*
      //this includes attributes
      NodeList e1children = e1.getChildNodes();
      NodeList e2children = e2.getChildNodes();
      assert e1children.getLength() == e2children.getLength() : "Different number of children in "+e1.getTagName();

      for (int i = 0; i < e1children.getLength(); i++) {
         if (e1children.item(i) instanceof Element) {
            //get equivelent e2 child
            Element e2child = AdqlXml074Parser.getSingleChildByTagName(e2, e1children.item(i).getNodeName());
            //this might not be quite right...
            assertElementsEqual( (Element) e1children.item(i), e2child);
         }
      }
      */
   }
   
   /* Returns the 'current' adql version of the 'standard test example' NVO queries
    generated on the page
   at http://openskyquery.net/AdqlTranslator/Convertor.aspx */
   public Document getNvo1() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdql(version + "/nvoexample-1-adql0.7.4.xml");
   }
   public InputStream getNvo1Stream() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdqlStream(version + "/nvoexample-1-adql0.7.4.xml");
   }

   public Document getNvo2() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdql(version + "/nvoexample-2-adql0.7.4.xml");
   }
   public InputStream getNvo2Stream() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdqlStream(version + "/nvoexample-2-adql0.7.4.xml");
   }
   
   public Document getNvo3() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdql(version + "/nvoexample-3-adql0.7.4.xml");
   }
   public InputStream getNvo3Stream() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdqlStream(version + "/nvoexample-3-adql0.7.4.xml");
   }
   
   public Document getNvo4() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdql(version + "/nvoexample-4-adql0.7.4.xml");
   }
   public InputStream getNvo4Stream() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdqlStream(version + "/nvoexample-4-adql0.7.4.xml");
   }

   /** Returns the 'current' adql verison of some tests that can be run against the
    * sample stars database */
   public Document getSampleDbPleidies() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdql(version + "/SampleStars-pleidies-adql.xml");
   }
   

   // This one uses the helper's default version
   public Document getSuiteAdql(String name) throws IOException, SAXException, ParserConfigurationException {
     return getSuiteAdql(name, this.version);
   }

   // This one accepts the actual version
   public Document getSuiteAdql(String name, String someVersion) throws IOException, SAXException, ParserConfigurationException {
     if ((name == null) || (name.equals("")) ) {
       throw new IOException("Please supply test name!\n");
     }
     String fullPath = someVersion + "/fullsuite/" + name + ".xml";
     return getTestAdql(fullPath);
   }

   public Document getSuiteAdqlExpected(String name) throws IOException, SAXException, ParserConfigurationException {
     return getSuiteAdqlExpected(name, this.version);
   }
   public Document getSuiteAdqlExpected(String name, String someVersion) throws IOException, SAXException, ParserConfigurationException {
     if ((name == null) || (name.equals("")) ) {
       throw new IOException("Please supply test name!\n");
     }
     String fullPath = someVersion + "/fullsuite/EXPECTED/" + name + ".xml";
     return getTestAdql(fullPath);
   }


   public InputStream getSuiteAdqlStream(String name) throws IOException, SAXException, ParserConfigurationException {
     return getSuiteAdqlStream(name, this.version);
   }

   public InputStream getSuiteAdqlStream(String name, String someVersion) throws IOException, SAXException, ParserConfigurationException {
     if ((name == null) || (name.equals("")) ) {
       throw new IOException("Please supply test name!\n");
     }
     String fullPath = someVersion + "/fullsuite/" + name + ".xml";
     assert (fullPath != null);
     InputStream is = this.getClass().getResourceAsStream(fullPath);
     assert (is != null) : "Could not open query file: " + fullPath;
     return is;
   }


   public String getSuiteAdqlString(String name) throws IOException, SAXException, ParserConfigurationException {
     return getSuiteAdqlString(name, this.version);
   }

   public String getSuiteAdqlString(String name, String someVersion) throws IOException, SAXException, ParserConfigurationException {
     if ((name == null) || (name.equals("")) ) {
       throw new IOException("Please supply test name!\n");
     }
     String fullPath = someVersion + "/fullsuite/" + name + ".xml";
     assert (fullPath != null);
     InputStream is = this.getClass().getResourceAsStream(fullPath);
     assert (is != null) : "Could not open query file: " + fullPath;

     StringWriter sw = new StringWriter();
     Piper.bufferedPipe(new InputStreamReader(is), sw);
     return sw.toString();
   }

   public String getSuiteAdqlStringExpected(String name) throws IOException, SAXException, ParserConfigurationException {
     return getSuiteAdqlStringExpected(name, this.version);
   }

   public String getSuiteAdqlStringExpected(String name, String someVersion) throws IOException, SAXException, ParserConfigurationException {
     if ((name == null) || (name.equals("")) ) {
       throw new IOException("Please supply test name!\n");
     }
     String fullPath = someVersion + "/fullsuite/EXPECTED/" + name + ".xml";
     assert (fullPath != null);
     InputStream is = this.getClass().getResourceAsStream(fullPath);
     assert (is != null) : "Could not open query file: " + fullPath;

     StringWriter sw = new StringWriter();
     Piper.bufferedPipe(new InputStreamReader(is), sw);
     return sw.toString();
   }
}

/*
 $Log: AdqlTestHelper.java,v $
 Revision 1.1  2009/05/13 13:21:01  gtr
 *** empty log message ***

 Revision 1.4  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.3.2.3  2006/04/19 13:57:31  kea
 Interim checkin.  All source is now compiling, using the new Query model
 where possible (some legacy classes are still using OldQuery).  Unit
 tests are broken.  Next step is to move the legacy classes sideways out
 of the active tree.

 Revision 1.3.2.2  2006/03/30 17:01:19  kea
 Experiments including:
  - Changing namespace of incoming xml from 0.7.4 to 1.0
  - Adding missing <From> nodes
  - Testing 0.7.4 and 1.0 sample queries

 Revision 1.3.2.1  2006/03/27 15:09:20  kea
 Added adql 1.0 testing stuff.

 Revision 1.3  2006/03/22 15:10:13  clq2
 KEA_PAL-1534

 Revision 1.2.82.1  2006/02/16 17:13:05  kea
 Various ADQL/XML parsing-related fixes, including:
  - adding xsi:type attributes to various tags
  - repairing/adding proper column alias support (aliases compulsory
     in adql 0.7.4)
  - started adding missing bits (like "Allow") - not finished yet
  - added some extra ADQL sample queries - more to come
  - added proper testing of ADQL round-trip conversions using xmlunit
    (existing test was not checking whole DOM tree, only topmost node)
  - tweaked test queries to include xsi:type attributes to help with
    unit-testing checks

 Revision 1.2  2005/03/21 18:31:51  mch
 Included dates; made function types more explicit

 Revision 1.1  2005/02/28 19:36:39  mch
 Fixes to tests

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1  2005/02/17 18:18:59  mch
 Moved in from datacenter project

 Revision 1.6  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.6.1  2004/10/19 17:21:10  mch
 Removed incorrect assertElementsEqual recursion - not yet right

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate


 */


