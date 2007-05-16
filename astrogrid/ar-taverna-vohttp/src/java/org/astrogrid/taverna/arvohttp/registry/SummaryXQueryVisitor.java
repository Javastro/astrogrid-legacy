/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** generate an xquery from the query model that queries on the 'summary' elements.
 * @author Noel Winstanley
 * @since Aug 9, 20066:36:57 PM
 */
class SummaryXQueryVisitor implements Builder{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(SummaryXQueryVisitor.class);
  
	public String build(AbstractQuery q, String filter) {
		Object o = q.accept(this);
		StringBuffer sb = new StringBuffer();
		//sb.append("//RootResource[@status='active' and (");
		sb.append("//vor:Resource[@status='active' and (");
		sb.append(o);
    	if (filter != null) {
    		sb.append(") and (").append(filter).append(")");
    	}	else {
    		sb.append(")");
    	}
		sb.append("]");
		System.out.println("the build in summaryxquery = " + sb.toString());
		logger.debug(sb);
		return sb.toString();
	}
	
	private static  final String[] elements = new String[]{"vr:title","vr:identifier","vr:shortName","vr:content/vr:subject","vr:content/vr:description"};
    
	public Object visit(AndQuery q) {
		return "(" + q.getLeft().accept(this) + ") and (" + q.getRight().accept(this) + ")";
	}

	public Object visit(OrQuery q) {
		return "(" + q.getLeft().accept(this) + ") or (" + q.getRight().accept(this) + ")";
	}

	public Object visit(NotQuery q) {
		return "not (" + q.getChild() + ")";
	}

	public Object visit(FuzzyQuery q) {
		return null;
	}

	public Object visit(TermQuery q) {
		return buildClause(q.getTerm());
	}

	public Object visit(PhraseQuery q) {
		return buildClause(q.getPhrase());
	}
	  //strange - more advanced xqeuery (union, some, etc) is very very slow.
    // seems to go faster if we write it out in longhand
    // still using non-standard &=, which is a pity.

	protected String buildClause(String kw) {
		StringBuffer sb = new StringBuffer();
		for (int el = 0; el < elements.length; el++) {
			//sb.append(elements[el]).append(" &= '*").append(kw).append( "*'" );
			sb.append("near(").append(elements[el]).append(",'").append(kw).append( "')" );
			if (el != elements.length -1) {
				sb.append(" or ");
			}
		}// for elements
		System.out.println("buildclause in summaryxquery = " + sb.toString());
		return sb.toString();
	}

}
