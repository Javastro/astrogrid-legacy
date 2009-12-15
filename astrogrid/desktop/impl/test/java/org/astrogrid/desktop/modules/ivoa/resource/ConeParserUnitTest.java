/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import net.sourceforge.jwebunit.WebTester;

import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.ConeService;
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
import org.astrogrid.acr.ivoa.resource.SimpleDataType;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.acr.ivoa.resource.TableDataType;
import org.astrogrid.acr.ivoa.resource.ConeCapability.Query;

/** test parsing of cone services.
 * 
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 20079:21:02 PM
 */
public class ConeParserUnitTest extends AbstractTestForParser{

	public void testConeService1() throws Exception {
		final ResourceStreamParser p = parse("adilCone.xml");
		final Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://org.astrogrid.regtest/vocone"
				, "ADIL"
				, "NCSA Astronomy Digital Image Library Cone Search"
				, "CatalogService");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("ADIL Librarian");}});
				setEmail("adil@ncsa.uiuc.edu");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{
					setValue("Dr. Raymond Plante");
				}});
				setLogoURI(new URI("http://adil.ncsa.uiuc.edu/gifs/adilfooter.gif"));
			}}	
		}
		, new ResourceName[] {		}
		, new Date[] {
		    new Date() {{
		        setRole("created");
		        setValue("2002-01-01");
		    }}
		}
		, new ResourceName() {{
			setValue("NCSA Astronomy Digital Image Library (ADIL)");
			setId(new URI("ivo://adil.ncsa/adil"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"data repositories","digital libraries"}
		, new String[] { "archive"} 
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
		// services.
		final Service s = validateService(r);
		assertEmpty(s.getRights());
		assertEquals(1,s.getCapabilities().length);
		
		assertTrue(" not an instanceof catalog service",  s instanceof CatalogService);
		final CatalogService catS =(CatalogService)s;
		assertEmpty(catS.getFacilities());
		assertEmpty(catS.getInstruments());
		assertEmpty(catS.getTables());
		final Coverage coverage = catS.getCoverage();
		checkCoverage(coverage,null,false,false,new String[0]);
		
		final Capability cap = s.getCapabilities()[0];
		checkCapability(cap,"ivo://ivoa.net/std/ConeSearch","ConeSearch",1);
		
		assertTrue(cap instanceof ConeCapability);
		assertSame(cap,((ConeService)s).findConeCapability());
		final Query q = new Query();
		q.setRa(102.2);
		q.setDec(28.5);
		q.setSr(0.5);
		checkConeCapability(cap,5000,10,false,q);
		
		final Interface i = cap.getInterfaces()[0];
		checkInterface(i,"std","vs:ParamHTTP",null,new SecurityMethod[0],new AccessURL[]{
		        new AccessURL() {{
		            setUse("base");
		            setValueURI(new URI("http://adil.ncsa.uiuc.edu/vocone?survey=f&"));
		        }}
		});
		assertTrue("expected a paramhttp interface",i instanceof ParamHttpInterface);
		final ParamHttpInterface phi = (ParamHttpInterface)i;
		assertNotNull("params must not be null",phi.getParams());
		assertEquals(0,phi.getParams().length);
		assertNull(phi.getQueryType());
		assertNull(phi.getResultType());
	
		final WebTester wt = basicResourceRendererTests(s);
		wt.assertTextPresent("Cone");
		wt.assertTextPresent("5000");
		wt.assertTextPresent(i.getAccessUrls()[0].getValueURI().toString());
	}
	
	   public void testConeService2() throws Exception {
	        final ResourceStreamParser p = parse("dsaConeCea.xml");
	        final Resource r =assertOnlyOne(p);
	        validateResource(r);    
	        checkResource(r
	                , "ivo://mssl.ucl.ac.uk_full/mysql-first-5-0"
	                , null
	                , "MYSQL TEST DSA"
	                , "CatalogService");
	        checkCuration(r.getCuration()
	                , new Contact[] {
	            new Contact() {{
	                setName(new ResourceName() {{setValue("Kona Andrews");}});
	                setEmail("kea@roe.ac.uk");
	            }}
	        }
	        , new Creator[] {
	            new Creator() {{
	                setName(new ResourceName() {{
	                    setValue("Dunno");
	                }});
	                setLogoURI(new URI("http://www2.astrogrid.org/Members/admin/frontpagepics/cassiopeiaxray_s.jpg"));
	            }}  
	        }
	        , new ResourceName[] {      }
	        , new Date[] {
	        }
	        , new ResourceName() {{
	            setValue("Kona Andrews,ROE");
	            }}
	        , null);
	        
	        checkContent(r.getContent()
	                , new String[] {"mysql test dsa"}
	        , new String[] { "catalog"} 
	        ,new String[] {}
	        , new Relationship[] {
	        }); 
	        // services.
	        final Service s = validateService(r);
	        assertEmpty(s.getRights());
	        assertEquals(2,s.getCapabilities().length);	        
	        
	        assertTrue(" not an instanceof catalog service",  s instanceof CatalogService);
	        final CatalogService catS =(CatalogService)s;
	        assertEmpty(catS.getFacilities());
	        assertEmpty(catS.getInstruments());
	        final Coverage coverage = catS.getCoverage();
	        checkCoverage(coverage,null,false,false,new String[0]);
	        final TableBean[] tables = catS.getTables();
	        assertNotNull(tables);
	        assertEquals(1,tables.length);
	        assertEquals("TabName_catalogue",tables[0].getName());
	        assertNull(tables[0].getDescription());
	        assertEquals(13,tables[0].getColumns().length);

	        final ColumnBean columnBean = tables[0].getColumns()[12];
	        assertNotNull(columnBean);
	        // noticed that this one has a datatype..
	        assertEquals("ColName_ID_FIELD",columnBean.getName());
	        assertNotNull(columnBean.getColumnDataType());
	        final TableDataType dataType = columnBean.getColumnDataType();
	        assertEquals("datatype not provided","*",dataType.getArraysize());
	        assertEquals("char",dataType.getType());
	        
	        final Capability cap = s.getCapabilities()[0];
	        checkCapability(cap,"ivo://ivoa.net/std/ConeSearch","ConeSearch",1
	                ,"Cone search on Catalog CatName_first, table TabName_catalogue"
	        );
	        
	        assertTrue(cap instanceof ConeCapability);
	        assertSame(cap,((ConeService)s).findConeCapability());
	        final Query q = new Query();
	        q.setRa(96.0);
	        q.setDec(5.0);
	        q.setSr(0.001);
	        checkConeCapability(cap,999999999,10,false,q);
	        
	        final Interface i = cap.getInterfaces()[0];
	        checkInterface(i,null,"vs:ParamHTTP",null,new SecurityMethod[0],new AccessURL[]{
	                new AccessURL() {{
	                    setUse("base");
	                    setValueURI(new URI("http://srif112.roe.ac.uk/mysql-first/SubmitCone?DSACAT=CatName_first&DSATAB=TabName_catalogue&"));
	                }}
	        });
	    
	        final WebTester wt = basicResourceRendererTests(s);
	        wt.assertTextPresent("Cone");
	        wt.assertTextPresent(cap.getDescription());
	        wt.assertTextPresent("999999999");       
	        wt.assertTextPresent("10");
	    }

	   public void testConeService3() throws Exception {
	        final ResourceStreamParser p = parse("heasarcCone.xml");
	        final Resource r =assertOnlyOne(p);
	        validateResource(r);    
	        checkResource(r
	                , "ivo://nasa.heasarc/a1"
	                , "A1"
	                , "HEAO 1 A-1 X-Ray Source Catalog"
	                , "CatalogService");
	        checkCuration(r.getCuration()
	                , new Contact[] {
	            new Contact() {{
	                setName(new ResourceName() {{setValue("Michael Preciado");}});
	                setEmail("preciado@milkyway.gsfc.nasa.gov");
	            }}
	        }
	        , new Creator[] {
	            new Creator() {{
	                setName(new ResourceName() {{
	                    setValue("Wood et al.");
	                }});
	            }}  
	        }
	        , new ResourceName[] {   
	            new ResourceName()
	        }
	        , new Date[] {
	            new Date() {{
	                setValue("2007-08-01");
	            }}
	        }
	        , new ResourceName() {{
	            setValue("NASA/GSFC HEASARC");
	            setId(new URI("ivo://nasa.heasarc/registry"));
	            }}
	        , null);
	        
	        checkContent(r.getContent()
	                , new String[] {"survey source"}
	        , new String[] { "catalog"} 
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
	        final Source source = r.getContent().getSource();
	        assertNotNull(source);
	        assertNull(source.getFormat());
	        assertEquals("1984ApJS...56..507W",source.getValue());
	        // services.
	        final Service s = validateService(r);
	        assertEmpty(s.getRights());
	        assertEquals(1,s.getCapabilities().length);
	        
	        assertTrue(" not an instanceof catalog service",  s instanceof CatalogService);
	        final CatalogService catS =(CatalogService)s;
	        assertEmpty(catS.getFacilities());
	        assertEmpty(catS.getInstruments());
	        assertEmpty(catS.getTables());
	        final Coverage coverage = catS.getCoverage();
	        checkCoverage(coverage,null,true,true,new String[]{"x-ray"});
	        
	        final Capability cap = s.getCapabilities()[0];
	        checkCapability(cap,"ivo://ivoa.net/std/ConeSearch","ConeSearch",1);
	        
	        assertTrue(cap instanceof ConeCapability);
	        assertSame(cap,((ConeService)s).findConeCapability());
	        final Query q = new Query();
	        q.setRa(355.187220068264992);
	        q.setDec(-86.132864346081405);
	        q.setSr(1);
	        checkConeCapability(cap,99999,180,false,q);
	        
	        final Interface i = cap.getInterfaces()[0];
	        checkInterface(i,"std","vs:ParamHTTP",null,new SecurityMethod[0],new AccessURL[]{
	                new AccessURL() {{
	                    setUse("base");
	                    setValueURI(new URI("http://heasarc.gsfc.nasa.gov/cgi-bin/vo/cone/coneGet.pl?table=a1&"));
	                }}
	        });
	        assertTrue("expected a paramhttp interface",i instanceof ParamHttpInterface);
	        final ParamHttpInterface phi = (ParamHttpInterface)i;
	        assertNotNull("params must not be null",phi.getParams());
	        assertEquals(0,phi.getParams().length);
	        assertEquals("get",phi.getQueryType());
	        assertEquals("text/xml",phi.getResultType());
	    
	        final WebTester wt = basicResourceRendererTests(s);
	        wt.assertTextPresent("Catalog");
	        wt.assertTextPresent("99999");       
	        wt.assertTextPresent("180");
	        wt.assertTextPresent(phi.getQueryType());
	        wt.assertTextPresent(phi.getResultType());
	        wt.assertTextPresent("All-Sky");
	    }
	      public void testConeService4() throws Exception {
	            final ResourceStreamParser p = parse("usnobCone.xml");
	            final Resource r =assertOnlyOne(p);
	            validateResource(r);    
	            checkResource(r
	                    , "ivo://roe.ac.uk/DSA_USNOB/cone"
	                    ,null
	                    , "USNO-B"
	                    , "CatalogService");
	            checkCuration(r.getCuration()
	                    , new Contact[] {
	                new Contact() {{
	                    setName(new ResourceName() {{setValue("Martin Hill");}});
	                    setEmail("mch@roe.ac.uk");
	                }}
	            }
	            , new Creator[] {
	            }
	            , new ResourceName[] {  
	            }
	            , new Date[] {
	            }
	            , new ResourceName() {{
	                setValue("Royal Observatory Edinburgh");
	                }}
	            , null);
	            
	            checkContent(r.getContent()
	                    , new String[] {}
	            , new String[] {"catalog" } 
	            ,new String[] {}
	            , new Relationship[] {
	            }); 

	            // services.
	            final Service s = validateService(r);
	            assertEmpty(s.getRights());
	            assertEquals(1,s.getCapabilities().length);
	            
	            assertTrue(" not an instanceof catalog service",  s instanceof CatalogService);
	            final CatalogService catS =(CatalogService)s;
	            assertEmpty(catS.getFacilities());
	            assertEmpty(catS.getInstruments());
	            assertEmpty(catS.getTables());
	            final Coverage coverage = catS.getCoverage();
	            checkCoverage(coverage,null,false,false,new String[]{});
	            
	            final Capability cap = s.getCapabilities()[0];
	            checkCapability(cap,"ivo://ivoa.net/std/ConeSearch","ConeSearch",1);
	            
	            assertTrue(cap instanceof ConeCapability);
	            assertSame(cap,((ConeService)s).findConeCapability());
	            final Query q = new Query();
	            q.setRa(120);
	            q.setDec(20);
	            q.setSr(0.5);
	            checkConeCapability(cap,2000,180,false,q);
	            
	            final Interface i = cap.getInterfaces()[0];
	            checkInterface(i,"std","vs:ParamHTTP",null,new SecurityMethod[0],new AccessURL[]{
	                    new AccessURL() {{
	                        setUse("base");
	                        setValueURI(new URI("http://adil.ncsa.uiuc.edu/vocone?survey=f"));
	                    }}
	            });
	            assertTrue("expected a paramhttp interface",i instanceof ParamHttpInterface);
	            final ParamHttpInterface phi = (ParamHttpInterface)i;
	            assertNotNull("params must not be null",phi.getParams());
	            assertEquals("get",phi.getQueryType());
	            assertEquals("text/xml+votable",phi.getResultType());
	            assertEquals(3,phi.getParams().length);
	            
	            final InputParam par = phi.getParams()[2];
	            final SimpleDataType dataType = par.getDataType();
	            assertNotNull(par);
	            assertNotNull(dataType);
	            assertNull(dataType.getArraysize());
	            assertEquals("real",dataType.getType());
	            
	            assertEquals("The search cone radius",par.getDescription());
	            assertEquals("SR",par.getName());
	            assertEquals("POS_ANG_DIST_REL",par.getUcd());
	            assertEquals("degrees",par.getUnit());
	            assertNull(par.getUse());
	        
	            final WebTester wt = basicResourceRendererTests(s);
	            wt.assertTextPresent("Catalog");
	            wt.assertTextPresent("2000");  
	            wt.assertTextPresent("180");
	            wt.assertTextPresent(phi.getQueryType());
	            wt.assertTextPresent(phi.getResultType());	     
	            wt.assertTextPresent(par.getDescription());
	            wt.assertTextPresent(par.getName());
	            wt.assertTextPresent(par.getUcd());
	            wt.assertTextPresent(par.getUnit());
	            wt.assertTextPresent(dataType.getType());
	        }
}
