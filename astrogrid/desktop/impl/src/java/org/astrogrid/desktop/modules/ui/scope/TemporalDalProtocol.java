/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ivoa.resource.Service;
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
	public TemporalDalProtocol(String name, Image img) {
		super(name,img);
	}

    public abstract Retriever[] createRetrievers(Service i, Date start, Date end, double ra, double dec, double raSize, double decSize);

}
