package org.eurovotech.bibxml2rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class SimpleRecordHandler
        implements BibRecordHandler {

    private java.io.OutputStream out;
    private Model accumulatingModel;

    public SimpleRecordHandler(java.io.OutputStream out) {
        this.out = out;
        accumulatingModel = ModelFactory.createDefaultModel();
    }

    public void handleRecord(Model record) {
        accumulatingModel.add(record);
    }

    public void close() {
        accumulatingModel.write(out);
    }
}
