/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import net.sourceforge.jwebunit.WebTester;

import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Organisation;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;

/** Tests parsing of organisation resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:51:20 PM
 */
public class OrganisationParserUnitTest extends AbstractTestForParser{


	public void testOrganisation1() throws Exception {
		ResourceStreamParser p = parse("organisation1.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://www.g-vo.org/GAVO"
				, "GAVO"
				, "German Astrophysical Virtual Observatory"
				, "Organisation");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Gerard Lemson");}});
				setEmail("gerard.lemson@mpe.mpg.de");
				setTelephone("+49-(0)89-300003316");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{setValue("Gerard Lemson");}});
			}}
		}
		, new ResourceName[] {		}
		, new Date[] {	new Date() {{setValue("2006-06-02");}}}
		, new ResourceName() {{
			setValue("German Astrophysical Virtual Observatory");
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"virtual observatory"}
		, new String[] { "organisation"} // nb, note lower casing here.
		,new String[] {"research"}
		, new Relationship[] {}); 
		Organisation o = validateOrganisation(r);
		assertEmpty(o.getFacilities());
		assertEmpty(o.getInstruments());
		
		WebTester wt = basicResourceRendererTests(o);
		wt.assertTextPresent("Organisation");
	}
	


	public void testOrganisation2() throws Exception {
		ResourceStreamParser p = parse("organisation2.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://wfau.roe.ac.uk/WFAU"
				, "WFAU"
				, "Wide Field Astronomy Unit, Institute for Astronomy, Edinburgh"
				, "Organisation");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Dr. Peredur Williams");}});
				setEmail("pmw@roe.ac.uk");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{
					setValue("Dr. John Taylor");
					setLogoURI(new URI("http://surveys.roe.ac.uk/wsa/wsa_bottom.gif"));
					}});
			}}
		}
		, new ResourceName[] {		}
		, new Date[] {	}
		, new ResourceName() {{
			setValue("Wide Field Astronomy Unit");
			setId(new URI("ivo://wfau.roe.ac.uk/WFAU"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"data repositories","digital libraries","grid-based processing"}
		, new String[] { "organisation"} // nb, note lower casing here.
		,new String[] {"university","research"}
		, new Relationship[] {}); 
		Organisation o = validateOrganisation(r);
		assertEmpty(o.getFacilities());
		assertEmpty(o.getInstruments());
		
		WebTester wt = basicResourceRendererTests(o);
		wt.assertTextPresent("Organisation");		
	}
	

	public void testOrganisation3() throws Exception {
		ResourceStreamParser p = parse("organisation3.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://stecf.euro-vo/ST-ECF"
				, "ST-ECF"
				, "ST-ECF group"
				, "Organisation");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Diego Sforna");}});
				setEmail("dsforna@eso.org");
			}}
		}
		, new Creator[] {}
		, new ResourceName[] {		}
		, new Date[] {	}
		, new ResourceName() {{
			setValue("ST-ECF");
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {}
		, new String[] {"archive", "organisation"} // nb, note lower casing here.
		,new String[] {}
		, new Relationship[] {}); 
		Organisation o = validateOrganisation(r);
		assertEquals(new ResourceName[] {
				new ResourceName() {{
					setValue("Space Telescope European Coordinating Facility");
					setId(new URI("ivo://stecf.euro-vo/"));
				}}
			},o.getFacilities());
		assertEmpty(o.getInstruments());
		
		WebTester wt = basicResourceRendererTests(o);
		wt.assertTextPresent("Organisation");
		wt.assertTextPresent(o.getFacilities()[0].getValue());
	}

	public void testOrganisation4() throws Exception {
		ResourceStreamParser p = parse("organisation4.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://nvo.caltech/B/4"
				, "ASS"
				, "Aspen Summer School"
				, "Organisation");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{
					setValue("NULL");
					setId(new URI("ivo://x.x/"));
					}});
				setEmail("NULL");
				setAddress("NULL");
				setTelephone("NULL");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setLogoURI(new URI(""));
				setName(new ResourceName() {{
			setValue("NULL");
			setId(new URI("ivo://x.x/"));
			}});
			}}
		}
		, new ResourceName[] {
			new ResourceName() {{
				setValue("NULL");
				setId(new URI("ivo://x.x/"));
				}}
			,new ResourceName() {{
				setValue("NULL");
				setId(new URI("ivo://x.x/"));
				}}
		}
		, new Date[] {	new Date() {{
			setRole("representative");
			setValue("2004-09-15");
			
		}}}
		, new ResourceName() {{
			setValue("NULL");
			setId(new URI("ivo://x.x/"));
			}}
		, "NULL");
		
		checkContent(r.getContent()
				, new String[] {"null"}
		, new String[] {"other"} // nb, note lower casing here.
		,new String[] {"general"}
		, new Relationship[] {
			new Relationship() {{
				setRelationshipType("mirror-of");
				setRelatedResources(new ResourceName[] {
						new ResourceName() {{
							setValue("NULL");
							setId(new URI("ivo://x.x/"));
							}}
				});
			}}
			
		}); 
		Organisation o = validateOrganisation(r);
		assertEquals(new ResourceName[] {
				new ResourceName() {{
					setValue("NULL");
					setId(new URI("ivo://x.x/"));
					}}
			},o.getFacilities());
		assertEquals(new ResourceName[] {
				new ResourceName() {{
					setValue("NULL");
					setId(new URI("ivo://x.x/"));
					}}
			},o.getInstruments());
		
		WebTester wt = basicResourceRendererTests(o);
		wt.assertTextPresent("Organisation");
		wt.assertTextPresent(o.getFacilities()[0].getValue());// todo refine
		wt.assertTextPresent(o.getInstruments()[0].getValue());
	}
	
	/** check this resource is an authority, and return it as such */
	private Organisation validateOrganisation(Resource r) {
		assertNotNull("resource is null",r);
		assertTrue("resource is not an organisation",r instanceof Organisation);
		return (Organisation)r;
	}


}
