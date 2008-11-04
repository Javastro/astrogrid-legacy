/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.List;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;

import ca.odell.glazedlists.TextFilterator;

/** filteration for file objects. - just on name at the moemnt
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 28, 20074:29:37 PM
 */
public class FileObjectFilterator implements TextFilterator {

	public void getFilterStrings(final List l, final Object arg1) {
		final FileObject f = (FileObject)arg1;
		final FileName name = f.getName();
		l.add(name.getBaseName());
	}

}
