/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry.srql;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** generate an xquery from SRQL that queries on the 'summary' elements of a resource
 * @author Noel Winstanley
 * @since Aug 9, 20066:36:57 PM
 */
public class BasicRegistrySRQLVisitor implements Builder{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(BasicRegistrySRQLVisitor.class);
	/** list of elements searched by default */
	private static  final String[] defaultTarget = new String[]{"$r/vr:title","$r/vr:identifier","$r/vr:shortName","$r/vr:content/vr:subject","$r/vr:content/vr:description"};
	/** a map of other alternate targets */
	private static Map targets = new HashMap();
	static {
		targets.put("shortname", new String[] {"$r/vr:shortName"});
		targets.put("title",new String[] {"$r/vr:title"});
		targets.put("name",new String[] {"$r/vr:shortName","$r/vr:title"});
		targets.put("id",new String[] {"$r/vr:identifier"});
		targets.put("subject",new String[]{"$r/vr:content/vr:subject"}); 
		targets.put("description", new String[] {"$r/vr:content/vr:description"});
		targets.put("default", defaultTarget);
		targets.put("any", new String[] {"$r//*"}); //@todo add attributes too - can't get //@* to work with latest XQuery.
		targets.put("all", new String[] {"$r//*"}); //@todo add attributes too?
		//targets.put("publisher", new String[] {"$r/vr:curation/vr:publisher","$r/vr:curation/vr:publisher/@ivo-id"});
		targets.put("curation",new String[] {"$r/vr:curation//*"});
		targets.put("type", new String[] {"@xsi:type","$r/vr:content/vr:type"});
		targets.put("level", new String[] {"$r/vr:content/vr:contentLevel"});
		targets.put("waveband",new String[] {"$r/vs:coverage/vs:spectral/vs:waveband"}); 
		targets.put("col",new String[]{"$r//vods:column/vods:name"});
				// combines - "$r/vods:table/vods:column/vods:name", "$r/tdb:db/tdb:table/vods:column/vods:name});
		targets.put("ucd",new String[]{"$r//vods:column/vods:ucd"});
				// likewise, search for both"$r/vods:table/vods:column/vods:ucd"});
		//
	}	
   
	public String build(SRQL q, String filter) {
		Object o = q.accept(this);
		StringBuffer sb = new StringBuffer();
		sb.append("for $r in //vor:Resource[not (@status = 'inactive' or @status = 'deleted')]\nwhere (");
    	if (filter != null) { // apply the filter first - as should restrict faster.
    		sb.append(filter).append(") and (");
    	}	
    	sb.append(o);
		sb.append(")\nreturn $r");
		logger.debug(sb);
		return sb.toString();
	}
	// keeps track of where the current clause of the SRQL is being targetted at.
	private String[] currentTarget = defaultTarget;
	
	// reset targetting.
	private void resetTarget() {
		currentTarget= defaultTarget;
	}
	
	private void setTarget(String target) {
		String[] candidate = (String[])targets.get(target.trim().toLowerCase());
		if (candidate != null) {
			currentTarget = candidate;
		}
	}
	
    
	public Object visit(AndSRQL q) {
		return "(" + q.getLeft().accept(this) + ") and (" + q.getRight().accept(this) + ")";
	}

	public Object visit(OrSRQL q) {
		return "(" + q.getLeft().accept(this) + ") or (" + q.getRight().accept(this) + ")";
	}

	public Object visit(NotSRQL q) {
		return "not (" + q.getChild().accept(this) + ")";
	}

	public Object visit(TermSRQL q) {
		return buildClause(q.getTerm());
	}

	public Object visit(PhraseSRQL q) {
		return buildClause(q.getPhrase());
	}
	public Object visit(TargettedSRQL q) {
		setTarget(q.getTarget());
		Object result = q.getChild().accept(this);
		resetTarget();
		return result;
	}
	

	
	public Object visit(XPathSRQL q) {
		return q.getXpath(); // just gets inlined.
	}
	  //strange - more advanced xqeuery (union, some, etc) is very very slow.
    // seems to go faster if we write it out in longhand
    // still using non-standard &=, which is a pity.

	protected String buildClause(String kw) {
		StringBuffer sb = new StringBuffer();
		for (int el = 0; el < currentTarget.length; el++) {
			sb.append(currentTarget[el]).append(" &= '*").append(kw).append( "*'" );
			if (el != currentTarget.length -1) {
				sb.append(" or ");
			}
		}// for elements
		return sb.toString();
	}


}
