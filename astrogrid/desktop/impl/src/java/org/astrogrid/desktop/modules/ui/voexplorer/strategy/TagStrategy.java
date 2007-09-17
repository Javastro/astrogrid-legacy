/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;
import org.astrogrid.desktop.modules.votech.Annotation;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.UserAnnotation;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** strategy that that filters on tag annotations.
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

	public Matcher createMatcher(final List selected) {
		return new Matcher() {

			public boolean matches(Object arg0) {
				Resource r = (Resource)arg0;
				boolean seenNone = true;
				for (Iterator i = annServer.getLocalAnnotations(r); i.hasNext(); ) {
					Annotation a = (Annotation)i.next();
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
					Set tags = a.getTags();
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

	public TransformedList createView(EventList base) {
		return new CollectionList(base,
				new CollectionList.Model() {
			public List getChildren(Object arg0) {
				final Resource r = (Resource)arg0;
				List result = NONE_PROVIDED;
				for(Iterator i = annServer.getLocalAnnotations(r); i.hasNext(); ) {
					Annotation a = (Annotation)i.next();
					if (a instanceof UserAnnotation && ((UserAnnotation)a).isFlagged()) {
						if (result == NONE_PROVIDED) {
							result = new ArrayList();
						}						
						result.add(FLAGGED);
					}
					if (StringUtils.isNotBlank(a.getAlternativeTitle())) {
					    if (result == NONE_PROVIDED) {
					        result = new ArrayList();
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
					Set tags = a.getTags();
					if (tags != null && ! tags.isEmpty()) {
						if (result == NONE_PROVIDED) {
							result = new ArrayList(tags.size());
						}
						result.addAll(tags);
					}
				}
				return result;
			}
	});
	}

	public String getName() {
		return "Annotations";
	}

}
