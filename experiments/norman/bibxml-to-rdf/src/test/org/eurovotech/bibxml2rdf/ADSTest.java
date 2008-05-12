package org.eurovotech.bibxml2rdf;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Literal;

public class ADSTest
        extends TestCase {

    private static final String FOAFns = "http://xmlns.com/foaf/0.1/";
    private static final String DCns ="http://purl.org/dc/terms/";
    private static final String ADSns = "http://ads.harvard.edu/schema/abs/1.0/abstracts#";

    public ADSTest(String name) {
        super(name);
    }

    public void testConversionBasic()
            throws Exception {
        String filename = "org/eurovotech/bibxml2rdf/ads-basic.xml";

        BibContentHandler bch = new ADSContentHandler();
        TestRecordHandler trh = new TestRecordHandler();
        bch.setBibRecordHandler(trh);

        // We need to be sure to parse the input XML file as UTF-8
        java.io.InputStreamReader fr
                = new java.io.InputStreamReader(ClassLoader.getSystemResourceAsStream(filename), "UTF8");

        // do the work
        boolean res = GrokXML.processXML(fr, bch);

        Model m = trh.getModel();
        //m.write(System.out);

        Resource record = m.createResource("http://ads.harvard.edu/resource/2008TMP...154..319L");
        Resource semenov = m.createResource("http://ads.harvard.edu/resource/people/S%C3%A9menovAG"); // Sémenov
        Resource lozovik = m.createResource("http://ads.harvard.edu/resource/people/LozovikYuE");
        Resource other = m.createResource("http://ads.harvard.edu/resource/people/OtherAN");
        
        Property creator = m.createProperty(DCns, "creator");
        assertTrue(m.contains(record, creator, semenov));
        assertTrue(m.contains(record, creator, lozovik));
        assertTrue(m.contains(record, creator, other));

        assertTrue(m.contains(semenov,
                              m.createProperty(FOAFns, "name"),        // A. G. Sémenov
                              m.createLiteral("A. G. S\u00e9menov")));

        Property isMemberOf = m.createProperty(FOAFns, "isMemberOf");
        assertTrue(m.contains(lozovik,
                              isMemberOf,
                              m.createLiteral("Institute of Spectroscopy, RAS", false)));
        assertFalse(m.contains(semenov, isMemberOf));
        assertFalse(m.contains(other, isMemberOf));

    }
}
