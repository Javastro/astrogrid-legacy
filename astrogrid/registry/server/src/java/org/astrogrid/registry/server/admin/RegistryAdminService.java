package org.astrogrid.registry.server.admin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;

import javax.security.auth.Subject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.astrogrid.registry.server.RegistryServerHelper;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Date;
import java.text.DateFormat;
import java.util.Iterator;
import org.astrogrid.config.Config;
import org.astrogrid.registry.server.XSLHelper;
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
import junit.framework.AssertionFailedError;
import java.text.SimpleDateFormat;
import javax.security.auth.Subject;
import org.astrogrid.security.ServiceSecurityGuard;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.astrogrid.xmldb.client.XMLDBFactory;



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
   
   private XMLDBFactory xdb = new XMLDBFactory();

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
   private static HashMap manageAuths; //, otherAuths;
   

   /**
    * Static to be used on the initiatian of this class for the config
    */
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         manageAuths = new HashMap();
         //otherAuths = new HashMap();         
      }      
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
      /*
      ServiceSecurityGuard sg = ServiceSecurityGuard.getInstanceFromContext();
      Subject s = sg.getGridSubject();
      log.info(s.getPrincipals().size() + " Principals in gridSubject");
      log.info(s.getPrivateCredentials().size() + " private credentials in gridSubject");
      if (sg.isAnonymous()) {
         log.info("it is anonymous");
        }
        else {
          log.info("found username = " + sg.getUsername());
        }
        */
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

      String authorityID = conf.getString("reg.amend.authorityid");
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
      String versionNumber = RegistryServerHelper.getRegistryVersionFromNode(findVersionElement);
      
      log.info("Registry Version being updated = " + versionNumber);      
      String vrNS = "http://www.ivoa.net/xml/VOResource/v" + versionNumber;
      log.info("Registry namespace being updated = " + vrNS);
      String collectionName = "astrogridv" + versionNumber.replace('.','_');
      log.info("Collection Name = " + collectionName);
      //String rootNodeString = RegistryServerHelper.getRootNodeLocalName(versionNumber);
      hasStyleSheet = conf.getBoolean("reg.custom.updatestylesheet." + versionNumber,false);
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
      
      boolean validateXML = conf.getBoolean("reg.amend.validate",false);
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
              if(conf.getBoolean("reg.amend.quiton.invalid",false)) {
                  throw new AxisFault("Invalid update, document not valid " + afe.getMessage());
              }//if              
          }//catch
      }//if
      
      log.info("Number of Resources to be updated = " + nl.getLength());
      AuthorityList someTestAuth = new AuthorityList(authorityID,versionNumber);      
      if(manageAuths.isEmpty()) {
          try {
              populateManagedMaps(collectionName, versionNumber);
          }catch(XMLDBException xmldbe) {
              xmldbe.printStackTrace();
              throw AxisFault.makeFault(xmldbe);              
          }
      }else if(!manageAuths.isEmpty() && !manageAuths.containsKey(someTestAuth)) {
          try {
              populateManagedMaps(collectionName, versionNumber);
          }catch(XMLDBException xmldbe) {
              xmldbe.printStackTrace();
              throw AxisFault.makeFault(xmldbe);              
          }
      }
      //printManagedAuthorities();
            
      if(manageAuths.isEmpty()) {
          //okay this must be the very first time into the registry where
          //registry is empty. So put a an empty entry for this version.
          log.info("Empty Registry; no registry types found");
      }
      /*
      if(otherAuths.get(versionNumber) == null) {
          //okay this must be the very first time into the registry where
          //registry is empty. So put a an empty entry for this version.
          otherAuths.put(versionNumber,new HashMap());
      }
      */
      
      
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
      Collection coll = null;
      Collection collStat = null;
      AuthorityList tempAuthorityListKey = null;
      AuthorityList tempAuthorityListVal = null;
      
      //log.info("here is the nl length = " + bhyxdtgdxdsnl.getLength());
      
      final int resourceNum = nl.getLength();
      //go through the various resource entries.
      for(int i = 0;i < resourceNum;i++) {
         long beginQ = System.currentTimeMillis();
         //Looks sort of odd always getting item at number 0
         //this is because we append it to another element later
         //hence the nodelist is like a queue and no longer
         //has the current resource and moves everything up the queue
         //get the ident/authorityid and resource key elements.
         ident = RegistryServerHelper.getAuthorityID((Element)nl.item(0));
         if(ident == null || ident.trim().length() <= 0) {
             throw AxisFault.makeFault(new RegistryException("Could not find the AuthorityID from the Identifier"));
         }//if
         log.info("here is the ident/authid = " + ident);         
         resKey = RegistryServerHelper.getResourceKey((Element)nl.item(0));
         log.info("here is the reskey = " + resKey);
         //set the currentResource element.
         Element currentResource = (Element)nl.item(0);
         //Sometimes there are other elements defined above the
         //Resource element so be sure this Resource element has a
         //those defined namespaces.
         
         Node parentNode = currentResource.getParentNode();
         if(parentNode != null && Node.ELEMENT_NODE == parentNode.getNodeType()) {
             NamedNodeMap attrNNM = parentNode.getAttributes();
             //currentResource.setPrefix("vr");
             for(int k= 0;k < attrNNM.getLength();k++) {
                 Node attrNode = attrNNM.item(k);
                 //log.info("attrNode getNodeName = " + attrNode.getNodeName() + " local name = " + attrNode.getLocalName());
                 if(!currentResource.hasAttribute(attrNode.getNodeName()) && 
                    !attrNode.getLocalName().equals(currentResource.getPrefix())) {
                     //log.info("inside");
                     if(attrNode.getNodeName().indexOf("xmlns") != -1 ) {
                         currentResource.setAttribute(attrNode.getNodeName(),
                                                      attrNode.getNodeValue());
                     }//else
                 }//if
             }//for
         }//if
         currentResource.setAttribute("updated",updateDateString);
         //log.info("element to string = " + DomHelper.ElementToString(currentResource));
         

         //set a temporary identifier.
         tempIdent = "ivo://" + ident;
         if(resKey != null && resKey.trim().length() > 0) tempIdent += "/" + resKey;
      
         log.info("serverside update ident = " + ident + " reskey = " + 
                  resKey + " the nl getlenth here = " + nl.getLength());
                  //see if we manage this authority id
                  //if we do then update it in the db.
         //do we manage this authority id, if so then add the resource.
         if(manageAuths.containsValue(new AuthorityList(ident,versionNumber, authorityID))) {
            //Essentially chop off other elemens wrapping the Resource element, and put
            //our own element. Would have been nice just to store the Resource element
            //at the root level, but it seems the XQueries on the database have problems
            //with that.
            root = xsDoc.createElement("AstrogridResource");
            root.appendChild(currentResource);

            //RegistryServerHelper.addStatusMessage("Entering new entry: " +  tempIdent);
            try {
               coll = xdb.openAdminCollection(collectionName);
               xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_"),root);               
               collStat = xdb.openAdminCollection("statv" + versionNumber.replace('.','_'));
               xdb.storeXMLResource(collStat,tempIdent.replaceAll("[^\\w*]","_"),createStats(tempIdent));
            } catch(XMLDBException xdbe) {
               log.error(xdbe);
               throw AxisFault.makeFault(xdbe);
            }finally {
                try {
                    xdb.closeCollection(coll);
                    xdb.closeCollection(collStat);
                }catch(XMLDBException xmldb) {
                    log.error(xmldb);
                }                
            }
         }else {
            // It is not one this registry manages, so check it's attributes
            // and if it is a Registry type then go ahead and update it
            // if it is an authoritytype
            addManageError = true;
            if(currentResource.hasAttributes()) {
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
                     //RegistryServerHelper.addStatusMessage("Entering new entry: " + tempIdent);
                     NodeList manageList = getManagedAuthorities(currentResource);
                     boolean managedAuthorityFound = false;
                     if(manageList.getLength() > 0)
                         clearManagedAuthoritiesForOwner(ident,versionNumber);
                     //if(authorityID.equals(ident)) {                        
                        //log.info("authority id equaled to ident");
                        for(int k = 0;k < manageList.getLength();k++) {
                            manageNodeVal = manageList.item(k).getFirstChild().getNodeValue();
                            log.info("try adding new manage node for this registry = " + manageNodeVal);
                            if(manageAuths.containsKey((tempAuthorityListKey = new AuthorityList(manageNodeVal,versionNumber)))) {
                                tempAuthorityListVal = (AuthorityList)manageAuths.get(tempAuthorityListKey);
                                log.error("Error - mismatch: Tried to update a Registry Type that has this managed Authority: " + manageNodeVal +
                                    " with this main Identifiers Authority ID " + ident + " This mismatches with another Registry Type that ownes/manages " + 
                                    " this same authority id, other registry type authority id: " + tempAuthorityListVal.getOwner());
                                throw AxisFault.makeFault(new RegistryException("Error - mismatch: Tried to update a Registry Type that has this managed Authority: " + manageNodeVal +
                                        " with this main Identifiers Authority ID " + ident + " This mismatches with another Registry Type that ownes/manages " + 
                                        " this same authority id, other registry type authority id: " + tempAuthorityListVal.getOwner()));
                            }//if
                            manageAuths.put(new AuthorityList(manageNodeVal, versionNumber),
                                            new AuthorityList(manageNodeVal, versionNumber, ident));
                                if(manageNodeVal.trim().equals(ident.trim()))
                                    managedAuthorityFound = true;
                        }//for
                        //printManagedAuthorities();
                        if(!managedAuthorityFound) {
                            log.error("Trying to update the main Registry Type for this Registry with no matching Managed AuthorityID with the Identifiers AuthorityID; The AuthorityID = " + ident);
                            throw AxisFault.makeFault(new RegistryException("Trying to update the main Registry Type for this Registry with no matching Managed AuthorityID with the Identifiers AuthorityID; The AuthorityID = " + ident));
                        }
                     //update this registry resource into our registry.
                     try {
                        coll = xdb.openAdminCollection(collectionName);
                        xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_"),root);
                        
                        collStat = xdb.openAdminCollection("statv" + versionNumber.replace('.','_'));
                        if(xdb.getResource(coll,tempIdent.replaceAll("[^\\w*]","_")) == null) {
                            xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_"),createStats(tempIdent,false));
                        }
                                                
                     } catch(XMLDBException xdbe) {
                        log.error(xdbe);
                        throw AxisFault.makeFault(xdbe);
                     } finally {
                         try {
                             xdb.closeCollection(coll);
                             xdb.closeCollection(collStat);
                         }catch(XMLDBException xmldb) {
                             log.error(xmldb);
                         }                
                     }                     
                  }else if(nodeVal != null && 
                           nodeVal.indexOf("Authority") != -1)
                  {
                     // Okay it is an AuthorityType and if no other registries 
                     // manage this authority then we can place it in this 
                     // registry as a new managed authority.
                     //printManagedAuthorities();
                     if(!manageAuths.containsKey(new AuthorityList(ident,versionNumber))) {
                        log.info(
                        "This is an AuthorityType and not managed by other authorities");
                        addManageError = false;
                        // Grab our current Registry resource we need to add
                        // a new managed authority tag.
                        RegistryQueryService rs = new RegistryQueryService();
                        Document loadedRegistry = rs.loadMainRegistry(versionNumber);
                        //log.info("THE LOADED REGISTRY before AUTHORITY = " + DomHelper.DocumentToString(loadedRegistry));
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
                           manageAuths.put(new AuthorityList(ident,versionNumber),new AuthorityList(ident,versionNumber,authorityID));

                           // Update our currentResource into the database
                           root = xsDoc.createElement("AstrogridResource");
                           root.appendChild(currentResource);
                           try {
                               collStat = xdb.openAdminCollection("statv" + versionNumber.replace('.','_'));
                               xdb.storeXMLResource(collStat,tempIdent.replaceAll("[^\\w*]","_"),createStats(tempIdent));                        
                               
                               coll = xdb.openAdminCollection(collectionName);
                               xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_"),root);                               
                            } catch(XMLDBException xdbe) {
                               log.error(xdbe);
                               throw AxisFault.makeFault(xdbe);
                            }finally {
                                try {
                                    xdb.closeCollection(collStat);
                                }catch(XMLDBException xmldb) {
                                    log.error(xmldb);
                                }                
                            }
                           
                           // Now get the information to re-update the
                           // Registry Resource which is for this registry.
                           ident = RegistryServerHelper.getAuthorityID(loadedRegistry.
                                                  getDocumentElement());
                           log.info("the ident form loaded registry right before update = " + ident);
                           resKey = RegistryServerHelper.getResourceKey(loadedRegistry.
                                                   getDocumentElement());
                           log.info("the resKey form loaded registry right before update = " + resKey);
                           tempIdent = "ivo://" + ident;
                           if(resKey != null) tempIdent += "/" + resKey;
                           //TODO: again this next line should not be needed.
                           //df = loadedRegistry.createDocumentFragment();
                           resListForAuth = loadedRegistry.getElementsByTagNameNS("*","Resource");
                           Element elem = loadedRegistry.
                                          createElement("AstrogridResource");
                           elem.appendChild(resListForAuth.item(0));                           
                           try {
                               log.info("updating the new registy");
                               xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_"),elem);                                            
                           } catch(XMLDBException xdbe) {
                               log.error(xdbe);
                               throw AxisFault.makeFault(xdbe);
                           } finally {
                               try {
                                   xdb.closeCollection(coll);
                               }catch(XMLDBException xmldb) {
                                   log.error(xmldb);
                               }                
                           }                                                     
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
               log.info("Error authority id not managed by this registry throwing AxisFault exception; the authority id = " + ident);
               throw AxisFault.makeFault(new RegistryException("Trying to update an entry that is not managed by this Registry authority id = " + ident));               
            }//if
         }//else
         log.info("Time taken to update an entry = " + 
                  (System.currentTimeMillis() - beginQ) +
                  " for ident  = " + tempIdent);
      }//for
      
      //printManagedAuthorities();

      Document returnDoc = null;      
      // Constructgs a small RegistryError element with all the
      // errored Resource that was not able to be updated in the db.
      try {      
          returnDoc = DomHelper.newDocument();
          returnDoc.appendChild(returnDoc.createElement("UpdateResponse"));          
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
       //versionNumber = versionNumber.replace('.','_');
//       otherAuths.put(versionNumber,null);
//       manageAuths.put(versionNumber,null);
        manageAuths.clear();
   }
   
   private void populateManagedMaps(String collectionName, String versionNumber) throws XMLDBException {
       log.debug("start populateManagedMaps");       
       //get All the Managed Authorities, the getManagedAutories() does not
       //perform a query every time only once.
       HashMap versionManaged = null;
       versionManaged = RegistryServerHelper.getManagedAuthorities(collectionName, versionNumber);
       
       manageAuths.putAll(versionManaged);
       log.info("After loading Managed Authorities from Query = " + manageAuths.size() + " for registry version = " + versionNumber);
       log.debug("end populateManagedMaps");
   }
 
  
   /**
    * Also an update method that updates into this Registry's db. But it does no
    * special checking.  This is used internally by harvesting other registries to
    * go ahead and place the Resources into our db.
    * 
    * @param update A DOM of XML of  one or more Resources.
    */
   public void updateNoCheck(Document update,String versionNumber) throws XMLDBException, IOException {
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
      Collection coll = null;
      Collection collStat = null;
      AuthorityList tempAuthorityListKey = null;
      AuthorityList tempAuthorityListVal = null;
      

      XSLHelper xs = new XSLHelper();
      
      if(update == null) {
          throw new IOException("Error nothing to update 'null sent'");
      }
      
      String authorityID = conf.getString("reg.amend.authorityid");      
      
      //String attrVersion = null;
      //the vr attribute can live at either or both of those elments and we just need to get the first one.
      //It is possible the <update> element will not be there hence we need to look at the root element
      //NodeList nl = DomHelper.getNodeListTags(update,"Resource","vr");
      NodeList nl = update.getElementsByTagNameNS("*","Resource");
      if(nl.getLength() == 0) {
          nl = update.getElementsByTagNameNS("*","resource");
          System.out.println("the resource nl.getLength= " + nl.getLength());
      }
      
      if(versionNumber == null) {
          versionNumber = RegistryServerHelper.getRegistryVersionFromNode(nl.item(0));
      }
          
      log.info("the nl length of resoruces = " + nl.getLength());      

      String vrNS = "http://www.ivoa.net/xml/VOResource/v" + versionNumber;
      //String versionNumber = attrVersion.replace('.','_');      
      String collectionName = "astrogridv" + versionNumber.replace('.','_');
      String defaultNS = null;
      log.info("Collection Name = " + collectionName);
      
      boolean hasStyleSheet = conf.getBoolean("reg.custom.harveststylesheet." + versionNumber,false);
      Document xsDoc = null;
      //System.out.println("has stylesheet for " + "org.astrogrid.registry.updatestylesheet.onHarvest." + versionNumber);
      log.info("Before the transform:::");
      log.info(DomHelper.DocumentToString(update));
      if(hasStyleSheet) {
         //System.out.println("lets call transform update");
         log.info("performing transformation before analysis of update for versionNumber = " + versionNumber);
         xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),versionNumber,true);
      } else {
         xsDoc = update;
      }
      //log.info("the xsdoc = " + DomHelper.DocumentToString(xsDoc));
      nl = xsDoc.getElementsByTagNameNS("*","Resource");            
      
      //System.out.println("the xsdoc in updateNocheck = " + DomHelper.DocumentToString(xsDoc));
      log.info("Number of Resources = " + nl.getLength());
      AuthorityList someTestAuth = new AuthorityList(authorityID,versionNumber);      
      if(manageAuths.isEmpty()) {
          try {
              populateManagedMaps(collectionName, versionNumber);
          }catch(XMLDBException xmldbe) {
              xmldbe.printStackTrace();
              throw AxisFault.makeFault(xmldbe);              
          }
      }else if(!manageAuths.isEmpty() && !manageAuths.containsKey(someTestAuth)) {
          try {
              populateManagedMaps(collectionName, versionNumber);
          }catch(XMLDBException xmldbe) {
              xmldbe.printStackTrace();
              throw AxisFault.makeFault(xmldbe);              
          }
      }
      
      //hmmm user is doing harvesting before he setup the registry, okay let it go.
      if(manageAuths.isEmpty()) {
          log.info("seems like user is doing harvesting first for versionNumber=" + versionNumber);
          //okay this must be the very first time into the registry where
          //registry is empty. So put a an empty entry for this version.          
      }
      
      // This does seem a little strange as if an infinte loop,
      // but later on an appendChild is performed which
      // automatically reduced the length by one.
      final int resourceNum = nl.getLength();
      for(int i = 0;i < resourceNum;i++) {
         ident = RegistryServerHelper.getAuthorityID( (Element)nl.item(0));
         resKey = RegistryServerHelper.getResourceKey( (Element)nl.item(0));
         Element currentResource = (Element)nl.item(0);
         if(manageAuths.containsValue(new AuthorityList(ident,versionNumber,authorityID))) {
             log.error("Either your harvesting your own Registry or another Registry is submitting authority id's owned by this registry. Ident = " + ident);
             xsDoc.getDocumentElement().removeChild(currentResource);
         } else {
             tempIdent = "ivo://" + ident;
             if(resKey != null) tempIdent += "/" + resKey;
             log.info("the ident in updateNoCheck = " + tempIdent);
             
             //root = update.createElement("AstrogridResource");
             root = xsDoc.createElement("AstrogridResource");
             root.appendChild(currentResource);
             //RegistryServerHelper.addStatusMessage("Entering new entry: " + tempIdent);
             try {
                 coll = xdb.openAdminCollection(collectionName);
                 xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_"),root);
              } finally {
                  try {
                      xdb.closeCollection(coll);
                  }catch(XMLDBException xmldb) {
                      log.error(xmldb);
                  }                
              }
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
                            collStat = xdb.openAdminCollection("statv" + versionNumber.replace('.','_'));
                            if(xdb.getResource(coll,tempIdent.replaceAll("[^\\w*]","_")) == null) {
                                xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_"),createStats(tempIdent,false));
                            }else {
                                xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_"),createStats(tempIdent));
                            }                                                
                         } finally {
                             try {
                                 xdb.closeCollection(collStat);
                             }catch(XMLDBException xmldb) {
                                 log.error(xmldb);
                             }                
                         }                       
                           NodeList manageList = getManagedAuthorities(currentResource);
                           if(manageList.getLength() > 0)
                               clearManagedAuthoritiesForOwner(ident, versionNumber);
                           else 
                               log.error("Registry type from a Harvest has no ManagedAuthorities; AuthorityID = " + ident + " versionNumber = " + versionNumber);
                           
                           for(int k = 0;k < manageList.getLength();k++) {
                               String manageNodeVal = manageList.item(k).getFirstChild().getNodeValue();
                               if(manageAuths.containsKey((tempAuthorityListKey = new AuthorityList(manageNodeVal,versionNumber)))) {
                                   tempAuthorityListVal = (AuthorityList)manageAuths.get(tempAuthorityListKey);
                                    log.error("Error - mismatch: Tried to update a Registry Type that has this managed Authority: " + manageNodeVal +
                                        " with this main Identifiers Authority ID " + ident + " This mismatches with another Registry Type that ownes/manages " + 
                                        " this same authority id, other registry type authority id: " + tempAuthorityListVal.getOwner());
                                    throw AxisFault.makeFault(new RegistryException("Error - mismatch: Tried to update a Registry Type that has this managed Authority: " + manageNodeVal +
                                            " with this main Identifiers Authority ID " + ident + " This mismatches with another Registry Type that ownes/manages " + 
                                            " this same authority id, other registry type authority id: " + tempAuthorityListVal.getOwner()));
                               }//if                           
                               if(manageNodeVal != null && manageNodeVal.trim().length() > 0) {
                                   manageAuths.put(tempAuthorityListKey, new AuthorityList(manageNodeVal,versionNumber,ident));
                               }//if
                           }//for
                           
                     }//if
                 }//if
         }//else
      }//for
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
   
   private void clearManagedAuthoritiesForOwner(String owner, String versionNumber) {
       java.util.Collection values = manageAuths.values();
       AuthorityList al = null;
       Iterator iter = values.iterator();
       while(iter.hasNext()) {
           al = (AuthorityList)iter.next();
           if(owner.equals(al.getOwner()) && versionNumber.equals(al.getVersionNumber())) {
               //javadocs says this will remove it from the manageAuths hashmap.
               //see HashMap.values()
               iter.remove();
           }//if
       }//while
   }
   
   private void printManagedAuthorities() {
       System.out.println("in printManaged the values size = " + manageAuths.values().size());
       java.util.Set keys = manageAuths.keySet();
       AuthorityList al = null;
       Iterator iter = keys.iterator();
       while(iter.hasNext()) {
           al = (AuthorityList)iter.next();
           System.out.println("The Key AuthorityList = " + al.toString() + " ident length = " + al.getAuthorityID().length());
           System.out.println("The Value AuthorityList = " + ((AuthorityList)manageAuths.get(al)).toString());
       }//while
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
          NodeList nl = doc.getElementsByTagNameNS("*","ManagedAuthority");
          
          if(nl.getLength() == 0) {
              nl = doc.getElementsByTagNameNS("*","managedAuthority");
          }          
          
          //NodeList nl = DomHelper.getNodeListTags(doc,"ManagedAuthority","vg");
          if(nl.getLength() > 0)
             return nl.item(0);
          log.debug("end getManagedAuthorityID");
      return null;
   }
   
   private NodeList getManagedAuthorities(Element regNode) {
       log.debug("start getManagedAuthorityID");
       NodeList nl = regNode.getElementsByTagNameNS("*","ManagedAuthority");
       if(nl.getLength() == 0) {
           nl = regNode.getElementsByTagNameNS("*","managedAuthority");
       }
       return nl;
    }
    
}
