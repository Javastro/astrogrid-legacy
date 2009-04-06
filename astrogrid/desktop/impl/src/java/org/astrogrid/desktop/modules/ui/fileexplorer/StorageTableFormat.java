/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.Comparator;
import java.util.Date;

import javax.swing.Icon;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileSystemException;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;

/** glazed lists format for a table of files.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 29, 20071:57:55 AM
 */
public class StorageTableFormat implements AdvancedTableFormat<FileObjectView> {

	private static final int COLUMN_COUNT = 5;
	
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	public String getColumnName(final int arg0) {
		switch(arg0) {
		case 0: return "Icon";
		case 1: return "Name";
		case 2: return "Date Modified";
		case 3: return "Size";
		case 4: return "Type";
		default:
			throw new IndexOutOfBoundsException("Oversized column index " + arg0);		
		}
	}

	public Object getColumnValue(final FileObjectView o, final int arg1) {
		switch(arg1) {
			case 0:
			// mk an icon.
				return o.getIcon();
			case 1:
				return o.getBasename();
			case 2:
				return o.getLastModified();
			case 3:
				if (! o.getType().hasContent()) {
					return null;
				}
				return o.getSize();			
			case 4:
				return findBestContentType(o);
			
			default:
				throw new IndexOutOfBoundsException("Oversized column index " + arg1);
		}
	}

    /** find the most user-readable type description for a file.
     * @param o
     * @return
     * @throws FileSystemException
     */
	public static String  findBestContentType(final FileObjectView o) {
	    if (o.getType().hasContent()&& StringUtils.isNotEmpty(o.getContentType())) {
	            return o.getContentType();	        
	    }
	    // fallback position
	    return o.getType().getName();
	}

	public Class getColumnClass(final int arg0) {
		switch(arg0) {
		case 0: return Icon.class;
		case 1: return Object.class;
		case 2: return Date.class;
		case 3: return Long.class;
		case 4: return Object.class;
		default:
			throw new IndexOutOfBoundsException("Oversized column index " + arg0);
		}
	}

	public Comparator getColumnComparator(final int arg0) {
		switch (arg0) {
		case 0:
			return null;
		case 1:
			return GlazedLists.caseInsensitiveComparator();
		case 2:
		case 3:
		case 4:
			return GlazedLists.comparableComparator(); 
		default:
			throw new IndexOutOfBoundsException("Oversized column ix:" +arg0);
		}
	}

}
