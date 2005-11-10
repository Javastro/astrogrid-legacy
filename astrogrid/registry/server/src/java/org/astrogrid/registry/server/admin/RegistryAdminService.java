package org.astrogrid.registry.server.admin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;

import org.astrogrid.store.Ivorn;
import javax.security.auth.Subject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.astrogrid.registry.server.RegistryServerHelper;
import org.astrogrid.registry.common.RegistryDOMHelper;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Date;
import java.text.DateFormat;
import java.util.Iterator;
import org.astrogrid.config.Config;
import org.astrogrid.registry.server.XSLHelper;
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
import java.io.StringReader;
import java.net.URL;
import java.net.HttpURLConnection;
import org.astrogrid.registry.server.SOAPFaultException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import java.net.MalformedURLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.axis.AxisFault;
import junit.framework.AssertionFailedError;
import java.text.SimpleDateFormat;
import javax.security.auth.Subject;
//commented out CLQ failed compile with security-gtr-1337, this import statement is redundent.
//import org.astrogrid.security.ServiceSecurityGuard;

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
 * in the registry including ones by the harvester.  It also makes sure Authority ids
 * do not conflict with one another.  A registry may manage 1 to many authority id's, but no 
 * other Regitry type can have those authority id's.  Read more on the manageAuths HashMap variable for more
 * information on this.
 * 
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
    * for updating to this registry and verify it is not owned by another registry. Uses the
    * AuthorityList object to keep track of Authority id's.  Both key and value are AuthorityList objects.
    * The HashMap is normally in the form of a key being a version number + main authority id and a value
    * being the form of a version number + authority id(being managed) + main authority (the owner). 
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
    * Method: Update
    * Description: Update Web Service method, performs an update to the registry. Actually calls updateResponse.  It can
    * handle many Resource elements if necessary to do multiple updates to the registry.  If a element
    * is not present in the database it automatically inserts.  Only Resource elements that are managed by
    * this registry are allowed, with the exception to Registry types(for discovering new registries)
    * and Authority types(for inserting an authority id to be managed by this registry).
    * 
    * @param update XML DOM containing Resource XML elements
    * @return Nothing is returned except an empty UpdateResponse element for conforming with SOAP standards
    * of a wrapped wsdl.
    */
   public Document Update(Document update) {
      log.debug("start update");      
      return updateResource(update);
   }
   
   /**
    * Method: updateResponse
    * Description: Called by the web service method and jsp's, performs an update to the registry. It can
    * handle many Resource elements if necessary to do multiple updates to the registry.  If a element
    * is not present in the database it automatically inserts.  Only Resource elements that are managed by
    * this registry are allowed, with the exception to Registry types(for discovering new registries)
    * and Authority types(for inserting an authority id to be managed by this registry).
    * 
    * @param update XML DOM containing Resource XML elements
    * @return Nothing is returned except an empty UpdateResponse element for conforming with SOAP standards
    * of a wrapped wsdl.
    */
   public Document updateResource(Document update) {
      log.debug("start updateResource");
      long beginUpdate = System.currentTimeMillis();

      Document testStoreDOM = null;
      String testStoreString = null;
      //get the main authority id for this registry.
      String authorityID = conf.getString("reg.amend.authorityid");
      
      log.debug("Default AuthorityID for this Registry = " + authorityID);
      
      // Transform the xml document into a consistent way.
      // xml can come in a few various forms.  This xsl will make it
      // consistent in the db and throughout this registry.
      XSLHelper xs = new XSLHelper();      
      Document xsDoc = null;
      
      String defaultNS = null;            
      boolean hasStyleSheet = false;            
      
      if(update == null) {
          return SOAPFaultException.createAdminSOAPFaultException("Nothing on request 'null sent'","Nothing on request 'null sent'");          
      }
      
      //Make a date. The registry automatically fills in the updated attribute for a Resource.
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      Date updateDate = new Date();
      String updateDateString = sdf.format(updateDate);
      
      //Get all the Resource elements
      NodeList nl = null;
      
      String attrVersion = null;
      Element findVersionElement = null;
      
      //todo: this is wrong correct this by gettingthe identifier element.
      if(update.getDocumentElement().hasChildNodes()) {
          nl = update.getDocumentElement().getChildNodes();
          for(int k = 0;k < nl.getLength() && findVersionElement == null;k++) {
              if(Node.ELEMENT_NODE == nl.item(k).getNodeType())
                  findVersionElement = (Element) nl.item(k);
          }
      }else {
          findVersionElement = (Element)update.getDocumentElement();
      }
      
      String versionNumber = RegistryDOMHelper.getRegistryVersionFromNode(findVersionElement);
      
      log.debug("Registry Version being updated = " + versionNumber);      
      String vrNS = "http://www.ivoa.net/xml/VOResource/v" + versionNumber;
      //log.info("Registry namespace being updated = " + vrNS);
      String collectionName = "astrogridv" + versionNumber.replace('.','_');
      log.debug("Collection Name = " + collectionName);
      //String rootNodeString = RegistryServerHelper.getRootNodeLocalName(versionNumber);
      //do we have a stylesheet to massage the data to make it consistent in the db.
      hasStyleSheet = conf.getBoolean("reg.custom.updatestylesheet." + versionNumber,false);
      //perform the transformation if necessary; either way set xsDoc to the
      //Document element to perform the updates through.
      if(hasStyleSheet) {
         //System.out.println("lets call transform update");
         log.debug("performing transformation before analysis of update for versionNumber = " + versionNumber);
         xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),versionNumber,false);
      } else {
         xsDoc = update;
      }

      //System.out.println("The xsDOC = " + DomHelper.DocumentToString(xsDoc));
      //Get all the Resource nodes.
      nl = xsDoc.getElementsByTagNameNS("*","Resource");
      if(nl.getLength() == 0) {
          return SOAPFaultException.createAdminSOAPFaultException("No Resources Found to be updated","No Resources Found to be updated");          
      }
      
      //is validation turned on.
      boolean validateXML = conf.getBoolean("reg.amend.validate",false);

      if(validateXML) {
          try {
              //validate the xml (start with the element below the <UPDATE> from the soap body)
              String rootElement = update.getDocumentElement().getFirstChild().getLocalName();
              if(rootElement == null) {
                  rootElement = update.getDocumentElement().getFirstChild().getNodeName();
              }
              log.debug("try Validating for version = " + versionNumber +
                       " with rootElement = " + rootElement);
              RegistryValidator.isValid(xsDoc,rootElement);
          }catch(AssertionFailedError afe) {
              afe.printStackTrace();
              log.error("Error invalid document = " + afe.getMessage());
              if(conf.getBoolean("reg.amend.quiton.invalid",false)) {
                  return SOAPFaultException.createAdminSOAPFaultException("Invalid update, document not valid ",afe.getMessage());
              }//if              
          }//catch
      }//if
      
      log.info("Number of Resources to be updated = " + nl.getLength());
      
      AuthorityList someTestAuth = new AuthorityList(authorityID,versionNumber);
      //our cache of managed authorityid's is empty (container must have started).
      //so go and see if we can fill it up based on Managed Authority elements from
      //Registry types.
      if(manageAuths.isEmpty()) {
          try {
              populateManagedMaps(collectionName, versionNumber);
          }catch(XMLDBException xmldbe) {
              xmldbe.printStackTrace();
              return SOAPFaultException.createAdminSOAPFaultException(xmldbe.getMessage(),xmldbe);
          }          
      }else if(!manageAuths.isEmpty() && !manageAuths.containsKey(someTestAuth)) {
          //todo: I do not believe this is needed
          //I guess sort of a safety check to make sure we have processed all our
          //authority id's into the hashmap.
          try {
              populateManagedMaps(collectionName, versionNumber);
          }catch(XMLDBException xmldbe) {
              xmldbe.printStackTrace();
              return SOAPFaultException.createAdminSOAPFaultException(xmldbe.getMessage(),xmldbe);              
          }
      }
      //printManagedAuthorities();
            
      if(manageAuths.isEmpty()) {
          //okay this must be the very first time into the registry where
          //registry is empty. So put a an empty entry for this version.
          log.info("Empty Registry; no registry types found");
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
         ident = RegistryDOMHelper.getAuthorityID((Element)nl.item(0));
         if(ident == null || ident.trim().length() <= 0) {
             return SOAPFaultException.createAdminSOAPFaultException("Could not find the AuthorityID from the Identifier","Could not find the AuthorityID from the Identifier");             
         }//if
         log.debug("here is the ident/authid = " + ident);         
         resKey = RegistryDOMHelper.getResourceKey((Element)nl.item(0));
         log.debug("here is the reskey = " + resKey);
         //set the currentResource element.
         Element currentResource = (Element)nl.item(0);

         
         //System.out.println("element to string24 = " + DomHelper.ElementToString(currentResource));
         NamedNodeMap attrNNMTest = currentResource.getAttributes();
         for(int tt= 0;tt < attrNNMTest.getLength();tt++) {
             Node attrNodeTest = attrNNMTest.item(tt);
             //System.out.println("attrNodeTest nsuri= " + attrNodeTest.getNamespaceURI() + " node name = " + attrNodeTest.getNodeName() + " local name = " + attrNodeTest.getLocalName());
         }
         
         //Sometimes there are other namespaces defined above the
         //Resource element so be sure this Resource element has a
         //those defined namespaces. Otherwise there could be a slight risk
         //of having invalid xml.
         Node parentNode = currentResource.getParentNode();

         if(parentNode != null && Node.ELEMENT_NODE == parentNode.getNodeType()) {
             //get the attributes.
             NamedNodeMap attrNNM = parentNode.getAttributes();
             
             for(int k= 0;k < attrNNM.getLength();k++) {
                 Node attrNode = attrNNM.item(k);
                 //log.info("attrNode getNodeName = " + attrNode.getNodeName() + " local name = " + attrNode.getLocalName());
                 //check if we don't have this attribute already.
                 //System.out.println("has asstribute for Node Name = " + attrNode.getNodeName() + " with ns = " + attrNode.getNamespaceURI() + " hasattr = " + currentResource.hasAttribute(attrNode.getNodeName()));
                 //System.out.println("localname = " + attrNode.getLocalName() + " and currentResource pref = " + currentResource.getPrefix() );

                 if(!currentResource.hasAttribute(attrNode.getNodeName()) &&
                    !currentResource.hasAttributeNS(attrNode.getNamespaceURI(),attrNode.getLocalName())) {                 
                     //okay were only after namespaces. if it is xmlns then set it on our resource.
                     if(attrNode.getNodeName().indexOf("xmlns") != -1 ) {
                         //currentResource.setAttribute(attrNode.getNodeName(),
                         //                             attrNode.getNodeValue());
                         currentResource.setAttributeNS(attrNode.getNamespaceURI(),attrNode.getNodeName(),
                                                        attrNode.getNodeValue());
                     }//else
                 }//if
             }//for
         }//if
         
         //set our new updated date attribute as well for the current date&time.
         currentResource.setAttribute("updated",updateDateString);
         //log.info("element to string = " + DomHelper.ElementToString(currentResource));
         
         //set a temporary identifier.
         tempIdent = ident;
         if(resKey != null && resKey.trim().length() > 0) tempIdent += "/" + resKey;
      
         log.debug("serverside update ident = " + ident + " reskey = " + 
                  resKey + " the nl getlenth here = " + nl.getLength());
         
         //Get the xsi:type.
         String nodeVal = "no type";         
         if(currentResource.hasAttributes()) {
             Node typeAttribute = currentResource.getAttributes().getNamedItem("xsi:type");
             if(typeAttribute != null) {
                 nodeVal = typeAttribute.getNodeValue();
             }//if
         }//if
         log.debug("The xsi:type for the Resource = " + nodeVal);         
                  //see if we manage this authority id
                  //if we do then update it in the db.
         //do we manage this authority id, if so then add the resource. Unless it is a Registry
         //type then we need to process its new managedAuthority list to make sure there is no 
         //conflict with authority id's.
         if(manageAuths.containsValue(new AuthorityList(ident,versionNumber, authorityID)) &&
            nodeVal.indexOf("Registry") == -1) {
            //Essentially chop off other elemens wrapping the Resource element, and put
            //our own element. Would have been nice just to store the Resource element
            //at the root level, but it seems the XQueries on the database have problems
            //with that.
            root = xsDoc.createElement("AstrogridResource");
            root.appendChild(currentResource);

            //RegistryServerHelper.addStatusMessage("Entering new entry: " +  tempIdent);
            try {
               coll = xdb.openAdminCollection(collectionName);
               //String storeTestString2 = DomHelper.ElementToString((Element)root);
               //System.out.println("THIS IS THE STORESTRINGNEW SAXWAY = " + storeTestString2);
               xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_") + ".xml",DomHelper.ElementToString((Element)root));
               //testStoreString = DomHelper.ElementToString((Element)root);
               //testStoreDOM = DomHelper.newDocument(testStoreString);
               //log.info("storing the new way ident = " + tempIdent);
               //System.out.println("storing the old way ident = " + tempIdent);
               //xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_") + ".xml",root);               
               //xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_") + ".xml",testStoreDOM);
               collStat = xdb.openAdminCollection("statv" + versionNumber.replace('.','_'));
               xdb.storeXMLResource(collStat,tempIdent.replaceAll("[^\\w*]","_") + ".xml",createStats(tempIdent));
            } catch(XMLDBException xdbe) {
               log.error(xdbe);
               return SOAPFaultException.createAdminSOAPFaultException(xdbe.getMessage(),xdbe);
            } catch(Exception e) {
                log.error(e);
                return SOAPFaultException.createAdminSOAPFaultException(e.getMessage(),e);                
            }
            finally {
                try {
                    xdb.closeCollection(coll);
                    xdb.closeCollection(collStat);
                }catch(XMLDBException xmldb) {
                    log.error(xmldb);
                }                
            }
         }else {
             //Okay it is either not managed by this Registry or a Registry type.
            addManageError = true;
                  //check if it is a registry type.
                  if(nodeVal.indexOf("Registry") != -1) {
                     addManageError = false;
                     log.debug("This is a RegistryType");
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
                            log.debug("try adding new manage node for this registry = " + manageNodeVal);
                            if(manageAuths.containsKey((tempAuthorityListKey = new AuthorityList(manageNodeVal,versionNumber)))) {
                                tempAuthorityListVal = (AuthorityList)manageAuths.get(tempAuthorityListKey);
                                log.error("Error - mismatch: Tried to update a Registry Type that has this managed Authority: " + manageNodeVal +
                                    " with this main Identifiers Authority ID " + ident + " This mismatches with another Registry Type that ownes/manages " + 
                                    " this same authority id, other registry type authority id: " + tempAuthorityListVal.getOwner());
                                return SOAPFaultException.createAdminSOAPFaultException("Mismatch on Authority id(s)",new RegistryException("Error - mismatch: Tried to update a Registry Type that has this managed Authority: " + manageNodeVal +
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
                            return SOAPFaultException.createAdminSOAPFaultException("No matching authority id found",new RegistryException("Trying to update the main Registry Type for this Registry with no matching Managed AuthorityID with the Identifiers AuthorityID; The AuthorityID = " + ident));
                        }
                     //update this registry resource into our registry.
                     try {
                        coll = xdb.openAdminCollection(collectionName);
                        xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_") + ".xml",DomHelper.ElementToString((Element)root));
                        collStat = xdb.openAdminCollection("statv" + versionNumber.replace('.','_'));
                        if(xdb.getResource(collStat,tempIdent.replaceAll("[^\\w*]","_") + ".xml") == null) {
                            xdb.storeXMLResource(collStat,tempIdent.replaceAll("[^\\w*]","_") + ".xml",createStats(tempIdent,false));
                        }
                     } catch(XMLDBException xdbe) {
                        log.error(xdbe);
                        return SOAPFaultException.createAdminSOAPFaultException(xdbe.getMessage(),xdbe);
                     } finally {
                         try {
                             xdb.closeCollection(coll);
                             xdb.closeCollection(collStat);
                         }catch(XMLDBException xmldb) {
                             log.error(xmldb);
                         }
                     }                     
                  }else if(nodeVal.indexOf("Authority") != -1)
                  {
                     // Okay it is an AuthorityType and if no other registries 
                     // manage this authority then we can place it in this 
                     // registry as a new managed authority.
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
                           log.debug(
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
                           
                           // For some reason the DOM model threw exceptions 
                           // when I tried to insert it as a sibling after 
                           // another existing ManagedAuthority tag, so just 
                           // add it to the end for now; just by luck the managedAuthority element is the last
                           // child for a Registry type otherwise we would be creating not valid to schema xml here.
                           NodeList resListForAuth = loadedRegistry.getElementsByTagNameNS("*","Resource");
                           resListForAuth.item(0).appendChild(newManage);
                           manageAuths.put(new AuthorityList(ident,versionNumber),new AuthorityList(ident,versionNumber,authorityID));

                           // Update our currentResource into the database
                           root = xsDoc.createElement("AstrogridResource");
                           root.appendChild(currentResource);
                           try {
                               collStat = xdb.openAdminCollection("statv" + versionNumber.replace('.','_'));
                               xdb.storeXMLResource(collStat,tempIdent.replaceAll("[^\\w*]","_") + ".xml",createStats(tempIdent));                        
                               
                               coll = xdb.openAdminCollection(collectionName);
                               //xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_") + ".xml",root);
                               xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_") + ".xml",DomHelper.ElementToString((Element)root));
                           } catch(XMLDBException xdbe) {
                               log.error(xdbe);
                               return SOAPFaultException.createAdminSOAPFaultException(xdbe.getMessage(),xdbe);                               
                           }finally {
                                try {
                                    xdb.closeCollection(collStat);
                                }catch(XMLDBException xmldb) {
                                    log.error(xmldb);
                                }                
                            }
                           
                           // Now get the information to re-update the
                           // Registry Resource which is for this registry.
                           ident = RegistryDOMHelper.getAuthorityID(loadedRegistry.
                                                  getDocumentElement());
                           log.debug("the ident from loaded registry right before update = " + ident);
                           resKey = RegistryDOMHelper.getResourceKey(loadedRegistry.
                                                   getDocumentElement());
                           log.debug("the resKey form loaded registry right before update = " + resKey);
                           //tempIdent = "ivo://" + ident;
                           tempIdent = ident;
                           if(resKey != null) tempIdent += "/" + resKey;
                           resListForAuth = loadedRegistry.getElementsByTagNameNS("*","Resource");
                           Element elem = loadedRegistry.
                                          createElement("AstrogridResource");
                           elem.appendChild(resListForAuth.item(0));                           
                           try {
                               log.debug("updating the new registy");
                               //xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_") + ".xml",elem);
                               xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_") + ".xml",DomHelper.ElementToString((Element)elem));
                           } catch(XMLDBException xdbe) {
                               log.error(xdbe);
                               return SOAPFaultException.createAdminSOAPFaultException(xdbe.getMessage(),xdbe);                               
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
                        }//else                           
                     }//if      
                  }//elseif   
            if(addManageError) {
               log.debug("Error authority id not managed by this registry throwing SOAPFault exception; the authority id = " + ident);
               return SOAPFaultException.createAdminSOAPFaultException("AuthorityID not managed by this registry",new RegistryException("Trying to update an entry that is not managed by this Registry authority id = " + ident));
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
          returnDoc.appendChild(returnDoc.createElementNS("http://www.astrogrid.org/registry/wsdl","UpdateResponse"));          
      }catch(ParserConfigurationException pce) {
          pce.printStackTrace();
          log.error(pce);
          return SOAPFaultException.createAdminSOAPFaultException("Could not create successfull DOM for soap body",
                  new RegistryException("Could not create successfull DOM for soap body"));          
      }
      
      log.info("Time taken to complete update on server = " +
               (System.currentTimeMillis() - beginUpdate) + "milliseconds");
      log.debug("end updateResource");

      return returnDoc;
   }
   
   public void remove(String id, String versionNumber) throws RegistryException, XMLDBException {
       String collectionName = "astrogridv" + versionNumber.replace('.','_');
       Collection coll = xdb.openAdminCollection(collectionName);
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryDOMHelper.getDefaultVersionNumber();
       }           
       if(id == null || id.trim().length() <= 0) {
           throw new RegistryException("Cannot have empty or null identifier");
       }
       String queryIvorn = id;
       if(Ivorn.isIvorn(id)) { 
           queryIvorn = id.substring(6);
       }
       
       id = queryIvorn.replaceAll("[^\\w*]","_");
       id += ".xml";
       xdb.removeResource(coll,id);
   }

   /**
    * Method: clearManagedCache
    * Description: Not really needed, used as a convenience method to clear the managed authority cache
    * of the Hashmap; normally by the clear cache jsp page.
    * 
    * @param versionNumber the hash map of the version to be cleard. No longer used.
    * 
    */
   public void clearManagedCache() {
        manageAuths.clear();
   }
   
   /**
    * Method: populateManagedMaps
    * Description: Small method to query for Registry type xml entries and get there Managed Authority ids
    * for placing into a HashMap.  This hashmap is used to verify no conflicts in authority id's.  Only one
    * registry can manage a particular Authority id.
    * 
    * @param collectionName
    * @param versionNumber
    * @throws XMLDBException
    */
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
    * Method: updateNoCheck
    * Description: Also an update method that updates into this Registry's db. But it does no
    * special checking.  This is used internally by harvesting other registries to
    * go ahead and place the Resources into our db.  It does check the Registry type entries
    * coming in to verify the authority id's are not conflicting with other Registries.
    * 
    * @param update A DOM of XML of  one or more Resources.
    * @param verisonNumber the version of the registry to be updated, this discovers the colleciton/table name
    * that is being updated.
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
          throw new IOException("Error nothing to update 'null sent as Document'");
      }
      
      String authorityID = conf.getString("reg.amend.authorityid");      
      
      //String attrVersion = null;
      //the vr attribute can live at either or both of those elments and we just need to get the first one.
      //It is possible the <update> element will not be there hence we need to look at the root element
      //NodeList nl = DomHelper.getNodeListTags(update,"Resource","vr");
      NodeList nl = update.getElementsByTagNameNS("*","Resource");
      if(nl.getLength() == 0) {
          nl = update.getElementsByTagNameNS("*","resource");
          //System.out.println("the resource nl.getLength= " + nl.getLength());
      }
      
      if(versionNumber == null) {
          versionNumber = RegistryDOMHelper.getRegistryVersionFromNode(nl.item(0));
      }
      if(nl.getLength() == 0) {
          log.debug("Nothing to Update");
          return;
      }
      log.debug("the nl length of resoruces = " + nl.getLength());      

      String vrNS = "http://www.ivoa.net/xml/VOResource/v" + versionNumber;
      //String versionNumber = attrVersion.replace('.','_');      
      String collectionName = "astrogridv" + versionNumber.replace('.','_');
      String defaultNS = null;
      log.debug("Collection Name = " + collectionName);
      
      boolean hasStyleSheet = conf.getBoolean("reg.custom.harveststylesheet." + versionNumber,false);
      Document xsDoc = null;
      //System.out.println("has stylesheet for " + "org.astrogrid.registry.updatestylesheet.onHarvest." + versionNumber);
      log.debug("Before the transform:::");
      log.debug(DomHelper.DocumentToString(update));
      if(hasStyleSheet) {
         //System.out.println("lets call transform update");
         log.debug("performing transformation before analysis of update for versionNumber = " + versionNumber);
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
              log.error(xmldbe);
          }
      }else if(!manageAuths.isEmpty() && !manageAuths.containsKey(someTestAuth)) {
          try {
              populateManagedMaps(collectionName, versionNumber);
          }catch(XMLDBException xmldbe) {
              xmldbe.printStackTrace();
              log.error(xmldbe);              
          }
      }
      
      //hmmm user is doing harvesting before he setup the registry, okay let it go.
      if(manageAuths.isEmpty()) {
          log.debug("seems like user is doing harvesting first for versionNumber=" + versionNumber);
          //okay this must be the very first time into the registry where
          //registry is empty. So put a an empty entry for this version.          
      }
      
      // This does seem a little strange as if an infinte loop,
      // but later on an appendChild is performed which
      // automatically reduced the length by one.
      final int resourceNum = nl.getLength();
      boolean updateResource = true;
      for(int i = 0;i < resourceNum;i++) {
         updateResource = true;
         ident = RegistryDOMHelper.getAuthorityID( (Element)nl.item(0));
         resKey = RegistryDOMHelper.getResourceKey( (Element)nl.item(0));
         Element currentResource = (Element)nl.item(0);
         if(manageAuths.containsValue(new AuthorityList(ident,versionNumber,authorityID))) {
             log.error("Either your harvesting your own Registry or another Registry is submitting authority id's owned by this registry. Ident = " + ident);
             currentResource.getParentNode().removeChild(currentResource);
         } else {
             //tempIdent = "ivo://" + ident;
             tempIdent = ident;
             if(resKey != null) tempIdent += "/" + resKey;
             log.debug("the ident in updateNoCheck = " + tempIdent);
             
             //root = update.createElement("AstrogridResource");
             if(currentResource.hasAttributes()) {                 
                 Node typeAttribute = currentResource.getAttributes().getNamedItem("xsi:type");
                 String nodeVal = null;
                 if(typeAttribute != null) {
                     nodeVal = typeAttribute.getNodeValue();
                 }
                 log.debug(
                 "Checking xsi:type for a Registry: = " 
                 + nodeVal);
                     //check if it is a registry type.
                     if(nodeVal != null && nodeVal.indexOf("Registry") != -1)
                     {
                        log.debug("A RegistryType in updateNoCheck add stats");
                        //update this registry resource into our registry.
                        try {                        
                            collStat = xdb.openAdminCollection("statv" + versionNumber.replace('.','_'));
                            if(xdb.getResource(collStat,tempIdent.replaceAll("[^\\w*]","_") + ".xml") == null) {
                                xdb.storeXMLResource(collStat,tempIdent.replaceAll("[^\\w*]","_") + ".xml",createStats(tempIdent,false));
                            }else {
                                xdb.storeXMLResource(collStat,tempIdent.replaceAll("[^\\w*]","_") + ".xml",createStats(tempIdent));
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
                                        " this same authority id, other registry type authority id: " + tempAuthorityListVal.getOwner() + "; NO UPDATE WILL HAPPEN FOR THIS Registry Type Resouce Entry");
                                   updateResource = false;                                   
                               }//if                           
                               if(manageNodeVal != null && manageNodeVal.trim().length() > 0) {
                                   manageAuths.put(tempAuthorityListKey, new AuthorityList(manageNodeVal,versionNumber,ident));
                               }//if
                           }//for
                     }//if
                 }//if
                 root = xsDoc.createElement("AstrogridResource");
                 root.appendChild(currentResource);
                 
                 try {
                     if(updateResource) {
                         coll = xdb.openAdminCollection(collectionName);
                         xdb.storeXMLResource(coll,tempIdent.replaceAll("[^\\w*]","_") + ".xml",DomHelper.ElementToString((Element)root));
                     }//if
                 } finally {
                     try {
                         xdb.closeCollection(coll);
                     }catch(XMLDBException xmldb) {
                         log.error(xmldb);
                     }//try
                 }//try&finally             
         }//else
      }//for
      log.debug("end updateNoCheck");
   }
   
   
   private Node createStats( String tempIdent, boolean addMillis) {
       log.debug("start createStats");
       Date statsTimeMillis = new Date();
       DateFormat shortDT = DateFormat.getDateTimeInstance();
       String statsXML = "<ResourceStat><Identifier>ivo://" + tempIdent +
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
