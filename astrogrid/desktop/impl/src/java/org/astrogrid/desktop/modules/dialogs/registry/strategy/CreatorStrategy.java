package org.astrogrid.desktop.modules.dialogs.registry.strategy;

import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.desktop.modules.dialogs.registry.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Strategy for filtering on list of subjects.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:36:34 PM
 */
public final class CreatorStrategy extends PipelineStrategy {
	public Matcher createMatcher(final List selected) {
		return new Matcher() {
			public boolean matches(Object arg0) {
				Resource r = (Resource)arg0;
				final Creator[] creators = r.getCuration().getCreators();
				for (int i = 0; i < creators.length; i++) {
					Creator c =creators[i];
					if (c !=null) {
						ResourceName name = c.getName();
						if (name != null) {
							return selected.contains(name.getValue());
						}
					}
				}
				return false;
			}					
		};
	}

	public TransformedList createView(EventList base) {
		return new CollectionList(base,
						new CollectionList.Model() {
							public List getChildren(Object arg0) {
								final Resource r = (Resource)arg0;
								final Creator[] creators = r.getCuration().getCreators();
								final List result = new ArrayList(creators.length);
								for (int i = 0; i < creators.length; i++) {
									Creator c =creators[i];
									if (c !=null) {
										ResourceName name = c.getName();
										if (name != null) {
											result.add(name.getValue());
										}
									}
								}
								return result;
							}
				});
	}

	public String getName() {
		return "Creator";
	}
}