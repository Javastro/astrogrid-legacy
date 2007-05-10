/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import javax.swing.Icon;
import javax.swing.JSeparator;

/** contribution that represents a divider.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 200712:28:40 PM
 */
public class UIDividerContribution extends JSeparator implements UIStructureContribution  {

	public UIStructureContribution cloneStructure() {
		UIDividerContribution other = new UIDividerContribution();
		return other;
	}

	protected String after;
	protected String before;
	protected String name;
	protected String parentName;
	/**
	 * @return the after
	 */
	public final String getAfter() {
		return this.after;
	}
	/**
	 * @param after the after to set
	 */
	public final void setAfter(String after) {
		this.after = after;
	}
	/**
	 * @return the before
	 */
	public final String getBefore() {
		return this.before;
	}
	/**
	 * @param before the before to set
	 */
	public final void setBefore(String before) {
		this.before = before;
	}
	/**
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}
	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parentName
	 */
	public final String getParentName() {
		return this.parentName;
	}
	/**
	 * @param parentName the parentName to set
	 */
	public final void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Icon getIcon() {
		return null;
	}
	public String getText() {
		return null;
	}
	

}
