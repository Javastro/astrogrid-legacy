package org.astrogrid.registry.server.harvest;

import org.astrogrid.registry.server.admin.RegistryAdminService;
import org.astrogrid.registry.common.RegistryValidator;
import org.astrogrid.registry.common.RegistryDOMHelper;
import org.astrogrid.registry.server.admin.AuthorityList;
import org.astrogrid.util.DomHelper;
import org.astrogrid.registry.server.XSLHelper;
import org.astrogrid.registry.server.InvalidStorageNodeException;
import org.astrogrid.registry.RegistryException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.XMLDBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

import java.text.DateFormat;
import java.util.Date;

import junit.framework.AssertionFailedError;
import org.xmldb.api.base.Collection;



public class RegistryHarvestAdmin extends RegistryAdminService {
    
    /**
     * Logging variable for writing information to the logs
     */
   private static final Log log = 
                               LogFactory.getLog(RegistryHarvestAdmin.class);
   
   
   
   public RegistryHarvestAdmin() {
       super(null,null,null);
   }
   
   public void addStatNewDate(String identifier, String versionNumber) throws RegistryException {
       Document statDoc = getStatus(identifier, versionNumber);
       if(statDoc != null) {
           Date statsTimeMillis = new Date();
           DateFormat shortDT = DateFormat.getDateTimeInstance();
           NodeList nl = statDoc.getElementsByTagNameNS("*","StatsDateMillis");
           if(nl.getLength() > 0) {
               nl.item(0).getFirstChild().setNodeValue(String.valueOf(statsTimeMillis.getTime()));
           }else {
               Element elem2 = statDoc.createElement("StatsDateMillis");
               elem2.appendChild(statDoc.createTextNode(String.valueOf(statsTimeMillis.getTime())));
               statDoc.getDocumentElement().appendChild(elem2);
           }
           nl = statDoc.getElementsByTagNameNS("*","StatsDate");
           if(nl.getLength() > 0) {
               nl.item(0).getFirstChild().setNodeValue(shortDT.format(statsTimeMillis));
           }else {
               Element elem = statDoc.createElement("StatsDate");
               elem.appendChild(statDoc.createTextNode(shortDT.format(statsTimeMillis)));
               statDoc.getDocumentElement().appendChild(elem);
           }
           storeStat(identifier, versionNumber, statDoc);
       }
   }
   
   public void addResumptionToken(String identifier, String versionNumber, String resumptionToken) throws RegistryException {
       Document statDoc = getStatus(identifier, versionNumber);
       if(statDoc != null) {
           //Date statsTimeMillis = new Date();
           //DateFormat shortDT = DateFormat.getDateTimeInstance();
           NodeList nl = statDoc.getElementsByTagNameNS("*","LastResumptionToken");
           if(nl.getLength() > 0) {
               nl.item(0).getFirstChild().setNodeValue(resumptionToken);
           }else {
               Element elem2 = statDoc.createElement("LastResumptiontoken");
               elem2.appendChild(statDoc.createTextNode(resumptionToken));
               statDoc.getDocumentElement().appendChild(elem2);
           }//else
       }//if
   }
   
   
   public void clearOAILastUpdateInfo(String identifier, String versionNumber) throws RegistryException {
       Document statDoc = getStatus(identifier, versionNumber);
       if(statDoc != null) {
       
           NodeList nl = statDoc.getElementsByTagNameNS("*","LastUpdateInfo");
           if(nl.getLength() > 0) {
               statDoc.removeChild(nl.item(0));
           }
           storeStat(identifier, versionNumber, statDoc);
       }else {
           System.out.println("it was null clearoai");
       }
   }
   
