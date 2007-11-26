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
		ResourceStreamParser p = parse("authority1.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://adil.ncsa"
				, "ADIL"
				, "ADIL Naming Authority"
				, "Authority");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Dr. Raymond Plante");}});
				setEmail("rai@ncsa.uiuc.edu");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setLogoURI(new URI("http://rai.ncsa.uiuc.edu/images/RAILogo.jpg"));
				setName(new ResourceName() {{setValue("Dr. Raymond Plante");}});
			}}
		}
		, new ResourceName[] {
			new ResourceName() {{setValue("Raymond Plante");}}
		}
		, new Date[] {	}
		, new ResourceName() {{
			setValue("NCSA Radio Astronomy Imaging");
			setId(new URI("ivo://rai.ncsa/RAI"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"radio astronomy","data repositories","digital libraries","grid-based processing"}
		, new String[] { "organisation"} // nb, note lower casing here.
		,new String[] {"university","research"}
		, new Relationship[] {}); 
		Authority a = validateAuthority(r);
		assertNull(a.getManagingOrg());
		WebTester tester = basicResourceRendererTests(a);		
		tester.assertTextPresent("Authority");		
	}
	
	public void testAuthority2() throws Exception {
		ResourceStreamParser p = parse("authority2.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://apl.ucl.ac.uk"
				, null
				, "Community Authority"
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
			setValue("Community Publisher");
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"the community authority for astrogrid.org"}
		, new String[] { "archive"}
		,new String[] {}
		, new Relationship[] {});
		Authority a = validateAuthority(r);
		assertNull(a.getManagingOrg());	
		
		WebTester tester = basicResourceRendererTests(a);
		tester.assertTextPresent("Authority");		
		
	}
		
	
	public void testAuthority3() throws Exception {
		ResourceStreamParser p = parse("authority3.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://archive.stsci.edu"
				, "STScI ARC"
				, "Space Telescope Science Institute Archive"
				, "Authority");
		
		checkCuration(r.getCuration()
				, new Contact[] {		}
		, new Creator[] {	new Creator() } // hmm should empty records be included? - garbage in, garbage out, I guess
		, new ResourceName[] { new ResourceName()}
		, new Date[] {	}
		, new ResourceName() {{
			setValue("Space Telescope Science Institute");
			}}
		, null);
		
			
		checkContent(r.getContent()
				, new String[] {"hubble space telescope","hst archive"}
		, new String[] {} // nb, note lower casing here.
		,new String[] {"general"}
		, new Relationship[] {}); 
		Authority a = validateAuthority(r);
		assertNull(a.getManagingOrg());
		
		WebTester tester = basicResourceRendererTests(a);
		tester.assertTextPresent("Authority");
	}
	
	public void testAuthority4() throws Exception {
		ResourceStreamParser p = parse("authority4.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://esavo"
				, "ESAVO Authority"
				, "ESAVO Authority"
				, "Authority");
		checkCuration(r.getCuration()
				, new Contact[] {}
		, new Creator[] {
			new Creator() {{
				setLogoURI(new URI("http://esavo.esac.esa.int/img/logo.gif"));
				setName(new ResourceName() {{setValue("ESAVO");}});
			}}
		}
		, new ResourceName[] {}
		, new Date[] {	}
		, new ResourceName() {{
			setValue("ESAVO");
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {}
		, new String[] {} // nb, note lower casing here.
		,new String[] {}
		, new Relationship[] {});
		Authority a = validateAuthority(r);
		assertNotNull(a.getManagingOrg());
		assertEquals(new ResourceName() {{setValue("ESAVO-TEST");}},a.getManagingOrg());
		
		WebTester tester = basicResourceRendererTests(a);
		tester.assertTextPresent("Authority");
		tester.assertTextPresent(a.getManagingOrg().getValue()); 	}
	
	/** check this resource is an authority, and return it as such */
	private Authority validateAuthority(Resource r) {
		assertNotNull("resource is null",r);
		assertTrue("resource is not an authority",r instanceof Authority);
		return (Authority)r;
	}
}
