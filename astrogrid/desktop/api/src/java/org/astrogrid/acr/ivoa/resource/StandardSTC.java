/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** a description of standard space-time coordinate systems,
           positions, and regions.
   @exclude
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface StandardSTC extends Resource {

    /** access the set of STC descriptions this resoure describes */
    public StcResourceProfile[] getResourceProfiles();
}
