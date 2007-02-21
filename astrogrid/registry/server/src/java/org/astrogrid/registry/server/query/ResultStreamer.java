package org.astrogrid.registry.server.query;

import org.astrogrid.registry.server.query.RegistryXMLStreamDelegate;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;


public class ResultStreamer extends RegistryXMLStreamDelegate {
	
	
	public ResultStreamer(ResourceSet resSet, String xmlWrapper) {
		super(resSet, xmlWrapper);
	}
		
	public String getResourceContent(Resource res, boolean identOnly) throws org.xmldb.api.base.XMLDBException {
		return res.getContent().toString();
	}	
}