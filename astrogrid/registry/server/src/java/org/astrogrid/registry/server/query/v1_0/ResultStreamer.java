package org.astrogrid.registry.server.query.v1_0;

import org.astrogrid.registry.server.query.RegistryXMLStreamDelegate;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;


public class ResultStreamer extends RegistryXMLStreamDelegate {
	
	
	public ResultStreamer(ResourceSet resSet, String xmlWrapper) {
		super(resSet, xmlWrapper);
	}
	
	public ResultStreamer(ResourceSet resSet, String xmlWrapper, boolean identOnly) {
		super(resSet, xmlWrapper, identOnly);
	}	
	
	public String getResourceContent(Resource res, boolean identOnly) throws org.xmldb.api.base.XMLDBException {
		StringBuffer resContent = new StringBuffer(res.getContent().toString());
		int tempIndex;
		if(identOnly) {
			tempIndex = resContent.indexOf(">",resContent.indexOf("identifier"));
			//ri:VOResources with the defined ri namespace was written out as a parent element.
			return "<ri:identifier>" + resContent.substring(tempIndex+1,resContent.indexOf("<",tempIndex)) + "</ri:identifier>";
		}
		String schemaLocations = null;
        schemaLocations =  "http://www.ivoa.net/xml/RegistryInterface/v1.0 "  + schemaLocationBase + 
                           "registry/RegistryInterface/v1.0/RegistryInterface.xsd " +
                           "http://www.ivoa.net/xml/VOResource/v1.0 " + 
                           schemaLocationBase + "vo-resource-types/VOResource/v1.0/VOResource.xsd ";
        
		
		String temp = resContent.substring(0,resContent.indexOf(">"));
        //see if it has a schemaLocation attribute if not then we need to add it.
          if(temp.indexOf("schemaLocation") == -1) {
        	  if((tempIndex = temp.indexOf("type")) != -1) {
              	tempIndex = temp.indexOf("\"",tempIndex);
              	temp = temp.substring((tempIndex+1),temp.indexOf("\"",tempIndex+1));
                if(temp.endsWith("Registry") || temp.endsWith("Authority")) {
                    schemaLocations += " http://www.ivoa.net/xml/VORegistry/v1.0 " + schemaLocationBase + "vo-resource-types/VORegistry/v1.0/VORegistry.xsd";
                } else {
                    schemaLocations += " http://www.ivoa.net/xml/VODataService/v1.0 " + schemaLocationBase + "vo-resource-types/VODataService/v1.0/VODataService.xsd " +
                    "http://www.ivoa.net/xml/VOTable/v1.0 " + schemaLocationBase + "vo-formats/VOTable/v1.0/VOTable.xsd";
                    if(temp.endsWith("ConeSearch")) {
                        schemaLocations += " http://www.ivoa.net/xml/ConeSearch/v1.0 " + schemaLocationBase + "vo-resource-types/ConeSearch/v1.0/ConeSearch.xsd";    
                    }else if(temp.endsWith("SimpleImageAccess")) {
                        schemaLocations += " http://www.ivoa.net/xml/SIA/v1.0 " + schemaLocationBase + "vo-resource-types/SIA/v1.0/SIA.xsd";
                    }else if(temp.endsWith("TabularDB")) {
                        schemaLocations += " urn:astrogrid:schema:vo-resource-types:TabularDB:v1.0 " + schemaLocationBase + "vo-resource-types/TabularDB/v1.0/TabularDB.xsd";
                    }else if(temp.endsWith("OpenSkyNode")) {
                        schemaLocations += " http://www.ivoa.net/xml/OpenSkyNode/v1.0 " + schemaLocations + "vo-resource-types/OpenSkyNode/v1.0/OpenSkyNode.xsd";
                    }else if(temp.endsWith("CeaService") || temp.endsWith("CeaHttpApplicationType") || temp.endsWith("CeaApplicationType")) {
                        schemaLocations += "http://www.ivoa.net/xml/CEAService/v1.0 " + schemaLocations + "vo-resource-types/CEAService/v1.0/CEAService.xsd";
                    }
                }
                //add schemaLocation.
                resContent.insert(resContent.indexOf(">")," xsi:schemaLocation=\"" + schemaLocations + "\"");
        }//if type
          }//if schemalocations
          return resContent.toString();          
	}
	
	
}