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

public class RegistryFileHelper {
 
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
   public static Document loadRegistryFile() {
      Document doc = null;

      String filename = (System.getProperty("user.dir"));
      File f = new File(filename);
      String registryFileName = f.getAbsolutePath() + "/../webapps/org/astrogrid/registry/adil.xml";
      File registryFile = new File(registryFileName);
      try {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder regBuilder = dbf.newDocumentBuilder();
        doc = regBuilder.parse(registryFile);
      } catch (ParserConfigurationException e) {
        e.printStackTrace();
      } catch (IOException ioe) {
         ioe.printStackTrace();
      } catch (SAXException sax) {
         sax.printStackTrace();
      }
      return doc;
   }
   
   public static void writeRegistryFile(Document doc) {
      String filename = (System.getProperty("user.dir"));
      File f = new File(filename);
      String registryFileName = f.getAbsolutePath() + "/../webapps/org/astrogrid/registry/adil.xml";
      File registryFile = new File(registryFileName);

      try {
         FileOutputStream fos = new FileOutputStream(registryFile,false);
         XMLUtils.DocumentToStream(doc,fos);                  
      }catch(FileNotFoundException e) {
         e.printStackTrace();
      }
   }
   
   
   public static Document addNode(Document replacedDocument, Node replacingElement){
          
       //Create a documentFragment of the replacingDocument
       //DocumentFragment docFrag = replacingDocument.createDocumentFragment();
       //Element rootElement = replacingDocument.getDocumentElement();
       //docFrag.appendChild(rootElement);    
       //Import docFrag under the ownership of replacedDocument
       Node replacingNode = 
           ((replacedDocument).importNode(replacingElement, true)); 
   
       
       //In order to replace the node need to retrieve replacedNode's parent
       //Node replaceNodeParent = replacedNode.getParentNode();
       //replaceNodeParent.replaceChild(replacingNode, replacedNode);
       replacedDocument.getDocumentElement().appendChild(replacingNode);
       return replacedDocument;
   }  

  
   public static Document replaceNode(Document replacedDocument, Node replacingElement, Node replacedNode){
          
      //Create a documentFragment of the replacingDocument
      //DocumentFragment docFrag = replacingDocument.createDocumentFragment();
      //Element rootElement = replacingDocument.getDocumentElement();
      //docFrag.appendChild(rootElement);    
     
   
      //Import docFrag under the ownership of replacedDocument
      Node replacingNode = 
          ((replacedDocument).importNode(replacingElement, true)); 
   
       
      //In order to replace the node need to retrieve replacedNode's parent
      Node replaceNodeParent = replacedNode.getParentNode();
      replaceNodeParent.replaceChild(replacingNode, replacedNode);
      return replacedDocument;
   }  

   public static Node findElementNode(String elementName, Node root){

        Node matchingNode = null;

        //Check to see if root is the desired element. If so return root.
        String nodeName = root.getNodeName();
     
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

        }

