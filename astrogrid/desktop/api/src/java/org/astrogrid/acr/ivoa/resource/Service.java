/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**   A resource that can be invoked to perform some action.
 * @author Noel Winstanley
 */
public interface Service extends Resource {
	/**  Information about rights held in and over the resource. */
	String[] getRights();
	/**  The capabilities of this service */
	Capability[] getCapabilities();
}