   public void addStatInfo(String identifier, String versionNumber, String info) throws RegistryException {
       Document statDoc = getStatus(identifier, versionNumber);
       if(statDoc != null) {
           
           NodeList nl = statDoc.getElementsByTagNameNS("*","LastUpdateInfo");
           if(nl.getLength() > 0) {
               Element elem = statDoc.createElement("Info");
               elem.appendChild(statDoc.createTextNode(info));
               nl.item(0).appendChild(elem);
           }else {
               Element lui = statDoc.createElement("LastUpdateInfo");
               Element elem = statDoc.createElement("Info");
               java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
               Element elemDT = statDoc.createElement("AttemptDate");
               java.util.Calendar rightNow = java.util.Calendar.getInstance();
               elemDT.appendChild(statDoc.createTextNode(sdf.format(rightNow.getTime())));           
               elem.appendChild(statDoc.createTextNode(info));
               lui.appendChild(elemDT);
               lui.appendChild(elem);
               statDoc.getDocumentElement().appendChild(lui);
           }
           storeStat(identifier, versionNumber, statDoc);
       }else {
           System.out.println("it was null info = " + info);
       }
   }
   
   public void addStatError(String identifier, String versionNumber, String error) throws RegistryException {
       Document statDoc = getStatus(identifier, versionNumber);
       if(statDoc != null) {
           
           NodeList nl = statDoc.getElementsByTagNameNS("*","LastUpdateInfo");
           if(nl.getLength() > 0) {
               Element elem = statDoc.createElement("Error");
               elem.appendChild(statDoc.createTextNode(error));
               nl.item(0).appendChild(elem);
           }else {
               Element lui = statDoc.createElement("LastUpdateInfo");
               java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
               Element elemDT = statDoc.createElement("AttemptDate");
               java.util.Calendar rightNow = java.util.Calendar.getInstance();
               elemDT.appendChild(statDoc.createTextNode(sdf.format(rightNow.getTime())));           
               Element elem = statDoc.createElement("Error");
               elem.appendChild(statDoc.createTextNode(error));
               lui.appendChild(elemDT);
               lui.appendChild(elem);
               statDoc.getDocumentElement().appendChild(lui);
           }
           storeStat(identifier, versionNumber, statDoc);
       }else {
           System.out.println("it was null error = " + error);
       }
   }
      
   public Document getStatus(String identifier, String versionNumber) throws RegistryException {
       String tempIdent = identifier;
       Document statDoc = null;
       if(identifier.startsWith("ivo"))
        tempIdent = identifier.substring(6);
       
       try {
          
           XMLResource xmlr = xdbRegistry.getResource(tempIdent.replaceAll("[^\\w*]","_") + ".xml","statv" + versionNumber.replace('.','_'));           
           if(xmlr != null) {
               try {
                System.out.println("it was not null in getSTatus and content = " + xmlr.getContent().toString());
                statDoc = DomHelper.newDocument(xmlr.getContent().toString());
               }catch(Exception e) {
                   log.error(e);
               }
           }
       } catch(XMLDBException xe) {
               throw new RegistryException(xe);
       }           
       return statDoc;
   }
   
   private void storeStat(String identifier, String versionNumber, Node statDoc) throws RegistryException {
       String tempIdent = identifier;
       if(identifier.startsWith("ivo"))
        tempIdent = identifier.substring(6);
       try {           
       xdbRegistry.storeXMLResource(tempIdent.replaceAll("[^\\w*]","_") + ".xml",
               "statv" + versionNumber.replace('.','_'), statDoc);
       }catch(InvalidStorageNodeException in) {
           log.error(in);
           throw new RegistryException(in);
       }catch(XMLDBException xe) {
           throw new RegistryException(xe);
       }
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
   public String harvestingUpdate(Document update, String versionNumber) throws XMLDBException, InvalidStorageNodeException, IOException {
       log.debug("start updateNoCheck");
       Node root = null;
       String ident = null;
       String resKey = null;
       String tempIdent = null;
       AuthorityList tempAuthorityListKey = null;
       AuthorityList tempAuthorityListVal = null;
       boolean validateResourceXML = true;
       String returnString = "";
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
       
       if(nl.getLength() == 0) {
           log.debug("Nothing to Update");
           return "";
       }
       log.debug("the nl length of resoruces = " + nl.getLength());      

       //String versionNumber = attrVersion.replace('.','_');      
       
       boolean hasStyleSheet = true;//conf.getBoolean("reg.custom.harveststylesheet." + versionNumber,false);
       Document xsDoc = null;

       log.debug("Before " + DomHelper.DocumentToString(update));
       if(hasStyleSheet) {
          log.debug("performing transformation before analysis of update for versionNumber = " + versionNumber);
          try {
        	  NodeList convertCheck = update.getDocumentElement().getElementsByTagNameNS("*","Resource");
        	  log.info("checking versions and convertCheck");
        	  if(versionNumber.equals("1.0") && convertCheck.getLength() > 0 &&
        	     convertCheck.item(0).getNamespaceURI().indexOf("0.10") != -1) {
        		  log.info("yes it needs converting to 1.0");
       			  xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),"0.10",true);
       			  xsDoc = xs.transformVersionConversion((Node)xsDoc.getDocumentElement());
       			  log.info("XML result After converting 0.10-1.0 = " + DomHelper.DocumentToString(xsDoc));
        	  }
        	  else {
        		  xsDoc = xs.transformUpdate((Node)update.getDocumentElement(),versionNumber,true);
        	  }
          }catch(RegistryException re) {
              log.error("Problem with xsl transformation of xml in the update method will try to use raw xml from web service ");
              log.error(re);
              xsDoc = update;
          }
       } else {
          xsDoc = update;
       }
       log.debug("After = " + DomHelper.DocumentToString(update));
       
