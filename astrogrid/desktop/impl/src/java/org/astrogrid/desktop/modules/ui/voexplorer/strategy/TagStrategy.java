/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;
import org.astrogrid.desktop.modules.votech.Annotation;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.UserAnnotation;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Filters on various kinds of annotation.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 20, 20071:47:40 AM
 */
public class TagStrategy extends PipelineStrategy {

	/**
     * 
     */
    private static final String ANNOTATED = "ANNOTATED";
    /**
     * 
     */
    private static final String RETITLED = "RETITLED";
    /**
     * 
     */
    private static final String FLAGGED = "FLAGGED";
    private final AnnotationService annServer;
	
	public TagStrategy(final AnnotationService annServer) {
		super();
		this.annServer = annServer;
	}

	@Override
    public Matcher<Resource> createMatcher(final List<String> selected) {
		return new Matcher<Resource>() {

			public boolean matches(final Resource r) {
				boolean seenNone = true;
				for (final Iterator i = annServer.getLocalAnnotations(r); i.hasNext(); ) {
					final Annotation a = (Annotation)i.next();
					if (a instanceof UserAnnotation 
							&& ((UserAnnotation)a).isFlagged()
							&& selected.contains(FLAGGED)) {
						return true;
					}			
					if (selected.contains(RETITLED) && StringUtils.isNotBlank(a.getAlternativeTitle())) {
					    return true;
					}
// doesn't work - finds all the notes from vomon too.					
//					if (selected.contains(ANNOTATED) && StringUtils.isNotBlank(a.getNote())) {
//					    System.err.println(a.getNote());
//					    return true;
//					}
					final Set<String> tags = a.getTags();
					if (tags != null && ! tags.isEmpty()) {
						seenNone = false;
						if (CollectionUtils.containsAny(selected,tags)) {
						    return true;
						}						
					}
				}
				return seenNone && selected.contains(NONE_PROVIDED.get(0));
			}
		};
	}

	@Override
    public TransformedList<Resource,String> createView(final EventList<Resource> base) {
		return new CollectionList<Resource,String>(base,
				new CollectionList.Model<Resource,String>() {
			public List<String> getChildren(final Resource r) {
				List<String> result = NONE_PROVIDED;
				for(final Iterator i = annServer.getLocalAnnotations(r); i.hasNext(); ) {
					final Annotation a = (Annotation)i.next();
					if (a instanceof UserAnnotation && ((UserAnnotation)a).isFlagged()) {
						if (result == NONE_PROVIDED) {
							result = new ArrayList<String>();
						}						
						result.add(FLAGGED);
					}
					if (StringUtils.isNotBlank(a.getAlternativeTitle())) {
					    if (result == NONE_PROVIDED) {
					        result = new ArrayList<String>();
					    }
					    result.add(RETITLED);
					}
					// doesn't work - finds all the notes from vomon too.       
//				    if (StringUtils.isNotBlank(a.getNote())) {
//				        if (result == NONE_PROVIDED) {
//				            result = new ArrayList();
//				        }
//				        result.add(ANNOTATED);
//				    }
					final Set<String> tags = a.getTags();
					if (tags != null && ! tags.isEmpty()) {
						if (result == NONE_PROVIDED) {
							result = new ArrayList<String>(tags.size());
						}
						result.addAll(tags);
					}
				}
				return result;
			}
	});
	}

	@Override
    public String getName() {
		return "Annotations";
	}

}
