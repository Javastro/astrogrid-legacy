/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import org.astrogrid.acr.ivoa.resource.Capability;
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
    public Matcher createMatcher(final List<JMenuItem> selected) {
		return new Matcher() {
			public boolean matches(final Object arg0) {
				final Capability[] c = getCapabilities(arg0);
				if (c == null) {
					return selected.contains(NONE_PROVIDED.get(0)); // obvioousluy nt a service.
				}				
				for (int i = 0; i < c.length; i++) {
					final String subj = PrettierResourceFormatter.formatType(c[i].getType());
					if (selected.contains(subj)) {
						return true;
					}
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
    public TransformedList createView(final EventList base) {	
		return new CollectionList(base,
			new CollectionList.Model() {
		public List getChildren(final Object arg0) {
			final Capability[] c = getCapabilities(arg0);
			if (c == null || c.length == 0) {
				return NONE_PROVIDED;
			}			
			final List result = new ArrayList(c.length);
			for (int i = 0; i < c.length; i++) {
				result.add(PrettierResourceFormatter.formatType(c[i].getType()));
			}
			return result;
		}
});
}

	@Override
    public String getName() {
		return "Service - Capability";
	}

}
