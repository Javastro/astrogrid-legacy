/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

/**   A datatype for an InputParam.
 * These types do not imply a size nor precise format.
 * @see InputParam
 * @author Noel.Winstanley@manchester.ac.uk
 */
public class SimpleDataType implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -1238641590718504319L;
    private String type;
    private String arraysize = "1";

    
    /**  The name of the type.
            @return one of {@code integer},{@code real}, {@code complex},{@code boolean},{@code char},{@code string} */
    public final String getType() {
        return this.type;
    }
    /** @exclude */
    public final void setType(final String type) {
        this.type = type;
    }
    /** @exclude */    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.arraysize == null) ? 0 : this.arraysize.hashCode());
        result = prime * result
                + ((this.type == null) ? 0 : this.type.hashCode());
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleDataType other = (SimpleDataType) obj;
        if (this.arraysize == null) {
            if (other.arraysize != null) {
                return false;
            }
        } else if (!this.arraysize.equals(other.arraysize)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        return true;
    }
    /**                     The shape of the array that constitutes the value.
     * 
     *     @return An expression of the shape of a multi-dimensional array
            of the form {@code LxNxM}, where each value between gives the
            integer length of the array along a dimension.  An
            asterisk (*) as the last dimension of the shape indicates 
            that the length of the last axis is variable or
            undetermined. 
           
     * @note the default return value is "1"; i.e. the value is a scalar.
     * */
    public final String getArraysize() {
        return this.arraysize;
    }
    /** @exclude */
    public final void setArraysize(final String arraysize) {
        this.arraysize = arraysize;
    }
    

}
