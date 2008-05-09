package org.eurovotech.bibxml2rdf;

public abstract class BibContentHandler
        extends org.xml.sax.helpers.DefaultHandler {
    protected BibRecordHandler bibRecordHandler;

//     public enum Type {
//         ADS
//     };
    
//     BibContentHandler() {
//         // non-instantiable class -- constructor disabled
//     }
//     public static BibContentHandler newBibContentHandler(Type type, BibRecordHandler brh) {
//         BibContentHandler ch = null;

//         switch (type) {
//           case ADS:
//             ch = new ADSContentHandler();
//             ch.setBibRecordHandler(brh);
//             break;
//           default:
//             throw new AssertionError("Impossible BibContentHandlerType: " + type);
//         }
//         assert ch != null;
//         return ch;
//     }

    void setBibRecordHandler(BibRecordHandler brh) {
        bibRecordHandler = brh;
    }
}
