package org.astrogrid.registry.server;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import org.apache.axis.utils.XMLUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import org.astrogrid.registry.server.query.RegistryService;
import org.astrogrid.registry.common.versionNS.IRegistryInfo;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import org.astrogrid.registry.common.RegistryHelper;

import org.astrogrid.config.Config;



/**
 * RegistryFile Helper class will have some of the methods placed in different classes in the next iteration.
 * The main purpose of this class is to help with the Registry File.  Currently handles 3 or 4 different purposes 
 * and again will need to have those purposes moved to different classes at a later stage.
 * It handles update & removals of the registry.  Looking up Registry Nodes, Replacing and Adding Registry Nodes
 * through the DOM model. Handles loading and writing the registry file.  And finally does a little bit of a "workaround"
 * around the translation of prefixes in the DOM model.
 * @author Kevin Benson
 *
 */
public class RegistryFileHelper {


   private static final String REGISTRY_VERSION_PROPERTY = "org.astrogrid.registry.version";
   
   private static final String REGISTRY_FILE_DOM_PROPERTY = "org.astrogrid.registry.file";   
   
   private static final String WRITEFILE_TIME_DELAY_MINUTES_PROPERTY = "org.astrogrid.registry.writefile.timedelay.minutes";

   /**
    * The main Registry Document.
    */   
   private static Document registryDocument;
   
   private static LinkedHashMap registryHash;
   
   private static String statusMessage = "";
   
   private static LinkedList ll = null;
   
   private static HashMap manageAuthorities = null;
   
   private static Calendar nextWriteTime = null;
   
   public static Config conf = null;
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
   
         
   public static void initStatusMessage() {      
      statusMessage = "Current Registry Version = ";
      statusMessage += conf.getString(REGISTRY_VERSION_PROPERTY) + "\r\n";
      statusMessage += "Current Registry Size = " + loadRegistryTable().size() + "\r\n";
      
      RegistryService rs = new RegistryService();
      Document regEntry = null;
      try {
         regEntry = rs.loadRegistry(null);
      }catch(Exception e) {
         e.printStackTrace();
         regEntry = null;   
      }
      if(regEntry != null) {         
         NodeList nl = regEntry.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","ManagedAuthority" );
         if(nl.getLength() > 0) {
            statusMessage += "Authorities owned by this Registry: |";
            for(int i=0;i < nl.getLength();i++) {
               statusMessage += nl.item(i).getFirstChild().getNodeValue() + "|";
            }
         }
         nl = regEntry.getElementsByTagName("AccessURL");
         if(nl.getLength() > 0) {
            statusMessage += "This Registries access url is " + nl.item(0).getFirstChild().getNodeValue() + "|";
         }
      }//if
   }
   
   private static void checkAndWriteFile() {
      Calendar current = Calendar.getInstance();
      if(nextWriteTime == null) {
         int val = conf.getInt(WRITEFILE_TIME_DELAY_MINUTES_PROPERTY);
         nextWriteTime = Calendar.getInstance();
         if(val <= 1) {
            val = 20;
         }
         nextWriteTime.add(Calendar.MINUTE,val);
      }
      if(current.after(nextWriteTime)) {
         writeRegistryFile();
      }
   }
   
   public static void addStatusMessage(String message) {
      if(ll == null || ll.size() <= 0) {
         ll = new LinkedList();
      }
      Calendar rightNow = Calendar.getInstance();
      ll.add(0,"Time Entry at: " + DateFormat.getDateInstance().format(rightNow.getTime()) + " " + message + "|");
      while(ll.size() >= 51) {
         ll.removeLast();
      }
   }
   
   public static String getStatusMessage() {
      if(ll == null || ll.size() <= 0) {
         ll = new LinkedList();
      }
      initStatusMessage();
      for(int i = 0;i < ll.size();i++) {
         statusMessage += ll.get(i);
      }
      return statusMessage;
   }

   /**
    * Method to translate a Node's previs to another prefix.  Read the translateRegistry method for explanation.
    * @param nd The Node to review the prefix.
    * @param oldPrefix The old prefix value.
    * @param newPrefix The new prefix value.
    * 
    * @author Kevin Benson 
    */ 
   public static void translationWalker(Node nd,String oldPrefix, String newPrefix) {
      Node child = nd.getFirstChild();
      while(child != null) {
         if(oldPrefix.equals(child.getPrefix())) {
            nd.setPrefix(newPrefix);
         }
         translationWalker(child,oldPrefix,newPrefix);
         child = child.getNextSibling();
      }//while
   }//translationWalker
 
