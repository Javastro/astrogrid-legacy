package org.astrogrid.registry.server.admin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;
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
import org.astrogrid.registry.common.RegistryValidator;
import org.astrogrid.util.DomHelper;
import java.util.ArrayList;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.server.query.RegistryQueryService;
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
import junit.framework.AssertionFailedError;
import java.text.SimpleDateFormat;


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
      
      
      UpdateDBService udbService = new UpdateDBService();
      
      
      if(update == null) {
          throw new AxisFault("Nothing on request 'null sent'");
      }
      
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      Date updateDate = new Date();
      String updateDateString = sdf.format(updateDate);
      
      //Get all the Resource elements
      NodeList nl = null;
      
      String attrVersion = null;
      Element findVersionElement = null;
      
      if(update.getDocumentElement().hasChildNodes()) {
          nl = update.getDocumentElement().getChildNodes();
          for(int k = 0;k < nl.getLength() && findVersionElement == null;k++) {
              if(Node.ELEMENT_NODE == nl.item(k).getNodeType())
                  findVersionElement = (Element) nl.item(k);
          }
          //attrVersion = RegistryServerHelper.getRegistryVersionFromNode(update.getDocumentElement().getFirstChild());
      }else {
          findVersionElement = (Element)update.getDocumentElement();
      }
      attrVersion = RegistryServerHelper.getRegistryVersionFromNode(findVersionElement);
      
      log.info("Registry Version being updated = " + attrVersion);      
      String vrNS = "http://www.ivoa.net/xml/VOResource/v" + attrVersion;
      log.info("Registry namespace being updated = " + vrNS);
      String versionNumber = attrVersion.replace('.','_');
      String collectionName = "astrogridv" + versionNumber;
      log.info("Collection Name = " + collectionName);
      //String rootNodeString = RegistryServerHelper.getRootNodeLocalName(versionNumber);
      hasStyleSheet = conf.getBoolean("org.astrogrid.registry.updatestylesheet." + versionNumber,false);
      if(hasStyleSheet) {
         //System.out.println("lets call transform update");
         log.info("performing transformation before analysis of update for versionNumber = " + versionNumber);
         xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),versionNumber,false);
      } else {
         xsDoc = update;
      }

      nl = xsDoc.getElementsByTagNameNS("*","Resource");
      if(nl.getLength() == 0) {
          throw new AxisFault("No Resources Found to be updated");
      }
      
      boolean validateXML = conf.getBoolean("registry.validate.onupdates",false);
      //log.info("Validate xml2 = " + validateXML);
      if(validateXML) {
          try {
              
              String rootElement = update.getDocumentElement().getFirstChild().getLocalName();
              if(rootElement == null) {
                  rootElement = update.getDocumentElement().getFirstChild().getNodeName();
              }
              log.info("try Validating for version = " + versionNumber +
                       " with rootElement = " + rootElement);
              RegistryValidator.isValid(xsDoc,rootElement);
          }catch(AssertionFailedError afe) {
              afe.printStackTrace();
              log.error("Error invalid document = " + afe.getMessage());
              if(conf.getBoolean("registry.quiton.invalid",false)) {
                  throw new AxisFault("Invalid update, document not valid " + afe.getMessage());
              }//if              
          }//catch
      }//if
      
      log.info("Number of Resources to be updated = " + nl.getLength());
      
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
      
      //log.info("here is the nl length = " + bhyxdtgdxdsnl.getLength());
      
      final int resourceNum = nl.getLength();
      //go through the various resource entries.
      for(int i = 0;i < resourceNum;i++) {
         long beginQ = System.currentTimeMillis();
         //Looks sort of odd always getting item at number 0
         //this is because we append it to another element later
         //hence the nodelist is like a queue and no longer
         //has the current resource and moves everything up the queue
         //get the iden and resource key elements.
         ident = RegistryServerHelper.getAuthorityID((Element)nl.item(0));
         log.info("here is the ident/authid = " + ident);         
         resKey = RegistryServerHelper.getResourceKey((Element)nl.item(0));
         log.info("here is the reskey = " + resKey);
         //set the currentResource element.
         Element currentResource = (Element)nl.item(0);
         //Sometimes there are other elements defined above the
         //Resource element so be sure this Resource element has a
         //defined vr namespace if it does not have one already.
         
         Node parentNode = currentResource.getParentNode();
         if(parentNode != null && Node.ELEMENT_NODE == parentNode.getNodeType()) {
             NamedNodeMap attrNNM = parentNode.getAttributes();
             //currentResource.setPrefix("vr");
             for(int k= 0;k < attrNNM.getLength();k++) {
                 Node attrNode = attrNNM.item(k);
                 if(!currentResource.hasAttribute(attrNode.getNodeName()) && 
                    !attrNode.getLocalName().equals(currentResource.getPrefix())) {
                     //System.out.println("2ADDING THIS ATTRIBUTE: the localname = " + attrNode.getLocalName() + " node name = " + attrNode.getNodeName() + " node value = " + attrNode.getNodeValue() + " namespace uri = " + attrNode.getNamespaceURI());
                     if(attrNode.getNodeName().indexOf("xmlns") != -1 && 
                        attrNode.getNodeName().indexOf("xsi") == -1) {
                         //System.out.println("adding xmlns");
                         currentResource.setAttribute(attrNode.getNodeName(),
                                                      attrNode.getNodeValue());
                     }//else
                 }//if
             }//for
         }//if
         currentResource.setAttribute("updated",updateDateString);
         

         //set a temporary identifier.
         tempIdent = ident;
         if(resKey != null && resKey.trim().length() > 0) tempIdent += "/" + resKey;
      
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
                
//               NamedNodeMap nnm = currentResource.getAttributes();
//               for(int j = 0;j < nnm.getLength();j++) {
//                  Node attrNode = nnm.item(j);
                  Node typeAttribute = currentResource.getAttributes().getNamedItem("xsi:type");
                  String nodeVal = null;
                  if(typeAttribute != null) {
                      nodeVal = typeAttribute.getNodeValue();
                  }
                  log.info(
                  "Checking xsi:type for a Registry or Authority: = " 
                  + nodeVal);
                  //check if it is a registry type.
                  if(nodeVal != null && nodeVal.indexOf("Registry") != -1) {
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
                        log.info("authority id equaled to ident");
                        ((HashMap)manageAuths.get(versionNumber)).put(ident,null);
                        for(int k = 0;k < manageList.getLength();k++) {
                            manageNodeVal = manageList.item(k).getFirstChild().getNodeValue();
                            log.info("try adding new manage node for this registry = " + manageNodeVal);
                            if(manageNodeVal != null && manageNodeVal.trim().length() > 0) {
                                ((HashMap)manageAuths.get(versionNumber)).put(manageNodeVal,null);
                            }//if
                        }//for
                     }else {
                        ((HashMap)otherAuths.get(versionNumber)).put(ident,null);
                        for(int k = 0;k < manageList.getLength();k++) {
                            manageNodeVal = manageList.item(k).getFirstChild().getNodeValue();
                            if(manageNodeVal != null && manageNodeVal.trim().length() > 0) {
                                log.info("try adding a managed authority for another registry = " + manageNodeVal);
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
                     if((otherAuths.get(versionNumber) != null &&
                        !((HashMap)otherAuths.get(versionNumber)).containsKey((String)ident))) {
                        log.info(
                        "This is an AuthorityType and not managed by other authorities");
                        addManageError = false;
                        // Grab our current Registry resource we need to add
                        // a new managed authority tag.
                        RegistryQueryService rs = new RegistryQueryService();
                        Document loadedRegistry = rs.loadMainRegistry(versionNumber);
                        log.info("THE LOADED REGISTRY before AUTHORITY = " + DomHelper.DocumentToString(loadedRegistry));
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
                           log.info("adding ident for managed authority = " + ident);
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
                           ident = RegistryServerHelper.getAuthorityID(loadedRegistry.
                                                  getDocumentElement());
                           log.info("the ident form loaded registry right before update = " + ident);
                           resKey = RegistryServerHelper.getResourceKey(loadedRegistry.
                                                   getDocumentElement());
                           log.info("the resKey form loaded registry right before update = " + resKey);
                           tempIdent = ident;
                           if(resKey != null) tempIdent += "/" + resKey;
                           //TODO: again this next line should not be needed.
                           //df = loadedRegistry.createDocumentFragment();
                           resListForAuth = loadedRegistry.getElementsByTagNameNS("*","Resource");
                           Element elem = loadedRegistry.
                                          createElement("AstrogridResource");
                           elem.appendChild(resListForAuth.item(0));
                           log.info("THE LOADED REGISTRY after AUTHORITY = " + DomHelper.ElementToString(elem));                           
                           try {
                               log.info("updating the new registy");
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
                           //xsDoc.getDocumentElement().
                           //      removeChild(currentResource);
                        }                           
                     }//if      
                  }//elseif   
//               }//for
            }//if
            
            if(addManageError) {
               al.add("This AuthorityID is not managed by the Registry: " + 
                      ident);
               log.info("Found entry not managed by this Registry = " + ident);
               //xsDoc.getDocumentElement().removeChild(currentResource);
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
   
   public void clearManagedCache(String versionNumber) {
       versionNumber = versionNumber.replace('.','_');
       otherAuths.put(versionNumber,null);
       manageAuths.put(versionNumber,null);
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
    * Also an update method that updates into this Registry's db. But it does no
    * special checking.  This is used internally by harvesting other registries to
    * go ahead and place the Resources into our db.
    * 
    * @param update A DOM of XML of  one or more Resources.
    */
   public void updateNoCheck(Document update,String attrVersion) throws MalformedURLException, IOException {
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
      
      if(update == null) {
          throw new IOException("Error nothing to update 'null sent'");
      }
      
      
      //String attrVersion = null;
      //the vr attribute can live at either or both of those elments and we just need to get the first one.
      //It is possible the <update> element will not be there hence we need to look at the root element
      //NodeList nl = DomHelper.getNodeListTags(update,"Resource","vr");
      NodeList nl = update.getElementsByTagNameNS("*","Resource");
      if(nl.getLength() == 0) {

          nl = update.getElementsByTagNameNS("*","resource");
          System.out.println("the resource nl.getLength= " + nl.getLength());
      }
      
      if(attrVersion == null) {
          attrVersion = RegistryServerHelper.getRegistryVersionFromNode(nl.item(0));
      }
          
      log.info("the nl length of resoruces = " + nl.getLength());      
      /*
      if(nl.getLength() > 0) {
          log.info("NODE TYPE = " + nl.item(0).getNodeType() + " NODE NAME = " + nl.item(0).getNodeName() + " Local Name = " + nl.item(0).getLocalName());
          attrVersion = RegistryServerHelper.getRegistryVersionFromNode((Element)nl.item(0)); 
      }
      */
      String vrNS = "http://www.ivoa.net/xml/VOResource/v" + attrVersion;
      String versionNumber = attrVersion.replace('.','_');      
      String collectionName = "astrogridv" + versionNumber;
      String defaultNS = null;
      log.info("Collection Name = " + collectionName);
      
      boolean hasStyleSheet = conf.getBoolean("org.astrogrid.registry.updatestylesheet.onHarvest." + versionNumber,false);
      Document xsDoc = null;
      System.out.println("has stylesheet for " + "org.astrogrid.registry.updatestylesheet.onHarvest." + versionNumber);
      log.info("Before the transform:::");
      log.info(DomHelper.DocumentToString(update));
      if(hasStyleSheet) {
         //System.out.println("lets call transform update");
         log.info("performing transformation before analysis of update for versionNumber = " + versionNumber);
         xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),versionNumber,true);
      } else {
         xsDoc = update;
      }
      log.info("the xsdoc = " + DomHelper.DocumentToString(xsDoc));
      nl = xsDoc.getElementsByTagNameNS("*","Resource");            
      
      //System.out.println("the xsdoc in updateNocheck = " + DomHelper.DocumentToString(xsDoc));
      log.info("Number of Resources = " + nl.getLength());
      UpdateDBService udbService = new UpdateDBService();
      
      if(manageAuths.get(versionNumber) == null)
          populateManagedMaps(collectionName, versionNumber);
      
      if(otherAuths.get(versionNumber) == null)
          populateOtherManagedMaps(collectionName, versionNumber);
      
      //hmmm he is doing harvesting before he setup the registry, okay let it go.
      if(manageAuths.get(versionNumber) == null) {
          //okay this must be the very first time into the registry where
          //registry is empty. So put a an empty entry for this version.
          manageAuths.put(versionNumber,new HashMap());
      }
      //hmmm he is doing harvesting before he setup the registry, okay let it go.
      if(otherAuths.get(versionNumber) == null) {
          //okay this must be the very first time into the registry where
          //registry is empty. So put a an empty entry for this version.
          otherAuths.put(versionNumber,new HashMap());
      }
      
      // This does seem a little strange as if an infinte loop,
      // but later on an appendChild is performed which
      // automatically reduced the length by one.
      final int resourceNum = nl.getLength();
      for(int i = 0;i < resourceNum;i++) {
         ident = RegistryServerHelper.getAuthorityID( (Element)nl.item(0));
         resKey = RegistryServerHelper.getResourceKey( (Element)nl.item(0));
         Element currentResource = (Element)nl.item(0);

         tempIdent = ident;
         if(resKey != null) tempIdent += "/" + resKey;
         log.info("the ident in updateNoCheck = " + tempIdent);
         
         /*
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
         */            
         //root = update.createElement("AstrogridResource");
         root = xsDoc.createElement("AstrogridResource");
         root.appendChild(currentResource);
         RegistryServerHelper.
             addStatusMessage("Entering new entry: " + tempIdent);
         udbService.updateQuery(tempIdent,"xml",collectionName,root);
                          
         if(currentResource.hasAttributes()) {
             
             Node typeAttribute = currentResource.getAttributes().getNamedItem("xsi:type");
             String nodeVal = null;
             if(typeAttribute != null) {
                 nodeVal = typeAttribute.getNodeValue();
             }
             log.info(
             "Checking xsi:type for a Registry: = " 
             + nodeVal);
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
          
          if(nl.getLength() == 0) {
              nl = doc.getElementsByTagNameNS("*","managedAuthority");
          }          
          
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
       if(nl.getLength() == 0) {
           nl = regNode.getElementsByTagNameNS("*","managedAuthority");
       }
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
