/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** Implementaiton of a service that provides additional metadata - annotaitons - about registry resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 20077:12:26 PM
 */
public class AnnotationServiceImpl implements AnnotationService{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(AnnotationServiceImpl.class);

	
	private final List annotationSources;
	private final AnnotationIO io;
	private final Ehcache cache;
	private final UIContext ui;
	private final AnnotationSource userSource;
	public AnnotationServiceImpl(final Ehcache cache, final UIContext ui, IterableObjectBuilder sources,Preference workDir) {
		super();
		this.io = new AnnotationIO(workDir, sources); 
		this.cache = cache;
		this.ui = ui;
		
		// populate the annotation source list.
		this.annotationSources = io.loadAnnotationSourceList();
		// this file load runs on startup thread, but at least it's local
		userSource = io.getUserSource();
		run();
	}

// source management.
	public void addSource(AnnotationSource nu) {
		if (annotationSources.contains(nu)) {
			logger.warn("Ignoring attempt to add duplicate source " + nu);
			return;
		}
		annotationSources.add(nu);
		saveSourceList();
		if (! (nu instanceof DynamicAnnotationSource)) {
			loadStaticSource(nu);
		}
	}

private void saveSourceList() {
	(new BackgroundWorker(ui,"Saving annotation source list") {

		protected Object construct() throws Exception {
			io.saveAnnotationSourceList(listSources());
			return null;
		}
	}).start();
}

	public AnnotationSource[] listSources() {
		return (AnnotationSource[])annotationSources.toArray(new AnnotationSource[annotationSources.size()]);
	}
	
	public void removeSource(AnnotationSource remove) {
		if (userSource.equals(remove)) {
			logger.warn("Ignoring attempt to remove user source");
			return; //ignored.
		}
		annotationSources.remove(remove);
		saveSourceList();
		// and in this case, all annotations are now invalid - simples way is to flush cache, and reload..
		cache.removeAll();
		run(); // reload.
	}

	/** load and process the annotation sources - or at least those that are static. */
	public void run() {
		for (Iterator i = annotationSources.iterator(); i.hasNext();) {
			AnnotationSource s = (AnnotationSource) i.next();
			if (! (s instanceof DynamicAnnotationSource)) {
				// so it's a static source.
				loadStaticSource(s);
			}
		}
	}	
	
	/** process a single static source */
	public void loadStaticSource(final AnnotationSource source) {
		// do this on a background thread..
		(new BackgroundWorker(ui,"Loading annotations:" + source.getName()) {

			protected Object construct() throws Exception {
				Annotation[] anns = io.load(source);
				for (int i = 0; i < anns.length; i++) {
					Annotation a = anns[i];
					a.setSource(source); // as probably haven't persisted this.
					URI resourceId = a.getResourceId();
					Element el = cache.get(resourceId);
					if (el == null) { // new.
						Map m = new HashMap(annotationSources.size());
						m.put(a.getSource(),a);
						el = new Element(resourceId,m);
					} else { // existing. just add it.
						Map m = (Map)el.getValue();
						m.put(a.getSource(),a);
					}
					cache.put(el);
				} // end for loop.
				return null; // done.
			}
			protected void doError(Throwable ex) {
				logger.warn("Failed loading " + source,ex);
			}
		}).start();
	}
	
	
// access the user annotation
	public AnnotationSource getUserAnnotationSource() {
		return userSource;
	}
	public UserAnnotation getUserAnnotation(Resource r) {
		Element el = cache.get(r.getId());
		if (el == null) {
			return null;
		}
		Map m = (Map)el.getValue();
		UserAnnotation ua = (UserAnnotation)m.get(userSource);
		return ua;
	}
	
	public void removeUserAnnotation(Resource r) {
		if (r == null) {
			return;
		}
		Element el = cache.get(r.getId());
		if (el == null) {
			// we're done.
			return;
		} else {
			Map m = (Map)el.getValue();
			
			if (m.containsKey(userSource)) {
				UserAnnotation ann = (UserAnnotation)m.remove(userSource);
				removeUserAnnotation(ann);			
				cache.put(el);		
			}
		}
	}
	
