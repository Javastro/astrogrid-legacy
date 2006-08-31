/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.util.Arrays;

import org.astrogrid.acr.astrogrid.TableBean;

/** Represents a catalog in a data collection.
 * @author Noel Winstanley
 * @since Aug 5, 20069:45:12 PM
 */
public class Catalog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3507122325311663364L;
	private static int hashCode(Object[] array) {
		final int PRIME = 31;
		if (array == null)
			return 0;
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = PRIME * result + (array[index] == null ? 0 : array[index].hashCode());
		}
		return result;
	}
	protected TableBean[] tables;
	protected String description;
	protected String name;
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = PRIME * result + Catalog.hashCode(this.tables);
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Catalog other = (Catalog) obj;
		if (this.description == null) {
			if (other.description != null)
				return false;
		} else if (!this.description.equals(other.description))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (!Arrays.equals(this.tables, other.tables))
			return false;
		return true;
	}
	/** description of this catalog */
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/** the name of the catalogue */
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/** lists the tables contained within this catalog */
	public TableBean[] getTables() {
		return this.tables;
	}
	public void setTables(TableBean[] tables) {
		this.tables = tables;
	}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Catalog[");
			if (tables == null) {
				buffer.append("tables = ").append("null");
			} else {
				buffer.append("tables = ").append(Arrays.asList(tables).toString());
			}
			buffer.append(", description = ").append(description);
			buffer.append(", name = ").append(name);
			buffer.append("]");
			return buffer.toString();
		}
}
