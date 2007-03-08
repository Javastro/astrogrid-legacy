/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.ui.AbstractVospaceBrowser;
import org.astrogrid.desktop.modules.ui.dnd.PreferredTransferable;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.filemanager.client.FileManagerNode;


/** file manager node as a transferable.
 * @author Noel Winstanley
 * @since Jun 24, 200611:41:39 AM
 */
public class FileManagerNodePreferredTransferable implements PreferredTransferable{

	
	static final DataFlavor[] anyFile = new DataFlavor[] {
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.STRING
		,VoDataFlavour.PLAIN
	};
	
	static final DataFlavor[] votableFile = new DataFlavor[]{
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.VOTABLE
		,VoDataFlavour.XML
		,VoDataFlavour.PLAIN		
	};
	
	static final DataFlavor[] fitsFile = new DataFlavor[] {
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.FITS_IMAGE
		,VoDataFlavour.FITS_TABLE
		,VoDataFlavour.PLAIN			
	};
	
	static final DataFlavor[] workflowFile = new DataFlavor[] {
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.WORKFLOW
		,VoDataFlavour.XML
		,VoDataFlavour.PLAIN			
	};
	
	static final DataFlavor[] toolFile = new DataFlavor[] {
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.CEA_TOOL
		,VoDataFlavour.XML
		,VoDataFlavour.PLAIN			
	};
	
	static final DataFlavor[] adqlFile = new DataFlavor[] {
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.ADQLX
		,VoDataFlavour.XML
		,VoDataFlavour.PLAIN			
	};
	
	static final DataFlavor[] xmlFile = new DataFlavor[] {
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.XML
		,VoDataFlavour.PLAIN			
	};
	
	static final DataFlavor[] htmlFile = new DataFlavor[] {
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.HTML
		,VoDataFlavour.PLAIN			
	};
	
	static final DataFlavor[] pngFile = new DataFlavor[] {
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.PNG
		,VoDataFlavour.PLAIN			
	};
	
	static final DataFlavor[] jpgFile = new DataFlavor[] {
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.JPEG
		,VoDataFlavour.PLAIN			
	};
	
	static final DataFlavor[] gifFile = new DataFlavor[] {
		VoDataFlavour.LOCAL_URI
		,VoDataFlavour.URL
		,VoDataFlavour.GIF
		,VoDataFlavour.PLAIN			
	};
	
	
	private final Map suffixMap;
	private final MyspaceInternal vos;
	/**
	 * builds the transferable, and populates the map.
	 * @future extend to work with local filesystem? 
	 * @future get information from mime-type, when available
	 * @future integrate with JDIC to get known file-type associations.
	 * @future maybe replace this hard-coded map with some kind of hivemind configuration?
	 * @param nodeManager
	 */
	public FileManagerNodePreferredTransferable(AbstractVospaceBrowser.CurrentNodeManager nodeManager, MyspaceInternal vos) {
		this.nodeManager = nodeManager;
		this.vos = vos;
		this.suffixMap = new HashMap();
		// populate my map.
		suffixMap.put("fits",fitsFile);
		suffixMap.put("vot",votableFile);
		suffixMap.put("votable",votableFile);
		suffixMap.put("wf",workflowFile);
		suffixMap.put("workflow",workflowFile);
		suffixMap.put("cea",toolFile);
		suffixMap.put("tool",toolFile);		
		suffixMap.put("adql",adqlFile);	
		suffixMap.put("adqlx",adqlFile);		
		suffixMap.put("query",adqlFile);		
		suffixMap.put("html",htmlFile);		
		suffixMap.put("htm",htmlFile);
		suffixMap.put("xml",xmlFile);	
		suffixMap.put("gif",gifFile);		
		suffixMap.put("png",pngFile);		
		suffixMap.put("jpg",jpgFile);		
		suffixMap.put("jpeg",jpgFile);				
	}
	private AbstractVospaceBrowser.CurrentNodeManager nodeManager;
	
	public DataFlavor getPreferredDataFlavor() {
		return VoDataFlavour.LOCAL_URI; // simple - always remote.
	}

	public Object getTransferData(DataFlavor arg0) throws UnsupportedFlavorException, IOException {
		if (arg0 == null || ! isDataFlavorSupported(arg0)) {
			throw new UnsupportedFlavorException(arg0);
		}
		try {
		URI ivorn =  new URI(nodeManager.getCurrent().getIvorn().toString());
		if (arg0.equals(VoDataFlavour.LOCAL_URI)) {
			return ivorn;
		} 
		
		if (arg0.equals(VoDataFlavour.URL)) {
			return nodeManager.getCurrent().contentURL();
		//	return vos.getReadContentURL(ivorn);
		}
		
		return vos.getInputStream(ivorn);
		} catch (Exception e) {
			IOException ex = new IOException("Failed to read file");
			ex.initCause(e);
			throw ex;
		}
	}

	public DataFlavor[] getTransferDataFlavors() {
		String fileName = nodeManager.getCurrent().getName();
		String[] arr = StringUtils.split(fileName,'.');
		if (arr.length == 1) { // no ending
			return anyFile;
		}
		String suffix = arr[arr.length-1].trim().toLowerCase();
		DataFlavor[] detected = (DataFlavor[])suffixMap.get(suffix);
		return detected != null ? detected : anyFile;
	}

	public boolean isDataFlavorSupported(DataFlavor arg0) {
		return ArrayUtils.contains(getTransferDataFlavors(),arg0);
	}

	public Map getMetaData() {
		FileManagerNode node =  nodeManager.getCurrent();
		Map m = new HashMap();
		m.put("name",node.getName());
		m.put("createdate",node.getMetadata().getCreateDate());
		m.put("modifydate",node.getMetadata().getModifyDate());
		m.put("size",node.getMetadata().getSize());
		m.put("location",node.getMetadata().getContentLocation());
		m.putAll(node.getMetadata().getAttributes());
		return m;
	}

}
