package org.astrogrid.registry.client.admin;


import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.axis.utils.XMLUtils;
/**
 * Class Name: RegistryAdminDocumentHelper
 * Description: This class is more used for the generation of a Document object based off of a Mapping object (TreeMap)
 * and vice-versa going from a Document Object back to unique strings in a TreeMap.  The primary use for this is on the
 * cocoon portal side.  Cocoon can now take a Document object that it will receive from a server and throw it to this
 * class for autogenerating a TreeMap object which it can then use to generate Form Input variables.
 * 
 * @author Kevin Benson
 * 
 */
public class RegistryAdminDocumentHelper {
  
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
   public static Map createMap(Document doc) {
      Element elem = doc.getDocumentElement();
      NodeList nl = elem.getChildNodes();
      String temp = null;
      LinkedHashMap lhm  = new LinkedHashMap();
      createMap(nl,lhm);
      
      return lhm;
   }
   
   /**
    * Recursive method to create the mapping.  Mainly done by iterating through the NodeList and check if it
    * an Attribute or a Text Node.  If it is one of those then get all the Parent Node names for generating a unique
    * string and get the value then input the unique string and value into a treemap.
    * 
    * @param nl A Nodelist of all the child nodes.
    * @param tm A Treemap Object th contains the mapping of all the unique strings with their values.
    */
   private static void createMap(NodeList nl,Map tm) {
      String temp = null;
      NamedNodeMap nnm = null;
      String val = null;
      //Loop through the childNodes.
      for(int i = 0;i < nl.getLength();i++) {
         Node nd = nl.item(i);
         //check if the current Node has any Attributes or if it is a Text Node type
         if((nnm = nd.getAttributes()) != null) {
            //found an Attribute get all the Parent Nodes.                        
            temp = getParentNodeToString(nd);
            
            //concat the current element node name this is not considered a parent node
            //that is why this must be done.
            temp += "." + nd.getNodeName();
            temp = temp.trim();
            
            //small hack the getParentNodeToString is a small recursion method
            //that will put a leading "." to the string which is not needed.
            if(temp.charAt(0) == '.') {
               temp = temp.replaceFirst(".","");
            }                              
            
            //Now go thorough each attribute creating a unique string
            for(int j = 0; j < nnm.getLength();j++) {
               Node attrNode = nnm.item(j);
               temp += ".attr." + attrNode.getNodeName();

               //now put the unique string and it's value in the map.
               tm.put(temp,attrNode.getNodeValue());
            }           
         }else if(nd.getNodeType() == Node.TEXT_NODE) {
            //found a text node get all it's parent nodes.
            temp = getParentNodeToString(nd);            
            temp = temp.trim();
            if(temp != null && temp.length() > 0) {
               
               //small hack the getParentNodeToString is a small recursion method
               //that will put a leading "." to the string which is not needed.
               if(temp.charAt(0) == '.') {
                  temp = temp.replaceFirst(".","");
               }                              
              
               
               //Get the value of the Node.
               val = nd.getNodeValue();
               if(val != null && val.trim().length() > 0 ){
                  
                  //Okay for multiple occuring elements when maxOccurs = unbounded.
                  //we need to check if this unique string already belongs in the map.
                  //if so then get its value and concatenate the values together with a 
                  //split string.
                  if(tm.containsKey(temp)) {
                     val += "__,__" + (String)tm.get(temp);
                  }
                  //put the entry in the treemap.
                  tm.put(temp,val);
               }
            }
         }
         
         //If the current node has more child nodes then call itself again with those childnodes.
         if(nd.hasChildNodes()) {
            createMap(nd.getChildNodes(),tm);
         }
      }//for      
      
   }

