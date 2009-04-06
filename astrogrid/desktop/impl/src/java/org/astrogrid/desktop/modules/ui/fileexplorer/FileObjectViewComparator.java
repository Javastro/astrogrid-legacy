/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.Comparator;

/** comparison for file objects. - first on filetype (folders get prominence), then on name
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 28, 20074:19:29 PM
 */
public class FileObjectViewComparator implements Comparator<FileObjectView> {

    /**
     * 
     */
    private FileObjectViewComparator() {
    }
    
    public static final FileObjectViewComparator getInstance() {
        return theInstance;
    }
    private static final FileObjectViewComparator theInstance = new FileObjectViewComparator();
    
	public int compare(final FileObjectView a, final FileObjectView b) {
		if (a.getType().equals(b.getType())) {
			return a.getBasename().compareTo(b.getBasename());
		} else if (a.getType().hasChildren()) {
			return -1;
		} else if (b.getType().hasChildren()) {
			return 1;
		} else { // something else...
		    return a.getBasename().compareTo(b.getBasename());		    
		}
	} 

}
