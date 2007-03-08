/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

import net.sourceforge.jwebunit.WebTester;

import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.Validation;

/** tests for basic resource type.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:45:09 PM
 */
public class ResourceParserUnitTest extends AbstractTestForParser {

	public void testSingleResource() throws Exception{
		ResourceStreamParser p = parse("pegase.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);
		checkResource(r
				, "ivo://org.astrogrid/Pegase"
				, "Pegase"
				, "Pegase App"
				, "CeaApplicationType");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Paul Harrison");}});
				setEmail("pah@jb.man.ac.uk");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setLogoURI(new URI("??"));
				setName(new ResourceName() {{setValue("Astrogrid");}});
			}}
		}
		, new ResourceName[0], new Date[] {
			new Date() {{
				setValue("2004-03-26");
				setRole("representative");
			}}
		}
		, new ResourceName() {{setValue("Astrogrid");}}
		, "1.0");

		checkContent(r.getContent()
				, new String[] {"???"}
		, new String[] { "other"} // nb, note lower casing here.
		,new String[] {}
		, new Relationship[] {});
		
		basicResourceRendererTests(r);
	}
	
	public void testAnotherSingleResource() throws Exception{
		ResourceStreamParser p = parse("resource.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);		
		checkResource(r
				,"ivo://CDS/VizieR/J/ApJ/613/682/table8"
				,"J/ApJ/613/682/ta"
				,"AGN central masses and broad-line region sizes (Peterson+, 2004) - Adopted Virial Products and Derived Black Hole Masses"
				,"Resource");
		checkContent(r.getContent()
				,new String[] {"agn","spectroscopy","redshifts"}
				,new String[] {}
				,new String[] {"research"}
				,new Relationship[] {}
				);
		checkCuration(r.getCuration() 
				,new Contact[] {}
				, new Creator[] {}
				, new ResourceName[] {}
				,new Date[] {}
				, new ResourceName() {{setValue("CDS");}}
				, null
				);
		
		basicResourceRendererTests(r);
	}
	



	

}
