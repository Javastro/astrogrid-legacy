/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

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

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.iconName == null) ? 0 : this.iconName.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Folder other = (Folder) obj;
		if (this.iconName == null) {
			if (other.iconName != null)
				return false;
		} else if (!this.iconName.equals(other.iconName))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		return true;
	}

}