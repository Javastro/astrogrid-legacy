/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import net.sourceforge.jwebunit.WebTester;

import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.InputParam;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.ParamHttpInterface;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.acr.ivoa.resource.SimpleDataType;
import org.astrogrid.acr.ivoa.resource.SiapCapability.ImageSize;
import org.astrogrid.acr.ivoa.resource.SiapCapability.SkyPos;

/** test parsing of siap resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 20079:19:31 PM
 */
public class SiapParserUnitTest extends AbstractTestForParser {

    public void testSiapService1() throws Exception {
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
                setName(new ResourceName() {{
                    setValue("Max Planck Institute for Exterrestrial Physics (Garching FRG)");
                }});
            }}  
        }
        , new ResourceName[] {
            new ResourceName() {{
                setValue("Skyview Project");
            }}
        }
        , new Date[] {
           new Date() {{
               setRole("created");
               setValue("2006-03-27");
           }}
        }
        , new ResourceName() {{
            setValue("NASA/GSFC HEASARC");
            setId(new URI("ivo://nasa.heasarc"));
            }}
        , null);
        
        checkContent(r.getContent()
                , new String[] {"surveys"}
        , new String[] { "archive"} 
        ,new String[] {"research"}
        , new Relationship[] {
            new Relationship() {{
                setRelationshipType("service-for");
                setRelatedResources(new ResourceName[]{
                        new ResourceName() {{
                            setValue("NASA/GSFC Exploration of the Universe Division");
                            setId(new URI("ivo://nasa.heasarc/eud"));
                        }}
                });
            }}
            
        }); 
        // services.
        Service s = validateService(r);
        assertEmpty(s.getRights());
        assertEquals(1,s.getCapabilities().length);
        
        assertTrue(" not an instanceof catalog service",  s instanceof CatalogService);
        CatalogService catS =(CatalogService)s;
        assertEmpty(catS.getFacilities());
        assertEmpty(catS.getInstruments());
        assertEmpty(catS.getTables());
        
        Capability cap = s.getCapabilities()[0];
        checkCapability(cap,"ivo://ivoa.net/std/SIA","SimpleImageAccess",1);
        
        assertTrue(cap instanceof SiapCapability);
        assertTrue(r instanceof SiapService);
        assertSame(cap,((SiapService)s).findSiapCapability());
        //
        org.astrogrid.acr.ivoa.resource.SiapCapability.Query q = new SiapCapability.Query();
        q.setPos(new SkyPos() {{
            setLat(20);
            setLong(120);
        }});
        q.setSize(new SiapCapability.SkySize() {{
            setLong(1);
            setLat(1);
        }});
        SiapCapability.SkySize imageExtent = new SiapCapability.SkySize() {{
            setLat(180);
            setLong(360);            
        }};
        ImageSize imageSize = new SiapCapability.ImageSize() {{
            setLong(5000);
            setLat(5000);
        }};
        SiapCapability.SkySize queryRegion = new SiapCapability.SkySize() {{
            setLat(180);
            setLong(360);
        }};
        checkSiapCapability(cap,"cutout"
                ,10000000
                ,imageExtent
                ,imageSize
                ,queryRegion
                ,500
                ,q
                );
        
        Interface i = cap.getInterfaces()[0];
        checkInterface(i,"std","vs:ParamHTTP",null,new SecurityMethod[0],new AccessURL[]{
                new AccessURL() {{
                    setUse("base");
                    setValueURI(new URI("http://skyview.gsfc.nasa.gov/cgi-bin/vo/sia.pl?survey=RASS&"));
                }}
        });
        assertTrue(i instanceof ParamHttpInterface);
        ParamHttpInterface phi = (ParamHttpInterface)i;
        assertEquals("get",phi.getQueryType());
        assertEquals("text/xml+votable",phi.getResultType());
        InputParam[] params = phi.getParams();
        assertNotNull(params);
        assertEquals(5,params.length);
        InputParam param = params[2];
        assertNotNull(param);
        SimpleDataType d= param.getDataType();
        assertNotNull("no datatype",d);
        assertNull(d.getArraysize());
        assertEquals("string",d.getType());
        assertEquals("FORMAT",param.getName());
        assertNull(param.getUcd());
        assertNull(param.getUnit());
        assertNull(param.getUse());
        assertFalse(param.isStandard());

        Coverage coverage = catS.getCoverage();
        checkCoverage(coverage,null,true,true,new String[]{"x-ray"});
        
        SiapCapability scap = (SiapCapability)cap;
        WebTester wt = basicResourceRendererTests(s);
        wt.assertTextPresent("Image");
        wt.assertTextPresent(scap.getImageServiceType());
        wt.assertTextPresent("" + scap.getMaxFileSize());
        wt.assertTextPresent("" + scap.getMaxRecords());
        wt.assertTextPresent("" + queryRegion.getLat());
        wt.assertTextPresent("" + imageSize.getLat());
        wt.assertTextPresent("" + imageExtent.getLong());
    }
    
    public void testSiapService2() throws Exception {
        ResourceStreamParser p = parse("cadcSiap.xml");
        Resource r =assertOnlyOne(p);
        validateResource(r);    
        checkResource(r
                , "ivo://cadc.nrc.ca/siap/jcmt"
                , "CADC/JCMT"
                , "CADC/JCMT Image Search"
                , "CatalogService");
        checkCuration(r.getCuration()
                , new Contact[] {
            new Contact() {{
                setName(new ResourceName() {{setValue("Patrick Dowler");}});
                setEmail("patrick.dowler@nrc-cnrc.gc.ca");
                setAddress("5071 West Saanich Rd, Victoria, BC, Canada, V9E 2E7");
            }}
        }
        , new Creator[] {
            new Creator() {{
                setName(new ResourceName() {{
                                }});
                setLogoURI(new URI("http://www.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/cvo/images/cvo.png"));
            }}  
        }
        , new ResourceName[] {
        }
        , new Date[] {
        }
        , new ResourceName() {{
            setValue("Canadian Astronomy Data Centre");
            setId(new URI("ivo://cadc.nrc.ca/org"));
            }}
        , null);
        
        checkContent(r.getContent()
                , new String[] {}
        , new String[] { "archive"} 
        ,new String[] {"research"}
        , new Relationship[] {
            new Relationship() {{
                setRelationshipType("service-for");
                setRelatedResources(new ResourceName[]{
                        new ResourceName() {{
                            setValue("James Clerk Maxwell Telescope Archive at CADC");
                            setId(new URI("ivo://cadc.nrc.ca/archive/jcmt"));
                        }}
                });
            }}
            
        }); 
        // services.
        Service s = validateService(r);
        assertEmpty(s.getRights());
        assertEquals(1,s.getCapabilities().length);
        
        assertTrue(" not an instanceof catalog service",  s instanceof CatalogService);
        CatalogService catS =(CatalogService)s;
        assertEmpty(catS.getFacilities());
        assertEmpty(catS.getInstruments());
        assertEmpty(catS.getTables());

        
        Capability cap = s.getCapabilities()[0];
        checkCapability(cap,"ivo://ivoa.net/std/SIA","SimpleImageAccess",1);
        
        assertTrue(cap instanceof SiapCapability);
        assertTrue(r instanceof SiapService);
        assertSame(cap,((SiapService)s).findSiapCapability());
        //
        org.astrogrid.acr.ivoa.resource.SiapCapability.Query q = new SiapCapability.Query();
        q.setPos(new SkyPos() {{
            setLat(20);
            setLong(120);
        }});
        q.setSize(new SiapCapability.SkySize() {{
            setLong(1);
            setLat(1);
        }});
        SiapCapability.SkySize imageExtent = new SiapCapability.SkySize() {{
            setLat(45);
            setLong(45);            
        }};
        ImageSize imageSize = new SiapCapability.ImageSize() {{
            setLong(324000);
            setLat(324000);
        }};
        SiapCapability.SkySize queryRegion = new SiapCapability.SkySize() {{
            setLat(45);
            setLong(45);
        }};
        checkSiapCapability(cap,"pointed"
                ,500000
                ,imageExtent
                ,imageSize
                ,queryRegion
                ,0
                ,q
                );
        
        Interface i = cap.getInterfaces()[0];
        checkInterface(i,"std","vs:ParamHTTP",null,new SecurityMethod[0],new AccessURL[]{
                new AccessURL() {{
                    setUse("base");
                    setValueURI(new URI("http://www.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/ivoa/JCMT/siapQuery?"));
                }}
        });
        assertTrue(i instanceof ParamHttpInterface);
        ParamHttpInterface phi = (ParamHttpInterface)i;
        assertEquals("get",phi.getQueryType());
        assertEquals("application/xml+votable",phi.getResultType());
        InputParam[] params = phi.getParams();
        assertNotNull(params);
        assertEquals(0,params.length);


        Coverage coverage = catS.getCoverage();
        checkCoverage(coverage,null,true,true,new String[]{"millimeter"});
        
        SiapCapability scap = (SiapCapability)cap;
        WebTester wt = basicResourceRendererTests(s);
        wt.assertTextPresent("Image");
        wt.assertTextPresent(scap.getImageServiceType());
        wt.assertTextPresent("" + scap.getMaxFileSize());
        wt.assertTextPresent("" + scap.getMaxRecords());
        wt.assertTextPresent("" + queryRegion.getLat());
        wt.assertTextPresent("" + imageSize.getLat());
        wt.assertTextPresent("" + imageExtent.getLong());
        wt.assertTextPresent(phi.getQueryType());
        wt.assertTextPresent(phi.getResultType());        
    }

}
