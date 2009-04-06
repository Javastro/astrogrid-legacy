/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.List;

import ca.odell.glazedlists.TextFilterator;

/** filteration for file objects. - just on name at the moemnt
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 28, 20074:29:37 PM
 */
public class FileObjectViewFilterator implements TextFilterator<FileObjectView> {

    
	public void getFilterStrings(final List<String> l, final FileObjectView f) {
		l.add(f.getBasename());
	}

}
