package org.tools.wstool;

import org.xml.sax.*;

public class XMLErrorHandler implements ErrorHandler {
	
	public void error(SAXParseException spe) throws SAXException {
		throw new SAXException(spe);
	}
	
	public void warning(SAXParseException spe) throws SAXException {
		System.out.println("\nParse Warning: " + spe.getMessage());
	}
	
	public void fatalError(SAXParseException spe) throws SAXException {
		throw new SAXException(spe);
	}
}