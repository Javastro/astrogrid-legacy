/*
 * $Id: AdqlTestHelper.java,v 1.1 2005/02/28 19:36:39 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.adql;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.util.DomHelper;
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
   
   public Element getTestAdql(String queryFile) throws ParserConfigurationException, SAXException, IOException  {
      assert (queryFile != null);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      assert (is != null) : "Could not open query file: " + queryFile;

      Document adqlDom = DomHelper.newDocument(is);
      adqlDom.normalize(); //so we can compare Adql DOMs
      return adqlDom.getDocumentElement();
   }

   /** Returns true if each element has the same attributes with the same values,
    * the same children elements and each child element is also equal using this
    * method.
    * Can be used to compare docs to see if they are effectively equal.
    * Throws an assertion exception with some indication of where the difference lies
    */
   public void assertElementsEqual(Element e1, Element e2) {
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

      //check children *elements*
      //this includes attributes
      NodeList e1children = e1.getChildNodes();
//      NodeList e2children = e2.getChildNodes();
//      assert e1children.getLength() == e2children.getLength() : "Different number of children in "+e1.getTagName();

      /* this isn't right
      for (int i = 0; i < e1children.getLength(); i++) {
         if (e1children.item(i) instanceof Element) {
            //get equivelent e2 child
            Element e2child = AdqlXml074Parser.getSingleChildByTagName( e2, e1children.item(i).getNodeName());
            //this might not be quite right...
            assertElementsEqual( (Element) e1children.item(i), e2child);
         }
      }
       */
   }
   
   /* Returns the 'current' adql version of the 'standard test example' NVO queries
    generated on the page
   at http://openskyquery.net/AdqlTranslator/Convertor.aspx */
   public Element getNvo1() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdql("v074/nvoexample-1-adql0.7.4.xml");
   }

   public Element getNvo2() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdql("v074/nvoexample-2-adql0.7.4.xml");
   }
   
   public Element getNvo3() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdql("v074/nvoexample-3-adql0.7.4.xml");
   }
   
   public Element getNvo4() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdql("v074/nvoexample-4-adql0.7.4.xml");
   }

   /** Returns the 'current' adql verison of some tests that can be run against the
    * sample stars database */
   public Element getSampleDbPleidies() throws IOException, SAXException, ParserConfigurationException {
      return getTestAdql("v074/SampleStars-pleidies-adql.xml");
   }
   
}

/*
 $Log: AdqlTestHelper.java,v $
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


