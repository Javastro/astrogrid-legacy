/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;

import org.astrogrid.acr.ivoa.resource.Service;

/** Base class for dal protocols that query on position.
 * @author Noel Winstanley
 * @since May 14, 20068:26:52 AM
 */
public abstract class SpatialDalProtocol extends DalProtocol {

	/**
	 * @param name
	 */
	public SpatialDalProtocol(String name,Image img) {
		super(name,img);
	}

	public abstract Retriever[] createRetrievers(Service i, double ra, double dec, double raSize, double decSize);


	



}
