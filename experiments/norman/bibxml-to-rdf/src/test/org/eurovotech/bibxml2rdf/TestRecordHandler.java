package org.eurovotech.bibxml2rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TestRecordHandler
        implements org.eurovotech.bibxml2rdf.BibRecordHandler {
    private Model model;

    public TestRecordHandler() {
        model = ModelFactory.createDefaultModel();
    }

    public void handleRecord(Model record) {
        model.add(record);
    }

    public void setNsPrefix(String prefix, String URI) {
        // do nothing
    }

    public void close() {
        // do nothing
    }

    public Model getModel() {
        return model;
    }
}