   /**
    * This small recursive method will take a node and recursive concatenate it's parent nodes (names)
    * till it reaches the top most node or it reaches a stop node.
    * Stop Node is just a Node you normally don't go past usually the root node.
    * @param nd Current node to be examined.
    * @return a unique string containing a nodex parents names.
    */   
   private static String getParentNodeToString(Node nd) {
      String nodeString = "";
      if(nd.getParentNode() != null && !nd.getParentNode().getNodeName().equals(STOP_NODE_NAME)) {
         nodeString = nd.getParentNode().getNodeName();
         nodeString = getParentNodeToString(nd.getParentNode()) + "." + nodeString;          
      }
      return nodeString;     
   }
   
   
  /**
   * This method takes in a mapping and creates a Document object model.  Does this by dealing and creating a custom
   * object called SchemaBuilder which has child SchemaBuilders and access to attributes.  Then finally
   * a call to it's generateDocument method in the SchemaBuilder will go through all it's children and attributes
   * generating a Document object.
   * 
   * @param tm A Mapping object filled with unique strings and values.
   * @return A Document object.
   */
   public static Document createDocument(Map tm) {
      Document registryDoc = null;
      try {
         Set st = tm.keySet();
         Iterator iter = st.iterator();
         String key = null;
         String val = null;
         String []elems = null;
         SchemaBuilder sb = new SchemaBuilder(null);
         SchemaBuilder currentsb = sb.findElement(null,null);
         boolean hasElement = false;
         String currentElement = null;
         String prevElement = null;
         while(iter.hasNext()) {
            key = (String)iter.next();
            val = (String)tm.get(key);
            elems = key.split("\\.");
            if(elems.length > 0) {
               for(int i=0;i < elems.length;i++) {
                  currentElement = elems[i];
                  if(i != 0) {
                    prevElement = elems[(i-1)];
                  }else {
                    prevElement = null;
                  }
                  if("attr".equals(elems[i])) { 
                     i++;
                     currentsb.addAttribute(elems[i],val);
                     i = elems.length;                    
                  }else {
                     if(sb.hasElement(currentElement,prevElement)) {
                        
                        hasElement = true;
                        currentsb = sb.findElement(currentElement,prevElement);                                                   
                     }                     
                     if(i != (elems.length - 1)) {
                        if(!hasElement) {                        
                           currentsb.addElement(elems[i]);
                           currentsb = sb.findElement(elems[i],prevElement);
                        }
                     }else {
                        currentsb.addElement(elems[i],val);
                     }
                     hasElement = false;
                  }//else
               }//for
            }//if
         }//while
         registryDoc = sb.generateDocument();
      }catch(Exception e) {
         e.printStackTrace();
      }
      return registryDoc;
   }
      
}



class SchemaBuilder {
   
   private String elementName = null;
   private String val = null;
   private Hashtable elementAttributes = null;
   private Vector childElements = null;
   
   public SchemaBuilder(String elementName) {
      this.elementName = elementName;
      childElements = new Vector(5);
      elementAttributes = new Hashtable();
   }
   
   public SchemaBuilder(String elementName,String val) {
      this.elementName = elementName;
      this.val = val;
      childElements = new Vector(5);
      elementAttributes = new Hashtable();
   }
   
   public String getElementName() {
      return this.elementName;
   }
   
   public String getVal() {
      return this.val;
   }
   
   public void addAttribute(String attrName,String val) {
      elementAttributes.put(attrName,val);     
   }
   
   
   public void addElement(String element) {
      this.addElement(element,val);
   }
   
