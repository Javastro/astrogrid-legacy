/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.Comparator;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;

/** comparison for file objects - first on filetype (folders get prominence), then on name
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 28, 20074:19:29 PM
 */
public class FileObjectComparator implements Comparator {

	public int compare(Object arg0, Object arg1) {
		try {
		FileObject a = (FileObject)arg0;
		FileObject b = (FileObject)arg1;
		if (a.getType().equals(b.getType())) {
			return a.getName().getBaseName().compareTo(b.getName().getBaseName());
		} else if (a.getType().hasChildren()) {
			return -1;
		} else if (b.getType().hasChildren()) {
			return 1;
		} else { // something else...
			return a.getName().getBaseName().compareTo(b.getName().getBaseName());
		}
		} catch (FileSystemException e) {
			return 0;
		}
	} 

}
