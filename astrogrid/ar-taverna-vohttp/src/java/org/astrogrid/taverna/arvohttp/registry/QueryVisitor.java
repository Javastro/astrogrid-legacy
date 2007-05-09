/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

/** visitor pattern over our query object model.
 * @author Noel Winstanley
 * @since Aug 9, 20062:49:29 PM
 */
public interface QueryVisitor {
	public Object visit(AndQuery q) ;
	public Object visit(OrQuery q) ;
	public Object visit(NotQuery q);
	public Object visit(FuzzyQuery q) ;
	public Object visit(TermQuery q);
	public Object visit(PhraseQuery q);
}
