/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** a description of standard space-time coordinate systems,
           positions, and regions.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 20, 20081:19:14 PM
 */
public interface StandardSTC extends Resource {

    /** access the set of STC descriptions this resoure describes */
    public StcResourceProfile[] getResourceProfiles();
}
