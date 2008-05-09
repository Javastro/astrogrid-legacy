/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;

import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;

/** Base class for dal protocols that query on position.
 * @author Noel Winstanley
 * @since May 14, 20068:26:52 AM
 */
public abstract class SpatialDalProtocol extends DalProtocol {

	/**
	 * @param name
	 */
	public SpatialDalProtocol(String name,Image img,RegistryInternal reg) {
		super(name,img,reg);
	}

	public abstract AbstractRetriever[] createRetrievers(Service i, double ra, double dec, double raSize, double decSize);


	



}
