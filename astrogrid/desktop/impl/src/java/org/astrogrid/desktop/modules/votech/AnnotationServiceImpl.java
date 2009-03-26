/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

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
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** Implemnentation of {@link AnnotationService}.
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
	public AnnotationServiceImpl(final Ehcache cache, final UIContext ui, final AnnotationIO io) {
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
	public void addSource(final AnnotationSource nu) {
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

		@Override
        protected Object construct() throws Exception {
			io.saveAnnotationSourceList(listSources());
			return null;
		}
	}).start();
}

	public AnnotationSource[] listSources() {
		return (AnnotationSource[])annotationSources.toArray(new AnnotationSource[annotationSources.size()]);
	}
	
	public void removeSource(final AnnotationSource remove) {
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
		for (final Iterator i = annotationSources.iterator(); i.hasNext();) {
			final AnnotationSource s = (AnnotationSource) i.next();
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

			@Override
            protected Object construct() throws Exception {
				final Collection anns = io.load(source);
				for (final Iterator i = anns.iterator(); i.hasNext();) {
					final Annotation a = (Annotation) i.next();
					a.setSource(source); // as probably haven't persisted this.
					final URI resourceId = a.getResourceId();
					Element el = cache.get(resourceId);
					if (el == null) { // new.
						final Map m = new HashMap(annotationSources.size());
						m.put(a.getSource(),a);
						el = new Element(resourceId,m);
					} else { // existing. just add it.
						final Map m = (Map)el.getValue();
						m.put(a.getSource(),a);
					}
					cache.put(el);
				} // end for loop.
				return null; // done.
			}
			@Override
            protected void doError(final Throwable ex) {
			    // silently swallow exceptions.
                //parent.showTransientWarning("Failed to load annotations from " + source.getName(),ExceptionFormatter.formatException(ex));                
                 logger.warn("Failed to load annotations from " + source.getName());
                
			}
		}).start();
	}
	
	
// access the user annotation
	public AnnotationSource getUserAnnotationSource() {
		return io.getUserSource();
	}
	public UserAnnotation getUserAnnotation(final Resource r) {
	    if (r == null) {
	        return null;
	    }
	    return getUserAnnotation(r.getId());
	}
	public UserAnnotation getUserAnnotation(final URI resourceId) {
	    if (resourceId == null) {
	        return null;
	    }	        	        
		final Element el = cache.get(resourceId);
		if (el == null) {
			return null;
		}
		final Map m = (Map)el.getValue();
		final UserAnnotation ua = (UserAnnotation)m.get(io.getUserSource());
		return ua;
	}
	

	
	public void setUserAnnotation(final Resource r, final UserAnnotation ann) {
		if (ann == null || r == null) {
			return;
		}
		final AnnotationSource userSource = io.getUserSource();
		// make sure references are correct.
		ann.setResourceId(r.getId());
		ann.setSource(userSource);
		
		// wap it into the cache.
		Element el = cache.get(r.getId());
		if (el == null) {
			final Map m = new HashMap();
			m.put(userSource,ann);
			el = new Element(r.getId(),m);
		} else {
			final Map m = (Map)el.getValue();
			m.put(userSource,ann);
		}
		cache.put(el);
		io.updateUserAnnotation(ann);
		
	}
	
	
	public void removeUserAnnotation(final Resource r) {
	    if (r == null) {
	        return;
	    }
	    final Element el = cache.get(r.getId());
	    if (el != null) { // else nothing needs doing.
	        final Map m = (Map)el.getValue();
	        if (m.remove(io.getUserSource()) != null) { // else wasn't a user annotaiton anyhow
	            cache.put(el);
	            io.removeUserAnnotation(r);
	        }
	    }
	}

// process annotations
	public void processLocalAnnotations(final Resource r, final AnnotationProcessor procesor) {
		if (r == null || procesor == null) {
		    return;
		}
	    final Element el = cache.get(r.getId());
		if (el == null) {
			return;
		}
		final Map m = (Map)el.getValue();
		for (final Iterator i = m.values().iterator(); i.hasNext(); ) {
			final Annotation a = (Annotation)i.next();
			if (a instanceof UserAnnotation) {
				procesor.process((UserAnnotation)a);
			} else {
				procesor.process(a);
			}
		}
	}
	
	public Iterator getLocalAnnotations(final Resource r) {
	    if (r == null) {
            return IteratorUtils.emptyIterator();
        }
		final Element el = cache.get(r.getId());
		if (el == null) {
			return IteratorUtils.emptyIterator();
		}
		final Map m = (Map)el.getValue();
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
		for (final Iterator i = annotationSources.iterator(); i.hasNext();) {
			final AnnotationSource source = (AnnotationSource) i.next();
			if (source instanceof DynamicAnnotationSource &&
					! m.containsKey(source)) {
				(new BackgroundWorker(ui,"Loading annotations from " + source.getName(),BackgroundWorker.SHORT_TIMEOUT,Thread.MIN_PRIORITY) {
					@Override
                    protected Object construct() throws Exception {
						final DynamicAnnotationSource dynSource = (DynamicAnnotationSource)source;
						final Annotation ann = (dynSource).getAnnotationFor(r);
						if (ann != null && dynSource.shouldCache()) {
							m.put(source,ann);
							cache.put(toCache); // risk of race, and repeated cache writes here.
							// but don't think it's a large risk.
						}
						return ann;
					}
					@Override
                    protected void doFinished(final Object result) {
						final Annotation ann = (Annotation)result;
						if (ann != null) {
							if (ann instanceof UserAnnotation) {
								processor.process((UserAnnotation)ann);
							} else {
								processor.process(ann);
							}
						}
					}
					@Override
                    protected void doError(final Throwable ex) {
					    // silently swallow exceptions.
					    //parent.showTransientWarning("Failed to load annotations from " + source.getName(),ExceptionFormatter.formatException(ex));
	                    logger.warn("Failed to load annotations from " + source.getName());
					    
					}
				}).start();
			}
		}
	}

    public Test getSelftest() {
        final TestSuite ts = new TestSuite("Annotations");
        ts.addTest(new TestCase("Annotations") {
            @Override
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
