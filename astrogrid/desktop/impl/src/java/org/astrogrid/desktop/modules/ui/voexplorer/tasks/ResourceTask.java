/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** helper class - makes implementations simpler for now... remove
 * when implementations become more general later.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:29:18 AM
 */
public abstract class ResourceTask extends AbstractTask implements Predicate {
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory.getLog(ResourceTask.class);


	protected List current;
	public boolean evaluate(Object arg0) {
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
		} else if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE_LIST)) {
			someSelected((List)r.getTransferData(VoDataFlavour.LOCAL_RESOURCE_LIST));
		} else if (r.isDataFlavorSupported(VoDataFlavour.RESOURCE_LIST)) {
			someSelected((List)r.getTransferData(VoDataFlavour.RESOURCE_LIST));
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
			current = Collections.singletonList(resource);
		} else {
			setEnabled( false);
			current = null;
		}
	}
	/**
	 * @param list
	 */
	public void someSelected(List list) {
		
		if(CollectionUtils.exists(list,this)) {
			setEnabled(true);
			current = list;
		} else {
			setEnabled(false);
			current = null;
		}
	}
	
	protected List computeInvokable() {
		List r = new ArrayList();
		CollectionUtils.select(current,this,r);
		return r;
	}
	
	protected  abstract boolean invokable(Resource r);
}
