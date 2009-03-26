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
    public Matcher createMatcher(final List selected) {
		return new Matcher() {
	
			public boolean matches(final Object arg0) {
				final Resource r = (Resource)arg0;
				final String type = r.getType();
				return selected.contains(PrettierResourceFormatter.formatType(type));
			}
		};
	}

	@Override
    public TransformedList createView(final EventList base) {
		return new FunctionList(base,
				new FunctionList.Function() {

					public Object evaluate(final Object arg0) {
						final Resource r = (Resource)arg0;
						final String rawType = r.getType();
						return PrettierResourceFormatter.formatType(rawType);
					}
		});
	}

	@Override
    public String getName() {
		return "Resource Type";
	}
}