/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**          
 * A parameter to a HTTP service interface.
            <p />
            The allowed data type names do not imply a size or precise
            format.  This type is intended to be sufficient for describing
            an input parameter to a simple REST service.
 * @see ParamHttpInterface                      
 * @author Noel.Winstanley@manchester.ac.uk
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
    /** @exclude */
    public final void setStandard(final boolean standard) {
        this.standard = standard;
    }
    /**   Describes whether this parameter is required for the application
                     or service to which it applies to work properly. 
                     @return - 'required', 'optional','ignored'*/
    public final String getUse() {
        return this.use;
    }
    /** @exclude */    
    public final void setUse(final String use) {
        this.use = use;
    }
    /**                         a type of data contained in the column*/
    public final SimpleDataType getDataType() {
        return this.dataType;
    }
    /** @exclude */    
    public final void setDataType(final SimpleDataType dataType) {
        this.dataType = dataType;
    }
    /** @exclude */    
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
        final InputParam other = (InputParam) obj;
        if (this.dataType == null) {
            if (other.dataType != null) {
                return false;
            }
        } else if (!this.dataType.equals(other.dataType)) {
            return false;
        }
        if (this.standard != other.standard) {
            return false;
        }
        if (this.use == null) {
            if (other.use != null) {
                return false;
            }
        } else if (!this.use.equals(other.use)) {
            return false;
        }
        return true;
    }
}
