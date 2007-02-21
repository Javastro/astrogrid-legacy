package org.astrogrid.registry.server.admin;

import org.w3c.dom.Document;
import javax.xml.stream.*;
import org.astrogrid.registry.RegistryException;
import org.xmldb.api.base.XMLDBException;


public interface IAdmin {
	
	public XMLStreamReader Update(Document update);
	
	public Document updateInternal(Document update);	
	
	public void remove(String id) throws RegistryException, XMLDBException;
	
	public Document updateIndex(Document doc) throws XMLDBException, RegistryException;
	
}