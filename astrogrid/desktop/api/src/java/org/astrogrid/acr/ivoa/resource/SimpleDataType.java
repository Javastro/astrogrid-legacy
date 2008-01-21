/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

/**      a simple data type that does not imply a size nor precise format.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 19, 20083:17:53 PM
 */
public class SimpleDataType implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -1238641590718504319L;
    private String type;
    private String arraysize = "1";

    
    /**  a scalar data type name taken from a small controlled set
            that does not imply a size nor precise format.
            @return 'integer','real','complex','boolean','char','string' */
    public final String getType() {
        return this.type;
    }
    public final void setType(String type) {
        this.type = type;
    }
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.arraysize == null) ? 0 : this.arraysize.hashCode());
        result = prime * result
                + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SimpleDataType other = (SimpleDataType) obj;
        if (this.arraysize == null) {
            if (other.arraysize != null)
                return false;
        } else if (!this.arraysize.equals(other.arraysize))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        } else if (!this.type.equals(other.type))
            return false;
        return true;
    }
    /**                     the shape of the array that constitutes the value
     * <p/>
     *             An expression of a the shape of a multi-dimensional array
            of the form LxNxM... where each value between gives the
            integer length of the array along a dimension.  An
            asterisk (*) as the last dimension of the shape indicates 
            that the length of the last axis is variable or
            undetermined. 
            <p />
     *                      the default is "1"; i.e. the value is a scalar.
     * */
    public final String getArraysize() {
        return this.arraysize;
    }

    public final void setArraysize(String arraysize) {
        this.arraysize = arraysize;
    }
    

}
