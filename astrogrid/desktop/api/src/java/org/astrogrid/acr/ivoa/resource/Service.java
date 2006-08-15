/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**   a resource that can be invoked by a client to perform some action
           on its behalf.  
 * @author Noel Winstanley
 * @since Jul 31, 20064:23:05 PM
 */
public interface Service extends Resource {
	/**  Information about rights held in and over the resource. */
	String[] getRights();
	/**  a description of a general capability of the
                        service and how to use it. */
	Capability[] getCapabilities();
}
