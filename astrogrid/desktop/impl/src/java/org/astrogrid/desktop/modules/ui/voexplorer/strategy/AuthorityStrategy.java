package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Filters on authority Id of the resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:35:59 PM
 */
public final class AuthorityStrategy extends PipelineStrategy {
	@Override
    public Matcher<Resource> createMatcher(final List<String> selected) {
		return new Matcher<Resource>() {
			public boolean matches(final Resource r) {
				final String auth = r.getId().getAuthority();
				return selected.contains(auth);
			}					
		};
	}

	@Override
    public TransformedList<Resource,String> createView(final EventList<Resource> base) {
		return new FunctionList<Resource,String>(base,
						new FunctionList.Function<Resource,String>() {
							public String evaluate(final Resource r) {
								return r.getId().getAuthority();
							}
				});
	}

	@Override
    public String getName() {
		return "IVOA-ID Prefix";
	}
}