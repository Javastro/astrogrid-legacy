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
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.impl.DefaultFileContentInfo;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;

/** a VFS fileInfo factory that uses standard technique (config file in jre)
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
			put("voevent",VoDataFlavour.MIME_VOEVENT);
			put("py","text/x-python");
			put("pl","text/x-perl");
			
		}
	public FileContentInfo create(final FileContent fileContent) {
	    // see if a wrapped instance of astroscopeFileObject already has this information..
	    // as AstroscopeFileObject has richer information provided compared to what can be deduced by filename alone, we should defer to this if present.
	    final FileObject fo = fileContent.getFile();
	    final AstroscopeFileObject afo = AstroscopeFileObject.findAstroscopeFileObject(fo);
	    //see if we've ended up with an afo
	    if (afo != null) {  // ok. so we're wrapping something..
	        try {
	            final FileContentInfo contentInfo = afo.getContent().getContentInfo();
	            if (contentInfo != null){
	                return contentInfo;
	            }                        
	        } catch (final FileSystemException x) {
	            // oh well, proceed as normal.
	        }	       	       
	    }
	    // deduce the type ourselves then..
	           String contentType = null;
	           final String name = fileContent.getFile().getName().getBaseName();
	           if (name != null){
	        	   final String extension = fileContent.getFile().getName().getExtension();
	        	   if (extension != null) {
	        	       contentType = (String) this.get(extension.toLowerCase());
	        	   if (contentType == null) {
	        		   contentType = inner.getContentTypeFor(name);
	        	   }
	        	   }
	           }
	           return new DefaultFileContentInfo(contentType, null);
	       }
	 
}
