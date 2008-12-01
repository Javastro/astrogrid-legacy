/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;

/** {@code Transferable} for more than one file object.
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
	public Object getTransferData(final DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (VoDataFlavour.LOCAL_FILEOBJECT_ARRAY.equals(flavor)){
			return l.toArray(new FileObject[l.size()]);
		} else if (VoDataFlavour.LOCAL_URI_ARRAY.equals(flavor)) {
			try {
			final List u = new ArrayList();
			for (final Iterator i = l.iterator(); i.hasNext();) {
				final FileObject r = (FileObject) i.next();
				u.add(new URI(StringUtils.replace(r.getName().getURI()," ","%20")));
			}
			return u.toArray(new URI[u.size()]);
			} catch (final URISyntaxException e) {
				logger.warn("Unable to create a URI for fileobject",e);
				final UnsupportedFlavorException exception = new UnsupportedFlavorException(flavor);
				exception.initCause(e);
				throw exception;
			}
		}
			final StringBuffer s = new StringBuffer();
			for (final Iterator i = l.iterator(); i.hasNext();) {
				final FileObject r = (FileObject) i.next();
				s.append(StringUtils.replace(r.getName().getURI()," ","%20"));
				s.append("\n");
			}
			if (VoDataFlavour.URI_LIST.equals(flavor)||VoDataFlavour.PLAIN.equals(flavor)) {
			    return IOUtils.toInputStream(s.toString());
			} else if (VoDataFlavour.URI_LIST_STRING.equals(flavor)){
			    return s.toString();
			}  else {
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
		VoDataFlavour.LOCAL_FILEOBJECT_ARRAY
		,VoDataFlavour.LOCAL_URI_ARRAY
		,VoDataFlavour.URI_LIST
		,VoDataFlavour.URI_LIST_STRING
		,VoDataFlavour.PLAIN
	};

}
