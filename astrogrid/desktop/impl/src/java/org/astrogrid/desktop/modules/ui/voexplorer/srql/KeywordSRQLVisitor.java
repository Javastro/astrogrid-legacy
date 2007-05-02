/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

/** visitor that traverse a SRQL and generates a normalized search string from it - indicates what the parser
 * thinks the user has inputted. The query is not executable - but indicates what's being searched for.
 * @author Noel Winstanley
 * @since Aug 15, 200612:12:11 PM
 */
public class KeywordSRQLVisitor implements SRQLVisitor {


	public Object visit(AndSRQL q) {
		return "(" + q.getLeft().accept(this) + ") AND (" + q.getRight().accept(this) + ")";
	}

	public Object visit(OrSRQL q) {

		return "(" + q.getLeft().accept(this) + ") OR (" + q.getRight().accept(this) + ")";		
	}

	public Object visit(NotSRQL q) {
		return "NOT " + parenthesizeNonLeaf(q.getChild());
	}


	public Object visit(TermSRQL q) {
		return q.getTerm();
	}

	public Object visit(PhraseSRQL q) {
		return "'" + q.getPhrase() + "'";
	}

	public Object visit(TargettedSRQL q) {
		return q.getTarget() + " = " + parenthesizeNonLeaf(q.getChild());
	}

	public Object visit(XPathSRQL q) {
		return "`" + q.getXpath() + "`";
	}
	
	private boolean isLeaf(SRQL s) {
		return s instanceof XPathSRQL 
				|| s instanceof PhraseSRQL
				|| s instanceof TermSRQL
				|| (s instanceof NotSRQL && isLeaf(((NotSRQL)s).getChild()))
				;
	}
	
	private String parenthesizeNonLeaf(SRQL s) {
		if (!isLeaf(s)) {
			return "(" + s.accept(this) + ")";
		} else {
			return  (String)s.accept(this);
		}
	}

}