       String collectionName = "astrogridv" + versionNumber.replace('.','_');
       log.debug("Collection Name = " + collectionName);
       /* && !collectionName.equals("astrogridv0_10") */
       //adding back the && check for 0.10 collection there is just
       //no point in validating 0.10 during harvests to many mistakes and
       //invalid to schema.
       if(validateResourceXML && !collectionName.equals("astrogridv0_10")) {
           try {
               //validate the xml, the xsl should have made it into a well-formed xml doc with a 
        	   //wrapper(root element) valid to schema, as far as the rest of the xml the validator
        	   //will check to see if it conforms to schema.
               RegistryValidator.isValid(xsDoc);
           }catch(AssertionFailedError afe) {
               afe.printStackTrace();
               log.error("Error invalid document = " + afe.getMessage());
               if(!versionNumber.equals("1.0")) {
            	   throw new IOException("Error validating document " + afe.getMessage());
               }else {
            	   log.error("though validation error occurred 1.0 XML will be revalidated individually for each Resource");
               }
               //return SOAPFaultException.createAdminSOAPFaultException("Server Error: " + "Invalid update, document not valid ",afe.getMessage());           
           }//catch
       }

       nl = xsDoc.getElementsByTagNameNS("*","Resource");
       log.info("Number of Resources to try validating and updating = " + nl.getLength());
       if(versionNumber.equals("1.0")) {
    	   int loopi = 0;
    	   while(loopi < nl.getLength()) {
    		   log.info("loopi = " + loopi + " and nl.getlength = " + nl.getLength());
    		   try {
    			   RegistryValidator.isValid(((Element)nl.item(loopi)),"Resource");
    			   loopi++;
    		   }catch(AssertionFailedError afe) {
    			   //log.error("Error invalid individual Resource = " + afe.getMessage() + " loop i = " + loopi);
    			   xsDoc.getDocumentElement().removeChild(nl.item(loopi));
    			   //log.info("nl.getlength in afe after removechild = " + nl.getLength());
                   //afe.printStackTrace();
                   
                   //return SOAPFaultException.createAdminSOAPFaultException("Server Error: " + "Invalid update, document not valid ",afe.getMessage());           
               }//catch
    	   }//while
       }
       
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
       String errorMessages = "";
       
