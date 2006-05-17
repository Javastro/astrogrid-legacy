/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.util.Calendar;

import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** Base class for dal protocols that query on time.
 * @author Noel Winstanley
 * @since May 14, 20068:26:52 AM
 * @todo - Kevin needs to think whether all these parameters are needed to the retriever - 
 * is just start time and end time enough?
 */
public abstract class TemporalDalProtocol extends DalProtocol {

	/**
	 * @param name
	 */
	public TemporalDalProtocol(String name) {
		super(name);
	}

	public abstract Retriever createRetriever(UIComponent parent,
			ResourceInformation i, Calendar start, Calendar end,
			 double ra, double dec, double raSize, double decSize,String format);

	
	public abstract Retriever createRetriever(UIComponent parent,
			ResourceInformation i, Calendar start, Calendar end,
			 double ra, double dec, double raSize, double decSize);


}