   /**
    * Currently the way the registry is stored in a flat file everything has a set prefix.  But the Registry when doing
    * harvesting, must handle other registry entries that have a different prefix.  Having a different prefix and storing
    * it in our set prefix registry file does not work.  So this method was created to go through a Registry Document if
    * needed and translate the prefixes of the registry.  side note: Most harvesting will result in the prefix's being already
    * set to what we desired.  
    * @param query Document to be reviewed and placed into our set prefix Registry.
    * 
    * @author Kevin Benson
    */
   public static void translateRegistry(Document query) {
      
      String strQuery = XMLUtils.DocumentToString(query);
      Node root = null;
      root = findElementNode("VODescription",query.getDocumentElement());
      if(root == null) {
         root = findElementNode("VOResource",query.getDocumentElement());
      }
      //if it is still null then an error will need to happen.
      NamedNodeMap nnm = root.getAttributes();
      String val = null;
      HashMap hm = new HashMap();
      hm.put("http://www.ivoa.net/xml/VOCommunity/v0.2","vc");
      hm.put("http://www.ivoa.net/xml/VOResource/v0.9","vr");
      hm.put("http://www.ivoa.net/xml/VORegistry/v0.2","vg");
      hm.put("http://www.ivoa.net/xml/VODataService/v0.4","vs");
      hm.put("http://www.ivoa.net/xml/VOTable/v0.1","vt");
      hm.put("http://www.ivoa.net/xml/ConeSearch/v0.2","cs");
      hm.put("http://www.ivoa.net/xml/SIA/v0.6","sia");
      String newPrefix = null;
      String oldPrefix = null;      
      for(int i = 0;i < nnm.getLength();i++) {
         Node nd = nnm.item(i);
         val = nd.getNodeValue();
         if((newPrefix = (String)hm.get(val)) != null) {
           oldPrefix = nd.getLocalName();
           System.out.println("The oldpref = " + oldPrefix + " new pref = " + newPrefix);
           if(oldPrefix != null && newPrefix != null && !oldPrefix.equals(newPrefix)) {
              //translationWalker(root,oldPrefix,newPrefix);
           }//if            
         }//if
      }
   }
   
   /**
    * Loads the registry flat file base. Synchronized so users are using the registry file in memory and not reloading the
    * registry each time.
    * @return a DOM document representing the registry.
    * 
    * @author Kevin Benson
    */
   public static synchronized Document loadRegistryFile() {
      
      //check if we already have the registry dom model. and return it
      if(registryDocument != null) {
         return registryDocument;  
      }
      //Don't have it so find the registry file from our configuration.
      registryDocument = conf.getDom(REGISTRY_FILE_DOM_PROPERTY);
      doManageAuthorities();
      return registryDocument;
   }
   
   public static synchronized Map loadRegistryTable() {
      if(registryHash != null && registryHash.size() > 0) {
         return registryHash;   
      }
      
      Document doc = loadRegistryFile();
      if(doc == null) {
         return null;   
      }
      Element rootElem = doc.getDocumentElement();
      Node authTemp = null;
      Node resKeyTemp = null;
      String key = null;
      if(rootElem.hasChildNodes()) {
         NodeList nl = rootElem.getChildNodes();
         registryHash = new LinkedHashMap(nl.getLength());
         for(int i = 0;i < nl.getLength();i++) {
            authTemp = findElementNode("AuthorityID",nl.item(i));
            if(authTemp != null && authTemp.hasChildNodes())
               key = authTemp.getFirstChild().getNodeValue();
            resKeyTemp = findElementNode("ResourceKey",nl.item(i));
            if(resKeyTemp != null && resKeyTemp.hasChildNodes())
               key += "/" + resKeyTemp.getFirstChild().getNodeValue();
            if(key != null) {
               registryHash.put(key,nl.item(i));
            }
            key = null;
         }//for
      }//if
      return registryHash;
   }
   
