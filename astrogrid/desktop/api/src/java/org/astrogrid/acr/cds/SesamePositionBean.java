/**
 * 
 */
package org.astrogrid.acr.cds;

import java.io.Serializable;
import java.util.Arrays;

/** 
 * Datastructure representing the position of an object known to Sesame.
 * @see Sesame
 * @author Noel Winstanley
 * @since 2006.3.rc4
 */
public class SesamePositionBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7521201935481185321L;

	private static int hashCode(Object[] array) {
		final int PRIME = 31;
		if (array == null)
			return 0;
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

	public double getDec() {
		return this.dec;
	}
	public void setDec(double dec) {
		this.dec = dec;
	}

	public double getDecErr() {
		return this.decErr;
	}
	public void setDecErr(double decErr) {
		this.decErr = decErr;
	}
	public String getOName() {
		return this.oName;
	}
	public void setOName(String name) {
		this.oName = name;
	}
	public String getOType() {
		return this.oType;
	}
	public void setOType(String type) {
		this.oType = type;
	}
	public String getPosStr() {
		return this.posStr;
	}
	public void setPosStr(String posStr) {
		this.posStr = posStr;
	}
	public double getRa() {
		return this.ra;
	}
	public void setRa(double ra) {
		this.ra = ra;
	}
	public double getRaErr() {
		return this.raErr;
	}
	public void setRaErr(double raErr) {
		this.raErr = raErr;
	}
	public String getTarget() {
		return this.target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getService() {
		return this.service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String[] getAliases() {
		return this.aliases;
	}
	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SesamePositionBean other = (SesamePositionBean) obj;
		if (!Arrays.equals(this.aliases, other.aliases))
			return false;
		if (Double.doubleToLongBits(this.dec) != Double.doubleToLongBits(other.dec))
			return false;
		if (Double.doubleToLongBits(this.decErr) != Double.doubleToLongBits(other.decErr))
			return false;
		if (this.oName == null) {
			if (other.oName != null)
				return false;
		} else if (!this.oName.equals(other.oName))
			return false;
		if (this.oType == null) {
			if (other.oType != null)
				return false;
		} else if (!this.oType.equals(other.oType))
			return false;
		if (this.posStr == null) {
			if (other.posStr != null)
				return false;
		} else if (!this.posStr.equals(other.posStr))
			return false;
		if (Double.doubleToLongBits(this.ra) != Double.doubleToLongBits(other.ra))
			return false;
		if (Double.doubleToLongBits(this.raErr) != Double.doubleToLongBits(other.raErr))
			return false;
		if (this.service == null) {
			if (other.service != null)
				return false;
		} else if (!this.service.equals(other.service))
			return false;
		if (this.target == null) {
			if (other.target != null)
				return false;
		} else if (!this.target.equals(other.target))
			return false;
		return true;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
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
