package org.astrogrid.desktop.modules.dialogs.registry.strategy;

import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.dialogs.registry.FilterPipelineFactory.PipelineStrategy;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Strategy for filtering on the type of resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:35:33 PM
 */
public final class TypeStrategy extends PipelineStrategy {
	public Matcher createMatcher(final List selected) {
		return new Matcher() {
	
			public boolean matches(Object arg0) {
				Resource r = (Resource)arg0;
				String type = r.getType();
				return selected.contains(ResourceFormatter.formatType(type));
			}
		};
	}

	public TransformedList createView(EventList base) {
		return new FunctionList(base,
				new FunctionList.Function() {

					public Object evaluate(Object arg0) {
						Resource r = (Resource)arg0;
						String rawType = r.getType();
						return ResourceFormatter.formatType(rawType);
					}
		});
	}

	public String getName() {
		return "Type";
	}
}