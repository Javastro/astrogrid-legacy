// JUnit test cases for Quaestor

// Control the Quaestor servlet which is tested by setting the quaestor.url
// property (which means setting -Dquaestor.url=http://.../quaestor/. 
// in the call to ant).
//
// Note that the tests below are NOT independent!  Various of the
// tests depend on the state generated in preceding tests.  This is
// certainly unfortunate, not least because I don't think that the
// order the tests are run in is actually defined to be the same as
// their order in the file.  However it seems to work OK for the
// moment, so there isn't a pressing need to change it.

package org.eurovotech.quaestor;

import org.eurovotech.quaestor.helpers.QuaestorConnection;
import org.eurovotech.quaestor.helpers.HttpResult;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class QuaestorTest
        extends TestCase {

    private static URL quaestorURL = null; // null indicates uninitialised
    private static URL contextURL;     // Same as quaestorURL, but usable
                                       // as a base URL for creating
                                       // new ones
    private static URL xmlrpcEndpoint;
    private static final String testKB = "testing";

    private static final String quaestorNamespace = "http://ns.eurovotech.org/quaestor#";

    private static final String sparqlSelectC1 = "SELECT ?i where { ?i a <urn:example#c1> }";
    String[] sparqlQueryHeaders = new String[] {
        "Accept", "text/csv",
        "Content-Type", "application/sparql-query", 
    };
                                                   

    public QuaestorTest(String name)
            throws Exception {
        super(name);

        if (quaestorURL == null) {
            // first time
            quaestorURL = new URL(System.getProperty("quaestor.url",
                                                     "http://localhost:8080/quaestor"));

            String normalisedPath = quaestorURL.getPath(); // make sure there's a / at the end of the path
            if (! normalisedPath.endsWith("/"))
                normalisedPath = normalisedPath + "/";

            contextURL = new URL(quaestorURL,  normalisedPath);
            xmlrpcEndpoint = new URL(contextURL, "xmlrpc");
            System.err.println("Testing quaestorURL=" + quaestorURL);
            System.err.println("    ...RPC endpoint=" + xmlrpcEndpoint);

            // Get the Quaestor front page -- this effectively checks that the
            // Tomcat server is running.  Of course, this would show up pretty
            // promptly below, but this gives us a chance to produce a
            // more helpful error.
            HttpResult r = QuaestorConnection.httpGet(quaestorURL);
            if (r.getStatus() == HttpURLConnection.HTTP_OK) {
                assertContentType(r, "text/html");
            } else {
                // Tomcat appears not to be running.
                // Print an explanatory message and stop the test.
                System.err.println
                        ("Can't get Quaestor top page: is Tomcat running?");
                throw new AssertionFailedError("Tomcat not running");
            }

            // Attempt to delete the knowledgebase, whether or not it's there.
            deleteKbIfPresent(makeKbUrl());
        }
    }

    public void testGetKnowledgebaseList()
            throws Exception {
        HttpResult r = QuaestorConnection.httpGet(new URL(contextURL, "kb"));
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/html");
        assertNotNull(r.getContentAsString());
    }

    public void testURLvariants()
            throws Exception {
        // Paths with extra slashes in them
        String qProtocol = quaestorURL.getProtocol();
        String qHost = quaestorURL.getHost();
        int qPort = quaestorURL.getPort();
        String qPath = quaestorURL.getPath();
        HttpResult r = QuaestorConnection.httpGet(new URL(qProtocol, qHost, qPort, "//" + qPath + "////kb"));
        // should have the same results as the previous test
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/html");
        assertNotNull(r.getContentAsString());
    }

    public void testCreateKnowledgebase()
            throws Exception {
        HttpResult r;

        // create the new knowledgebase
        r = QuaestorConnection.httpPut(makeKbUrl(),
                                       "My test knowledgebase",
                                       "text/plain");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        assertNull(r.getContentAsString());

        // Does ?metadata work?
        r = QuaestorConnection.httpGet(makeKbUrl("?metadata"));
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/rdf+n3");

        // try creating the knowledgebase again -- should fail, since
        // we can't create the knowledgebase twice
        r = QuaestorConnection.httpPut(makeKbUrl(),
                               "My test knowledgebase",
                               "text/plain");
        assertStatus(r, HttpURLConnection.HTTP_FORBIDDEN);
    }

    // as before, but this time uploading metadata
    public void testCreateKnowledgebaseWithMetadata()
            throws Exception {
        HttpResult r = null;
        String kbName = "kb/metadataTest";
        URL testURL = new URL(contextURL, kbName);
        String mdString =
                "@prefix dc: <http://purl.org/dc/elements/1.1/>. @prefix quaestor: <"
                + quaestorNamespace
                + ">. <> dc:description \"My metadata-started knowledgebase\"; dc:creator \"Norman\"; quaestor:requiredReasoner [ quaestor:level \"simpleRDFS\" ].";

        // Try this for alternative MIME types
        String[] mimeTypes = { "text/rdf+n3", "text/turtle" };
        
        try {
            for (String mime : mimeTypes) {

                // create the new knowledgebase
                r = QuaestorConnection.httpPut(testURL, mdString, mime);
                assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
                assertNull(r.getContentAsString());

                // Does ?metadata work?
                r = QuaestorConnection.httpGet(new URL(contextURL, kbName + "?metadata"));
                assertStatus(r, HttpURLConnection.HTTP_OK);
                assertContentType(r, "text/rdf+n3");

                // try creating the knowledgebase again -- should fail, since
                // we can't create the knowledgebase twice
                r = QuaestorConnection.httpPut(testURL, mdString, mime);
                assertStatus(r, HttpURLConnection.HTTP_FORBIDDEN);
            
                r = QuaestorConnection.httpGet(new URL(contextURL, "kb"));
                assertStatus(r, HttpURLConnection.HTTP_OK);
                // the following test is slightly fragile, as it depends on the detail of the
                // Jena serialisation of the metadata model
                assertTrue(r.getContentAsString().contains("dc:creator \"Norman\""));
                assertContentType(r, "text/html");

                // ...and delete it
                r = QuaestorConnection.httpDelete(testURL);
                assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
            }
        } catch (AssertionFailedError e) {
            System.err.println("testCreateKnowledgebaseWithMetadata: r=" + r);
            throw e;
        }
    }


    /* ******************** Other test methods ******************** */

    public void testQueryEmptyKB()
            throws Exception {
        // Query the empty KB.  This should produce no results, but shouldn't fail
        HttpResult r = QuaestorConnection.httpPost(makeKbUrl(), sparqlSelectC1, sparqlQueryHeaders);
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/csv");
        assertEquals(new String[] { "i" }, r.getContentAsStringList());
    }

    public void testAddOntology()
            throws Exception {
        HttpResult r = QuaestorConnection.httpPut
                (makeKbUrl("ontology"),
                 "<?xml version='1.0'?><rdf:RDF xmlns='urn:example#' xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' xmlns:owl='http://www.w3.org/2002/07/owl#' xml:base='urn:example'><owl:Ontology rdf:about=''/><owl:Class rdf:ID='c1'/></rdf:RDF>",
                 "application/rdf+xml");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        assertNull(r.getContentAsString());
    }

    public void testGetOntology ()
            throws Exception {
        HttpResult r = QuaestorConnection.httpGet(makeKbUrl("ontology"), "*/*");
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/rdf+n3");
        assertNotNull(r.getContentAsString());
        // the actual content is a bit of a fuss to check

        // check handling of Accept headers
        String[] mimeTypes = { "text/rdf+n3", "application/n3", "text/turtle" };
        for (String mime : mimeTypes) {

            r = QuaestorConnection.httpGet(makeKbUrl("ontology"), mime);
            assertStatus(r, HttpURLConnection.HTTP_OK);
            assertContentType(r, mime);
            assertNotNull(r.getContentAsString());
        }
    }
    
    public void testAddInstances ()
            throws Exception {
        HttpResult r = QuaestorConnection.httpPut(makeKbUrl("instances"),
                                                  "<urn:example#i2> a <urn:example#c1>.",
                                                  "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        assertNull(r.getContentAsString());

        r = QuaestorConnection.httpPost(makeKbUrl(), sparqlSelectC1, sparqlQueryHeaders);
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/csv");
        assertEquals(new String[] { "i", "urn:example#i2" }, r.getContentAsStringList());

        // Test the deprecated, but admissable, application/n3 MIME type.
        // Note that we must leave the 'instances' KB holding this triple,
        // for the following test to work.
        r = QuaestorConnection.httpPut(makeKbUrl("instances"),
                                       "<urn:example#i1> a <urn:example#c1>.",
                                       "application/n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        assertNull(r.getContentAsString());

        // Now do the query again -- the above RDF (describing <#i1>) should have
        // replaced what was there before
        r = QuaestorConnection.httpPost(makeKbUrl(), sparqlSelectC1, sparqlQueryHeaders);
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/csv");
        assertEquals(new String[] { "i", "urn:example#i1" }, r.getContentAsStringList());
    }

    public void testAddSubmodelMetadata ()
            throws Exception {
        URL instancesURL = makeKbUrl("instances");
        URL instancesMetadataURL = makeKbUrl("instances?metadata");
        
        HttpResult r = QuaestorConnection.httpPut(instancesMetadataURL,
                                                  "<> <http://purl.org/dc/elements/1.1/description> \"Instance submodel metadata\".",
                                                  "text/turtle");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);

        // and retrieve it
        r = QuaestorConnection.httpGet(instancesMetadataURL, "text/turtle");
        // remove all spaces in both strings below, to avoid spurious mismatches
        assertEquals("<" + instancesURL + "><http://purl.org/dc/elements/1.1/description>\"Instancesubmodelmetadata\".", 
                     r.getContentAsString().replaceAll("\\s+", ""));
    }

    public void testAppendRdf ()
            throws Exception {
        HttpResult r;

        r = QuaestorConnection.httpGet(makeKbUrl("instances"), "text/turtle");
        assertStatus(r, HttpURLConnection.HTTP_OK);
        String currentContents = r.getContentAsString();

        // POST RDF to the submodel -- should append
        r = QuaestorConnection.httpPost(makeKbUrl("instances"),
                                        "<urn:example#i3> a <urn:example#c1>.",
                                        "text/turtle");
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/html");

        // The following should fail:

        // append with a bad MIME-type
        r = QuaestorConnection.httpPost(makeKbUrl("instances"),
                                        "<urn:example#i4> a <urn:example#c1>.",
                                        "text/wibble");
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);
        assertContentType(r, "text/html");

        // append to a non-existent submodel
        r = QuaestorConnection.httpPost(makeKbUrl("bogus-instances"),
                                        "<urn:example#i5> a <urn:example#c1>.",
                                        "text/turtle");
        assertStatus(r, HttpURLConnection.HTTP_NOT_FOUND);
        assertContentType(r, "text/html");

        // now append malformed RDF
        r = QuaestorConnection.httpPost(makeKbUrl("instances"),
                                        "This is not RDF",
                                        "text/turtle");
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);

        // query again -- the successful POST above, and nothing else,
        // should have appended to what's there
        r = QuaestorConnection.httpPost(makeKbUrl(), sparqlSelectC1, sparqlQueryHeaders);
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/csv");
        assertEquals(new String[] { "i", "urn:example#i3", "urn:example#i1" }, r.getContentAsStringList());

        // ...and replace what was initially there
        r = QuaestorConnection.httpPut(makeKbUrl("instances"), currentContents, "text/turtle");
    }


    /* ******************** SPARQL tests ******************** */

    public void testSparqlQueriesSelect() 
            throws Exception {
        // note: the sparqlSelectC1 query depends on the previous test making it satisfiable
        HttpResult r;

        // Make a successful query. and check XML response
        r = QuaestorConnection.httpPost(makeKbUrl(), sparqlSelectC1, "application/sparql-query");
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "application/sparql-results+xml");
        assertNotNull(r.getContentAsString());
        // don't check actual content string -- it's a bunch of XML

        // try making a query against a submodel -- should fail
        r = QuaestorConnection.httpPost(makeKbUrl("ontology"), sparqlSelectC1, "application/sparql-query");
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);
        assertNotNull(r.getContentAsString()); // error message

        // POST a query with the wrong content-type
        r = QuaestorConnection.httpPost(makeKbUrl(), sparqlSelectC1, "wibble/w00t");
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);
        assertNotNull(r.getContentAsString()); // error message

        // check n-triple response
        // XXX this isn't actually correct yet, in that this still
        // returns a tabular-style query rather than n-triples.
        r = QuaestorConnection.httpPost(makeKbUrl(),
                                        sparqlSelectC1,
                                        new String[] {
                                            "Accept", "text/plain",
                                            "Content-Type", "application/sparql-query",
                                        });
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/plain");
        assertNotNull(r.getContentAsString());
        // don't check actual content

        // Query through the GET interface
        String encodedQuery = java.net.URLEncoder.encode(sparqlSelectC1, "UTF-8");
        r = QuaestorConnection.httpGet(makeKbUrl("?query="+encodedQuery));
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "application/sparql-results+xml");
        assertNotNull(r.getContentAsString());

        // the same, requesting text/plain
        r = QuaestorConnection.httpGet(makeKbUrl("?query="+encodedQuery),
                    new String[] {"Accept", "text/plain"});
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/plain");
        assertNotNull(r.getContentAsString());

        // ...and requesting CSV
        // Note that we actually test the content, here, so are coupled to
        // testAddInstances.
        r = QuaestorConnection.httpGet(makeKbUrl("?query="+encodedQuery),
                    new String[] {"Accept", "text/csv"});
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/csv");
        assertEquals("header=present", r.getContentTypeParameters());
        assertEquals(new String[] { "i", "urn:example#i1" },
                     r.getContentAsStringList());
    }

    public void testSparqlQueriesAsk()
            throws Exception {
        String query = "ASK { <urn:example#i1> a <urn:example#c1> }";
        HttpResult r;
        URL url = makeKbUrl();

        r = QuaestorConnection.httpPost(url, query, "application/sparql-query");
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "application/sparql-results+xml");
        String[] correctResponses = {
            "<?xml version=\"1.0\"?>",
            "<sparql xmlns=\"http://www.w3.org/2005/sparql-results#\">",
            "  <head>",
            "  </head>",
            "  <boolean>true</boolean>",
            "</sparql>",
        };

        // Request plain text -- should return yes/no
        r = QuaestorConnection.httpPost(url,
                                        query,
                                        new String[] {
                                            "Content-Type", "application/sparql-query",
                                            "Accept", "text/plain",
                                        });
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/plain");
        assertEquals(new String[] { "yes" }, r.getContentAsStringList());

        // Request CSV -- should fail
        r = QuaestorConnection.httpPost(url, query,
                                        new String[] {
                                            "Content-Type", "application/sparql-query",
                                            "Accept", "text/csv",
                                        });
        assertEquals(4, r.getStatus()/100);
        assertNotNull(r.getContentAsString()); // error message
    }

    public void testSparqlQueriesMalformed()
            throws Exception {
        // good query
        String goodQuery = "ASK { <urn:example#i1> a <urn:example#c1> }";
        // following query has a missing '>'
        String badQuery =  "ASK { <urn:example#i1 a <urn:example#c1> }";
        HttpResult r;
        URL url = makeKbUrl();

        // good query with (otherwise plausible) post query part
        r = QuaestorConnection.httpPost(makeKbUrl("?sparql"),
                     goodQuery,
                     "application/sparql-query");
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);
        assertNotNull(r.getContentAsString());

        r = QuaestorConnection.httpPost(url, badQuery, "application/sparql-query");
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);
        assertNotNull(r.getContentAsString());

        // GET with unrecognised query string
        r = QuaestorConnection.httpGet(makeKbUrl("?wibble"));
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);
        assertNotNull(r.getContentAsString());

        // GET with correct query string, but no argument
        r = QuaestorConnection.httpGet(makeKbUrl("?sparql="));
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);
        assertNotNull(r.getContentAsString());
    }


    /* ******************** Delegation tests ******************** */

    public void testLocalDelegation()
            throws Exception {
        HttpResult r;
        URL kb_d1 = new URL(contextURL, "kb/d1");
        URL kb_d2 = new URL(contextURL, "kb/d2");
        URL kb_d3 = new URL(contextURL, "kb/d3");
        String baseMDprefix = "@prefix dc: <http://purl.org/dc/elements/1.1/>.\n@prefix quaestor: <http://ns.eurovotech.org/quaestor#>.\n\n<> dc:description \"UCD knowledgebase\";\n    dc:creator \"Norman\";\n    quaestor:requiredReasoner [\n        quaestor:level \"none\"\n    ]";
        
        String baseMDextraList[] = {
            "",
            "; quaestor:debug \"externaldelegation\"",
        };

        for (String baseMDextra : baseMDextraList) {
            // the network of delegation is as follows:
            // d3 ---> d1 ---> testing
            //    |
            //    +--> d2
            String baseMD = baseMDprefix + baseMDextra;

        deleteKbIfPresent(kb_d1);
        deleteKbIfPresent(kb_d2);
        deleteKbIfPresent(kb_d3);

        r = QuaestorConnection.httpPut(kb_d1,
                                       baseMD+";  quaestor:delegatesTo <" + makeKbUrl() + ">.",
                                       "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        r = QuaestorConnection.httpPut(kb_d2,
                                       baseMD+".",
                                       "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        r = QuaestorConnection.httpPut(kb_d3,
                                       baseMD+";  quaestor:delegatesTo <"+kb_d1+">, <"+kb_d2+">.",
                                       "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        r = QuaestorConnection.httpPut(new URL(kb_d1.toString()+"/content"),
                                       "<urn:del#d1> a <urn:example#c1>.",
                                       "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        r = QuaestorConnection.httpPut(new URL(kb_d2.toString()+"/content"),
                                       "<urn:del#d2> a <urn:example#c1>.",
                                       "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        r = QuaestorConnection.httpPut(new URL(kb_d3.toString()+"/content"),
                                       "<urn:del#d3> a <urn:example#c1>.",
                                       "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);

        //String query = "SELECT ?i where { ?i a <urn:example#c1>}";

        // Make a successful query. and check XML response
        r = QuaestorConnection.httpPost(kb_d3, sparqlSelectC1, sparqlQueryHeaders);
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/csv");
        // the order of the following isn't important, but this seems to be consistent
        assertEquals(new String[] {"i",
                                   "urn:del#d3",
                                   "urn:del#d2",
                                   "urn:del#d1",
                                   "urn:example#i1" },
                     r.getContentAsStringList());
        }

        // tidy up
        deleteKbIfPresent(kb_d1);
        deleteKbIfPresent(kb_d2);
        deleteKbIfPresent(kb_d3);
    }

    /* ******************** /code tests ******************** */
    /* These tests don't examine documented functionality, but
     * it's functionality I've broken before, and it's reassuring to
     * confirm that it's still working. These are dependent on the /code
     * functionality being enabled, and so should probably have slightly different setup.
     * Worry about when I need to.
     */
    public void testCode()
            throws Exception {

        URL codeURL = new URL(contextURL, "code");
        HttpResult r = QuaestorConnection.httpPost(codeURL, "(ident)", "text/plain");
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/plain");
        assertNotNull(r.getContentAsString());
    }

/* ******************** XML-RPC tests ******************** */

    public void testRpcGetSimple()
            throws Exception {
        String call = makeXmlRpcCall
                ("get-model",
                 new Object[] {testKB},
                 new String[] {
                     "omit-xml-declaration", "yes",
                     "indent", "yes",
                     "standalone", "yes",
                     "encoding", "UTF-8",
                 });
        ParsedRpcResponse rpc;
        URL kbbaseurl = makeKbUrl();
        rpc = getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint, call, "text/xml"));
        assertTrue(rpc.isValid() && !rpc.isFault());
        assertEquals(String.class, rpc.getResponseClass());
        assertEquivalentURLs(kbbaseurl, rpc.getResponseString());

        // Exactly the same, but with different properties
        call = makeXmlRpcCall("get-model",
                              new Object[] {testKB},
                              new String[] {
                                  "omit-xml-declaration", "no",
                                  "indent", "no",
                                  "standalone", "no",
                                  "encoding", "ISO-8859-1",
                              });
        rpc = getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint, call, "text/xml"));
        assertTrue(rpc.isValid() && !rpc.isFault());
        assertEquals(String.class, rpc.getResponseClass());
        assertEquivalentURLs(kbbaseurl, rpc.getResponseString());
    }

    public void testRpcQuery()
            throws Exception {
        String askQuery = "ASK { <urn:example#i1> a <urn:example#c1>}";
        String call = makeXmlRpcCall("query-model", new Object[] {testKB, sparqlSelectC1});
        ParsedRpcResponse rpc
                = getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint, call, "text/xml"));
        assertTrue(rpc.isValid() && !rpc.isFault());
        assertEquals(String.class, rpc.getResponseClass());
        assertNotNull(rpc.getResponseString());

        URL pickup = new URL(rpc.getResponseString());
        HttpResult r = QuaestorConnection.httpGet(pickup);
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "application/sparql-results+xml");
        assertNotNull(r.getContentAsString());

        // getting it a second time should fail
        r = QuaestorConnection.httpGet(pickup);
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);

        // test requesting different MIME type
        rpc = performXmlRpcCall("query-model",
                                new Object[] {testKB, sparqlSelectC1, "text/plain"});
        assertTrue(rpc.isValid() && !rpc.isFault());
        assertEquals(String.class, rpc.getResponseClass());
        assertNotNull(rpc.getResponseString());
        pickup = new URL(rpc.getResponseString());
        r = QuaestorConnection.httpGet(pickup);
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/plain");
        assertNotNull(r.getContentAsString());

        // test requesting bad MIME type
        rpc = performXmlRpcCall("query-model",
                                new Object[] {testKB, sparqlSelectC1, "wibble/woot"});
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(20, rpc.getFaultCode());

        // request OK mime type for SELECT, bad for ASK
        rpc = performXmlRpcCall("query-model",
                                new Object[] {testKB, sparqlSelectC1, "text/csv"});
        assertTrue(rpc.isValid() && !rpc.isFault());
        assertEquals(String.class, rpc.getResponseClass());
        assertNotNull(rpc.getResponseString());
        pickup = new URL(rpc.getResponseString());
        r = QuaestorConnection.httpGet(pickup);
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/csv");
        assertEquals(new String[] {"i", "urn:example#i1"}, r.getContentAsStringList());

        // ...but bad for ASK
        rpc = performXmlRpcCall("query-model",
                                new Object[] {testKB, askQuery, "text/csv"});
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(20, rpc.getFaultCode());
    }

    // test a miscellaneous variety of bad calls (various specific
    // bad calls are tested above)
    public void testRpcInvalidCalls()
            throws Exception {
        String call;
        //HttpResult r;
        ParsedRpcResponse rpc;

        // malformed input: ill-formed XML
        // I'm taking it that a proper response to malformed input is still
        // a 200 response with the correct fault-code, even though the `spec'
        // is hopelessly vague about this.
        call = "<junk";
        rpc = getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint, call, "text/xml"));
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(0, rpc.getFaultCode());
        // don't bother testing fault message

        // malformed input (2): well-formed but non-conforming XML
        call = "<junk><but>well-formed</but></junk>";
        rpc = getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint, call, "text/xml"));
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(0, rpc.getFaultCode());

        // invalid method
        rpc = performXmlRpcCall("wibble-woot", null);
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(1, rpc.getFaultCode());

        // OK method, but wrong content-type and wrong number of args
        call = makeXmlRpcCall("get-model",
                              new Object[] {"one", "two", "three"});
        rpc = getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint, call, "text/plain"));
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(0, rpc.getFaultCode());

        // ...wrong number of arguments (same call)
        rpc = getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint, call, "text/xml"));
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(2, rpc.getFaultCode());

        // good method and number of arguments, but wrong type
        rpc = performXmlRpcCall("get-model",
                                new Object[] {Boolean.TRUE});
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(2, rpc.getFaultCode());

        // good method and arguments, but non-existent knowledgebase
        rpc = performXmlRpcCall("get-model",
                                new Object[] {"wibble"});
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(10, rpc.getFaultCode());

        // good method, but wrong number of arguments (this one is checked
        // by the xmlrpc-handler harness, rather than the handler itself, so
        // is not redundant with the test above).
        call = makeXmlRpcCall("query-model",
                              new Object[] {"wibble"}); // should be two args
        rpc = getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint, call, "text/xml"));
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(2, rpc.getFaultCode());

        // query non-existent knowledgebase
        rpc = performXmlRpcCall("query-model",
                                new Object[] {"wibble", "dummy-query"});
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(10, rpc.getFaultCode());

        // query knowledgebase with bad query
        String badQuery = "SELECT ?i where { ?i a <urn:example#c1}";
        rpc = performXmlRpcCall("query-model",
                                new Object[] {testKB, badQuery});
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(20, rpc.getFaultCode());
    }

    /** 
     * Helper method to check that the result of the XML-RPC call is correct.
     * Returns the parsed RPC response, after checking with assertions that
     * the HttpResult was indeed status 200 and text/xml.
     * @param r the response from the HTTP interaction
     * @return a parsed RPC response
     */
    public ParsedRpcResponse getRpcResponse(HttpResult r) 
            throws Exception {
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/xml");
        return parseXmlRpcResponse(r.getContentAsString());
    }

    /* ******************** Persistence tests ******************** */

    /* This is slightly difficult to test.
     *
     * It'd be nice to test the SDB support, but that really requires
     * an external SDB database to be set up (using MySQL, PostgreSQL,
     * or whatever), which is complicated.  As well, that support is
     * arguably slightly peripheral.
     *
     * Testing TDB is easier, because the store is self-contained
     * within Java, but it still requires some configuration of the
     * Quaestor server.  Thus the tests below have to be skipped if
     * that support isn't configured in.
     */
    public void testTDBKnowledgebase()
            throws Exception {
        HttpResult r;
        URL tdbKb = new URL(contextURL, "kb/test-tdbkb/");
        URL transientSubmodel = new URL(tdbKb, "transient");
        URL persistentSubmodel = new URL(tdbKb, "persistent");
        URL persistentSubmodelMetadata = new URL(tdbKb, "persistent?metadata");

        // delete the KB first, in case it's hanging on from a
        // previous run
        deleteKbIfPresent(tdbKb);

        r = QuaestorConnection.httpPut(tdbKb, "TDB knowledgebase", "text/plain");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);

        // now add a transient submodel
        r = QuaestorConnection.httpPut(transientSubmodel,
                                       "<urn:example#t1> a <urn:example#c1>.",
                                       "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);

        // ...and a persistent one
        r = QuaestorConnection.httpPut(persistentSubmodelMetadata,
                                       "<> a <" + quaestorNamespace + "PersistentModel>; <http://purl.org/dc/elements/1.1/description> \"My description\".",
                                       "text/turtle");
        // if this doesn't work, we assume that it's because the TDB
        // support isn't available

        boolean doTests;
        if (r.getStatus() == 204) {
            // persistent submodel created
            doTests = true;
        } else if (r.getStatus() == 501) {
            // persistence is not implemented in this Quaestor
            System.err.println("No persistence implemented in this Quaestor; response=" + r);
            doTests = false;
        } else {
            throw new AssertionFailedError("Unexpected response " + r.getStatus() 
                                           + " when creating persistent submodel (response=" + r + ")");
        }

        if (doTests) {
            // set the persistent submodel's contents to have one
            // statement in it (don't POST, since that might include
            // the results of the last time this test suite was run)
            r = QuaestorConnection.httpPut(persistentSubmodel,
                                           "<urn:example#p1> a <urn:example#c1>.",
                                           "text/turtle");
            assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);

            // check that was added OK
            r = QuaestorConnection.httpPost(tdbKb, sparqlSelectC1, sparqlQueryHeaders);
            assertStatus(r, HttpURLConnection.HTTP_OK);
            assertContentType(r, "text/csv");
            String[] resultStringList = r.getContentAsStringList();
            java.util.Arrays.sort(resultStringList);
            assertEquals(new String[] { "i", "urn:example#p1", "urn:example#t1"}, resultStringList);

            // now delete the knowledgebase
            r = QuaestorConnection.httpDelete(persistentSubmodel);
            assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
            r = QuaestorConnection.httpDelete(tdbKb);
            assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);

            // Recreate the KB, recreate the persistent submodel, add
            // new RDF, and query again.  We should find the new RDF,
            // the old stuff in the persistent model, but not the
            // stuff in the transient model.
            r = QuaestorConnection.httpPut(tdbKb, "TDB knowledgebase again", "text/plain");
            assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);

            r = QuaestorConnection.httpPut(persistentSubmodelMetadata,
                                           "<> a <" + quaestorNamespace + "PersistentModel>.",
                                           "text/turtle");

            // add another statement to the model
            r = QuaestorConnection.httpPost(persistentSubmodel,
                                            "<urn:example#p2> a <urn:example#c1>.",
                                            "text/turtle");
            assertStatus(r, HttpURLConnection.HTTP_OK);

            r = QuaestorConnection.httpPost(tdbKb, sparqlSelectC1, sparqlQueryHeaders);
            assertStatus(r, HttpURLConnection.HTTP_OK);
            assertContentType(r, "text/csv");
            resultStringList = r.getContentAsStringList();
            java.util.Arrays.sort(resultStringList);
            // the order is not mandated here, but this happens to be correct
            assertEquals(new String[] { "i", "urn:example#p1", "urn:example#p2", }, resultStringList);

            // tidy up by deleting the submodel and the model
            // (though this won't actually get rid of the persistence
            // structures on disk)
            r = QuaestorConnection.httpDelete(persistentSubmodel);
            assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
            r = QuaestorConnection.httpDelete(tdbKb);
            assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        }
    }
    
