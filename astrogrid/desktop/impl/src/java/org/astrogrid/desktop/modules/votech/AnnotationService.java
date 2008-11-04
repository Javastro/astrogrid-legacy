/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.net.URI;
import java.util.Iterator;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.util.Selftest;



/** A component that provides additional metadata (annotations) for resources from various sources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 20077:21:46 PM
 */
public interface AnnotationService extends Selftest {

	// control annotation sources.
	/** List the locations that annotation sources are being read from
	 * - this includes dynamic and static sources. */
	AnnotationSource[] listSources();
	
	/** add a new annotation source to the list. 
	 * it's expected that this will be persisted so that
	 * it's permently added to the list of sources.
	 * 
	 * unused, and not fully implemented.
	 * @param nu
	 */
	void addSource(AnnotationSource nu);
	/** remove the user annotation for a resource
	 * unused, and not fully implemented
	 *  */
	public void removeUserAnnotation(Resource r);	
	
	
	// editing annotations.
	
	/** the source of user annotations  - useful for disinguishing them */
	AnnotationSource getUserAnnotationSource();
	/** access the user annotation for this resource
	 * may return 'null' if no annotation available for this resource
	 */
	UserAnnotation getUserAnnotation(Resource r);
	UserAnnotation getUserAnnotation(URI resourceId);
	
	/** set the annotation for a particular resource */
	void setUserAnnotation(Resource r, UserAnnotation ann);
	
	
	// query for annotations.
	
	/** access annotations that are local.
	 * this is static sources, and dynamic sources that were previously queried,
	 * and their result cached.
	 * 
	 * A low cost operation.
	 */
	void processLocalAnnotations(final Resource r,AnnotationProcessor procesor);
	
	/** an alternative to the annotationprocessor approach - returns the data directly as
	 * an iterator
	 * @param r
	 * @return an iterator of Annotatino objects
	 */
	Iterator getLocalAnnotations(Resource r);
	
	
	/** access the remaining annotations - i.e. those from dynamic services
	 * that were not cached previously
	 * 
	 * as each remaining annotaiton is received, the processor will be passed the
	 * annotation and called. 
	 * 
	 * If no further results are returned, the closure is never called.
	 * 
	 * If some results are returned, these are will be cached, so that next time
	 * getLocalAnnotations will return them all.
	 *  */
	void processRemainingAnnotations(Resource r,AnnotationProcessor processor);

	/** a bulk operation - pass an array of resouces and process all remaining
	 * annotations for them.
	 * @param r
	 * @return a non-null array, whose elements will be as for {@link #getRemainingAnnotations(Resource)}
	 */
//	void processRemainingAnnotationsForAll(Resource[] r,ResourceAnnotationProcessor processor);
	
	/** interface to a class that processes an annotation in some way */
	public interface AnnotationProcessor {
		void process(UserAnnotation a);
		void process(Annotation a);
	}
	/** interface to a class that process an annotation (with resoufce specified) */
	public interface ResourceAnnotationProcessor {
		void process(Resource r, Annotation a);
	}
	
}
