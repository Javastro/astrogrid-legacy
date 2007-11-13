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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;

/** transferable object that represents a selection of more than one file object.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 200710:49:08 AM
 */
public class FileObjectListTransferable implements Transferable{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(FileObjectListTransferable.class);

	private final List l;

	public FileObjectListTransferable(final List l) {
		super();
		this.l = l;
	}
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (VoDataFlavour.LOCAL_FILEOBJECT_ARRAY.equals(flavor)){
			return l.toArray(new FileObject[l.size()]);
		} else if (VoDataFlavour.LOCAL_URI_ARRAY.equals(flavor)) {
			try {
			List u = new ArrayList();
			for (Iterator i = l.iterator(); i.hasNext();) {
				FileObject r = (FileObject) i.next();
				u.add(new URI(r.getName().getURI()));
			}
			return u.toArray(new URI[u.size()]);
			} catch (URISyntaxException e) {
				logger.warn("Unable to create a URI for fileobject",e);
				throw new UnsupportedFlavorException(flavor);
			}
		}
			StringBuffer s = new StringBuffer();
			for (Iterator i = l.iterator(); i.hasNext();) {
				FileObject r = (FileObject) i.next();
				s.append(r.getName().getURI());
				s.append("\n");
			}
			if (VoDataFlavour.URI_LIST.equals(flavor)||VoDataFlavour.PLAIN.equals(flavor)) {
			    return new ByteArrayInputStream( s.toString().getBytes());
			} else if (VoDataFlavour.URI_LIST_STRING.equals(flavor)){
			    return s.toString();
			}  else {
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
		VoDataFlavour.LOCAL_FILEOBJECT_ARRAY
		,VoDataFlavour.LOCAL_URI_ARRAY
		,VoDataFlavour.URI_LIST
		,VoDataFlavour.URI_LIST_STRING
		,VoDataFlavour.PLAIN
	};

}
