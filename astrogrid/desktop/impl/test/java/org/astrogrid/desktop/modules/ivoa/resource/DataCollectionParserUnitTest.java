/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;

import net.sourceforge.jwebunit.WebTester;

import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Format;
import org.astrogrid.acr.ivoa.resource.Organisation;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.acr.ivoa.resource.StcResourceProfile;
import org.astrogrid.acr.ivoa.resource.TabularDB;
import org.astrogrid.util.DomHelper;
import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/** unit tests for data collection resources, and deprecated subclass TabularDB.
 * @author Noel.Winstanley@manchester.ac.uk
 * @todo find another tabulardb that is more fully filled out.
 * @since Feb 20, 20079:23:15 PM
 */
public class DataCollectionParserUnitTest extends AbstractTestForParser{


	public void testDataCollection1() throws Exception {
		ResourceStreamParser p = parse("raiDataCollection.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://org.astrogrid.regtest/bima"
				, "BIMA"
				, "NCSA BIMA Data Archive"
				, "DataCollection");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("BIMA Archivist");}});
				setEmail("bimadata@ncsa.uiuc.edu");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setLogoURI(new URI("http://rai.ncsa.uiuc.edu/rai.jpg"));
				setName(new ResourceName() {{setValue("Dr. Richard Crutcher");}});
			}}
		}
		, new ResourceName[] {	
		    new ResourceName() {{setValue("Randal Sharpe");}}
            , new ResourceName() {{setValue("Dr. Raymond Plante");}}  
            , new ResourceName() {{setValue("Dr. Dave Merhinger");}}         
		    }
		, new Date[] {
		    new Date() {{
		        setValue("1993-01-01");
		        setRole("created");
		    }}
		}
		, new ResourceName() {{
			setValue("NCSA Radio Astronomy Imaging");
			setId(new URI("ivo://rai.ncsa/RAI"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"radio astronomy","data repositories","digital libraries"}
		, new String[] { "archive"} 
		,new String[] {"university","research"}
		, new Relationship[] {}); 
		DataCollection c = validateDataCollection(r);
		
		assertNull(c.getAccessURL());
		assertEmpty(c.getCatalogues());
		
	       checkCoverage(c.getCoverage(),null,false,false,new String[]{});
	       
		assertEquals(new ResourceName[] {
		        new ResourceName() {{
		            setValue("Berkeley-Illinois-Maryland Association Millimeter Array Telescope (BIMA)");
		        }}
		    },c.getFacilities());
	
		
		assertEquals(new Format[] {
						new Format() {{
						    setMimeType(false);
						    setValue("tarred miriad visibililty datasets");}} // all lower-cased.
						,                     new Format() {{
                            setMimeType(true);
                            setValue("image/fits");}}
				},c.getFormats());
		
		assertEmpty(c.getInstruments());
		assertEquals(new String[] {"proprietary"},c.getRights());
		
		WebTester wt = basicResourceRendererTests(c);
		wt.assertTextPresent(c.getFacilities()[0].getValue());
		wt.assertTextPresent(c.getFormats()[0].getValue());
		wt.assertTextPresent(c.getRights()[0]);
		
	}
	

	public void testDataCollection2() throws Exception {
		ResourceStreamParser p = parse("trapVizierDataCollection.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://org.astrogrid.regtest/CDS/VizieR/I/134/data"
				, "I/134/data"
				, "Trapezium Multiple Systems (Salukvadze, 1978) - The Catalogue of Trapezium Multiple Systems"
				, "DataCollection");
		
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("CDS support team");}});
				setAddress("CDS, Observatoire de Strasbourg, 11 rue de l'Universite, F-67000 Strasbourg, France");
				setEmail("question@simbad.u-strasbg.fr");
			}}
		}
		
		, new Creator[] {
			new Creator() {{
				setName(new ResourceName() {{setValue("Salukvadze G.N.");}});
			}}
		}
		, new ResourceName[] {	new ResourceName() {{setValue("Francois Ochsenbein [CDS]");}}	}
		, new Date[] { new Date() {{
		    setValue("1997-12-09");
		    setRole("creation");
		    }}
		}
		, new ResourceName() {{
			setValue("VizieR");
			setId(new URI("ivo://CDS/VizieR"));
			}}
		, "11-Feb-1997");
		
		checkContent(r.getContent()
		        , new String[] { "multiple_stars"}
				, new String[] { "catalog"}
		,new String[] {"research"}
		, new Relationship[] {}); 
		Source source = r.getContent().getSource();
		assertNotNull(source);
		assertEquals("bibcode",source.getFormat());
		assertEquals("1978AbaOB..49...39S",source.getValue());
		assertEquals("http://vizier.u-strasbg.fr/cgi-bin/Cat?I/134/data",r.getContent().getReferenceURI().toString());
		DataCollection c = validateDataCollection(r);
		
		assertNull(c.getAccessURL());
		assertEquals(new String[]{"public"},c.getRights());
		assertEquals(new Format[]{
		        new Format() {{
		            setMimeType(true);
		            setValue("text/xml+votable");
		        }}
		        ,             new Format() {{
                    setMimeType(true);
                    setValue("text/plain+csv");
                }}
		            
		},c.getFormats());
		
		assertEmpty(c.getFacilities());
		assertEmpty(c.getInstruments());
	       checkCoverage(c.getCoverage(),null,false,false,new String[]{"optical"});

		
		Catalog[] catalogues = c.getCatalogues();
		assertEquals(1,catalogues.length);
		Catalog cat = catalogues[0];
		assertNull(cat.getDescription());
		assertNull(cat.getName());
		assertEquals(1,cat.getTables().length);
		TableBean table = cat.getTables()[0];		
		assertNotNull(table);
		assertNull(table.getRole());
		assertEquals("I/134/data",table.getName());
		assertEquals("The Catalogue of Trapezium Multiple Systems",table.getDescription());
		ColumnBean[] columns = table.getColumns();
		assertEquals(13,columns.length);
		ColumnBean sampleCol = columns[6];
		assertNotNull(sampleCol);
		assertEquals("rho",sampleCol.getName());
		assertEquals("Distance of a component to the main star",sampleCol.getDescription());
		assertNull(sampleCol.getDatatype());
		assertNull(sampleCol.getColumnDataType());
		assertEquals("ORBIT_SEPARATION",sampleCol.getUcd());
		assertEquals(sampleCol.getUcd(),sampleCol.getUCD());
		assertEquals("arcsec",sampleCol.getUnit());
		assertFalse(sampleCol.isStd());
		
		

		WebTester wt = basicResourceRendererTests(c);
		wt.assertTextPresent(c.getRights()[0]);		
		wt.assertTextPresent(c.getFormats()[0].getValue());
        wt.assertTextPresent(c.getFormats()[1].getValue());        
        wt.assertTextPresent(c.getCoverage().getWavebands()[0]);
        
		
	}
	
	public void testDataCollection3() throws Exception {
        ResourceStreamParser p = parse("ausvoDataCollectionCoverage.xml");
        Resource r =assertOnlyOne(p);
        validateResource(r);    
        checkResource(r
                , "ivo://ausvo/hipassdata"
                , "HIPASS data"
                , "HI Parkes All Sky Survey data"
                , "DataCollection");
        checkCuration(r.getCuration()
                , new Contact[] {
            new Contact() {{
                setName(new ResourceName() {{setValue("David Barnes");}});
                setEmail("barnesd@unimelb.edu.au");
            }}
        }
        , new Creator[] {
            new Creator() {{
                setLogoURI(new URI("http://www.aus-vo.org/ausvologo_small.png"));
                setName(new ResourceName() {{setValue("The Australian Virtual Observatory");}});
            }}
        }
        , new ResourceName[] {  
            new ResourceName() {{setValue("School of Physics, The University of Melbourne");}}
            }
        , new Date[] {
            new Date() {{
                setValue("2004-02-01");
            }}
        }
        , new ResourceName() {{
            setValue("The Australian Virtual Observatory");
            setId(new URI("ivo://ausvo/organisation"));
            }}
        , "1.0");
        
        checkContent(r.getContent()
                , new String[] {}
        , new String[] { "catalog","survey"} 
        ,new String[] {"university","research"}
        , new Relationship[] {}); 
        
        DataCollection c = validateDataCollection(r);
        
        assertNull(c.getAccessURL());
        assertEmpty(c.getCatalogues());
        
        checkCoverage(c.getCoverage(),null,true,false,new String[]{"radio"});

        assertEmpty(c.getFacilities());
        assertEmpty(c.getFormats());
        assertEmpty(c.getRights());

        
        WebTester wt = basicResourceRendererTests(c);
        wt.assertTextPresent(c.getCoverage().getWavebands()[0]);
        
    }
    
//	
	/** chekc basic srtructure of a datacollection is correct 
	 *  - don't check values however 
	 * @param r
	 * @return
	 */
	public static DataCollection validateDataCollection(Resource r) {
		assertNotNull(r);
		assertTrue("not an instance of datacollection",r instanceof DataCollection);
		
		DataCollection c =  (DataCollection)r;
		assertNotNull("catalogues are null",c.getCatalogues());
		assertNotNull("facilities are null",c.getFacilities());
		assertNotNull("formats are null",c.getFormats());
		assertNotNull("instruments are null",c.getInstruments());
		assertNotNull("rights are null",c.getRights());
		return c;
	}
	
	/** check basic structure of a coverage is correct */
	public static void validateCoverage(Coverage c) {
		assertNotNull(c);
		assertNotNull(c.getWavebands());
	
	}
	
	

	
	
}
