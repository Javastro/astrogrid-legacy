/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.util.Arrays;

/**
 *            A service invoked via an HTTP Query.
 *            
 *         The HTTP query may be of any form (i.e. Get or Post)
           with a set of arguments consisting of keyword name-value pairs.
           @note the URL for help with this service can be put into
           the {@link Content#getReferenceURI()} field.  
 * @author Noel.Winstanley@manchester.ac.uk
 */
public class ParamHttpInterface extends Interface {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3531933273879098193L;
    private static int hashCode(final Object[] array) {
        final int prime = 31;
        if (array == null) {
            return 0;
        }
        int result = 1;
        for (int index = 0; index < array.length; index++) {
            result = prime * result
                    + (array[index] == null ? 0 : array[index].hashCode());
        }
        return result;
    }

    private String queryType;
    
    private String resultType;
    private InputParam[] params;
    /**    The type of HTTP request
     * @return either {@code GET} or {@code POST}
     * @note it is possible that {@code PUT} may also be valid in the future. */
    public final String getQueryType() {
        return this.queryType;
    }
    /** @exclude */
    public final void setQueryType(final String queryType) {
        this.queryType = queryType;
    }

    /**   The MIME type of the document returned */
    public final String getResultType() {
        return this.resultType;
    }
    /** @exclude */
    public final void setResultType(final String resultType) {
        this.resultType = resultType;
    }
/**                   The required input parameters.  
 * @note when {@link #getQueryType()} returns {@code GET} each  input parameter should be 
                       rendered as {@code name=value} in the query URL.  */
    public final InputParam[] getParams() {
        return this.params;
    }
    /** @exclude */
    public final void setParams(final InputParam[] params) {
        this.params = params;
    }
    /** @exclude */
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ParamHttpInterface.hashCode(this.params);
        result = prime * result
                + ((this.queryType == null) ? 0 : this.queryType.hashCode());
        result = prime * result
                + ((this.resultType == null) ? 0 : this.resultType.hashCode());
        return result;
    }
    /** @exclude */
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParamHttpInterface other = (ParamHttpInterface) obj;
        if (!Arrays.equals(this.params, other.params)) {
            return false;
        }
        if (this.queryType == null) {
            if (other.queryType != null) {
                return false;
            }
        } else if (!this.queryType.equals(other.queryType)) {
            return false;
        }
        if (this.resultType == null) {
            if (other.resultType != null) {
                return false;
            }
        } else if (!this.resultType.equals(other.resultType)) {
            return false;
        }
        return true;
    }
    
    
    

}
