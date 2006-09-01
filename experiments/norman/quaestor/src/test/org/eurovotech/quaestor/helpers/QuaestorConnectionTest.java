// JUnit test cases for Quaestor, checking the QuaestorConnection API
//
// This is more-or-less a variant of the QuaestorTest.java test harness,
// but it is intended to test the QuaestorConnection API, rather than
// Quaestor's functionality.  That is thoroughly tested by QuaestorTest.

// Control the Quaestor servlet which is tested by setting the quaestor.url
// property (which means setting -Dquaestor.url=http://.../quaestor/. 
// in the call to ant).

package org.eurovotech.quaestor.helpers;

import org.eurovotech.quaestor.helpers.QuaestorConnection;
import org.eurovotech.quaestor.QuaestorException;

import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;
import junit.framework.TestCase;

public class QuaestorConnectionTest
        extends TestCase {

    private static URL baseURL = null; // null indicates uninitialised
    private static final String testKB = "testing";

    public QuaestorConnectionTest(String name)
            throws Exception {
        super(name);

        if (baseURL == null) {
            // first time
            baseURL = new URL(System.getProperty
                              ("quaestor.url",
                               "http://localhost:8080/quaestor"));
        }
        
    }

    // The same actions, but this time using the QuaestorConnection API
    public void testCreateKnowledgebase()
            throws Exception {
        QuaestorConnection conn;

        // Attempt to delete the knowledgebase, whether or not it's there.
        try {
            conn = new QuaestorConnection(baseURL, testKB);
            conn.deleteKnowledgebase();
        } catch (QuaestorException e) {
            // no such knowledgebase -- that's OK
        }
        
        // create the new knowledgebase
        conn = QuaestorConnection
                .createKnowledgebase(baseURL,
                                     testKB,
                                     "My test knowledgebase");
        assertNotNull(conn);

        // Does ?metadata work?
        InputStream s = conn.getMetadata(QuaestorConnection.FORMAT_DEFAULT);
        assertNotNull(s);
        
        // try creating the knowledgebase again -- should fail, since
        // we can't create the knowledgebase twice
        try {
            QuaestorConnection c
                    = QuaestorConnection.createKnowledgebase(baseURL,
                                                             testKB,
                                                             "wibble");
            throw new junit.framework.AssertionFailedError
                    ("Recreating knowledgebase succeeded");
        } catch (QuaestorException e) {
            // that's correct
        }
    }

    public void testAddOntology()
            throws Exception {
        QuaestorConnection conn = new QuaestorConnection(baseURL, testKB);
        assertNotNull(conn);
        boolean rdfok = conn.putRDF("ontology",
                                    "<?xml version='1.0'?><rdf:RDF xmlns='urn:example#' xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' xmlns:owl='http://www.w3.org/2002/07/owl#' xml:base='urn:example'><owl:Ontology rdf:about=''/><owl:Class rdf:ID='c1'/></rdf:RDF>",
                                    true,
                                    QuaestorConnection.FORMAT_RDFXML);
        assertTrue(rdfok);
    }

    public void testGetOntology()
            throws Exception {
        QuaestorConnection conn = new QuaestorConnection(baseURL, testKB);
        InputStream s = conn.getModel("ontology");
        assertNotNull(s);
        consumeStream(s);
    }
    
    public void testAddInstances ()
            throws Exception {
        QuaestorConnection conn = new QuaestorConnection(baseURL, testKB);
        assertNotNull(conn);

        // Note that we must leave the 'instances' KB holding this triple,
        // for the following test to work.
        String content = "<urn:example#i2> a <urn:example#c2>.";
        InputStream stringInputStream
                = new java.io.ByteArrayInputStream(content.getBytes());
        boolean rdfok = conn.putRDF("instances",
                                    stringInputStream,
                                    false,
                                    QuaestorConnection.FORMAT_N3);
        assertTrue(rdfok);
    }

    public void testSparqlQueriesSelect()
            throws Exception {
        // note: this query depends on the previous test making it satisfiable
        String query = "SELECT ?i where { ?i a <urn:example#c1>}";

        QuaestorConnection conn = new QuaestorConnection(baseURL, testKB);
        assertNotNull(conn);

        InputStream s = conn.query(query);
        assertNotNull(s);
        consumeStream(s);

        s = conn.query(query, "text/plain");
        assertNotNull(s);
        consumeStream(s);
    }

    public void testSparqlQueriesMalformed()
            throws Exception {
        // following query has a missing '>'
        String badQuery =  "ASK { <urn:example#i1 a <urn:example#c1> }";

        QuaestorConnection conn = new QuaestorConnection(baseURL, testKB);
        assertNotNull(conn);

        try {
            InputStream s = conn.query(badQuery);
            throw new junit.framework.AssertionFailedError
                    ("Bad query succeeded");
        } catch (QuaestorException e) {
            // that's correct
        }
    }

    /* ******************** deletion of knowledgebases ******************** */

    public void testDeleteKnowledgebase()
            throws Exception {
        QuaestorConnection conn = new QuaestorConnection(baseURL, testKB);
        assertTrue(conn.deleteKnowledgebase());

        // should fail when done a second time
        try {
            conn = new QuaestorConnection(baseURL, testKB);
            throw new junit.framework.AssertionFailedError
                    ("Connected to deleted knowledgebase");
        } catch (QuaestorException e) {
            // that's correct
        }
    }

    private static void consumeStream(InputStream s) {
        try {
            byte[] b = new byte[1024];
            while (s.read(b) >= 0)
                ;
            s.close();
        } catch (java.io.IOException e) {
            // not much we can do
        }
    }

}
