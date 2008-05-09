package org.eurovotech.bibxml2rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;


public class Util {

    /**
     * A non-string property value.  Each of these is able to represent itself as a Resource.
     */
    interface SubResource {
        /**
         * Produce a (anonymous or not) Resource which represents this object
         */
        Resource asResource();
    }

    static Reasoner createSchemaReasoner(java.io.InputStream schemaStream) {
        Model schemaModel = ModelFactory.createDefaultModel();
        schemaModel.read(schemaStream, "", "N3");
        Reasoner reasoner = com.hp.hpl.jena.reasoner.rulesys.RDFSRuleReasonerFactory
                .theInstance().create(null);
        reasoner.setParameter(com.hp.hpl.jena.vocabulary.ReasonerVocabulary.PROPsetRDFSLevel, 
                              com.hp.hpl.jena.vocabulary.ReasonerVocabulary.RDFS_SIMPLE);
        return reasoner.bindSchema(schemaModel);
    }

}
