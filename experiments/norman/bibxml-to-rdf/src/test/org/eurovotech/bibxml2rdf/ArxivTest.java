package org.eurovotech.bibxml2rdf;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Literal;

public class ArxivTest
        extends TestCase {

    private static final String FOAFns = "http://xmlns.com/foaf/0.1/";
    private static final String DCns ="http://purl.org/dc/terms/";
    private static final String ARXIVns = "http://arxiv.org/OAI/arXiv/#";

    public ArxivTest(String name) {
        super(name);
    }

    public void testConversionBasic()
            throws Exception {
        String filename = "org/eurovotech/bibxml2rdf/arxiv-basic.xml";

        BibContentHandler bch = new ArXivContentHandler();
        TestRecordHandler trh = new TestRecordHandler();
        bch.setBibRecordHandler(trh);

        // We need to be sure to parse the input XML file as UTF-8
        java.io.InputStreamReader fr
                = new java.io.InputStreamReader(ClassLoader.getSystemResourceAsStream(filename), "UTF8");

        // do the work
        boolean res = GrokXML.processXML(fr, bch);

        Model m = trh.getModel();
        //m.write(System.out);

        Resource record = m.createResource("http://arxiv.org/resources/astro-ph/1234567");
        Resource oShaughnessy = m.createResource("http://arxiv.org/resources/people/OShaughnessy%C3%98l%C3%A9Vlad");
        Resource conBeethoven = m.createResource("http://arxiv.org/resources/people/conBeethovenSeamus");
        Property creator = m.createProperty(DCns, "creator");
        assertTrue(m.contains(record, creator, oShaughnessy));
        assertTrue(m.contains(record, creator, conBeethoven));

        assertTrue(m.contains(oShaughnessy,
                              m.createProperty(FOAFns, "name"),        // O'Shaughnessy, Ølé Vlad
                              m.createLiteral("O'Shaughnessy, \u00d8l\u00e9 Vlad", false)));
        assertTrue(m.contains(oShaughnessy,
                              m.createProperty(FOAFns, "isMemberOf"),        // Université del ¡Ping!
                              m.createLiteral("Universit\u00e9 del \u00a1Ping!", false)));

        Property categories = m.createProperty(ARXIVns, "categories");
        assertTrue(m.contains(record,
                              categories,
                              m.createResource("http://arxiv.org/categories/astro-ph")));
        assertTrue(m.contains(record,
                              categories,
                              m.createResource("http://arxiv.org/categories/hep-th")));

    }
}
