package org.eurovotech.bibxml2rdf;

public interface BibRecordHandler {
    public void handleRecord(com.hp.hpl.jena.rdf.model.Model record);
    public void close();
}

