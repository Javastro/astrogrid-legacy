/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry.srql;

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
		return "NOT (" + q.getChild().accept(this) + ")";
	}


	public Object visit(TermSRQL q) {
		return q.getTerm();
	}

	public Object visit(PhraseSRQL q) {
		return "'" + q.getPhrase() + "'";
	}

	public Object visit(TargettedSRQL q) {
		return q.getTarget() + "=(" + q.getChild().accept(this) + ")";
	}

	public Object visit(XPathSRQL q) {
		return "`" + q.getXpath() + "`";
	}

}
