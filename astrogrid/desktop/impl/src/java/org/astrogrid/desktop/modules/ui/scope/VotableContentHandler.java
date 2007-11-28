/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import org.apache.commons.lang.text.StrBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import uk.ac.starlink.votable.TableContentHandler;
import uk.ac.starlink.votable.TableHandler;

/** Extension of STIL's TableContentHandler that provides access to more of the
 * VOTABLE wrapper elements that enclose each table.
 * Provides access to content necessary for detecting all possible error reporting
 * methods in conesearch responses, and determining which table is the 'results' table
 * in a SIAP / SSAP response.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 28, 200712:02:18 PM
 */
public class VotableContentHandler extends TableContentHandler {
    
    /** extended handler interface that provides access to some of the votable wrapper elements too */
    public static interface VotableHandler extends TableHandler {
        /** called when an INFO element is encountered anywhere outside a RESOURCE */
        void info(String name,String value, String content) throws SAXException;
        /** called when a PARAM element is encountered anywhere outside a RESOURCE */
        void param(String name,String value, String description) throws SAXException;
        /** called when a RESOURCE is encountered */
        void resource(String name,String id, String type) throws SAXException;
    }
    public VotableContentHandler(boolean strict) {
        super(strict);
    }

    /** @deprecated prefer setVoTableHandler to take full advantage of the funtionalities of this subclass */
    public void setTableHandler(TableHandler handler) {
        super.setTableHandler(handler);
    }
    /** set the handler for this parse */
    public void setVotableHandler(VotableHandler handler) {
        this.handler = handler;
        super.setTableHandler(handler);
        
    }
    
    /** reference to our handler */
    private VotableHandler handler;
    /** indicates whether we're in an 'intersesting' INFO element */
    private boolean inInfo = false;
    /** indicates whether we're in an 'interesting' PARAM element */
    private boolean inParam = false;
    /** indicates whether content should be saved to the 'descritpion' field */
    private boolean saveContent = false;
    /** indicates whether we're currently inside a resource element.
     * int, rather than boolean, as resource elements can be nested.
     */   
    private int inResource = 0;

    /** used to store value of name Attribute in PARAM or INFO */
    private String nameAttribute;
    /** used to store value of Value attribute in PARAM or INFO */
    private String valueAttribute;
    /** used to store a description text content */
    private final StrBuilder description = new StrBuilder();

    public void startElement(String namespaceURI, String localName,
            String name, Attributes atts) throws SAXException {
        String tagName =getTagName(namespaceURI,localName,name);
        if (inResource == 0 && "INFO".equals(tagName)) {
            // store the name and value attributes
            inInfo = true;
            saveContent = true;
            nameAttribute = getAttribute(atts,"name");
            valueAttribute = getAttribute(atts,"value");
        } else if (inResource == 0 && "PARAM".equals(tagName)) {
            // store the name and value attributes
            inParam = true;
            nameAttribute = getAttribute(atts,"name");
            valueAttribute = getAttribute(atts,"value");
        } else if (inParam && "DESCRIPTION".equals(tagName)) {
            saveContent = true;
        } else if ("RESOURCE".equals(tagName)) {
            // note that we're in a resource, and so should ignore any more INFO or PARAM until out of again.
            inResource +=1;
            String resName = getAttribute(atts,"name");
            String id = getAttribute(atts,"ID");
            String type = getAttribute(atts,"type");
            if (type == null) {
                type = "results";
            }
            handler.resource(resName,id,type);
        }
        super.startElement(namespaceURI, localName, name, atts);
    }
    
    public void endElement(String namespaceURI, String localName, String name)
            throws SAXException {
        String tagName = getTagName(namespaceURI,localName,name);
        if ("RESOURCE".equals(tagName)) {
            inResource -= 1;
        } else if (inParam && "DESCRIPTION".equals(tagName)) {
            saveContent = false;
        } else if (inResource == 0 && "PARAM".equals(tagName)) {
            handler.param(nameAttribute,valueAttribute,description.toString());
            // clean up state
            inParam = false;            
            nameAttribute = null;
            valueAttribute = null;
            description.clear();
        } else if (inResource == 0 && "INFO".equals(tagName)) {
            handler.info(nameAttribute,valueAttribute,description.toString());
            inInfo = false;
            saveContent = false;
            nameAttribute = null;
            valueAttribute = null;
            description.clear();
        }
        super.endElement(namespaceURI, localName, name);
    }
    
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (saveContent) {
            description.append(ch,start,length);
        }
        super.characters(ch, start, length);
    }



    
    /**
     * Returns the value of an attribute.
     *
     * @param  atts  attribute set
     * @param  name  normal VOTable name of the attribute
     * @return  value of attribute <tt>name</tt> or null if it doesn't exist
     */
    private String getAttribute( Attributes atts, String name ) {
        String val = atts.getValue( name );
        return val != null ? val : atts.getValue( "", name );
    }
    
    /**
     * Returns the name of an element as a normal string like "TABLE" in
     * the VOTable namespace, given the various name items that
     * SAX provides for a start/end element event.
     *
     * @param  namespaceURI  namespaceURI
     * @param  localName   local name
     * @param  qName   qualified name
     */
    private String getTagName( String namespaceURI, String localName,
                                 String qName ) {
        if ( localName != null && localName.length() > 0 ) {
            return localName;
        }
        else {
            return qName;
        }
    }


}
