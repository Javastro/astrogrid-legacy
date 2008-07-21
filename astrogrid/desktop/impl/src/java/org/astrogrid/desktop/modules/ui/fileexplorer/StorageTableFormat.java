/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.Comparator;
import java.util.Date;

import javax.swing.Icon;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;

/** glazed lists format for a table of files.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 29, 20071:57:55 AM
 */
public class StorageTableFormat implements AdvancedTableFormat {
	/**
	 * 
	 */
	public StorageTableFormat(IconFinder icons) {
		this.icons = icons;
	}
	private final IconFinder icons;
	private static final int COLUMN_COUNT = 5;
	
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	public String getColumnName(int arg0) {
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

	public Object getColumnValue(Object arg0, int arg1) {
		FileObject o = (FileObject)arg0;
		try {
		switch(arg1) {
			case 0:
			// mk an icon.
				return icons.find(o);
			case 1:
				return o.getName().getBaseName();
			case 2:
				return new Date(o.getContent().getLastModifiedTime());
			case 3:
				if (! o.getType().hasContent()) {
					return null;
				}
				long sz = o.getContent().getSize() ;
				return new Long(sz);
			case 4:
				return findBestContentType(o);
			
			default:
				throw new IndexOutOfBoundsException("Oversized column index " + arg1);
		}
		} catch (FileSystemException e) {
			return null;
		}
	}

    /** find the most user-readable type description for a file.
     * @param o
     * @return
     * @throws FileSystemException
     */
	public static String  findBestContentType(FileObject o) throws FileSystemException {
	    if (o.getType().hasContent()) {
	        String cType =  o.getContent().getContentInfo().getContentType();
	        while (StringUtils.isEmpty(cType) && AstroscopeFileObject.isOnlyDelegateFileObject(o)) {
	            o = ((DelegateFileObject)o).getDelegateFile();
	            cType = o.getContent().getContentInfo().getContentType();
	        }
	        if (StringUtils.isNotEmpty(cType)) {
	            return cType;
	        }
	    }
	    // fallback position
	    return o.getType().getName();
	}

	public Class getColumnClass(int arg0) {
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

	public Comparator getColumnComparator(int arg0) {
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
