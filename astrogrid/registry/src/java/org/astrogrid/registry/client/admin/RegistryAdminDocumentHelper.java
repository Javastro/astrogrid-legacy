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
      createMap(nl,lhm,"");
      
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
   private static void createMap(NodeList nl,Map tm,/*int parentLoopCounter,*/ String parentStrings) {
      String temp = null;
      NamedNodeMap nnm = null;
      String val = null;
      boolean hadAttributes = false;
      //Loop through the childNodes.
      for(int i = 0;i < nl.getLength();i++) {
         hadAttributes = false;
         Node nd = nl.item(i);
         //check if the current Node has any Attributes or if it is a Text Node type
         if((nnm = nd.getAttributes()) != null) {
            hadAttributes = true;
            //Okay Attributes go ahead and get the node/element it lives on and make the new parent strigns
            parentStrings += nd.getNodeName() + String.valueOf(i) + "/";
            //Okay don't touch the parent strings anymore so make a temp of the parent strings.
            temp = new String(parentStrings);
            String attrTemp = temp;

            //Now go thorough each attribute creating a unique string
            for(int j = 0; j < nnm.getLength();j++) {
               Node attrNode = nnm.item(j);
               tm.put(temp + "attr/" + attrNode.getNodeName(),attrNode.getNodeValue());
            }           
         }else if(nd.getNodeType() == Node.TEXT_NODE) {
            //found a text node get all it's parent nodes.
            parentStrings = parentStrings.trim();
            //make sure it does have a parent which should always happen.
            if(parentStrings != null && parentStrings.length() > 0) {
               
               //Get the value of the Node.
               val = nd.getNodeValue();
               //Sometiems the DOM parse makes empty text nodes for newlines so make sure we have a real
               //value to the text node.
               if(val != null && val.trim().length() > 0 ){
                  tm.put(parentStrings,val);
               }//if
            }//if
         }//elseif
         
         //If the current node has more child nodes then call itself again with those childnodes.
         if(nd.hasChildNodes()) {
            String newNodeEndingString = "";
            //if we had attributes earlier then the current nodename/element is already on the parent strings.
            if(!hadAttributes)
               newNodeEndingString = nd.getNodeName() + String.valueOf(i) + "/"; 
            //add the new parent elements to the parentstrings.
            parentStrings += newNodeEndingString;
            
            //Now call itself with the child nodes.
            createMap(nd.getChildNodes(),tm,parentStrings);
            //Okay we came back from a recursive call so get rid of the previous node name.
            //this makes sure our parent strings go back up the tree.
            parentStrings = parentStrings.substring(0,parentStrings.indexOf(nd.getNodeName() + String.valueOf(i)));
         }//if
      }//for
   }

   
  /**
   * This method takes in a mapping and creates a Document object model.  Does this by dealing and creating a custom
   * object called SchemaBuilder which has child SchemaBuilders and access to attributes.  Then finally
   * a call to it's generateDocument method in the SchemaBuilder will go through all it's children and attributes
   * generating a Document object. 
   * THIS METHOD WILL NEED TO BE REWRITTEN.
   * Because ran into trouble that I really needed to look back at all the parents above a current node.
   * If it only needed to lookup once it was not to bad, but because of the Registry it really need to go
   * look up to 2 parents.  Which caused a small hack for the moment.  Need to rework this.
   * 
   * @param tm A Mapping object filled with unique strings and values.
   * @return A Document object.
   */
   public static Document createDocument(Map tm) {
      Document registryDoc = null;
      try {
         //get the key set to the map.
         Set st = tm.keySet();
         //And get an iterator for the keys.
         Iterator iter = st.iterator();
         
         String key = null;
         String val = null;
         String []elems = null;
         //Okay initialize the first Schema Builder to nothing but nulls.  This is just a
         //reference point.         
         SchemaBuilder sb = new SchemaBuilder(null,null,null);
         //Also set the current schema bulder to the first one.
         SchemaBuilder currentsb = sb.findElement(null,null,null);
         
         boolean hasElement = false;
         String currentElement = null;
         String prevElement = null;
         String prevElement2 = null;
         //start going thourgh all the keys.
         while(iter.hasNext()) {
            key = (String)iter.next();
            //get the values to the key.
            val = (String)tm.get(key);
            //Now split the keys into their strings based off of a "/"
            elems = key.split("\\/");
            if(elems.length > 0) {
               //Go through all the various elements.
               for(int i=0;i < elems.length;i++) {
                  
                  currentElement = elems[i];
                  //Okay we need to get some parent elements/strings.
                  if(i != 0) {
                    prevElement = elems[(i-1)];
                    if( (i-1) != 0) {
                       prevElement2 = elems[(i-2)];
                    }else {
                       prevElement2 = null;
                    }
                  }else {
                    prevElement = null;
                    prevElement2 = null;
                  }
                  //we ran into an attribute.
                  if("attr".equals(elems[i])) {
                     //So add the next element string which is the attribute name
                     //to the current schema builder.
                     //current schemabuilder should have the element that goes with
                     //this attribute. 
                     i++;
                     currentsb.addAttribute(elems[i],val);
                     //Go ahead and go to the next string.
                     i = elems.length;                    
                  }else {
                     //See if this element is already in the schema builder objects.
                     if(sb.hasElement(currentElement,prevElement,prevElement2)) {
                        //it is so set the current schema builder to it.
                        hasElement = true;
                        currentsb = sb.findElement(currentElement,prevElement,prevElement2);                                                   
                     }                     
                     if(i != (elems.length - 1)) {
                        //if were not on the last string where the value goes then
                        //if the element does not exist yet then add it as a 
                        //child schema builder object to the current schema builder object.
                        if(!hasElement) {                        
                           currentsb.addElement(elems[i],prevElement);
                           currentsb = sb.findElement(elems[i],prevElement,prevElement2);
                        }
                     }else {
                        //we are on the last string. which has a value to it.
                        currentsb.addElement(elems[i],val,prevElement2);
                     }
                     //reset the hasElement to false were going to the next string in the list.
                     hasElement = false;
                  }//else
               }//for
            }//if
         }//while
         
         //finaly let the Schema builder generate the DOM model.
         registryDoc = sb.generateDocument();
      }catch(Exception e) {
         e.printStackTrace();
      }
      return registryDoc;
   }
      
}


