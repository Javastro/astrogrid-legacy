/**
 * 
 */
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

/**Filters on content level.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:49:49 PM
 */
public class ContentLevelStrategy extends PipelineStrategy {

	@Override
    public Matcher createMatcher(final List selected) {
		return new Matcher() {
			public boolean matches(final Object arg0) {
				final Resource r = (Resource)arg0;
				final Content content = r.getContent();
				if (content == null || content.getSubject() == null || content.getSubject().length == 0) {
					return selected.contains(NONE_PROVIDED.get(0));
				}
				final String[] subjects = content.getContentLevel();
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
			if (content == null || content.getContentLevel() == null || content.getContentLevel().length == 0) {
				return NONE_PROVIDED;
			}
			final String[] level =  content.getContentLevel();
			final List result = new ArrayList(level.length);
			for (int i = 0; i < level.length; i++) {
				result.add(level[i]);
			}
			return result;
		}
});
}

	@Override
    public String getName() {
		return "Content - Level";
	}

}
