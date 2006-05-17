/*$Id: ResolvingDescriptorParser.java,v 1.3 2006/05/17 23:57:46 nw Exp $
 * Created on 24-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.hivemind;

import java.io.InputStream;
import java.util.Map;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.parse.DescriptorParser;
import org.apache.hivemind.parse.ModuleDescriptor;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
/** extension of the standard hivemind descriptor parser that knows how to resolve external entities in xml - especially thos with the 'classpath:' protocol
 * 
 * used to allow my hivemind descrptors to include other xml files.
 * */
class ResolvingDescriptorParser extends DefaultHandler {
    public void begin(String arg0, Map arg1) {
        this.p.begin(arg0, arg1);
    }
    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
        this.p.characters(arg0, arg1, arg2);
    }
    public void end(String arg0) {
        this.p.end(arg0);
    }
    public void endDocument() throws SAXException {
        this.p.endDocument();
    }
    public void endElement(String arg0, String arg1, String arg2) throws SAXException {
        this.p.endElement(arg0, arg1, arg2);
    }
    public void endPrefixMapping(String prefix) throws SAXException {
        this.p.endPrefixMapping(prefix);
    }
    public boolean equals(Object obj) {
        return this.p.equals(obj);
    }
    public void error(SAXParseException arg0) throws SAXException {
        this.p.error(arg0);
    }
    public void fatalError(SAXParseException arg0) throws SAXException {
        this.p.fatalError(arg0);
    }
    public ModuleDescriptor getModuleDescriptor() {
        return this.p.getModuleDescriptor();
    }
    public int hashCode() {
        return this.p.hashCode();
    }
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        this.p.ignorableWhitespace(ch, start, length);
    }
    public void initialize(Resource arg0, ClassResolver arg1) {
        this.p.initialize(arg0, arg1);
    }
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
        this.p.notationDecl(name, publicId, systemId);
    }
    public void processingInstruction(String target, String data) throws SAXException {
        this.p.processingInstruction(target, data);
    }
    public void reset() {
        this.p.reset();
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        if (systemId.startsWith(PROTOCOL)) {
            InputStream is = this.getClass().getResourceAsStream(systemId.substring(PROTOCOL.length()));
            if (is != null) {
                return new InputSource(is);
            }
        }
        // fallback to default.
        try {
        return this.p.resolveEntity(publicId, systemId);
        }catch (Exception e) { // weird little work-around - seems to be necessary to make it compile in 1.4.
            throw new SAXException(e);
        }
    }        
    public void setDocumentLocator(Locator arg0) {
        this.p.setDocumentLocator(arg0);
    }
    public void skippedEntity(String name) throws SAXException {
        this.p.skippedEntity(name);
    }
    public void startDocument() throws SAXException {
        this.p.startDocument();
    }
    public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
        this.p.startElement(arg0, arg1, arg2, arg3);
    }
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        this.p.startPrefixMapping(prefix, uri);
    }
    public String toString() {
        return this.p.toString();
    }
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        this.p.unparsedEntityDecl(name, publicId, systemId, notationName);
    }
    public void warning(SAXParseException arg0) throws SAXException {
        this.p.warning(arg0);
    }
    public ResolvingDescriptorParser(final DescriptorParser p) {
        this.p = p;
    }
    private final DescriptorParser p;
    private static final String PROTOCOL = "classpath:";

}

/* 
$Log: ResolvingDescriptorParser.java,v $
Revision 1.3  2006/05/17 23:57:46  nw
documentation improvements.

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.2.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/28 13:47:35  nw
first webstartable version.
 
*/