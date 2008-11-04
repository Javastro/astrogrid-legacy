/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Generate an XQuery from SRQL, using the {@code for - where} form.
 * @author Noel Winstanley
 * @since Aug 9, 20066:36:57 PM
 */
public class BasicRegistrySRQLVisitor implements Builder{
    /**
     * 
     */
    public BasicRegistrySRQLVisitor() {
    }
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory
			.getLog(BasicRegistrySRQLVisitor.class);
	// these are deliberatley not static, so that subclasses can alter them.
	/** list of elements searched by default */
	protected  final String[] defaultTarget = new String[]{"$r/title","$r/identifier","$r/shortName","$r/content/subject","$r/content/description"};
	/** a map of other alternate targets */
	protected  final Map<String,String[]> targets = new HashMap<String,String[]>();
	 { // constructor initializer.
		targets.put("shortname", new String[] {"$r/shortName"});
		targets.put("title",new String[] {"$r/title"});
		targets.put("name",new String[] {"$r/shortName","$r/title"});
		targets.put("id",new String[] {"$r/identifier"});
		targets.put("subject",new String[]{"$r/content/subject"}); 
		targets.put("source",new String[]{"$r/content/source"});
		targets.put("description", new String[] {"$r/content/description"});
		targets.put("default", defaultTarget);
		targets.put("any", new String[] {"$r//*"}); //@todo add attributes too - can't get //@* to work with latest XQuery.
		targets.put("all", new String[] {"$r//*"}); //@todo add attributes too?
		//targets.put("publisher", new String[] {"$r/curation/publisher","$r/curation/publisher/@ivo-id"});
		targets.put("curation",new String[] {"$r/curation//*"});
		targets.put("type", new String[] {
		        "$r/@xsi:type"
		        ,"$r/content/type"
		        ,"$r/capability/@xsi:type"
		 //drags in too much cruft.       ,"$r/capability/@standardID"
		        }); 
		targets.put("level", new String[] {"$r/content/contentLevel"});
		targets.put("waveband",new String[] {"$r/coverage/waveband"}); 
		targets.put("col",     new String[]{
                "$r/catalog/table/column/name",
                "$r/table/column/name"});    
		targets.put("ucd",		 new String[]{
		        "$r/catalog/table/column/ucd",
		        "$r/table/column/ucd"});		
		//
	}	
   
	public String build(final SRQL q, final String filter) {
		final Object o = q.accept(this);
		final StringBuffer sb = new StringBuffer();
		sb.append("for $r in //vor:Resource[not (@status = 'inactive' or @status = 'deleted')]\nwhere (");
    	if (filter != null) { // apply the filter first - as should restrict faster.
    		sb.append(filter).append(") and (");
    	}	
    	sb.append(o);
		sb.append(")\nreturn $r");
		logger.debug(sb);
		return sb.toString();
	}
	/** keeps track of where the current clause of the SRQL is being targetted at.*/
	protected String[] currentTarget = defaultTarget;
	
	/** reset targetting. */
	protected void resetTarget() {
		currentTarget= defaultTarget;
	}
	
	protected void setTarget(final String target) {
		final String[] candidate = targets.get(target.trim().toLowerCase());
		if (candidate != null) {
			currentTarget = candidate;
		}
	}
	
    
	public Object visit(final AndSRQL q) {
		return "(" + q.getLeft().accept(this) + ") and (" + q.getRight().accept(this) + ")";
	}

	public Object visit(final OrSRQL q) {
		return "(" + q.getLeft().accept(this) + ") or (" + q.getRight().accept(this) + ")";
	}

	public Object visit(final NotSRQL q) {
		return "not (" + q.getChild().accept(this) + ")";
	}

	public Object visit(final TermSRQL q) {
		return buildClause(q.getTerm());
	}

	public Object visit(final PhraseSRQL q) {
		return buildClause(q.getPhrase());
	}
	public Object visit(final TargettedSRQL q) {
		setTarget(q.getTarget());
		final Object result = q.getChild().accept(this);
		resetTarget();
		return result;
	}
	

	
	public Object visit(final XPathSRQL q) {
		return q.getXpath(); // just gets inlined.
	}
	  //strange - more advanced xqeuery (union, some, etc) is very very slow.
    // seems to go faster if we write it out in longhand
    // still using non-standard &=, which is a pity.

	protected String buildClause(final String kw) {
		final StringBuffer sb = new StringBuffer();
		for (int el = 0; el < currentTarget.length; el++) {
			sb.append(currentTarget[el]).append(" &= '*").append(kw).append( "*'" );
			if (el != currentTarget.length -1) {
				sb.append(" or ");
			}
		}// for elements
		return sb.toString();
	}


}
