/*
 * Thrown-together program to extract all the namespaces declared in a
 * given XML file.  It includes a --generate-xslt option which
 * purportedly spits out useful XSLT, but that's a line I've
 * more-or-less abandoned.
 *
 * This isn't currently much use, but I'm not convinced it's useless
 * enough to throw away....
 */

import java.io.File;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import org.w3c.dom.*;

import org.xml.sax.ContentHandler;
//import org.xml.sax.XMLReader;

public class ExtractNamespaces {
    private static final int EXTRACT_NAMESPACES = 111;
    private static final int GENERATE_XSLT = 222;

    private static boolean verbose = false;

    private static java.util.Map<String,NSInfo> nsinfo;
    static {
        nsinfo = new java.util.HashMap<String,NSInfo>();
        nsinfo.put("http://www.ivoa.net/xml/VOResource/v0.10",
                   new NSInfo("rdf-VOResource-0.10.xslt",
                              "vor",
                              "voro",
                              "http://www.ivoa.net/xml/VOResource/v1.0#"));
        nsinfo.put("http://www.ivoa.net/xml/RegistryInterface/v0.1",
                   new NSInfo("rdf-RegistryInterface-0.1.xslt",
                              "ri",
                              "rio",
                              "urn:example.org/DUMMY"));
    }

    public static void main(String[] args) {
        if (args.length < 1)
            Usage();

        File infile = null;
        int action = EXTRACT_NAMESPACES;

        for (int i=0; i<args.length; i++) {
            String a = args[i];
            if (a.equals("--extract"))
                action = EXTRACT_NAMESPACES;
            else if (a.equals("--generate-xslt"))
                action = GENERATE_XSLT;
            else if (a.equals("--verbose"))
                verbose = true;
            else if (a.startsWith("--"))
                Usage();
            else if (infile != null)
                Usage();
            else
                infile = new File(a);
        }

        assert infile != null;

        try {
            // Extract the list of namespaces
            SAXParser saxparser = javax.xml.parsers.SAXParserFactory
                    .newInstance()
                    .newSAXParser();
            saxparser.setProperty
                    ("http://xml.org/sax/features/namespaces", true);
            saxparser.setProperty
                    ("http://xml.org/sax/features/namespace-prefixes", true);

            NamespaceHandler nsh = new NamespaceHandler();
            saxparser.parse(infile, nsh);

            if (action == EXTRACT_NAMESPACES) {
                for (String s : nsh.getNamespaces())
                    System.out.println(s);
            } else {
                assert action == GENERATE_XSLT;
                generateXSLT(nsh.getNamespaces(), System.out);
            }

        } catch (Exception e) {
            System.err.println("Exception: " + e);
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void generateXSLT(Set<String> namespaces,
                                     java.io.OutputStream stream)
            throws Exception {
        Document d = javax.xml.parsers.DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .newDocument();
        Element de = d.createElement("stylesheet");
        de.setAttribute("version", "1.0");
        de.setAttribute("xmlns", "http://www.w3.org/1999/XSL/Transform");
        de.setAttribute("xmlns:rdf",
                        "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        de.setAttribute("xmlns:rdfs",
                        "http://www.w3.org/2000/01/rdf-schema#");
        d.appendChild(de);

        // Import the fallback script, which matches "*".  If we did this
        // in the script itself, it would have a higher import precedence
        // than anything else, and so match absolutely everything.
        Element rootTemplate = d.createElement("template");
        rootTemplate.setAttribute("match", "/");

        Element rdfRDF = d.createElement("rdf:RDF");
        rootTemplate.appendChild(rdfRDF);
        rdfRDF.appendChild(d.createElement("apply-templates"));
        rdfRDF.setAttribute("xmlns:x2s",
                            "http://ns.eurovotech.org/registry-metadata");

        Element fallbackImport = d.createElement("import");
        fallbackImport.setAttribute("href", "fallback.xslt");
        de.appendChild(fallbackImport);



        for (String s : namespaces) {
            NSInfo h = nsinfo.get(s);
            if (h == null && verbose)
                System.err.println("Unmatched namespace: " + s);
            if (h != null) {
                Element importElement = d.createElement("import");
                importElement.setAttribute("href", h.getHandler());
                // declare namespaces here, so they'll appear on RDF element
                // in output
                de.setAttribute("xmlns:"+h.getPrefix(), s);
                rdfRDF.setAttribute("xmlns:"+h.getOntologyPrefix(),
                                    h.getOntologyURI());
                de.appendChild(importElement);
            }
        }

        de.appendChild(rootTemplate);

        // serialise it
        javax.xml.transform.Transformer t =
                javax.xml.transform.TransformerFactory
                .newInstance()
                .newTransformer();
        t.setOutputProperty("indent", "yes"); // marginally prettier
        t.transform(new javax.xml.transform.dom.DOMSource(d),
                    new javax.xml.transform.stream.StreamResult(stream));
    }

    private static void Usage() {
        System.err.println("Usage: ExtractNamespaces [--extract|--generate-xslt] file");
        System.exit(1);
    }

    private static class NamespaceHandler
            extends org.xml.sax.helpers.DefaultHandler {

        java.util.Set<String> namespaces;

        public NamespaceHandler() {
            namespaces = new java.util.TreeSet<String>();
        }

        public Set<String> getNamespaces() {
            return namespaces;
        }

        public void startPrefixMapping(String prefix, String uri) {
            if (verbose)
                System.err.println("Mapping " + prefix + " -> " + uri);
            namespaces.add(uri);
        }
    }

    private static class NSInfo {
        String handler;
        String xsdprefix;
        String ontologyprefix;
        String ontologyURI;
        
        public NSInfo(String handler,
                      String xsdprefix,
                      String ontologyprefix,
                      String ontologyURI) {
            this.handler = handler;
            this.xsdprefix = xsdprefix;
            this.ontologyprefix = ontologyprefix;
            this.ontologyURI = ontologyURI;
        }
        public String getHandler() {
            return handler;
        }
        public String getPrefix() {
            return xsdprefix;
        }
        public String getOntologyPrefix() {
            return ontologyprefix;
        }
        public String getOntologyURI() {
            return ontologyURI;
        }
    }
}
