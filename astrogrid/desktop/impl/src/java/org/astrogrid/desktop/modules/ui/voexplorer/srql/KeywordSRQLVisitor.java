/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

/** 
 * Genrate a normalized SRQL search string from SRQL tree.
 * Used to indicate what the parser
 * thinks the user has inputted. The query is not executable - but indicates what's being searched for.
 * This visitor generates output that is compatible with the SRQL parser.
 * @author Noel Winstanley
 * @since Aug 15, 200612:12:11 PM
 */
public class KeywordSRQLVisitor implements SRQLVisitor<String> {

	public String visit(final AndSRQL q) {
		return "(" + q.getLeft().accept(this) + ") AND (" + q.getRight().accept(this) + ")";
	}

	public String visit(final OrSRQL q) {

		return "(" + q.getLeft().accept(this) + ") OR (" + q.getRight().accept(this) + ")";		
	}

	public String visit(final NotSRQL q) {
		return "NOT " + parenthesizeNonLeaf(q.getChild());
	}


	public String visit(final TermSRQL q) {
		return q.getTerm();
	}

	public String visit(final PhraseSRQL q) {
		return "'" + q.getPhrase() + "'";
	}

	public String visit(final TargettedSRQL q) {
		return q.getTarget() + " = " + (q.getChild() == null ? "null" :  parenthesizeNonLeaf(q.getChild())); // a little more tolerant of commonest error cases.
	}

	public String visit(final XPathSRQL q) {
		return "`" + q.getXpath() + "`";
	}
	
	private boolean isLeaf(final SRQL s) {
		return s instanceof XPathSRQL 
				|| s instanceof PhraseSRQL
				|| s instanceof TermSRQL
				|| (s instanceof NotSRQL && isLeaf(((NotSRQL)s).getChild()))
				;
	}
	
	private String parenthesizeNonLeaf(final SRQL s) {
		if (!isLeaf(s)) {
			return "(" + s.accept(this) + ")";
		} else {
			return  (String)s.accept(this);
		}
	}

}
