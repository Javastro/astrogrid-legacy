/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** A naming authority.
 * 
 *  This kind of registry entry makes an assertion of control over a
           namespace represented by an authority identifier. 
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface Authority extends Resource {
	/** The organization that owns or manages this authority 
	 * @see Organisation*/
	ResourceName getManagingOrg();
}
