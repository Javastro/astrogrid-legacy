/**
 * 
 */
package org.astrogrid.acr.ivoa;

import java.util.Arrays;
import java.util.Date;

/** Service Availability
 * @see Vosi
 * @author Noel.Winstanley@manchester.ac.uk
 * @since 1.2.1
 */
public class VosiAvailabilityBean {

    private String[] notes = new String[] {};
    private boolean available;
    private Date upSince;
    private Date downAt;
    private Date backAt;
    /**
     * One or more notes that the service has provided.
     */
    public String[] getNotes() {
        return this.notes;
    }
    /** @exclude 
     * @param notes the notes to set
     */
    public void setNotes(final String[] notes) {
        this.notes = notes;
    }
    /** @exclude */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.available ? 1231 : 1237);
        result = prime * result
                + ((this.backAt == null) ? 0 : this.backAt.hashCode());
        result = prime * result
                + ((this.downAt == null) ? 0 : this.downAt.hashCode());
        result = prime * result + Arrays.hashCode(this.notes);
        result = prime * result
                + ((this.upSince == null) ? 0 : this.upSince.hashCode());
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
        final VosiAvailabilityBean other = (VosiAvailabilityBean) obj;
        if (this.available != other.available) {
            return false;
        }
        if (this.backAt == null) {
            if (other.backAt != null) {
                return false;
            }
        } else if (!this.backAt.equals(other.backAt)) {
            return false;
        }
        if (this.downAt == null) {
            if (other.downAt != null) {
                return false;
            }
        } else if (!this.downAt.equals(other.downAt)) {
            return false;
        }
        if (!Arrays.equals(this.notes, other.notes)) {
            return false;
        }
        if (this.upSince == null) {
            if (other.upSince != null) {
                return false;
            }
        } else if (!this.upSince.equals(other.upSince)) {
            return false;
        }
        return true;
    }
    /** 
     * Indicates whether the service is currently available
     */
    public boolean isAvailable() {
        return this.available;
    }
    /** @exclude
     * @param available the available to set
     */
    public void setAvailable(final boolean available) {
        this.available = available;
    }
    /**
     * Date since the service has been up.
     */
    public Date getUpSince() {
        return this.upSince;
    }
    /** @exclude
     * @param upSince the upSince to set
     */
    public void setUpSince(final Date upSince) {
        this.upSince = upSince;
    }
    /**
     * Date that the service will be going down.
     */
    public Date getDownAt() {
        return this.downAt;
    }
    /** @exclude
     * @param downAt the downAt to set
     */
    public void setDownAt(final Date downAt) {
        this.downAt = downAt;
    }
    /**
     * Date at which the service will be available again.
     */
    public Date getBackAt() {
        return this.backAt;
    }
    /** @exclude
     * @param backAt the backAt to set
     */
    public void setBackAt(final Date backAt) {
        this.backAt = backAt;
    }
    
 /** @exclude */   
  @Override
public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("VosiAvailabilityBean[");
    sb.append("available=").append(available);
    sb.append(", upSince=").append(upSince);
    sb.append(", downAt=").append(downAt);
    sb.append(", backAt=").append(backAt);
    sb.append(", notes=").append(Arrays.toString(notes));
    sb.append("]");
    return sb.toString();
}
    
}
