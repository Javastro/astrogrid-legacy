package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Filtering on the' source' of a resource - the publication it came from.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:35:33 PM
 */
public final class SourceStrategy extends PipelineStrategy {
	@Override
    public Matcher createMatcher(final List selected) {
		return new Matcher() {
	
			public boolean matches(final Object arg0) {
				final Resource r = (Resource)arg0;
				final Source source = r.getContent().getSource();
				if (source != null && source.getValue() != null) {
					return selected.contains(source.getValue());
				}
				return selected.contains(NONE_PROVIDED.get(0));
			}
		};
	}

	@Override
    public TransformedList createView(final EventList base) {
		return new FunctionList(base,
				new FunctionList.Function() {

					public Object evaluate(final Object arg0) {
						final Resource r = (Resource)arg0;
		                final Source source = r.getContent().getSource();
						if (source == null || source.getValue() == null) {
							return NONE_PROVIDED.get(0); 
						}
						return source.getValue();
					}
		});
	}

	@Override
    public String getName() {
		return "Source";
	}
}