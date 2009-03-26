/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import junit.framework.TestCase;

import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** unit test for the ar's adql parser.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20072:58:17 PM
 */
public class AdqlUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		adql = new AdqlImpl();
	}
	AdqlInternal adql;
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		adql = null;
	}

	public void testEmptyS2x() {
		
	}
	
	public void testNullS2x() {
	}
	
	public void testSimpleS2x() throws Exception {
		Document d= adql.s2x("select * from x as a");
		assertNotNull(d);
		String string = DomHelper.DocumentToString(d);
		assertTrue(string.indexOf("Select") != -1);

	}
	
	public void testS2xs() throws Exception {
		String string= adql.s2xs("Select Top 100 * From iras_asc as a ");
		assertNotNull(string);
		assertTrue(string.indexOf("Select") != -1);
	}
	
    public void tests2xs() throws Exception {
        String s = adql.s2xs("select * from tab t where t.identifier = '3'" );
        assertNotNull(s);
    }

    public void tests2x() throws Exception {
        Document d  = adql.s2x("select * from tab t where t.identifier = '3'" );
        assertNotNull(d);
    }
	
    // just check we can do a registry-like query without throwing.
    public void testRegistryAdql() throws Exception {
       String s= adql.s2xs("select * from registry r where r.identifier = 'ivo://foo.bar.choo'");
       System.err.println(s);
    }

}
