package org.astrogrid.desktop.modules.ui.scope;

import org.xml.sax.SAXException;

/** An exception produced from a DalProtocol */
public class DalProtocolException extends SAXException {

    /**
     * @param message
     */
    public DalProtocolException(final String message) {
        super(message);
    }
    
    
    /** subclass representing the 'ERROR' parameter being encountered */
    public static class ERROR extends DalProtocolException {

        /**
         * @param message
         */
        public ERROR(final String message) {
            super(message);
        }
        
    }
    /** subclass representing the 'QURY_STATUS' parameter != 'OK' */
    public static class QUERY_STATUS extends DalProtocolException {

        /**
         * @param message
         */
        public QUERY_STATUS(final String message) {
            super(message);
        }
    }
    

    /** Exception thrown when response table returns insufficient metadata.
     */
    public abstract static class InsufficientMetadata extends DalProtocolException {

        /**
         * @param message
         */
        public InsufficientMetadata(final String message) {
            super("Response from service lacks metadata: " + message);
        }

    }
    /** RA column cannot be deduced */
    public static class RA_UNDETECTED extends InsufficientMetadata {

        /**
         * @param message
         */
        public RA_UNDETECTED() {
            super("unable to detect RA column - no column is marked with UCD POS_EQ_RA_MAIN");
        }
    }
    
    /** Declination column cannot be deduced */
    public static class Dec_UNDETECTED extends InsufficientMetadata {

        /**
         * @param message
         */
        public Dec_UNDETECTED() {
            super("unable to detected Dec column  - no column is marked with UCD POS_EQ_DEC_MAIN");
        }
    }
    
    /** access ref column cannot be deduced */
    public static class AccessReference_UNDETECTED extends InsufficientMetadata {

        /**
         * @param message
         */
        public AccessReference_UNDETECTED() {
            super("unable to detect Access Reference column");
        }
    }
    
}