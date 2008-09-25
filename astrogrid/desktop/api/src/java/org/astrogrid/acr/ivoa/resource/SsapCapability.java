/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;

import org.astrogrid.acr.ivoa.Ssap;

/** The capability to perform a Simple Spectral Access (SSAP) query.
 * @see SsapService
 * @see Ssap
 * @author Noel.Winstanley@manchester.ac.uk
 */
public class SsapCapability extends Capability {


    /**
     * 
     */
    private static final long serialVersionUID = -6409405290279095376L;

    private static int hashCode(final Object[] array) {
        final int prime = 31;
        if (array == null) {
            return 0;
        }
        int result = 1;
        for (int index = 0; index < array.length; index++) {
            result = prime * result
                    + (array[index] == null ? 0 : array[index].hashCode());
        }
        return result;
    }
    /**
     * @exclude
     */
    public SsapCapability() {
        setStandardID(CAPABILITY_ID);
    }
    /** standard identifier for this capability */
    private static final URI CAPABILITY_ID = URI.create("ivo://ivoa.net/std/SSA");
     private String complianceLevel;
    private String[] dataSource;
    private String[] creationType;
    private double maxSearchRadius;
    private int maxRecords;
    private int defaultMaxRecords;
    private double maxApeture;
    private String[] supportedFrames;
    private int maxFileSize;
    private Query testQuery;
    
    /** the level at which a 
            service instance complies with the SSA standard.
            @return One of {@code minimal},{@code query},{@code full} */
    public String getComplianceLevel() {
        return complianceLevel;
    }
    /** The defined categories that specify where the spectral data 
           originally came from. 
           @return One of {@code survey},{@code pointed},{@code custom},{@code theory},{@code artificial} */
    
    public String[] getDataSources() {
        return dataSource;
    }
    
    /** A test query for a SSAP service. */
    public static class Query implements Serializable{
        /**
         * 
         */
        private static final long serialVersionUID = 2188976022432815410L;
        /**
         * 
         */
        private PosParam pos;
        private double size;
        private String queryDataCmd;

