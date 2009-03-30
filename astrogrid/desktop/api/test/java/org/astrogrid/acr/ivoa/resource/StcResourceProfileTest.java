/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import junit.framework.TestCase;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:22:03 PM
 */
public class StcResourceProfileTest extends TestCase {

    /**
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Oct 6, 200812:49:24 PM
     */
    private static final class DummyMockTestDocument implements Document {
        public Node adoptNode(final Node source) throws DOMException {
            return null;
        }

        public Attr createAttribute(final String name) throws DOMException {
            return null;
        }

        public Attr createAttributeNS(final String namespaceURI,
                final String qualifiedName) throws DOMException {
            return null;
        }

        public CDATASection createCDATASection(final String data)
                throws DOMException {
            return null;
        }

        public Comment createComment(final String data) {
            return null;
        }

        public DocumentFragment createDocumentFragment() {
            return null;
        }

        public Element createElement(final String tagName) throws DOMException {
            return null;
        }

        public Element createElementNS(final String namespaceURI,
                final String qualifiedName) throws DOMException {
            return null;
        }

        public EntityReference createEntityReference(final String name)
                throws DOMException {
            return null;
        }

        public ProcessingInstruction createProcessingInstruction(
                final String target, final String data) throws DOMException {
            return null;
        }

        public Text createTextNode(final String data) {
            return null;
        }

        public DocumentType getDoctype() {
            return null;
        }

        public Element getDocumentElement() {
            return null;
        }

        public String getDocumentURI() {
            return null;
        }

        public DOMConfiguration getDomConfig() {
            return null;
        }

        public Element getElementById(final String elementId) {
            return null;
        }

        public NodeList getElementsByTagName(final String tagname) {
            return null;
        }

        public NodeList getElementsByTagNameNS(final String namespaceURI,
                final String localName) {
            return null;
        }

        public DOMImplementation getImplementation() {
            return null;
        }

        public String getInputEncoding() {
            return null;
        }

        public boolean getStrictErrorChecking() {
            return false;
        }

        public String getXmlEncoding() {
            return null;
        }

        public boolean getXmlStandalone() {
            return false;
        }

        public String getXmlVersion() {
            return null;
        }

        public Node importNode(final Node importedNode, final boolean deep)
                throws DOMException {
            return null;
        }

        public void normalizeDocument() {
        }

        public Node renameNode(final Node n, final String namespaceURI,
                final String qualifiedName) throws DOMException {
            return null;
        }

        public void setDocumentURI(final String documentURI) {
        }

        public void setStrictErrorChecking(final boolean strictErrorChecking) {
        }

        public void setXmlStandalone(final boolean xmlStandalone)
                throws DOMException {
        }

        public void setXmlVersion(final String xmlVersion) throws DOMException {
        }

        public Node appendChild(final Node newChild) throws DOMException {
            return null;
        }

        public Node cloneNode(final boolean deep) {
            return null;
        }

        public short compareDocumentPosition(final Node other)
                throws DOMException {
            return 0;
        }

        public NamedNodeMap getAttributes() {
            return null;
        }

        public String getBaseURI() {
            return null;
        }

        public NodeList getChildNodes() {
            return null;
        }

        public Object getFeature(final String feature, final String version) {
            return null;
        }

        public Node getFirstChild() {
            return null;
        }

        public Node getLastChild() {
            return null;
        }

        public String getLocalName() {
            return null;
        }

        public String getNamespaceURI() {
            return null;
        }

        public Node getNextSibling() {
            return null;
        }

        public String getNodeName() {
            return null;
        }

        public short getNodeType() {
            return 0;
        }

        public String getNodeValue() throws DOMException {
            return null;
        }

        public Document getOwnerDocument() {
            return null;
        }

        public Node getParentNode() {
            return null;
        }

        public String getPrefix() {
            return null;
        }

        public Node getPreviousSibling() {
            return null;
        }

        public String getTextContent() throws DOMException {
            return null;
        }

        public Object getUserData(final String key) {
            return null;
        }

        public boolean hasAttributes() {
            return false;
        }

        public boolean hasChildNodes() {
            return false;
        }

        public Node insertBefore(final Node newChild, final Node refChild)
                throws DOMException {
            return null;
        }

        public boolean isDefaultNamespace(final String namespaceURI) {
            return false;
        }

        public boolean isEqualNode(final Node arg) {
            return false;
        }

        public boolean isSameNode(final Node other) {
            return false;
        }

        public boolean isSupported(final String feature, final String version) {
            return false;
        }

        public String lookupNamespaceURI(final String prefix) {
            return null;
        }

        public String lookupPrefix(final String namespaceURI) {
            return null;
        }

        public void normalize() {
        }

        public Node removeChild(final Node oldChild) throws DOMException {
            return null;
        }

        public Node replaceChild(final Node newChild, final Node oldChild)
                throws DOMException {
            return null;
        }

        public void setNodeValue(final String nodeValue) throws DOMException {
        }

        public void setPrefix(final String prefix) throws DOMException {
        }

        public void setTextContent(final String textContent) throws DOMException {
        }

        public Object setUserData(final String key, final Object data,
                final UserDataHandler handler) {
            return null;
        }
    }

    private StcResourceProfile stc;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        stc =new StcResourceProfile();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        stc = null;
    }
    
    public void testToString() throws Exception {
        assertNotNull(stc.toString());
    }

    public void testEquals() throws Exception {
        assertEquals(stc,stc);
    }
    
    public void testIsAllSky() throws Exception {
        assertFalse(stc.isAllSky());
        stc.setAllSky(true);
        assertTrue(stc.isAllSky());
    }
    
    public void testDocument() throws Exception {
        final Document d = new DummyMockTestDocument();
        stc.setStcDocument(d);
        assertEquals(d,stc.getStcDocument());
    }
    
    
}
