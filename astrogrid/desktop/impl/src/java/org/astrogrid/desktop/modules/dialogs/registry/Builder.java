/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry;

/** custom visitor that produces a string query.
 * @author Noel Winstanley
 * @since Aug 11, 200610:11:04 AM
 */
public interface Builder extends QueryVisitor {
	public String build(AbstractQuery q,String filter);

}