       // This does seem a little strange as if an infinte loop,
       // but later on an appendChild is performed which
       // automatically reduced the length by one.
       final int resourceNum = nl.getLength();
       log.info("Attempting to update number of Resources = " + resourceNum);
       boolean updateResource = true;
       Collection harvestColl = xdbRegistry.getCollection(collectionName,true);
       try {
       for(int i = 0;i < resourceNum;i++) {
          updateResource = true;
          Element currentResource = (Element)nl.item(0);
          ident = RegistryDOMHelper.getAuthorityID( currentResource);
          resKey = RegistryDOMHelper.getResourceKey( currentResource);
          

          
          if(manageAuths.containsValue(new AuthorityList(ident,versionNumber,authorityID))) {
              log.error("Either your harvesting your own Registry or another Registry is submitting authority id's owned by this registry. Ident = " + ident);
              currentResource.getParentNode().removeChild(currentResource);
              continue;
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
                            /*
                            if(xdbRegistry.getResource(tempIdent.replaceAll("[^\\w*]","_") + ".xml", "statv" + versionNumber.replace('.','_')) == null) {
                                 xdbRegistry.storeXMLResource(tempIdent.replaceAll("[^\\w*]","_") + ".xml",
                                                              "statv" + versionNumber.replace('.','_'), 
                                                              adminHelper.createStats(tempIdent,false));
                            }
                            
                            else {
                                 xdbRegistry.storeXMLResource(tempIdent.replaceAll("[^\\w*]","_") + ".xml",
                                                      "statv" + versionNumber.replace('.','_'),
                                                      adminHelper.createStats(tempIdent));
                            }
                            */
                            NodeList manageList = adminHelper.getManagedAuthorities(currentResource);
                            if(manageList.getLength() > 0)
                                alm.clearManagedAuthoritiesForOwner(ident, versionNumber);
                            else {
                                log.warn("Registry type from a Harvest has no ManagedAuthorities; AuthorityID = " + ident + " versionNumber = " + versionNumber);
                                errorMessages += "Registry type from a Harvest has no ManagedAuthorities, this is okay but rare logging as error for double checking; AuthorityID = " + ident + " versionNumber = " + versionNumber + "\n";
                            }
                            
                            for(int k = 0;k < manageList.getLength();k++) {
                                String manageNodeVal = manageList.item(k).getFirstChild().getNodeValue();
                                if(manageAuths.containsKey((tempAuthorityListKey = new AuthorityList(manageNodeVal,versionNumber)))) {
                                    tempAuthorityListVal = (AuthorityList)manageAuths.get(tempAuthorityListKey);
                                    log.error("Error - mismatch: Tried to update a Registry Type that has this managed Authority: " + manageNodeVal +
                                         " with this main Identifiers Authority ID " + ident + " This mismatches with another Registry Type that ownes/manages " + 
                                         " this same authority id, other registry type authority id: " + tempAuthorityListVal.getOwner() + "; NO UPDATE WILL HAPPEN FOR THIS Registry Type Resouce Entry");
                                    errorMessages += "Error - mismatch: Tried to update a Registry Type that has this managed Authority: " + manageNodeVal +
                                    " with this main Identifiers Authority ID " + ident + " This mismatches with another Registry Type that ownes/manages " + 
                                    " this same authority id, other registry type authority id: " + tempAuthorityListVal.getOwner() + "; NO UPDATE WILL HAPPEN FOR THIS Registry Type Resouce Entry\n";
                                    updateResource = false;                                   
                                }//if                           
                                if(manageNodeVal != null && manageNodeVal.trim().length() > 0) {
                                    manageAuths.put(tempAuthorityListKey, new AuthorityList(manageNodeVal,versionNumber,ident));
                                }//if
                            }//for
                            if(errorMessages.trim().length() > 0) {
                            	throw new IOException(errorMessages);
                            	/*
                                try {
                                    addStatError(tempIdent, versionNumber, errorMessages);
                                }catch(RegistryException re) { }
                                errorMessages = "";
                                */
                            }                            
                      }//if
                  }//if
                      if(updateResource) {
                          root = xsDoc.createElementNS("urn:astrogrid:schema:RegistryStoreResource:v1","agr:AstrogridResource");
                          root.appendChild(currentResource);
                          xdbRegistry.storeXMLResource(harvestColl, tempIdent.replaceAll("[^\\w*]","_") + ".xml", root); 
                                                         //collectionName, root /*currentResource*/);
                          //xdbRegistry.storeXMLResource(tempIdent.replaceAll("[^\\w*]","_") + ".xml", 
                                                       //collectionName, root /*currentResource*/);
                      }else {
                        currentResource.getParentNode().removeChild(currentResource);
                      }
                      returnString += tempIdent + ",\r\n";
          }//else
       }//for
       }finally {
           xdbRegistry.closeCollection(harvestColl);
           //System.out.println("harvesting update took this long with remove= " + (System.currentTimeMillis() - begin));
       }
       log.debug("end updateNoCheck");
       return returnString;
   }
}