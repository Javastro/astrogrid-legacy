package org.eurovotech.bibxml2rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


/**
 * A simple record handler, which accumulates records and writes them out
 */
public class SimpleRecordHandler
        implements BibRecordHandler {

    private java.io.OutputStream out;
    private Model accumulatingModel;
    private int nrecords = 0;

    public SimpleRecordHandler(java.io.OutputStream out) {
        this.out = out;
        accumulatingModel = ModelFactory.createDefaultModel();
    }

    public void handleRecord(Model record) {
        accumulatingModel.add(record);
        nrecords++;
    }

    public void setNsPrefix(String prefix, String URI) {
        accumulatingModel.setNsPrefix(prefix, URI);
    }

    public void close() {
        // set some namespace prefixes, just for prettiness
        accumulatingModel.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
        accumulatingModel.setNsPrefix("dcterms", "http://purl.org/dc/terms/");

        // debugging statements
//         System.out.println("SimpleRecordHandler: statements:");
//         com.hp.hpl.jena.rdf.model.StmtIterator si = accumulatingModel.listStatements();
//         while (si.hasNext()) {
//             com.hp.hpl.jena.rdf.model.Statement s = (com.hp.hpl.jena.rdf.model.Statement)si.nextStatement();
//             System.out.println("Property object: " + s.getPredicate() + " -> " + s.getObject());
//         }

        System.err.println("writing model: "
                           + nrecords + " records, ~"
                           + accumulatingModel.size() + " triples");
        accumulatingModel.write(out);
    }
}
