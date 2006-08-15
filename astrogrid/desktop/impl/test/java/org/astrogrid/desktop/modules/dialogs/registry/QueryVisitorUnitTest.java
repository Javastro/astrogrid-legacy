/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry;

import junit.framework.TestCase;

/** unit test to check the visitor code.
 * @author Noel Winstanley
 * @since Aug 11, 20069:12:24 AM
 */
public class QueryVisitorUnitTest extends TestCase implements QueryVisitor {

	protected void setUp() throws Exception {
		super.setUp();
		QueryParser qp = new QueryParser("fred and barney or 'wilma jones'");
		q = qp.parse();
		assertNotNull(q);
	}
	
	
	protected AbstractQuery q;
	
	public void testVisitor() {
		Object result = q.accept(this);
		assertNotNull(result);
		System.out.println(result);
		assertEquals(1,andCount);
		assertEquals(1,orCount);
		assertEquals(2,termCount);
		assertEquals(1,phraseCount);
		assertEquals(0,notCount);
	}

	protected int andCount = 0;
	protected int orCount = 0;
	protected int termCount = 0;
	protected int phraseCount = 0;
	protected int notCount = 0;
	public Object visit(AndQuery q) {
		andCount++;
		return "(" + q.getLeft().accept(this) + ") & (" + q.getRight().accept(this) + ")";
	}

	public Object visit(OrQuery q) {
		orCount++;
		return "(" + q.getLeft().accept(this) + ") + (" + q.getRight().accept(this) + ")";
	}

	public Object visit(NotQuery q) {
		notCount++;
		return "! (" + q.getChild().accept(this) + ")"; 
	}

	public Object visit(FuzzyQuery q) {
		return null;
	}

	public Object visit(TermQuery q) {
		termCount++;
		return q.getTerm();
	}

	public Object visit(PhraseQuery q) {
		phraseCount++;
		return "'" + q.getPhrase() + "'";
	}
	

}
