package org.astrogrid.desktop.modules.system.converters;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.Converter;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Noel Winstanley
 * @since Apr 14, 20062:34:21 AM
 */
public  class DocumentConverter implements Converter {
	public Object convert(Class arg0, Object arg1) {
	   if (arg0 != Document.class) { 
	       throw new RuntimeException("Can only convert to documents " + arg0.getName());
	   }
	   ByteArrayInputStream is= new ByteArrayInputStream(arg1.toString().getBytes());
	   try {
	    return DomHelper.newDocument(is);
	} catch (ParserConfigurationException e) {
	    throw new RuntimeException(e);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	       
	}
}