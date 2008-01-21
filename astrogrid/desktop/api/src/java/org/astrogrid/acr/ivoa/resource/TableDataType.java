/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

/**    a (VOTable-supported) data type
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 19, 20083:43:05 PM
 */
public class TableDataType implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 6077581732125120430L;
    private String type;
    private String arraysize = "1";
    /**
     * a scalar data type name taken from a controlled set that
            corresponds closely to VOTable data types.
     * @return 'boolean','bit','unsignedByte','short','int','long','char','unicodeChar','float','double','floatComplex','doubleComplex','string'
     */
    public final String getType() {
        return this.type;
    }
    public final void setType(String type) {
        this.type = type;
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
        final TableDataType other = (TableDataType) obj;
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
    
    
}
