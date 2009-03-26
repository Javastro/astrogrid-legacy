/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import junit.framework.TestCase;

import org.astrogrid.acr.InvalidArgumentException;

/** Exercise the various visitors.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 200711:27:07 PM
 */
public class VisitorsUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		k = new KeywordSRQLVisitor();
	//	s = new BasicRegistrySRQLVisitor();
		s = new HeadClauseSRQLVisitor();
	}

	KeywordSRQLVisitor k;
	BasicRegistrySRQLVisitor s;

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		k = null;
		s = null;
	}

	
	public void testSimpleKeywords() throws Exception {
		final SRQL q = mkQuery("foo");
		final Object  keywords = q.accept(k);
		assertNotNull(keywords);
		assertEquals("foo",keywords);
	}

	public void testSimpleXQuery() throws Exception {
		final SRQL q = mkQuery("foo");
		final Object summary = q.accept(s);
		assertNotNull(summary);
		final String query = s.build(q, "fred");
		assertTrue(query.indexOf("(fred") != -1);
		assertTrue(query.indexOf(summary.toString()) != -1);
		assertEquals(-1,query.indexOf("Query")); // 'Query' indicates somewhere toString() is being called, instead of accept().		
	}
	
	
	public void testComplexKeywords() throws Exception {
		final SRQL q = mkQuery("\"fred bloggs\" and (foo or not shortname=Bar) and `/foo/nar/choo`");
		final Object  keywords = q.accept(k);
		assertNotNull(keywords);
		assertEquals("('fred bloggs') AND (((foo) OR (NOT (shortname = bar))) AND (`/foo/nar/choo`))",keywords);
	}

	public void testComplexXQuery() throws Exception {
		final SRQL q = mkQuery("\"fred bloggs\" and (foo or not shortname=bar) and `/foo/nar/choo`");
		final String xq = (String)q.accept(s);
		assertNotNull(xq);
		assertEquals(-1,xq.indexOf("Query")); // 'Query' indicates somewhere toString() is being called, instead of accept().
	}
	
	public void testComplexXQuery1() throws Exception {
		final SRQL q = mkQuery("galaxy and type=skyservice and not type=tabularskyservice");
		final String xq = (String)q.accept(s);
		assertNotNull(xq);
		assertEquals(-1,xq.indexOf("Query")); // 'Query' indicates somewhere toString() is being called, instead of accept().
	}


	/**
	 * @throws InvalidArgumentException
	 */
	// convienent way to build a query
	private SRQL  mkQuery(final String arg) throws InvalidArgumentException {
		final SRQLParser qp = new SRQLParser(arg);
		final SRQL q = qp.parse();
		assertNotNull(q);
		return q;
	}
	
}
