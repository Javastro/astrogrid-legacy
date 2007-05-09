/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

/** visitor that traverse the query tree and generates a normalized search string from it - indicates what the parser
 * thinks the user has inputted. The query is not executable - but indicates what's being searched for.
 * @author Noel Winstanley
 * @since Aug 15, 200612:12:11 PM
 */
public class KeywordQueryVisitor implements QueryVisitor {


	public Object visit(AndQuery q) {
		return "(" + q.getLeft().accept(this) + ") AND (" + q.getRight().accept(this) + ")";
	}

	public Object visit(OrQuery q) {

		return "(" + q.getLeft().accept(this) + ") OR (" + q.getRight().accept(this) + ")";		
	}

	public Object visit(NotQuery q) {
		return "NOT (" + q.getChild().accept(this) + ")";
	}

	public Object visit(FuzzyQuery q) {
		return null;
	}

	public Object visit(TermQuery q) {
		return q.getTerm();
	}

	public Object visit(PhraseQuery q) {
		return "'" + q.getPhrase() + "'";
	}

}
