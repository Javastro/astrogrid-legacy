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
 * piece of the registry.  This class will handle inserts/updates Ressources
 * in the registry.
 * 
 * @see org.astrogrid.registry..common.RegistryAdminInterface
 * @author Kevin Benson
 * 
 */
public class RegistryAdminService {
                          
    /**
     * Logging variable for writing information to the logs
     */
   private static final Log log = 
                               LogFactory.getLog(RegistryAdminService.class);

   /**
    * conf - Config variable to access the configuration for the server normally
    * jndi to a config file.
    * @see org.astrogrid.config.Config
    */   
   public static Config conf = null;

   /**
    * final variable for the default AuthorityID associated to this registry.
    */   
   private static final String AUTHORITYID_PROPERTY = 
                               "org.astrogrid.registry.authorityid";

   /**
    * Hashmap of the Authories managed by this registry, and the authority
    * ids managed by other registries. Used for determining if things are valid
    * for updating to this registry and verify it is not owned by another registry.
    */   
   private static HashMap manageAuths, otherAuths;

   /**
    * Static to be used on the initiatian of this class for the config
    */
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
    * Update Web Service method, performs an update through all the Resources of
    * a Registry.
    * 
    * @param update XML DOM containing Resource XML elements
    * @return XML of identifiers that did not get updated otherwise nothing
    * if all were successfull.
    * @throws AxisFault if an error goes wrong trying to update into the
    * XML database or parsing XML.
    */
   public Document Update(Document update) throws AxisFault {
      log.debug("start update");
      return updateResource(update);
   }
   
   /**
    * Takes an XML Document and will either update and insert the data in the
    * registry.  If a client wants an insert, but the primarykey (AuthorityID
    * and ResourceKey) are already in the registry an automatic update will 
    * occur.  This method will only update XML Resource elements.
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
      log.info("Default AuthorityID for this Registry = " + authorityID);
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
      String defaultNS = null;
            
      boolean hasStyleSheet = false;
      
      //If there is a stylesheet placed in the astrogrid-registry-common.jar
      //with aparticular name for that version it will perform an XSL
      //translations.  Currently not used.
      
      UpdateDBService udbService = new UpdateDBService();
      
      
      //Get all the Resource elements
      NodeList nl = update.getElementsByTagNameNS("*","Resource");
      /*
      try {
         nl = DomHelper.getNodeListTags(update,"Resource","vr");
      } catch(IOException ioe) {
         log.error("IO error trying to find Resources");
         throw new AxisFault("IO Error when retrieving Resource nodes.");
      }
      */
      if(nl.getLength() == 0) {
          log.error("No Resources found to be updated");
          throw new AxisFault("No Resources found to be updated");
      }
      String attrVersion = RegistryServerHelper.getRegistryVersionFromNode(nl.item(0));
      log.info("Registry Version being updated = " + attrVersion);      
      String vrNS = "http://www.ivoa.net/xml/VOResource/v" + attrVersion;
      log.info("Registry namespace being updated = " + vrNS);
      String versionNumber = attrVersion.replace('.','_');
      String collectionName = "astrogridv" + versionNumber;
      log.info("Collection Name = " + collectionName);
      
      