   /*
   private static synchronized Document createDocument(Object []keys) {
      RegistryConfig.loadConfig();
      IRegistryInfo iri = null;
      Document doc = null;
      Map regHash = loadRegistryTable();
      if(regHash == null) {
         return registryDocument;
      }
      NodeList nl = registryDocument.getDocumentElement().getChildNodes();
      Set st = regHash.keySet();
      Object []keySet = st.toArray();
      String key = null;
      Node nd = null;
      String lookupKey = null;
      final int nodeListLength = nl.getLength();
      for(int j = 0;j < keys.length;j++) {
         lookupKey = (String)keys[j];
         for(int i = (keySet.length - 1);i > 0;i--) {
            key = (String)keySet[i];
            if(lookupKey.equals(key)) {
               if(i >= (nodeListLength - 2)) {
                  nd = (Node)regHash.get(key);
                  //System.out.println("Addnode3 nodelist length = " + nodeListLength + " and i = " + i);
                  System.out.println("New Node Added = " + XMLUtils.ElementToString((Element)nd));
                  addNode(registryDocument,nd);
                  i = 0;
               }else {
                  Node oldNode = nl.item(i+1);
                  //registryDocument.getDocumentElement().removeChild(nd);
                  Node newNode = (Node)regHash.get(key);
                  //addNode(registryDocument,nd);
                  //System.out.println("Lookup Key3 = " + lookupKey + " and key = " + key + " and i = " + i + " keyset length = " + keySet.length);
                  //System.out.println("nodelist length = " + nodeListLength);
                  System.out.println("Replacing document oldNode = " + XMLUtils.ElementToString((Element)oldNode));
                  System.out.println("New Node = " + XMLUtils.ElementToString((Element)newNode));                  
                  replaceNode(registryDocument,newNode,oldNode);
                  i = 0;
               }//else
            }//if
         }//for
      }//for         
      return registryDocument;   
   }
   */
   
   private static synchronized Document createDocument(Object []keys) {
      IRegistryInfo iri = null;
      Map regHash = loadRegistryTable();
      if(regHash == null) {
         return registryDocument;
      }
      iri = RegistryHelper.loadRegistryInfo();
      Document doc = iri.getDocument();
      
      NodeList nl = registryDocument.getDocumentElement().getChildNodes();
      Set st = regHash.keySet();
      Object []keySet = st.toArray();
      String key = null;
      for(int i = 0;i < keySet.length; i++) {
         key = (String)keySet[i];
         addNode(doc,(Node)regHash.get(key));         
      }
      registryDocument = null;
      registryDocument = doc;
      return registryDocument;   
   }
   
   
   
   /**
    * Writes the current registry file in memory to the original flat file. 
    *
    * @author Kevin Benson
    */
   public static synchronized void writeRegistryFile() {
      //Don't have it so find the registry file from our configuration.
      File registryFile = new File(conf.getString(REGISTRY_FILE_DOM_PROPERTY));
      try {
         FileOutputStream fos = new FileOutputStream(registryFile,false);
         XMLUtils.DocumentToStream(loadRegistryFile(),fos);                  
      }catch(FileNotFoundException e) {
         e.printStackTrace();
      }
   }
   
   /**
    * DOM does not let you easily add child nodes from one DOM to another DOM.  This method does exactly that. Adds a 
    * child node from one DOM as a child node of another DOM.
    * @param replacedDocument the DOM for which an element is to be added.
    * @param replacingElement the Node from another DOM to be added to replacedDocument.
    * @return Go ahead and return the replacedDocument.
    * 
    * @author Kevin Benson
    */
   public static Document addNode(Document replacedDocument, Node replacingElement){
       
       //It is actually quite easy to do, but you must do an importNode method which creates a Node
       //attached to the target DOM.   
       Node replacingNode = 
           ((replacedDocument).importNode(replacingElement, true)); 
       //The import node attaches it to DOM, but you msut still append it as a child to your root element.   
       replacedDocument.getDocumentElement().appendChild(replacingNode);
       return replacedDocument;
   }  


   /**
    * DOM does not let you easily add child nodes from one DOM to another DOM.  This method will replace a Node from
    * one DOM with a Node from another DOM. 
    * @param replacedDocument the Document to have it's children replaces.
    * @param replacingElement the new Node to be placed in the DOM
    * @param replacedNode the old Node to be replaced.
    * @return the new DOM
    * 
    * @author Kevin Benson
    */  
   public static Document replaceNode(Document replacedDocument, Node replacingElement, Node replacedNode){
          
      //It is actually quite easy to do, but you must do an importNode method which creates a Node
      //attached to the target DOM.
      Node replacingNode = 
          ((replacedDocument).importNode(replacingElement, true)); 
   
       
      //Now just replace the Nodes
      Node replaceNodeParent = replacedNode.getParentNode();
      Node oldNode = replaceNodeParent.replaceChild(replacingNode, replacedNode);
      //System.out.println("this oldNode was replaced = " + XMLUtils.ElementToString((Element)oldNode));
      return replacedDocument;
   }
   
