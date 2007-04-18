/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;
import net.sourceforge.jwebunit.TestContext;
import net.sourceforge.jwebunit.WebTester;

import org.apache.commons.lang.ArrayUtils;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.Validation;

/** abstract base class for parser tests.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 21, 20078:28:31 PM
 */
public class AbstractTestForParser extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		fac = XMLInputFactory.newInstance();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		fac = null;
	}
	
	XMLInputFactory fac ;
	
	/** create a parser and verify that it's correct.
	 * @param string
	 * @return
	 * @throws XMLStreamException
	 */
	protected ResourceStreamParser parse(final String string) throws XMLStreamException {
		InputStream is = this.getClass().getResourceAsStream(string);
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		return p;
	}
	/** verify that there's only one resource iin the parser , and return it */
	public static Resource assertOnlyOne(ResourceStreamParser p) {
		assertTrue(p.hasNext());
		Object o = p.next();
		assertNotNull(o);
		assertTrue(o instanceof Resource);
		assertFalse(p.hasNext());
		return (Resource)o;
	}
	
	/** assert this is a non-null 0-length array */
	public static void assertEmpty(Object[] arr) {
		assertNotNull("assertEmpty: arr is null",arr);
		assertEquals("assertEmpty: arr contains items",0,arr.length);
	}
	
	/** assert two arrays are equal */
	public static void assertEquals(Object[] expected, Object[] actual) {
		assertEquals("arrays don't match",expected, actual);
	}

	/** assert two arrays are equal */
	public static void assertEquals(String msg,Object[] expected, Object[] actual) {
		assertEquals("array sizes differ",expected.length,actual.length);
		assertTrue(msg + ": " + ArrayUtils.toString(expected) + ", " + ArrayUtils.toString(actual)
				,Arrays.equals(expected, actual));
	}

	/** check the basics of a resource are in place.
	 * recursively calls validateContent, validateCuration, etc
	 * @param r
	 */
	public static void validateResource(Resource r) {
		// can't guarantee these, sadly.
	//	assertNotNull("created date is null",r.getCreated());
	//	assertNotNull("updated date is null",r.getUpdated());
		assertNotNull("id is null",r.getId());
	//	assertNotNull("shortname is null",r.getShortName());
		assertNotNull("title is null",r.getTitle());
		assertEquals("status != active","active",r.getStatus());
		assertNotNull("type is null",r.getType());

		validateValidationLevel(r.getValidationLevel());		
		validateContent(r.getContent());
		validateCuration(r.getCuration());
	}
	
	
	/** check the basics of a content are in place.
	 * @param c
	 */
	public static void validateContent(Content c) {
		assertNotNull("null content",c);
		assertNotNull("null contentlevel",c.getContentLevel());
		assertNotNull("null description",c.getDescription());
		assertNotNull("null referenceuri",c.getReferenceURI());
		assertNotNull("null relationships",c.getRelationships());
		assertNotNull("null subjects",c.getSubject());
		assertNotNull("null types",c.getType());
		
		//@todo source?
	}
	
	/** check basics of validation are correct */
	public static void validateValidationLevel(Validation[] vs) {
		// does nothing at present. waiting for 1.0 registry 
		assertEmpty(vs);
	}

	/** checks the basics of a curation are in place.
	 * @param cu
	 */
	public static void validateCuration(Curation cu) {
		assertNotNull("null curation",cu);
		assertNotNull("null contacts",cu.getContacts());
		assertNotNull("null contributors",cu.getContributors());
		assertNotNull("null creators",cu.getCreators());
		assertNotNull("null dates",cu.getDates());
		assertNotNull("null publisher",cu.getPublisher());
		// can't guarantee version, it seems.
		//assertNotNull(cu.getVersion());
	}
	
	// service validation

	public static Service validateService(Resource r) {
		assertNotNull("resouce is null",r);
		assertTrue("not an instanceof service",r instanceof Service);
		Service s = (Service)r;
		Capability[] caps = s.getCapabilities();
		assertNotNull("capabilities are null",caps);
		for (int i = 0; i < caps.length; i++) {
			validateCapability(caps[i]);
		}
		assertNotNull("rights are null",s.getRights());
		return s;
	}
	
	public static void validateCapability(Capability c) {
		assertNotNull("capability is null",c);
	//@FIXME put this back in afterwards	validateValidationLevel(c.getValidationLevel());
		Interface[] is = c.getInterfaces();
		assertNotNull("intefaces is null",is);
		for (int i = 0 ; i < is.length; i++) {
			validateInterface(is[i]);
		}
	}
	
	/**
	 * @param interface1
	 */
	public static void validateInterface(Interface i) {
		assertNotNull("interface is null",i);
		assertNotNull(i.getAccessUrls());
		assertNotNull(i.getSecurityMethods());
		
	}

	/** checks a resource against expected values
	 * 
	 * @param r resource
	 * @param id uri
	 * @param shortname 
	 * @param title
	 * @param typeSubstring subtring to expect to find in @xsi:type
	 * @throws Exception
	 */
	public static void checkResource(Resource r, String id, String shortname, String title, String typeSubstring) throws Exception {
		assertEquals("id",id,r.getId().toString());
		assertEquals("shortname",shortname,r.getShortName());
		assertEquals("title",title,r.getTitle());
		assertTrue("type",r.getType().indexOf(typeSubstring) != -1);		
	}
	
	/** checks a content against expected values */
	public static void checkContent(Content c, String[] subjects, String[] types, String[] levels, Relationship[] relationships) {
		assertEquals("subjects don't match",subjects,c.getSubject());
		assertEquals("types don't match",types,c.getType());
		assertEquals("contentLevel",levels,c.getContentLevel());
		assertEquals("relationships",relationships,c.getRelationships());
	}
	
	/** checks a curation against expected values */
	public static void checkCuration(Curation cu, Contact[] contacts, Creator[] creators, ResourceName[] contributors, Date[] dates, ResourceName publisher, String version) throws Exception{
		assertEquals("contacts",contacts,cu.getContacts());
		assertEquals("contributors",contributors,cu.getContributors());
		assertEquals("creators",creators,cu.getCreators());
		assertEquals("dates",dates,cu.getDates());
		assertEquals("publisher",publisher,cu.getPublisher());
		if (version == null) {
			assertNull(version);
		} else {
			assertNotNull(version);
			assertEquals("version",version,cu.getVersion());
		}
		
	}
	
	/** as we're creating so many resources, might as well pass them through the 
	 * formatter too
	 * this method renders the resouce to html, and then returns a web-tester
	 * attached to the html.
	 * 
	 * it's necessary to write the html out to a temporary file to achieve this.
	 * 
	 * just calling this method by itself is enough to exercise a large part of the 
	 * renderer - however, web tester should really hten be used to make some
	 * assertions about the result
	 *  */
	public static WebTester createWebTester(Resource r) throws Exception {
		String s = ResourceFormatter.renderResourceAsHTML(r);
		assertNotNull(s);
		File f = File.createTempFile("resourceFormatterUnitTest", ".html");
		assertNotNull(f);
		f.deleteOnExit();
		PrintWriter p = new PrintWriter(new FileWriter(f));
		p.print(s);
		p.close();
		WebTester t = new WebTester();
		
		TestContext testContext = t.getTestContext();
		testContext.setBaseUrl(f.toURL().toString());
		t.beginAt("/");
		//t.dumpResponse();
		return t;
	}
	
	/** create a web tester and use it to verify some basic things about the generated
	 * html
	 * @param r
	 * @return
	 * @throws Exception
	 */ 
	public static WebTester basicResourceRendererTests(Resource r) throws Exception {
		WebTester wt = createWebTester(r);
		wt.assertTextPresent(r.getTitle());
		wt.assertTextPresent(r.getId().toString());
		if (r.getShortName() != null) {
			wt.assertTextPresent(r.getShortName());
		}
		return wt;
	}
	
}
