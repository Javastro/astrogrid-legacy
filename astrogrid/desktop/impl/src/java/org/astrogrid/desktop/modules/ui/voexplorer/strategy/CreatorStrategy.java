package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Filters on creator.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:36:34 PM
 */
public final class CreatorStrategy extends PipelineStrategy {
	@Override
    public Matcher createMatcher(final List selected) {
		return new Matcher() {
			public boolean matches(final Object arg0) {
				final Resource r = (Resource)arg0;
				final Creator[] creators = r.getCuration().getCreators();
				for (int i = 0; i < creators.length; i++) {
					final Creator c =creators[i];
					if (c !=null) {
						final ResourceName name = c.getName();
						if (name != null) {
							return selected.contains(name.getValue());
						}
					}
				}
				return false;
			}					
		};
	}

	@Override
    public TransformedList createView(final EventList base) {
		return new CollectionList(base,
						new CollectionList.Model() {
							public List getChildren(final Object arg0) {
								final Resource r = (Resource)arg0;
								final Creator[] creators = r.getCuration().getCreators();
								final List result = new ArrayList(creators.length);
								for (int i = 0; i < creators.length; i++) {
									final Creator c =creators[i];
									if (c !=null) {
										final ResourceName name = c.getName();
										if (name != null) {
											result.add(name.getValue());
										}
									}
								}
								return result;
							}
				});
	}

	@Override
    public String getName() {
		return "Creator";
	}
}