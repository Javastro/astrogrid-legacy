package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Strategy for filtering on the publisher of the resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:35:33 PM
 */
public final class PublisherStrategy extends PipelineStrategy {
	public Matcher createMatcher(final List selected) {
		return new Matcher() {
	
			public boolean matches(Object arg0) {
				Resource r = (Resource)arg0;
				final ResourceName publisher = r.getCuration().getPublisher();
				if (publisher != null) {
					return selected.contains(publisher.getValue());
				}
				return selected.contains(NONE_PROVIDED.get(0));
			}
		};
	}

	public TransformedList createView(EventList base) {
		return new FunctionList(base,
				new FunctionList.Function() {

					public Object evaluate(Object arg0) {
						Resource r = (Resource)arg0;
						final ResourceName publisher = r.getCuration().getPublisher();
						if (publisher == null) { // ouch
							return NONE_PROVIDED.get(0); 
						}
						return publisher.getValue();
					}
		});
	}

	public String getName() {
		return "Publisher";
	}
}