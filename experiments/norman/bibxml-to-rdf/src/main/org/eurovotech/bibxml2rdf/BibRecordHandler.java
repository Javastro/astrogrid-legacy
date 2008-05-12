package org.eurovotech.bibxml2rdf;

/**
 * Handles final processing of RDF models.  A record handler is given
 * a sequence of RDF Model instances, each one representing a single
 * record in the input, and its close method is finally called.  It
 * should do something with these models, such as accumulate them and
 * finally write them out.
 */

public interface BibRecordHandler {
    /**
     * Given an RDF model, do something with it
     * @param record the model to be processed
     */
    public void handleRecord(com.hp.hpl.jena.rdf.model.Model record);

    /**
     * Optionally declare an association between a prefix and a
     * namespace.  This might make some ultimate output more
     * readable.  An implementation may ignore this method.
     *
     * @param prefix the namespace prefix being mapped
     * @param namespace the URI the prefix is to be mapped to
     * @seealso com.hp.hpl.jena.rdf.model.Model#setNsPrefix
     */
    public void setNsPrefix(String prefix, String URI);

    /**
     * Finish processing the stream of records
     */
    public void close();
}

