/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.ExternalRegistry;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.util.DomHelper;
import static org.custommonkey.xmlunit.XMLAssert.*;
import org.w3c.dom.Document;

/** System tests for ADQL querying part of External Registry Component.
 * @author Noel Winstanley
 * @since Aug 3, 20062:20:00 AM
 */
public class ExternalRegistryADQLSystemTest extends InARTestCase {



	@Override
    protected void setUp() throws Exception {
		super.setUp() ;
		ACR reg = getACR();
		ex =(ExternalRegistry) reg.getService(ExternalRegistry.class);
		assertNotNull(ex);
		Registry internal = (Registry)reg.getService(Registry.class);
		endpoint = internal.getSystemRegistryEndpoint();
		//endpoint = new URI("http://127.0.0.1:8099/mssl-registry/services/RegistryQueryv1_0"); // tcpmon
		assertNotNull(endpoint);
	}

	protected ExternalRegistry ex;
	protected URI endpoint;
	

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		ex = null;
		endpoint = null;
	}
	public static final String RESOURCE_ID = ExternalRegistryXQuerySystemTest.SINGLE_RESOURCE_XQUERY_ID;
	/** query that returns one result */
	 public static final String ADQLS_QUERY_STRING = "select * from Registry r where r.identifier='" + RESOURCE_ID + "'";
   
	 
	 public static final String ADQLX_QUERY_STRING = 
	     "<v1:Select xmlns:v1='http://www.ivoa.net/xml/ADQL/v1.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>\n"
	     +"<v1:SelectionList>\n"
	     +"<v1:Item xsi:type='v1:allSelectionItemType'/>\n"
	     +"</v1:SelectionList>\n"
	     +"<v1:From>\n"
	     +"<v1:Table xsi:type='v1:tableType' Name='registry' Alias='r'/>\n"
	     +"</v1:From>\n"
	     +"<v1:Where>\n"
	     +"<v1:Condition xsi:type='v1:comparisonPredType' Comparison='='>\n"
	     +"   <v1:Arg xsi:type='v1:columnReferenceType' xpathName='identifier'/>\n"
	     +"   <v1:Arg xsi:type='v1:atomType'>\n"
	     +"       <v1:Literal xsi:type='v1:stringType' Value='"
	      + RESOURCE_ID
	         +"'/>\n"
	     +"   </v1:Arg>\n"
	     +"</v1:Condition>\n"
	     +"</v1:Where>\n"
	+"</v1:Select>";
	 
	   public static final String ADQLX_UNKNOWN_QUERY_STRING = 
	         "<v1:Select xmlns:v1='http://www.ivoa.net/xml/ADQL/v1.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>\n"
	         +"<v1:SelectionList>\n"
	         +"<v1:Item xsi:type='v1:allSelectionItemType'/>\n"
	         +"</v1:SelectionList>\n"
	         +"<v1:From>\n"
	         +"<v1:Table xsi:type='v1:tableType' Name='registry' Alias='r'/>\n"
	         +"</v1:From>\n"
	         +"<v1:Where>\n"
	         +"<v1:Condition xsi:type='v1:comparisonPredType' Comparison='='>\n"
	         +"   <v1:Arg xsi:type='v1:columnReferenceType' xpathName='identifier'/>\n"
	         +"   <v1:Arg xsi:type='v1:atomType'>\n"
	         +"       <v1:Literal xsi:type='v1:stringType' Value='"
	          + "ivo://unknown.identifier.fo"
	             +"'/>\n"
	         +"   </v1:Arg>\n"
	         +"</v1:Condition>\n"
	         +"</v1:Where>\n"
	    +"</v1:Select>";
	 /**
     *  qiuery that retrusn nothing
     */
    private static final String NONEXISTENT_ADQLS_QUERY_STRING = ADQLS_QUERY_STRING + " and r.identifier='ivo://test.nonexistent'";
	  
	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#adqlsSearch(java.net.URI, java.lang.String)}.
	 */
	public void testAdqlsSearch() throws Exception{
		Resource[] r = ex.adqlsSearch(endpoint,ADQLS_QUERY_STRING );
		assertNotNull(r);
		assertEquals(1,r.length);
		assertEquals(RESOURCE_ID,r[0].getId().toString());
	}
	// query that produces no results
    public void testAdqlsSearchUnknown() throws Exception{
        Resource[] r = ex.adqlsSearch(endpoint,NONEXISTENT_ADQLS_QUERY_STRING );
        assertNotNull(r);
        assertEquals(0,r.length);
    }
    
    public void testAdqlsSearchInvalid() throws Exception{
        try {
            ex.adqlsSearch(endpoint,"nonsense nonsense nonsense" );
            fail("expected to chuck");
        } catch (InvalidArgumentException e) {
            // expected
        }
    }


	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#adqlsSearchXML(java.net.URI, java.lang.String, boolean)}.
	 */
	public void testAdqlsSearchXML() throws Exception{
		Document d = ex.adqlsSearchXML(endpoint,ADQLS_QUERY_STRING,false);
		assertNotNull(d);
		
		assertXpathEvaluatesTo("VOResources","local-name(/*)",d); // root is a VOResources
		assertXpathEvaluatesTo("Resource","local-name(/*/*)",d); // it contains a resource
		assertXpathNotExists("/*/*[local-name() != 'Resource']",d); // only contains resources

		
		// feed it through the parser.
		Resource[] r = ex.buildResources(d);
		assertNotNull(r);
		assertEquals(1,r.length);
		assertEquals(RESOURCE_ID,r[0].getId().toString());	
	}
	
	public void testAdqlsSearchXMLIdentifiersOnly() throws Exception{
		Document d = ex.adqlsSearchXML(endpoint,ADQLS_QUERY_STRING,true);
		assertNotNull(d);
        assertXpathEvaluatesTo("VOResources","local-name(/*)",d); // root is a VOResources
        assertXpathEvaluatesTo("identifier","local-name(/*/*)",d); 
        assertXpathNotExists("/*/*[local-name() != 'identifier']",d); 

	}

	
	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#adqlxSearch(java.net.URI, org.w3c.dom.Document)}.
	 */
	public void testAdqlxSearch() throws Exception{
	    Document doc = DomHelper.newDocument(ADQLX_QUERY_STRING);
	    Resource[] resources = ex.adqlxSearch(endpoint,doc);
	    assertNotNull(resources);
	    assertEquals(1,resources.length);
        assertEquals(RESOURCE_ID,resources[0].getId().toString());	    
	    
        // test that implementation doesnt corrupt the nput query.
       Document control =  DomHelper.newDocument(ADQLX_QUERY_STRING);
       assertNotNull(control);
        assertXMLEqual(control,doc);
	    
	}
   

	
	// what happens when we pass a query which won't match anything.
	public void testAdqlxSearchUnknown() throws Exception{
	       Document doc = DomHelper.newDocument(ADQLX_UNKNOWN_QUERY_STRING);
        Resource[] resources = ex.adqlxSearch(endpoint,doc);
        assertNotNull(resources);
        assertEquals(0,resources.length);      
	}
	
	/** what happens when we pass a non-adql input in ? */
	public void testAdqlxSearchInvalid() throws Exception{
	    Document doc = DomHelper.newDocument("<foo/>");
	    try {
	        ex.adqlxSearch(endpoint,doc);
	        fail("expected to chuck");
	    } catch (ServiceException e) {
	        // ok
	    }
	}
		
	/**
	 * Test method for {@link org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl#adqlxSearchXML(java.net.URI, org.w3c.dom.Document, boolean)}.
	 */
	public void testAdqlxSearchXML()throws Exception {
	       Document doc = DomHelper.newDocument(ADQLX_QUERY_STRING);
        Document d = ex.adqlxSearchXML(endpoint,doc,false);
        assertNotNull(d);
        
        assertXpathEvaluatesTo("VOResources","local-name(/*)",d); // root is a VOResources
        assertXpathEvaluatesTo("Resource","local-name(/*/*)",d); // it contains a resource
        assertXpathNotExists("/*/*[local-name() != 'Resource']",d); // only contains resources

        
        // feed it through the parser.
        Resource[] r = ex.buildResources(d);
        assertNotNull(r);
        assertEquals(1,r.length);
        assertEquals(RESOURCE_ID,r[0].getId().toString());    
	}

	// query that returns no results.
	public void testAdqlxSearchXMLEmpty()throws Exception {
	       Document doc = DomHelper.newDocument(ADQLX_UNKNOWN_QUERY_STRING);

    Document d = ex.adqlxSearchXML(endpoint,doc,false);
    assertNotNull(d);
    
    assertXpathEvaluatesTo("VOResources","local-name(/*)",d); // root is a VOResources   
    assertXpathNotExists("/*/*[local-name() = 'Resource']",d); // contains no resources

    
    // feed it through the parser.
    Resource[] r = ex.buildResources(d);
    assertNotNull(r);
    assertEquals(0,r.length);  
	}
	
	public void testAdqlxSearchXMLInvalid()throws Exception {
        Document doc = DomHelper.newDocument("<foo/>");
        try {
            ex.adqlxSearchXML(endpoint,doc,false);
            fail("expected to chuck");
        } catch (ServiceException e) {
            // ok
        }		
	}
	
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ExternalRegistryADQLSystemTest.class));
    }
}
