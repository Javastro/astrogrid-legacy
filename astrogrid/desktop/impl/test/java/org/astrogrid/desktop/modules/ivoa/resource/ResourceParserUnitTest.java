/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;

/** tests for basic resource type.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:45:09 PM
 */
public class ResourceParserUnitTest extends AbstractTestForParser {

	public void testSingleResource() throws Exception{
		ResourceStreamParser p = parse("frass.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);
		checkResource(r
				, "ivo://nasa.heasarc/skyview/rass"
				, "RASS"
				, "ROSAT All-Sky X-ray Survey 1.5 keV"
				, "CatalogService");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("SkyView Help");}});
				setEmail("Skyview@skyview.gsfc.nasa.gov");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{setValue("Max Planck Institute for Exterrestrial Physics (Garching FRG)");}});
			}}
		}
		, new ResourceName[] {
		    new ResourceName() {{
		        setValue("Skyview Project");
		    }}
		}, new Date[] {
			new Date() {{
				setValue("2006-03-27");
				setRole("created");
			}}
		}
		, new ResourceName() {{
		    setValue("NASA/GSFC HEASARC");
		    setId(URI.create("ivo://nasa.heasarc"));
		    }}
		, null);

		checkContent(r.getContent()
				, new String[] {"surveys"}
		, new String[] { "archive"} // nb, note lower casing here.
		,new String[] { "research"}
		, new Relationship[] {
		    new Relationship() {{
		        setRelationshipType("service-for");
		        setRelatedResources(new ResourceName[]{
		                new ResourceName() {{
		                    setValue("NASA/GSFC Exploration of the Universe Division");
		                    setId(URI.create("ivo://nasa.heasarc/eud"));
		                }}
		        });
		    }}
		});
		
		basicResourceRendererTests(r);
	}
	
	public void testAnotherSingleResource() throws Exception{
		ResourceStreamParser p = parse("tmpRofROrg.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);		
		checkResource(r
				,"ivo://astrogrid.pub.rofr/organisation"
				,null
				,"Temporary Astrogrid Registry of Registries"
				,"Organisation");
		checkContent(r.getContent()
				,new String[] {"organisation"}
				,new String[] {"organisation"}
				,new String[] {}
				,new Relationship[] {}
				);
		checkCuration(r.getCuration() 
				,new Contact[] {
		            new Contact() {{
		                setEmail("kmb@mssl.ucl.ac.uk");
		                setName(new ResourceName() {{
		                            setValue("Kevin Benson");
		                        }});
		            }}
		}
				, new Creator[] {}
				, new ResourceName[] {}
				,new Date[] {}
				, new ResourceName() {{setValue("MSSL");}}
				, null
				);
		
		basicResourceRendererTests(r);
	}
	



	

}
