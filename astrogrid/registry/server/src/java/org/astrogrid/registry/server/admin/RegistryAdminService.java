package org.astrogrid.registry.server.admin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.astrogrid.registry.server.RegistryServerHelper;
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
import org.astrogrid.registry.server.harvest.RegistryHarvestService;
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
import org.apache.axis.AxisFault;
import org.astrogrid.xmldb.eXist.server.UpdateDBService;

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
public class RegistryAdminService {
                          
   private static final Log log = 
                               LogFactory.getLog(RegistryAdminService.class);
   public static Config conf = null;
   
   private static final String AUTHORITYID_PROPERTY = 
                               "org.astrogrid.registry.authorityid";
   private static HashMap manageAuths, otherAuths;

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         manageAuths = new HashMap();
         otherAuths = new HashMap();
      }      
   }
   
   public Document harvestResource(Document resources)  throws AxisFault {
       RegistryHarvestService rhs = new RegistryHarvestService();
       try {   
          rhs.harvestResource(resources,null);
       }catch(IOException ioe) {
          throw new AxisFault("IOE problem",ioe);
       }catch(RegistryException re) {
        throw new AxisFault("Registry exception", re);
       }
       return resources;      
   }
   
   
   
   /**
   * Determines what type of xml document it is, such as a Resource entry
   * a schema file, or a stylesheet and calls the correct update method for
   * updating to the database.
   */   
   public Document Update(Document update) throws AxisFault {
      log.debug("start update");
      log.info("entered update on server side");
      return updateResource(update);
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
   public Document updateResource(Document update) throws AxisFault {
      log.debug("start updateResource");
      long beginUpdate = System.currentTimeMillis();

      String authorityID = conf.getString("org.astrogrid.registry.authorityid");
      
      // Transform the xml document into a consistent way.
      // xml can come in a few various forms.  This xsl will make it
      // consistent in the db and throughout this registry.
      XSLHelper xs = new XSLHelper();      
      Document xsDoc = null;
      
      //Okay we need to get the vr attribute namespace.
      //The confusing part is since we are using message style there is a chance it 
      //could be in the root element (which might be the web service operation name)
      //hence we need to look at the root element, its fist child, and lastly
      //the child of that node
      //ex of possible message style input: 
      //<update><VoResource xmlns:vr=... ><Resource xmlns:vr=...>
      //the vr attribute can live at either or both of those elments and we just need to get the first one.
      //It is possible the <update> element will not be there hence we need to look at the root element
      String attrVersion = null;
      attrVersion = DomHelper.getNodeAttrValue((Element)update.getDocumentElement(),"vr","xmlns");
      if(attrVersion == null || attrVersion.trim().length() == 0) {
         attrVersion = DomHelper.getNodeAttrValue(
             (Element)update.getDocumentElement().getFirstChild(),"vr","xmlns");
         if(attrVersion == null || attrVersion.trim().length() == 0) {
            attrVersion = DomHelper.getNodeAttrValue(
                     (Element)update.getDocumentElement().getFirstChild().getFirstChild(),"vr","xmlns");
         }
      }
      if(attrVersion == null || attrVersion.trim().length() == 0) {
          throw new AxisFault("Could not find version");
      }
      String defaultNS = null;
      String vrNS = attrVersion;
      attrVersion = attrVersion.substring((attrVersion.lastIndexOf("v")+1));
      String versionNumber = attrVersion.replace('.','_');
      log.info("grabbed this version number off the xml" + versionNumber);      
      boolean hasStyleSheet = false;
      
      hasStyleSheet = conf.getBoolean("org.astrogrid.registry.updatestylesheet." + versionNumber,false);
      
      if(hasStyleSheet) {
          System.out.println("lets call transform update");
          xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),versionNumber);
      } else {
         xsDoc = update;
      }

      UpdateDBService udbService = new UpdateDBService();
      String collectionName = "astrogridv" + versionNumber;
      log.info("server side update the xsDoc = " + 
               DomHelper.DocumentToString(xsDoc));
      NodeList nl = null;
      try {
         nl = DomHelper.getNodeListTags(xsDoc,"Resource","vr");
      } catch(IOException ioe) {
         throw new AxisFault("No Resources to update");
      }
      
      try {
         manageAuths = RegistryServerHelper.getManagedAuthorities();
      }catch(SAXException se) {
         //throw new AxisFault("Could not parse xml for getting the Managed Authorities", se);
      }catch(MalformedURLException me) {
       //  throw new AxisFault("Could not construct url to the query database", me);
      }catch(ParserConfigurationException pce) {
       //  throw new AxisFault("Server configuration error", pce);
      }catch(IOException ioe) {
       //  throw new AxisFault("IO problem", ioe);   
      }
      try {
         otherAuths = RegistryServerHelper.getOtherManagedAuthorities();
      }catch(SAXException se) {
         //throw new AxisFault("Could not parse xml for getting the Managed Authorities", se);
      }catch(MalformedURLException me) {
         //  throw new AxisFault("Could not construct url to the query database", me);
      }catch(ParserConfigurationException pce) {
         //  throw new AxisFault("Server configuration error", pce);
      }catch(IOException ioe) {
         //  throw new AxisFault("IO problem", ioe);   
      }
      
      ArrayList al = new ArrayList();
      String xql = null;
      DocumentFragment df = null;
      Node root = null;
      Document resultDoc = null;
      String ident = null;
      String resKey = null;
      String tempIdent = null;
      boolean addManageError = false;
      
      log.info("here is the nl length = " + nl.getLength() + 
               " and manauths size = " + manageAuths.size() + 
               " and otherAuths size = " + otherAuths.size());
      
      final int resourceNum = nl.getLength();
      System.out.println("THE VRns = " + vrNS);
      //go through the various resource entries.
      for(int i = 0;i < resourceNum;i++) {
         long beginQ = System.currentTimeMillis();
         ident = getAuthorityID((Element)nl.item(0));
         resKey = getResourceKey((Element)nl.item(0));
         Element currentResource = (Element)nl.item(0);
         if(!"vr".equals(currentResource.getPrefix())) {
             currentResource.setAttribute("xmlns:vr",vrNS);
         }
         if(defaultNS == null && currentResource.getParentNode() != null) {
             defaultNS = DomHelper.getNodeAttrValue((Element)currentResource.getParentNode(),"xmlns");
             System.out.println("THE DEFAULTNS = " + defaultNS);
         }
         if(defaultNS != null && defaultNS.trim().length() > 0) {
             System.out.println("SETTING DEFAULTNS = " + defaultNS);
             currentResource.setAttribute("xmlns",defaultNS);
         }
         
         tempIdent = ident;
         if(resKey != null) tempIdent += "/" + resKey;
      
         log.info("serverside update ident = " + ident + " reskey = " + 
                  resKey + " the nl getlenth here = " + nl.getLength());
                  //see if we manage this authority id
                  //if we do then update it in the db.
         if(manageAuths.containsKey(ident)) {         
            root = xsDoc.createElement("AstrogridResource");
            root.appendChild(currentResource);

            RegistryServerHelper.addStatusMessage("Entering new entry: " + 
                                                tempIdent);
            try {
               udbService.updateQuery(tempIdent,"xml",collectionName,root);
               udbService.updateQuery(tempIdent,"xml","statv" + versionNumber,createStats(tempIdent));
            } catch(MalformedURLException mue) {
               log.error(mue);
               throw new AxisFault("Malformed URL on the update", mue);
            } catch(IOException ioe) {
               log.error(ioe);
               throw new AxisFault("IO problem", ioe);
            }

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
                     RegistryServerHelper.addStatusMessage(
                              "Entering new entry: " + tempIdent);
                     //update this registry resource into our registry.
                     try {
                        udbService.updateQuery(tempIdent,"xml",collectionName,root);
                        udbService.updateQuery(tempIdent,"xml","statv" + versionNumber,createStats(tempIdent));
                     } catch(MalformedURLException mue) {
                        log.error(mue);
                        throw new AxisFault("Malformed URL on the update", mue);
                     } catch(IOException ioe) {
                        log.error(ioe);
                        throw new AxisFault("IO problem", ioe);
                     }
                     if(authorityID.equals(ident)) {
                        manageAuths.put(ident,null);                        
                     }else {
                        otherAuths.put(ident,null);
                     }
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
                           RegistryServerHelper.addStatusMessage(
                              "Entering new entry: " + tempIdent);
                           try {
                              udbService.updateQuery(tempIdent,"xml",collectionName,root);
                              udbService.updateQuery(tempIdent,"xml","statv" + versionNumber,createStats(tempIdent));
                           } catch(MalformedURLException mue) {
                              log.error(mue);
                              throw new AxisFault("Malformed URL on the update", mue);
                           } catch(IOException ioe) {
                              log.error(ioe);
                              throw new AxisFault("IO problem", ioe);
                           }

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
                           try {
                              udbService.updateQuery(tempIdent,"xml",collectionName,elem);                                            
                           } catch(MalformedURLException mue) {
                              log.error(mue);
                              throw new AxisFault("Malformed URL on the update", mue);
                           } catch(IOException ioe) {
                              log.error(ioe);
                              throw new AxisFault("IO problem", ioe);
                           }
                           
                           //XQueryExecution.updateQuery("xml","astrogridv" + 
                           //                            versionNumber,tempIdent,
                           //                            elem);
                           // reset our hashmap of the managed authorities.
                           // TODO: this is wrong should just add the new 
                           // ident to the hashmap and not re-query the db all
                           // over again.
                           manageAuths.put(ident,null);
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
       
      // Constructgs a small RegistryError element with all the
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
   public void updateNoCheck(Document update) throws MalformedURLException, IOException {
      log.debug("start updateNoCheck");

      //log.info("This is xsDoc = " + XMLUtils.DocumentToString(xsDoc));
      //NodeList nl = xsDoc.getElementsByTagNameNS("vr","Resource" );

      ArrayList al = new ArrayList();
      String xql = null;
      DocumentFragment df = null;
      Node root = null;
      Document resultDoc = null;
      String ident = null;
      String resKey = null;
      boolean addManageError = false;
      String tempIdent = null;
      Document xsDoc = null;

      XSLHelper xs = new XSLHelper();      
      
      
      //Okay we need to get the vr attribute namespace.
      //The confusing part is since we are using message style there is a chance it 
      //could be in the root element (which might be the web service operation name)
      //hence we need to look at the root element, its fist child, and lastly
      //the child of that node
      //ex of possible message style input: 
      //<update><VoResource xmlns:vr=... ><Resource xmlns:vr=...>
      //the vr attribute can live at either or both of those elments and we just need to get the first one.
      //It is possible the <update> element will not be there hence we need to look at the root element
      String attrVersion = DomHelper.getNodeAttrValue((Element)update.getDocumentElement(),"vr","xmlns");
      if(attrVersion == null || attrVersion.trim().length() == 0) {
          attrVersion = DomHelper.getNodeAttrValue(
                  (Element)update.getDocumentElement().getFirstChild(),"vr","xmlns");
          if(attrVersion == null || attrVersion.trim().length() == 0) {
              attrVersion = DomHelper.getNodeAttrValue(
                      (Element)update.getDocumentElement().getFirstChild().getFirstChild(),"vr","xmlns");
          }
      }
      if(attrVersion == null || attrVersion.trim().length() == 0) {
          throw new IOException("Could not find version");
      }
      attrVersion = attrVersion.substring((attrVersion.lastIndexOf("v")+1));
      String versionNumber = attrVersion.replace('.','_');
      System.out.println("the version number found = " + versionNumber);
      
      NodeList nl = DomHelper.getNodeListTags(xsDoc,"Resource","vr");      
      String collectionName = "astrogridv" + versionNumber;

      boolean hasStyleSheet = false;
      
      hasStyleSheet = conf.getBoolean("org.astrogrid.registry.updatestylesheet." + versionNumber,false);
      
      if(hasStyleSheet) {
          xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),versionNumber);
      } else {
         xsDoc = update;
      }
      
      
      UpdateDBService udbService = new UpdateDBService();

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
         RegistryServerHelper.
                          addStatusMessage("Entering new entry: " + tempIdent);
         udbService.updateQuery(tempIdent,"xml",collectionName,root);                                                
                          
        // XQueryExecution.updateQuery("xml","astrogridv" + versionNumber,
        //                             tempIdent,root);         
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
    * Need to use RegistryServerHelper class to get the NodeList.
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
    * Need to use RegistryServerHelper class to get the NodeList. It has
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
    * Need to use RegistryServerHelper class to get the NodeList. It has
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
   public Document getStatus(Document status) throws AxisFault {

      log.debug("start getStatus");
   
      Document doc = null;
      try {
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().
                                                 newDocumentBuilder();
         doc = registryBuilder.newDocument();
      
         Element elem = doc.createElement("status");
         elem.appendChild(doc.createTextNode(RegistryServerHelper.
                                             getStatusMessage()));
         doc.appendChild(elem);
         log.info("Document returned for Status message = " +
                   DomHelper.DocumentToString(doc));
      } catch (ParserConfigurationException pce){
         log.error(pce);
         throw new AxisFault("Parser config problem", pce);
      } catch (IOException ioe) {
         log.error(ioe);
         throw new AxisFault("IO problem", ioe);
      }
      log.debug("end getStatus");
      return doc;
   } 
}
