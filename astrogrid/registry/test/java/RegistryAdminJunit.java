//package org.astrogrid.registry.client.admin;


import java.util.TreeMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import org.apache.axis.utils.XMLUtils;
import junit.framework.*;
import org.astrogrid.registry.client.admin.RegistryAdminDocumentHelper;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import java.io.Reader;
import java.io.StringReader;
import java.io.File;
import org.xml.sax.InputSource;
import java.io.IOException;
import org.xml.sax.SAXException;


public class RegistryAdminJunit extends TestCase {
   
   
    
   public RegistryAdminJunit(String name) {
      super(name);
   } 
  
   public static void main(String[] args) { 
      junit.textui.TestRunner.run(suite());
   } 
    
   public static Test suite() {
      return new TestSuite(RegistryAdminJunit.class);
   }
   
   public void testUpdateOrganisation() throws Exception {
      String fileName = System.getProperty("junit.xml.dir");
      fileName += "/" + "Organisation1.xml";
      File fi = new File(fileName);
      
      RegistryAdminService ras = new RegistryAdminService();
      String requestQuery = " ";      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      DocumentBuilder registryBuilder = null;
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      registryBuilder = dbf.newDocumentBuilder();
      System.out.println("ns aware = " + registryBuilder.isNamespaceAware());
      Document doc = registryBuilder.parse(fi);
      Element elem = doc.getDocumentElement();
      Node e = elem.getFirstChild().getNextSibling();
      System.out.println("ndname = " + e.getNodeName() + " pref = " + e.getPrefix() + " locname = " + e.getLocalName());
      Node e2 = e.getFirstChild().getNextSibling();
      System.out.println("ndname = " + e2.getNodeName() + " pref = " + e2.getPrefix() + " locname = " + e2.getLocalName());
      
      ras.update(doc);
   }
   
   public void testAddOrganisation() throws Exception {
      String fileName = System.getProperty("junit.xml.dir");
      fileName += "/" + "Organisation2.xml";
      File fi = new File(fileName);
      
      RegistryAdminService ras = new RegistryAdminService();
      String requestQuery = " ";      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(fi);
      
      ras.update(doc);
   }
   
   public void testRemoveOrganisation() throws Exception {
      String fileName = System.getProperty("junit.xml.dir");
      fileName += "/" + "Organisation2.xml";
      File fi = new File(fileName);
      
      RegistryAdminService ras = new RegistryAdminService();
      String requestQuery = " ";      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(fi);
      
      ras.remove(doc);
   }
   
   public void testAdminConversionOrganisation() throws Exception {
      String fileName = System.getProperty("junit.xml.dir");
      fileName += "/" + "Organisation2.xml";
      File fi = new File(fileName);
      
      RegistryAdminService ras = new RegistryAdminService();
      String requestQuery = " ";      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      DocumentBuilder registryBuilder = null;
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      registryBuilder = dbf.newDocumentBuilder();
      Document doc = registryBuilder.parse(fi);
      System.out.println("okay create the Treemap " );
      Map docMap = RegistryAdminDocumentHelper.createMap(doc);
      System.out.println("here and treemap size = " + docMap.size());
      printMap(docMap);
      Document convertBackDoc = RegistryAdminDocumentHelper.createDocument(docMap);
      System.out.println("convertedbackdoc = " + XMLUtils.DocumentToString(convertBackDoc));
   }
   
   private void printMap(Map tm) {
      Set keySet = tm.keySet();
      Iterator iter = keySet.iterator();
      String key = null;
      while(iter.hasNext()) {
         key = (String)iter.next();
         System.out.println(" The key = " + key + " The value = " + (String)tm.get(key));
      }//while      
   }
}
