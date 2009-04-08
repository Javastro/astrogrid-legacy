/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ivoa.resource.PrettierResourceFormatter;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Filters on capability.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:49:49 PM
 */
public class CapabilityStrategy extends PipelineStrategy {

	@Override
    public Matcher<Resource> createMatcher(final List<String> selected) {
		return new Matcher<Resource>() {
			public boolean matches(final Resource arg0) {
				final Capability[] c = getCapabilities(arg0);
				if (c == null) {
					return selected.contains(NONE_PROVIDED.get(0)); // obvioousluy nt a service.
				}				
				for (final Capability cap : c) {
					final String subj = PrettierResourceFormatter.formatType(cap.getType());
					if (selected.contains(subj)) {
						return true;
					}
//					final URI id = cap.getStandardID();
//					if (id != null && selected.contains(id.toString())) {
//					   return true;
//					}
				}
				return false;
			}					
		};
	}

	private final Capability[] getCapabilities(final Object o) {
		if (o instanceof Service) {
			return ((Service)o).getCapabilities();
		} else {
            return null;
        }
	}
	
	@Override
    public TransformedList<Resource,String> createView(final EventList<Resource> base) {	
		return new CollectionList<Resource,String>(base,
			new CollectionList.Model<Resource,String>() {
		public List<String> getChildren(final Resource arg0) {
			final Capability[] c = getCapabilities(arg0);
			if (c == null || c.length == 0) {
				return NONE_PROVIDED;
			}			
			final List<String> result = new ArrayList<String>(c.length);
			for (final Capability cap : c) {
				result.add(PrettierResourceFormatter.formatType(cap.getType()));
//				final URI uri = cap.getStandardID();
//				if (uri != null) {
//				    result.add(uri.toString());
//				}
			}
			return result;
		}
});
}

	@Override
    public String getName() {
		return "Service Capability";
	}

}
