/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.io.InputStream;
import java.net.URI;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import net.sourceforge.jwebunit.WebTester;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.DataService;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.HasCoverage;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.Service;

import junit.framework.TestCase;

/** test parsing of cone services.
 * @todo find way of adding coverage into the parsed datastructure.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 20079:21:02 PM
 */
public class ConeParserUnitTest extends AbstractTestForParser{

	public void testConeService1() throws Exception {

		ResourceStreamParser p = parse("cone1.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://cadc.nrc.ca/cs/cnoc1Cat"
				, "CNOC1"
				, "Canadian Network for Observational Cosmology"
				, "ConeSearch");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("David Schade");}});
				setEmail("David.Schade@nrc.ca");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{
					setValue("Yee et al.");
				}});
			}}	
		}
		, new ResourceName[] { new ResourceName() {{ setValue("CADC");}}		}
		, new Date[] {}
		, new ResourceName() {{
			setValue("Canadian Astronomy Data Centre");
			setId(new URI("ivo://cadc.nrc.ca/org"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"galaxies","clusters of galaxies","redshifts"}
		, new String[] { "catalog"} // nb, note lower casing here.
		,new String[] {"research"}
		, new Relationship[] {}); 
		// services.
		Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals(1,s.getCapabilities().length);
		// little bit uncomforatblae about this still.
		Capability cap = s.getCapabilities()[0];
		assertNull(cap.getDescription());
		assertEquals("vs:ParamHTTP",cap.getType()); //@todo should this be some kind of cone thing
		assertNull(cap.getStandardID());
		assertEquals(1,cap.getInterfaces().length);
		
		Interface i = cap.getInterfaces()[0];
		assertEquals("access URL",new AccessURL[] {
					new AccessURL() {{
			setUse("base");
			setValueURI(new URI("http://cadcwww.hia.nrc.ca/cadcbin/cadcConeSearch.pl?CAT=CNOC1"));
		}}
			},i.getAccessUrls());
		assertNull(i.getRole());
		assertEquals("vs:ParamHTTP",i.getType());
		assertNull(i.getVersion());
		assertEmpty(i.getSecurityMethods());
		
		assertTrue(r instanceof DataService); // has coverage, a facility and instrument
		DataService ds = (DataService)r;
		assertNotNull(ds.getCoverage());
		// @todo test the resul of the data service stuff here.
		
		assertTrue(cap instanceof ConeCapability);
		assertSame(cap,((ConeService)s).findConeCapability());
		
		ConeCapability cc = (ConeCapability)cap;
		assertEquals(1.0,cc.getMaxSR(),0.1);
		assertEquals(1000,cc.getMaxRecords());
		assertTrue(cc.isVerbosity());
		assertNull(cc.getTestQuery());


		WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("Cone");
		wt.assertTextPresent("cadcwww");		
	}
	
	public void testConeService2() throws Exception {
		ResourceStreamParser p = parse("cone2.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://uk.ac.cam.ast/first-dsa-test/cone"
				, null
				, "FIRST object catalogue"
				, "ConeSearch");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("");}});
				setEmail("");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setLogoURI(new URI("http://ag03.ast.cam.ac.uk:8080/first-dsa//logo.gif"));
				setName(new ResourceName() {{
					setValue("(should really be dataset creator's name!)");
				}});
			}}	
		}
		, new ResourceName[] {	}
		, new Date[] {}
		, new ResourceName() {{
			setValue("");
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {}
		, new String[] { "catalog"} // nb, note lower casing here.
		,new String[] {}
		, new Relationship[] {}); 
		// services.
		Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals(1,s.getCapabilities().length);
		// little bit uncomforatblae about this still.
		Capability cap = s.getCapabilities()[0];
		assertNull(cap.getDescription());
		assertEquals("vs:ParamHTTP",cap.getType()); //@todo should this be some kind of cone thing
		assertNull(cap.getStandardID());
		assertEquals(1,cap.getInterfaces().length);
		
		Interface i = cap.getInterfaces()[0];
		assertEquals("access URL",new AccessURL[] {
					new AccessURL() {{
			setUse("base");
			setValueURI(new URI("http://ag03.ast.cam.ac.uk:8080/first-dsa/SubmitCone?"));
		}}
			},i.getAccessUrls());
		assertNull(i.getRole());
		assertEquals("vs:ParamHTTP",i.getType());
		assertNull(i.getVersion());
		assertEmpty(i.getSecurityMethods());

		assertFalse(r instanceof DataService); 
		
		assertTrue(cap instanceof ConeCapability);
		assertSame(cap,((ConeService)s).findConeCapability());
		
		ConeCapability cc = (ConeCapability)cap;
		assertEquals(180.0,cc.getMaxSR(),0.1);
		assertEquals(50000,cc.getMaxRecords());
		assertFalse(cc.isVerbosity());
		assertNull(cc.getTestQuery());


		WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("Cone");
		wt.assertTextPresent("ast.cam");		
	}
	
	public void testConeService3() throws Exception {
		ResourceStreamParser p = parse("cone3.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://fs.usno/cat/usnob"
				, "USNO-B1"
				, "USNO-B1 Catalogue"
				, "ConeSearch");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("S. Levine");}});
				setEmail("mailto:sel@nofs.navy.mil");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{
					setValue("Monet, D. G., et al.");
				}});
			}}	
		}
		, new ResourceName[] {	}
		, new Date[] { new Date() {{setValue("2005-03-29"); }}}
		, new ResourceName() {{
			setValue("United States Naval Observatory, Flagstaff Station");
			setId(new URI("ivo://fs.usno/org"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"astrometric catalogue","optical astronomy", "finding charts","photographic images"}
		, new String[] { "catalog","basicdata","photographic"} // nb, note lower casing here.
		,new String[] {"secondary education","community college","university","research","amateur"}
		, new Relationship[] {}); 
		// services.
		Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals(1,s.getCapabilities().length);
		// little bit uncomforatblae about this still.
		Capability cap = s.getCapabilities()[0];
		assertNull(cap.getDescription());
		assertEquals("vs:ParamHTTP",cap.getType()); //@todo should this be some kind of cone thing
		assertNull(cap.getStandardID());
		assertEquals(1,cap.getInterfaces().length);
		
		Interface i = cap.getInterfaces()[0];
		assertEquals("access URL",new AccessURL[] {
					new AccessURL() {{
				setUse("base");
				setValueURI(new URI("http://www.nofs.navy.mil/cgi-bin/vo_cone.cgi?CAT=USNO-B1&"));
				}}	
			},i.getAccessUrls());
		assertNull(i.getRole());
		assertEquals("vs:ParamHTTP",i.getType());
		assertNull(i.getVersion());
		assertEmpty(i.getSecurityMethods());
		// this interface also has qtype and resultType
		
		assertTrue(cap instanceof ConeCapability);
		assertSame(cap,((ConeService)s).findConeCapability());
		
		ConeCapability cc = (ConeCapability)cap;
		assertEquals(1.0,cc.getMaxSR(),0.1);
		assertEquals(100000,cc.getMaxRecords());
		assertTrue(cc.isVerbosity());
		assertNull(cc.getTestQuery());

		// this entry also has a coverage block.
		assertTrue(r instanceof DataService); // has coverage,
		DataService ds = (DataService)r;
		assertNotNull(ds.getCoverage());
		// @todo test the resul of the data service stuff here.
			

		WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("nofs.navy");		
	}
	
	
	public void testConeService4() throws Exception {
		ResourceStreamParser p = parse("cone4.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		//@todo fill in resource, curation and content checking.
		/*
		checkResource(r
				, "ivo://fs.usno/cat/usnob"
				, "USNO-B1"
				, "USNO-B1 Catalogue"
				, "ConeSearch");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("S. Levine");}});
				setEmail("mailto:sel@nofs.navy.mil");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{
					setValue("Monet, D. G., et al.");
				}});
			}}	
		}
		, new ResourceName[] {	}
		, new Date[] { new Date() {{setValue("2005-03-29"); }}}
		, new ResourceName() {{
			setValue("United States Naval Observatory, Flagstaff Station");
			setId(new URI("ivo://fs.usno/org"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"astrometric catalogue","optical astronomy", "finding charts","photographic images"}
		, new String[] { "catalog","basicdata","photographic"} // nb, note lower casing here.
		,new String[] {"secondary education","community college","university","research","amateur"}
		, new Relationship[] {});
		*/ 
		// services.
		Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals(1,s.getCapabilities().length);
		// little bit uncomforatblae about this still.
		Capability cap = s.getCapabilities()[0];
		assertNull(cap.getDescription());
		assertEquals("vs:ParamHTTP",cap.getType()); //@todo should this be some kind of cone thing
		assertNull(cap.getStandardID());
		assertEquals(1,cap.getInterfaces().length);
		
		Interface i = cap.getInterfaces()[0];
		assertEquals("access URL",new AccessURL[] {
					new AccessURL() {{
				setUse("base");
				setValueURI(new URI("http://nedwww.ipac.caltech.edu/cgi-bin/nph-NEDobjsearch?search_type=Near+Position+Search&of=xml_main&"));
				}}	
			},i.getAccessUrls());
		assertNull(i.getRole());
		assertEquals("vs:ParamHTTP",i.getType());
		assertNull(i.getVersion());
		assertEmpty(i.getSecurityMethods());
		// this interface also has qtype and resultType
		
		assertTrue(cap instanceof ConeCapability);
		assertSame(cap,((ConeService)s).findConeCapability());
		
		ConeCapability cc = (ConeCapability)cap;
		assertEquals(5.0,cc.getMaxSR(),0.1);
		assertEquals(3000,cc.getMaxRecords());
		assertFalse(cc.isVerbosity());
		assertNull(cc.getTestQuery());

		// this entry also has a coverage block.
		assertTrue(r instanceof DataService); // has coverage,
		DataService ds = (DataService)r;
		assertNotNull(ds.getCoverage());
		// @todo test the resul of the data service stuff here.
			

		WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("ipac.caltech");		
	}

	public void testConeService5() throws Exception {
		ResourceStreamParser p = parse("cone5.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		//@todo fill in resource, curation and content checking.
		/*
		checkResource(r
				, "ivo://fs.usno/cat/usnob"
				, "USNO-B1"
				, "USNO-B1 Catalogue"
				, "ConeSearch");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("S. Levine");}});
				setEmail("mailto:sel@nofs.navy.mil");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{
					setValue("Monet, D. G., et al.");
				}});
			}}	
		}
		, new ResourceName[] {	}
		, new Date[] { new Date() {{setValue("2005-03-29"); }}}
		, new ResourceName() {{
			setValue("United States Naval Observatory, Flagstaff Station");
			setId(new URI("ivo://fs.usno/org"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"astrometric catalogue","optical astronomy", "finding charts","photographic images"}
		, new String[] { "catalog","basicdata","photographic"} // nb, note lower casing here.
		,new String[] {"secondary education","community college","university","research","amateur"}
		, new Relationship[] {});
		*/ 
		// services.
		Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals(1,s.getCapabilities().length);
		// little bit uncomforatblae about this still.
		Capability cap = s.getCapabilities()[0];
		assertNull(cap.getDescription());
		assertEquals("q2:ParamHTTP",cap.getType()); //@todo should this be some kind of cone thing
		assertNull(cap.getStandardID());
		assertEquals(1,cap.getInterfaces().length);
		
		Interface i = cap.getInterfaces()[0];
		assertEquals("access URL",new AccessURL[] {
					new AccessURL() {{
				setValueURI(new URI("http://casjobs.sdss.org/vo/dr4cone/sdssConeSearch.asmx/ConeSearch?"));
				}}	
			},i.getAccessUrls());
		assertNull(i.getRole());
		assertEquals("q2:ParamHTTP",i.getType());
		assertNull(i.getVersion());
		assertEmpty(i.getSecurityMethods());
		
		assertFalse(r instanceof DataService);
			
		assertTrue(cap instanceof ConeCapability);
		assertSame(cap,((ConeService)s).findConeCapability());
		
		ConeCapability cc = (ConeCapability)cap;
		assertEquals(1.0,cc.getMaxSR(),0.1);
		assertEquals(5000,cc.getMaxRecords());
		assertFalse(cc.isVerbosity());
		assertNull(cc.getTestQuery());


		WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("casjobs.sdss");	
		wt.assertTextPresent("5000"); //@todo get this output.
	}
	
	public void testConeService6() throws Exception {
		ResourceStreamParser p = parse("cone6.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		//@todo fill in resource, curation and content checking.
		/*
		checkResource(r
				, "ivo://fs.usno/cat/usnob"
				, "USNO-B1"
				, "USNO-B1 Catalogue"
				, "ConeSearch");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("S. Levine");}});
				setEmail("mailto:sel@nofs.navy.mil");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{
					setValue("Monet, D. G., et al.");
				}});
			}}	
		}
		, new ResourceName[] {	}
		, new Date[] { new Date() {{setValue("2005-03-29"); }}}
		, new ResourceName() {{
			setValue("United States Naval Observatory, Flagstaff Station");
			setId(new URI("ivo://fs.usno/org"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"astrometric catalogue","optical astronomy", "finding charts","photographic images"}
		, new String[] { "catalog","basicdata","photographic"} // nb, note lower casing here.
		,new String[] {"secondary education","community college","university","research","amateur"}
		, new Relationship[] {});
		*/ 
		// services.
		Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals(1,s.getCapabilities().length);
		// little bit uncomforatblae about this still.
		Capability cap = s.getCapabilities()[0];
		assertNull(cap.getDescription());
		assertEquals("q2:ParamHTTP",cap.getType()); //@todo should this be some kind of cone thing
		assertNull(cap.getStandardID());
		assertEquals(1,cap.getInterfaces().length);
		
		Interface i = cap.getInterfaces()[0];
		assertEquals("access URL",new AccessURL[] {
					new AccessURL() {{
				setValueURI(new URI("http://archive.stsci.edu/euve/search.php?"));
				}}	
			},i.getAccessUrls());
		assertNull(i.getRole());
		assertEquals("q2:ParamHTTP",i.getType());
		assertNull(i.getVersion());
		assertEmpty(i.getSecurityMethods());
		// this interface also has qtype and resultType
		
		assertFalse(r instanceof DataService);
		
		assertTrue(cap instanceof ConeCapability);
		assertSame(cap,((ConeService)s).findConeCapability());
		
		ConeCapability cc = (ConeCapability)cap;
		assertEquals(180.0,cc.getMaxSR(),0.1);
		assertEquals(100,cc.getMaxRecords());
		assertFalse(cc.isVerbosity());
		assertNull(cc.getTestQuery());

		// this entry also has a coverage block.

		WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("180.0"); //@todo output this.
		wt.assertTextPresent("archive.stsci");
	}

}
