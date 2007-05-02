/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** abstract class for activities that just operates on registry resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:29:18 AM
 */
public abstract class AbstractResourceActivity extends AbstractActivity implements Predicate {
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory.getLog(AbstractResourceActivity.class);


	protected Resource[] current;
	public boolean evaluate(Object arg0) {
		if (! (arg0 instanceof Resource)) {
			return false;
		}
		return invokable((Resource) arg0);
	}
	public  void noneSelected() {
		setEnabled(false);
		current = null;
	}
	
	public void selected(Transferable r) {
		try {
		if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE)) {
			oneSelected((Resource)r.getTransferData(VoDataFlavour.RESOURCE));
		} else if (r.isDataFlavorSupported(VoDataFlavour.RESOURCE)) {
			oneSelected((Resource)r.getTransferData(VoDataFlavour.RESOURCE));
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE_ARRAY)) {
			someSelected((Resource[])r.getTransferData(VoDataFlavour.LOCAL_RESOURCE_ARRAY));
		} else if (r.isDataFlavorSupported(VoDataFlavour.RESOURCE_ARRAY)) {
			someSelected((Resource[])r.getTransferData(VoDataFlavour.RESOURCE_ARRAY));
		} else {
			noneSelected();
		}
		} catch (IOException x) {
			logger.error("UnsupportedFlavorException",x);			
		} catch (UnsupportedFlavorException x) {
			logger.error("UnsupportedFlavorException",x);
		}
	}
	/**
	 * @param resource
	 */
	public void oneSelected(Resource resource) {
		if (invokable(resource)) {
			setEnabled(true);
			current =new Resource[]{resource};
		} else {
			setEnabled( false);
			current = null;
		}
	}
	/**
	 * @param list
	 */
	public void someSelected(Resource[] list) {
		for (int i = 0; i < list.length; i++) {
			if(evaluate(list[i])) {
				setEnabled(true);
				current = list;
				return;
			}
		}
			setEnabled(false);
			current = null;
	}
	
	protected List computeInvokable() {
		List r = new ArrayList();
		for (int i = 0; i < current.length; i++) {
			if (evaluate(current[i])) {
				r.add(current[i]);
			}
		}
		return r;
	}
	/** to be implemented by subclasses - a filter
	 * that selects just those resources this activity can operate on.
	 * @param r
	 * @return
	 */
	protected  abstract boolean invokable(Resource r);
}
