/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** A resource that contains coverage information.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface HasCoverage extends Resource {
    /** Extent of the content of the resource over space, time, 
    and frequency. */
	Coverage getCoverage();

}
