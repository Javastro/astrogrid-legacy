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
import java.util.Date;
import java.text.DateFormat;
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
 * piece of the registry.  This class will handle inserts/updates of
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
   
   /**
    * This method will soon be factored out because it is also in RegistryFileHelper class.
    * @param doc
    * @param tagName
    * @param prefix
    * @return
    */
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
   
   /**
    * Updates a xml document that is a schema.
    * 
    * @param update a DOM object of a xml schema.
    */
   private void updateSchema(Document update) {
      log.debug("start updateSchema");
      //get all the schema tags.
      NodeList nl = getNodeListTags(update,"schema","xs");
      log.info("the nodelist length = " + nl.getLength());
      //if there are none then leave.
      if(nl == null || nl.getLength() == 0) {
         //throw some type of exception;
         return;   
      }
      //go through al the schema elements.
      for(int i = 0;i < nl.getLength();i++) {
         // use the targetnamespace for a unique name to the xml resource in 
         // the exist db.
         Node ident = ((Element)nl.item(i)).
                                           getAttributeNode("targetNamespace");
         String identifier = null;
         if(ident != null && ident.getNodeValue() != null) {
            identifier = ident.getNodeValue();
         }
         //if there was no targetNamespace then use the current time.
         if(identifier == null) {
            identifier = String.valueOf(System.currentTimeMillis());
         }
         log.info("the identifier in updateSchema = " + identifier);
         //update the xml.
         XQueryExecution.updateQuery("xsd","schemas",identifier,nl.item(i));
      }//for
      log.debug("end updateSchema");
   }
   
   /**
    * Updates a stylesheet xsl document into the database.
    * @param update an XML dom object of a stylesheet
    */
   private void updateStyleSheet(Document update) {
      log.debug("start updateStyleSheet");
      //get the stylesheet tags.
      NodeList nl = getNodeListTags(update,"stylesheet","xsl");
      log.info("the nodelist length = " + nl.getLength());
      //if there is no stylesheet elements then return.
      if(nl == null || nl.getLength() == 0) {
         //throw some type of exception;
         return;   
      }
      //Need to make something unique, but for now this is just one
      //style sheet unique to the registry.
      for(int i = 0;i < nl.getLength();i++) {
         XQueryExecution.updateQuery("xsl","xsl","regstylesheet",nl.item(i));
      }//for
      log.debug("end updateSchema");
   }
   
   private final int RESOURCE_TYPE = 0;
   private final int SCHEMA_TYPE =  1;
   private final int STYLESHEET_TYPE = 2;
   
   /**
    * Check the xml DOM object to see if it is a astrogrid resource, schema, or
    * a stylesheet
    * @param update
    * @return
    */
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
   * Determines what type of xml document it is, such as a Resource entry
   * a schema file, or a stylesheet and calls the correct update method for
   * updating to the database.
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
    * Takes an XML Document and will either update and insert the data in the
    * registry.  If a client wants an insert, but the primarykey (AuthorityID
    * and ResourceKey) are already in the registry an automatic update will 
    * occur.  This method will only update main pieces of data/elements
    * conforming to the IVOA schema.
    * 
    * Main Pieces: Organisation, Authority, Registry, Resource, Service, 
    *              SkyService, TabularSkyService, DataCollection 
    * 
    * @param update Document a XML document dom object to be updated on the registry.
    * @return the document updated on the registry is returned.
    * @author Kevin Benson
    * 
    */   
   public Document updateResource(Document update) {
      log.debug("start updateResource");
      long beginUpdate = System.currentTimeMillis();
      // Transform the xml document into a consistent way.
      // xml can come in a few various forms.  This xsl will make it
      // consistent in the db and throughout this registry.
      XSLHelper xs = new XSLHelper();      
      Document xsDoc = xs.transformDatabaseProcess((Node)update.
                                                         getDocumentElement());
      log.info("server side update the xsDoc = " + 
               DomHelper.DocumentToString(xsDoc));
      NodeList nl = getNodeListTags(xsDoc,"Resource","vr");
      
      // Get the managing authority id's and the authority id's that
      // other registries used.
      HashMap manageAuths = RegistryFileHelper.getManagedAuthorities();
      HashMap otherAuths  = RegistryFileHelper.getOtherManagedAuthorities();
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

      final int resourceNum = nl.getLength();
      //go through the various resource entries.
      for(int i = 0;i < resourceNum;i++) {
         long beginQ = System.currentTimeMillis();
         ident = getAuthorityID((Element)nl.item(0));
         resKey = getResourceKey((Element)nl.item(0));
         Element currentResource = (Element)nl.item(0);
         tempIdent = ident;
         if(resKey != null) tempIdent += "/" + resKey;
      
         log.info("serverside update ident = " + ident + " reskey = " + 
                  resKey + " the nl getlenth here = " + nl.getLength());
                  //see if we manage this authority id
                  //if we do then update it in the db.
         if(manageAuths.containsKey(ident)) {         
            root = xsDoc.createElement("AstrogridResource");
            root.appendChild(currentResource);

            RegistryFileHelper.addStatusMessage("Entering new entry: " + 
                                                tempIdent);
            XQueryExecution.updateQuery("xml","astrogridv" + versionNumber,
                                        tempIdent,root);
            XQueryExecution.updateQuery("xml","statv" + versionNumber,
                                        tempIdent,createStats(tempIdent));
         }else {
            // It is not one this registry manages, so check it's attributes
            // and if it is a Registry type then go ahead and update it
            // if it is an authoritytype
            addManageError = true;
            if(currentResource.hasAttributes()) {
               NamedNodeMap nnm = currentResource.getAttributes();
               for(int j = 0;j < nnm.getLength();j++) {
                  Node attrNode = nnm.item(j);
                  String nodeVal = attrNode.getNodeValue();
                  log.info(
                  "Checking NodeVal for an authorityType: current NodeVal = " 
                  + nodeVal);
                  //check if it is a registry type.
                  if(nodeVal != null && nodeVal.indexOf("RegistryType") != -1)
                  {
                     addManageError = false;
                     log.info("This is an RegistryType");
                     root = xsDoc.createElement("AstrogridResource");
                     root.appendChild(currentResource);
                     RegistryFileHelper.addStatusMessage(
                              "Entering new entry: " + tempIdent);
                     //update this registry resource into our registry.
                     XQueryExecution.updateQuery("xml","astrogridv" +
                                                 versionNumber,tempIdent,root);
                     XQueryExecution.updateQuery("xml","statv" +
                                                 versionNumber,tempIdent,
                                                 createStats(tempIdent));
                  }else if(nodeVal != null && 
                           nodeVal.indexOf("AuthorityType") != -1)
                  {
                     // Okay it is an AuthorityType and if no other registries 
                     // manage this authority then we can place it in this 
                     // registry as a new managed authority.
                     if(!otherAuths.containsKey((String)ident)) {
                        log.info(
              "This is an AuthorityType and not managed by other authorities");
                        addManageError = false;
                        // Grab our current Registry resource we need to add
                        // a new managed authority tag.
                        RegistryService rs = new RegistryService();
                        Document loadedRegistry = rs.loadRegistry(null);
                        
                        // Get a ManagedAuthority Node region/area
                        // so we can append a sibling to it, and
                        // use its info for creating another ManagedAuthority
                        // element.

                        Node manageNode = 
                                        getManagedAuthorityID(loadedRegistry);
                        if(manageNode != null) {
                           log.info(
                     "creating new manage element for authorityid = " + ident);
                           // Create a new ManagedAuthority element.
                           Element newManage = loadedRegistry.
                                               createElementNS(
                                                 manageNode.getNamespaceURI(),
                                                 manageNode.getNodeName());
                           // Put in the text node the new ident.
                           newManage.appendChild(loadedRegistry.
                                                 createTextNode(ident));
                           // For some reason the DOM model threw exceptions 
                           // when I tried to insert it as a sibling after 
                           // another existing ManagedAuthority tag, so just 
                           // add it to the end for now.
                           loadedRegistry.getDocumentElement().
                                        getFirstChild().appendChild(newManage);

                           // TODO: Need to check this next line I believe is 
                           // useless.
                           df = xsDoc.createDocumentFragment();
                           
                           // Update our currentResource into the database
                           root = xsDoc.createElement("AstrogridResource");
                           root.appendChild(currentResource);
                           log.info(
                             "running query with new authorityentry = " + xql);
                           RegistryFileHelper.addStatusMessage(
                              "Entering new entry: " + tempIdent);

                           XQueryExecution.updateQuery("xml","astrogridv" +
                                                        versionNumber,
                                                        tempIdent,root);
                           XQueryExecution.updateQuery("xml","statv" +
                                                       versionNumber,
                                                       tempIdent,
                                                       createStats(tempIdent));
                           // Now get the information to re-update the
                           // Registry Resource which is for this registry.
                           ident = getAuthorityID(loadedRegistry.
                                                  getDocumentElement());
                           resKey = getResourceKey(loadedRegistry.
                                                   getDocumentElement());
                           tempIdent = ident;
                           if(resKey != null) tempIdent += "/" + resKey;
                           //TODO: again this next line should not be needed.
                           df = loadedRegistry.createDocumentFragment();
                           
                           Element elem = loadedRegistry.
                                          createElement("AstrogridResource");
                           elem.appendChild(loadedRegistry.
                                            getDocumentElement());
                           XQueryExecution.updateQuery("xml","astrogridv" + 
                                                       versionNumber,tempIdent,
                                                       elem);
                           // reset our hashmap of the managed authorities.
                           // TODO: this is wrong should just add the new 
                           // ident to the hashmap and not re-query the db all
                           // over again.
                           manageAuths = RegistryFileHelper.
                                         doManageAuthorities();
                        }else {
                           // This resource is already owned by another 
                           // Registry.
                           log.info(
                           "IN THE AUTO-INTEGRATION YOU SHOULD NOT GET HERE, "+
                           "Removing Resource and not updating this Resource");
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
      }//for
       
      // Constructs a small RegistryError element with all the
      // errored Resource that was not able to be updated in the db. 
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
               (System.currentTimeMillis() - beginUpdate) + "milliseconds");
      log.debug("end updateResource");
      return update;
   }
 
   /**
    * Not used and Not finished yet, but this will be a validate method to
    * validate the DOM/XML that comes in, using the local schemas.
    * @param xmlDoc
    * @return
    */ 
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
  
   /**
    * Also an update method that updates into this Registry's db. But it does no
    * special checking.  This is used internally by harvesting other registries to
    * go ahead and place the Resources into our db.
    * 
    * @param xsDoc A DOM of XML of  one or more Resources.
    */
   public void updateNoCheck(Document xsDoc) {
      log.debug("start updateNoCheck");

      //log.info("This is xsDoc = " + XMLUtils.DocumentToString(xsDoc));
      //NodeList nl = xsDoc.getElementsByTagNameNS("vr","Resource" );
      NodeList nl = getNodeListTags(xsDoc,"Resource","vr");

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
      // but later on an appendChild is performed which
      // automatically reduced the length by one.
      final int resourceNum = nl.getLength();
      for(int i = 0;i < resourceNum;i++) {
         ident = getAuthorityID( (Element)nl.item(0));
         resKey = getResourceKey( (Element)nl.item(0));
         Element currentResource = (Element)nl.item(0);

         tempIdent = ident;
         if(resKey != null) tempIdent += "/" + resKey;
                  
         root = xsDoc.createElement("AstrogridResource");
         root.appendChild(currentResource);
         RegistryFileHelper.
                          addStatusMessage("Entering new entry: " + tempIdent);
         XQueryExecution.updateQuery("xml","astrogridv" + versionNumber,
                                     tempIdent,root);         
      }//for
      log.debug("end updateNoCheck");
   }

   /**
    * Create statistical data to store in the eXist database when each 
    * managed or registry Resource entry is either created or updated. This
    * will shortly be used to drive Harvesting so that only appropriate
    * entries will be extracted.
    *
    * @param tempIdent The identifier for this Resource
    * @return Node representing the <ResourceStat> Element
    */     
   private Node createStats( String tempIdent ) {
      log.debug("start createStats");
      Date statsTimeMillis = new Date();
      DateFormat shortDT = DateFormat.getDateTimeInstance();
      String statsXML = "<ResourceStat><Identifier>" + tempIdent +
                               "</Identifier><StatsDateMillis>" +
                               statsTimeMillis.getTime() +
                               "</StatsDateMillis><StatsDate>" +
                               shortDT.format(statsTimeMillis) +
                               "</StatsDate></ResourceStat>";
      try {
         log.debug("end createStats");
         return DomHelper.newDocument(statsXML).getDocumentElement();
      }
      catch ( Exception e ) {
      // This will be improved shortly with other Exception handling!
         e.printStackTrace();
         log.error(e);
     }
     return null;
   }

   /**
    * Need to use RegistryFileHelper class to get the NodeList.
    * But leave for the moment.
    * @param doc
    * @return
    */
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
  
   /**
    * Gets the text out of the First authority id element.
    * Need to use REgistryFileHelper class to get the NodeList. It has
    * already these common methods  in it.  Once it gets the NodeList
    * then return the text in the first child. 
    * @param doc xml element normally the full DOM root element.
    * @return AuthorityID text
    */
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

   /**
    * Gets the text out of the First ResourceKey element.
    * Need to use REgistryFileHelper class to get the NodeList. It has
    * already these common methods  in it.  Once it gets the NodeList
    * then return the text in the first child. 
    * @param doc xml element normally the full DOM root element.
    * @return ResourceKey text
    */  
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
  
   /**
    * NOT USED ANYMORE.
    * Leaving in for the moment, and will shortly delete.
    * 
    * @param doc
    * @param ident
    * @param resKey
    * @return
    */
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
//   return ".//@*:type='RegistryType' and .//*:AuthorityID != '" + 
//           regAuthID +"'";   
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
