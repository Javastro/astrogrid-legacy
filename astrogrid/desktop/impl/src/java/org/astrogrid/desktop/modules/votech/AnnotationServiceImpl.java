/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

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
	protected final UIContext ui;	
	public AnnotationServiceImpl(final Ehcache cache, final UIContext ui, AnnotationIO io) {
		super();
		this.io = io;
		this.cache = cache;
		this.ui = ui;
		
		// populate the annotation source list.
		this.annotationSources = io.getSourcesList();
		// this file load runs on startup thread, but at least it's local
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
		if (io.getUserSource().equals(remove)) {
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
	public final void run() {
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
		(new BackgroundWorker(ui,"Loading annotations from " + source.getName(),BackgroundWorker.LONG_TIMEOUT,Thread.MIN_PRIORITY) {

			protected Object construct() throws Exception {
				Collection anns = io.load(source);
				for (Iterator i = anns.iterator(); i.hasNext();) {
					Annotation a = (Annotation) i.next();
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
                parent.showTransientWarning("Failed to load annotations from " + source.getName(),ExceptionFormatter.formatException(ex));                
			}
		}).start();
	}
	
	
// access the user annotation
	public AnnotationSource getUserAnnotationSource() {
		return io.getUserSource();
	}
	public UserAnnotation getUserAnnotation(Resource r) {
	    if (r == null) {
	        return null;
	    }
	    return getUserAnnotation(r.getId());
	}
	public UserAnnotation getUserAnnotation(URI resourceId) {
	    if (resourceId == null) {
	        return null;
	    }	        	        
		Element el = cache.get(resourceId);
		if (el == null) {
			return null;
		}
		Map m = (Map)el.getValue();
		UserAnnotation ua = (UserAnnotation)m.get(io.getUserSource());
		return ua;
	}
	

	
	public void setUserAnnotation(Resource r, UserAnnotation ann) {
		if (ann == null || r == null) {
			return;
		}
		AnnotationSource userSource = io.getUserSource();
		// make sure references are correct.
		ann.setResourceId(r.getId());
		ann.setSource(userSource);
		
		// wap it into the cache.
		Element el = cache.get(r.getId());
		if (el == null) {
			Map m = new HashMap();
			m.put(userSource,ann);
			el = new Element(r.getId(),m);
		} else {
			Map m = (Map)el.getValue();
			m.put(userSource,ann);
		}
		cache.put(el);
		io.updateUserAnnotation(ann);
		
	}
	
	
	public void removeUserAnnotation(Resource r) {
	    if (r == null) {
	        return;
	    }
	    Element el = cache.get(r.getId());
	    if (el != null) { // else nothing needs doing.
	        Map m = (Map)el.getValue();
	        if (m.remove(io.getUserSource()) != null) { // else wasn't a user annotaiton anyhow
	            cache.put(el);
	            io.removeUserAnnotation(r);
	        }
	    }
	}

// process annotations
	public void processLocalAnnotations(Resource r, AnnotationProcessor procesor) {
		if (r == null || procesor == null) {
		    return;
		}
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
	    if (r == null) {
            return IteratorUtils.emptyIterator();
        }
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
				(new BackgroundWorker(ui,"Loading annotations from " + source.getName(),BackgroundWorker.SHORT_TIMEOUT,Thread.MIN_PRIORITY) {
					protected Object construct() throws Exception {
						final DynamicAnnotationSource dynSource = (DynamicAnnotationSource)source;
						Annotation ann = (dynSource).getAnnotationFor(r);
						if (ann != null && dynSource.shouldCache()) {
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
					    parent.showTransientWarning("Failed to load annotations from " + source.getName(),ExceptionFormatter.formatException(ex));
					}
				}).start();
			}
		}
	}

    public Test getSelftest() {
        TestSuite ts = new TestSuite("Annotations");
        ts.addTest(new TestCase("Annotations") {
            protected void runTest()  {
                assertEquals("Problem with cache",Status.STATUS_ALIVE,cache.getStatus());
            }
        });
        // don't think there's much point doing any of this - none are vital.
//        AnnotationSource[] srcs = listSources();
//        for (int i = 0; i < srcs.length; i++) {
//            final AnnotationSource src = srcs[i];
//            if (src == io.getUserSource()) {
//                continue; // don't test this one - it's local, and not always present
//            }
//            ts.addTest(new TestCase( src.getName() + " annotations"){
//                protected void runTest() throws Throwable {
//                    try {
//                        src.getSource().toURL().openConnection().connect();
//                    } catch (MalformedURLException x) {
//                        fail("invalid endpoint");
//                    } catch (IOException x) {
//                        fail("Unable to connect to endpoint");
//                    }
//                }
//            });
//
//        }
        return ts;
    }


}
