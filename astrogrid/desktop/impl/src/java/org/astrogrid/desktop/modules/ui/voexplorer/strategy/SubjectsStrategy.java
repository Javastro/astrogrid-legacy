package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Filters on subjects.
 * 
 * @todo handle malformed registry entries with comma-separated lists of values.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:36:34 PM
 */
public final class SubjectsStrategy extends PipelineStrategy {
	@Override
    public Matcher createMatcher(final List selected) {
		return new Matcher() {
			public boolean matches(final Object arg0) {
				final Resource r = (Resource)arg0;
				final Content content = r.getContent();
				if (content == null || content.getSubject() == null || content.getSubject().length == 0) {
					return selected.contains(NONE_PROVIDED.get(0));
				}
				final String[] subjects = content.getSubject();
				for (int i = 0; i < subjects.length; i++) {
					final String subj = subjects[i];
					if (selected.contains(subj)) {
						return true;
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
								final Content content = r.getContent();
								if (content == null || content.getSubject() == null || content.getSubject().length == 0) {
									return NONE_PROVIDED;
								}
								final String[] subs =  content.getSubject();
								final List result = new ArrayList(subs.length);
								for (int i = 0; i < subs.length; i++) {
									result.add(subs[i]);
								}
								
								return result;
							}
				});
	}

	@Override
    public String getName() {
		return "Content - Subject";
	}
}