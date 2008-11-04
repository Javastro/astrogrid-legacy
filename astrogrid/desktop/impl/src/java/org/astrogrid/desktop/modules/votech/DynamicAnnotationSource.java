/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.net.URI;

import org.astrogrid.acr.ivoa.resource.Resource;

/** A pointer to an external service that
 * can be queried for annotations dynamically.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 20077:25:58 PM
 */
public abstract class DynamicAnnotationSource extends AnnotationSource {

	/** abstract method, implement to call the service and parse result
	 * into an annotation.
	 * @param r
	 * @return
	 */
	public abstract Annotation getAnnotationFor(Resource r);

	/** if true, results from this source should be cached for the session */
	public abstract boolean shouldCache();
	
	public DynamicAnnotationSource() {
		super();
	}

	public DynamicAnnotationSource(final URI source, final String name) {
		super(source, name);
	}
	
	
}
