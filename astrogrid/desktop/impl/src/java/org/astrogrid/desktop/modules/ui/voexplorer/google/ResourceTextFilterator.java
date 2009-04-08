/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.HasCoverage;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.desktop.modules.votech.Annotation;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.UserAnnotation;

import ca.odell.glazedlists.Filterator;
import ca.odell.glazedlists.TextFilterator;

/** Filterator for resources - extracts fields from a resource that are used in incremental searching. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20076:55:58 PM
 */
public final class ResourceTextFilterator implements Filterator<String,Resource>, TextFilterator<Resource> {

	private final AnnotationService annotationService;
	
	
	public ResourceTextFilterator(final AnnotationService annotationService) {
		super();
		this.annotationService = annotationService;
	}

	public void getFilterValues(final List<String> l, final Resource res) {
	    l.add(res.getId().toString());
	    l.add(res.getTitle());
	    l.add(res.getShortName());
	    
	    // conntent
	    final Content content = res.getContent();
	    if (content != null) {
	        final String[] subject = content.getSubject();
	        if (subject != null) {
	            for (int i = 0; i < subject.length; i++) {
	                l.add(subject[i]);
	            }
	        }
	        final String[] type = content.getType();
	        if (type != null) {
	            for (int i = 0; i < type.length; i++) {
	                l.add(type[i]);
	            }
	        }
	        // unsure whether to include this one..
	        l.add(content.getDescription());
	    }
	    
	    // curation
	    final Curation curation = res.getCuration();
	    if (curation != null) {
	        final Creator[] creators = curation.getCreators();
	        if (creators != null) {
	           for (int i =0; i < creators.length; i++) {
	               final ResourceName name = creators[i].getName();
	               if (name != null) {
	                   if (name.getValue() != null) {
	                       l.add(name.getValue());
	                   }
	                   if (name.getId() != null) {
	                       l.add(name.getId().toString());
	                   }
	               }
	           }
	        }
	        final ResourceName publisher = curation.getPublisher();
	        if (publisher != null) {
	            if (publisher.getValue() != null) {
	                l.add(publisher.getValue());
	            }
	            if (publisher.getId() != null) {
	                l.add(publisher.getId().toString());
	            }
	        }
	        
	        final ResourceName[] contributors = curation.getContributors();
	        if (contributors != null) {
	            for (int i = 0; i < contributors.length ; i++) {
	                final String value = contributors[i].getValue();
	                if (value != null) {
	                    l.add(value);
	                }
	            }
	        }
	        final Contact[] contacts = curation.getContacts();
	        if (contacts != null) {
	            for (int i =0; i < contacts.length; i++) {
	                final ResourceName name = contacts[i].getName();
	                if (name != null && name.getValue() != null) {
	                    l.add(name.getValue());
	                }
	            }
	        }
	    }
	    
	    // check for annotations too.
	    for (final Iterator i = annotationService.getLocalAnnotations(res); i.hasNext(); ) {
	        final Annotation a = (Annotation)i.next();
	        final String t = a.getAlternativeTitle();
	        final String n = a.getNote();
	        if (t != null) {
	            l.add(t);
	        }
	        if (n != null) {
	            l.add(n);
	        }
	        final Set<String> tags = a.getTags();
	        if (tags != null) {
	            l.addAll(tags);
	        }
	        if (a instanceof UserAnnotation 
	                && ((UserAnnotation)a).isFlagged()) {
	            l.add("FLAGGED");
	        }			
	    }

	    // and check for coverage.
	    if (res instanceof HasCoverage) {
	        final Coverage coverage = ((HasCoverage)res).getCoverage();
	        if (coverage != null) {
	            final String[] wavebands = coverage.getWavebands();
	            if (wavebands != null) {
	                for (int i = 0 ; i< wavebands.length; i++) {
	                    l.add(wavebands[i]);	                  
	                }
	            }
	        }
	    }
	}

	public void getFilterStrings(final List<String> arg0, final Resource arg1) {
		getFilterValues(arg0,arg1);
	}

}
