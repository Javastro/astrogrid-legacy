package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.net.URI;
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
    public Matcher<Resource> createMatcher(final List<String> selected) {
		return new Matcher<Resource>() {
			public boolean matches(final Resource r) {
				final Creator[] creators = r.getCuration().getCreators();
				for (int i = 0; i < creators.length; i++) {
					final Creator c =creators[i];
					if (c !=null) {
						final ResourceName name = c.getName();
						if (name != null) {
						    final URI id = name.getId();
						    if (id != null) {
	                            return selected.contains(name.getValue())
                                || selected.contains(id);						        
						    } else {
	                            return selected.contains(name.getValue());			        
						    }

						}
					}
				}
				return false;
			}					
		};
	}

	@Override
    public TransformedList<Resource,String> createView(final EventList<Resource> base) {
		return new CollectionList<Resource,String>(base,
						new CollectionList.Model<Resource,String>() {
							public List<String> getChildren(final Resource r) {						
								final Creator[] creators = r.getCuration().getCreators();
								final List<String> result = new ArrayList<String>(creators.length);
								for (int i = 0; i < creators.length; i++) {
									final Creator c =creators[i];
									if (c !=null) {
										final ResourceName name = c.getName();
										if (name != null) {
											result.add(name.getValue());
											if (name.getId() != null) {
											    result.add(name.getId().toString());
											}
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