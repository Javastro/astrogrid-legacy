/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileContentInfo;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** Maps FileObjects to suitable icons, using a variety of rules.
 * @future configure the filemap using hivemind later??
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 29, 200712:46:02 PM
 */
public class IconFinder {

	public static final String FOLDER = "folder16.png";
	public static final String FILE = "document16.png";
	public static final String EMPTY_FILE = "fileempty16.png";
	public static final String UNKNOWN = "filebroken6.png";
	public static final Map typeMap = new HashMap();
	static {
		typeMap.put("application/pdf","filepdf16.png");
		typeMap.put("application/postscript","filepostscript16.png");
		typeMap.put("application/x-tex","filetex16.png");
		typeMap.put("application/x-latex","filetex16.png");
		typeMap.put("application/zip","filearchive16.png");
		typeMap.put("application/x-tar","filearchive16.png");
		typeMap.put("application/xml","filexml16.png");
		typeMap.put("text/plain","filetext16.png");
		typeMap.put("text/html","filehtml16.png");
		typeMap.put("image/jpeg","fileimage16.ppng");
		typeMap.put("image/png","fileimage16.png");
		typeMap.put("image/gif","fileimage16.png");
		typeMap.put("image/tiff","fileimage16.png");
		typeMap.put(VoDataFlavour.MIME_VOTABLE,"filetable16.png");
		typeMap.put(VoDataFlavour.MIME_FITS_TABLE,"filebinary16.png");
		typeMap.put(VoDataFlavour.MIME_ADQL,"fileadql16.png");
		typeMap.put(VoDataFlavour.MIME_ADQLX,"fileadql16.png");		
		typeMap.put(VoDataFlavour.MIME_CEA,"filetool16.png");
		typeMap.put("text/x-python","source16.png");
		typeMap.put("text/x-perl","source16.png");
	}
	public ImageIcon find(FileObject fo) {
		try {
		final FileType type = fo.getType();
		if ( type.hasChildren()) {
			return IconHelper.loadIcon(FOLDER);
		}
		if ( type.hasContent()) {
			FileContent content = fo.getContent();
			if (content.getSize() ==0) {
				return IconHelper.loadIcon(EMPTY_FILE);
			}
			FileContentInfo nfo = content.getContentInfo();
			String cType = nfo.getContentType();
			String suggested = (String)typeMap.get(cType);
			return IconHelper.loadIcon(suggested != null ? suggested : FILE);
		}
		// neither a file, or a folder.
		return IconHelper.loadIcon(UNKNOWN);
		} catch (FileSystemException e) {
			return IconHelper.loadIcon(UNKNOWN);
		}
	}
}
