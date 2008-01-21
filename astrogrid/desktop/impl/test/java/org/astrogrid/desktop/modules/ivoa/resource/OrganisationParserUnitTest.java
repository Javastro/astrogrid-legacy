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
		ResourceStreamParser p = parse("tmpRofROrg.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://astrogrid.pub.rofr/organisation"
				, null
				, "Temporary Astrogrid Registry of Registries"
				, "Organisation");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Kevin Benson");}});
				setEmail("kmb@mssl.ucl.ac.uk");
			}}
		}
		, new Creator[] {}
		, new ResourceName[] {		}
		, new Date[] {}
		, new ResourceName() {{
			setValue("MSSL");
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"organisation"}
		, new String[] { "organisation"} // nb, note lower casing here.
		,new String[] {}
		, new Relationship[] {}); 
		Organisation o = validateOrganisation(r);
		ResourceName[] facilities = o.getFacilities();
		assertNotNull(facilities);
		assertEquals(1,facilities.length);
		assertEquals("MSSL",facilities[0].getValue());
		assertNull(facilities[0].getId());
		assertEmpty(o.getInstruments());
		
		WebTester wt = basicResourceRendererTests(o);
		wt.assertTextPresent("Organisation");
	}
	


	public void testOrganisation2() throws Exception {
		ResourceStreamParser p = parse("ivoaOrg.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://org.astrogrid.regtest/IVOA"
				, "IVOA"
				, "International Virtual Observatory Alliance"
				, "Organisation");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("IVOA Executive Committee");}});
				setEmail("ivoa@ivoa.net");
			}}
			,            new Contact() {{
                setName(new ResourceName() {{setValue("IVOA Community");}});
                setEmail("interop@ivoa.net");
            }}
		}
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{
					setValue("VO community");
					setLogoURI(new URI("http://www.ivoa.net/icons/ivoa_logo_small.jpg"));
					}});
			}}
		}
		, new ResourceName[]{
		        new ResourceName() {{
		            setValue("Armenian Virtual Observatory");
		        }}
		        ,new ResourceName() {{
		            setValue("AstroGrid");
		        }}
		                   }
		, new Date[] {
		    new Date() {{
		            setValue("2002-06-01");
		    }}
		}
		, new ResourceName() {{
			setValue("International Virtual Observatory Alliance");
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"standards","virtual observatory"}
		, new String[] { "organisation"} // nb, note lower casing here.
		,new String[] {}
		, new Relationship[] {}); 
		Organisation o = validateOrganisation(r);
		assertEmpty(o.getFacilities());
		assertEmpty(o.getInstruments());
		
		WebTester wt = basicResourceRendererTests(o);
		wt.assertTextPresent("Organisation");		
	}
	

	public void testOrganisation3() throws Exception {
		ResourceStreamParser p = parse("heasarcOrg.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://nasa.heasarc/ASD"
				, null
				, "Astrophysics Science Division"
				, "Organisation");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Thomas A. McGlynn");}});
				setEmail("request@athena.gsfc.nasa.gov");
			}}
		}
		, new Creator[] {}
		, new ResourceName[] {		}
		, new Date[] {
		    new Date() {{
		        setValue("2005-01-01");
		    }}
		}
		, new ResourceName() {{
			setValue("NASA/GSFC HEASARC");
			setId(new URI("ivo://nasa.heasarc/registry"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {}
		, new String[] { "organisation"} 
		,new String[] {}
		, new Relationship[] {}); 
		Organisation o = validateOrganisation(r);
		assertEquals(new ResourceName[] {
				new ResourceName() {{
					setValue("HEASARC");
				}}
			},o.getFacilities());
		assertEmpty(o.getInstruments());
		
		WebTester wt = basicResourceRendererTests(o);
		wt.assertTextPresent("Organisation");
		wt.assertTextPresent(o.getFacilities()[0].getValue());
	}

	/** check this resource is an authority, and return it as such */
	private Organisation validateOrganisation(Resource r) {
		assertNotNull("resource is null",r);
		assertTrue("resource is not an organisation",r instanceof Organisation);
		return (Organisation)r;
	}


}
