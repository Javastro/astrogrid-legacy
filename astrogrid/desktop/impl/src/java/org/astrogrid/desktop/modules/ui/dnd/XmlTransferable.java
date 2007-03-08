/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import java.util.Arrays;

/** transferable for xml.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20074:15:02 PM
 */
public class XmlTransferable implements Transferable {
/**
 * 
 */
public XmlTransferable(String s) {
	this.s = s;
}
	private final String s;
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (VoDataFlavour.XML.equals(flavor)) {
			return new ByteArrayInputStream(s.getBytes());
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

	public DataFlavor[] getTransferDataFlavors() {
		return supportedDataFlavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return ArrayUtils.contains(supportedDataFlavors,flavor);
	}
	private static final DataFlavor[] supportedDataFlavors = new DataFlavor[] {
		VoDataFlavour.XML
//		VoDataFlavour.PLAIN
//		,VoDataFlavour.HTML		

	};
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("XmlTransferable[");
			buffer.append("s = ").append(s);
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
