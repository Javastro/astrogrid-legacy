package org.astrogrid.registry.server.admin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.astrogrid.registry.server.RegistryFileHelper;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import org.exolab.castor.xml.*;
import org.astrogrid.registry.beans.resource.*;

import org.astrogrid.config.Config;
import org.astrogrid.registry.common.XSLHelper;
import org.astrogrid.registry.server.XQueryExecution;
import org.astrogrid.util.DomHelper;
import java.util.ArrayList;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.server.query.RegistryService;



/**
 * Class Name: RegistryAdminService
 * Description: This class represents the web service to the Administration piece of the
 * registry.  This class will handle inserts/updates/remove's of data in the registry.
 * 
 * @see org.astrogrid.registry..common.RegistryAdminInterface
 * @author Kevin Benson
 *
 * 
 */
public class RegistryAdminService implements
                       org.astrogrid.registry.common.RegistryAdminInterface {
                          
   public static Config conf = null;
   
   private static final String AUTHORITYID_PROPERTY = "org.astrogrid.registry.authorityid";

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
   
   /**
    * Takes an XML Document and will either update and insert the data in the registry.  If a client is
    * intending for an insert, but the primarykey (AuthorityID and ResourceKey) are already in the registry
    * an automatic update will occur.  This method will only update main pieces of data/elements
    * conforming to the IVOA schema.
    * 
    * Main Pieces: Organisation, Authority, Registry, Resource, Service, SkyService, TabularSkyService, 
    * DataCollection 
    * 
    * @param update Document a XML document dom object to be updated on the registry.
    * @return the document updated on the registry is returned.
    * @author Kevin Benson
    * 
    */   
  public Document update(Document update) {
    System.out.println("entered update on server side");
    XSLHelper xs = new XSLHelper();
    NodeList voList = update.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","VODescription");
         
    if(voList.getLength() == 0) {
       voList = update.getElementsByTagNameNS("vr","VODescription");
    }
    if(voList.getLength() == 0) {
       voList = update.getElementsByTagName("VODescription");
    }
    if(voList.getLength() == 0) {
       voList = update.getElementsByTagName("vr:VODescription");
    }
    if(voList.getLength() == 0) {
       System.out.println("returning update no vodesc found");
       return update;   
    }
    
    Document xsDoc = xs.transformDatabaseProcess(voList.item(0));
    //String query = formCompareXQLFromDocument(xsDoc);
    //Document resultDoc = QueryParser3_0.runQuery(query);
    
    
    System.out.println("server side update the xsDoc = " + DomHelper.DocumentToString(xsDoc));
    
    //System.out.println("This is xsDoc = " + XMLUtils.DocumentToString(xsDoc));
    NodeList nl = xsDoc.getElementsByTagNameNS("vr","Resource" );
    if(nl.getLength() == 0) {
       nl = xsDoc.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","Resource" );
    }
    if(nl.getLength() == 0) {
       nl = xsDoc.getElementsByTagName("Resource" );
    }
    if(nl.getLength() == 0) {
       nl = xsDoc.getElementsByTagName("vr:Resource" );
    }
    
    HashMap manageAuths = RegistryFileHelper.getManagedAuthorities();
    HashMap otherAuths = RegistryFileHelper.getOtherManagedAuthorities();    
    ArrayList al = new ArrayList();
    String xql = null;
    DocumentFragment df = null;
    Node root = null;
    Document resultDoc = null;
    String ident = null;
    String resKey = null;
    boolean addManageError = false;
    System.out.println("here is the nl length = " + nl.getLength() + " and manauths size = " + manageAuths.size() + " and otherAuths size = " + otherAuths.size());
    for(int i = 0;i < nl.getLength();i++) {
      ident = getAuthorityID((Element)nl.item(i));
      resKey = getResourceKey((Element)nl.item(i));
      Element currentResource = (Element)nl.item(i);
      System.out.println("serverside update ident = " + ident + " reskey = " + resKey);
      if(manageAuths.containsKey(ident)) {         
         xql = formUpdateXQLQuery(currentResource,ident,resKey);
         df = xsDoc.createDocumentFragment();
         root = xsDoc.getDocumentElement().cloneNode(false);
         root.appendChild(currentResource);
         df.appendChild(root);
         resultDoc = XQueryExecution.runQuery(xql,df);
         System.out.println("the resultDoc to find an id = " + DomHelper.DocumentToString(resultDoc));         
      }else {
         addManageError = true;
         System.out.println("checking if this is an AuthorityType it has attributes correct = " + currentResource.hasAttributes());
         if(currentResource.hasAttributes()) {
            NamedNodeMap nnm = currentResource.getAttributes();
            for(int j = 0;j < nnm.getLength();j++) {
               Node attrNode = nnm.item(j);
               String nodeVal = attrNode.getNodeValue();
               System.out.println("Checking NodeVal for an authorityType: current NodeVal = " + nodeVal);
               if(nodeVal != null && nodeVal.indexOf("AuthorityType") != -1) {
                  if(!otherAuths.containsKey((String)ident)) {
                     System.out.println("This is an AuthorityType and not managed by other authorities");
                     addManageError = false;
                     //update new managed authority.
                     RegistryService rs = new RegistryService();
                     Document loadedRegistry = rs.loadRegistry(null);
                     //System.out.println("the load registry = " + DomHelper.DocumentToString(loadedRegistry));
                     Node manageNode = getManagedAuthorityID(loadedRegistry);
                     if(manageNode != null) {
                        System.out.println("creating new manage element for authorityid = " + ident);
                        Element newManage = loadedRegistry.createElementNS(manageNode.getNamespaceURI(),manageNode.getNodeName());                        
                        newManage.appendChild(loadedRegistry.createTextNode(ident));
                        //System.out.println("inserting the new node new manage node = " + newManage.getNodeName() + " text to it = " + newManage.getFirstChild().getNodeValue());
                        loadedRegistry.getDocumentElement().getFirstChild().appendChild(newManage);
                        //System.out.println("firstchild of loadedRegistry = " + loadedRegistry.getDocumentElement().getFirstChild().getNodeName());
                        //loadedRegistry.insertBefore(newManage,manageNode);
                        //addManageNode.appendChild(loadedRegistry.createTextNode(ident));
                        //loadedRegistry.insertBefore(addManageNode,manageNode);
                        System.out.println("forming xql query and loadedRegistry = " + DomHelper.DocumentToString(loadedRegistry));
                        xql = formUpdateXQLQuery(currentResource,ident,resKey);
                        df = xsDoc.createDocumentFragment();
                        root = xsDoc.getDocumentElement().cloneNode(false);
                        root.appendChild(currentResource);
                        df.appendChild(root);
                        System.out.println("running query with new authorityentry = " + xql);
                        resultDoc = XQueryExecution.runQuery(xql,df);
                        System.out.println("the resultDoc to find an id = " + DomHelper.DocumentToString(resultDoc));

                        ident = getAuthorityID(loadedRegistry.getDocumentElement());
                        resKey = getResourceKey(loadedRegistry.getDocumentElement());                        
                        xql = formUpdateXQLQuery(loadedRegistry.getDocumentElement(),ident,resKey);
                        resultDoc = XQueryExecution.runQuery(xql,loadedRegistry);
                        System.out.println("the resultDoc to find an id = " + DomHelper.DocumentToString(resultDoc));
                        manageAuths = RegistryFileHelper.doManageAuthorities();
                     }//if                           
                  }//if      
               }//if   
            }//for   
         }//if
         if(addManageError)
            al.add("This AuthorityID is not managed by this Registry: " + ident);
      }//else
    }//for
    
   if(al != null && al.size() > 0) {
      try {
         Document errorDoc = DomHelper.newDocument();
         Element errorRoot = errorDoc.createElement("RegistryError");
         errorDoc.appendChild(errorRoot);
         for(int i = 0;i < al.size();i++) {
            Element errorElem = errorDoc.createElement("error");
            errorElem.appendChild(errorDoc.createTextNode((String)al.get(i)));
            errorRoot.appendChild(errorElem);
         }   
         return errorDoc;
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();   
      }
    }
    System.out.println("made to end and returning update again");
    return update;
  }
  
   public void updateNoCheck(Document xsDoc) {

      //System.out.println("This is xsDoc = " + XMLUtils.DocumentToString(xsDoc));
      NodeList nl = xsDoc.getElementsByTagNameNS("vr","Resource" );
      if(nl.getLength() == 0) {
         nl = xsDoc.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","Resource" );
      }
      if(nl.getLength() == 0) {
         nl = xsDoc.getElementsByTagName("Resource" );
      }
      if(nl.getLength() == 0) {
         nl = xsDoc.getElementsByTagName("vr:Resource" );
      }
    
      ArrayList al = new ArrayList();
      String xql = null;
      DocumentFragment df = null;
      Node root = null;
      Document resultDoc = null;
      String ident = null;
      String resKey = null;
      boolean addManageError = false;
      for(int i = 0;i < nl.getLength();i++) {
         ident = getAuthorityID((Element)nl.item(i));
         resKey = getResourceKey((Element)nl.item(i));
         Element currentResource = (Element)nl.item(i);         
         xql = formUpdateXQLQuery(currentResource,ident,resKey);
         df = xsDoc.createDocumentFragment();
         root = xsDoc.getDocumentElement().cloneNode(false);
         if(!"VODescription".equals(root.getLocalName())) {
            root = xsDoc.createElementNS("http://www.ivoa.net/xml/VOResource/v0.9","VODescription");
         }
         root.appendChild(currentResource);
         df.appendChild(root);
         resultDoc = XQueryExecution.runQuery(xql,df);
         System.out.println("the resultDoc to find an id = " + DomHelper.DocumentToString(resultDoc));         
      }//for
   }
  
   private Node getManagedAuthorityID(Document doc) {
      NodeList nl = doc.getElementsByTagNameNS("vg","ManagedAuthority" );
      //Okay for some reason vg seems to pick up the ManagedAuthority.
      //Lets try to find it by the url namespace.
      if(nl.getLength() == 0) {
         nl = doc.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","ManagedAuthority" );
      }
      if(nl.getLength() == 0) {
         nl = doc.getElementsByTagName("vg:ManagedAuthority" );
      }
      if(nl.getLength() == 0) {
         nl = doc.getElementsByTagName("ManagedAuthority" );
      }      
      if(nl.getLength() > 0)
         return nl.item(0);
      return null;
     
  }
  
  private String getAuthorityID(Element doc) {
     NodeList nl = doc.getElementsByTagNameNS("vr","AuthorityID" );
     
     //Okay for some reason vg seems to pick up the ManagedAuthority.
     //Lets try to find it by the url namespace.
     if(nl.getLength() == 0) {
        nl = doc.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","AuthorityID" );
     }
     if(nl.getLength() == 0) {
        nl = doc.getElementsByTagName("AuthorityID" );
     }
     if(nl.getLength() == 0) {
        nl = doc.getElementsByTagName("vr:AuthorityID" );
     }
     if(nl.getLength() == 0) {
      return null;   
     }
     return nl.item(0).getFirstChild().getNodeValue();
  }
  
  private String getResourceKey(Element doc) {

     NodeList nl = doc.getElementsByTagNameNS("vr","ResourceKey" );
     //Okay for some reason vg seems to pick up the ManagedAuthority.
     //Lets try to find it by the url namespace.
     if(nl.getLength() == 0) {
        nl = doc.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","ResourceKey" );
     }
     if(nl.getLength() == 0) {
        nl = doc.getElementsByTagName("ResourceKey" );
     }
     if(nl.getLength() == 0) {
        nl = doc.getElementsByTagName("vr:ResourceKey" );
     }
     if(nl.getLength() == 0) {
      return null;   
     }
     if(nl.item(0).hasChildNodes()) {
       return nl.item(0).getFirstChild().getNodeValue();   
     }
     return null;
  }
  
  private String formUpdateXQLQuery(Element doc,String ident, String resKey) {
     if(resKey != null) {
       return ".//*:AuthorityID = '" + ident + "' and .//*:ResourceKey = '" + resKey + "'";   
     }else {
       return ".//*:AuthorityID = '" + ident + "'";
     }
  }
  
  /*
  private String formInvalidAuthorityIDCheckQuery() {
   String regAuthID = conf.getString(AUTHORITYID_PROPERTY);
   
   return ".//@*:type='RegistryType' and .//*:AuthorityID != '" + regAuthID +"'";   
  }
  */
  

 /**
   * Takes an XML Document and will either update and insert the data in the registry.  If a client is
   * intending for an insert, but the primarykey (AuthorityID and ResourceKey) are already in the registry
   * an automatic update will occur.  This method will only update main pieces of data/elements
   * conforming to the IVOA schema.
   * 
   * Main Pieces: Organisation, Authority, Registry, Resource, Service, SkyService, TabularSkyService, 
   * DataCollection 
   * 
   * @param status This DOM object really should be null each time.
   * @return the document updated on the registry is returned.
   * @author Kevin Benson
   * 
   */   
 public Document getStatus(Document status) {
   Document doc = null;
   try {
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.newDocument();
      
         Element elem = doc.createElement("status");
         elem.appendChild(doc.createTextNode(RegistryFileHelper.getStatusMessage()));
         doc.appendChild(elem);
         System.out.println("Document returned for Status message = " + DomHelper.DocumentToString(doc));
   } catch (ParserConfigurationException pce){
      pce.printStackTrace();
   }
   return doc;
 } 
    
}
