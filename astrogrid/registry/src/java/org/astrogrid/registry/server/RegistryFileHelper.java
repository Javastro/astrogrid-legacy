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
import org.astrogrid.registry.RegistryConfig;

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

   /**
    * The main Registry Document.
    */   
   private static Document registryDocument;

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
      Element root = query.getDocumentElement();
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
      RegistryConfig.loadConfig();
      File registryFile = RegistryConfig.getRegistryFile();

      //Now create a DOM model from the registry file in the config.
      try {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder regBuilder = dbf.newDocumentBuilder();
        registryDocument = regBuilder.parse(registryFile);
       // System.out.println("the big xml registry = " + XMLUtils.DocumentToString(registryDocument));
      } catch (ParserConfigurationException e) {
        e.printStackTrace();
      } catch (IOException ioe) {
         ioe.printStackTrace();
      } catch (SAXException sax) {
         sax.printStackTrace();
      }
      return registryDocument;
   }
   
   /**
    * Writes the current registry file in memory to the original flat file. 
    *
    * @author Kevin Benson
    */
   public static synchronized void writeRegistryFile() {
      //Don't have it so find the registry file from our configuration.
      RegistryConfig.loadConfig();
      File registryFile = RegistryConfig.getRegistryFile();

      try {
         FileOutputStream fos = new FileOutputStream(registryFile,false);
         XMLUtils.DocumentToStream(registryDocument,fos);                  
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
      replaceNodeParent.replaceChild(replacingNode, replacedNode);
      return replacedDocument;
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
      
      //see if their is a match with the current node.
      if((nodeName != null) & (nodeName.equals(elementName))) 
         return root;

        //Check to see if root has any children if not return null
      if(!(root.hasChildNodes()))
         return null;

        //Root has children, so continue searching for them
      NodeList childNodes = root.getChildNodes();
      int noChildren = childNodes.getLength();
      for(int i = 0; i < noChildren; i++){
         if(matchingNode == null){
            Node child = childNodes.item(i);
            matchingNode = findElementNode(elementName,child);
         } else break;
      }//for
      return matchingNode;
   }

   
   /**
    * Method takes in a DOM to be updated (seems to be always the registry file so this parameter may be removed.)
    * And takes in a DOM that has the new information to be updated.  This method updates the registry by
    * reviewing a DOM for AuthorityID's & ResourceKey's and checking if their are matches.  If their are matches 
    * then an update occurs if not then an Add will occur.
    * @param fileDocument Main DOM - Document to be compared against.
    * @param subDocument DOM that has nodes to be updated/added to the main DOM.
    * 
    * @author Kevin Benson
    */
   public static synchronized void updateDocument(Document fileDocument, Document subDocument) {
      boolean updated = false;
      //First get the root elements of both documents.
      Element fileRoot = fileDocument.getDocumentElement();
      Element subRoot = subDocument.getDocumentElement();
      
      //Now find the first AuthorityID.
      Node subFoundNode = findElementNode("AuthorityID",subRoot);
      Node fileFoundNode = null;
      String subAuthorityVal = null;
      String subResourceVal = null;
      String subParentName = null;
      Node removalNode = null;
      
      //Now go through all the AuthorityID's until their are no more.
      while(subFoundNode != null) {
         
         //Get the AuthorityID's Node value (its contents)
         subAuthorityVal = subFoundNode.getFirstChild().getNodeValue();
         //Go ahead and get the MAIN parent. MAIN parent is a direct child node of the root element.
         subParentName = subFoundNode.getParentNode().getParentNode().getNodeName();
         
         //Now go ahead and get the ResourceKey element.  And get it's contents if it has any.
         subFoundNode = findElementNode("ResourceKey",subFoundNode.getParentNode());
         if(subFoundNode.hasChildNodes()) {
            subResourceVal = subFoundNode.getFirstChild().getNodeValue();
         }else {
            subResourceVal = null;
         }

         //See if this same node and parent is in the compareed DOM
         fileFoundNode = findElementNode(subParentName,fileRoot);
         //Go through all the AuthorityID's until you find a match if you can (they have the same MAIN parent).
         while(fileFoundNode != null) {
            //look for authority id's.
            fileFoundNode = findElementNode("AuthorityID",fileFoundNode);
            
            if(fileFoundNode == null) {
               //error an AuthorityID is not found for a resource.
            }else {
               //Do the Authority id's match.
               if(subAuthorityVal.equals(fileFoundNode.getFirstChild().getNodeValue())) {
                  //Now look for the ResourceKey.  Which should come with an Authority id.
                  fileFoundNode = findElementNode("ResourceKey",fileFoundNode.getParentNode());
                  if(fileFoundNode == null) {
                     //again an error no ResourceKey for a resource, should always be an element of ResourceKey
                     //even when their is no text node for it.
                  }else {
                     //Okay the ResoruceKey's match as well.
                     if(subResourceVal == null && !fileFoundNode.hasChildNodes()) {
                        // a match set update
                        updated = true;
                        //replace the node in the compared file DOM with the new Node
                        replaceNode(fileDocument,subFoundNode.getParentNode().getParentNode(),fileFoundNode.getParentNode().getParentNode());
                     }else if(fileFoundNode.hasChildNodes() && subResourceVal != null && subResourceVal.equals(fileFoundNode.getFirstChild().getNodeValue())) {
                        // a match set update
                        updated = true;
                        //replace the node in the compared file DOM with the new Node                        
                        replaceNode(fileDocument,subFoundNode.getParentNode().getParentNode(),fileFoundNode.getParentNode().getParentNode());
                     }//elseif
                  }//else
               }//if
            }//else
            //if their has been an update then stop going through our registry file.
            if(updated) {
               fileFoundNode = null;
            }else {
               fileFoundNode = findElementNode(subParentName,fileFoundNode.getParentNode().getParentNode().getNextSibling());
            }            
         }//while
         //if no update was found then add it.
         if(!updated) {
            //add the new entry into the registry.
            addNode(fileDocument,subFoundNode.getParentNode().getParentNode());
            //now add it was never found.
         }
         //Look for the next Authority ID that is in the next MAIN parent. MAIN parent being a direct child node from root element.
         subFoundNode = findElementNode("AuthorityID",subFoundNode.getParentNode().getParentNode().getNextSibling());
      }//while
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
   public static synchronized void removeDocument(Document fileDocument, Document subDocument) {
      boolean updated = false;
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