/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**            a description of a service or function parameter having a
            fixed data type. 
            <p />
            The allowed data type names do not imply a size or precise
            format.  This type is intended to be sufficient for describing
            an input parameter to a simple REST service or a function 
            written in a weakly-typed (e.g., scripting) language.            
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 19, 20083:15:29 PM
 */
public class InputParam extends BaseParam{
    /**
     * 
     */
    private static final long serialVersionUID = 7308322346251330483L;
    private boolean standard = false; 
    private String use = "optional";
    private SimpleDataType dataType;
    /**
    If true, the meaning and behavior of this parameter is
    reserved and defined by a standard interface.  If
    false, it represents an implementation-specific
    parameter that effectively extends the behavior of the 
    service or application.  */
    public final boolean isStandard() {
        return this.standard;
    }
    public final void setStandard(boolean standard) {
        this.standard = standard;
    }
    /**   Describes whether this parameter is required for the application
                     or service to which it applies to work properly. 
                     @reiurn - 'required', 'optional','ignored'*/
    public final String getUse() {
        return this.use;
    }
    public final void setUse(String use) {
        this.use = use;
    }
    /**                         a type of data contained in the column*/
    public final SimpleDataType getDataType() {
        return this.dataType;
    }
    public final void setDataType(SimpleDataType dataType) {
        this.dataType = dataType;
    }
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.dataType == null) ? 0 : this.dataType.hashCode());
        result = prime * result + (this.standard ? 1231 : 1237);
        result = prime * result
                + ((this.use == null) ? 0 : this.use.hashCode());
        return result;
    }
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final InputParam other = (InputParam) obj;
        if (this.dataType == null) {
            if (other.dataType != null)
                return false;
        } else if (!this.dataType.equals(other.dataType))
            return false;
        if (this.standard != other.standard)
            return false;
        if (this.use == null) {
            if (other.use != null)
                return false;
        } else if (!this.use.equals(other.use))
            return false;
        return true;
    }
}