   public static HashMap getManagedAuthorities() {
      if(manageAuthorities == null || manageAuthorities.size() <= 0) {
         doManageAuthorities();   
      }
      return manageAuthorities;      
   }
   
   public static HashMap doManageAuthorities() {
      RegistryService rs = new RegistryService();
      Document regEntry = null;
      if(manageAuthorities == null) {
         manageAuthorities = new HashMap();
      }
      manageAuthorities.clear();
      try {
         regEntry = rs.loadRegistry(null);
      }catch(Exception e) {
         e.printStackTrace();
         regEntry = null;   
      }
      if(regEntry != null) {
         System.out.println("The Registry entry = " + XMLUtils.DocumentToString(regEntry));
         //TODO fix this so it uses namespaces instead.  This should go away anyways with the new db.
         //NodeList nl =  regEntry.getElementsByTagNameNS("*","ManagedAuthority" );
         NodeList nl = regEntry.getElementsByTagName("vg:ManagedAuthority");
         //System.out.println("the nodelist size for getting manageauthority2 = " + nl2.getLength());
         System.out.println("the nodelist size for getting manageauthority = " + nl.getLength());
         if(nl.getLength() > 0) {
            for(int i=0;i < nl.getLength();i++) {
               manageAuthorities.put(nl.item(i).getFirstChild().getNodeValue(),null);
            }//for
         }//if
      }//if
      return manageAuthorities;
   }  
   
   public static void updateRegistryEntry(Document authorityEntries) {
      Node parentNode = findElementNode("Authority",authorityEntries.getDocumentElement());
      Document regEntry = null;
      String childName = null;
      if(parentNode != null) {
         RegistryService rs = new RegistryService();
         try {
            regEntry = rs.loadRegistry(null);
         }catch(Exception e) {
            e.printStackTrace();
            regEntry = null;   
         }
         if(regEntry != null) {
            System.out.println("okay ole regEntry = " + XMLUtils.DocumentToString(regEntry));
            while(parentNode != null) {
               Node nd = findElementNode("AuthorityID",parentNode);
               Node refChild = findElementNode("ManagedAuthority",regEntry.getDocumentElement());
               Element elem = regEntry.createElement("ManagedAuthority");
               childName = nd.getFirstChild().getNodeValue();
               elem.appendChild(regEntry.createTextNode(childName));
               System.out.println("new element node name = "  + elem.getNodeName() + " and node value = " + childName);
               Node regNode = findElementNode("vg:Registry",regEntry.getDocumentElement());
               if(regNode != null) {
                  regNode.insertBefore(elem,refChild);
                  System.out.println("new Node to be placed in the registry = " + XMLUtils.ElementToString((Element)regNode) + " and nodename = " + regNode.getNodeName());                  
                  Document temp = updateDocument(regNode,false,false);
                  manageAuthorities.put(childName,null);
               }

               parentNode = findElementNode("Authority",parentNode.getNextSibling());   
            }//while                
         }//if
      }//if     
   }
   