/**
 * A Schema builder class that handles holding it's current element name and it's
 * attributes along with child objects which are also Schema Builder objects. 
 * @author Kevin Benson
 * 
 */
class SchemaBuilder {
   
   private String elementName = null;
   private String val = null;
   private Hashtable elementAttributes = null;
   private Vector childElements = null;
   private String parentElement = null;
   
   /**
    * Constructure that sets the current element name and it's parent element name.
    * @param elementName the element name
    * @param parentElement the parent to the element name if any.
    */
   public SchemaBuilder(String elementName,String parentElement) {
      this(elementName,null,parentElement);
   }
   
   /**
    * Main constructor that sets the current element, the value if any and a parent element if any.
    * @param elementName the elemnt name
    * @param val value for this element if any.
    * @param parentElement the parent element name if any.
    */
   public SchemaBuilder(String elementName,String val, String parentElement) {
      this.elementName = elementName;
      this.val = val;
      this.parentElement = parentElement;
      childElements = new Vector(5);
      elementAttributes = new Hashtable();
   }
   
   /**
    * Returns the current Element name.
    * @return the element name.
    */
   public String getElementName() {
      return this.elementName;
   }
   
   /**
    * Returns the parent element name
    * @return the paent element name.
    */
   public String getParentElement() {
      return this.parentElement;
   }
   
   /**
    * Property to return the Value.
    * @return the value
    */
   public String getVal() {
      return this.val;
   }
   
