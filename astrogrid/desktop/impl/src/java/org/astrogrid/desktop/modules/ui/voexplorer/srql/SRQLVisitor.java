/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

/** visitor pattern over the SRQL model.
 * it's the visitor's responsibility to call accept() on sub terms - the accept methods
 * of the SRQL classes don't do the recursive calls for you.
 * @author Noel Winstanley
 * @since Aug 9, 20062:49:29 PM
 */
public interface SRQLVisitor {
	public Object visit(AndSRQL q) ;
	public Object visit(OrSRQL q) ;
	public Object visit(NotSRQL q);
	public Object visit(TermSRQL q);
	public Object visit(PhraseSRQL q);
	public Object visit(TargettedSRQL q);
	public Object visit(XPathSRQL q);
}
