/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.util.Iterator;

import net.sourceforge.hiveutils.service.ObjectBuilder;

/** Hivemind object builder that allows iteration throught the constructed objects.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 5, 200711:07:49 AM
 */
public interface IterableObjectBuilder<I> extends ObjectBuilder {
	/** lists the names of the objects this component knows how to build */
	String[] listObjectNames();
	
	/** iterate through all the objects this builder can create, instantiate
	 *a new one of each
	 * @return an iterator that creates objects
	 */
	Iterator<I> creationIterator();
	
	/** iterate through all the objects this builder can create, instantiating
	 * a new one at each call
	 * @param o constructor parameter to pass to each object to instantiate
	 * @return  an iterator that creates objects
	 */
	Iterator<I> creationIterator(Object o);
	/** iterate through all the objects this builder can create, instantiating
	 * a new one at each call
	 * @param o list of constructor parameters to pass to each object to instantiate
	 * @return  an iterator that creates objects
	 */	
	Iterator<I> creationIterator(Object[] o);
	
}
