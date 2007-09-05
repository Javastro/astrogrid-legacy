// JUnit test cases for Quaestor

// Control the Quaestor servlet which is tested by setting the quaestor.url
// property (which means setting -Dquaestor.url=http://.../quaestor/. 
// in the call to ant).

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
import junit.framework.TestCase;

public class QuaestorTest
        extends TestCase {

    private static URL quaestorURL = null; // null indicates uninitialised
    private static URL contextURL;     // Same as quaestorURL, but usable
                                       // as a base URL for creating
                                       // new ones
    private static URL xmlrpcEndpoint;
    private static final String testKB = "testing";

    public QuaestorTest(String name)
            throws Exception {
        super(name);

        if (quaestorURL == null) {
            // first time
            quaestorURL = new URL(System.getProperty
                                  ("quaestor.url",
                                   "http://localhost:8080/quaestor"));
            contextURL = new URL(quaestorURL, quaestorURL.getPath() + "/");
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
                throw new junit.framework.AssertionFailedError
                        ("Tomcat not running");
            }

            // Attempt to delete the knowledgebase, whether or not it's there.
            // Don't care about the return value.
            System.err.println("Deleting any KB at " + makeKbUrl());
            r = QuaestorConnection.httpDelete(makeKbUrl());
        }
    }

    public void testGetKnowledgebaseList()
            throws Exception {
        HttpResult r = QuaestorConnection.httpGet(new URL(contextURL, "kb"));
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

    /* ******************** Other test methods ******************** */

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

        r = QuaestorConnection.httpGet(makeKbUrl("ontology"), "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/rdf+n3");
        assertNotNull(r.getContentAsString());

        // same content type if we request deprecated application/n3
        r = QuaestorConnection.httpGet(makeKbUrl("ontology"),
                    new String[] {"Accept", "application/n3"});
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/rdf+n3");
        assertNotNull(r.getContentAsString());
    }
    
    public void testAddInstances ()
            throws Exception {
        HttpResult r = QuaestorConnection.httpPut
                (makeKbUrl("instances"),
                 "<urn:example#i2> a <urn:example#c2>.",
                 "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        assertNull(r.getContentAsString());

        // Test the deprecated, but admissable, application/n3 MIME type.
        // Note that we must leave the 'instances' KB holding this triple,
        // for the following test to work.
        r = QuaestorConnection.httpPut
                (makeKbUrl("instances"),
                 "<urn:example#i1> a <urn:example#c1>.",
                 "application/n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        assertNull(r.getContentAsString());
    }

    public void testSparqlQueriesSelect() 
            throws Exception {
        // note: this query depends on the previous test making it satisfiable
        String query = "SELECT ?i where { ?i a <urn:example#c1>}";
        HttpResult r;

        // Make a successful query. and check XML response
        r = QuaestorConnection.httpPost(makeKbUrl(), query, "application/sparql-query");
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "application/xml");
        assertNotNull(r.getContentAsString());
        // don't check actual content string

        // try making a query against a submodel -- should fail
        r = QuaestorConnection.httpPost(makeKbUrl("ontology"),
                     query,
                     "application/sparql-query");
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);
        assertNotNull(r.getContentAsString()); // error message

        // Note we don't check whether the query fails if it's given with
        // the wrong content-type.  Perhaps we should.

        // check n-triple response
        // XXX this isn't actually correct yet, in that this still
        // returns a tabular-style query rather than n-triples.
        r = QuaestorConnection.httpPost(makeKbUrl(),
                                        query,
                                        new String[] {
                                            "Accept", "text/plain",
                                            "Content-Type", "application/sparql-query",
                                        });
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/plain");
        assertNotNull(r.getContentAsString());
        // don't check actual content

        String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
        r = QuaestorConnection.httpGet(makeKbUrl("?sparql="+encodedQuery));
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "application/xml");
        assertNotNull(r.getContentAsString());

        // the same, requesting text/plain
        r = QuaestorConnection.httpGet(makeKbUrl("?sparql="+encodedQuery),
                    new String[] {"Accept", "text/plain"});
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/plain");
        assertNotNull(r.getContentAsString());

        // ...and requesting CSV
        // Note that we actually test the content, here, so are coupled to
        // testAddInstances.
        r = QuaestorConnection.httpGet(makeKbUrl("?sparql="+encodedQuery),
                    new String[] {"Accept", "text/csv"});
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/csv");
        assertEquals("header=present", r.getContentTypeParameters());
        assertEquals("i\r\nurn:example#i1\r\n", r.getContentAsString());
    }

    public void testSparqlQueriesAsk()
            throws Exception {
        String query = "ASK { <urn:example#i1> a <urn:example#c1> }";
        HttpResult r;
        URL url = makeKbUrl();

        r = QuaestorConnection.httpPost(url, query, "application/sparql-query");
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "application/xml");
        assertNotNull(r.getContentAsString());
        // don't check actual content

        // Request plain text -- should return yes/no
        r = QuaestorConnection.httpPost(url,
                                        query,
                                        new String[] {
                                            "Content-Type", "application/sparql-query",
                                            "Accept", "text/plain",
                                        });
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/plain");
        // if I test the content for equality with "yes\r\n" it fails -- why?
        assertEquals("yes", r.getContentAsString().substring(0,3));

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
        String kbbaseurl = new URL(quaestorURL,
                                   quaestorURL.getPath()+"/kb/"+testKB).toString();
        rpc = getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint, call, "text/xml"));
        assertTrue(rpc.isValid() && !rpc.isFault());
        assertEquals(String.class, rpc.getResponseClass());
        assertEquals(kbbaseurl,
                     rpc.getResponseString());

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
        assertEquals(kbbaseurl,
                     rpc.getResponseString());
    }

    public void testRpcQuery()
            throws Exception {
        String query = "SELECT ?i where { ?i a <urn:example#c1>}";
        String askQuery = "ASK { <urn:example#i1> a <urn:example#c1>}";
        String call = makeXmlRpcCall("query-model",
                                     new Object[] {testKB, query});
        ParsedRpcResponse rpc
                = getRpcResponse(QuaestorConnection.httpPost(xmlrpcEndpoint, call, "text/xml"));
        assertTrue(rpc.isValid() && !rpc.isFault());
        assertEquals(String.class, rpc.getResponseClass());
        assertNotNull(rpc.getResponseString());

        URL pickup = new URL(rpc.getResponseString());
        HttpResult r = QuaestorConnection.httpGet(pickup);
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "application/xml");
        assertNotNull(r.getContentAsString());

        // getting it a second time should fail
        r = QuaestorConnection.httpGet(pickup);
        assertStatus(r, HttpURLConnection.HTTP_BAD_REQUEST);

        // test requesting different MIME type
        rpc = performXmlRpcCall("query-model",
                                new Object[] {testKB, query, "text/plain"});
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
                                new Object[] {testKB, query, "wibble/woot"});
        assertTrue(rpc.isValid() && rpc.isFault());
        assertEquals(20, rpc.getFaultCode());

        // request OK mime type for SELECT, bad for ASK
        rpc = performXmlRpcCall("query-model",
                                new Object[] {testKB, query, "text/csv"});
        assertTrue(rpc.isValid() && !rpc.isFault());
        assertEquals(String.class, rpc.getResponseClass());
        assertNotNull(rpc.getResponseString());
        pickup = new URL(rpc.getResponseString());
        r = QuaestorConnection.httpGet(pickup);
        assertStatus(r, HttpURLConnection.HTTP_OK);
        assertContentType(r, "text/csv");
        assertNotNull(r.getContentAsString());

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
    
    /* ******************** deletion of knowledgebases ******************** */

    public void testDeleteKnowledgebase()
            throws Exception {
        HttpResult r;

        // first add a new submodel, which we will momently delete
        r = QuaestorConnection.httpPut
                (makeKbUrl("tempsubmodel"),
                 "<urn:example#test1> a <urn:example#testclass1>.",
                 "text/rdf+n3");
        assertStatus(r, HttpURLConnection.HTTP_NO_CONTENT);
        assertNull(r.getContentAsString());

        // now delete it with the wrong namd
        r = QuaestorConnection.httpDelete(makeKbUrl("tempxx"));
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
        //assertEquals("", r.getContentAsString());
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
     * @throws junit.framework.AssertionFailedError if the assertion fails
     */
    private void assertStatus(HttpResult r, int expectedStatus)
            throws junit.framework.AssertionFailedError {
        if (r.getStatus() != expectedStatus) {
            System.err.println("Status: expected " + expectedStatus
                               + ", got " + r.getStatus());
            System.err.println(r);
            
            throw new junit.framework.AssertionFailedError
                    ("Expected status " + expectedStatus
                     + ", but got " + r.getStatus());
        }
    }

    /**
     * Assert that the given HttpResult has the expected content type.
     * Throw an assertion error and emit debugging info if not.
     * @param r the result obtained from an HTTP transaction
     * @param expectedType the content type it should have
     * @throws junit.framework.AssertionFailedError if the assertion fails
     */
    private void assertContentType(HttpResult r, String expectedType)
            throws junit.framework.AssertionFailedError {
        if (! r.getContentType().equals(expectedType)) {
            System.err.println("ContentType: expected " + expectedType
                               + ", got " + r.getContentType());
            System.err.println(r);
            
            throw new junit.framework.AssertionFailedError
                    ("ContentType: expected " + expectedType
                     + ", got " + r.getContentType());
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
