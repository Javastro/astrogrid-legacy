/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;

import org.astrogrid.acr.ivoa.Cone;

/** The capability to perform an IVOA Cone Search
 * @bean
 * @author Noel Winstanley
 * @see ConeService
 * @see Cone
 */
public class ConeCapability extends Capability {
    /**
     * 
     */
    private static final long serialVersionUID = -5142255759957380546L;
    /** @exclude */
    public ConeCapability() {
        setStandardID(CAPABILITY_ID);
    }
    /** standard identifier for this capability */
    private static final URI CAPABILITY_ID = URI.create("ivo://ivoa.net/std/ConeSearch");
	/**
	 * 
	 */
	/**
	 * 
	 */
	protected float maxSR;
    protected int maxRecords;
    protected boolean verbosity;
    protected Query testQuery;
	
	/** the maximum number of records this service will return for a cone search.*/
	public int getMaxRecords() {
		return this.maxRecords;
	}
	/** @exclude */
	public void setMaxRecords(final int maxRecords) {
		this.maxRecords = maxRecords;
	}
	/** the maximum search radius accepted by this service */
	public float getMaxSR() {
		return this.maxSR;
	}
    /** @exclude */	
	public void setMaxSR(final float maxSR) {
		this.maxSR = maxSR;
	}
	
	/** Determines whether this service accepts the VERB parameter */
	public boolean isVerbosity() {
		return this.verbosity;
	}
    /** @exclude */	
	public void setVerbosity(final boolean verbosity) {
		this.verbosity = verbosity;
	}
	/** A test query for a cone search service. 
	 * @bean */
	public static class Query implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7994110673447948866L;
		/**
		 * 
		 */
		private double ra;
		private double dec;
		private double sr;
		private int verb;
		private String catalog;
		private String extras;
		/**         the catalog to query.   */
		public String getCatalog() {
			return this.catalog;
		}
	    /** @exclude */		
		public void setCatalog(final String catalog) {
			this.catalog = catalog;
		}
		/** the declination of the search cone's center in
                   decimal degrees.  */
		public double getDec() {
			return this.dec;
		}
	    /** @exclude */		
		public void setDec(final double dec) {
			this.dec = dec;
		}
		/**   any extra (non-standard) parameters that must be 
                   provided (apart from what is part of base URL given 
                   by the accessURL element).*/
		public String getExtras() {
			return this.extras;
		}
	    /** @exclude */		
		public void setExtras(final String extras) {
			this.extras = extras;
		}
		/** the right ascension of the search cone's center in
                   decimal degrees. */
		public double getRa() {
			return this.ra;
		}
	    /** @exclude */		
		public void setRa(final double ra) {
			this.ra = ra;
		}
		/** the radius of the search cone in decimal degrees. */
		public double getSr() {
			return this.sr;
		}
	    /** @exclude */		
		public void setSr(final double sr) {
			this.sr = sr;
		}
		/** the verbosity level to use where 1 means the bare
                   minimum set of columns and 3 means the full set of 
                   available columns.*/
		public int getVerb() {
			return this.verb;
		}
	    /** @exclude */		
		public void setVerb(final int verb) {
			this.verb = verb;
		}
	    /** @exclude */		
		@Override
        public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((this.catalog == null) ? 0 : this.catalog.hashCode());
			long temp;
			temp = Double.doubleToLongBits(this.dec);
			result = PRIME * result + (int) (temp ^ (temp >>> 32));
			result = PRIME * result + ((this.extras == null) ? 0 : this.extras.hashCode());
			temp = Double.doubleToLongBits(this.ra);
			result = PRIME * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(this.sr);
			result = PRIME * result + (int) (temp ^ (temp >>> 32));
			result = PRIME * result + this.verb;
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
			if (!(obj instanceof Query)) {
                return false;
            }
			final Query other = (Query) obj;
			if (this.catalog == null) {
				if (other.catalog != null) {
                    return false;
                }
			} else if (!this.catalog.equals(other.catalog)) {
                return false;
            }
			if (Double.doubleToLongBits(this.dec) != Double.doubleToLongBits(other.dec)) {
                return false;
            }
			if (this.extras == null) {
				if (other.extras != null) {
                    return false;
                }
			} else if (!this.extras.equals(other.extras)) {
                return false;
            }
			if (Double.doubleToLongBits(this.ra) != Double.doubleToLongBits(other.ra)) {
                return false;
            }
			if (Double.doubleToLongBits(this.sr) != Double.doubleToLongBits(other.sr)) {
                return false;
            }
			if (this.verb != other.verb) {
                return false;
            }
			return true;
		}
		/**
		 * @exclude
			 * toString methode: creates a String representation of the object
			 * @return the String representation
			 * @author info.vancauwenberge.tostring plugin
		
			 */
			@Override
            public String toString() {
				final StringBuffer buffer = new StringBuffer();
				buffer.append("Query[");
				buffer.append("ra = ").append(ra);
				buffer.append(", dec = ").append(dec);
				buffer.append(", sr = ").append(sr);
				buffer.append(", verb = ").append(verb);
				buffer.append(", catalog = ").append(catalog);
				buffer.append(", extras = ").append(extras);
				buffer.append("]");
				return buffer.toString();
			}
	}
	/** A query that can be used to test the service - it will result in at least one
                        matched record  */
	public Query getTestQuery() {
		return this.testQuery;
	}
    /** @exclude */	
	public void setTestQuery(final Query testQuery) {
		this.testQuery = testQuery;
	}
    /** @exclude */	
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + this.maxRecords;
		result = PRIME * result + Float.floatToIntBits(this.maxSR);
		result = PRIME * result + ((this.testQuery == null) ? 0 : this.testQuery.hashCode());
		result = PRIME * result + (this.verbosity ? 1231 : 1237);
		return result;
	}
    /** @exclude */	
	@Override
    public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (!super.equals(obj)) {
            return false;
        }
		if (!(obj instanceof ConeCapability)) {
            return false;
        }
		final ConeCapability other = (ConeCapability) obj;
		if (this.maxRecords != other.maxRecords) {
            return false;
        }
		if (Float.floatToIntBits(this.maxSR) != Float.floatToIntBits(other.maxSR)) {
            return false;
        }
		if (this.testQuery == null) {
			if (other.testQuery != null) {
                return false;
            }
		} else if (!this.testQuery.equals(other.testQuery)) {
            return false;
        }
		if (this.verbosity != other.verbosity) {
            return false;
        }
		return true;
	}
	/** @exclude
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("ConeCapability[");
			buffer.append("maxSR = ").append(maxSR);
			buffer.append(", maxRecords = ").append(maxRecords);
			buffer.append(", verbosity = ").append(verbosity);
			buffer.append(", testQuery = ").append(testQuery);
			buffer.append("]");
			return buffer.toString();
		}
	
}