   public void addElement(String element,String val) {
      if(val != null) {
         String []elementValues = val.split("__,__");
         for(int i = 0;i < elementValues.length;i++) {
            childElements.add(new SchemaBuilder(element,elementValues[i]));
         }//for
      }else {
         childElements.add(new SchemaBuilder(element,val));
      }
   }

/*   
   public SchemaBuilder findElement(String element) {
      SchemaBuilder temp = new SchemaBuilder(element);
      SchemaBuilder recCheck = null;
      if(element == null) {
         return this;
      }
      if(element.equals(this.elementName)) {
         return this;
      }
      for(int i = 0;i < childElements.size();i++) {
         SchemaBuilder current = (SchemaBuilder)childElements.get(i);
         if(temp.equals(current)) {
            return (SchemaBuilder)childElements.get(i);     
         }else {
            if(current.childElements.size() > 0) {
               recCheck = current.findElement(element);
               if(!recCheck.getElementName().equals(null)  && temp.equals(recCheck) ) {
                  return recCheck;
               }
                  
            }
         }
      }
      return this;
   }
  */ 
   /*
   public boolean hasElement(String element,String prevElement) {
      SchemaBuilder temp = new SchemaBuilder(element);
      boolean recValue = false;
      if(element.equals(this.elementName)) {
         return true;
      }
      for(int i = 0;i < childElements.size();i++) {
         SchemaBuilder current = (SchemaBuilder)childElements.get(i);
         if(temp.equals(current)) {
            return true;
         }else {
            if(current.childElements.size() > 0) {
               recValue = current.hasElement(element,prevElement);
               if(recValue) {
                  return true;
               }
            }
         }//else         
      }//for
      return false;
   }
   */
   
   public boolean hasElement(String element,String prevElement) {
      SchemaBuilder temp = new SchemaBuilder(element);
      boolean recValue = false;
      if(element.equals(this.elementName)) {
         return true;
      }
      for(int i = 0;i < childElements.size();i++) {
         SchemaBuilder current = (SchemaBuilder)childElements.get(i);
         if(temp.equals(current)) {
            if(prevElement == null) {
               return true;
            }else if(this.elementName.equals(prevElement)) {
               return true;
            }
         }else {
            if(current.childElements.size() > 0) {
               recValue = current.hasElement(element,prevElement);
               if(recValue) {
                  return true;
               }
            }
         }//else         
      }//for
      return false;
   }
   
   public SchemaBuilder findElement(String element,String prevElement) {
      SchemaBuilder temp = new SchemaBuilder(element);
      SchemaBuilder recCheck = null;
      if(element == null) {
         return this;
      }
      if(element.equals(this.elementName)) {
         return this;
      }
      for(int i = 0;i < childElements.size();i++) {
         SchemaBuilder current = (SchemaBuilder)childElements.get(i);
         if(temp.equals(current)) {
            if(prevElement == null) {
               return (SchemaBuilder)childElements.get(i);
            }else if(this.elementName.equals(prevElement)) {
               return (SchemaBuilder)childElements.get(i);   
            }                 
         }else {
            if(current.childElements.size() > 0) {
               recCheck = current.findElement(element,prevElement);
               if(!recCheck.getElementName().equals(null)  && temp.equals(recCheck) ) {
                  return recCheck;
               }
                  
            }
         }
      }
      return this;
   }
   
   


   
   public boolean equals(SchemaBuilder sb) {
     if(this.elementName.equals(sb.getElementName()) ) {
         return true;   
     }
     return false;
   }
   
   public Document generateDocument() throws Exception {
      DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document registryDoc = registryBuilder.newDocument();
      Element root = registryDoc.createElement("VODescription");
      registryDoc.appendChild(root);
      System.out.println("the size of childelements = " + childElements.size());
      if(childElements.size() > 0) {
         for(int i = 0; i < childElements.size();i++) {
            SchemaBuilder sb = (SchemaBuilder)childElements.get(i);
               sb.generateChildDocument(registryDoc,root);
         }
      }      
      //generateChildDocument(registryDoc,root);
      return registryDoc;
   }
   
   public void generateChildDocument(Document doc,Element currentElement) throws Exception {

      Element elem = doc.createElement(this.elementName);
       
      if(val != null) {
         elem.appendChild(doc.createTextNode(val));
      }
      Enumeration enum = elementAttributes.keys();
      while(enum.hasMoreElements()) {
         String attrKey = (String)enum.nextElement();
         String attrVal = (String)elementAttributes.get(attrKey);
         elem.setAttribute(attrKey,attrVal);
      }
      if(childElements.size() > 0) {
         for(int i = 0; i < childElements.size();i++) {
            SchemaBuilder sb = (SchemaBuilder)childElements.get(i);
            sb.generateChildDocument(doc,elem);
         }
      }
      currentElement.appendChild(elem);      
   }

   
}
