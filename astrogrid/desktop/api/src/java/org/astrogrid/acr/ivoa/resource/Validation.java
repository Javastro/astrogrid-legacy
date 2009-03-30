/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;

/**    A validation stamp for a resouce.
 * It combinines a validation level and the ID of 
         the validator.
       
 * @author Noel Winstanley
 */
public class Validation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7087731844708485379L;
	private URI validatedBy;
	private  int validationLevel = 0;
	/** the name of the authority that has validated this record */
	public URI getValidatedBy() {
		return this.validatedBy;
	}
	/** the validation level this record has attained */
	public int getValidationLevel() {
		return this.validationLevel;
	}
    /** @exclude */
	public void setValidatedBy(final URI validatedBy) {
		this.validatedBy = validatedBy;
	}
    /** @exclude */
	public void setValidationLevel(final int validationLevel) {
		this.validationLevel = validationLevel;
	}
    /** @exclude */
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.validatedBy == null) ? 0 : this.validatedBy.hashCode());
		result = PRIME * result + this.validationLevel;
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
		if (! (obj instanceof Validation)) {
            return false;
        }
		final Validation other = (Validation) obj;
		if (this.validatedBy == null) {
			if (other.validatedBy != null) {
                return false;
            }
		} else if (!this.validatedBy.equals(other.validatedBy)) {
            return false;
        }
		if (this.validationLevel != other.validationLevel) {
            return false;
        }
		return true;
	}
    /** @exclude */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("Validation[");
			buffer.append(", validatedBy = ").append(validatedBy);
			buffer.append(", validationLevel = ").append(validationLevel);
			buffer.append("]");
			return buffer.toString();
		}
	
	

}
