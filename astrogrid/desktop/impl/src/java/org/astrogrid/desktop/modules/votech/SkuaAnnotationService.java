package org.astrogrid.desktop.modules.votech;

import java.net.URI;
import java.net.URL;

import java.util.Collections;
import java.util.Iterator;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/**
 * Provides an annotation service based on SKUA.
 * @author Norman Gray, norman@astro.gla.ac.uk
 * @since 2008-10-23
 */
public class SkuaAnnotationService implements AnnotationService {
	private static final org.apache.commons.logging.Log logger
			= org.apache.commons.logging.LogFactory.getLog(SkuaAnnotationService.class);

	protected final UIContext ui;
	private final URL skuaServiceURL;

	private AnnotationSource thisSource;
	private SkuaClaimMap skuaClaims;

	public SkuaAnnotationService(final Preference skuaURL, final UIContext ui) 
			throws ACRException {
		super();
		this.ui = ui;

		try {
			this.skuaServiceURL = new URL(skuaURL.toString());
		
			thisSource = new AnnotationSource(skuaServiceURL.toURI(), "SKUA node");
			skuaClaims = new SkuaClaimMap(thisSource);
		} catch (java.net.MalformedURLException e) {
			logger.warn("Malformed SKUA URL: " + e);
			throw new ACRException("Malformed SKUA URL: " + e);
		} catch (java.net.URISyntaxException e){
			logger.warn("Malformed SKUA URI: " + e);
			throw new ACRException("Malformed SKUA URI", e);
		}
	}

	public AnnotationSource[] listSources() {
		AnnotationSource[] l = new AnnotationSource[1];
		l[0] = thisSource;
		return l;
	}

	public void addSource(AnnotationSource nu) {
		logger.warn("Ignoring attempt to add AnnotationSource");
		return;
	}

	public void removeUserAnnotation(Resource r) {
		logger.warn("Ignoring attempt to remove Annotation for resource: " + r);
		return;
	}

	public AnnotationSource getUserAnnotationSource() {
		return thisSource;
	}

	public UserAnnotation getUserAnnotation(final Resource r) {
		return getUserAnnotation(r.getId());
	}

	public UserAnnotation getUserAnnotation(final URI resourceId) {
		return skuaClaims.get(resourceId);
	}
	
	/** Set the annotation for a particular resource */
	public void setUserAnnotation(Resource r, UserAnnotation ann) {
		// I think I only _need_ to do the second of these, noting that
		// it's this source which is 'responsible' for this annotation,
		// but there's no harm, I'm sure, in doing both.
		if (ann.getResourceId() == null)
			ann.setResourceId(r.getId());
		if (ann.getSource() == null)
			ann.setSource(thisSource);
		skuaClaims.put(r.getId(), ann);
	}

	/** 
	 * Process local annotations of the named resource.
	 * In this implementation, we make no distinction between 'local' and 'remote'
	 * annotations
	 * <p>In this implementation, we can have only a single annotation per resource
	 * @param r the resource, annotations of which are to be processed
	 * @param processor a closure which will act on the annotations
	 */
	public void processLocalAnnotations(final Resource r, final AnnotationProcessor processor) {
		UserAnnotation ua = skuaClaims.get(r.getId());
		logger.warn("ua=" + ua);
		if (ua != null)
			processor.process(ua);
	}
	
	/** an alternative to the annotationprocessor approach - returns the data directly as
	 * an iterator
	 * @param r
	 * @return an iterator of Annotation objects
	 */
	public Iterator<UserAnnotation> getLocalAnnotations(final Resource r) {
		UserAnnotation ua = skuaClaims.get(r.getId());
		if (ua == null)
			return Collections.EMPTY_LIST.iterator();
		else {
			UserAnnotation[] ual = new UserAnnotation[1];
			ual[0] = ua;
			return java.util.Arrays.asList(ual).iterator();
		}
	}

	/** 
	 * Access the remaining annotations, namely those from dynamic services
	 * that were not cached previously.  In this implementation, 
	 * there is no difference between 'local' and 'remote' services -- although
	 * all are in principle remote, they appear as local.
	 *
	 * <p>If no further results are returned, the closure is never called.
	 */
	public void processRemainingAnnotations(final Resource r, final AnnotationProcessor processor) {
		processLocalAnnotations(r, processor);
	}

	public junit.framework.Test getSelftest() {
		// no tests yet
		return null;
	}

		
}


// Temporary: Norman's buffer settings
// Local Variables:
// c-basic-offset: 4
// tab-width: 4
// indent-tabs-mode: t
// End:
