/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** a naming authority; an assertion of control over a
           namespace represented by an authority identifier. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 20073:39:54 PM
 */
public interface Authority extends Resource {
	/** the organization that owns or manages this authority */
	ResourceName getManagingOrg();
}
