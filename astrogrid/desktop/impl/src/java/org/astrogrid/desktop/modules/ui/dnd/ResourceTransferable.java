/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;

/** transferable object that rerpresents a single registry resource
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20077:47:59 PM
 */
public class ResourceTransferable implements Transferable {

	 /**
	 * 
	 */
	public ResourceTransferable(Resource r) {
		this.r = r;
	}
	
	private final Resource r;
	
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (VoDataFlavour.LOCAL_RESOURCE.equals(flavor)
				|| VoDataFlavour.RESOURCE.equals(flavor)) {
			return r;
		} else if (VoDataFlavour.LOCAL_URI.equals(flavor)){
			return r.getId();
		} else if (VoDataFlavour.URI_LIST.equals(flavor) || VoDataFlavour.PLAIN.equals(flavor)) {
			return new ByteArrayInputStream( (r.getId().toString() ).getBytes());
		} else if (VoDataFlavour.HTML.equals(flavor)) {
			//@todo add more efficient stream method here later?
			String s = ResourceFormatter.renderResourceAsHTML(r);
			return new ByteArrayInputStream(s.getBytes());
		} else {
			throw new UnsupportedFlavorException(flavor);
			//@fixme implement conversions to other supported types.
		}
	}

	public DataFlavor[] getTransferDataFlavors() {
		return supportedDataFlavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return ArrayUtils.contains(supportedDataFlavors,flavor);
	}
	private static final DataFlavor[] supportedDataFlavors = new DataFlavor[] {
		VoDataFlavour.LOCAL_RESOURCE
		, VoDataFlavour.RESOURCE
		,VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URI_LIST
		// decided not to provide these data-carrying flavours.
		//	,VoDataFlavour.VORESOURCE
		//,VoDataFlavour.XML
	//	,VoDataFlavour.HTML
		,VoDataFlavour.PLAIN
		
	};

	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
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