   /**
    * Adds an attribute name and value to the Hashtable.  
    * @param attrName the name of the attribute.
    * @param val value of the attribute.
    */
   public void addAttribute(String attrName,String val) {
      elementAttributes.put(attrName,val);     
   }
   
   /**
    * Adds a new child Schema Builder object to this element.
    * @param element
    * @param parent
    */
   public void addElement(String element,String parent) {
      this.addElement(element,null,parent);
   }
   
   /**
    * Adds a new child Schema Builder object to this element.
    * @param element element name.
    * @param val value of the element if any.
    * @param parent parent element if any.
    */
   public void addElement(String element,String val,String parent) {
      childElements.add(new SchemaBuilder(element,val,parent));
   }

   /**
    * Checks if the current same element exists already in all the schema objects.
    * Recursively looking through all the child Schema Builder Objects.
    * @param element main element to look for.
    * @param prevElement check the previous element (parent)
    * @param parent check another prevous element (grandpa)
    * @return true/false if the element exits.
    */
   public boolean hasElement(String element,String prevElement,String parent) {
      SchemaBuilder temp = new SchemaBuilder(element,null,parent);
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
               if(this.parentElement == null) {
                  return true;
               }else if(this.parentElement.equals(parent)) {
                  return true;   
               }               
            }
         }else {
            if(current.childElements.size() > 0) {
               recValue = current.hasElement(element,prevElement,parent);
               if(recValue) {
                  return true;
               }
            }
         }//else         
      }//for
      return false;
   }

   /**
    * Returns the Schema Builder object that matches this exact same element.
    * Recursively looking through all the child Schema Builder Objects.
    * @param element main element to look for.
    * @param prevElement check the previous element (parent)
    * @param parent check another prevous element (grandpa)
    * @return true/false if the element exits.
    */   
   public SchemaBuilder findElement(String element,String prevElement,String parent) {
      SchemaBuilder temp = new SchemaBuilder(element,null,parent);
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
               if(this.parentElement == null) {
                  return (SchemaBuilder)childElements.get(i);
               }else if(this.parentElement.equals(parent)) {                  
                  return (SchemaBuilder)childElements.get(i);      
               }                  
            }                 
         }else {
            if(current.childElements.size() > 0) {
               recCheck = current.findElement(element,prevElement,parent);
               if(!recCheck.getElementName().equals(null)  && temp.equals(recCheck) ) {
                  return recCheck;
               }
                  
            }
         }
      }
      return this;
   }

   /**
    * Equals method that looks only at the elment name.  Probably needs to be reworked to look at parent name.
    * Then could remove some stuff from the find and has methods.
    * @param sb
    * @return
    */   
   public boolean equals(SchemaBuilder sb) {
     if(this.elementName.equals(sb.getElementName()) ) {
         return true;   
     }
     return false;
   }
   
   /**
    * Generates a new DOM object based off of the Schema builder.
    * @return DOM document object.
    * @throws Exception
    */
   public Document generateDocument() throws Exception {
      DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document registryDoc = registryBuilder.newDocument();
      Element root = registryDoc.createElement("VODescription");
      root.setAttribute("xmlns","http://www.ivoa.net/xml/VOResource/v0.9");
      root.setAttribute("xmlns:vr","http://www.ivoa.net/xml/VOResource/v0.9");
      root.setAttribute("xmlns:vc","http://www.ivoa.net/xml/VOCommunity/v0.2");
      root.setAttribute("xmlns:vg","http://www.ivoa.net/xml/VORegistry/v0.2");
      root.setAttribute("xmlns:vs","http://www.ivoa.net/xml/VODataService/v0.4");
      root.setAttribute("xmlns:vt","http://www.ivoa.net/xml/VOTable/v0.1");
      root.setAttribute("xmlns:cs","http://www.ivoa.net/xml/ConeSearch/v0.2");
      root.setAttribute("xmlns:sia","http://www.ivoa.net/xml/SIA/v0.6");
      root.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
      root.setAttribute("xsi:schemaLocation","http://www.ivoa.net/xml/VOResource/v0.9 " +
      "http://www.ivoa.net/xml/VOResource/VOResource-v0.9.xsd " +
      "http://www.ivoa.net/xml/VOCommunity/v0.2 " +
      "http://www.ivoa.net/xml/VOCommunity/VOCommunity-v0.2.xsd " +
      "http://www.ivoa.net/xml/VORegistry/v0.2" +
      " http://www.ivoa.net/xml/VORegistry/VORegistry-v0.2.xsd" +
      "  http://www.ivoa.net/xml/ConeSearch/v0.2" +
      "  http://www.ivoa.net/xml/ConeSearch/ConeSearch-v0.2.xsd" +
      " http://www.ivoa.net/xml/SIA/v0.6" +
      " http://www.ivoa.net/xml/SIA/SIA-v0.6.xsd");
      //root.setAttributeNS("http://www.ivoa.net/xml/VOResource/v0.9","xmlns","http://www.ivoa.net/xml/VOResource/v0.9");
      //root.setAttributeNS("http://www.ivoa.net/xml/VOResource/v0.9","xmlns:vr","http://www.ivoa.net/xml/VOResource/v0.9");
      //root.setAttributeNS("http://www.ivoa.net/xml/VOCommunity/v0.2","xmlns:vc","http://www.ivoa.net/xml/VOCommunity/v0.2");
      //root.setAttributeNS("http://www.ivoa.net/xml/VORegistry/v0.2","xmlns:vg","http://www.ivoa.net/xml/VORegistry/v0.2");
      registryDoc.appendChild(root);
      if(childElements.size() > 0) {
         for(int i = 0; i < childElements.size();i++) {
            SchemaBuilder sb = (SchemaBuilder)childElements.get(i);
               sb.generateChildDocument(registryDoc,root);
         }
      }
      return registryDoc;
   }
   
   /**
    * Recursivley calls its child nodes to build up and append the appropriate elment names and attributes to add to the
    * DOM object. Recusively builds up the elements from the leaf node back up to the top before then adding it to the DOM.
    * @param doc document that is being updated.
    * @param currentElement curent element name
    * @throws Exception
    */
   public void generateChildDocument(Document doc,Element currentElement) throws Exception {
      String strElementName = null;
      //Okay Normally the Schema Builder objects create unique names to their elements
      //by concatenating digits to the end of their names.  These digits now need to be
      //taken off.
      if(Character.isDigit(this.elementName.charAt(this.elementName.length()-1)))
         this.elementName = this.elementName.substring(0,this.elementName.length()-1);
      if(Character.isDigit(this.elementName.charAt(this.elementName.length()-1)))
         this.elementName = this.elementName.substring(0,this.elementName.length()-1);
      if(Character.isDigit(this.elementName.charAt(this.elementName.length()-1)))
         this.elementName = this.elementName.substring(0,this.elementName.length()-1);
      
      //create the element name and attach to the DOM      
      Element elem = doc.createElement(this.elementName);
      
      //create a text node to this element. 
      if(val != null) {
         elem.appendChild(doc.createTextNode(val));
      }
      //Get the attributes to this element if their are any.
      Enumeration enum = elementAttributes.keys();
      while(enum.hasMoreElements()) {
         String attrKey = (String)enum.nextElement();
         String attrVal = (String)elementAttributes.get(attrKey);
         //now set these attributes to the current element.
         elem.setAttribute(attrKey,attrVal);
      }
      //Check if their are any more child objects and build their structure as well
      //by recalling this same method.
      if(childElements.size() > 0) {
         for(int i = 0; i < childElements.size();i++) {
            SchemaBuilder sb = (SchemaBuilder)childElements.get(i);
            sb.generateChildDocument(doc,elem);
         }
      }
      //finally append the element to the document.
      currentElement.appendChild(elem);      
   }
  
}