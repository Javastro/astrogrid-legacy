package org.astrogrid.taverna.arvohttp.registry;

import java.util.Vector;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.taverna.arvohttp.ARTask;

/*
import org.astrogrid.desktop.modules.ivoa.CacheFactory;
import org.astrogrid.desktop.modules.dialogs.registry.AbstractQuery;
import org.astrogrid.desktop.modules.dialogs.registry.QueryParser;
import org.astrogrid.desktop.modules.dialogs.registry.SummaryXQueryVisitor;
import org.astrogrid.desktop.modules.dialogs.registry.FullTextXQueryVisitor;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl.DocumentBuilderStreamProcessor;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceStreamParser;
import RegistryGooglePanel.SearchWorker;

*/
//import net.sf.ehcache.Ehcache;
//import net.sf.ehcache.Element;
import org.w3c.dom.Document;
import org.apache.log4j.Logger;


public class ARRegistrySearch {
	
	private static Logger logger = Logger.getLogger(ARRegistrySearch.class);
	
	public ARRegistrySearch() {
		//do nothing for now.
		
	}
	
    private static final Builder briefXQueryBuilder = new SummaryXQueryVisitor();
    private static final Builder fullTextXQueryBuilder = new FullTextXQueryVisitor();

	public static Resource[] search(Registry reg, String searchTerm, String filter) throws InvalidArgumentException, ServiceException{

	    QueryParser qp = new QueryParser(searchTerm);
	    AbstractQuery q;
		//try {
			q = qp.parse();
			//String summary =(String) q.accept(feedbackVisitor);
			String briefXQuery = briefXQueryBuilder.build(q,filter);
			logger.warn("XQuery from the briefXQueryBuilder = " + briefXQuery);
			Resource []res = reg.xquerySearch(briefXQuery);
			if(res.length == 0) {
				String fullXQuery = fullTextXQueryBuilder.build(q,filter);
				logger.warn("XQuery from the fullTextXQueryBuilder = " + briefXQuery);
				res = reg.xquerySearch(fullXQuery);
			}
			logger.warn("number of resources from arregistrySearch.search = " + res.length);
			return res;
		/*	
		} catch (InvalidArgumentException x) {
			parent.showError("Failed to parse search expression",x);
		}
		*/
	}
}