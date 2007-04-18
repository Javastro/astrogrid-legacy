/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileContentInfo;
import org.apache.commons.vfs.FileContentInfoFactory;
import org.apache.commons.vfs.impl.DefaultFileContentInfo;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** an additional fileInfo factory that uses standard technique (config file in jre)
 * and an additional map of vo-specific types.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 29, 20073:36:21 PM
 */
public class AugmentedFileContentInfoFilenameFactory extends HashMap implements FileContentInfoFactory {
		// a composite filename map.
		private final FileNameMap inner =  URLConnection.getFileNameMap();
	
		// populate this object with additional mappings
		{
			put("vot",VoDataFlavour.MIME_VOTABLE);
			put("votable",VoDataFlavour.MIME_VOTABLE);
			put("fits",VoDataFlavour.MIME_FITS_TABLE);
			put("adql",VoDataFlavour.MIME_ADQL);
			put("adqlx",VoDataFlavour.MIME_ADQLX);			
			put("tool",VoDataFlavour.MIME_CEA);
			put("py","text/x-python");
			put("pl","text/x-perl");
			
		}
	public FileContentInfo create(FileContent fileContent) {
	           String contentType = null;
	           String name = fileContent.getFile().getName().getBaseName();
	           if (name != null){
	        	   contentType = (String) this.get(fileContent.getFile().getName().getExtension());
	        	   if (contentType == null) {
	        		   contentType = inner.getContentTypeFor(name);
	        	   }
	           }
	           return new DefaultFileContentInfo(contentType, null);
	       }
	 
}
