/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;

/** Descripiton of the Capabilities of a Simple Image Access Service.
 * @author Noel Winstanley
 * @since Aug 5, 20069:57:07 PM
 */
public class SiapCapability extends Capability {
    public SiapCapability() {
        setStandardID(CAPABILITY_ID);
    }
    /** standard identifier for this capability */
    private static final URI CAPABILITY_ID = URI.create("ivo://ivoa.net/std/SIA");

	
	public static class SkySize implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -360917596945148030L;
		private float _long;
		private float _lat;
		/** The maximum size in the latitdude (Dec.) direction */
		public float getLat() {
			return this._lat;
		}
		public void setLat(float _lat) {
			this._lat = _lat;
		}
		/**     The maximum size in the longitude (R.A.) direction  */
		public float getLong() {
			return this._long;
		}
		public void setLong(float _long) {
			this._long = _long;
		}
		/**
			 * toString methode: creates a String representation of the object
			 * @return the String representation
			 * @author info.vancauwenberge.tostring plugin
		
			 */
			public String toString() {
				StringBuffer buffer = new StringBuffer();
				buffer.append(getLong()).append(", ").append(getLat());
				return buffer.toString();
			}
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + Float.floatToIntBits(this._lat);
			result = PRIME * result + Float.floatToIntBits(this._long);
			return result;
		}
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (! (obj instanceof SkySize))
				return false;
			final SkySize other = (SkySize) obj;
			if (Float.floatToIntBits(this._lat) != Float.floatToIntBits(other._lat))
				return false;
			if (Float.floatToIntBits(this._long) != Float.floatToIntBits(other._long))
				return false;
			return true;
		}
		
	}
	
	public static class ImageSize implements Serializable {
	
        /**
         * 
         */
        private static final long serialVersionUID = -2822467213850873238L;
        private int _long;
        private int _lat;
        /** The maximum size in the latitdude (Dec.) direction */
        public int getLat() {
            return this._lat;
        }
        public void setLat(int _lat) {
            this._lat = _lat;
        }
        /**     The maximum size in the longitude (R.A.) direction  */
        public int getLong() {
            return this._long;
        }
        public void setLong(int _long) {
            this._long = _long;
        }
        /**
             * toString methode: creates a String representation of the object
             * @return the String representation
             * @author info.vancauwenberge.tostring plugin
        
             */
            public String toString() {
                StringBuffer buffer = new StringBuffer();
                buffer.append(getLong()).append(", ").append(getLat());
                return buffer.toString();
            }
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + this._lat;
            result = PRIME * result + this._long;
            return result;
        }
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (! (obj instanceof ImageSize))
                return false;
            final ImageSize other = (ImageSize) obj;
            if (this._lat != other._lat)
                return false;
            if (this._long != other._long)
                return false;
            return true;
        }
	}
	
	public static class SkyPos implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -2983641280264751668L;
        private double _long;
        private double _lat;
        /** The maximum size in the latitdude (Dec.) direction */
        public double getLat() {
            return this._lat;
        }
        public void setLat(double _lat) {
            this._lat = _lat;
        }
        /**     The maximum size in the longitude (R.A.) direction  */
        public double getLong() {
            return this._long;
        }
        public void setLong(double _long) {
            this._long = _long;
        }
        /**
             * toString methode: creates a String representation of the object
             * @return the String representation
             * @author info.vancauwenberge.tostring plugin
        
             */
            public String toString() {
                StringBuffer buffer = new StringBuffer();
                buffer.append(getLong()).append(", ").append(getLat());
                return buffer.toString();
            }
        public int hashCode() {
            final long PRIME = 31;
            long result = 1;
            result = PRIME * result + Double.doubleToLongBits(this._lat);
            result = PRIME * result + Double.doubleToLongBits(this._long);
            return (int)result;
        }
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (! (obj instanceof SkyPos))
                return false;
            final SkyPos other = (SkyPos) obj;
            if (Double.doubleToLongBits(this._lat) != Double.doubleToLongBits(other._lat))
                return false;
            if (Double.doubleToLongBits(this._long) != Double.doubleToLongBits(other._long))
                return false;
            return true;
        }
	}
	
	public static class Query implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -167553349516967483L;
		private SkySize size;
		private SkyPos pos;
		private int verb;
		private String extras;
		/**  any extra (particularly non-standard) parameters that must 
                   be provided (apart from what is part of base URL given by 
                   the accessURL element */
		public String getExtras() {
			return this.extras;
		}
		public void setExtras(String extras) {
			this.extras = extras;
		}

		public SkyPos getPos() {
			return this.pos;
		}
		public void setPos(SkyPos pos) {
			this.pos = pos;
		}
		public SkySize getSize() {
			return this.size;
		}
		public void setSize(SkySize size) {
			this.size = size;
		}
		/**  the verbosity level to use where 0 means the bare
                   minimum set of columns and 3 means the full set of 
                   available columns. */
		public int getVerb() {
			return this.verb;
		}
		public void setVerb(int verb) {
			this.verb = verb;
		}
		/**
			 * toString methode: creates a String representation of the object
			 * @return the String representation
			 * @author info.vancauwenberge.tostring plugin
		
			 */
			public String toString() {
				StringBuffer buffer = new StringBuffer();
				buffer.append("Query[");
				buffer.append("size = ").append(size);
				buffer.append(", pos = ").append(pos);
				buffer.append(", verb = ").append(verb);
				buffer.append(", extras = ").append(extras);
				buffer.append("]");
				return buffer.toString();
			}
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((this.extras == null) ? 0 : this.extras.hashCode());
			result = PRIME * result + ((this.pos == null) ? 0 : this.pos.hashCode());
			result = PRIME * result + ((this.size == null) ? 0 : this.size.hashCode());
			result = PRIME * result + this.verb;
			return result;
		}
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Query other = (Query) obj;
			if (this.extras == null) {
				if (other.extras != null)
					return false;
			} else if (!this.extras.equals(other.extras))
				return false;
			if (this.pos == null) {
				if (other.pos != null)
					return false;
			} else if (!this.pos.equals(other.pos))
				return false;
			if (this.size == null) {
				if (other.size != null)
					return false;
			} else if (!this.size.equals(other.size))
				return false;
			if (this.verb != other.verb)
				return false;
			return true;
		}
	}
	
  
	protected String imageServiceType ;
    protected SkySize maxQueryRegionSize;
    protected SkySize maxImageExtent;
    protected ImageSize maxImageSize;
    protected int maxFileSize;
    protected int maxRecords;
    protected Query testQuery;
    /** describes what kind of service this is - cutout, etc */
	public String getImageServiceType() {
		return this.imageServiceType;
	}
	public void setImageServiceType(String imageServiceType) {
		this.imageServiceType = imageServiceType;
	}
	/** access the maximum size for returned files */
	public int getMaxFileSize() {
		return this.maxFileSize;
	}
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	/**  The maximum image query region size, expressed in 
                         decimal degrees. A value of 360 degrees indicates 
                         that there is no limit and the entire data collection 
                         (entire sky) can be requested.*/
	public SkySize getMaxImageExtent() {
		return this.maxImageExtent;
	}
	public void setMaxImageExtent(SkySize maxImageExtent) {
		this.maxImageExtent = maxImageExtent;
	}
	/** The largest image (in terms of pixels) that
                  		can be requested. */
	public ImageSize getMaxImageSize() {
		return this.maxImageSize;
	}
	public void setMaxImageSize(ImageSize maxImageSize) {
		this.maxImageSize = maxImageSize;
	}
	/** The maximum image query region size, expressed in 
                         decimal degrees. A value of 360 degrees indicates that 
                         there is no limit and the entire data collection 
                         (entire sky) can be queried.  */
	public SkySize getMaxQueryRegionSize() {
		return this.maxQueryRegionSize;
	}
	public void setMaxQueryRegionSize(SkySize maxQueryRegionSize) {
		this.maxQueryRegionSize = maxQueryRegionSize;
	}
	/** a set of query parameters that is expected
                        to produce at least one matched record which
                        can be used to test the service.   */
	public Query getTestQuery() {
		return this.testQuery;
	}
	public void setTestQuery(Query testQuery) {
		this.testQuery = testQuery;
	}
	/**  The largest number of records that the Image Query web
                        method will return.  */
	public int getMaxRecords() {
		return this.maxRecords;
	}
	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.imageServiceType == null) ? 0 : this.imageServiceType.hashCode());
		result = PRIME * result + this.maxFileSize;
		result = PRIME * result + ((this.maxImageExtent == null) ? 0 : this.maxImageExtent.hashCode());
		result = PRIME * result + ((this.maxImageSize == null) ? 0 : this.maxImageSize.hashCode());
		result = PRIME * result + ((this.maxQueryRegionSize == null) ? 0 : this.maxQueryRegionSize.hashCode());
		result = PRIME * result + this.maxRecords;
		result = PRIME * result + ((this.testQuery == null) ? 0 : this.testQuery.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SiapCapability other = (SiapCapability) obj;
		if (this.imageServiceType == null) {
			if (other.imageServiceType != null)
				return false;
		} else if (!this.imageServiceType.equals(other.imageServiceType))
			return false;
		if (this.maxFileSize != other.maxFileSize)
			return false;
		if (this.maxImageExtent == null) {
			if (other.maxImageExtent != null)
				return false;
		} else if (!this.maxImageExtent.equals(other.maxImageExtent))
			return false;
		if (this.maxImageSize == null) {
			if (other.maxImageSize != null)
				return false;
		} else if (!this.maxImageSize.equals(other.maxImageSize))
			return false;
		if (this.maxQueryRegionSize == null) {
			if (other.maxQueryRegionSize != null)
				return false;
		} else if (!this.maxQueryRegionSize.equals(other.maxQueryRegionSize))
			return false;
		if (this.maxRecords != other.maxRecords)
			return false;
		if (this.testQuery == null) {
			if (other.testQuery != null)
				return false;
		} else if (!this.testQuery.equals(other.testQuery))
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
			buffer.append("SiapCapability[");
			buffer.append("imageServiceType = ").append(imageServiceType);
			buffer.append(", maxQueryRegionSize = ").append(maxQueryRegionSize);
			buffer.append(", maxImageExtent = ").append(maxImageExtent);
			buffer.append(", maxImageSize = ").append(maxImageSize);
			buffer.append(", maxFileSize = ").append(maxFileSize);
			buffer.append(", maxRecords = ").append(maxRecords);
			buffer.append(", testQuery = ").append(testQuery);
			buffer.append("]");
			return buffer.toString();
		}

}
