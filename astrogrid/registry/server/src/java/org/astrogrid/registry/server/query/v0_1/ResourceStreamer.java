package org.astrogrid.registry.server.query.v0_1;

import org.astrogrid.registry.server.query.ResourceStreamDelegate;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import java.util.List;


/**
 * Class: ResultStreamer
 * Description: RegistryXMLStreamDelegate is the main class for streaming results
 * to the client, this subclass overrides the abstract method getResourceContent to
 * do anything special to any single Resource before being sent to the client.  Also the class
 * passes up to the super class the set of Resources and any xml wrapper that is needed for the
 * XML.
 * @author kevinbenson
 *
 */
public class ResourceStreamer extends ResourceStreamDelegate {
	
	
	/**
	 * Constructor
	 * Simply passes the information up to the abstract super class RegistryXMLStreamDelegate.
	 * Passes a set of XML Resources to be iterated through and sent to the client.
	 * @param resSet ResourceSet much like ResultSet.  Set of XML Resources.
	 * @param xmlWrapper XML String to be used for wrapping around the XML (full set) of Resources.
	 */	
	public ResourceStreamer(List resSet, String xmlWrapper) {
		super(resSet, xmlWrapper);
	}
	
	
	/**
	 * Method: getResourceContent
	 * Description: returns a String of XML for a Single XML Resource.  A Resource is
	 * passed in and analyzed to see if it needs anything special added to it such as schemaLocations or
	 * dates being modified or only to extract out the identifier elements.
	 * @param res Single XML Resource object from the database.
	 * @param identOnly boolean to determine if only identifier elements are needed.
	 * @return XML String of the Resource to be streamed out to the client.
	 * @throws org.xmldb.api.base.XMLDBException Error fetching/connecting to the database to get the Resource object contents.
	 */	
	public String getResourceContent(Resource res, boolean identOnly) throws org.xmldb.api.base.XMLDBException {
		StringBuffer resContent = new StringBuffer(res.getContent().toString());
		int tempIndex;
		if(identOnly) {
			tempIndex = resContent.indexOf(">",resContent.indexOf("identifier"));
			return "<identifier>" + resContent.substring(tempIndex+1,resContent.indexOf("<",tempIndex)) + "</identifier>";
		}
		String schemaLocations = null;
        schemaLocations =  "http://www.ivoa.net/xml/VOResource/v0.10 " + 
                           schemaLocationBase + "vo-resource-types/VOResource/v0.10/VOResource.xsd ";
        
		
		String temp = resContent.substring(0,resContent.indexOf(">"));
		tempIndex = resContent.indexOf("created=\"");
		int tempIndex2;
		if(tempIndex != -1 && tempIndex < temp.length()) {
			tempIndex2 = (resContent.indexOf("\"",tempIndex + 10));
            tempIndex = (resContent.indexOf("T",tempIndex + 10));
            if(tempIndex != -1 && tempIndex < tempIndex2) {
                resContent.replace(tempIndex,tempIndex2,"");
            }			
			temp = resContent.substring(0,resContent.indexOf(">"));
		}
		
		tempIndex = resContent.indexOf("updated=\"");
		if(tempIndex != -1 && tempIndex < temp.length()) {
			tempIndex2 = (resContent.indexOf("\"",tempIndex + 10));
            tempIndex = (resContent.indexOf("T",tempIndex + 10));
            if(tempIndex != -1 && tempIndex < tempIndex2) {
                resContent.replace(tempIndex,tempIndex2,"");
            }			
			temp = resContent.substring(0,resContent.indexOf(">"));
		}
		
        //see if it has a schemaLocation attribute if not then we need to add it.
          if(temp.indexOf("schemaLocation") == -1) {
        	  if((tempIndex = temp.indexOf("type")) != -1) {
              	tempIndex = temp.indexOf("\"",tempIndex);
              	temp = temp.substring((tempIndex+1),temp.indexOf("\"",tempIndex+1));
                if(temp.endsWith("Registry") || temp.endsWith("Authority")) {
                	schemaLocations = "http://www.ivoa.net/xml/VORegistry/v0.3 " + schemaLocationBase + "vo-resource-types/VORegistry/v0.3/VORegistry.xsd";
                } else {
                	schemaLocations = "http://www.ivoa.net/xml/VODataService/v0.5 " + schemaLocationBase + "vo-resource-types/VODataService/v0.5/VODataService.xsd " +
                    "http://www.ivoa.net/xml/VOTable/v1.0 " + schemaLocationBase + "vo-formats/VOTable/v1.0/VOTable.xsd";
                    if(temp.endsWith("ConeSearch")) {
                        schemaLocations += " http://www.ivoa.net/xml/ConeSearch/v0.3 " + schemaLocationBase + "vo-resource-types/ConeSearch/v0.3/ConeSearch.xsd";    
                    }else if(temp.endsWith("SimpleImageAccess")) {
                        schemaLocations += " http://www.ivoa.net/xml/SIA/v0.7 " + schemaLocationBase + "vo-resource-types/SIA/v0.7/SIA.xsd";
                    }else if(temp.endsWith("TabularDB")) {
                        schemaLocations += " urn:astrogrid:schema:vo-resource-types:TabularDB:v0.3 " + schemaLocationBase + "vo-resource-types/TabularDB/v0.3/TabularDB.xsd";
                    }else if(temp.endsWith("OpenSkyNode")) {
                        schemaLocations += " http://www.ivoa.net/xml/OpenSkyNode/v0.1 " + schemaLocations + "vo-resource-types/OpenSkyNode/v0.1/OpenSkyNode.xsd";
                    }else if(temp.endsWith("CeaService") || temp.endsWith("CeaHttpApplicationType") || temp.endsWith("CeaApplicationType")) {
                        schemaLocations += "http://www.ivoa.net/xml/CEAService/v0.2 " + schemaLocations + "vo-resource-types/CEAService/v0.2/CEAService.xsd";
                    }
                }
                //add schemaLocation.
                resContent.insert(resContent.indexOf(">")," xsi:schemaLocation=\"" + schemaLocations + "\"");
                
        }//if type
          }//if schemalocations
       
          //resContent.insert(resContent.indexOf("Resource"),"ri:");
          //resContent.insert(resContent.indexOf(">")," xmlns:ri=\"" + RegistryQueryService.QUERY_WSDL_NS + "\" ");
          //resContent.insert(resContent.indexOf("/Resource")+1,"ri:");
          return resContent.toString();          
	}
}