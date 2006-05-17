/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.util.Calendar;

import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** Base class for dal protocols that query on position.
 * @author Noel Winstanley
 * @since May 14, 20068:26:52 AM
 */
public abstract class SpatialDalProtocol extends DalProtocol {

	/**
	 * @param name
	 */
	public SpatialDalProtocol(String name) {
		super(name);
	}

	public abstract Retriever createRetriever(UIComponent parent,
			ResourceInformation i, double ra, double dec, double raSize, double decSize);
	



}
