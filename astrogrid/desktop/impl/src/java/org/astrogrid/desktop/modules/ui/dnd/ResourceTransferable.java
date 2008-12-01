/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.PrettierResourceFormatter;

/** {@code Transferable} for a single registry resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20077:47:59 PM
 */
public class ResourceTransferable implements Transferable {

	 /**
	 * 
	 */
	public ResourceTransferable(final Resource r) {
		this.r = r;
	}
	
	private final Resource r;
	
	public Object getTransferData(final DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (VoDataFlavour.LOCAL_RESOURCE.equals(flavor)
				|| VoDataFlavour.RESOURCE.equals(flavor)) {
			return r;
		} else if (VoDataFlavour.LOCAL_URI.equals(flavor)){
			return r.getId();
		} else if (VoDataFlavour.URI_LIST.equals(flavor) || VoDataFlavour.PLAIN.equals(flavor)) {
		    return IOUtils.toInputStream(r.getId().toString());
		} else if (VoDataFlavour.HTML.equals(flavor)) {
			//@todo add more efficient stream method here later?
			final String s = PrettierResourceFormatter.renderResourceAsHTML(r);
			return IOUtils.toInputStream(s);
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

	public DataFlavor[] getTransferDataFlavors() {
		return supportedDataFlavors;
	}

	public boolean isDataFlavorSupported(final DataFlavor flavor) {
		return ArrayUtils.contains(supportedDataFlavors,flavor);
	}
	private static final DataFlavor[] supportedDataFlavors = new DataFlavor[] {
		VoDataFlavour.LOCAL_RESOURCE
		, VoDataFlavour.RESOURCE
		,VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URI_LIST
		,VoDataFlavour.PLAIN
		
	};

	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
	
		 */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("ResourceTransferable[");
			buffer.append("r = ").append(r);
			if (supportedDataFlavors == null) {
				buffer.append(", supportedDataFlavors = ").append("null");
			} else {
				buffer.append(", supportedDataFlavors = ").append(
					Arrays.asList(supportedDataFlavors).toString());
			}
			buffer.append("]");
			return buffer.toString();
		}
	

}
