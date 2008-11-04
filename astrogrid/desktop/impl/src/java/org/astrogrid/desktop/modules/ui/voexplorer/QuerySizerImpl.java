/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.Builder;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.HeadClauseSRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;

/** Implementation of {@link QuerySizer}.
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
    private final Preference preventLargeQueries;
    private final int oversizeThreshold;
    private final int goodThreshold;
	
	public QuerySizerImpl(final RegistryInternal reg, final Ehcache cache, final Preference preventLargeQueries
	        ,final int goodThreshold, final int oversizeThreshold) {
		super();
		this.reg = reg;
		this.cache = cache;
        this.preventLargeQueries = preventLargeQueries;
        this.goodThreshold = goodThreshold;
        this.oversizeThreshold = oversizeThreshold;
	}
	
	public synchronized Integer regSize() { // synchronize this, so query to reg is only run one time.
		return size(ALL_QUERY);
	}
	/** the query that returns all 'active' resources. use with caution */
	public static final String ALL_QUERY = "//vor:Resource[not (@status='inactive' or @status='deleted')]";
	
	public Integer size(final SRQL query) {
		try {
		final String q = queryBuilder.build(query,null);
		return size(q);
		} catch (final IllegalArgumentException e) {
			// query is currently incomplete.
			return ERROR;
		}
	}
	
	public Integer size(final String query) {
		final String sizingQuery = constructSizingQuery(query);
		final Element result = cache.get(sizingQuery);
		if (result != null) {
			return (Integer)result.getValue();
		} else {
			try {
				final SizingStreamProcessor proc = new SizingStreamProcessor();
				reg.xquerySearchStream(sizingQuery,proc);
				final Integer computed = proc.getResult();
				if (! computed.equals(ERROR)) {
					cache.put(new Element(sizingQuery,computed));
				}
				return computed;
			} catch (final ServiceException x) {
				return ERROR;
			}
		}
		
	}

    /** construct a query which will return a result of format &lt;size>size of query&lt;/size>
     * @param query
     * @return
     */
    public static String constructSizingQuery(final String query) {
        if (query == null) {
            throw new IllegalArgumentException("null query");
        }
        return "let $sizeResults := ( " + query.trim() + ") return <size>{count($sizeResults)}</size>";
    }
	
	private final Builder queryBuilder = new HeadClauseSRQLVisitor();
//	this functionality already in HeadClauseSV
//	static class OnlyCompleteQueriesVisitor extends HeadClauseSRQLVisitor/*BasicRegistrySRQLVisitor*/ {
//		public Object visit(TermSRQL q) {
//			if (q.getTerm() == null || q.getTerm().trim().length() == 0) {
//				throw new IllegalArgumentException("Not a complete query");
//			}
//			return super.visit(q);
//		}
//	}
	/** a xml stream process that parses the registry response to extract the sizing */
	private static class SizingStreamProcessor implements StreamProcessor {
		private Integer size = ERROR;
		public void process(final XMLStreamReader r) throws XMLStreamException {
				r.next();
				// a bit unexpected, but works. - seems like the wrapper xml isn't being passed back by the reg client.
				final String szString = r.getText();
					try{
						size = Integer.valueOf(szString);
					} catch (final NumberFormatException e) {
					    //ignored
					}
		}
		public Integer getResult() {
			return size;
		}
	}

    public boolean isPreventOversizeQueries() {
        return preventLargeQueries.asBoolean();
    }

    public int getOversizeThreshold() {
        return oversizeThreshold;
    }

    public int getGoodThreshold() {
        return goodThreshold;
    }
	
}