	public void setUserAnnotation(Resource r, UserAnnotation ann) {
		if (ann == null || r == null) {
			return;
		}
		// make sure references are correct.
		ann.setResourceId(r.getId());
		ann.setSource(userSource);
		
		// wap it into the cache.
		Element el = cache.get(r.getId());
		if (el == null) {
			Map m = new HashMap();
			m.put(userSource,ann);
			el = new Element(r.getId(),m);
			persistNewUserAnnotation(ann);
		} else {
			Map m = (Map)el.getValue();
			if (m.containsKey(userSource)) {
				persistUpdatedUserAnnotation(ann);
			} else {
				persistNewUserAnnotation(ann);				
			}
			m.put(userSource,ann);
		}
		cache.put(el);
		
	}
	private void persistNewUserAnnotation(final UserAnnotation ann) {
		(new BackgroundWorker(ui,"Saving new annotation") {

			protected Object construct() throws Exception {
				io.addUserAnnotation(ann);
				return null;
			}
		}).start();
	}	
	
	private void persistUpdatedUserAnnotation(final UserAnnotation ann) {
		(new BackgroundWorker(ui,"Updating annotation") {

			protected Object construct() throws Exception {
				io.updateUserAnnotation(ann);
				return null;
			}
		}).start();
	}	
	private void removeUserAnnotation(final UserAnnotation ann) {
		(new BackgroundWorker(ui,"Removing annotation") {

			protected Object construct() throws Exception {
				io.removeUserAnnotation(ann);
				return null;
			}
		}).start();
	}	

// process annotations
	public void processLocalAnnotations(Resource r, AnnotationProcessor procesor) {
		Element el = cache.get(r.getId());
		if (el == null) {
			return;
		}
		Map m = (Map)el.getValue();
		for (Iterator i = m.values().iterator(); i.hasNext(); ) {
			Annotation a = (Annotation)i.next();
			if (a instanceof UserAnnotation) {
				procesor.process((UserAnnotation)a);
			} else {
				procesor.process(a);
			}
		}
	}
	
	public Iterator getLocalAnnotations(Resource r) {
		Element el = cache.get(r.getId());
		if (el == null) {
			return IteratorUtils.emptyIterator();
		}
		Map m = (Map)el.getValue();
		return m.values().iterator();
	}

	public void processRemainingAnnotations(final Resource r, final AnnotationProcessor processor) {
		Element el = cache.get(r.getId());
		final Map m;
		if (el == null) {
			m = new HashMap();
			el = new Element(r.getId(),m);
		} else {
			m = (Map)el.getValue();
		}
		final Element toCache = el; // final restriction work-around.
		for (Iterator i = annotationSources.iterator(); i.hasNext();) {
			final AnnotationSource source = (AnnotationSource) i.next();
			if (source instanceof DynamicAnnotationSource &&
					! m.containsKey(source)) {
				(new BackgroundWorker(ui,"Processing " + source.getName()) {

					protected Object construct() throws Exception {
						final DynamicAnnotationSource dynSource = (DynamicAnnotationSource)source;
						Annotation ann = (dynSource).getAnnotationFor(r);
						if (dynSource.shouldCache()) {
							m.put(source,ann);
							cache.put(toCache); // risk of race, and repeated cache writes here.
							// but don't think it's a large risk.
						}
						return ann;
					}
					protected void doFinished(Object result) {
						Annotation ann = (Annotation)result;
						if (ann != null) {
							if (ann instanceof UserAnnotation) {
								processor.process((UserAnnotation)ann);
							} else {
								processor.process(ann);
							}
						}
					}
					protected void doError(Throwable ex) {
						logger.info("Failed for " + source.getName());
					}
				}).start();
			}
		}
	}

//	public void processRemainingAnnotationsForAll(Resource[] r, ResourceAnnotationProcessor processor) {
//	}



	


}