      hasStyleSheet = conf.getBoolean("org.astrogrid.registry.updatestylesheet." + versionNumber,false);
      if(hasStyleSheet) {
         //System.out.println("lets call transform update");
         log.info("performing transformation before analysis of update for versionNumber = " + versionNumber);
         xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),versionNumber);
      } else {
         xsDoc = update;
      }

      nl = xsDoc.getElementsByTagNameNS("*","Resource");
      /*
      try {
          nl = DomHelper.getNodeListTags(xsDoc,"Resource","vr");
      } catch(IOException ioe) {
          throw new AxisFault("IO Error when retrieving Resource nodes.");
      }
      */
      log.info("Number of Resources = " + nl.getLength());
      log.info("server side update the xsDoc = " + 
              DomHelper.DocumentToString(xsDoc));      
      
      if(manageAuths.get(versionNumber) == null)
          populateManagedMaps(collectionName, versionNumber);
      
      if(otherAuths.get(versionNumber) == null)
          populateOtherManagedMaps(collectionName, versionNumber);
      
      if(manageAuths.get(versionNumber) == null) {
          //okay this must be the very first time into the registry where
          //registry is empty. So put a an empty entry for this version.
          manageAuths.put(versionNumber,new HashMap());
      }
      if(otherAuths.get(versionNumber) == null) {
          //okay this must be the very first time into the registry where
          //registry is empty. So put a an empty entry for this version.
          otherAuths.put(versionNumber,new HashMap());
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
      String manageNodeVal = null;
      
      //log.info("here is the nl length = " + nl.getLength());
      
      final int resourceNum = nl.getLength();
      //go through the various resource entries.
      for(int i = 0;i < resourceNum;i++) {
         long beginQ = System.currentTimeMillis();
         //Looks sort of odd always getting item at number 0
         //this is because we append it to another element later
         //hence the nodelist is like a queue and no longer
         //has the current resource and moves everything up the queue
         //get the iden and resource key elements.
         ident = getAuthorityID((Element)nl.item(0));
         resKey = getResourceKey((Element)nl.item(0));
         //set the currentResource element.
         Element currentResource = (Element)nl.item(0);
         //Sometimes there are other elements defined above the
         //Resource element so be sure this Resource element has a
         //defined vr namespace if it does not have one already. 
         if(!"vr".equals(currentResource.getPrefix())) {
             currentResource.setAttribute("xmlns:vr",vrNS);
         }
         //same goes for default namespaces make sure there is one set
         //on the Resource element otherwise we might get XML where elements
         //don't have a defined default namespace.
         if(defaultNS == null) {
             defaultNS = DomHelper.getNodeAttrValue((Element)currentResource,"xmlns");             
         }//if
         if((defaultNS == null || defaultNS.trim().length() == 0) && currentResource.getParentNode() != null) {
             defaultNS = DomHelper.getNodeAttrValue((Element)currentResource.getParentNode(),"xmlns");
         }//if
         if(defaultNS != null && defaultNS.trim().length() > 0) {
             currentResource.setAttribute("xmlns",defaultNS);
         }//if             
         
         
         //set a temporary identifier.
         tempIdent = ident;
         if(resKey != null) tempIdent += "/" + resKey;
      
         log.info("serverside update ident = " + ident + " reskey = " + 
                  resKey + " the nl getlenth here = " + nl.getLength());
                  //see if we manage this authority id
                  //if we do then update it in the db.
         //doe we manage this authority id, if so then add the resource.
         if(manageAuths.get(versionNumber) != null &&
            ((HashMap)manageAuths.get(versionNumber)).containsKey(ident)) {
            //Essentially chop off other elemens wrapping the Resource element, and put
            //our own element. Would have been nice just to store the Resource element
            //at the root level, but it seems the XQueries on the database have problems
            //with that.
            root = xsDoc.createElement("AstrogridResource");
            root.appendChild(currentResource);

            RegistryServerHelper.addStatusMessage("Entering new entry: " + 
                                                tempIdent);
            try {
               //update the resource element.
               udbService.updateQuery(tempIdent,"xml",collectionName,root);
               //we want to keep a little bit of stats information on the
               //Resources managed.
               udbService.updateQuery(tempIdent,"xml","statv" + versionNumber,
                                      createStats(tempIdent));
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
                  if(nodeVal != null && nodeVal.indexOf("Registry") != -1)
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
                        if(authorityID.equals(ident)) {
                            udbService.updateQuery(tempIdent,"xml","statv" + versionNumber,createStats(tempIdent));
                        }else {
                            udbService.updateQuery(tempIdent,"xml","statv" + versionNumber,createStats(tempIdent,false));
                        }
                     } catch(MalformedURLException mue) {
                        log.error(mue);
                        throw new AxisFault("Malformed URL on the update", mue);
                     } catch(IOException ioe) {
                        log.error(ioe);
                        throw new AxisFault("IO problem", ioe);
                     }
                     NodeList manageList = getManagedAuthorities(currentResource);                     
                     if(authorityID.equals(ident)) {
                        ((HashMap)manageAuths.get(versionNumber)).put(ident,null);
                        for(int k = 0;k < manageList.getLength();k++) {
                            manageNodeVal = manageList.item(k).getFirstChild().getNodeValue();
                            if(manageNodeVal != null && manageNodeVal.trim().length() > 0) {
                                ((HashMap)manageAuths.get(versionNumber)).put(manageNodeVal,null);
                            }//if
                        }//for
                     }else {
                        ((HashMap)otherAuths.get(versionNumber)).put(ident,null);
                        for(int k = 0;k < manageList.getLength();k++) {
                            manageNodeVal = manageList.item(k).getFirstChild().getNodeValue();
                            if(manageNodeVal != null && manageNodeVal.trim().length() > 0) {
                                /*
                                 Need to think about this a little more.
                                if(((HashMap)otherAuths.get(versionNumber)).containsKey(manageNodeVal)) {
                                    log.error(
                                    "Update Successfull, but when trying to update a Registry with authority id = " +
                                    ident + "; Found an AuthorityID already owned by another Registry, " +
                                    "the authority id in conflict is: " + manageNodeVal);
                                    al.add(
                                      "Update Successfull, but when trying to update a Registry with authority id = " +
                                      ident + "; Found an AuthorityID already owned by another Registry, " +
                                      "the authority id in conflict is: " + manageNodeVal);
                                }
                                */
                                ((HashMap)otherAuths.get(versionNumber)).put(manageNodeVal,null);
                            }//if
                        }//for
                     }
                  }else if(nodeVal != null && 
                           nodeVal.indexOf("Authority") != -1)
                  {
                     // Okay it is an AuthorityType and if no other registries 
                     // manage this authority then we can place it in this 
                     // registry as a new managed authority.
                     if(otherAuths.get(versionNumber) == null ||
                        (otherAuths.get(versionNumber) != null &&
                        !((HashMap)otherAuths.get(versionNumber)).containsKey((String)ident))) {
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
                           
                           //System.out.println("the loadedREgistry in admin service = " + DomHelper.DocumentToString(loadedRegistry));
                           // For some reason the DOM model threw exceptions 
                           // when I tried to insert it as a sibling after 
                           // another existing ManagedAuthority tag, so just 
                           // add it to the end for now.
                           NodeList resListForAuth = loadedRegistry.getElementsByTagNameNS("*","Resource");
                           resListForAuth.item(0).appendChild(newManage);
                           ((HashMap)manageAuths.get(versionNumber)).put(ident,null);
                           //System.out.println("the loadedREgistry in admin service2 = " + DomHelper.DocumentToString(loadedRegistry));
                           //loadedRegistry.getDocumentElement().
                           //             getFirstChild().appendChild(newManage);

                           // TODO: Need to check this next line I believe is 
                           // useless.
                           //df = xsDoc.createDocumentFragment();
                           
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
                           ((HashMap)manageAuths.get(versionNumber)).put(ident,null);
                        }else {
                           log.error("Removing child - but somehow the Registries" +
                                     " main RegistryType has no ManagedAuthority");
                           // This resource is already owned by another 
                           // Registry.
                           log.error(
                           "IN THE AUTO-INTEGRATION YOU SHOULD NOT GET HERE, " +
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
               log.info("Found entry not managed by this Registry = " + ident);
               xsDoc.getDocumentElement().removeChild(currentResource);
            }//if
         }//else
         log.info("Time taken to update an entry = " + 
                  (System.currentTimeMillis() - beginQ) +
                  " for ident  = " + tempIdent);
      }//for
      
      Document returnDoc = null;      
      // Constructgs a small RegistryError element with all the
      // errored Resource that was not able to be updated in the db.
      try {      
          if(al != null && al.size() > 0) {
             
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
          } else {
              returnDoc = DomHelper.newDocument();
              returnDoc.appendChild(returnDoc.createElement("UpdateResponse"));              
          }          
      }catch(ParserConfigurationException pce) {
          pce.printStackTrace();
          log.error(pce);   
      }
      
      log.info("Time taken to complete update on server = " +
               (System.currentTimeMillis() - beginUpdate) + "milliseconds");
      log.debug("end updateResource");

      return returnDoc;
   }
   
   private void populateOtherManagedMaps(String collectionName, String versionNumber) {
       log.debug("start populateOtherManagedMaps");
       HashMap versionOtherManaged = null;
       //get All the Managed Authorities, the getOtherManagedAutories() does not
       //perform a query every time only once.
       try {
          //otherAuths = RegistryServerHelper.getOtherManagedAuthorities(collectionName, versionNumber);
           versionOtherManaged = RegistryServerHelper.getOtherManagedAuthorities(collectionName, versionNumber);
           log.info("found other managed authorities size = " + 
                   versionOtherManaged.size() + " for registry version = "
                     + versionNumber);           
           otherAuths.put(versionNumber,versionOtherManaged);
       }catch(SAXException se) {
           //hmmm need to relook the first time with an empty eXist db an
           //exception is okay, but probably only IOException. 
       }catch(MalformedURLException me) {
           //hmmm need to relook the first time with an empty eXist db an
           //exception is okay, but probably only IOException. 
       }catch(ParserConfigurationException pce) {
           //hmmm need to relook the first time with an empty eXist db an
           //exception is okay, but probably only IOException.            
       }catch(IOException ioe) {
           //hmmm need to relook the first time with an empty eXist db an
           //exception is okay, but probably only IOException.   
       }
       log.debug("end populateOtherManagedMaps");
   }
   
   private void populateManagedMaps(String collectionName, String versionNumber) {
       log.debug("start populateManagedMaps");       
       //get All the Managed Authorities, the getManagedAutories() does not
       //perform a query every time only once.
       HashMap versionManaged = null;

       try {
          //manageAuths = RegistryServerHelper.getManagedAuthorities(collectionName, versionNumber);
           versionManaged = RegistryServerHelper.getManagedAuthorities(collectionName, versionNumber);
           log.info("found managed authorities size = " + versionManaged.size()
                    + " for registry version = " + versionNumber);
           manageAuths.put(versionNumber,versionManaged);
       }catch(SAXException se) {
         //hmmm need to relook the first time with an empty eXist db an
         //exception is okay, but probably only IOException. 
       }catch(MalformedURLException me) {
           //hmmm need to relook the first time with an empty eXist db an
           //exception is okay, but probably only IOException. 
       }catch(ParserConfigurationException pce) {
           //hmmm need to relook the first time with an empty eXist db an
           //exception is okay, but probably only IOException. 
       }catch(IOException ioe) {
           //hmmm need to relook the first time with an empty eXist db an
           //exception is okay, but probably only IOException.    
       }
       log.debug("end populateManagedMaps");
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
    * @param update A DOM of XML of  one or more Resources.
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
      //NodeList nl = DomHelper.getNodeListTags(update,"Resource","vr");
      NodeList nl = update.getElementsByTagNameNS("*","Resource");
      log.info("the nl length of resoruces = " + nl.getLength());
      String attrVersion = conf.getString("org.astrogrid.registry.version");
      if(nl.getLength() > 0) {
          attrVersion = RegistryServerHelper.getRegistryVersionFromNode(nl.item(0)); 
      }
      String vrNS = "http://www.ivoa.net/xml/VOResource/v" + attrVersion;
      String versionNumber = attrVersion.replace('.','_');      
      String collectionName = "astrogridv" + versionNumber;
      String defaultNS = null;
      log.info("Collection Name = " + collectionName);
      
      boolean hasStyleSheet = conf.getBoolean("org.astrogrid.registry.updatestylesheet." + versionNumber,false);
      Document xsDoc = null;
      if(hasStyleSheet) {
         //System.out.println("lets call transform update");
         log.info("performing transformation before analysis of update for versionNumber = " + versionNumber);
         xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),versionNumber);
      } else {
         xsDoc = update;
      }
      nl = xsDoc.getElementsByTagNameNS("*","Resource");
      
      //System.out.println("the xsdoc in updateNocheck = " + DomHelper.DocumentToString(xsDoc));
      log.info("Number of Resources = " + nl.getLength());
      UpdateDBService udbService = new UpdateDBService();
      
      if(manageAuths.get(versionNumber) == null)
          populateManagedMaps(collectionName, versionNumber);
      
      if(otherAuths.get(versionNumber) == null)
          populateOtherManagedMaps(collectionName, versionNumber);

      

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
         
         if(!"vr".equals(currentResource.getPrefix())) {
             currentResource.setAttribute("xmlns:vr",vrNS);
         }
         if(defaultNS == null) {
             defaultNS = DomHelper.getNodeAttrValue((Element)currentResource,"xmlns");             
         }
         if((defaultNS == null || defaultNS.trim().length() == 0) && currentResource.getParentNode() != null) {
             defaultNS = DomHelper.getNodeAttrValue((Element)currentResource.getParentNode(),"xmlns");
         }
         if(defaultNS != null && defaultNS.trim().length() > 0) {
             currentResource.setAttribute("xmlns",defaultNS);
         }             
         //root = update.createElement("AstrogridResource");
         root = xsDoc.createElement("AstrogridResource");
         root.appendChild(currentResource);
         RegistryServerHelper.
             addStatusMessage("Entering new entry: " + tempIdent);
         udbService.updateQuery(tempIdent,"xml",collectionName,root);                                                
                          
         if(currentResource.hasAttributes()) {
             NamedNodeMap nnm = currentResource.getAttributes();
             for(int j = 0;j < nnm.getLength();j++) {
                 Node attrNode = nnm.item(j);
                 String nodeVal = attrNode.getNodeValue();
                 //check if it is a registry type.
                 if(nodeVal != null && nodeVal.indexOf("Registry") != -1)
                 {
                    log.info("A RegistryType in updateNoCheck add stats");
                    //update this registry resource into our registry.
                    try {
                       log.info("UPDADING STATS FROM HARVEST = " + tempIdent);
                       udbService.updateQuery(tempIdent,"xml","statv" + versionNumber,createStats(tempIdent));
                    } catch(MalformedURLException mue) {
                       log.error(mue);
                       throw new AxisFault("Malformed URL on the update", mue);
                    } catch(IOException ioe) {
                       log.error(ioe);
                       throw new AxisFault("IO problem", ioe);
                    }//try
                    if(otherAuths != null) {
                       ((HashMap)otherAuths.get(versionNumber)).put(ident,null);
                       NodeList manageList = getManagedAuthorities(currentResource);
                       for(int k = 0;k < manageList.getLength();k++) {
                           String manageNodeVal = manageList.item(k).getFirstChild().getNodeValue();
                           if(manageNodeVal != null && manageNodeVal.trim().length() > 0) {
                               ((HashMap)otherAuths.get(versionNumber)).put(manageNodeVal,null);
                           }
                       }
                    }//if
                 }//if                 
             }//for
         }//if
      }//if
      log.debug("end updateNoCheck");
   }
   
   
   private Node createStats( String tempIdent, boolean addMillis) {
       log.debug("start createStats");
       Date statsTimeMillis = new Date();
       DateFormat shortDT = DateFormat.getDateTimeInstance();
       String statsXML = "<ResourceStat><Identifier>" + tempIdent +
                                "</Identifier>";
       if(addMillis) {
           statsXML += "<StatsDateMillis>" +
                       statsTimeMillis.getTime() +
                       "</StatsDateMillis>";
       }
       statsXML += "<StatsDate>" +
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
    * Create statistical data to store in the eXist database when each 
    * managed or registry Resource entry is either created or updated. This
    * will shortly be used to drive Harvesting so that only appropriate
    * entries will be extracted.
    *
    * @param tempIdent The identifier for this Resource
    * @return Node representing the <ResourceStat> Element
    */     
   private Node createStats( String tempIdent ) {
       return createStats(tempIdent,true);
   }

   /**
    * Need to use RegistryServerHelper class to get the NodeList.
    * But leave for the moment.
    * @param doc
    * @return
    */
   private Node getManagedAuthorityID(Document doc) {
      log.debug("start getManagedAuthorityID");
      //try {
          NodeList nl = doc.getElementsByTagNameNS("*","ManagedAuthority");
          //NodeList nl = DomHelper.getNodeListTags(doc,"ManagedAuthority","vg");
          if(nl.getLength() > 0)
             return nl.item(0);
          log.debug("end getManagedAuthorityID");
      //}catch(IOException ioe) {
          //ioe.printStackTrace();
          //log.error(ioe);
      //}
      return null;
   }
   
   private NodeList getManagedAuthorities(Element regNode) {
       log.debug("start getManagedAuthorityID");
       //NodeList nl = null;
       NodeList nl = regNode.getElementsByTagNameNS("*","ManagedAuthority");
       /*
       try {
           
           nl = DomHelper.getNodeListTags(regNode,"ManagedAuthority","vg");
           log.debug("end getManagedAuthorityID");
       }catch(IOException ioe) {
           ioe.printStackTrace();
           log.error(ioe);
       }*/
       return nl;
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
      NodeList nl = doc.getElementsByTagNameNS("*","Identifier" );
      String val = null;
      if(nl.getLength() == 0) {
          nl = doc.getElementsByTagNameNS("*","identifier" );
          if(nl.getLength() == 0)
              return null;
      }
    
      NodeList authNodeList = ((Element)nl.item(0)).getElementsByTagNameNS("*","AuthorityID");
      
      if(authNodeList.getLength() == 0) {
          val = nl.item(0).getFirstChild().getNodeValue();
          if(val.indexOf("/") != -1) 
              return val.substring(0,val.indexOf("/"));
      }
      return authNodeList.item(0).getFirstChild().getNodeValue();
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
       NodeList nl = doc.getElementsByTagNameNS("*","Identifier" );
       if(nl.getLength() == 0) {
           nl = doc.getElementsByTagNameNS("*","identifier" );
           if(nl.getLength() == 0)
               return null;
       }
       NodeList resNodeList = ((Element)nl.item(0)).getElementsByTagNameNS("*","ResourceKey");
       String val = null;
       if(resNodeList.getLength() == 0) {
           val = nl.item(0).getFirstChild().getNodeValue();
           if(val.indexOf("/") != -1) 
               return val.substring(val.indexOf("/")+1);
       }
       if(resNodeList.item(0).hasChildNodes())
           return resNodeList.item(0).getFirstChild().getNodeValue();
       //it is just an empty ResourceKey which is okay.
       return "";
   }
  
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
