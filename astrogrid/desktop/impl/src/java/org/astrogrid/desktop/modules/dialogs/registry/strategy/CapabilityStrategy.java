/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.dialogs.registry.FilterPipelineFactory.PipelineStrategy;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Strategy for filtering based on capability.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:49:49 PM
 */
public class CapabilityStrategy extends PipelineStrategy {

	public Matcher createMatcher(final List selected) {
		return new Matcher() {
			public boolean matches(Object arg0) {
				final Capability[] c = getCapabilities(arg0);
				if (c == null) {
					return false; // obvioousluy nt a service.
				}				
				for (int i = 0; i < c.length; i++) {
					String subj = ResourceFormatter.formatType(c[i].getType());
					if (selected.contains(subj)) {
						return true;
					}
				}
				return false;
			}					
		};
	}

	private final Capability[] getCapabilities(Object o) {
		if (o instanceof Service) {
			return ((Service)o).getCapabilities();
		} else return null;
	}
	
	public TransformedList createView(EventList base) {	
		return new CollectionList(base,
			new CollectionList.Model() {
		public List getChildren(Object arg0) {
			final Capability[] c = getCapabilities(arg0);
			if (c == null) {
				return ListUtils.EMPTY_LIST;
			}			
			final List result = new ArrayList(c.length);
			for (int i = 0; i < c.length; i++) {
				result.add(ResourceFormatter.formatType(c[i].getType()));
			}
			return result;
		}
});
}

	public String getName() {
		return "Service - Capability";
	}

}