   /**
    * Recursively searches Nodes and all child Nodes from a DOM looking for a particular name.
    * When the name is found return the current pointer position of that node.
    * @param elementName name that matches the NodeName to look for.
    * @param root root to start looking at.
    * @return the Match noded or null if no node was found.
    * 
    * @author Kevin Benson
    */
   public static Node findElementNode(String elementName, Node root){

      Node matchingNode = null;
      
      //root is null so return a null.   
      if(root == null) {
         return null;   
      }
      //Check to see if root is the desired element. If so return root.
      String nodeName = root.getNodeName();
      
      //System.out.println("element name finding = " + elementName + " the node name = " +  nodeName);
      //see if their is a match with the current node.
      if((nodeName != null) & (nodeName.equals(elementName))) {
         //System.out.println("infindelemnetnode = " + nodeName);
         return root;
      }
         
         
      String localName = root.getLocalName();
      
      //might be a match with the local part of the name if any.
      if((localName != null) && (localName.equals(elementName))) 
         return root;
      

        //Check to see if root has any children if not return null
      if(!(root.hasChildNodes()))
         return null;

        //Root has children, so continue searching for them
      NodeList childNodes = root.getChildNodes();
      int noChildren = childNodes.getLength();
      //System.out.println("has this many children = " + noChildren);
      for(int i = 0; i < noChildren; i++){
         if(matchingNode == null){
            Node child = childNodes.item(i);
            matchingNode = findElementNode(elementName,child);
         } else break;
      }//for
      return matchingNode;
   }

   
   /**
    * Method takes in a DOM to be updated.
    * And takes in a DOM that has the new information to be updated.  This method updates the registry by
    * reviewing a DOM for AuthorityID's & ResourceKey's and checking if their are matches.  If their are matches 
    * then an update occurs.
    * @param subDocument DOM that has nodes to be updated/added to the main DOM.
    * 
    * @author Kevin Benson
    */
   public static synchronized Document updateDocument(Node subRoot,boolean createEntries, boolean checkAuth) {
      Map fileHash = loadRegistryTable();
      //First get the root elements of both documents.
      //Element fileRoot = fileDocument.getDocumentElement();
      //Element subRoot = subDocument.getDocumentElement();
      //Now find the first AuthorityID.
      Node subFoundNode = findElementNode("AuthorityID",subRoot);
      Node fileFoundNode = null;
      String subAuthorityVal = null;
      String subResourceVal = null;
      String subParentName = null;
      Node removalNode = null;
      Node siblNode = null;
      String errorMessage = "";
      Document doc = null;
      DocumentBuilder regBuilder = null;
      boolean keyContains = false;
      ArrayList keyList = new ArrayList(); 
      if(fileHash == null) {
         errorMessage = "Could not load the Registry.";
      }//if
      
      try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      regBuilder = dbf.newDocumentBuilder(); 
      }catch(Exception e) {
         e.printStackTrace();
      }
      
      String subKey = null;
            
