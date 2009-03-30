/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

import org.astrogrid.acr.astrogrid.ColumnBean;

/**   A datatype supported by a VOTable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @see ColumnBean
 */
public class TableDataType implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 6077581732125120430L;
    private String type;
    private String arraysize = "1";
    /**
     * A scalar data type name from the set supported by VOTable.
     * @return one of {@code boolean}, {@code bit}, {@code unsignedByte}, {@code short}
     * ,{@code int}, {@code long}, {@code char}, {@code unicodeChar}
     * ,{@code float}, {@code double}, {@code floatComplex}, {@code doubleComplex}, {@code string}
     */
    public final String getType() {
        return this.type;
    }
    /** @exclude */
    public final void setType(final String type) {
        this.type = type;
    }
    /**                     The shape of the array that constitutes the value
     * @return
     *             An expression of a the shape of a multi-dimensional array
            of the form LxNxM... where each value between gives the
            integer length of the array along a dimension.  An
            asterisk (*) as the last dimension of the shape indicates 
            that the length of the last axis is variable or
            undetermined. 
            
     * @note the default is {@code 1}; i.e. the value is a scalar.
     * */    
    public final String getArraysize() {
        return this.arraysize;
    }
    /** @exclude */
    public final void setArraysize(final String arraysize) {
        this.arraysize = arraysize;
    }
    /** @exclude */
    @Override
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
    @Override
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
        final TableDataType other = (TableDataType) obj;
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
    
    
}
