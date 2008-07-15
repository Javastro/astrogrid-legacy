/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import javax.xml.stream.XMLStreamReader;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.Builder;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.HeadClauseSRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.TermSRQL;

/** Class that estimates size of queries. 
 * 
 * returns Integer objects, instead of int primitives, as it's easier to cache these
 * and these methods are often called from a background worker - which prefers to 
 * manipulat objects thather than primitives itself.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 27, 200711:18:35 AM
 */
public class QuerySizerImpl implements QuerySizer {
	private final RegistryInternal reg;
	private final Ehcache cache;
	
	public QuerySizerImpl(final RegistryInternal reg, Ehcache cache) {
		super();
		this.reg = reg;
		this.cache = cache;
	}
	
	public synchronized Integer regSize() { // synchronize this, so query to reg is only run one time.
		return size(ALL_QUERY);
	}
	/** the query that returns all 'active' resources. use with caution */
	public static final String ALL_QUERY = "//vor:Resource[not (@status='inactive' or @status='deleted')]";
	
	public Integer size(SRQL query) {
		try {
		final String q = queryBuilder.build(query,null);
		return size(q);
		} catch (IllegalArgumentException e) {
			// query is currently incomplete.
			return ERROR;
		}
	}
	
	public Integer size(String query) {
		String sizingQuery = constructSizingQuery(query);
		Element result = cache.get(sizingQuery);
		if (result != null) {
			return (Integer)result.getValue();
		} else {
			try {
				final SizingStreamProcessor proc = new SizingStreamProcessor();
				reg.xquerySearchStream(sizingQuery,proc);
				Integer computed = proc.getResult();
				if (! computed.equals(ERROR)) {
					cache.put(new Element(sizingQuery,computed));
				}
				return computed;
			} catch (ServiceException x) {
				return ERROR;
			}
		}
		
	}

    /** construct a query which will return a result of format &lt;size>size of query&lt;/size>
     * @param query
     * @return
     */
    public static String constructSizingQuery(String query) {
        if (query == null) {
            throw new IllegalArgumentException("null query");
        }
        return "let $sizeResults := ( " + query.trim() + ") return <size>{count($sizeResults)}</size>";
    }
	
	private final Builder queryBuilder = new OnlyCompleteQueriesVisitor();
	
	static class OnlyCompleteQueriesVisitor extends HeadClauseSRQLVisitor/*BasicRegistrySRQLVisitor*/ {
		public Object visit(TermSRQL q) {
			if (q.getTerm() == null || q.getTerm().trim().length() == 0) {
				throw new IllegalArgumentException("Not a complete query");
			}
			return super.visit(q);
		}
	}
	
	private static class SizingStreamProcessor implements StreamProcessor {
		private Integer size = ERROR;
		public void process(XMLStreamReader r) throws Exception {
				r.next();
				// a bit unexpected, but works. - seems like the wrapper xml isn't being passed back by the reg client.
				String szString = r.getText();
					try{
						size = Integer.valueOf(szString);
					} catch (NumberFormatException e) {
					    //ignored
					}
		}
		public Integer getResult() {
			return size;
		}
	}
	
}
