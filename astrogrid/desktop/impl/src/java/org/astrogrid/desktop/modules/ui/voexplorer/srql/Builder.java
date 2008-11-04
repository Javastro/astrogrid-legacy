/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;


/** Specialization of {@link SRQLVisitor}  that produces a string.
 * @author Noel Winstanley
 * @since Aug 11, 200610:11:04 AM
 */
public interface Builder extends SRQLVisitor {
	public String build(SRQL q,String filter);

}
