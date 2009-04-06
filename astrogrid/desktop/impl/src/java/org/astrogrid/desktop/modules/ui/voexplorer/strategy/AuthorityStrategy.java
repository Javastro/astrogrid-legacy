package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.List;

import javax.swing.JMenuItem;

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
    public Matcher createMatcher(final List<JMenuItem> selected) {
		return new Matcher() {
			public boolean matches(Object arg0) {
				Resource r = (Resource)arg0;
				String auth = r.getId().getAuthority();
				return selected.contains(auth);
			}					
		};
	}

	@Override
    public TransformedList createView(EventList base) {
		return new FunctionList(base,
						new FunctionList.Function() {
							public Object evaluate(Object arg0) {
								Resource r = (Resource)arg0;
								return r.getId().getAuthority();
							}
				});
	}

	@Override
    public String getName() {
		return "Authority";
	}
}