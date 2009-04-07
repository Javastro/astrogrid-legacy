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
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.Validation;
import org.astrogrid.acr.ivoa.resource.ConeCapability.Query;
import org.astrogrid.acr.ivoa.resource.SiapCapability.ImageSize;
import org.astrogrid.acr.ivoa.resource.SiapCapability.SkySize;

/** abstract base class for parser tests.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 21, 20078:28:31 PM
 */
public class AbstractTestForParser extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		fac = XMLInputFactory.newInstance();
	}
	
	@Override
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
		final InputStream is = this.getClass().getResourceAsStream(string);
		assertNotNull(is);
		final XMLStreamReader in = fac.createXMLStreamReader(is);
		final ResourceStreamParser p = new ResourceStreamParser(in);
		return p;
	}
	/** verify that there's only one resource iin the parser , and return it */
	public static Resource assertOnlyOne(final ResourceStreamParser p) {
		assertTrue("no resources",p.hasNext());
		final Object o = p.next();
		assertNotNull(o);
		assertTrue(o instanceof Resource);
		assertFalse(p.hasNext());
		return (Resource)o;
	}
	
	/** assert this is a non-null 0-length array */
	public static void assertEmpty(final Object[] arr) {
		assertNotNull("assertEmpty: arr is null",arr);
		assertEquals("assertEmpty: arr contains items",0,arr.length);
	}
	
	/** assert two arrays are equal */
	public static void assertEquals(final Object[] expected, final Object[] actual) {
		assertEquals("arrays don't match",expected, actual);
	}

	/** assert two arrays are equal */
	public static void assertEquals(final String msg,final Object[] expected, final Object[] actual) {
	    if (expected == null) {
	        assertNull("actuall is not null",actual);
	        return;
	    } else {
	        assertNotNull("actual is null",actual);
	    }
		assertEquals("array sizes differ",expected.length,actual.length);
		assertTrue(msg + ": " + ArrayUtils.toString(expected) + ", " + ArrayUtils.toString(actual)
				,Arrays.equals(expected, actual));
	}

	/** check the basics of a resource are in place.
	 * recursively calls validateContent, validateCuration, etc
	 * @param r
	 */
	public static void validateResource(final Resource r) {
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
	public static void validateContent(final Content c) {
		assertNotNull("null content",c);
		assertNotNull("null contentlevel",c.getContentLevel());
		// sometimes it is null.
	//	assertNotNull("null description",c.getDescription());
	//	assertNotNull("null referenceuri",c.getReferenceURI());
		assertNotNull("null relationships",c.getRelationships());
		assertNotNull("null subjects",c.getSubject());
		assertNotNull("null types",c.getType());
		
		//@todo source?
	}
	
	/** check basics of validation are correct */
	public static void validateValidationLevel(final Validation[] vs) {
		assertNotNull(vs);
	}

	/** checks the basics of a curation are in place.
	 * @param cu
	 */
	public static void validateCuration(final Curation cu) {
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

	public static Service validateService(final Resource r) {
		assertNotNull("resouce is null",r);
		assertTrue("not an instanceof service",r instanceof Service);
		final Service s = (Service)r;
		final Capability[] caps = s.getCapabilities();
		assertNotNull("capabilities are null",caps);
		for (int i = 0; i < caps.length; i++) {
			validateCapability(caps[i]);
		}
		assertNotNull("rights are null",s.getRights());
		return s;
	}
	
	public static void validateCapability(final Capability c) {
		assertNotNull("capability is null",c);
	//@later put this back in afterwards	validateValidationLevel(c.getValidationLevel());
		final Interface[] is = c.getInterfaces();
		assertNotNull("intefaces is null",is);
		for (int i = 0 ; i < is.length; i++) {
			validateInterface(is[i]);
		}
	}
	/** check the basics of a capabilitiy - expects description to be null */
	public static void checkCapability(final Capability c, final String standardID, final String type, final int ifaceCount) {
	    if (standardID == null) {
	        assertNull("expected null standardID",c.getStandardID());
	    } else {
	        assertEquals("capability - standardId",standardID,c.getStandardID().toString());
	    }
	    assertTrue("capability - type",c.getType().indexOf(type) != -1);
	    assertEquals("capabiltiy - iface count",ifaceCount,c.getInterfaces().length);
	    assertNull("description is non-null",c.getDescription());
	}
	
	   /** check the basics of a capabilitiy  */
    public static void checkCapability(final Capability c, final String standardID, final String type, final int ifaceCount,final String desc) {
        if (standardID == null) {
            assertNull("expected null standardID",c.getStandardID());
        } else {
            assertEquals("capability - standardId",standardID,c.getStandardID().toString());
        }
        assertTrue("capability - type",c.getType().indexOf(type) != -1);
        assertEquals("capabiltiy - iface count",ifaceCount,c.getInterfaces().length);
        assertEquals("capability - description",desc,c.getDescription());
    }
	
	
	/** checks a capability to be a cone 
	 * @param records 
	 * @param sr 
	 * @param verbose 
	 * @param query */
	public static void checkConeCapability(final Capability c, final int records, final float sr, final boolean verbose, final Query query) {
	    assertTrue("not of type ConeCapability",c instanceof ConeCapability);
	    final ConeCapability cap = (ConeCapability)c;
	    assertEquals(records,cap.getMaxRecords());
	    assertEquals(sr,cap.getMaxSR(),0.001);
	    assertEquals(verbose,cap.isVerbosity());
	    assertEquals(query,cap.getTestQuery());
	}
	
	   public static void checkSiapCapability(final Capability c, final String type, final int maxFileSize, final SkySize maxImageExtent, final ImageSize maxImageSize, final SkySize maxQueryReg, final int maxRec, final SiapCapability.Query query) {
	        assertTrue("not of type SiapCapability",c instanceof SiapCapability);
	       final SiapCapability cap = (SiapCapability)c;
	       assertEquals(type,cap.getImageServiceType());
	       assertEquals(maxFileSize,cap.getMaxFileSize());
	       
	       assertEquals(maxImageExtent,cap.getMaxImageExtent());   
	      assertEquals(maxImageSize,cap.getMaxImageSize());
           assertEquals(maxQueryReg,cap.getMaxQueryRegionSize());
           assertEquals(maxRec,cap.getMaxRecords());
	       assertEquals(query,cap.getTestQuery());
	    }
	/** checks a coverage 
	 * @param footprint 
	 * @param hasSTC 
	 * @param wbands */
	public static void checkCoverage(final Coverage c, final ResourceName footprint, final boolean hasSTC, final boolean allSky,final String[] wbands) {
	    assertNotNull("null coverage",c);
	    assertEquals("coverage - footprint",footprint,c.getFootprint());
	    assertEquals("coverage - stc",hasSTC,c.getStcResourceProfile() != null) ;	 
	    if (hasSTC) {
	        assertNotNull(c.getStcResourceProfile().getStcDocument());
	        assertEquals("stc - all sky",allSky,c.getStcResourceProfile().isAllSky());
	    }
	    assertEquals("coverage - wavebands",wbands,c.getWavebands());
	}
	/**
	 * check an interface
	 * @param i
	 * @param role 
	 * @param type 
	 * @param version 
	 * @param sec 
	 * @param accURL 
	 */
	public static void checkInterface(final Interface i, final String role, final String type, final String version, final SecurityMethod[] sec, final AccessURL[] accURL) {
	    assertNotNull("null interface",i);
	    if (role != null) {
	        assertNotNull(i.getRole());
	    }
	    assertEquals("interface - role",role,i.getRole());
	    if (type != null) {
	        assertNotNull(i.getType());
	    }
	    assertEquals("interface - type",type,i.getType());
	    if (version != null) {
	        assertNotNull(i.getVersion());
	    }
	    assertEquals("interface - version",version,i.getVersion());
	    if (sec != null) {
	        assertNotNull(i.getSecurityMethods());
	    }
	    assertEquals("interface - security",sec,i.getSecurityMethods());
	    if (accURL != null) {
	        assertNotNull(i.getAccessUrls());
	    }
	    assertEquals("interface - accessURL",accURL,i.getAccessUrls());
	}
	/**
	 * @param interface1
	 */
	public static void validateInterface(final Interface i) {
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
	public static void checkResource(final Resource r, final String id, final String shortname, final String title, final String typeSubstring) throws Exception {
		assertEquals("id",id,r.getId().toString());
		assertEquals("shortname",shortname,r.getShortName());
		assertEquals("title",title,r.getTitle());
		assertTrue("type",r.getType().indexOf(typeSubstring) != -1);		
	}
	
	/** checks a content against expected values */
	public static void checkContent(final Content c, final String[] subjects, final String[] types, final String[] levels, final Relationship[] relationships) {
		assertEquals("subjects don't match",subjects,c.getSubject());
		assertEquals("types don't match",types,c.getType());
		assertEquals("contentLevel",levels,c.getContentLevel());
		assertEquals("relationships",relationships,c.getRelationships());
	}
	
	/** checks a curation against expected values */
	public static void checkCuration(final Curation cu, final Contact[] contacts, final Creator[] creators, final ResourceName[] contributors, final Date[] dates, final ResourceName publisher, final String version) throws Exception{
		assertEquals("contacts",contacts,cu.getContacts());
		assertEquals("contributors",contributors,cu.getContributors());
		assertEquals("creators",creators,cu.getCreators());
		assertEquals("dates",dates,cu.getDates());
		assertEquals("publisher",publisher,cu.getPublisher());
		if (version == null) {
			assertNull(cu.getVersion());
		} else {
		assertNotNull("version not expected to be null",cu.getVersion());
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
	public static WebTester createWebTester(final Resource r) throws Exception {
		//String s = ResourceFormatter.renderResourceAsHTML(r);
	    final String s= PrettierResourceFormatter.renderResourceAsHTML(r);
		assertNotNull(s);
		final File f = File.createTempFile("resourceFormatterUnitTest", ".html");
		assertNotNull(f);
		f.deleteOnExit();
		final PrintWriter p = new PrintWriter(new FileWriter(f));
		p.print(s);
		p.close();
		final WebTester t = new WebTester();
		
		final TestContext testContext = t.getTestContext();
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
	public static WebTester basicResourceRendererTests(final Resource r) throws Exception {
		final WebTester wt = createWebTester(r);
	// title is now displayed in a jlabel	wt.assertTextPresent(r.getTitle());
		wt.assertTextPresent(r.getId().toString());
		if (r.getShortName() != null) {
			wt.assertTextPresent(r.getShortName());
		}
		return wt;
	}
	
}
