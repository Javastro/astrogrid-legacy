package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.PrettierResourceFormatter;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/**Filters on the type of resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:35:33 PM
 */
public final class TypeStrategy extends PipelineStrategy {
	@Override
    public Matcher<Resource> createMatcher(final List<String> selected) {
		return new Matcher<Resource>() {
	
			public boolean matches(final Resource r) {
				return selected.contains(PrettierResourceFormatter.formatResourceType(r));
			}
		};
	}

	@Override
    public TransformedList<Resource,String> createView(final EventList<Resource> base) {
		return new FunctionList<Resource,String>(base,
				new FunctionList.Function<Resource,String>() {

					public String evaluate(final Resource r) {
						return PrettierResourceFormatter.formatResourceType(r);
					}
		});
	}

	@Override
    public String getName() {
		return "Resource Type";
	}
}