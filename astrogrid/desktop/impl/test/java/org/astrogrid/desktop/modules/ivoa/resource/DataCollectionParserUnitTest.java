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
import org.astrogrid.acr.ivoa.resource.TabularDB;

/** unit tests for data collection resources, and deprecated subclass TabularDB.
 * @author Noel.Winstanley@manchester.ac.uk
 * @todo find another tabulardb that is more fully filled out.
 * @since Feb 20, 20079:23:15 PM
 */
public class DataCollectionParserUnitTest extends AbstractTestForParser{


	public void testDataCollection1() throws Exception {
		ResourceStreamParser p = parse("datacollection1.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://adil.ncsa/adil"
				, "ADIL"
				, "NCSA Astronomy Digital Image Library"
				, "DataCollection");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Dr. Raymond Plante");}});
				setEmail("adil@ncsa.uiuc.edu");
			}}
		}
		, new Creator[] {
			new Creator() {{
				setLogoURI(new URI("http://adil.ncsa.uiuc.edu/gifs/adilfooter.gif"));
				setName(new ResourceName() {{setValue("Dr. Raymond Plante");}});
			}}
		}
		, new ResourceName[] {	new ResourceName() {{setValue("Raymond Plante");}}	}
		, new Date[] {}
		, new ResourceName() {{
			setValue("NCSA Astronomy Digital Image Library");
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {"radio astronomy","data repositories","digital libraries"}
		, new String[] { "archive"} // nb, note lower casing here.
		,new String[] {"university","research"}
		, new Relationship[] {}); 
		DataCollection c = validateDataCollection(r);
		
		assertNull(c.getAccessURL());
		assertEmpty(c.getCatalogues());
		Coverage coverage = c.getCoverage();
		//@todo need to add a representation for 'all sky' at least here.
		
		assertEquals(new String[] {"radio","millimeter","infrared","optical","uv"},coverage.getWavebands());
		
		assertEmpty(c.getFacilities());
		
		assertEquals(new Format[] {
						new Format() {{setValue("fits");}}// lower-cased these.
						,new Format() {{setValue("gif");}}
						,new Format() {{setValue("html");}}
				},c.getFormats());
		
		assertEmpty(c.getInstruments());
		assertEquals(new String[] {"public"},c.getRights());
		
		WebTester wt = basicResourceRendererTests(c);
		wt.assertTextPresent("optical");
		wt.assertTextPresent("fits");
		wt.assertTextPresent("public");
		
	}
	

	public void testDataCollection2() throws Exception {
		ResourceStreamParser p = parse("datacollection2.xml");
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
		, new ResourceName[] {	new ResourceName() {{setValue("School of Physics, The University of Melbourne");}}	}
		, new Date[] { new Date() {{setValue("2004-02-01");}}}
		, new ResourceName() {{
			setValue("The Australian Virtual Observatory");
			setId(new URI("ivo://ausvo/organisation"));
			}}
		, "1.0");
		
		checkContent(r.getContent()
				, new String[] {}
		, new String[] { "catalog", "survey"} // nb, note lower casing here.
		,new String[] {"university","research"}
		, new Relationship[] {}); 
		DataCollection c = validateDataCollection(r);
		
		assertNull(c.getAccessURL());
		assertEmpty(c.getCatalogues());
		Coverage coverage = c.getCoverage();
		//too hard to parse, and gross difference between new propose standard, and
		// what's there inthe existing schema. leave for now
		//assertNotNull(coverage.getStcResourceProfile());

		assertEquals(new String[] {"radio"},coverage.getWavebands());
		//this reg entry has additional metadata for wavebands - but they're not in the schema.
		// (they're present in the old echema )/
		assertEquals(new ResourceName[] {
				new ResourceName() {{setValue("CSIRO ATNF Parkes 64m radio telescope");}}
			},c.getFacilities());
		
		// verify that this hasnt been mis-recognized as an Oranisation
		assertFalse(r instanceof Organisation);
		assertEquals(new Format[] {
				new Format() {{setValue("ascii text");}}// lower-cased these.
				,new Format() {{setValue("html");}}
				,new Format() {{setValue("votable");}}
				,new Format() {{setValue("png");}}				
		},c.getFormats());
		
		assertEquals(new ResourceName[] {
				new ResourceName() {{setValue("ATNF 21-cm Multibeam receiver");}}
			},	c.getInstruments());
		assertEquals(new String[] {"public"},c.getRights());
		
		WebTester wt = basicResourceRendererTests(c);
		wt.assertTextPresent("html");
		wt.assertTextPresent("64m");
		
	}
	

	public void testDataCollection3() throws Exception {
		ResourceStreamParser p = parse("datacollection3.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://cadc.nrc.ca/archive/hst/wfpc2"
				, "WFPC2 Assoc"
				, "WFPC2 Associations Project"
				, "DataCollection");
		checkCuration(r.getCuration()
				, new Contact[] {
			new Contact() {{
				setName(new ResourceName() {{setValue("Daniel Durand");}});
				setEmail("daniel.durand@nrc-cnrc.gc.ca");
				setAddress("5071 West Saanich Rd\nVictoria, BC, Canada\nV9E 2E7"); //@todo need to thik how to handle carriage returns..
			}}
		}
		, new Creator[] {	}
		, new ResourceName[] {	}
		, new Date[] { }
		, new ResourceName() {{
			setValue("Canadian Astronomy Data Centre");
			setId(new URI("ivo://cadc.nrc.ca/org"));
			}}
		, null);
		
		checkContent(r.getContent()
				, new String[] {}
		, new String[] { "archive", "project"} // nb, note lower casing here.
		,new String[] {"research"}
		, new Relationship[] {}); 
		DataCollection c = validateDataCollection(r);
		
		assertNull(c.getAccessURL());
		assertEmpty(c.getCatalogues());
		Coverage coverage = c.getCoverage();
		// leave this until it settles more.
		//	assertNotNull(coverage.getStcResourceProfile()); 
		
		assertEquals(new String[] {"optical","uv"},coverage.getWavebands());
		assertEquals(new ResourceName[] {	},c.getFacilities());
		assertEquals(new Format[] {	},c.getFormats());
		assertEquals(new ResourceName[] {	},	c.getInstruments());
		assertEquals(new String[] {},c.getRights());
		
		WebTester wt = basicResourceRendererTests(c);
		wt.assertTextPresent("optical");
	}	

	public void testTabularDb() throws Exception {
		
		ResourceStreamParser p = parse("tabulardb1.xml");
		Resource r =assertOnlyOne(p);
		validateResource(r);	
		checkResource(r
				, "ivo://mssl.ucl.ac.uk/solar_events_dsa/TDB"
				, null
				, "Datacenter for Solar Event Catalogue"
				, "TabularDB");

		
		checkContent(r.getContent()
				, new String[] {}
		, new String[] { "catalog"} // nb, note lower casing here.
		,new String[] {}
		, new Relationship[] {}); 
		DataCollection c = validateDataCollection(r);
		assertNull(c.getAccessURL());
		assertEquals(1,c.getCatalogues().length);
		Catalog cat = c.getCatalogues()[0];
		assertEquals("Solar Event Catalogue",cat.getName());
		assertNull(cat.getDescription());
		
		TableBean[] tables = cat.getTables();
		assertNotNull(tables);
		assertEquals(17,tables.length);
		TableBean tb= tables[16];
		assertEquals("yohkoh_flare_list",tb.getName());
		assertNotNull(tb.getDescription());
		assertNotNull(tb.getColumns());
		assertEquals(15,tb.getColumns().length);
		ColumnBean[] arr = tb.getColumns();
		assertNotNull(arr);	
		ColumnBean cb = arr[1];	
		assertNotNull(cb);		
		assertNotNull(cb.getName());
		assertEquals("time_start",cb.getName());
		assertNull(cb.getDescription());
		assertNotNull(cb.getDatatype());
		assertEquals("char",cb.getDatatype());
		assertNull(cb.getUCD());
		assertNotNull(cb.getUnit());
		assertEquals("iso8601",cb.getUnit());
		
		assertNull(c.getCoverage());

		assertEquals(new ResourceName[] {	},c.getFacilities());
		assertEquals(new Format[] {	},c.getFormats());
		assertEquals(new ResourceName[] {	},	c.getInstruments());
		assertEquals(new String[] {},c.getRights());
		
		// finally, verify that this is a tabular db too. and the databse bean is same as first column.
		assertTrue(c instanceof TabularDB);
		final DatabaseBean database = ((TabularDB)c).getDatabase();
		assertNotNull(database);
		assertEquals(c.getCatalogues()[0],database);
		WebTester wt = basicResourceRendererTests(c);
		wt.assertTextPresent("yohkoh_flare_list");
		//not showing cols anymore wt.assertTextPresent("time_start");
		
	}
	
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
