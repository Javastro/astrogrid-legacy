/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry;

/** visitor that produces a full text query from the query model.
 * @author Noel Winstanley
 * @since Aug 11, 20069:52:09 AM
 */
public class FullTextXQueryVisitor extends SummaryXQueryVisitor {

protected String buildClause(String kw) {
	return "$r//* &= '*" + kw + "*'";
}

}
