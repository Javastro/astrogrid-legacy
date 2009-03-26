/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import junit.framework.TestCase;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.AndSRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.NotSRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.OrSRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.PhraseSRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQLParser;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.TargettedSRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.TermSRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.XPathSRQL;

/**
 * @author Noel Winstanley
 * @since Aug 10, 20069:11:37 AM
 */
public class SRQLParserUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testEmpty() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("   ");
		assertNull(qp.parse());
		qp = new SRQLParser("");		
		assertNull(qp.parse());
		
		try {
		qp = new SRQLParser(null);
		fail("expected to throw");
		} catch (IllegalArgumentException e) {
			// ok;
		}
	}
	
	public void testSingleTerm() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("fred");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("fred");}},q);
	}
	
	public void testSingleTermGoesToLower() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("Fred");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("fred");}},q);
	}
	
	public void testSingleTermParens() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("(fred)");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("fred");}},q);
	}
	
	public void testSingleTermParensMismatch() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("fred)");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("fred");}},q);
	}
	
	public void testSingleTermParensMismatchStart() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("(fred");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("fred");}},q);
	}
	
	public void testNumberTerm() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("1234");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("1234");}},q);
	}
	
	public void testSingleTermWhitespace() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred  ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("fred");}},q);
	}
	
	
	public void testSinglePhraseSQ() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  ' fred barney '  ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseSRQL(){{setPhrase(" fred barney ");}},q);		
	}
	
	
	public void testSinglePhraseDQ() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  \" fred barney \"  ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseSRQL(){{setPhrase(" fred barney ");}},q);		
	}
	
	
	public void testSinglePhraseMalformed() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  ' fred barney   ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseSRQL(){{setPhrase(" fred barney   ");}},q);		
	}
	
	
	public void testSinglePhraseEmpty() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  ''  ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseSRQL(){{setPhrase("");}},q);		
	}
	
	public void testSinglePhraseWhiteSpace() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  ' '   ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new PhraseSRQL(){{setPhrase(" ");}},q);		
	}
	
	public void testTargettedSRQLEquality() throws Exception {
		// oddity - check that eclipse-generated equals isn't broken. (doesn't test for instanceof by default)
		SRQL a = new TargettedSRQL() {{setTarget("shortname"); setChild(new TermSRQL() {{setTerm("fred");}});}};
		SRQL b = new TargettedSRQL() {{setTarget("shortname"); setChild(new TermSRQL() {{setTerm("fred");}});}};
		assertNotSame(a, b);
		assertEquals(a,b);
	}

	public void testSingleTargettedSpaced() throws Exception {
		SRQLParser qp = new SRQLParser("shortname = fred");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TargettedSRQL() {{setTarget("shortname"); setChild(new TermSRQL() {{setTerm("fred");}});}},q);
	}
	
	public void testSingleTargettedSemiSpaced() throws Exception {
		SRQLParser qp = new SRQLParser("shortname= fred");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TargettedSRQL() {{setTarget("shortname"); setChild(new TermSRQL() {{setTerm("fred");}});}},q);
	}
	
	public void testSingleTargettedNoSpaced() throws Exception {
		SRQLParser qp = new SRQLParser("shortname=fred");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TargettedSRQL() {{setTarget("shortname"); setChild(new TermSRQL() {{setTerm("fred");}});}},q);
	}
	
	public void testSingleTargettedTermContainsSemi() throws Exception {
		SRQLParser qp = new SRQLParser("id=ivo://org.astrogrid/galaxev");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TargettedSRQL() {{setTarget("id"); setChild(new TermSRQL() {{setTerm("ivo://org.astrogrid/galaxev");}});}},q);
	}
	
	public void testStringTargettedParens() throws Exception {
		SRQLParser qp = new SRQLParser("shortname= (fred or barney) ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new TargettedSRQL() {{
					setTarget("shortname");
					setChild(
							new OrSRQL(){{
								setLeft(new TermSRQL(){{setTerm("fred");}});
								setRight(new TermSRQL(){{setTerm("barney");}});
								}}
					);
				}}
				,q);
	}
	
	public void testStringTargettedNegatedParens() throws Exception {
		SRQLParser qp = new SRQLParser("shortname= not (fred or barney) ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new TargettedSRQL() {{
					setTarget("shortname");
					setChild(
							new NotSRQL() {{
								setChild(
										new OrSRQL(){{
											setLeft(new TermSRQL(){{setTerm("fred");}});
											setRight(new TermSRQL(){{setTerm("barney");}});
										}}
								);
							}}
					);
				}}
				,q);
	}
	
	public void testStringTargettedParensNegated() throws Exception {
		SRQLParser qp = new SRQLParser("shortname= (not (fred or barney)) ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new TargettedSRQL() {{
					setTarget("shortname");
					setChild(
							new NotSRQL() {{
								setChild(
										new OrSRQL(){{
											setLeft(new TermSRQL(){{setTerm("fred");}});
											setRight(new TermSRQL(){{setTerm("barney");}});
										}}
								);
							}}
					);
				}}
				,q);
	}
	
	public void testNegatedTargetted() throws Exception {
		SRQLParser qp = new SRQLParser("not shortname= (fred or barney) ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotSRQL() {{
					setChild(
							new TargettedSRQL() {{
								setTarget("shortname");
								setChild(
										new OrSRQL(){{
											setLeft(new TermSRQL(){{setTerm("fred");}});
											setRight(new TermSRQL(){{setTerm("barney");}});
										}}
								);
							}}
					);
				}}
				,q);
	}
	
	public void testSingleXPath() throws Exception {
		SRQLParser qp = new SRQLParser("`foo/bar/choo`");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new XPathSRQL() {{setXpath("foo/bar/choo");}},q);
	}
	
	public void testSingleXPathComplex() throws Exception {
		final String xp = "foo/bar/choo[id='37']/feg < \"42\"";
		SRQLParser qp = new SRQLParser("`" + xp + "`");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new XPathSRQL() {{setXpath(xp);}},q);
	}
	
	public void testImplicitOrXPath() throws Exception {
		final String xp = "foo/bar/choo[id='37']/feg < \"42\"";
		SRQLParser qp = new SRQLParser("`" + xp + "` fred");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrSRQL() {{
					setLeft(new XPathSRQL() {{setXpath(xp);}});
					setRight(new TermSRQL() {{setTerm("fred");}});
				}}
				,q);
	}
	
	public void testExplicitOrXPath() throws Exception {
		final String xp = "foo/bar/choo[id='37']/feg < \"42\"";
		SRQLParser qp = new SRQLParser("`" + xp + "` or fred");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrSRQL() {{
					setLeft(new XPathSRQL() {{setXpath(xp);}});
					setRight(new TermSRQL() {{setTerm("fred");}});
				}}
				,q);
	}
	
	public void testExplicitOrXPathReversed() throws Exception {
		final String xp = "foo/bar/choo[id='37']/feg < \"42\"";
		SRQLParser qp = new SRQLParser("fred or `" + xp + "`");
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrSRQL() {{
					setLeft(new TermSRQL() {{setTerm("fred");}});
					setRight(new XPathSRQL() {{setXpath(xp);}});
				}}
				,q);
	}
	
	
	
	public void testSingleNot() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("not fred ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotSRQL() {{
					setChild(new TermSRQL(){{setTerm("fred");}});
				}}
				,q);
		
	}
	
	public void testSingleNotParens() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("not (fred) ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotSRQL() {{
					setChild(new TermSRQL(){{setTerm("fred");}});
				}}
				,q);
		
	}
	

	
	public void testTwoTermsImplicit() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred  barney ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrSRQL(){{
				setLeft(new TermSRQL(){{setTerm("fred");}});
				setRight(new TermSRQL(){{setTerm("barney");}});
				}}
				,q);		
	}
	
	public void testTwoTermsExplicitOR() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred or barney  ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrSRQL(){{
				setLeft(new TermSRQL(){{setTerm("fred");}});
				setRight(new TermSRQL(){{setTerm("barney");}});
				}}
				,q);		
	}
	
	public void testTwoTermsExplicitAND() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred  and barney ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new AndSRQL(){{
				setLeft(new TermSRQL(){{setTerm("fred");}});
				setRight(new TermSRQL(){{setTerm("barney");}});
				}}
				,q);		
	}
	
	public void testTwoTermsExplicitANDNegated() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("not (fred and barney) ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotSRQL() {{
					setChild(
							new AndSRQL(){{
								setLeft(new TermSRQL(){{setTerm("fred");}});
								setRight(new TermSRQL(){{setTerm("barney");}});
								}}
					);
				}}
				,q);
		
	}
	
	public void testTwoTermsExplicitORNegated() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("not (fred or barney) ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotSRQL() {{
					setChild(
							new OrSRQL(){{
								setLeft(new TermSRQL(){{setTerm("fred");}});
								setRight(new TermSRQL(){{setTerm("barney");}});
								}}
					);
				}}
				,q);
		
	}
	
	public void testTwoTermsImplicitORNegated() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("not (fred barney) ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new NotSRQL() {{
					setChild(
							new OrSRQL(){{
								setLeft(new TermSRQL(){{setTerm("fred");}});
								setRight(new TermSRQL(){{setTerm("barney");}});
								}}
					);
				}}
				,q);
		
	}
	
	public void testAndMalformedPost() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred  and ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("fred");}}
				,q);		
	}
	
	public void testOrMalformedPost() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred  or ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("fred");}}
				,q);		
	}
	
	
	public void testAndMalformedPre() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  and fred ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("fred");}}
				,q);		
	}
	
	public void testOrMalformedPre() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser(" or  fred  ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(new TermSRQL(){{setTerm("fred");}}
				,q);		
	}
	
	public void testThreeTermsOrImplicit() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred  barney wilma ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrSRQL(){{
				setLeft(new TermSRQL(){{setTerm("fred");}});
				setRight(
						new OrSRQL(){{
							setLeft(new TermSRQL(){{setTerm("barney");}});
							setRight(new TermSRQL(){{setTerm("wilma");}});
							}}
							);			
				}},q);
	}
	
	public void testThreeTermsOrExplicit() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred or barney or wilma ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrSRQL(){{
				setLeft(new TermSRQL(){{setTerm("fred");}});
				setRight(
						new OrSRQL(){{
							setLeft(new TermSRQL(){{setTerm("barney");}});
							setRight(new TermSRQL(){{setTerm("wilma");}});
							}}
							);			
				}},q);
		// exercise the query classes a little.
		assertNotNull(q.toString());
		assertEquals(q,q);
	}
	
	public void testThreeTermsAndExplicit() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred and barney and wilma ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new AndSRQL(){{
				setLeft(new TermSRQL(){{setTerm("fred");}});
				setRight(
						new AndSRQL(){{
							setLeft(new TermSRQL(){{setTerm("barney");}});
							setRight(new TermSRQL(){{setTerm("wilma");}});
							}}
							);			
				}},q);
		// exercise the query classes a little.
		assertNotNull(q.toString());
		assertEquals(q,q);
		q.hashCode();	
	}
	
	public void testThreeTermsAndOrExplicit() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred and barney or wilma ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrSRQL(){{
				setLeft(
						new AndSRQL(){{
							setLeft(new TermSRQL(){{setTerm("fred");}});
							setRight(new TermSRQL(){{setTerm("barney");}});
							}}
							);			
				setRight(new TermSRQL(){{setTerm("wilma");}});
				}},q);
		// exercise the query classes a little.
		assertNotNull(q.toString());
		assertEquals(q,q);
		q.hashCode();		
	}
	
	
	public void testThreeTermsAndOrPhraseExplicit() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("fred and barney or 'wilma jones'");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrSRQL(){{
				setLeft(
						new AndSRQL(){{
							setLeft(new TermSRQL(){{setTerm("fred");}});
							setRight(new TermSRQL(){{setTerm("barney");}});
							}}
							);			
				setRight(new PhraseSRQL(){{setPhrase("wilma jones");}});
				}},q);
		assertNotNull(q.toString());
		assertEquals(q,q);		
		assertTrue(q.hashCode() > 0);		
	}
	
	
	public void testThreeTermsAndOrExplicitParens() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  (barney or wilma ) and fred ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new AndSRQL(){{
				setLeft(
						new OrSRQL(){{
							setLeft(new TermSRQL(){{setTerm("barney");}});
							setRight(new TermSRQL(){{setTerm("wilma");}});
							}}
							);			
				setRight(new TermSRQL(){{setTerm("fred");}});
				}},q);
	}
	
	public void testThreeTermsOrAndExplicit() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  fred or barney and wilma ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new OrSRQL(){{
				setLeft(new TermSRQL(){{setTerm("fred");}});
				setRight(
						new AndSRQL(){{
							setLeft(new TermSRQL(){{setTerm("barney");}});
							setRight(new TermSRQL(){{setTerm("wilma");}});
							}}
							);			
				}},q);
	}
	
	public void testComplex() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  (barney or (not wilma) ) and fred ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new AndSRQL(){{
				setLeft(
						new OrSRQL(){{
							setLeft(new TermSRQL(){{setTerm("barney");}});
							setRight(new NotSRQL() {{
								setChild(new TermSRQL(){{setTerm("wilma");}});
							}});
						}});
				setRight(new TermSRQL(){{setTerm("fred");}});
				}},q);
		// exercise the query classes a little.
		assertNotNull(q.toString());
		assertEquals(q,q);		
		assertTrue(q.hashCode() > 0);
	}
	
	public void testComplex1() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  (barney or (not shortname=wilma) ) and fred ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new AndSRQL(){{
				setLeft(
						new OrSRQL(){{
							setLeft(new TermSRQL(){{setTerm("barney");}});
							setRight(new NotSRQL() {{
								setChild(
										new TargettedSRQL() {{
											setTarget("shortname");
											setChild(new TermSRQL(){{setTerm("wilma");}});
										}}	);
							}});
						}});
				setRight(new TermSRQL(){{setTerm("fred");}});
				}},q);
		// exercise the query classes a little.
		assertNotNull(q.toString());
		assertEquals(q,q);		
		assertTrue(q.hashCode() > 0);
	}
	
	public void testComplex2() throws InvalidArgumentException {
		SRQLParser qp = new SRQLParser("  (`foo/bar < 34` or (not wilma) ) and fred ");		
		SRQL q = qp.parse();
		assertNotNull(q);
		assertEquals(
				new AndSRQL(){{
				setLeft(
						new OrSRQL(){{
							setLeft(
									new XPathSRQL() {{setXpath("foo/bar < 34");}}
							);
							setRight(new NotSRQL() {{
								setChild(new TermSRQL(){{setTerm("wilma");}});
							}});
						}});
				setRight(new TermSRQL(){{setTerm("fred");}});
				}},q);
		// exercise the query classes a little.
		assertNotNull(q.toString());
		assertEquals(q,q);		
	}
	
}
