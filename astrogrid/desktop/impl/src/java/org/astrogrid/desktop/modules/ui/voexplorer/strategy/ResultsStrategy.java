/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.scope.Retriever;
import org.astrogrid.desktop.modules.ui.scope.RetrieverService;
import org.astrogrid.desktop.modules.ui.scope.ScopeServicesList;
import org.astrogrid.desktop.modules.ui.scope.QueryResults.QueryResult;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Specialized strategy that filters on <b>astroscope results</b> - only used within {@link ScopeServicesList} 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 6, 20083:33:42 PM
 */
public class ResultsStrategy extends PipelineStrategy {
      
    private final ScopeServicesList services;
    /** I pass in the whole scope service list, rather than the query-results 
     * subcomponent as at this point it hasn't been initialized
     * @param servicesList
     */
    public ResultsStrategy(final ScopeServicesList servicesList) {
       
        this.services = servicesList;
    }

    @Override
    public Matcher<Resource> createMatcher(final List<String> selected) {
        return new Matcher<Resource>() {
            public boolean matches(final Resource arg0) {
                final Retriever r = ((RetrieverService)arg0).getRetriever();
                final QueryResult result = services.getQueryResults().getResult(r);
                if (result == null) {
                    return false;
                } 
                return selected.contains(result.getSummarizedResultCount());
            }
        };
    }

    @Override
    public TransformedList<Resource,String> createView(final EventList<Resource> base) {
        return new FunctionList<Resource,String>(base,
                new FunctionList.Function<Resource,String>() {
            public String evaluate(final Resource arg0) {
                final Retriever r = ((RetrieverService)arg0).getRetriever();
                final QueryResult result = services.getQueryResults().getResult(r);
                if (result == null) {
                    return "Failed";
                }
                return result.getSummarizedResultCount();
            }        
        });
    }

    @Override
    public String getName() {
        return "Results";
    }

}
