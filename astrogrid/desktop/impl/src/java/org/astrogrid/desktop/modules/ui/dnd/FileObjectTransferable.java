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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

/** transferable object that represents a single file object
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 200712:15:13 AM
 */
public class FileObjectTransferable implements Transferable {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(FileObjectTransferable.class);

	
	
	public FileObjectTransferable(FileObject fo){
		this.fo = fo;
		//String mime = null; //not a good idea..= fo.getContent().getContentInfo().getContentType();
	//	if (mime == null) {
			flavs = alwaysSupportedDataFlavors;
		//} else {
			//DataFlavor mimeFlavor = new DataFlavor(mime);
			//flavs = (DataFlavor[])ArrayUtils.add(alwaysSupportedDataFlavors,mimeFlavor);
	//	}
	}
		private final DataFlavor[] flavs;
	
	private final FileObject fo;
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (! isDataFlavorSupported(flavor)) {
			throw new UnsupportedFlavorException(flavor);
		}
		if (VoDataFlavour.LOCAL_FILEOBJECT.equals(flavor)) {
			return fo;
		} else if (VoDataFlavour.LOCAL_URI.equals(flavor)) {
			try {
				return new URI(StringUtils.replaceChars(fo.getName().getURI(),' ','+'));
			} catch (URISyntaxException x) {
				logger.error("Unable to create URI for fileObject",x);
				throw new UnsupportedFlavorException(flavor);
			}
		} else if (VoDataFlavour.URI_LIST.equals(flavor)) {
			return new ByteArrayInputStream((StringUtils.replaceChars(fo.getName().getURI(),' ','+')).getBytes());
		} else { // must be asking for the content of the file then..
			return fo.getContent().getInputStream();
		}
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavs;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return ArrayUtils.contains(flavs,flavor);
	}
	
	private static final DataFlavor[] alwaysSupportedDataFlavors = new DataFlavor[] {
		VoDataFlavour.LOCAL_FILEOBJECT
		,VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URI_LIST
	};

}
