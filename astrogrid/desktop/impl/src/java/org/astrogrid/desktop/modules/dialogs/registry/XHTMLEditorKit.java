/* XHTMLEditorKit.java
 * Created on 22-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.dialogs.registry;

import java.io.IOException;
import java.io.Reader;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author pjn3
 * Extends HTMLEditorKit to improve how JEditorPanel renders XHTML.
 * @see javax.swing.text.html.HTMLEditorKit
 */
public class XHTMLEditorKit extends HTMLEditorKit {
	protected Parser getParser() {
		return new Parser() {
			public void parse(Reader reader, ParserCallback callback, boolean ignoreCharSet) throws IOException {
				try {
					SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
					SaxHandler handler = new SaxHandler(callback);
					parser.parse(new InputSource(reader), handler);
				} catch(Exception ex) {
					ex.printStackTrace();
					IOException ioex = new IOException();
					ioex.initCause(ex);
					throw ioex;
				}
			}
		};
	}
	
	private static class SaxHandler extends DefaultHandler {
		private final ParserCallback callback;
		
		public SaxHandler(ParserCallback callback) {
			this.callback = callback;
		}
		
		public void endElement(String uri, String name, String qName) throws SAXException {
			if (HTML.getTag(qName) != null)
			    callback.handleEndTag(HTML.getTag(qName), -1);
		}
		
		public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {			
			SimpleAttributeSet attributeSet = convertAttributes(atts);			
			if (HTML.getTag(qName) != null)
			    callback.handleStartTag(HTML.getTag(qName), attributeSet, -1);
		}
		
		private SimpleAttributeSet convertAttributes(Attributes atts) {
			SimpleAttributeSet attributeSet = new SimpleAttributeSet();
			
			for (int i=0; i < atts.getLength(); i++) {
				HTML.Attribute attribute = HTML.getAttributeKey(atts.getQName(i));
				if (attribute != null)
				  attributeSet.addAttribute(attribute, atts.getValue(i));
			}
			return attributeSet;
		}
		
		public void characters(char[] ch, int start, int length) throws SAXException {
			char[] tmp = new char[length];
			System.arraycopy(ch, start, tmp, 0, length);
			callback.handleText(tmp, -1);
		}
		
		public void error(SAXParseException ex) throws SAXException {
			callback.handleError(ex.getMessage(), -1);
		}
		
		public void fatalError(SAXException ex) throws SAXException {
			callback.handleError(ex.getMessage(), -1);
		}
	}
}
