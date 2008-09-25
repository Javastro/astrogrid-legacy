/**
 * 
 */
package org.votech;

import java.io.Serializable;
import java.net.URI;

/** Description of a service's availabiltiy.
 * @author Noel Winstanley
 */
public class VoMonBean implements Serializable{
/** code representing 'up' / 'available'*/
	public static final int UP_CODE = 5;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8485171001578027954L;
	int code = 6; // think this means unknown.
	URI id;
	long millis = -1;
	String status = "unknown";
	String timestamp = "1970-01-01 00:59:59.999";
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
		final VoMonBean other = (VoMonBean) obj;
		if (this.code != other.code) {
            return false;
        }
		if (this.id == null) {
			if (other.id != null) {
                return false;
            }
		} else if (!this.id.equals(other.id)) {
            return false;
        }
		if (this.millis != other.millis) {
            return false;
        }
		if (this.status == null) {
			if (other.status != null) {
                return false;
            }
		} else if (!this.status.equals(other.status)) {
            return false;
        }
		if (this.timestamp == null) {
			if (other.timestamp != null) {
                return false;
            }
		} else if (!this.timestamp.equals(other.timestamp)) {
            return false;
        }
		return true;
	}
	/** the status code of the service 
	 * @see #getStatus()*/
	public int getCode() {
		return this.code;
	}
	/** the registry id of the service */
	public URI getId() {
		return this.id;
	}
	/** time (in milliseconds) when this service was last queried
	 * @see #getTimestamp() */
	public long getMillis() {
		return this.millis;
	}
	/** Readable string version of code 
	 * @see #getStatus()*/
	public String getStatus() {
		return this.status;
	}
	/** time (as string timestamp) when this service was last queried
	 * @see #getMillis()*/
	public String getTimestamp() {
		return this.timestamp;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + this.code;
		result = PRIME * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = PRIME * result + (int) (this.millis ^ (this.millis >>> 32));
		result = PRIME * result + ((this.status == null) ? 0 : this.status.hashCode());
		result = PRIME * result + ((this.timestamp == null) ? 0 : this.timestamp.hashCode());
		return result;
	}
	public void setCode(final int code) {
		this.code = code;
	}
	public void setId(final URI id) {
		this.id = id;
	}
	public void setMillis(final long millis) {
		this.millis = millis;
	}
	public void setStatus(final String status) {
		this.status = status;
	}
	public void setTimestamp(final String timestamp) {
		this.timestamp = timestamp;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("VoMonBean[");
			buffer.append(" id = ").append(id);
			buffer.append(", status = ").append(status);
			buffer.append(", millis = ").append(millis);
			buffer.append(", code = ").append(code);
			buffer.append(", timestamp = ").append(timestamp);
			buffer.append("]");
			return buffer.toString();
		}
}
