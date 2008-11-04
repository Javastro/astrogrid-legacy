/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.astrogrid.acr.ivoa.resource.Resource;

/** {@code Transferable} for a list of registry resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20077:47:59 PM
 */
public class ResourceListTransferable implements Transferable {

	 /**
	 * 
	 */
	public ResourceListTransferable(final List l) {
		this.l = new ArrayList(l); // take a copy, else the selection changes as we drag.
	}
	
	private final List l;
	
	
	public Object getTransferData(final DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (VoDataFlavour.LOCAL_RESOURCE_ARRAY.equals(flavor)
				|| VoDataFlavour.RESOURCE_ARRAY.equals(flavor)) {
			return l.toArray(new Resource[l.size()]);
		} else if (VoDataFlavour.LOCAL_URI_ARRAY.equals(flavor)){
			final List u = new ArrayList();
			for (final Iterator i = l.iterator(); i.hasNext();) {
				final Resource r = (Resource) i.next();
				u.add(r.getId());
			}
			return u.toArray(new URI[u.size()]);
		} else if (VoDataFlavour.URI_LIST.equals(flavor) || VoDataFlavour.PLAIN.equals(flavor)) {
			final StringBuffer s = new StringBuffer();
			for (final Iterator i = l.iterator(); i.hasNext();) {
				final Resource r = (Resource) i.next();
				s.append(r.getId());
				s.append("\n");
			}
			return new ByteArrayInputStream( s.toString().getBytes());
			
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
		VoDataFlavour.LOCAL_RESOURCE_ARRAY
		, VoDataFlavour.RESOURCE_ARRAY
		,VoDataFlavour.LOCAL_URI_ARRAY
		,VoDataFlavour.URI_LIST
		,VoDataFlavour.PLAIN
		/*decided not to provide these.
		,VoDataFlavour.XML
		,VoDataFlavour.HTML		
		*/
	};


	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
	
		 */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("ResourceListTransferable[");
			buffer.append("l = ").append(l);
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
