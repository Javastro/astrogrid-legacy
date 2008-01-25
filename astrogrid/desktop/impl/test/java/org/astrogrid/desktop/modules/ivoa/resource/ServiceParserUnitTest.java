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
import org.astrogrid.acr.ivoa.resource.Validation;
import org.astrogrid.acr.ivoa.resource.WebServiceInterface;

/** Tests parsing of service resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:51:20 PM
 */
public class ServiceParserUnitTest extends AbstractTestForParser {

	public void testServices() throws Exception {
		ResourceStreamParser p = parse("multiple.xml");
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
		validateResource(arr[0]);
		Service s = validateService(arr[0]);
		assertEquals("no capabilities",1,s.getCapabilities().length);
		Capability c = s.getCapabilities()[0];
		assertNull(c.getDescription());
		assertNotNull(c.getType());
		assertEquals("sia:SimpleImageAccess",c.getType());
		assertEquals(new URI("ivo://ivoa.net/std/SIA"),c.getStandardID());
		assertEquals(1,c.getInterfaces().length);
		Interface iface = c.getInterfaces()[0];
		validateInterface(iface);
		assertEquals("std",iface.getRole());
		assertEquals("vs:ParamHTTP",iface.getType());
		assertEmpty(iface.getSecurityMethods());
		assertNull(iface.getVersion());
		AccessURL[] accessUrls = iface.getAccessUrls();
		assertEquals(1,accessUrls.length);
		assertEquals("base",accessUrls[0].getUse());
		assertEquals("http://skyview.gsfc.nasa.gov/cgi-bin/vo/sia.pl?survey=RASS&",accessUrls[0].getValueURI().toString());
		//
		assertFalse(arr[1] instanceof Service);		
		assertTrue(arr[2] instanceof Service);
        assertFalse(arr[3] instanceof Service);
		
	}
	
