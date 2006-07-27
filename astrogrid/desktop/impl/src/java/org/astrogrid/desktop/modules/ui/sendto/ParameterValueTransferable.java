package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang.ArrayUtils;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;

/** transferable for a parameter value (i.e. result from lookout)*/
public class ParameterValueTransferable  implements PreferredTransferable {

	
	
	/** don't know what type this result is - so cover all bases */
	static final DataFlavor[] myspaceRemoteResult =new DataFlavor[]{
		VoDataFlavour.IVORN
		,VoDataFlavour.URL
		,VoDataFlavour.VOTABLE
		,VoDataFlavour.FITS_IMAGE
		,VoDataFlavour.FITS_TABLE
		,VoDataFlavour.XML
		,VoDataFlavour.PLAIN
	};
	
	
	static final DataFlavor[] otherRemoteResult =new DataFlavor[]{
		VoDataFlavour.URL
		,VoDataFlavour.VOTABLE
		,VoDataFlavour.FITS_IMAGE
		,VoDataFlavour.FITS_TABLE
		,VoDataFlavour.XML
		,VoDataFlavour.PLAIN
	};
	/** string variants - for local results */ 
	static final DataFlavor[] localResult =new DataFlavor[]{
		VoDataFlavour.VOTABLE_STRING
		,VoDataFlavour.FITS_IMAGE
		,VoDataFlavour.FITS_TABLE
		,VoDataFlavour.XML_STRING
		,VoDataFlavour.PLAIN_STRING
		,VoDataFlavour.URL
	};
	
	/** local transcript */
	static final DataFlavor[] transcript = new DataFlavor[]{
		VoDataFlavour.WORKFLOW_TRANSCRIPT_STRING
		,VoDataFlavour.XML_STRING
		,VoDataFlavour.PLAIN_STRING
		,VoDataFlavour.URL		
	};
	
	public ParameterValueTransferable(ParameterValue val,Myspace vos) {
		this.val = val;
		this.vos = vos;
		if (val.getIndirect()) {
			if (val.getValue().trim().startsWith("ivo://")) {	
				myFlavours = myspaceRemoteResult;
			} else {
				myFlavours = otherRemoteResult;
			}
		} else {
			if (val.getName().equals("transcript")) { // HACK - special case for a transcript..
				myFlavours = transcript;
			} else {
				myFlavours = localResult;
			}
		}
	}
	private final Myspace vos;
	public ParameterValue getValue() {
		return val;
	}
	private final ParameterValue val;
	private final DataFlavor[] myFlavours;
	
	public DataFlavor getPreferredDataFlavor() {
			return myFlavours[0];
	}
	public Object getTransferData(DataFlavor arg0) throws UnsupportedFlavorException, IOException {
		if (! ArrayUtils.contains(myFlavours,arg0)) {
			throw new UnsupportedFlavorException(arg0);
		}
		try {
		if (myFlavours == myspaceRemoteResult) {
			return processMyspaceRemote(arg0);
		} else if (myFlavours == otherRemoteResult) {
			return processOtherRemote(arg0);
		} else {
			return processLocal(arg0);
		}
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage());
		} catch (ACRException e) {
			throw new IOException(e.getMessage());
		} 
	}

	private URL tmpLocalURL = null;
	
	/**
	 * @param arg0
	 * @return
	 * @throws IOException 
	 */
	private Object processLocal(DataFlavor arg0) throws IOException {
		if (arg0.equals(VoDataFlavour.URL)) {
			if (tmpLocalURL == null) {
				File f = File.createTempFile("parameterValue",".txt"); // lowest common denominator
				//f.deleteOnExit(); not safe - as clients may still be running after we exit.
				Writer w = null;
				try {
				w = new FileWriter(f);
				w.write(val.getValue());
				w.close();
				tmpLocalURL = f.toURL();
				} finally {
					if (w != null) {
						try {
							w.close();
						} catch (IOException ignored) {
						}
					}
				}
			}
			return tmpLocalURL;
		} else {
			return val.getValue();
		}
	}
	/**
	 * @param arg0
	 * @return
	 * @throws IOException 
	 */
	private Object processOtherRemote(DataFlavor arg0) throws IOException {
		URL u = new URL(val.getValue());
		if (arg0.equals(VoDataFlavour.URL)) {
			return u;
		} else {
			return u.openStream();
		}
	}
	/**
	 * @param arg0
	 * @return
	 * @throws URISyntaxException 
	 * @throws NotApplicableException 
	 * @throws SecurityException 
	 * @throws ServiceException 
	 * @throws InvalidArgumentException 
	 * @throws NotFoundException 
	 * @throws IOException 
	 */
	private Object processMyspaceRemote(DataFlavor arg0) throws URISyntaxException, NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException, IOException {
		if (arg0.equals(VoDataFlavour.IVORN)) {
			return new URI(val.getValue());
		}
		URI u = new URI(val.getValue());
		URL url = vos.getReadContentURL(u);
		if (arg0.equals(VoDataFlavour.URL)) {
			return url; 
		} else {
			return url.openStream();
		}
	}
	
	public DataFlavor[] getTransferDataFlavors() {
		return myFlavours;
	}

	
	public boolean isDataFlavorSupported(DataFlavor arg0) {
	/* don't think this is correct - as we'll get a misleading object type out..
		String mime = arg0.getPrimaryType();
		for (int i = 0; i < myFlavours.length; i++) {
			if (myFlavours[i].getPrimaryType().equals(mime)) {
				return true;
			}
		}
		return false;
		*/
		return ArrayUtils.contains(myFlavours,arg0);
	}
}