/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.util.Date;

import org.joda.time.Period;

/** representation of the vosi response.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 20088:27:12 PM
 */
public class VosiAvailabilityBean {

    private boolean available;
    private Period uptime;
    private Date validTill;
    /** indicates whether this service is currently available */
    public final boolean isAvailable() {
        return this.available;
    }
    public final void setAvailable(boolean available) {
        this.available = available;
    }
    /** return the uptime (in seconds) */
    public final Period getUptime() {
        return this.uptime;
    }

    
    public final void setUptime(Period uptime) {
        this.uptime = uptime;
    }
    /** date that this availability is valid till- ie.. next scheduled downtime
     * may be null.
     * @return
     */
    public final Date getValidTill() {
        return this.validTill;
    }
    public final void setValidTill(Date validTill) {
        this.validTill = validTill;
    }
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.available ? 1231 : 1237);
        result = prime * result + uptime.hashCode();
        result = prime * result
                + ((this.validTill == null) ? 0 : this.validTill.hashCode());
        return result;
    }
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final VosiAvailabilityBean other = (VosiAvailabilityBean) obj;
        if (this.available != other.available)
            return false;
        if (this.uptime != other.uptime)
            return false;
        if (this.validTill == null) {
            if (other.validTill != null)
                return false;
        } else if (!this.validTill.equals(other.validTill))
            return false;
        return true;
    }
    
}