	public void testService1() throws Exception {
		ResourceStreamParser p = parse("webpageService.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://org.astrogrid.regtest/QueryPage.html"
				, "ADIL"
				, "NCSA Astronomy Digital Image Library Search Page"
				, "Service");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("ADIL Librarian");}});
				setEmail("adil@ncsa.uiuc.edu");
			}}
		}
		, new Creator[] {
		    new Creator() {{
		        setLogoURI(new URI("http://adil.ncsa.uiuc.edu/gifs/adilfooter.gif"));
		        setName(new ResourceName(){{setValue("Dr. Raymond Plante");}});
		    }}
		}
		, new ResourceName[] {		}
		, new Date[] {
		    new Date() {{
		        setRole("created");
		        setValue("1994-09-01");
		    }}
		    
		}
		, new ResourceName() {{
			setValue("NCSA Astronomy Digital Image Library (ADIL)");
			setId(new URI("ivo://adil.ncsa/adil"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"data repositories","digital libraries"}
		, new String[] { "archive"} // nb, note lower casing here.
		,new String[] {"university","research","community college"}
		, new Relationship[] {
		    new Relationship() {{
		        setRelationshipType("service-for");
		        setRelatedResources(new ResourceName[]{
		                new ResourceName() {{
		                    setValue("NCSA Astronomy Digital Image Library");
		                    setId(new URI("ivo://adil.ncsa/adil"));
		                }}
		        });
		    }}
		}); 
		
		Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals("no capabilities",1,s.getCapabilities().length);
		Capability cap = s.getCapabilities()[0];
		
		assertNull(cap.getDescription());
		assertNull(cap.getStandardID());
		assertNull(cap.getType());
		
		assertEquals(1,cap.getInterfaces().length);
		
		Interface i = cap.getInterfaces()[0];
		validateInterface(i);
		assertNull(i.getRole());
		assertNull(i.getVersion());
		assertEmpty(i.getSecurityMethods());
		assertEquals("vr:WebBrowser",i.getType());

		assertEquals("access URL",new AccessURL[] {
					new AccessURL() {{
			setValueURI(new URI("http://adil.ncsa.uiuc.edu/QueryPage.html"));
		}}
			},i.getAccessUrls());
		
		
		WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("Service");
		wt.assertTextPresent(i.getAccessUrls()[0].getValueURI().toString());
	}

	public void testService2() throws Exception {
		ResourceStreamParser p = parse("conetestService.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://org.astrogrid.regtest/validater/ConeSearch"
				, "CSValidater"
				, "Cone Search Validation Service"
				, "Service");
		Validation[] validationLevel = r.getValidationLevel();
		assertEquals(1,validationLevel.length);
		assertEquals(1,validationLevel[0].getValidationLevel());
		assertEquals("ivo://nvo.ncsa/registry",validationLevel[0].getValidatedBy().toString());
		
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Ray Plante");}});
				setEmail("rplante@ncsa.uiuc.edu");
			}}
		}
		, new Creator[] {
			new Creator() {{
			    setName(new ResourceName() {{
			        setValue("Dr. Raymond Plante");
			    }});
			}}
		}
		, new ResourceName[] {		}
		, new Date[] {
		    new Date() {{
		        setRole("created");
		        setValue("1994-09-01");
		    }}
		}
		, new ResourceName() {{
			setValue("The NVO Project at NCSA");
			setId(new URI("ivo://nvo.ncsa/NVO"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"services","standards"}
		,new String[] {"other"}
		, new String[] { } // nb, note lower casing here.
		, new Relationship[] {
		
		}); 
		
		Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals("no capabillities",1,s.getCapabilities().length);
		Capability cap = s.getCapabilities()[0];
		assertNotNull(cap.getDescription());
		assertNull(cap.getStandardID());
		assertNull(cap.getType());

		assertEquals(2,cap.getInterfaces().length);
		
		Interface i = cap.getInterfaces()[0];
		validateInterface(i);		
		assertTrue("Expected a web service interface",i instanceof WebServiceInterface);
		assertEquals("access URL",new AccessURL[] {
					new AccessURL() {{
			setValueURI(new URI("http://nvo.ncsa.uiuc.edu:8081/validate/ConeSearchValidater"));
		}}
			},i.getAccessUrls());
		assertNull(i.getRole());
		assertEmpty(i.getSecurityMethods());
		assertEquals("vr:WebService",i.getType());
		assertNull(i.getVersion());
		WebServiceInterface wi = (WebServiceInterface)i;
		assertNotNull("wsdlurls should not be null",wi.getWsdlURLs());
		assertEquals(1,wi.getWsdlURLs().length);
		assertEquals(new URI("http://nvo.ncsa.uiuc.edu/VO/services/ConeSearchValidater.wsdl"),wi.getWsdlURLs()[0]);
					
        Interface i1 = cap.getInterfaces()[1];
        validateInterface(i1);       
        assertEquals("access URL",new AccessURL[] {
                    new AccessURL() {{
            setValueURI(new URI("http://nvo.ncsa.uiuc.edu/VO/services/csvalidate.html"));
        }}
            },i1.getAccessUrls());
        assertNull(i1.getRole());
        assertEmpty(i1.getSecurityMethods());
        assertEquals("vr:WebBrowser",i1.getType());
        assertNull(i1.getVersion());		
		
		WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("Service");
		wt.assertTextPresent(cap.getDescription());
		wt.assertTextPresent(i.getAccessUrls()[0].getValueURI().toString());
		wt.assertTextPresent(wi.getWsdlURLs()[0].toString()); // wsdl urls.
	}
	
	// interesting becfause it's a service with _no_ capabilities.
	public void testService3() throws Exception {
        ResourceStreamParser p = parse("octetService.xml");
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
                setAddress("5071 West Saanich Rd, Victoria, BC, Canada, V9E 2E7");
                setEmail("patrick.dowler@nrc-cnrc.gc.ca");
            }}
        }
        , new Creator[] {
            new Creator() {{
                setName(new ResourceName());
                setLogoURI(new URI("http://www.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/cvo/images/cvo.png"));
            }}
        }
        , new ResourceName[] {      }
        , new Date[] {
        }
        , new ResourceName() {{
            setValue("Canadian Astronomy Data Centre");
            setId(new URI("ivo://cadc.nrc.ca/org"));
            }}
        , null);
        
        checkContent(r.getContent()
                , new String[] {}
        , new String[] { "other"}
        ,new String[] {"research"}
        , new Relationship[] {
            new Relationship() {{
                setRelationshipType("service-for");
                setRelatedResources(new ResourceName[] {
                        new ResourceName() {{
                            setValue("Canadian Virtual Observatory");
                            setId(new URI("ivo://cadc.nrc.ca/cvo"));
                        }}
                });
            }}
        }); 
        
        Service s = validateService(r);
        assertEmpty(s.getRights());
        assertEquals(1,s.getCapabilities().length);
        // but it's all null.
        
        
        WebTester wt = basicResourceRendererTests(s);
        wt.assertTextPresent("Service");
        wt.assertTextPresent("cadc");
    }
	

    public void testService4() throws Exception {
        ResourceStreamParser p = parse("vospaceService.xml");
        Resource r =assertOnlyOne(p);
        validateResource(r);    
        checkResource(r
                , "ivo://org.astrogrid.regtest/vospace-service"
                , null
                , "VOSpace/MYSpace test service"
                , "Service");
      
        
        Service s = validateService(r);
        assertEmpty(s.getRights());
        assertEquals("no capabillities",3,s.getCapabilities().length);
        Capability cap = s.getCapabilities()[0];
        // ust want to verify that my parser works with URI fragments - nothing else novel in this resource.
        assertEquals("ivo://org.astrogrid/std/myspace/v1.0#myspace",cap.getStandardID().toString());

        assertEquals(1,cap.getInterfaces().length);
        

    }


}
