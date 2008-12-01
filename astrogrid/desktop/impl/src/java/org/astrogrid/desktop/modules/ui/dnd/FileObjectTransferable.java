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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

/** {@code Transferable} for a single file object.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 200712:15:13 AM
 */
public class FileObjectTransferable implements Transferable{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(FileObjectTransferable.class);


	
	/**
	 * tries to determine from the file object what kind of file this is.
	 * @param fo
	 * @throws FileSystemException
	 */
	public FileObjectTransferable(final FileObject fo) throws FileSystemException{
	    this(fo,fo.getType().hasChildren());
	}
		/** constructor where the kind of file object is specified
     * @param fileObject
     * @param isFolder if true, this is a folder. else it's a file.
     */
    public FileObjectTransferable(final FileObject fileObject, final boolean isFolder) {
        this.fo = fileObject;
        flavs = isFolder ? folderDataFlavours : fileDataFlavours;
    }
        private final DataFlavor[] flavs;
	
	private final FileObject fo;
	public Object getTransferData(final DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (! isDataFlavorSupported(flavor)) {
			throw new UnsupportedFlavorException(flavor);
		}
		if (VoDataFlavour.LOCAL_FILEOBJECT.equals(flavor)) {
			return fo;
		} else if (VoDataFlavour.LOCAL_URI.equals(flavor)) {
			try {
				return new URI(StringUtils.replace(fo.getName().getURI()," ","%20"));
			} catch (final URISyntaxException x) {
				logger.error("Unable to create URI for fileObject",x);
				final UnsupportedFlavorException exception = new UnsupportedFlavorException(flavor);
				exception.initCause(x);
				throw exception;
			}
		} else if (VoDataFlavour.URI_LIST.equals(flavor)) {
			return IOUtils.toInputStream((StringUtils.replace(fo.getName().getURI()," ","%20")));
		} else if (VoDataFlavour.URI_LIST_STRING.equals(flavor)){
		    return StringUtils.replace(fo.getName().getURI()," ","%20");
		} else { // must be asking for the content of the file then..
			return fo.getContent().getInputStream();
		}
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavs;
	}

	public boolean isDataFlavorSupported(final DataFlavor flavor) {
		return ArrayUtils.contains(flavs,flavor);
	}
	
	private static final DataFlavor[] fileDataFlavours = new DataFlavor[] {
		VoDataFlavour.LOCAL_FILEOBJECT
		,VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URI_LIST
		,VoDataFlavour.URI_LIST_STRING
		,VoDataFlavour.PLAIN // seems to take precedence over uri-list. but this is probably generally more useful.
	};
	   private static final DataFlavor[] folderDataFlavours = new DataFlavor[] {
	        VoDataFlavour.LOCAL_FILEOBJECT
	        ,VoDataFlavour.LOCAL_URI
	        ,VoDataFlavour.URI_LIST
	        ,VoDataFlavour.URI_LIST_STRING
	        
	    };




}
