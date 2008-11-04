/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;
import java.util.Date;

import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;

/** Base class for Dal protocols that query on time.
 * @author Noel Winstanley
 * @since May 14, 20068:26:52 AM
 * @todo - Kevin needs to think whether all these parameters are needed to the retriever - 
 * is just start time and end time enough?
 */
public abstract class TemporalDalProtocol extends DalProtocol {

	/**
	 * @param name
	 */
	public TemporalDalProtocol(final String name, final Image img,final RegistryInternal reg) {
		super(name,img,reg);
	}

    public abstract AbstractRetriever[] createRetrievers(Service i, Date start, Date end, double ra, double dec, double raSize, double decSize);

}
