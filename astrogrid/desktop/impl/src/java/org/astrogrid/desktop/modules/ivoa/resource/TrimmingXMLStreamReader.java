/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

/** XMLStream reader which delegates to an underlying implementation,
 * and trims to null all the data accesses.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 22, 200710:27:12 AM
 */
public final class TrimmingXMLStreamReader implements XMLStreamReader {

    private final XMLStreamReader orig;

    public void close() throws XMLStreamException {
        this.orig.close();
    }

    public int getAttributeCount() {
        return this.orig.getAttributeCount();
    }

    public String getAttributeLocalName(int arg0) {
        return this.orig.getAttributeLocalName(arg0);
    }

    public QName getAttributeName(int arg0) {
        return this.orig.getAttributeName(arg0);
    }

    public String getAttributeNamespace(int arg0) {
        return this.orig.getAttributeNamespace(arg0);
    }

    public String getAttributePrefix(int arg0) {
        return this.orig.getAttributePrefix(arg0);
    }

    public String getAttributeType(int arg0) {
        return this.orig.getAttributeType(arg0);
    }

    public String getAttributeValue(int arg0) {
        return StringUtils.trimToNull(this.orig.getAttributeValue(arg0));
    }

    public String getAttributeValue(String arg0, String arg1) {
        return StringUtils.trimToNull(this.orig.getAttributeValue(arg0, arg1));
    }

    public String getCharacterEncodingScheme() {
        return this.orig.getCharacterEncodingScheme();
    }

    public String getElementText() throws XMLStreamException {
        return StringUtils.trimToNull(this.orig.getElementText());
    }

    public String getEncoding() {
        return this.orig.getEncoding();
    }

    public int getEventType() {
        return this.orig.getEventType();
    }

    public String getLocalName() {
        return this.orig.getLocalName();
    }

    public Location getLocation() {
        return this.orig.getLocation();
    }

    public QName getName() {
        return this.orig.getName();
    }

    public NamespaceContext getNamespaceContext() {
        return this.orig.getNamespaceContext();
    }

    public int getNamespaceCount() {
        return this.orig.getNamespaceCount();
    }

    public String getNamespacePrefix(int arg0) {
        return this.orig.getNamespacePrefix(arg0);
    }

    public String getNamespaceURI() {
        return this.orig.getNamespaceURI();
    }

    public String getNamespaceURI(int arg0) {
        return this.orig.getNamespaceURI(arg0);
    }

    public String getNamespaceURI(String arg0) {
        return this.orig.getNamespaceURI(arg0);
    }

    public String getPIData() {
        return this.orig.getPIData();
    }

    public String getPITarget() {
        return this.orig.getPITarget();
    }

    public String getPrefix() {
        return this.orig.getPrefix();
    }

    public Object getProperty(String arg0) throws IllegalArgumentException {
        return this.orig.getProperty(arg0);
    }

    public String getText() {
        return StringUtils.trimToNull(this.orig.getText());
    }

    public char[] getTextCharacters() {
        return this.orig.getTextCharacters();
    }

    public int getTextCharacters(int arg0, char[] arg1, int arg2, int arg3)
            throws XMLStreamException {
        return this.orig.getTextCharacters(arg0, arg1, arg2, arg3);
    }

    // nb - might be incorrect.
    public int getTextLength() {
        return this.orig.getTextLength();
    }

    public int getTextStart() {
        return this.orig.getTextStart();
    }

    public String getVersion() {
        return this.orig.getVersion();
    }

    public boolean hasName() {
        return this.orig.hasName();
    }

    public boolean hasNext() throws XMLStreamException {
        return this.orig.hasNext();
    }

    public boolean hasText() {
        return this.orig.hasText();
    }

    public boolean isAttributeSpecified(int arg0) {
        return this.orig.isAttributeSpecified(arg0);
    }

    public boolean isCharacters() {
        return this.orig.isCharacters();
    }

    public boolean isEndElement() {
        return this.orig.isEndElement();
    }

    public boolean isStandalone() {
        return this.orig.isStandalone();
    }

    public boolean isStartElement() {
        return this.orig.isStartElement();
    }

    public boolean isWhiteSpace() {
        return this.orig.isWhiteSpace();
    }

    public int next() throws XMLStreamException {
        return this.orig.next();
    }

    public int nextTag() throws XMLStreamException {
        return this.orig.nextTag();
    }

    public void require(int arg0, String arg1, String arg2)
            throws XMLStreamException {
        this.orig.require(arg0, arg1, arg2);
    }

    public boolean standaloneSet() {
        return this.orig.standaloneSet();
    }

    public TrimmingXMLStreamReader(XMLStreamReader orig) {
        super();
        this.orig = orig;
    }
    

}