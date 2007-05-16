/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

/** visitor that produces a full text query from the query model.
 * @author Noel Winstanley
 * @since Aug 11, 20069:52:09 AM
 */
public class FullTextXQueryVisitor extends SummaryXQueryVisitor {

protected String buildClause(String kw) {
	//return "$r//* &= '*" + kw + "*'";
	System.out.println("returning in buildClause fulltextxqueryvistor = " + "near('//*','" + kw + "')");
	return "near(.//*,'" + kw + "')";
}

}