        return matchingNode;
   }

   
   public static void updateDocument(Document fileDocument, Document subDocument) {
      boolean updated = false;
      Element fileRoot = fileDocument.getDocumentElement();
      Element subRoot = subDocument.getDocumentElement();
      Node subFoundNode = findElementNode("AuthorityID",subRoot);
      Node fileFoundNode = null;
      String subAuthorityVal = null;
      String subResourceVal = null;
      String subParentName = null;
      Node removalNode = null;
      while(subFoundNode != null) {
         subAuthorityVal = subFoundNode.getFirstChild().getNodeValue();
         subParentName = subFoundNode.getParentNode().getParentNode().getNodeName();
         
         subFoundNode = findElementNode("ResourceKey",subFoundNode.getParentNode());
         if(subFoundNode.hasChildNodes()) {
            subResourceVal = subFoundNode.getFirstChild().getNodeValue();
         }else {
            subResourceVal = null;
         }

         fileFoundNode = findElementNode(subParentName,fileRoot);
         while(fileFoundNode != null) {
            fileFoundNode = findElementNode("AuthorityID",fileFoundNode);
            if(fileFoundNode == null) {
               //error an AuthorityID is not found for a resource.
            }else {
               if(subAuthorityVal.equals(fileFoundNode.getFirstChild().getNodeValue())) {
                  fileFoundNode = findElementNode("ResourceKey",fileFoundNode.getParentNode());
                  if(fileFoundNode == null) {
                     //again an error no ResourceKey for a resource, should always be an element of ResourceKey
                     //even when their is no text node for it.
                  }else {
                     if(subResourceVal == null && !fileFoundNode.hasChildNodes()) {
                        // a match call update
                        updated = true;
                        replaceNode(fileDocument,subFoundNode.getParentNode().getParentNode(),fileFoundNode.getParentNode().getParentNode());                        
//                        System.out.println("a match found on the null of resourcekey");
                     }else if(fileFoundNode.hasChildNodes() && subResourceVal != null && subResourceVal.equals(fileFoundNode.getFirstChild().getNodeValue())) {
                        // a match call update
                        updated = true;
                        replaceNode(fileDocument,subFoundNode.getParentNode().getParentNode(),fileFoundNode.getParentNode().getParentNode());
//                        System.out.println("a match found on the resourcekey and authorityid");
                     }//elseif
                  }//else
               }//if
            }//else
            if(updated) {
               fileFoundNode = null;
            }else {
               fileFoundNode = findElementNode(subParentName,fileFoundNode.getParentNode().getParentNode().getNextSibling());
            }            
         }//while
         if(!updated) {
            addNode(fileDocument,subFoundNode.getParentNode().getParentNode());
            //now add it was never found.
         }
         subFoundNode = findElementNode("AuthorityID",subFoundNode.getParentNode().getParentNode().getNextSibling());
      }
      System.out.println("printout of the document = " + XMLUtils.DocumentToString(fileDocument));
   }
   
   public static void removeDocument(Document fileDocument, Document subDocument) {
      boolean updated = false;
      Element fileRoot = fileDocument.getDocumentElement();
      Element subRoot = subDocument.getDocumentElement();
      Node subFoundNode = findElementNode("AuthorityID",subRoot);
      Node fileFoundNode = null;
      String subAuthorityVal = null;
      String subResourceVal = null;
      String subParentName = null;
      Node removalNode = null;
      while(subFoundNode != null) {
         subAuthorityVal = subFoundNode.getFirstChild().getNodeValue();
         subParentName = subFoundNode.getParentNode().getParentNode().getNodeName();
         
         subFoundNode = findElementNode("ResourceKey",subFoundNode.getParentNode());
         if(subFoundNode.hasChildNodes()) {
            subResourceVal = subFoundNode.getFirstChild().getNodeValue();
         }else {
            subResourceVal = null;
         }

         fileFoundNode = findElementNode(subParentName,fileRoot);
         while(fileFoundNode != null) {
            fileFoundNode = findElementNode("AuthorityID",fileFoundNode);
            if(fileFoundNode == null) {
               //error an AuthorityID is not found for a resource.
            }else {
               if(subAuthorityVal.equals(fileFoundNode.getFirstChild().getNodeValue())) {
                  fileFoundNode = findElementNode("ResourceKey",fileFoundNode.getParentNode());
                  if(fileFoundNode == null) {
                     //again an error no ResourceKey for a resource, should always be an element of ResourceKey
                     //even when their is no text node for it.
                  }else {
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
            fileFoundNode = findElementNode(subParentName,fileFoundNode.getParentNode().getParentNode().getNextSibling());
         }//while
         subFoundNode = findElementNode("AuthorityID",subFoundNode.getParentNode().getParentNode().getNextSibling());
      }
      System.out.println("printout of the document = " + XMLUtils.DocumentToString(fileDocument));
   }   
   
}