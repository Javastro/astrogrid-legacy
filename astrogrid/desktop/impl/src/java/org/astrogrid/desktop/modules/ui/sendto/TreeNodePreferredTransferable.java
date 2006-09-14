/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.lang.ArrayUtils;
import org.astrogrid.desktop.modules.ui.scope.Retriever;
import org.astrogrid.desktop.modules.ui.scope.SiapRetrieval;
import org.astrogrid.desktop.modules.ui.scope.SsapRetrieval;
import org.astrogrid.desktop.modules.ui.scope.StapRetrieval;


import edu.berkeley.guir.prefuse.graph.TreeNode;

/** transferable object for a node in a scope tree.
 * relies on {@link #setTreeNode(TreeNode)} being called to initialize.
 * and also that the tree node has the required properties.
 * @todo hard-coded for now. make extensible later...
  @author Noel Winstanley
 * @since Jun 25, 20062:22:53 AM
 */
public class TreeNodePreferredTransferable implements PreferredTransferable {

	/** set of flavors available for a siap/ssap, etc, etc response */
	static final DataFlavor[] serviceResponse = new DataFlavor[] {
		VoDataFlavour.URL
		,VoDataFlavour.VOTABLE
		,VoDataFlavour.XML
		,VoDataFlavour.PLAIN
	};
	
	static final DataFlavor[] fitsImage = new DataFlavor[]{
		VoDataFlavour.URL
		,VoDataFlavour.FITS_IMAGE
	};
	
	static final DataFlavor[] gifImage = new DataFlavor[] {
		VoDataFlavour.URL
		,VoDataFlavour.GIF
	};
	
	static final DataFlavor[] pngImage = new DataFlavor[] {
		VoDataFlavour.URL
		,VoDataFlavour.PNG
	};
	
	static final DataFlavor[] jpgImage = new DataFlavor[] {
		VoDataFlavour.URL
		,VoDataFlavour.JPEG
	};
	
	static final DataFlavor[] html = new DataFlavor[] {
		VoDataFlavour.URL
		,VoDataFlavour.HTML
	};
	
	public DataFlavor getPreferredDataFlavor() {
		return VoDataFlavour.URL; // always remote data.
	}

	public Object getTransferData(DataFlavor arg0)
			throws UnsupportedFlavorException, IOException {
		if (arg0 == null || ! isDataFlavorSupported(arg0)) {
			throw new UnsupportedFlavorException(arg0);
		}
		try {
		String serviceType = getServiceType();
		URL u;
		if (node.getChildCount() ==0 && serviceType != null) {// is a leaf, with it's own data.
			String urlString;
			if (serviceType.equals("SIAP")) {
				urlString = node.getAttribute(SiapRetrieval.IMAGE_URL_ATTRIBUTE);
			}  else if (serviceType.equals("SSAP")) {
				urlString = node.getAttribute(SsapRetrieval.SPECTRA_URL_ATTRIBUTE);
			} else if (serviceType.equals("STAP")) {
				urlString = node.getAttribute(StapRetrieval.IMAGE_URL_ATTRIBUTE);
			} else { // cone, and anything unknown.
				urlString = serviceNode.getAttribute(Retriever.SERVICE_URL_ATTRIBUTE);// nothing special for cones.
			}			
			u = new URL(urlString);			
		} else { // look to the service node.
			u =  new URL(serviceNode.getAttribute(Retriever.SERVICE_URL_ATTRIBUTE));
		}
			if (arg0.equals(VoDataFlavour.URL)) {
				return u;
			} else {
				return u.openStream();
			}		 
		} catch (Exception e) {
			IOException ex =  new IOException("Failed to read data");
			ex.initCause(e);
			throw ex;
		}
	}

	public DataFlavor[] getTransferDataFlavors() {
		String serviceType = getServiceType();
		if (node.getChildCount() == 0 &&  serviceType != null) {
			if (serviceType.equals("SIAP")) {
				String type = node.getAttribute(SiapRetrieval.IMAGE_TYPE_ATTRIBUTE);
				return chooseByMimeType(type);
			}  else if (serviceType.equals("SSAP")) {
				String type = node.getAttribute(SsapRetrieval.SPECTRA_FORMAT_ATTRIBUTE);
				return chooseByMimeType(type);
			} else if (serviceType.equals("STAP")) {
				String type = node.getAttribute(StapRetrieval.IMAGE_TYPE_ATTRIBUTE);
				return chooseByMimeType(type);
			} else if (serviceType.equals("CONE")){
				return serviceResponse; // nothing special for cones.
			}
		}
			return serviceResponse;
		
	}

	/**
	 * @param mime
	 */
	private DataFlavor[] chooseByMimeType(String mime) {
		if ("image/fits".equals(mime)) {
			return fitsImage;
		}
		if ("spectrum/fits".equals(mime)) {
			return fitsImage;
		}		
		if ("image/gif".equals(mime)) {
			return gifImage;
		}
		if ("image/png".equals(mime)) {
			return pngImage;
		}
		if ("image/jpeg".equals(mime)) {
			return jpgImage;
		}					
		if ("text/html".equals(mime)) { // really! they occur.
			return html;
		}
		// default
		return serviceResponse;
	}

	private String getServiceType() {
		return node.getAttribute(Retriever.SERVICE_TYPE_ATTRIBUTE);
	}
	
	
	
	public boolean isDataFlavorSupported(DataFlavor arg0) {
		return ArrayUtils.contains(getTransferDataFlavors(),arg0);
	}
	
	public void setTreeNode(TreeNode node) {
		this.node = node;
		this.serviceNode = this.node;
		while(this.serviceNode.getAttribute(Retriever.SERVICE_ID_ATTRIBUTE) == null
				&& this.serviceNode.getParent() != null) {
			this.serviceNode = this.serviceNode.getParent();
		}
	}
	
	private TreeNode node;
	private TreeNode serviceNode;
}