        /**The center position the search  */
        public final PosParam getPos() {
            return this.pos;
        }
        /** @exclude */
        public final void setPos(final PosParam pos) {
            this.pos = pos;
        }
        /**The diameter of the search region specified in decimal
          degrees..*/
        public final double getSize() {
            return this.size;
        }
        /** @exclude */
        public final void setSize(final double size) {
            this.size = size;
        }
        /**Fully specified queryData test query formatted as an URL.
           @return argument list in the syntax specified by the SSA standard.
                   The list must exclude the REQUEST argument which is 
                   assumed to be set to "queryData".  VERSION may be
           included if the test query applies to a specific version
           of the service protocol.*/
        public final String getQueryDataCmd() {
            return this.queryDataCmd;
        }
        /** @exclude */        
        public final void setQueryDataCmd(final String queryDataCmd) {
            this.queryDataCmd = queryDataCmd;
        }
        /** @exclude */        
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((this.pos == null) ? 0 : this.pos.hashCode());
            result = prime
                    * result
                    + ((this.queryDataCmd == null) ? 0 : this.queryDataCmd
                            .hashCode());
            long temp;
            temp = Double.doubleToLongBits(this.size);
            result = prime * result + (int) (temp ^ (temp >>> 32));
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
            final Query other = (Query) obj;
            if (this.pos == null) {
                if (other.pos != null) {
                    return false;
                }
            } else if (!this.pos.equals(other.pos)) {
                return false;
            }
            if (this.queryDataCmd == null) {
                if (other.queryDataCmd != null) {
                    return false;
                }
            } else if (!this.queryDataCmd.equals(other.queryDataCmd)) {
                return false;
            }
            if (Double.doubleToLongBits(this.size) != Double
                    .doubleToLongBits(other.size)) {
                return false;
            }
            return true;
        }
        
    }

    /** The central coordinate of the spatial region to be searched.*/
    public static class PosParam implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 6413600222419505695L;
        private double longitude;
        private double lat;
        private String refframe;
        /**The longitude ( Right Ascension) of the center of the
                  search position in decimal degrees.*/
        public final double getLong() {
            return this.longitude;
        }
        /** @exclude */        
        public final void setLong(final double longitude) {
            this.longitude = longitude;
        }
        /**   The latitude (Declination) of the center of the
                  search position in decimal degrees.*/
        public final double getLat() {
            return this.lat;
        }
        /** @exclude */        
        public final void setLat(final double lat) {
            this.lat = lat;
        }
        /** The coordinate system reference frame name indicating
                  the frame to assume for the given position.   If not 
                  provided, ICRS is assumed.*/
        public final String getRefframe() {
            return this.refframe;
        }
        /** @exclude */        
        public final void setRefframe(final String refframe) {
            this.refframe = refframe;
        }
        /** @exclude */        
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            long temp;
            temp = Double.doubleToLongBits(this.lat);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(this.longitude);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result
                    + ((this.refframe == null) ? 0 : this.refframe.hashCode());
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
            final PosParam other = (PosParam) obj;
            if (Double.doubleToLongBits(this.lat) != Double
                    .doubleToLongBits(other.lat)) {
                return false;
            }
            if (Double.doubleToLongBits(this.longitude) != Double
                    .doubleToLongBits(other.longitude)) {
                return false;
            }
            if (this.refframe == null) {
                if (other.refframe != null) {
                    return false;
                }
            } else if (!this.refframe.equals(other.refframe)) {
                return false;
            }
            return true;
        }
    }
    /** @exclude */    
    public final void setDataSources(final String[] dataSource) {
        this.dataSource = dataSource;
    }
    /** the process used to create this dataset 
     * @return one of {@code archival},{@code cutout},{@code filtered},{@code mosaic},{@code projection},{@code specialExtraction},{@code catalogExtraction}
     * */
    public final String[] getCreationTypes() {
        return this.creationType;
    }
    /** @exclude */
    public final void setCreationTypes(final String[] creationType) {
        this.creationType = creationType;
    }
    /**
     * The largest search radius, in degrees, that will be
                        accepted by the service without returning an error 
                        condition.
     * @return the maximum search radius
     */
    public final double getMaxSearchRadius() {
        return this.maxSearchRadius;
    }
    /** @exclude */
    public final void setMaxSearchRadius(final double maxSearchRadius) {
        this.maxSearchRadius = maxSearchRadius;
    }
    /** The hard limit on the largest number of records that 
                        the query operation will return in a single response */
    public final int getMaxRecords() {
        return this.maxRecords;
    }
    /** @exclude */    
    public final void setMaxRecords(final int maxRecords) {
        this.maxRecords = maxRecords;
    }
    /**The largest number of records that the service will
                        return when the MAXREC parameter not specified
                        in the query input.*/
    public final int getDefaultMaxRecords() {
        return this.defaultMaxRecords;
    }
    /** @exclude */    
    public final void setDefaultMaxRecords(final int defaultFaxRecords) {
        this.defaultMaxRecords = defaultFaxRecords;
    }
    /**The largest aperture that can be supported upon 
                        request via the APERTURE input parameter by a 
                        service that supports the special extraction 
                        creation method.  */
    public final double getMaxAperture() {
        return this.maxApeture;
    }
    /** @exclude */    
    public final void setMaxAperture(final double maxApeture) {
        this.maxApeture = maxApeture;
    }
    /** An identifier for a world coordinate system 
                        frame supported by this service. */
    public final String[] getSupportedFrames() {
        return this.supportedFrames;
    }
    /** @exclude */    
    public final void setSupportedFrames(final String[] supportedFrames) {
        this.supportedFrames = supportedFrames;
    }

    /** The maximum image file size in bytes.*/
    public final int getMaxFileSize() {
        return this.maxFileSize;
    }
    /** @exclude */    
    public final void setMaxFileSize(final int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
    /** A test query for this service.
     * @return a set of query parameters that is expected to
                        produce at least one matched record */
    public final Query getTestQuery() {
        return this.testQuery;
    }
    /** @exclude */
    public final void setTestQuery(final Query testQuery) {
        this.testQuery = testQuery;
    }
    /** @exclude */
    public final void setComplianceLevel(final String complianceLevel) {
        this.complianceLevel = complianceLevel;
    }
    /** @exclude */
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime
                * result
                + ((this.complianceLevel == null) ? 0 : this.complianceLevel
                        .hashCode());
        result = prime * result + SsapCapability.hashCode(this.creationType);
        result = prime * result + SsapCapability.hashCode(this.dataSource);
        result = prime * result + this.defaultMaxRecords;
        long temp;
        temp = Double.doubleToLongBits(this.maxApeture);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + this.maxFileSize;
        result = prime * result + this.maxRecords;
        temp = Double.doubleToLongBits(this.maxSearchRadius);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + SsapCapability.hashCode(this.supportedFrames);
        result = prime * result
                + ((this.testQuery == null) ? 0 : this.testQuery.hashCode());
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
        final SsapCapability other = (SsapCapability) obj;
        if (this.complianceLevel == null) {
            if (other.complianceLevel != null) {
                return false;
            }
        } else if (!this.complianceLevel.equals(other.complianceLevel)) {
            return false;
        }
        if (!Arrays.equals(this.creationType, other.creationType)) {
            return false;
        }
        if (!Arrays.equals(this.dataSource, other.dataSource)) {
            return false;
        }
        if (this.defaultMaxRecords != other.defaultMaxRecords) {
            return false;
        }
        if (Double.doubleToLongBits(this.maxApeture) != Double
                .doubleToLongBits(other.maxApeture)) {
            return false;
        }
        if (this.maxFileSize != other.maxFileSize) {
            return false;
        }
        if (this.maxRecords != other.maxRecords) {
            return false;
        }
        if (Double.doubleToLongBits(this.maxSearchRadius) != Double
                .doubleToLongBits(other.maxSearchRadius)) {
            return false;
        }
        if (!Arrays.equals(this.supportedFrames, other.supportedFrames)) {
            return false;
        }
        if (this.testQuery == null) {
            if (other.testQuery != null) {
                return false;
            }
        } else if (!this.testQuery.equals(other.testQuery)) {
            return false;
        }
        return true;
    }
}
