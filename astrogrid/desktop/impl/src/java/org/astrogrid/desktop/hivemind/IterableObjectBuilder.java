/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.util.Iterator;

import net.sourceforge.hiveutils.service.ObjectBuilder;

/** Custom object builder that provides access to a list of all objects it will construct.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 5, 200711:07:49 AM
 */
public interface IterableObjectBuilder extends ObjectBuilder {
	/** lists the names of the objects this component knows how to build */
	String[] listObjectNames();
	
	/** iterate through all the objects this builder can create, instantiate
	 *a new one of each
	 * @return an iterator that creates objects
	 */
	Iterator creationIterator();
	
	/** iterate through all the objects this builder can create, instantiating
	 * a new one at each call
	 * @param o constructor parameter to pass to each object to instantiate
	 * @return  an iterator that creates objects
	 */
	Iterator creationIterator(Object o);
	/** iterate through all the objects this builder can create, instantiating
	 * a new one at each call
	 * @param o list of constructor parameters to pass to each object to instantiate
	 * @return  an iterator that creates objects
	 */	
	Iterator creationIterator(Object[] o);
	
}
