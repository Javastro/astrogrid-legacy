/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import net.sourceforge.jwebunit.WebTester;

import org.astrogrid.acr.ivoa.resource.Authority;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;

/** Tests parsing of authority resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:51:20 PM
 */
public class AuthorityParserUnitTest extends AbstractTestForParser{


	
	public void testAuthority1() throws Exception {
		ResourceStreamParser p = parse("heasarcAuthority.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://nasa.heasarc/authority" // was"ivo://adil.ncsa"
				, null
				, "The High Energy Astrophysics Science Archive Research Center"
				, "Authority");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Thomas A. McGlynn");}});
				setEmail("request@athena.gsfc.nasa.gov");
			}}
		}
		, new Creator[] {}
		, new ResourceName[] {}
		, new Date[] { new Date() {{
		       setValue("2004-10-11");
		}}
		}
		, new ResourceName() {{
			setValue("NASA/GSFC HEASARC");
			setId(new URI("ivo://nasa.heasarc/registry"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {}
		, new String[] {} 
		,new String[] {}
		, new Relationship[] {}); 
		Authority a = validateAuthority(r);
        ResourceName org = a.getManagingOrg();
        assertNotNull(org);
        assertEquals("NASA/GSFC HEASARC",org.getValue());
        assertEquals(URI.create("ivo://nasa.heasarc/registry"),org.getId());
		WebTester tester = basicResourceRendererTests(a);		
		tester.assertTextPresent("Authority");	
	      tester.assertTextPresent(a.getManagingOrg().getValue());
	}
	
	public void testAuthority2() throws Exception {
		ResourceStreamParser p = parse("msslAuthority.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://mssl.ucl.ac.uk_full"
				, null
				, "MSSL Full Registry"
				, "Authority");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Kevin Benson");}});
				setEmail("kmb@mssl.ucl.ac.uk");
			}}
		}
		, new Creator[] {	}
		, new ResourceName[] {	}
		, new Date[] {	}
		, new ResourceName() {{
			setValue("MSSL");
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"authority"}
		, new String[] { }
		,new String[] {}
		, new Relationship[] {});
		Authority a = validateAuthority(r);
		ResourceName org = a.getManagingOrg();
		assertNotNull(org);
		assertNull(org.getValue());
		assertEquals(URI.create("ivo://mssl.ucl.ac.uk_full/organisation"),org.getId());
		
		WebTester tester = basicResourceRendererTests(a);
		tester.assertTextPresent("Authority");		

		
	}
	
	/** check this resource is an authority, and return it as such */
	private Authority validateAuthority(Resource r) {
		assertNotNull("resource is null",r);
		assertTrue("resource is not an authority",r instanceof Authority);
		return (Authority)r;
	}
}
