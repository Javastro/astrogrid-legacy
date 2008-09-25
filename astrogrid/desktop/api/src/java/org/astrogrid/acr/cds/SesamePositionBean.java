/**
 * 
 */
package org.astrogrid.acr.cds;

import java.io.Serializable;
import java.util.Arrays;

/** 
 * Object resolution information provided by Sesame
 * @see Sesame
 * @author Noel Winstanley

 */
public class SesamePositionBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7521201935481185321L;

	private static int hashCode(final Object[] array) {
		final int PRIME = 31;
		if (array == null) {
            return 0;
        }
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = PRIME * result + (array[index] == null ? 0 : array[index].hashCode());
		}
		return result;
	}
	/**
	 * 
	 */
	protected String target;
	protected String service;
	protected String oType;
	protected String oName;
	protected String posStr;
	protected double ra;
	protected double dec;
	protected double raErr;
	protected double decErr;
	protected String[] aliases;
	/** the declination of the object */
	public double getDec() {
		return this.dec;
	}
	/** @exclude */
	public void setDec(final double dec) {
		this.dec = dec;
	}
	/** the error in declination */
	public double getDecErr() {
		return this.decErr;
	}
	/** @exclude */
	public void setDecErr(final double decErr) {
		this.decErr = decErr;
	}
	/** the object name */
	public String getOName() {
		return this.oName;
	}
	/** @exclude */
	public void setOName(final String name) {
		this.oName = name;
	}
	/** the object type */
	public String getOType() {
		return this.oType;
	}
	/**@exclude */
	public void setOType(final String type) {
		this.oType = type;
	}
	/** full position, as a string */
	public String getPosStr() {
		return this.posStr;
	}
	/** @exclude */
	public void setPosStr(final String posStr) {
		this.posStr = posStr;
	}
	/** the right ascension of the object */
	public double getRa() {
		return this.ra;
	}
	/** @exclude */
	public void setRa(final double ra) {
		this.ra = ra;
	}
	/** the error in right ascension */
	public double getRaErr() {
		return this.raErr;
	}
	/** @exclude */
	public void setRaErr(final double raErr) {
		this.raErr = raErr;
	}
	/** the target object name */
	public String getTarget() {
		return this.target;
	}
	/** @exclude */
	public void setTarget(final String target) {
		this.target = target;
	}
	/** the service that resolved this object */
	public String getService() {
		return this.service;
	}
	/** @exclude */
	public void setService(final String service) {
		this.service = service;
	}
	/** aliases for this object name */
	public String[] getAliases() {
		return this.aliases;
	}
	/** @exclude */
	public void setAliases(final String[] aliases) {
		this.aliases = aliases;
	}
	/** @exclude */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + SesamePositionBean.hashCode(this.aliases);
		long temp;
		temp = Double.doubleToLongBits(this.dec);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.decErr);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + ((this.oName == null) ? 0 : this.oName.hashCode());
		result = PRIME * result + ((this.oType == null) ? 0 : this.oType.hashCode());
		result = PRIME * result + ((this.posStr == null) ? 0 : this.posStr.hashCode());
		temp = Double.doubleToLongBits(this.ra);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.raErr);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + ((this.service == null) ? 0 : this.service.hashCode());
		result = PRIME * result + ((this.target == null) ? 0 : this.target.hashCode());
		return result;
	}
	/** @exclude */
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
		final SesamePositionBean other = (SesamePositionBean) obj;
		if (!Arrays.equals(this.aliases, other.aliases)) {
            return false;
        }
		if (Double.doubleToLongBits(this.dec) != Double.doubleToLongBits(other.dec)) {
            return false;
        }
		if (Double.doubleToLongBits(this.decErr) != Double.doubleToLongBits(other.decErr)) {
            return false;
        }
		if (this.oName == null) {
			if (other.oName != null) {
                return false;
            }
		} else if (!this.oName.equals(other.oName)) {
            return false;
        }
		if (this.oType == null) {
			if (other.oType != null) {
                return false;
            }
		} else if (!this.oType.equals(other.oType)) {
            return false;
        }
		if (this.posStr == null) {
			if (other.posStr != null) {
                return false;
            }
		} else if (!this.posStr.equals(other.posStr)) {
            return false;
        }
		if (Double.doubleToLongBits(this.ra) != Double.doubleToLongBits(other.ra)) {
            return false;
        }
		if (Double.doubleToLongBits(this.raErr) != Double.doubleToLongBits(other.raErr)) {
            return false;
        }
		if (this.service == null) {
			if (other.service != null) {
                return false;
            }
		} else if (!this.service.equals(other.service)) {
            return false;
        }
		if (this.target == null) {
			if (other.target != null) {
                return false;
            }
		} else if (!this.target.equals(other.target)) {
            return false;
        }
		return true;
	}
	/** @exclude
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("SesamePositionBean[");
			buffer.append("target = ").append(target);
			buffer.append(", service = ").append(service);
			buffer.append(", oType = ").append(oType);
			buffer.append(", oName = ").append(oName);
			buffer.append(", posStr = ").append(posStr);
			buffer.append(", ra = ").append(ra);
			buffer.append(", dec = ").append(dec);
			buffer.append(", raErr = ").append(raErr);
			buffer.append(", decErr = ").append(decErr);
			if (aliases == null) {
				buffer.append(", aliases = ").append("null");
			} else {
				buffer.append(", aliases = ").append(
					Arrays.asList(aliases).toString());
			}
			buffer.append("]");
			return buffer.toString();
		}
	
	
}
