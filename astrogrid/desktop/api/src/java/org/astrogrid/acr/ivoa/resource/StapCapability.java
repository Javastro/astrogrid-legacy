/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 24, 20083:14:53 PM
 */
public class StapCapability extends Capability {
/**
     * 
     */
    private static final long serialVersionUID = 6271402987019886252L;

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
public StapCapability() {
    setStandardID(CAPABILITY_ID);
}
    public static final URI CAPABILITY_ID = URI.create("ivo://org.astrogrid/std/STAP/v1.0");
    
    private boolean supportPositioning;
    private String[] supportedFormats;
    private int maxRecords;
    private Query testQuery;
    /**   A query to be sent to the service */
    public static class Query implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 5787312409412686155L;
        private String start;
        private String end;
        private SiapCapability.SkyPos pos;
        private SiapCapability.SkySize size;
        public final String getStart() {
            return this.start;
        }
        public final void setStart(String start) {
            this.start = start;
        }
        public final String getEnd() {
            return this.end;
        }
        public final void setEnd(String end) {
            this.end = end;
        }
        public final SiapCapability.SkyPos getPos() {
            return this.pos;
        }
        public final void setPos(SiapCapability.SkyPos pos) {
            this.pos = pos;
        }
        public final SiapCapability.SkySize getSize() {
            return this.size;
        }
        public final void setSize(SiapCapability.SkySize size) {
            this.size = size;
        }
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((this.end == null) ? 0 : this.end.hashCode());
            result = prime * result
                    + ((this.pos == null) ? 0 : this.pos.hashCode());
            result = prime * result
                    + ((this.size == null) ? 0 : this.size.hashCode());
            result = prime * result
                    + ((this.start == null) ? 0 : this.start.hashCode());
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
            if (this.end == null) {
                if (other.end != null)
                    return false;
            } else if (!this.end.equals(other.end))
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
            if (this.start == null) {
                if (other.start != null)
                    return false;
            } else if (!this.start.equals(other.start))
                return false;
            return true;
        }
        
    }
/** Does the STAP Service handle Positioning parameters pos and size.*/
    public final boolean isSupportPositioning() {
        return this.supportPositioning;
    }

    public final void setSupportPositioning(boolean supportPositioning) {
        this.supportPositioning = supportPositioning;
    }

    public final String[] getSupportedFormats() {
        return this.supportedFormats;
    }

    public final void setSupportedFormats(String[] supportedFormats) {
        this.supportedFormats = supportedFormats;
    }

    /**The largest number of records that the Time Query web
                        method will return. */
    public final int getMaxRecords() {
        return this.maxRecords;
    }

    public final void setMaxRecords(int maxRecords) {
        this.maxRecords = maxRecords;
    }

    /** a set of query parameters that is expected
                        to produce at least one matched record which
                        can be used to test the service.  */
    public final Query getTestQuery() {
        return this.testQuery;
    }

    public final void setTestQuery(Query testQuery) {
        this.testQuery = testQuery;
    }

    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.maxRecords;
        result = prime * result + (this.supportPositioning ? 1231 : 1237);
        result = prime * result
                + StapCapability.hashCode(this.supportedFormats);
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
        final StapCapability other = (StapCapability) obj;
        if (this.maxRecords != other.maxRecords)
            return false;
        if (this.supportPositioning != other.supportPositioning)
            return false;
        if (!Arrays.equals(this.supportedFormats, other.supportedFormats))
            return false;
        if (this.testQuery == null) {
            if (other.testQuery != null)
                return false;
        } else if (!this.testQuery.equals(other.testQuery))
            return false;
        return true;
    }
    
    
}

