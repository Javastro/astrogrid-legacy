/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.util.Arrays;

import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;

/** A catalog in a data collection.
 * 
 * A catalog aggegates together a number of tables (modelled by {@link TableBean}). Each table in turn contains 
 * a number of columns (modelled by {@link ColumnBean}).
 * <p />
 * {@textdiagram database
 *   +-----------+
 *   |  Catalog  |
 *   +-----+-----+
 *         |
 *         | 1..*
 *         |
 *  +------+------+
 *  |  TableBean  | ...
 *  +------+------+
 *         |
 *         | 1..*
 *         |
 *  +------+-------+
 *  |  ColumnBean  | ...
 *  +--------------+
 * }
 * 
 * @see DataCollection
 * @author Noel Winstanley
 */
public class Catalog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3507122325311663364L;
	private static int hashCode(final Object[] array) {
		final int PRIME = 31;
		if (array == null) {
            return 0;
        }
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = PRIME * result + (array[index] == null ? 0 : array[index].hashCode());
		}
		return result;
	}
	protected TableBean[] tables = new TableBean[0];
	protected String description;
	protected String name;
	/** @exclude */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = PRIME * result + Catalog.hashCode(this.tables);
		return result;
	}
	/** @exclude */
	public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (!( obj instanceof Catalog)) {
            return false;
        }
		final Catalog other = (Catalog) obj;
		if (this.description == null) {
			if (other.description != null) {
                return false;
            }
		} else if (!this.description.equals(other.description)) {
            return false;
        }
		if (this.name == null) {
			if (other.name != null) {
                return false;
            }
		} else if (!this.name.equals(other.name)) {
            return false;
        }
		if (!Arrays.equals(this.tables, other.tables)) {
            return false;
        }
		return true;
	}
	/** A readable description of this catalog */
	public String getDescription() {
		return this.description;
	}
	/** @exclude */
	public void setDescription(final String description) {
		this.description = description;
	}
	/** the name of the catalogue */
	public String getName() {
		return this.name;
	}
	/** @exclude */
	public void setName(final String name) {
		this.name = name;
	}
	/** lists the tables contained within this catalog */
	public TableBean[] getTables() {
		return this.tables;
	}
	/** @exclude */
	public void setTables(final TableBean[] tables) {
		this.tables = tables;
	}
	/** @exclude */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
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
