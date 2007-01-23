/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry;

import junit.framework.TestCase;

import org.astrogrid.acr.InvalidArgumentException;

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

	
	public void testEmpty() throws InvalidArgumentException {
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
	
	public void testSingleTerm() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("fred");
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}},q);
	}
	
	public void testSingleTermParens() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("(fred)");
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}},q);
	}
	
	public void testSingleTermParensMismatch() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("fred)");
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}},q);
	}
	
	public void testSingleTermParensMismatchStart() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("(fred");
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}},q);
	}
	
	public void testNumberTerm() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("1234");
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("1234");}},q);
	}
	
	public void testSingleTermWhitespace() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("  fred  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}},q);
	}
	
	
	public void testSinglePhraseSQ() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("  ' fred barney '  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseQuery(){{setPhrase(" fred barney ");}},q);		
	}
	
	
	public void testSinglePhraseDQ() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("  \" fred barney \"  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseQuery(){{setPhrase(" fred barney ");}},q);		
	}
	
	
	public void testSinglePhraseMalformed() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("  ' fred barney   ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseQuery(){{setPhrase(" fred barney   ");}},q);		
	}
	
	
	public void testSinglePhraseEmpty() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("  ''  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseQuery(){{setPhrase("");}},q);		
	}
	
	public void testSinglePhraseWhiteSpace() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("  ' '   ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseQuery(){{setPhrase(" ");}},q);		
	}
	

	
	public void testSingleNot() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("not fred ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotQuery() {{
					setChild(new TermQuery(){{setTerm("fred");}});
				}}
				,q);
		
	}
	
	public void testSingleNotParens() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("not (fred) ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotQuery() {{
					setChild(new TermQuery(){{setTerm("fred");}});
				}}
				,q);
		
	}
	

	
	public void testTwoTermsImplicit() throws InvalidArgumentException {
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
	
	public void testTwoTermsExplicitOR() throws InvalidArgumentException {
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
	
	public void testTwoTermsExplicitAND() throws InvalidArgumentException {
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
	
	public void testTwoTermsExplicitANDNegated() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("not (fred and barney) ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotQuery() {{
					setChild(
							new AndQuery(){{
								setLeft(new TermQuery(){{setTerm("fred");}});
								setRight(new TermQuery(){{setTerm("barney");}});
								}}
					);
				}}
				,q);
		
	}
	
	public void testTwoTermsExplicitORNegated() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("not (fred or barney) ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotQuery() {{
					setChild(
							new OrQuery(){{
								setLeft(new TermQuery(){{setTerm("fred");}});
								setRight(new TermQuery(){{setTerm("barney");}});
								}}
					);
				}}
				,q);
		
	}
	
	public void testTwoTermsImplicitORNegated() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("not (fred barney) ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotQuery() {{
					setChild(
							new OrQuery(){{
								setLeft(new TermQuery(){{setTerm("fred");}});
								setRight(new TermQuery(){{setTerm("barney");}});
								}}
					);
				}}
				,q);
		
	}
	
	public void testAndMalformedPost() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("  fred  and ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}}
				,q);		
	}
	
	public void testOrMalformedPost() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("  fred  or ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}}
				,q);		
	}
	
	
	public void testAndMalformedPre() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("  and fred ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}}
				,q);		
	}
	
	public void testOrMalformedPre() throws InvalidArgumentException {
		QueryParser qp = new QueryParser(" or  fred  ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermQuery(){{setTerm("fred");}}
				,q);		
	}
	
	public void testThreeTermsOrImplicit() throws InvalidArgumentException {
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
	
	public void testThreeTermsOrExplicit() throws InvalidArgumentException {
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
	
	public void testThreeTermsAndExplicit() throws InvalidArgumentException {
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
	
	public void testThreeTermsAndOrExplicit() throws InvalidArgumentException {
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
	
	
	public void testThreeTermsAndOrPhraseExplicit() throws InvalidArgumentException {
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
	
	
	public void testThreeTermsAndOrExplicitParens() throws InvalidArgumentException {
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
	
	public void testThreeTermsOrAndExplicit() throws InvalidArgumentException {
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
	
	public void testComplex() throws InvalidArgumentException {
		QueryParser qp = new QueryParser("  (barney or (not wilma) ) and fred ");		
		AbstractQuery q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new AndQuery(){{
				setLeft(
						new OrQuery(){{
							setLeft(new TermQuery(){{setTerm("barney");}});
							setRight(new NotQuery() {{
								setChild(new TermQuery(){{setTerm("wilma");}});
							}});
						}});
				setRight(new TermQuery(){{setTerm("fred");}});
				}},q);
	}
	
}
