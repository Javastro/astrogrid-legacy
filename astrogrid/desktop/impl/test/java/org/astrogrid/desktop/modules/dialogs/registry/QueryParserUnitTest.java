/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley
 * @since Aug 10, 20069:11:37 AM
 */
public class QueryParserUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testEmpty() {
		QueryParser qp = new QueryParser("   ");
		assertNull(qp.parse());
		qp = new QueryParser("");		
		assertNull(qp.parse());
		
		try {
		qp = new QueryParser(null);
		fail("expected to throw");
		} catch (IllegalArgumentException e) {
			// ok;
		}
	}
	
	public void testSingleTerm() {
		QueryParser qp = new QueryParser("fred");
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}},q);
	}
	
	public void testNumberTerm() {
		QueryParser qp = new QueryParser("1234");
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("1234");}},q);
	}
	
	public void testSingleTermWhitespace() {
		QueryParser qp = new QueryParser("  fred  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}},q);
	}
	
	
	public void testSinglePhraseSQ() {
		QueryParser qp = new QueryParser("  ' fred barney '  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseQuery(){{setPhrase(" fred barney ");}},q);		
	}
	
	
	public void testSinglePhraseDQ() {
		QueryParser qp = new QueryParser("  \" fred barney \"  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseQuery(){{setPhrase(" fred barney ");}},q);		
	}
	
	
	public void testSinglePhraseMalformed() {
		QueryParser qp = new QueryParser("  ' fred barney   ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseQuery(){{setPhrase(" fred barney   ");}},q);		
	}
	
	public void testSinglePhraseEmpty() {
		QueryParser qp = new QueryParser("  ''  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseQuery(){{setPhrase("");}},q);		
	}
	
	public void testSinglePhraseWhiteSpace() {
		QueryParser qp = new QueryParser("  ' '   ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseQuery(){{setPhrase(" ");}},q);		
	}
	
	public void testSingleNot() {
		QueryParser qp = new QueryParser("not fred ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotQuery() {{
					setChild(new TermQuery(){{setTerm("fred");}});
				}}
				,q);
		
	}
	

	
	public void testTwoTermsImplicit() {
		QueryParser qp = new QueryParser("  fred  barney ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrQuery(){{
				setLeft(new TermQuery(){{setTerm("fred");}});
				setRight(new TermQuery(){{setTerm("barney");}});
				}}
				,q);		
	}
	
	public void testTwoTermsExplicitOR() {
		QueryParser qp = new QueryParser("  fred or barney  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrQuery(){{
				setLeft(new TermQuery(){{setTerm("fred");}});
				setRight(new TermQuery(){{setTerm("barney");}});
				}}
				,q);		
	}
	
	public void testTwoTermsExplicitAND() {
		QueryParser qp = new QueryParser("  fred  and barney ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new AndQuery(){{
				setLeft(new TermQuery(){{setTerm("fred");}});
				setRight(new TermQuery(){{setTerm("barney");}});
				}}
				,q);		
	}
	
	public void testAndMalformedPost() {
		QueryParser qp = new QueryParser("  fred  and ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}}
				,q);		
	}
	
	public void testOrMalformedPost() {
		QueryParser qp = new QueryParser("  fred  or ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}}
				,q);		
	}
	
	
	public void testAndMalformedPre() {
		QueryParser qp = new QueryParser("  and fred ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}}
				,q);		
	}
	
	public void testOrMalformedPre() {
		QueryParser qp = new QueryParser(" or  fred  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}}
				,q);		
	}
	
	public void testThreeTermsAndImplicit() {
		QueryParser qp = new QueryParser("  fred  barney wilma ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrQuery(){{
				setLeft(new TermQuery(){{setTerm("fred");}});
				setRight(
						new OrQuery(){{
							setLeft(new TermQuery(){{setTerm("barney");}});
							setRight(new TermQuery(){{setTerm("wilma");}});
							}}
							);			
				}},q);
	}
	
	public void testThreeTermsOrExplicit() {
		QueryParser qp = new QueryParser("  fred or barney or wilma ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrQuery(){{
				setLeft(new TermQuery(){{setTerm("fred");}});
				setRight(
						new OrQuery(){{
							setLeft(new TermQuery(){{setTerm("barney");}});
							setRight(new TermQuery(){{setTerm("wilma");}});
							}}
							);			
				}},q);
	}
	
	public void testThreeTermsAndExplicit() {
		QueryParser qp = new QueryParser("  fred and barney and wilma ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new AndQuery(){{
				setLeft(new TermQuery(){{setTerm("fred");}});
				setRight(
						new AndQuery(){{
							setLeft(new TermQuery(){{setTerm("barney");}});
							setRight(new TermQuery(){{setTerm("wilma");}});
							}}
							);			
				}},q);
	}
	
	public void testThreeTermsAndOrExplicit() {
		QueryParser qp = new QueryParser("  fred and barney or wilma ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrQuery(){{
				setLeft(
						new AndQuery(){{
							setLeft(new TermQuery(){{setTerm("fred");}});
							setRight(new TermQuery(){{setTerm("barney");}});
							}}
							);			
				setRight(new TermQuery(){{setTerm("wilma");}});
				}},q);
	}
	
	
	public void testThreeTermsAndOrPhraseExplicit() {
		QueryParser qp = new QueryParser("fred and barney or 'wilma jones'");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrQuery(){{
				setLeft(
						new AndQuery(){{
							setLeft(new TermQuery(){{setTerm("fred");}});
							setRight(new TermQuery(){{setTerm("barney");}});
							}}
							);			
				setRight(new PhraseQuery(){{setPhrase("wilma jones");}});
				}},q);
	}
	
	public void testThreeTermsAndOrExplicitParens() {
		QueryParser qp = new QueryParser("  (barney or wilma ) and fred ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new AndQuery(){{
				setLeft(
						new OrQuery(){{
							setLeft(new TermQuery(){{setTerm("barney");}});
							setRight(new TermQuery(){{setTerm("wilma");}});
							}}
							);			
				setRight(new TermQuery(){{setTerm("fred");}});
				}},q);
	}
	
	public void testThreeTermsOrAndExplicit() {
		QueryParser qp = new QueryParser("  fred or barney and wilma ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrQuery(){{
				setLeft(new TermQuery(){{setTerm("fred");}});
				setRight(
						new AndQuery(){{
							setLeft(new TermQuery(){{setTerm("barney");}});
							setRight(new TermQuery(){{setTerm("wilma");}});
							}}
							);			
				}},q);
	}
	
}
