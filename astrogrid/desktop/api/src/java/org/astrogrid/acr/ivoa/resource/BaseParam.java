/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

/**     a description of a parameter that places no restriction on 
            the parameter's data type.
            <p/>
            As the parameter's data type is usually important, schemas
            normally employ a sub-class of this type
            rather than this type directly.            
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 19, 20083:15:42 PM
 */
public class BaseParam implements Serializable {
    private String name;
    private String description;
    private String unit;
    private String ucd;
    /**                  the name of the column*/
    public String getName() {
        return this.name;
    }
    public final void setName(String name) {
        this.name = name;
    }
    /**                   a description of the column's contents */
    public final String getDescription() {
        return this.description;
    }
    public final void setDescription(String description) {
        this.description = description;
    }
    /**                  the unit associated with all values in the column*/
    public final String getUnit() {
        return this.unit;
    }
    public final void setUnit(String unit) {
        this.unit = unit;
    }
    /**                  the name of a unified content descriptor.*/
    public final String getUcd() {
        return this.ucd;
    }
    public final void setUcd(String ucd) {
        this.ucd = ucd;
    }
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result
                + ((this.ucd == null) ? 0 : this.ucd.hashCode());
        result = prime * result
                + ((this.unit == null) ? 0 : this.unit.hashCode());
        return result;
    }
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BaseParam other = (BaseParam) obj;
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
        if (this.ucd == null) {
            if (other.ucd != null)
                return false;
        } else if (!this.ucd.equals(other.ucd))
            return false;
        if (this.unit == null) {
            if (other.unit != null)
                return false;
        } else if (!this.unit.equals(other.unit))
            return false;
        return true;
    }

}
