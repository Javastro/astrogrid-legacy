package org.astrogrid.registry.client.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.registry.common.versionNS.IRegistryInfo;
import org.astrogrid.registry.common.RegistryHelper;

/**
 * Class Name: RegistryAdminDocumentHelper
 * Description: This class is more used for the generation of a Document object based off of a Mapping object (TreeMap)
 * and vice-versa going from a Document Object back to unique strings in a TreeMap.  The primary use for this is on the
 * cocoon portal side.  Cocoon can now take a Document object that it will receive from a server and throw it to this
 * class for autogenerating a TreeMap object which it can then use to generate Form Input variables.
 * @todo - there's a lot in this class. need to check it is being used in portal / elsewhere - if so spend some time polishing up a bit, otherwise remove it.
 * @author Kevin Benson
 * 
 */
public class RegistryAdminDocumentHelper {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(RegistryAdminDocumentHelper.class);
  
  /**
   * @This is used to STOP a recursion process of generating a unique string.
   */
   private static final String STOP_NODE_NAME = "VODescription";
  
   /**
    * Empty Constructor
    *
    */
   public RegistryAdminDocumentHelper() {
     
   } 
  
   /**
    * Where the creation of the Map begins from a Document object. First it will create the empty TreeMap object then
    * grab all the ChildNodes from the Document and then pass it to a recursive method for the generation of mapping.
    * 
    * @param doc The Document Object to be analysed and parsed into unique strings
    * @return A TreeMap which is a mapping of unique strings to their values in the document object.
    */
   public Map createMap(Document doc) {
      Element elem = doc.getDocumentElement();
      NodeList nl = elem.getChildNodes();
      String temp = null;
      LinkedHashMap lhm  = new LinkedHashMap();
      //createMap(nl,lhm,"");
      processNode(doc.getDocumentElement(),lhm,"");
      //printMap(lhm);
      return lhm;
   }
   
   public void processNode(Node currentNode,Map mp,String parentStrings) {
       
       switch( currentNode.getNodeType()) {
           case Node.ELEMENT_NODE:
               System.out.println("element node found = " + currentNode.getNodeName());
               NamedNodeMap attributeNodes = currentNode.getAttributes();
               
               for(int i = 0;i < attributeNodes.getLength();i++) {
                   Attr attribute = (Attr) attributeNodes.item(i);
                   //if(attribute.getPrefix() != null) {
                   //    mp.put(parentStrings + "/@" + attribute.getPrefix() + ":" + attribute.getName(),null);
                   //}else {
                       mp.put(parentStrings + currentNode.getNodeName() + "/@" + attribute.getName(),null);
                   //}
               }
               //if(currentNode.getPrefix() != null) {
               //    parentStrings += currentNode.getPrefix() + ":" +  currentNode.getNodeName() + "/";
               //}else {
                   parentStrings += currentNode.getNodeName() + "/";
               //}
                
               processChildNodes(currentNode.getChildNodes(),mp,parentStrings);
           break;
           case Node.TEXT_NODE:
               mp.put(parentStrings,null);
               System.out.println("the text node of it");
           break;
       }
   }
   
   public void processChildNodes(NodeList children,Map mp,String parentStrings) {
       for(int i = 0;i < children.getLength();i++)
           processNode(children.item(i),mp,parentStrings );
   }
   
   public void printMap(Map mp) {
      Set st = mp.keySet();
      Iterator iter = st.iterator();
      while(iter.hasNext()) {
          System.out.println("the Key = " + iter.next());
      }
   }
   
      
}