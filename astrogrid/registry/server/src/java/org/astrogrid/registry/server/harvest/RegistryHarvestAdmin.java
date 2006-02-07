package org.astrogrid.registry.server.harvest;

import org.astrogrid.registry.server.admin.RegistryAdminService;
import org.astrogrid.registry.common.RegistryDOMHelper;
import org.astrogrid.registry.server.admin.AdminHelper;
import org.astrogrid.registry.server.admin.AuthorityList;
import org.astrogrid.registry.server.admin.AuthorityListManager;
import org.astrogrid.util.DomHelper;
import org.astrogrid.registry.server.XSLHelper;
import org.astrogrid.registry.server.InvalidStorageNodeException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.XMLDBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

import java.util.ArrayList;


public class RegistryHarvestAdmin extends RegistryAdminService {
    
    /**
     * Logging variable for writing information to the logs
     */
   private static final Log log = 
                               LogFactory.getLog(RegistryHarvestAdmin.class);  
   
   public RegistryHarvestAdmin() {
       super();
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
   public void harvestingUpdate(Document update,String versionNumber) throws XMLDBException, InvalidStorageNodeException, IOException {
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
       }
       
       if(versionNumber == null) {
           versionNumber = RegistryDOMHelper.findVOResourceVersionFromNode(nl.item(0));
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
       log.debug("Before the transform:::");
       log.debug(DomHelper.DocumentToString(update));
       if(hasStyleSheet) {
          log.debug("performing transformation before analysis of update for versionNumber = " + versionNumber);
          xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),versionNumber,true);
       } else {
          xsDoc = update;
       }
       //log.info("the xsdoc = " + DomHelper.DocumentToString(xsDoc));
       nl = xsDoc.getElementsByTagNameNS("*","Resource");
       log.info("Number of Resources = " + nl.getLength());
       AuthorityList someTestAuth = new AuthorityList(authorityID,versionNumber);      
       if(manageAuths.isEmpty()) {
           try {
               alm.populateManagedMaps(collectionName, versionNumber);
           }catch(XMLDBException xmldbe) {
               xmldbe.printStackTrace();
               log.error(xmldbe);
           }
       }else if(!manageAuths.isEmpty() && !manageAuths.containsKey(someTestAuth)) {
           try {
               alm.populateManagedMaps(collectionName, versionNumber);
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
                            if(xdbRegistry.getResource(tempIdent.replaceAll("[^\\w*]","_") + ".xml", "statv" + versionNumber.replace('.','_')) == null) {
                                 xdbRegistry.storeXMLResource(tempIdent.replaceAll("[^\\w*]","_") + ".xml",
                                                              "statv" + versionNumber.replace('.','_'), 
                                                              adminHelper.createStats(tempIdent,false));
                            }else {
                                 xdbRegistry.storeXMLResource(tempIdent.replaceAll("[^\\w*]","_") + ".xml",
                                                      "statv" + versionNumber.replace('.','_'),
                                                      adminHelper.createStats(tempIdent));
                            }                                                                       
                            NodeList manageList = adminHelper.getManagedAuthorities(currentResource);
                            if(manageList.getLength() > 0)
                                alm.clearManagedAuthoritiesForOwner(ident, versionNumber);
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
                  
                      if(updateResource) {
                          xdbRegistry.storeXMLResource(tempIdent.replaceAll("[^\\w*]","_") + ".xml", 
                                                       collectionName, root);
                      }//if             
          }//else
       }//for
       log.debug("end updateNoCheck");
   }
}