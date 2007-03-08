/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry.srql;


/** custom visitor that produces a string query.
 * @author Noel Winstanley
 * @since Aug 11, 200610:11:04 AM
 */
public interface Builder extends SRQLVisitor {
	public String build(SRQL q,String filter);

}