      //Now go through all the AuthorityID's until their are no more.
      while(subFoundNode != null  && fileHash != null) {
         
         //Get the AuthorityID's Node value (its contents)

         subAuthorityVal = subFoundNode.getFirstChild().getNodeValue();
         if(checkAuth && !manageAuthorities.containsKey(subAuthorityVal) && subFoundNode.getParentNode().getParentNode().getNodeName().indexOf("Authority") == -1) {
            errorMessage += " The AuthorityID associated to this identifier is not managed by this registry.  AuthorityID = " + subAuthorityVal; 
         } else {
            //Go ahead and get the MAIN parent. MAIN parent is a direct child node of the root element.
            //subParentName = subFoundNode.getParentNode().getParentNode().getNodeName();
            
            //Now go ahead and get the ResourceKey element.  And get it's contents if it has any.
   
            subFoundNode = findElementNode("ResourceKey",subFoundNode.getParentNode());
            if(subFoundNode.hasChildNodes()) {
               subResourceVal = subFoundNode.getFirstChild().getNodeValue();
            }else {
               subResourceVal = "";
            }
            subKey = subAuthorityVal + "/" + subResourceVal;
            
            keyContains = false;
            keyContains = fileHash.containsKey(subKey);
            if(keyContains || createEntries) {
               keyList.add(subKey);
               setNewDates(subFoundNode.getParentNode().getParentNode(),createEntries);
               fileHash.put(subKey,subFoundNode.getParentNode().getParentNode());
               if(keyContains) {
                  addStatusMessage("Updated entry in Registry, Identifier = " + subKey);
               }else {
                  addStatusMessage("Added entry in Registry, Identifier = " + subKey);
               }   
            }else {
               errorMessage += " The Identifier could not be found to do an update.  No update was made on Identifier = " + subKey;
            }
         }

         siblNode = subFoundNode.getParentNode().getParentNode().getNextSibling();
         subFoundNode = findElementNode("AuthorityID",siblNode);

         while( siblNode != null && subFoundNode == null) {
            siblNode = siblNode.getNextSibling();
            if(siblNode != null)
               subFoundNode = findElementNode("AuthorityID",siblNode);
         }
      }//while
      if(errorMessage == null || errorMessage.trim().length() <= 0) {
         createDocument(keyList.toArray());
      }      
      if(errorMessage != null && errorMessage.trim().length() > 0) {
         if(regBuilder != null) {
            doc = regBuilder.newDocument();
            Element elem = doc.createElement("error");
            elem.appendChild(doc.createTextNode(errorMessage));
            doc.appendChild(elem);
         }//if   
      }//if
      return doc;
   }
   
   /**
    * Method takes in a DOM to be added
    * And takes in a DOM that has the new information to be added.  This method updates the registry by
    * reviewing a DOM for AuthorityID's & ResourceKey's and checking if their are matches.  If their are no matches 
    * then an add occurs.
    * @param subDocument DOM that has nodes to be updated/added to the main DOM.
    * 
    * @author Kevin Benson
    */
   public static synchronized Document addDocument(Node subRoot,boolean checkAuth) {
      Map fileHash = loadRegistryTable();
      //First get the root elements of both documents.
      //Element fileRoot = fileDocument.getDocumentElement();
      //Element subRoot = subDocument.getDocumentElement();
      //Now find the first AuthorityID.
      Node subFoundNode = findElementNode("AuthorityID",subRoot);
      Node fileFoundNode = null;
      String subAuthorityVal = null;
      String subResourceVal = null;
      String subParentName = null;
      Node removalNode = null;
      Node siblNode = null;
      String errorMessage = "";
      ArrayList keyList = new ArrayList();
      
      String subKey = null;
      
      Document doc = null;
      DocumentBuilder regBuilder = null;
      
      if(fileHash == null) {
         errorMessage = "Could not load Registry";
      }//if
      
      try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      regBuilder = dbf.newDocumentBuilder(); 
      }catch(Exception e) {
         e.printStackTrace();
      }
      
      /*
      Set stTemp = manageAuthorities.keySet();
      Object []checkAuthTemp = stTemp.toArray();
      for(int m = 0;m < checkAuthTemp.length;m++) {
         System.out.println("manage authorities = " + checkAuthTemp[m]);
      }
      */
      
      //Now go through all the AuthorityID's until their are no more.
      while(subFoundNode != null  && fileHash != null) {
         
         //Get the AuthorityID's Node value (its contents)

         subAuthorityVal = subFoundNode.getFirstChild().getNodeValue();
         if(checkAuth && !manageAuthorities.containsKey(subAuthorityVal) && subFoundNode.getParentNode().getParentNode().getNodeName().indexOf("Authority") == -1) {
            errorMessage += " The AuthorityID associated to this identifier is not managed by this registry.  AuthorityID = " + subAuthorityVal; 
         } else {
            //Now go ahead and get the ResourceKey element.  And get it's contents if it has any.
            subFoundNode = findElementNode("ResourceKey",subFoundNode.getParentNode());
            if(subFoundNode.hasChildNodes()) {
               subResourceVal = subFoundNode.getFirstChild().getNodeValue();
            }else {
               subResourceVal = "";
            }
            subKey = subAuthorityVal + "/" + subResourceVal;
            if(!fileHash.containsKey(subKey)) {
               keyList.add(subKey);
               setNewDates(subFoundNode.getParentNode().getParentNode(),true);
               fileHash.put(subKey,subFoundNode.getParentNode().getParentNode());
               addStatusMessage("Added entry in Registry, Identifier = " + subKey);   
            }else {
               errorMessage += " This Identifier already exists in the registry.  No add was created; Identifier = " + subKey;
            }
         }//else
         siblNode = subFoundNode.getParentNode().getParentNode().getNextSibling();
         subFoundNode = findElementNode("AuthorityID",siblNode);

         while( siblNode != null && subFoundNode == null) {
            siblNode = siblNode.getNextSibling();
            if(siblNode != null)
               subFoundNode = findElementNode("AuthorityID",siblNode);
         }
      }//while
      if(errorMessage == null || errorMessage.trim().length() <= 0) {
         createDocument(keyList.toArray());
      }
      if(errorMessage != null && errorMessage.trim().length() > 0) {
         if(regBuilder != null) {
            doc = regBuilder.newDocument();
            Element elem = doc.createElement("error");
            elem.appendChild(doc.createTextNode(errorMessage));
            doc.appendChild(elem);
         }//if   
      }//if
      return doc;
      
   }//addDocument
   
   public static void setNewDates(Node nd,boolean add) {
      if(!nd.hasAttributes()) {
         //TODO logerror this method is supposed to be used when they have a created and upated attributes.
         return;         
      }
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      NamedNodeMap nnm = nd.getAttributes();
      Node updateNode = nnm.getNamedItem("updated");
      Calendar rightNow = Calendar.getInstance();
      System.out.println("checking updated");
      if(updateNode != null) {
         //System.out.println("nod value before = " + updateNode.getNodeValue());
         updateNode.setNodeValue(sdf.format(rightNow.getTime()));
         //System.out.println("nod value after = " + updateNode.getNodeValue());
      }//if
      if(add) {
         Node createNode = nnm.getNamedItem("created");
         if(createNode != null) {
            createNode.setNodeValue(sdf.format(rightNow.getTime()));
         }//if
      }//if
   }
   
   /**
    * Method takes in a DOM to have a node removed (seems to be always the registry file so this parameter may be removed.)
    * And takes in a DOM that has the information to be removed.  This method removes a Node in the registry by
    * reviewing a DOM for AuthorityID's & ResourceKey's and checking if their are matches.  If their are matches 
    * then an remove occurs.
    * @param fileDocument Main DOM - Document to be compared against.
    * @param subDocument DOM that has nodes to be removed to the main DOM.
    * 
    * @author Kevin Benson
    */
   public static synchronized void removeDocument(Document subDocument) {
      boolean updated = false;
      
      Document fileDocument = loadRegistryFile();      
      //First get the root elements
      Element fileRoot = fileDocument.getDocumentElement();
      Element subRoot = subDocument.getDocumentElement();
      
      //Go and find the first AuthorityID
      Node subFoundNode = findElementNode("AuthorityID",subRoot);
      Node fileFoundNode = null;
      String subAuthorityVal = null;
      String subResourceVal = null;
      String subParentName = null;
      Node removalNode = null;
      
      //Go through all the Authority id's until their are no more.
      while(subFoundNode != null) {
         //get the authority id's value.
         subAuthorityVal = subFoundNode.getFirstChild().getNodeValue();
         //And ge the MAIN parent.
         subParentName = subFoundNode.getParentNode().getParentNode().getNodeName();
         
         //Now get the ResourceKey as well.
         subFoundNode = findElementNode("ResourceKey",subFoundNode.getParentNode());
         if(subFoundNode.hasChildNodes()) {
            subResourceVal = subFoundNode.getFirstChild().getNodeValue();
         }else {
            subResourceVal = null;
         }

         //Now lets look for this AuthorityID and ResourceKey in our main document.
         fileFoundNode = findElementNode(subParentName,fileRoot);
         while(fileFoundNode != null) {
            
            fileFoundNode = findElementNode("AuthorityID",fileFoundNode);
            if(fileFoundNode == null) {
               //error an AuthorityID is not found for a resource.
            }else {
               //see if the authority id's match.
               if(subAuthorityVal.equals(fileFoundNode.getFirstChild().getNodeValue())) {
                  //Now get a resourceKey
                  fileFoundNode = findElementNode("ResourceKey",fileFoundNode.getParentNode());
                  if(fileFoundNode == null) {
                     //again an error no ResourceKey for a resource, should always be an element of ResourceKey
                     //even when their is no text node for it.
                  }else {
                     //Their has been a match, so remove the MAIN parent node which is 2 levels up.
                     if(subResourceVal == null && !fileFoundNode.hasChildNodes()) {
                        removalNode = fileFoundNode.getParentNode().getParentNode();
                        removalNode.getParentNode().removeChild(removalNode);
                        return;
                     }else if(fileFoundNode.hasChildNodes() && subResourceVal != null && subResourceVal.equals(fileFoundNode.getFirstChild().getNodeValue())) {
                        removalNode = fileFoundNode.getParentNode().getParentNode();
                        removalNode.getParentNode().removeChild(removalNode);
                        return;
                     }//elseif
                  }//else
               }//if
            }//else
            //go to the next MAIN parent.            
            fileFoundNode = findElementNode(subParentName,fileFoundNode.getParentNode().getParentNode().getNextSibling());
         }//while
         //go to the next MAIN parent.
         subFoundNode = findElementNode("AuthorityID",subFoundNode.getParentNode().getParentNode().getNextSibling());
      }//while
   }   
   
}