/* ******************** deletion of knowledgebases ******************** */

    public void testDeleteKnowledgebase()
            throws Exception {
        HttpResult r;

        // first add a new submodel, which we will very shortly delete
        r = QuaestorConnection.httpPut(makeKbUrl("tempsubmodel"),
                                       "<urn:example#test1> a <urn:example#testclass1>.",
                                       "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        assertNull(r.getContentAsString());

        // now delete it with the wrong name
        r = QuaestorConnection.httpDelete(makeKbUrl("bogusxxx"));
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);

        // ...and with the correct name
        r = QuaestorConnection.httpDelete(makeKbUrl("tempsubmodel"));
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        assertNull(r.getContentAsString());

        // Now delete the whole knowledgebase
        r = QuaestorConnection.httpDelete(makeKbUrl());
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        assertNull(r.getContentAsString());

        // should fail when done a second time
        r = QuaestorConnection.httpDelete(makeKbUrl());
        assertEquals(4, r.getStatus()/100);
    }

    /* ********** Helper methods ********** */

    private URL makeKbUrl() 
            throws java.net.MalformedURLException {
        return makeKbUrl(null);
    }
    private static URL makeKbUrlBase;
    private URL makeKbUrl(String submodel) 
            throws java.net.MalformedURLException {
        if (submodel == null) {
            if (makeKbUrlBase == null)
                makeKbUrlBase = new URL(contextURL, "kb/"+testKB);
            return makeKbUrlBase;
        }
        else if (submodel.startsWith("?"))
            return new URL(contextURL, "kb/"+testKB+submodel);
        else
            return new URL(contextURL, "kb/"+testKB+"/"+submodel);
    }

    /**
     * Assert that the given HttpResult has the expected status.
     * Throw an assertion error and emit debugging info if not.
     * @param r the result obtained from an HTTP transaction
     * @param expectedStatus the status it should have
     * @throws AssertionFailedError if the assertion fails
     */
    private void assertStatus(HttpResult r, int expectedStatus)
            throws AssertionFailedError {
        if (r.getStatus() != expectedStatus) {
            System.err.println("Status: expected " + expectedStatus
                               + ", got " + r.getStatus());
            System.err.println(r);
            
            throw new AssertionFailedError("Expected status " + expectedStatus
                                           + ", but got " + r.getStatus());
        }
    }

    /**
     * Assert that the given HttpResult has the expected content type.
     * Throw an assertion error and emit debugging info if not.
     * @param r the result obtained from an HTTP transaction
     * @param expectedType the content type it should have
     * @throws AssertionFailedError if the assertion fails
     */
    private void assertContentType(HttpResult r, String expectedType)
            throws AssertionFailedError {
        if (! r.getContentType().equals(expectedType)) {
            System.err.println("ContentType: expected " + expectedType
                               + ", got " + r.getContentType());
            System.err.println(r);
            
            throw new AssertionFailedError("ContentType: expected " + expectedType
                                           + ", got " + r.getContentType());
        }
    }

    /**
     * Assert that two URLs are equivalent.  We can't just compare
     * them as strings, because http://foo and http://foo:80 should be
     * the same, and there are probably others we haven't come across yet.
     */
    private void assertEquivalentURLs(URL u1, URL u2) 
            throws AssertionFailedError {
        int port1 = u1.getPort();
        if (port1 < 0) port1 = u1.getDefaultPort();
        int port2 = u2.getPort();
        if (port2 < 0) port2 = u2.getDefaultPort();

//         System.err.println("u1: protocol=" + u1.getProtocol()
//                            + ", host=" + u1.getHost()
//                            + ", port=" + port1
//                            + ", path=" + u1.getPath());
//         System.err.println("u2: protocol=" + u2.getProtocol()
//                            + ", host=" + u2.getHost()
//                            + ", port=" + port2
//                            + ", path=" + u2.getPath());
        assertEquals(u1.getProtocol(), u2.getProtocol());
        assertEquals(u1.getHost(),     u2.getHost());
        assertEquals(port1,            port2);
        assertEquals(u1.getPath(),     u2.getPath());
    }

    private void assertEquivalentURLs(URL u1, String u2) 
            throws AssertionFailedError {
        try {
            assertEquivalentURLs(u1, new URL(u2));
        } catch (java.net.MalformedURLException e) {
            System.err.println("test URL " + u2 + " malformed");
            throw new AssertionFailedError("assertEquivalentURLs: expected " + u1
                                           + ", got " + u2 + ", which is malformed");
        }
    }

    private void assertEquals(String[] slcorrect, String[] sltest) {
        try {
            assertEquals(slcorrect.length, sltest.length);
            for (int i=0; i<slcorrect.length; i++)
                assertEquals(slcorrect[i], sltest[i]);
        } catch (AssertionFailedError e) {
            System.err.println("assertEquals[StringList]:");
            System.err.println("  expected:");
            for (int i=0; i<slcorrect.length; i++)
                System.err.println("    " + slcorrect[i]);
            System.err.println("  got:");
            for (int i=0; i<sltest.length; i++)
                System.err.println("    " + sltest[i]);

            // and rethrow the error
            throw e;
        }
    }

    /**
     * Delete a knowledgebase, without failing if it doesn't exist.
     * This is useful before creating a test KB, to clear away that KB if it was left
     * lying around after a previous unsuccessful test.
     */
    private static void deleteKbIfPresent(URL kbURL) {
        try {
            HttpResult r = QuaestorConnection.httpDelete(kbURL);
            switch (r.getStatus()) {
              case HttpURLConnection.HTTP_NO_CONTENT:
                // ooops -- a "testing" KB hanging over from a previous test?
                // System.err.println("Deleted old KB at " + kbURL);
                break;
              case HttpURLConnection.HTTP_BAD_REQUEST:
                // there was no knowledgebase there to delete (fair enough)
                break;
              default:
                throw new AssertionFailedError("Unexpected status " + r.getStatus()
                                               + " when clearing old testing knowledgebase " + kbURL
                                               + "; r=" + r);
            }
        } catch (QuaestorException e) {
            throw new AssertionFailedError("Got exception <" + e 
                                           +"> when attempting to delete knowledgebase + kbURL");
        }
    }
    
    /**
     * Create an RPC call from a method and arguments.  Returns a valid
     * XML-RPC call element as XML in a string.
     * @param method the method name
     * @param args an array of objects, each one of which will be turned into
     * a method argument.  This may be null to indicate no arguments
     */
    private String makeXmlRpcCall(String method, Object[] args) 
            throws Exception {
        return makeXmlRpcCall(method, args, null);
    }

    /**
     * Create an RPC call from a method and arguments.  Returns a valid
     * XML-RPC call element as XML in a string.
     * @param method the method name
     * @param args an array of objects, each one of which will be turned into
     * a method argument.  This may be null to indicate no arguments
     * @param transformerProperties a list of pairs of strings representing
     * options to be set on the transformer
     * @see javax.xml.transform.OutputKeys
     */
    private String makeXmlRpcCall(String method,
                                  Object[] args,
                                  String[] transformerProperties)
            throws Exception {
        Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .newDocument();
        Element methodCall = dom.createElement("methodCall");
        dom.appendChild(methodCall);
        Element methodName = dom.createElement("methodName");
        methodName.appendChild(dom.createTextNode(method));
        methodCall.appendChild(methodName);
        Element params = dom.createElement("params");
        methodCall.appendChild(params);
        if (args != null) {
            for (int i=0; i<args.length; i++) {
                Element param = dom.createElement("param");
                Element value = dom.createElement("value");
                String valueType;
                boolean isBoolean = false;
                if (args[i] instanceof String)
                    valueType = "string";
                else if (args[i] instanceof Boolean) {
                    valueType = "boolean";
                    isBoolean = true;
                } else if (args[i] instanceof Integer)
                    valueType = "int";
                else if (args[i] instanceof Double)
                    valueType = "double";
                else
                    throw new IllegalArgumentException
                            ("Don't yet support XML-RPC with objects like "
                             + args[i]);
                Element theValue = dom.createElement(valueType);
                if (isBoolean)
                    theValue.appendChild(dom.createTextNode
                                         (((Boolean)args[i]).booleanValue()
                                          ? "1" : "0"));
                else
                    theValue.appendChild(dom.createTextNode(args[i].toString()));
                value.appendChild(theValue);
                param.appendChild(value);
                params.appendChild(param);
            }
        }
        Transformer t = TransformerFactory.newInstance().newTransformer();
        if (transformerProperties != null) {
            assert transformerProperties.length%2 == 0;
            for (int i=0; i<transformerProperties.length; i+=2) 
                t.setOutputProperty
                        (transformerProperties[i],
                         transformerProperties[i+1]);
        }
        java.io.ByteArrayOutputStream baos
                = new java.io.ByteArrayOutputStream();
        t.transform(new javax.xml.transform.dom.DOMSource(methodCall),
                    new javax.xml.transform.stream.StreamResult(baos));
        return baos.toString();
    }

    /**
     * Make an RPC call.  This is just a convenience method,
     * wrapping {@link #getRpcResponse}, {@link #makeXmlRpcResponse}
     * and {@link #httpPost}. 
     * @param method the method name
     * @param args the list of object arguments
     * @return the result of making the call
     */
    public ParsedRpcResponse performXmlRpcCall(String method, Object[] args) 
            throws Exception {
        return getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint,
                                       makeXmlRpcCall(method, args),
                                       "text/xml"));
    }

    private ParsedRpcResponse parseXmlRpcResponse(String xmlResponse)
            throws Exception {
        try {
            Document dom = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new org.xml.sax.InputSource
                           (new java.io.StringReader(xmlResponse)));
            Element top = dom.getDocumentElement();
            NodeList faults = top.getElementsByTagName("fault");
            if (faults.getLength() != 0) {
                NodeList members = ((Element)faults.item(0))
                        .getElementsByTagName("member");
                if (members.getLength() != 2) {
                    return new ParsedRpcResponse("Invalid fault structure",
                                                 xmlResponse);
                } else {
                    Object faultCode
                            = valueToObject(((Element)members.item(0))
                                            .getElementsByTagName("value")
                                            .item(0));
                    if (! (faultCode instanceof Integer))
                        return new ParsedRpcResponse
                                ("Invalid fault structure: not int code",
                                 xmlResponse);
                    Object faultString
                            = valueToObject(((Element)members.item(1))
                                            .getElementsByTagName("value")
                                            .item(0));
                    if (! (faultString instanceof String))
                        return new ParsedRpcResponse
                                ("Invalid fault structure: not string msg",
                                 xmlResponse);
                    return new ParsedRpcResponse
                            (((Integer)faultCode).intValue(),
                             (String)faultString);
                }
            } else {
                NodeList values = top.getElementsByTagName("value");
                if (values.getLength() != 1) {
                    return new ParsedRpcResponse
                            ("Invalid response structure", xmlResponse);
                } else {
                    return new ParsedRpcResponse
                            (valueToObject(values.item(0)));
                }
            }
        } catch (org.w3c.dom.DOMException e) {
            return new ParsedRpcResponse(e.getMessage() + " in "
                                         + xmlResponse);
        }
        // shouldn't get here
    }

    /** Transform a value element to an Object.
     * @throws a DOMException on formatting problems
     */
    private Object valueToObject(org.w3c.dom.Node v)
            throws org.w3c.dom.DOMException {
        short code = 0;
        if (! v.getNodeName().equals("value"))
            throw new org.w3c.dom.DOMException
                    (code,
                     "Bad call to valueToObject with node " + v.getNodeName());
        org.w3c.dom.Node vv = v.getFirstChild(); // a <string>, <int>...
        Object ret;
        if (vv.getNodeType() == vv.TEXT_NODE) {
            ret = vv.getNodeValue();
        } else if (vv.getNodeType() == vv.ELEMENT_NODE) {
            String nn = vv.getNodeName();
            String nv = vv.getFirstChild().getNodeValue();
            if (nn.equals("string")) {
                ret = nv;
            } else if (nn.equals("int") || nn.equals("i4")) {
                ret = new Integer(nv);
            } else if (nn.equals("double")) {
                ret = new Double(nv);
            } else if (nn.equals("boolean")) {
                if (nv.equals("0"))
                    ret = Boolean.FALSE;
                else if (nv.equals("1"))
                    ret = Boolean.TRUE;
                else
                    throw new org.w3c.dom.DOMException(code, "Bad boolean");
            } else {
                throw new org.w3c.dom.DOMException
                        (code, "Unexpected element name " + nn);
            }
        } else {
            throw new org.w3c.dom.DOMException
                    (code, "Unexpected element type of node "
                     + vv.getNodeName());
        }
        return ret;
    }

    /* ******************** Helper class -- manage RPC ******************** */

    public static class ParsedRpcResponse {
        boolean isValidResponse;
        boolean isFaultResponse;
        int faultCode;
        int intValue;
        String stringValue;
        double doubleValue;
        boolean booleanValue;
        Object objectValue;
        Class valueClass;

        public ParsedRpcResponse(int faultCode, String faultString) {
            isValidResponse = true;
            isFaultResponse = true;
            this.faultCode = faultCode;
            stringValue = faultString;
        }
        public ParsedRpcResponse(Object param) {
            isValidResponse = true;
            isFaultResponse = false;
            valueClass = param.getClass();
            objectValue = param;
            if (param instanceof String)
                stringValue = (String)param;
            else if (param instanceof Integer)
                intValue = ((Integer)param).intValue();
            else if (param instanceof Double)
                doubleValue = ((Double)param).doubleValue();
            else if (param instanceof Boolean)
                booleanValue = ((Boolean)param).booleanValue();
            else
                throw new IllegalArgumentException
                        ("ParsedRpcResponse: bad call: param " + param
                         + " is of type " + param.getClass().toString());
        }
        public ParsedRpcResponse(String errormessage, String sourceXml) {
            isValidResponse = false;
            isFaultResponse = false;
            stringValue = errormessage + " in " + sourceXml;
        }
        public boolean isValid() {
            return isValidResponse;
        }
        public boolean isFault() {
            return isFaultResponse;
        }
        public int getFaultCode() {
            if (!isValidResponse || !isFaultResponse)
                throw new IllegalStateException
                        ("Can't get fault code for non-fault response");
            return faultCode;
        }
        public String getFaultMessage() {
            if (!isValidResponse || !isFaultResponse)
                throw new IllegalStateException
                        ("Can't get fault message for non-fault response");
            return stringValue;
        }
        public int getResponseInt() {
            if (!isValidResponse || isFaultResponse)
                throw new IllegalStateException
                        ("Can't get response for non-success response");
            if (valueClass == Integer.TYPE)
                return intValue;
            throw new IllegalStateException
                    ("Can't return int for non-int value (param type "
                     + valueClass + ")");
        }
        public String getResponseString() {
            if (!isValidResponse || isFaultResponse)
                throw new IllegalStateException
                        ("Can't get response for non-success response");
            if (valueClass == String.class)
                return stringValue;
            throw new IllegalStateException
                    ("Can't return string for non-string value (param type "
                     + valueClass + ")");
        }
        public double getResponseDouble() {
            if (!isValidResponse || isFaultResponse)
                throw new IllegalStateException
                        ("Can't get response for non-success response");
            if (valueClass == Double.TYPE)
                return doubleValue;
            throw new IllegalStateException
                    ("Can't return double for non-double value (param type "
                     + valueClass + ")");
        }
        public boolean getResponseBoolean() {
            if (!isValidResponse || isFaultResponse)
                throw new IllegalStateException
                        ("Can't get response for non-success response");
            if (valueClass == Boolean.TYPE)
                return booleanValue;
            throw new IllegalStateException
                    ("Can't return boolean for non-boolean value (param type "
                     + valueClass + ")");
        }
        public Class getResponseClass() {
            return valueClass;
        }
        public String toString() {
            if (!isValidResponse)
                return "Error: " + stringValue;
            else if (isFaultResponse)
                return "Fault: code=" + faultCode + ", msg=" + stringValue;
            else
                return "Param(" + valueClass + ")=" + objectValue;
        }
    }

}
