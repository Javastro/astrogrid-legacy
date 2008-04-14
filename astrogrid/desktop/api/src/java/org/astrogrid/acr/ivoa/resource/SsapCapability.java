/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;

/** Capability for Simple Spectral Access Service
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 20, 200812:05:09 PM
 */
public class SsapCapability extends Capability {


    /**
     * 
     */
    private static final long serialVersionUID = -6409405290279095376L;

    private static int hashCode(Object[] array) {
        final int prime = 31;
        if (array == null)
            return 0;
        int result = 1;
        for (int index = 0; index < array.length; index++) {
            result = prime * result
                    + (array[index] == null ? 0 : array[index].hashCode());
        }
        return result;
    }
    /**
     * 
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
            service instance complies with the SSA standard
            @return 'minimal','query','full' */
    public String getComplianceLevel() {
        return complianceLevel;
    }
    /** The defined categories that specify where the spectral data 
           originally came from. 
           @return 'survey','pointed','custom','theory','artificial' */
    public String[] getDataSources() {
        return dataSource;
    }
    
    /** a query to be sent to the service */
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

        /**the center position the search cone given in
                   decimal degrees.  */
        public final PosParam getPos() {
            return this.pos;
        }
        public final void setPos(PosParam pos) {
            this.pos = pos;
        }
        /**The diameter of the search region specified in decimal
          degrees..*/
        public final double getSize() {
            return this.size;
        }
        public final void setSize(double size) {
            this.size = size;
        }
        /**Fully specified queryData test query formatted as an URL
           argument list in the syntax specified by the SSA standard.
                   The list must exclude the REQUEST argument which is 
                   assumed to be set to "queryData".  VERSION may be
           included if the test query applies to a specific version
           of the service protocol.*/
        public final String getQueryDataCmd() {
            return this.queryDataCmd;
        }
        public final void setQueryDataCmd(String queryDataCmd) {
            this.queryDataCmd = queryDataCmd;
        }
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
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Query other = (Query) obj;
            if (this.pos == null) {
                if (other.pos != null)
                    return false;
            } else if (!this.pos.equals(other.pos))
                return false;
            if (this.queryDataCmd == null) {
                if (other.queryDataCmd != null)
                    return false;
            } else if (!this.queryDataCmd.equals(other.queryDataCmd))
                return false;
            if (Double.doubleToLongBits(this.size) != Double
                    .doubleToLongBits(other.size))
                return false;
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
        /**The longitude (e.g. Right Ascension) of the center of the
                  search position in decimal degrees.*/
        public final double getLong() {
            return this.longitude;
        }
        public final void setLong(double longitude) {
            this.longitude = longitude;
        }
        /**   The latitude (e.g. Declination) of the center of the
                  search position in decimal degrees.*/
        public final double getLat() {
            return this.lat;
        }
        public final void setLat(double lat) {
            this.lat = lat;
        }
        /** the coordinate system reference frame name indicating
                  the frame to assume for the given position.   If not 
                  provided, ICRS is assumed.*/
        public final String getRefframe() {
            return this.refframe;
        }
        public final void setRefframe(String refframe) {
            this.refframe = refframe;
        }
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
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final PosParam other = (PosParam) obj;
            if (Double.doubleToLongBits(this.lat) != Double
                    .doubleToLongBits(other.lat))
                return false;
            if (Double.doubleToLongBits(this.longitude) != Double
                    .doubleToLongBits(other.longitude))
                return false;
            if (this.refframe == null) {
                if (other.refframe != null)
                    return false;
            } else if (!this.refframe.equals(other.refframe))
                return false;
            return true;
        }
    }
    public final void setDataSources(String[] dataSource) {
        this.dataSource = dataSource;
    }
    /** the process used to create this dataset 
     * @return 'archival','cutout','filtered','mosaic','projection','specialExtraction','catalogExtraction'
     * */
    public final String[] getCreationTypes() {
        return this.creationType;
    }
    public final void setCreationTypes(String[] creationType) {
        this.creationType = creationType;
    }
    /**
     * The largest search radius, in degrees, that will be
                        accepted by the service without returning an error 
                        condition.
     * @return
     */
    public final double getMaxSearchRadius() {
        return this.maxSearchRadius;
    }
    public final void setMaxSearchRadius(double maxSearchRadius) {
        this.maxSearchRadius = maxSearchRadius;
    }
    /** The hard limit on the largest number of records that 
                        the query operation will return in a single response */
    public final int getMaxRecords() {
        return this.maxRecords;
    }
    public final void setMaxRecords(int maxRecords) {
        this.maxRecords = maxRecords;
    }
    /**The largest number of records that the service will
                        return when the MAXREC parameter not specified
                        in the query input.*/
    public final int getDefaultMaxRecords() {
        return this.defaultMaxRecords;
    }
    public final void setDefaultMaxRecords(int defaultFaxRecords) {
        this.defaultMaxRecords = defaultFaxRecords;
    }
    /**The largest aperture that can be supported upon 
                        request via the APERTURE input parameter by a 
                        service that supports the special extraction 
                        creation method.  */
    public final double getMaxAperture() {
        return this.maxApeture;
    }
    public final void setMaxAperture(double maxApeture) {
        this.maxApeture = maxApeture;
    }
    /** An identifier for a world coordinate system 
                        frame supported by this service. */
    public final String[] getSupportedFrames() {
        return this.supportedFrames;
    }
    public final void setSupportedFrames(String[] supportedFrames) {
        this.supportedFrames = supportedFrames;
    }

    /** The maximum image file size in bytes.*/
    public final int getMaxFileSize() {
        return this.maxFileSize;
    }
    public final void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
    /** a set of query parameters that is expected to
                        produce at least one matched record which can be
                        used to test the service.*/
    public final Query getTestQuery() {
        return this.testQuery;
    }
    public final void setTestQuery(Query testQuery) {
        this.testQuery = testQuery;
    }
    public final void setComplianceLevel(String complianceLevel) {
        this.complianceLevel = complianceLevel;
    }
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SsapCapability other = (SsapCapability) obj;
        if (this.complianceLevel == null) {
            if (other.complianceLevel != null)
                return false;
        } else if (!this.complianceLevel.equals(other.complianceLevel))
            return false;
        if (!Arrays.equals(this.creationType, other.creationType))
            return false;
        if (!Arrays.equals(this.dataSource, other.dataSource))
            return false;
        if (this.defaultMaxRecords != other.defaultMaxRecords)
            return false;
        if (Double.doubleToLongBits(this.maxApeture) != Double
                .doubleToLongBits(other.maxApeture))
            return false;
        if (this.maxFileSize != other.maxFileSize)
            return false;
        if (this.maxRecords != other.maxRecords)
            return false;
        if (Double.doubleToLongBits(this.maxSearchRadius) != Double
                .doubleToLongBits(other.maxSearchRadius))
            return false;
        if (!Arrays.equals(this.supportedFrames, other.supportedFrames))
            return false;
        if (this.testQuery == null) {
            if (other.testQuery != null)
                return false;
        } else if (!this.testQuery.equals(other.testQuery))
            return false;
        return true;
    }
}
