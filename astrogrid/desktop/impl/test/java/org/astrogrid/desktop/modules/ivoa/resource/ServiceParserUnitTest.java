/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import net.sourceforge.jwebunit.WebTester;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.Service;

/** Tests parsing of service resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:51:20 PM
 */
public class ServiceParserUnitTest extends AbstractTestForParser {

	// I'll leave this one in, alghouth it doesn't follow the pattern of my other tests.
	public void testServices() throws Exception {
		ResourceStreamParser p = parse("multiple.xml");
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
		validateResource(arr[0]);
		Service s = validateService(arr[0]);
		assertEquals(1,s.getCapabilities().length);
		Capability c = s.getCapabilities()[0];
		assertNull(c.getDescription());
		assertNotNull(c.getType());
		assertEquals("vs:WebService",c.getType());
		assertEquals(1,c.getInterfaces().length);
		assertNotNull(c.getInterfaces()[0].getAccessUrls()[0].getValueURI());
		
		assertFalse(arr[1] instanceof Service);
		
		assertTrue(arr[2] instanceof Service);
	}
	
	public void testService1() throws Exception {
		ResourceStreamParser p = parse("service1.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://wfau.roe.ac.uk/filestore"
				, null
				, "Astrogrid VO Store"
				, "Service");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("John Taylor");}});
				setEmail("jdt@roe.ac.uk");
			}}
		}
		, new Creator[] {
		}
		, new ResourceName[] {		}
		, new Date[] {}
		, new ResourceName() {{
			setValue("Filestore Service");
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"users vospace"}
		, new String[] { "archive"} // nb, note lower casing here.
		,new String[] {}
		, new Relationship[] {}); 
		
		Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals(1,s.getCapabilities().length);
		Capability cap = s.getCapabilities()[0];
		assertNull(cap.getDescription());
		assertEquals("vs:WebService",cap.getType());
		assertNull(cap.getStandardID());
		assertEquals(1,cap.getInterfaces().length);
		
		Interface i = cap.getInterfaces()[0];
		assertEquals("access URL",new AccessURL[] {
					new AccessURL() {{
			setUse("full");
			setValueURI(new URI("http://loki.roe.ac.uk/astrogrid-filestore/services/FileStore"));
		}}
			},i.getAccessUrls());
		assertNull(i.getRole());
		assertEquals("vs:WebService",i.getType());
		assertNull(i.getVersion());
		assertEmpty(i.getSecurityMethods());
		
		
		WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("Service");
		wt.assertTextPresent("loki");
	}

	public void testService2() throws Exception {
		ResourceStreamParser p = parse("service2.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://cadc.nrc.ca/cvo/octet"
				, "Octet"
				, "Octet: CVO Observation Catalog Exploration Tool"
				, "Service");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Patrick Dowler");}});
				setEmail("patrick.dowler@nrc-cnrc.gc.ca");
				setAddress("5071 West Saanich Rd\nVictoria, BC, Canada\nV9E 2E7");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setLogoURI(new URI("http://www.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/cvo/images/cvo.png"));
			}}
		}
		, new ResourceName[] {		}
		, new Date[] {}
		, new ResourceName() {{
			setValue("Canadian Astronomy Data Centre");
			setId(new URI("ivo://cadc.nrc.ca/org"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {}
		, new String[] { "other"} // nb, note lower casing here.
		,new String[] {"research"}
		, new Relationship[] {
			new Relationship() {{
				setRelationshipType("service-for");
				setRelatedResources(new ResourceName[] {
						new ResourceName() {{
							setId(new URI("ivo://cadc.nrc.ca/cvo"));
							setValue("Canadian Virtual Observatory");
							
						}}
				});
			}}
		}); 
		
		Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals(1,s.getCapabilities().length);
		Capability cap = s.getCapabilities()[0];
		assertNull(cap.getDescription());
		assertEquals("vs:WebService",cap.getType());
		assertNull(cap.getStandardID());
		assertEquals(1,cap.getInterfaces().length);
		
		Interface i = cap.getInterfaces()[0];
		assertEquals("access URL",new AccessURL[] {
					new AccessURL() {{
			setUse("base");
			setValueURI(new URI("http://www.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/cvoProto/launch.jsp"));
		}}
			},i.getAccessUrls());
		assertNull(i.getRole());
		assertEquals("vs:WebService",i.getType());
		assertNull(i.getVersion());
		assertEmpty(i.getSecurityMethods());
		
		
		WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("Service");
		wt.assertTextPresent("launch.jsp");
	}
		
	

	


}
