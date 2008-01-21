/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.util.Arrays;

/**
 *            A service invoked via an HTTP Query (either Get or Post)
           with a set of arguments consisting of keyword name-value pairs.
           <p />
                    Note that the URL for help with this service can be put into
           the Service/ReferenceURL element.  
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 19, 20083:11:56 PM
 */
public class ParamHttpInterface extends Interface {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3531933273879098193L;
    private static int hashCode(Object[] array) {
        final int prime = 31;
        if (array == null)
            return 0;
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
    /**    The type of HTTP request, either GET or POST. */
    public final String getQueryType() {
        return this.queryType;
    }

    public final void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    /**   The MIME type of a document returned */
    public final String getResultType() {
        return this.resultType;
    }

    public final void setResultType(String resultType) {
        this.resultType = resultType;
    }
/**                       a description of a input parameter.  Each should be 
                       rendered as name=value in the query URL's arguements.  */
    public final InputParam[] getParams() {
        return this.params;
    }

    public final void setParams(InputParam[] params) {
        this.params = params;
    }

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

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ParamHttpInterface other = (ParamHttpInterface) obj;
        if (!Arrays.equals(this.params, other.params))
            return false;
        if (this.queryType == null) {
            if (other.queryType != null)
                return false;
        } else if (!this.queryType.equals(other.queryType))
            return false;
        if (this.resultType == null) {
            if (other.resultType != null)
                return false;
        } else if (!this.resultType.equals(other.resultType))
            return false;
        return true;
    }
    
    
    

}
