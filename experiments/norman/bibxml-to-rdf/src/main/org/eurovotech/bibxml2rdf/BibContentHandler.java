package org.eurovotech.bibxml2rdf;

/**
 * Process an XML source, to help produce an RDF model.
 *
 * <p>This abstract class extends the SAX {@link org.xml.sax.helpers.DefaultHandler}
 * class, and so can be handed to a SAX parser.  It should be extended
 * to handle the SAX stream produced by a given input type, and use
 * the stream to populate an RDF model, as mediated by the
 * {@link BibRecordHandler} passed to it.
 *
 * <p>Classes which extend this one will probably not need to override
 * {@link #endDocument}, but if they do, they should be sure to call
 * <code>super</code> when doing so.
 */
public abstract class BibContentHandler
        extends org.xml.sax.helpers.DefaultHandler {
    protected BibRecordHandler bibRecordHandler;

    /**
     * Associate a BibRecordHandler with this content handler
     */
    void setBibRecordHandler(BibRecordHandler brh) {
        bibRecordHandler = brh;
    }

    public void endDocument() {
        bibRecordHandler.close();
    }
}
