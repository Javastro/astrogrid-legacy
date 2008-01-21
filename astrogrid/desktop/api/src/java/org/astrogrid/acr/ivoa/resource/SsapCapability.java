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
    public static final URI CAPABILITY_ID = URI.create("ivo://ivoa.net/std/SSA");
    private static final long serialVersionUID = -8906326302739355714L;
    private String complianceLevel;
    private String[] dataSource;
    private String[] creationType;
    private double maxSearchRadius;
    private int maxRecords;
    private int defaultFaxRecords;
    private double maxApeture;
    private URI[] supportedFrames;
    private String[] supports;
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
        private static final long serialVersionUID = -7840524860959751650L;
        private PosParam pos;
        private double size;
        private BandParam band;
        private TimeParam time;
        private String extras;
        /**the center position the search cone given in
                   decimal degrees.  */
        public final PosParam getPos() {
            return this.pos;
        }
        public final void setPos(PosParam pos) {
            this.pos = pos;
        }
        /** the size of the search radius.*/
        public final double getSize() {
            return this.size;
        }
        public final void setSize(double size) {
            this.size = size;
        }
        /**the spectal band to search, given in the format supported
                  by the BAND input parameter.*/
        public final BandParam getBand() {
            return this.band;
        }
        public final void setBand(BandParam band) {
            this.band = band;
        }
        /**the size of the search radius.*/
        public final TimeParam getTime() {
            return this.time;
        }
        public final void setTime(TimeParam time) {
            this.time = time;
        }
        /**any extra (particularly non-standard) parameters that must 
                   be provided (apart from what is part of base URL given by 
                   the accessURL element).*/
        public final String getExtras() {
            return this.extras;
        }
        public final void setExtras(String extras) {
            this.extras = extras;
        }
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((this.band == null) ? 0 : this.band.hashCode());
            result = prime * result
                    + ((this.extras == null) ? 0 : this.extras.hashCode());
            result = prime * result
                    + ((this.pos == null) ? 0 : this.pos.hashCode());
            long temp;
            temp = Double.doubleToLongBits(this.size);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result
                    + ((this.time == null) ? 0 : this.time.hashCode());
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
            if (this.band == null) {
                if (other.band != null)
                    return false;
            } else if (!this.band.equals(other.band))
                return false;
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
            if (Double.doubleToLongBits(this.size) != Double
                    .doubleToLongBits(other.size))
                return false;
            if (this.time == null) {
                if (other.time != null)
                    return false;
            } else if (!this.time.equals(other.time))
                return false;
            return true;
        }
        
    }

    /** a position in the sky to search.*/
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
    /**  a bandpass or an approximate range of frequency to search.*/
    public static class BandParam implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 6440401321376569939L;
        private double min;
        private double max;
        private String restFrame;
        private String name;
        /**The minimum wavelength in the bandpass to search.  If not 
                  provided, all frequencies less than maxWavelength will be 
                  searched.*/
        public final double getMin() {
            return this.min;
        }
        public final void setMin(double min) {
            this.min = min;
        }
        /**The minimum wavelength in the bandpass to search.  If not 
                  provided, all frequencies less than maxWavelength will be 
                  searched.*/
        public final double getMax() {
            return this.max;
        }
        public final void setMax(double max) {
            this.max = max;
        }
        /**  the spectral rest frame to assume while searching.
         * 
         * @return 'source','observer'
         */
        public final String getRestFrame() {
            return this.restFrame;
        }
        public final void setRestFrame(String restFrame) {
            this.restFrame = restFrame;
        }
        /**  the name of a supported bandpass to search.  */
        public final String getName() {
            return this.name;
        }
        public final void setName(String name) {
            this.name = name;
        }
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            long temp;
            temp = Double.doubleToLongBits(this.max);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(this.min);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result
                    + ((this.name == null) ? 0 : this.name.hashCode());
            result = prime
                    * result
                    + ((this.restFrame == null) ? 0 : this.restFrame.hashCode());
            return result;
        }
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final BandParam other = (BandParam) obj;
            if (Double.doubleToLongBits(this.max) != Double
                    .doubleToLongBits(other.max))
                return false;
            if (Double.doubleToLongBits(this.min) != Double
                    .doubleToLongBits(other.min))
                return false;
            if (this.name == null) {
                if (other.name != null)
                    return false;
            } else if (!this.name.equals(other.name))
                return false;
            if (this.restFrame == null) {
                if (other.restFrame != null)
                    return false;
            } else if (!this.restFrame.equals(other.restFrame))
                return false;
            return true;
        }
    }
    
    /**A range of time values that can be formed into a legal value
            for the TIME input parameter.*/
    public static class TimeParam implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -1158497142662748709L;
        private String earliest;
        private String latest;
        /**The earliest date and time (inclusive) to search.  If 
                  not provided,no lower limit should be applied.
                 @return The value may either be just a date or a full ISO
                    8601, compliant string (using the "T" data-time
                    delimiter with no timezone specifier). 
                  */
        public final String getEarliest() {
            return this.earliest;
        }
        public final void setEarliest(String earliest) {
            this.earliest = earliest;
        }
        /**The latest date and time (inclusive) to search.  If 
                  not provided, no lower limit should be applied
                  @return The value may either be just a date or a full ISO
                    8601, compliant string (using the "T" data-time
                    delimiter with no timezone specifier)..*/
        public final String getLatest() {
            return this.latest;
        }
        public final void setLatest(String latest) {
            this.latest = latest;
        }
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((this.earliest == null) ? 0 : this.earliest.hashCode());
            result = prime * result
                    + ((this.latest == null) ? 0 : this.latest.hashCode());
            return result;
        }
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final TimeParam other = (TimeParam) obj;
            if (this.earliest == null) {
                if (other.earliest != null)
                    return false;
            } else if (!this.earliest.equals(other.earliest))
                return false;
            if (this.latest == null) {
                if (other.latest != null)
                    return false;
            } else if (!this.latest.equals(other.latest))
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
        return this.defaultFaxRecords;
    }
    public final void setDefaultMaxRecords(int defaultFaxRecords) {
        this.defaultFaxRecords = defaultFaxRecords;
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
    /** A URI identifier for a world coordinate system 
                        frame supported by this service. */
    public final URI[] getSupportedFrames() {
        return this.supportedFrames;
    }
    public final void setSupportedFrames(URI[] supportedFrames) {
        this.supportedFrames = supportedFrames;
    }
    /**lists optional named features that that the
                        server supports. The vocabulary of the content*/
    public final String[] getSupports() {
        return this.supports;
    }
    public final void setSupports(String[] supports) {
        this.supports = supports;
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
        result = prime * result + this.defaultFaxRecords;
        long temp;
        temp = Double.doubleToLongBits(this.maxApeture);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + this.maxFileSize;
        result = prime * result + this.maxRecords;
        temp = Double.doubleToLongBits(this.maxSearchRadius);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + SsapCapability.hashCode(this.supportedFrames);
        result = prime * result + SsapCapability.hashCode(this.supports);
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
        if (this.defaultFaxRecords != other.defaultFaxRecords)
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
        if (!Arrays.equals(this.supports, other.supports))
            return false;
        if (this.testQuery == null) {
            if (other.testQuery != null)
                return false;
        } else if (!this.testQuery.equals(other.testQuery))
            return false;
        return true;
    }
}
