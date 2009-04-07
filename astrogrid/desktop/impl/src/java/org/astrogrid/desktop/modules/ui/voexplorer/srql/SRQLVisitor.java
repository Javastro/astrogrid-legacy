/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

/** Visitor that traverses a SRQL parse tree.
 * it's the visitor's responsibility to call accept() on sub terms - the accept methods
 * of the SRQL classes don't do the recursive calls for you.
 * @author Noel Winstanley
 * @param <R> return type when visiting an element.
 * @since Aug 9, 20062:49:29 PM
 */
public interface SRQLVisitor<R> {
	public R visit(AndSRQL q) ;
	public R visit(OrSRQL q) ;
	public R visit(NotSRQL q);
	public R visit(TermSRQL q);
	public R visit(PhraseSRQL q);
	public R visit(TargettedSRQL q);
	public R visit(XPathSRQL q);
}
