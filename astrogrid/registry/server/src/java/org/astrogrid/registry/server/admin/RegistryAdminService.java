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
import org.astrogrid.config.Config;
import org.astrogrid.registry.common.XSLHelper;
import org.astrogrid.registry.server.XQueryExecution;
import org.astrogrid.util.DomHelper;
import java.util.ArrayList;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.server.query.RegistryService;
import org.xml.sax.helpers.DefaultHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import java.net.MalformedURLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class Name: RegistryAdminService
 * Description: This class represents the web service to the Administration
 * piece of the registry.  This class will handle inserts/updates/remove's of
 * data in the registry.
 * 
 * @see org.astrogrid.registry..common.RegistryAdminInterface
 * @author Kevin Benson
 *
 * 
 */
public class RegistryAdminService implements
                       org.astrogrid.registry.common.RegistryAdminInterface {
                          
   private static final Log log = 
                               LogFactory.getLog(RegistryAdminService.class);
   public static Config conf = null;
   
   private static final String AUTHORITYID_PROPERTY = 
                               "org.astrogrid.registry.authorityid";

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
   
   public NodeList getNodeListTags(Document doc,String tagName, String prefix)
   {
      log.debug("start getNodeListTags");
      NodeList nl = doc.getElementsByTagName(tagName);
      
      if(nl.getLength() == 0) {
         nl = doc.getElementsByTagNameNS(prefix,tagName );
      }
      if(nl.getLength() == 0) {
         nl = doc.getElementsByTagName(prefix + ":" + tagName );
      }
      log.debug("end getNodeListTags");
      return nl;
   }
   
   private void updateSchema(Document update) {
      log.debug("start updateSchema");
      NodeList nl = getNodeListTags(update,"schema","xs");
      log.info("the nodelist length = " + nl.getLength());
      if(nl == null || nl.getLength() == 0) {
         //throw some type of exception;
         return;   
      }
      for(int i = 0;i < nl.getLength();i++) {
         Node ident = ((Element)nl.item(i)).
                                           getAttributeNode("targetNamespace");
         String identifier = null;
         if(ident != null && ident.getNodeValue() != null) {
            identifier = ident.getNodeValue();
         }
         if(identifier == null) {
            identifier = String.valueOf(System.currentTimeMillis());
         }
         log.info("the identifier in updateSchema = " + identifier);
         XQueryExecution.updateQuery("xsd","schemas",identifier,nl.item(i));
      }//for
      log.debug("end updateSchema");
   }
   
   private void updateStyleSheet(Document update) {
      log.debug("start updateStyleSheet");
      NodeList nl = getNodeListTags(update,"stylesheet","xsl");
      log.info("the nodelist length = " + nl.getLength());
      if(nl == null || nl.getLength() == 0) {
         //throw some type of exception;
         return;   
      }
      for(int i = 0;i < nl.getLength();i++) {
         XQueryExecution.updateQuery("xsl","xsl","regstylesheet",nl.item(i));
      }//for
      log.debug("end updateSchema");
   }
   
   private final int RESOURCE_TYPE = 0;
   private final int SCHEMA_TYPE =  1;
   private final int STYLESHEET_TYPE = 2;
   
   private int getUpdateType(Document update) {
      log.debug("start getUpdateType");
      NodeList nl = getNodeListTags(update,"Resource","vr");
      if(nl.getLength() > 0)
         return RESOURCE_TYPE;
      nl = getNodeListTags(update,"schema","xs");
      if(nl.getLength() > 0)
         return SCHEMA_TYPE;
      nl = getNodeListTags(update,"stylesheet","xsl");
      if(nl.getLength() > 0)
         return STYLESHEET_TYPE;
      return -1;
   }
   
   /**
    * Takes an XML Document and will either update and insert the data in the
    * registry.  If a client wants an insert, but the primarykey (AuthorityID
    * and ResourceKey) are already in the registry an automatic update will 
    * occur.  This method will only update main pieces of data/elements
    * conforming to the IVOA schema.
    * 
    * Main Pieces: Organisation, Authority, Registry, Resource, Service, 
    *              SkyService, TabularSkyService, 
    * DataCollection 
    * 
    * @param update Document a XML document dom object to be updated on the registry.
    * @return the document updated on the registry is returned.
    * @author Kevin Benson
    * 
    */   
   public Document update(Document update) {
      log.debug("start update");
      log.info("entered update on server side");
      int updateType = getUpdateType(update);
      if(updateType == -1) {
         //throw an exception
         return null;
      }else if(updateType == RESOURCE_TYPE) {
         return updateResource(update);
      }else if(updateType == SCHEMA_TYPE) {
         updateSchema(update);
      }else if(updateType == STYLESHEET_TYPE) {
         updateStyleSheet(update);
      }
      return null;
   }
   
   /**
    * @param update
    * @return
    */
   public Document updateResource(Document update) {
      log.debug("start updateResource");
      long beginUpdate = System.currentTimeMillis();
      XSLHelper xs = new XSLHelper();      
      Document xsDoc = xs.transformDatabaseProcess((Node)update.
                                                         getDocumentElement());
      log.info("server side update the xsDoc = " + 
               DomHelper.DocumentToString(xsDoc));
      NodeList nl = getNodeListTags(xsDoc,"Resource","vr");
      
      HashMap manageAuths = RegistryFileHelper.getManagedAuthorities();
      HashMap otherAuths = RegistryFileHelper.getOtherManagedAuthorities();    
      ArrayList al = new ArrayList();
      String xql = null;
      DocumentFragment df = null;
      Node root = null;
      Document resultDoc = null;
      String ident = null;
      String resKey = null;
      String tempIdent = null;
      boolean addManageError = false;
      String versionNumber = conf.getString("org.astrogrid.registry.version");
      log.info("here is the nl length = " + nl.getLength() + 
               " and manauths size = " + manageAuths.size() + 
               " and otherAuths size = " + otherAuths.size());

      // Need to revamp/refactor a bunch to not use a while loop anymore
      // just use a for loop througha ll the Resources.  Also no need to use 
      // Document fragments.
      //while(nl.getLength() > 0) {
      final int resourceNum = nl.getLength();
      for(int i = 0;i < resourceNum;i++) {
         long beginQ = System.currentTimeMillis();
         ident = getAuthorityID((Element)nl.item(0));
         resKey = getResourceKey((Element)nl.item(0));
         Element currentResource = (Element)nl.item(0);
         tempIdent = ident;
         if(resKey != null) tempIdent += "/" + resKey;
      
         log.info("serverside update ident = " + ident + " reskey = " + 
                  resKey + " the nl getlenth here = " + nl.getLength());
         if(manageAuths.containsKey(ident)) {         
            //xql = formUpdateXQLQuery(currentResource,ident,resKey);
//          root = xsDoc.getDocumentElement().cloneNode(false);
            root = xsDoc.createElement("AstrogridResource");
            root.appendChild(currentResource);
            //df.appendChild(root);
            RegistryFileHelper.addStatusMessage("Entering new entry: " + 
                                                tempIdent);
            //XQueryExecution.updateQuery(tempIdent,df);
            //XQueryExecution.updateQuery("xml","astrogridv0_9",tempIdent,currentResource);         
            //XQueryExecution.updateQuery("xml","astrogridv0_9",tempIdent,df);
            XQueryExecution.updateQuery("xml","astrogridv" + versionNumber,
                                        tempIdent,root);
         }else {
            addManageError = true;
            if(currentResource.hasAttributes()) {
               NamedNodeMap nnm = currentResource.getAttributes();
               for(int j = 0;j < nnm.getLength();j++) {
                  Node attrNode = nnm.item(j);
                  String nodeVal = attrNode.getNodeValue();
                  log.info(
                  "Checking NodeVal for an authorityType: current NodeVal = " 
                  + nodeVal);
                  if(nodeVal != null && nodeVal.indexOf("RegistryType") != -1)
                  {
                     log.info("This is an RegistryType");
                     //xql = formUpdateXQLQuery(currentResource,ident,resKey);
                     //root = xsDoc.getDocumentElement().cloneNode(false);
                     root = xsDoc.createElement("AstrogridResource");
                     root.appendChild(currentResource);
                     //df.appendChild(root);
                     //log.info("running query with new registry entry = " +
                     //         xql);
                     RegistryFileHelper.addStatusMessage(
                              "Entering new entry: " + tempIdent);
                     //XQueryExecution.updateQuery(tempIdent,df);
                     //XQueryExecution.updateQuery("xml","astrogridv0_9",
                     //                            tempIdent,currentResource);
                     //XQueryExecution.updateQuery("xml","astrogridv0_9",
                     //                            tempIdent,df);
                     XQueryExecution.updateQuery("xml","astrogridv" +
                                                 versionNumber,tempIdent,root);
                  }else if(nodeVal != null && 
                           nodeVal.indexOf("AuthorityType") != -1)
                  {
                     if(!otherAuths.containsKey((String)ident)) {
                        log.info(
              "This is an AuthorityType and not managed by other authorities");
                        addManageError = false;
                        //update new managed authority.
                        RegistryService rs = new RegistryService();
                        Document loadedRegistry = rs.loadRegistry(null);
                        //log.info("the load registry = " + 
                        //         DomHelper.DocumentToString(loadedRegistry));
                        Node manageNode = 
                                        getManagedAuthorityID(loadedRegistry);
                        if(manageNode != null) {
                           log.info(
                     "creating new manage element for authorityid = " + ident);
                           Element newManage = loadedRegistry.
                                               createElementNS(
                                                 manageNode.getNamespaceURI(),
                                                 manageNode.getNodeName());
                           newManage.appendChild(loadedRegistry.
                                                 createTextNode(ident));
                           //log.info(
                           // "inserting the new node new manage node = " + 
                           // newManage.getNodeName() + " text to it = " + 
                           // newManage.getFirstChild().getNodeValue());
                           loadedRegistry.getDocumentElement().
                                        getFirstChild().appendChild(newManage);

                              df = xsDoc.createDocumentFragment();
                           
                           //root = xsDoc.getDocumentElement().
                           //             cloneNode(false);
                           root = xsDoc.createElement("AstrogridResource");
                           root.appendChild(currentResource);
                           //df.appendChild(root);
                           log.info(
                             "running query with new authorityentry = " + xql);
                           RegistryFileHelper.addStatusMessage(
                              "Entering new entry: " + tempIdent);
                           //XQueryExecution.updateQuery("xml","
                           //       astrogridv0_9",tempIdent,currentResource);
                           //XQueryExecution.updateQuery("xml","astrogridv0_9",
                           //                            tempIdent,df);
                           XQueryExecution.updateQuery("xml","astrogridv" +
                                                        versionNumber,
                                                        tempIdent,root);
   
                           ident = getAuthorityID(loadedRegistry.
                                                  getDocumentElement());
                           resKey = getResourceKey(loadedRegistry.
                                                   getDocumentElement());
                           tempIdent = ident;
                           if(resKey != null) tempIdent += "/" + resKey;
                           df = loadedRegistry.createDocumentFragment();
                           Element elem = loadedRegistry.
                                          createElement("AstrogridResource");
                           elem.appendChild(loadedRegistry.
                                            getDocumentElement());
                           //XQueryExecution.updateQuery("xml","astrogridv0_9",
                           //                       tempIdent, loadedRegistry);
                           XQueryExecution.updateQuery("xml","astrogridv" + 
                                                       versionNumber,tempIdent,
                                                       elem);
                           manageAuths = RegistryFileHelper.
                                         doManageAuthorities();
                        }else {
                           log.info(
                           "IN THE AUTO-INTEGRATION YOU SHOULD NOT GET HERE, "+
                           "BUT REMOVING CHILD FOR INFINITE LOOP");
                           xsDoc.getDocumentElement().
                                 removeChild(currentResource);
                        }                           
                     }//if      
                  }//elseif   
               }//for   
            }
            
            if(addManageError) {
               al.add("This AuthorityID is not managed by this Registry: " + 
                      ident);
               xsDoc.getDocumentElement().removeChild(currentResource);
            }//if
         }//else
         log.info("Time taken to update an entry = " + 
                  (System.currentTimeMillis() - beginQ) +
                  " for ident  = " + tempIdent);
      }//while
       
      if(al != null && al.size() > 0) {
         try {
            Document errorDoc = DomHelper.newDocument();
            Element errorRoot = errorDoc.createElement("RegistryError");
            errorDoc.appendChild(errorRoot);
            for(int i = 0;i < al.size();i++) {
               Element errorElem = errorDoc.createElement("error");
               errorElem.appendChild(errorDoc.
                                     createTextNode((String)al.get(i)));
               errorRoot.appendChild(errorElem);
            }   
            return errorDoc;
         }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
            log.error(pce);   
         }
      }
      log.info("Time taken to complete update on server = " +
               (System.currentTimeMillis() - beginUpdate));
      log.debug("end updateResource");
      return update;
   }
  
   public boolean isValid(Document xmlDoc) {
      log.debug("start isValid");
      
      // Okay not sure might need to think of a better way, but here is how
      // this method will work.
      // Create the SAXParserFactory stuff and setnamespaceaware and 
      // setValidting to true.
      String regNameSpaces = conf.getString("registry.namespaces");
      String []nameSpaces = regNameSpaces.split(";");
       
      String location = conf.getString("exist.db.url");
      location += "/servlet/db/schemas";
      
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setNamespaceAware(true);
      spf.setValidating(true);
      SAXParser sp = null;
      try {
         sp = spf.newSAXParser();
         /*
         for(int i = 0;i < nameSpaces.length;i++) {
            String xmlDocName = nameSpaces[i].replaceAll("[^\\w*]","_") + 
                                ".xsd";
            URL localLocation = XQueryExecution.getUpdateURL("schemas",
                                                             xmlDocName);
            HttpURLConnection huc = (HttpURLConnection) localLocation.
                                                        openConnection();
            if(HttpURLConnection.HTTP_NOT_FOUND == huc.getResponseCode()) {
               huc.disconnect();
              //Okay we don't have it in our Exist Db.  Create a Dom object
              //and update it in our schema.
               Document newSchema = DomHelper.newDocument(new URL(
                                                              nameSpaces[i]));
               updateSchema(newSchema);
               huc = (HttpURLConnection) XQueryExecution.
                                         getUpdateURL("schemas", xmlDocName).
                                         openConnection();
               if(HttpURLConnection.HTTP_NOT_FOUND == huc.getResponseCode()) {
                  //Okay if there is still something wrong then just use the
                  //external link for now.
                  //Probably should log this though.
                  huc.disconnect();
                  localLocation = new URL(nameSpaces[i]);
               }//if
            }//if
            sp.setProperty(nameSpaces[i],localLocation.toString());
         }
         */
         sp.setProperty(
             "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
             "http://www.w3.org/2001/XMLSchema"
         );         
      }
      catch(ParserConfigurationException pce) {
         pce.printStackTrace();
         log.error(pce);   
      }
      catch(SAXNotSupportedException sse) {
         sse.printStackTrace();
         log.error(sse);
      }
      catch(SAXException se) {
         se.printStackTrace();
         log.error(se);
      }

      try {
         DefaultHandler dh = new DefaultHandler();
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         DomHelper.DocumentToStream(xmlDoc,baos);
         ByteArrayInputStream bais = new ByteArrayInputStream(baos.
                                                              toByteArray());
         sp.parse(bais, dh);
      }
      catch(SAXException se) {
         se.printStackTrace();
         log.error(se);
      }
      catch(IOException ioe) {
         ioe.printStackTrace();
         log.error(ioe);   
      }
      log.debug("end isValid");
      return true;
   }  
  
   public void updateNoCheck(Document xsDoc) {
      log.debug("start updateNoCheck");

      //log.info("This is xsDoc = " + XMLUtils.DocumentToString(xsDoc));
      NodeList nl = xsDoc.getElementsByTagNameNS("vr","Resource" );
      if(nl.getLength() == 0) {
         nl = xsDoc.getElementsByTagNameNS(
                    "http://www.ivoa.net/xml/VOResource/v0.9","Resource" );
      }
      if(nl.getLength() == 0) {
         nl = xsDoc.getElementsByTagName("Resource" );
      }
      if(nl.getLength() == 0) {
         nl = xsDoc.getElementsByTagName("vr:Resource" );
      }
      String versionNumber = conf.getString("org.astrogrid.registry.version");
      ArrayList al = new ArrayList();
      String xql = null;
      DocumentFragment df = null;
      Node root = null;
      Document resultDoc = null;
      String ident = null;
      String resKey = null;
      boolean addManageError = false;
      String tempIdent = null;

      // This does seem a little strange as if an infinte loop,
      // but later on an appendChild is performed to a DocumentFragment which
      // automatically reduced the length by one.
      // while(nl.getLength() > 0) {
      final int resourceNum = nl.getLength();
      for(int i = 0;i < resourceNum;i++) {         
         ident = getAuthorityID( (Element)nl.item(0));
         resKey = getResourceKey( (Element)nl.item(0));
         Element currentResource = (Element)nl.item(0);

         tempIdent = ident;
         if(resKey != null) tempIdent += "/" + resKey;
                  
         //xql = formUpdateXQLQuery(currentResource,ident,resKey);
         //df = xsDoc.createDocumentFragment();
         //root = xsDoc.getDocumentElement().cloneNode(false);
         root = xsDoc.createElement("AstrogridResource");
         //if(!"VODescription".equals(root.getLocalName())) {
         //   root = xsDoc.createElementNS(
         //         "http://www.ivoa.net/xml/VOResource/v0.9","VODescription");
         //}
         root.appendChild(currentResource);
         //df.appendChild(root);
         RegistryFileHelper.
                          addStatusMessage("Entering new entry: " + tempIdent);
         XQueryExecution.updateQuery("xml","astrogridv" + versionNumber,
                                     tempIdent,root);         
      }//for
      log.debug("end updateNoCheck");
   }
  
   private Node getManagedAuthorityID(Document doc) {
      log.debug("start getManagedAuthorityID");
      
      NodeList nl = doc.getElementsByTagNameNS("vg","ManagedAuthority" );
      //Okay for some reason vg seems to pick up the ManagedAuthority.
      //Lets try to find it by the url namespace.
      if(nl.getLength() == 0) {
         nl = doc.getElementsByTagNameNS(
                                    "http://www.ivoa.net/xml/VORegistry/v0.2",
                                    "ManagedAuthority" );
      }
      if(nl.getLength() == 0) {
         nl = doc.getElementsByTagName("vg:ManagedAuthority" );
      }
      if(nl.getLength() == 0) {
         nl = doc.getElementsByTagName("ManagedAuthority" );
      }      
      if(nl.getLength() > 0)
         return nl.item(0);

      log.debug("end getManagedAuthorityID");
      return null;
   }
  
   private String getAuthorityID(Element doc) {
      log.debug("start getAuthorityID");
     
      NodeList nl = doc.getElementsByTagNameNS("vr","AuthorityID" );
     
      //Okay for some reason vg seems to pick up the ManagedAuthority.
      //Lets try to find it by the url namespace.
      if(nl.getLength() == 0) {
         nl = doc.getElementsByTagNameNS(
                             "http://www.ivoa.net/xml/VOResource/v0.9",
                             "AuthorityID" );
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
      log.debug("end getAuthorityID");
      return nl.item(0).getFirstChild().getNodeValue();
   }
  
   private String getResourceKey(Element doc) {
      log.debug("start getResourceKey");

      NodeList nl = doc.getElementsByTagNameNS("vr","ResourceKey" );
      //Okay for some reason vg seems to pick up the ManagedAuthority.
      //Lets try to find it by the url namespace.
      if(nl.getLength() == 0) {
         nl = doc.getElementsByTagNameNS(
                             "http://www.ivoa.net/xml/VOResource/v0.9",
                             "ResourceKey" );
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
         return ".//*:Identifier/AuthorityID = '" + ident + 
                "' and .//*:Identifier/ResourceKey = '" + resKey + "'";   
      } else {
         return ".//*:Identifier/AuthorityID = '" + ident + "'";
      }
   }
  
//  private String formInvalidAuthorityIDCheckQuery() {
//   String regAuthID = conf.getString(AUTHORITYID_PROPERTY);
//   
//   return ".//@*:type='RegistryType' and .//*:AuthorityID != '" + 
//          regAuthID +"'";   
//  }
  
//   private String formInvalidAuthorityIDCheckQuery() {
//   log.debug("start formInvalidAuthorityIDCheckQuery");
//   String regAuthID = conf.getString(AUTHORITYID_PROPERTY);
//   
//   return ".//@*:type='RegistryType' and .//*:AuthorityID != '" + regAuthID +"'";   
//  }

 /**
   * Takes an XML Document and will either update and insert the data in the
   * registry.  If a client is intending for an insert, but the primarykey 
   * (AuthorityID and ResourceKey) are already in the registry an automatic
   * update will occur.  This method will only update main pieces of 
   * data/elements conforming to the IVOA schema.
   * 
   * Main Pieces: Organisation, Authority, Registry, Resource, Service,
   * SkyService, TabularSkyService, DataCollection 
   * 
   * @param status This DOM object really should be null each time.
   * @return the document updated on the registry is returned.
   * @author Kevin Benson
   * 
   */   
   public Document getStatus(Document status) {

      log.debug("start getStatus");
   
      Document doc = null;
      try {
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().
                                                 newDocumentBuilder();
         doc = registryBuilder.newDocument();
      
         Element elem = doc.createElement("status");
         elem.appendChild(doc.createTextNode(RegistryFileHelper.
                                             getStatusMessage()));
         doc.appendChild(elem);
         log.info("Document returned for Status message = " +
                   DomHelper.DocumentToString(doc));
      } catch (ParserConfigurationException pce){
         pce.printStackTrace();
         log.error(pce);
      }
      log.debug("end getStatus");
      return doc;
   } 
}
