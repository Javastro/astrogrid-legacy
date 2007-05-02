/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import javax.swing.Icon;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.icons.IconHelper;

/** baseclass for all persistent folders
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 200712:14:07 AM
 */
public class Folder {
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory.getLog(Folder.class);

	/**
	 * 
	 */
	public Folder(String name, String iconName) {
		this.name = name;
		this.iconName = iconName;
	}
	
	public Folder() {
	}

	protected String name;
	protected String iconName;

	public String getIconName() {
		return this.iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public Icon getIcon() {
		return IconHelper.loadIcon(iconName); // iconHelper takes care of caching.
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append(StringUtils.substringAfterLast(this.getClass().getName(),"."));
			buffer.append("[");
			buffer.append("name = ").append(name);
			buffer.append(", iconName = ").append(iconName);
			buffer.append("]");
			return buffer.toString();
		}


}