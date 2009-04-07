/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import junit.framework.TestCase;

/** unit test to check the visitor code.
 * @author Noel Winstanley
 * @since Aug 11, 20069:12:24 AM
 */
public class SRQLVisitorUnitTest extends TestCase implements SRQLVisitor<String> {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		final SRQLParser qp = new SRQLParser("fred and barney or 'wilma jones' or shortname=nigel or ` //*[@id='fred']`");
		q = qp.parse();
		assertNotNull(q);
	}
	
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		q = null;
	}
	
	
	protected SRQL q;
	
	public void testVisitor() {
		final Object result = q.accept(this);
		assertNotNull(result);
		System.out.println(result);
		assertEquals(1,andCount);
		assertEquals(3,orCount);
		assertEquals(3,termCount);
		assertEquals(1,phraseCount);
		assertEquals(0,notCount);
		assertEquals(1,targetCount);
		assertEquals(1,xpathCount);
	}

	protected int andCount = 0;
	protected int orCount = 0;
	protected int termCount = 0;
	protected int phraseCount = 0;
	protected int notCount = 0;
	int targetCount = 0;
	int xpathCount = 0;
	public String visit(final AndSRQL q) {
		//System.out.println(q);		
		andCount++;
		return "(" + q.getLeft().accept(this) + ") & (" + q.getRight().accept(this) + ")";
	}

	public String visit(final OrSRQL q) {
		//System.out.println(q);		
		orCount++;
		return "(" + q.getLeft().accept(this) + ") + (" + q.getRight().accept(this) + ")";
	}

	public String visit(final NotSRQL q) {
		//System.out.println(q);		
		notCount++;
		return "! (" + q.getChild().accept(this) + ")"; 
	}

	public String visit(final TermSRQL q) {
	//System.out.println(q);		
		termCount++;
		return q.getTerm();
	}

	public String visit(final PhraseSRQL q) {
		//System.out.println(q);		
		phraseCount++;
		return "'" + q.getPhrase() + "'";
	}

	public String visit(final TargettedSRQL q) {
		//System.out.println(q);
		targetCount++;
		return q.getTarget() + "=" + q.getChild().accept(this);
	}

	public String visit(final XPathSRQL q) {
		//System.out.println(q);
		xpathCount++;
		return "`" + q.getXpath() + "`";
	}
	

}
