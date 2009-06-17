/*
 * XML Type:  astroCoordsType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans;


/**
 * An XML astroCoordsType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public interface AstroCoordsType extends org.astrogrid.stc.coords.v1_10.beans.CoordsType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AstroCoordsType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("astrocoordstype51f4type");
    
    /**
     * Gets the "Time" element
     */
    org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType getTime();
    
    /**
     * True if has "Time" element
     */
    boolean isSetTime();
    
    /**
     * Sets the "Time" element
     */
    void setTime(org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType time);
    
    /**
     * Appends and returns a new empty "Time" element
     */
    org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType addNewTime();
    
    /**
     * Unsets the "Time" element
     */
    void unsetTime();
    
    /**
     * Gets the "Position" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordinateType getPosition();
    
    /**
     * True if has "Position" element
     */
    boolean isSetPosition();
    
    /**
     * Sets the "Position" element
     */
    void setPosition(org.astrogrid.stc.coords.v1_10.beans.CoordinateType position);
    
    /**
     * Appends and returns a new empty "Position" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordinateType addNewPosition();
    
    /**
     * Unsets the "Position" element
     */
    void unsetPosition();
    
    /**
     * Gets the "Velocity" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordinateType getVelocity();
    
    /**
     * True if has "Velocity" element
     */
    boolean isSetVelocity();
    
    /**
     * Sets the "Velocity" element
     */
    void setVelocity(org.astrogrid.stc.coords.v1_10.beans.CoordinateType velocity);
    
    /**
     * Appends and returns a new empty "Velocity" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordinateType addNewVelocity();
    
    /**
     * Unsets the "Velocity" element
     */
    void unsetVelocity();
    
    /**
     * Gets the "Spectral" element
     */
    org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral getSpectral();
    
    /**
     * True if has "Spectral" element
     */
    boolean isSetSpectral();
    
    /**
     * Sets the "Spectral" element
     */
    void setSpectral(org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral spectral);
    
    /**
     * Appends and returns a new empty "Spectral" element
     */
    org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral addNewSpectral();
    
    /**
     * Unsets the "Spectral" element
     */
    void unsetSpectral();
    
    /**
     * Gets the "Redshift" element
     */
    org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift getRedshift();
    
    /**
     * True if has "Redshift" element
     */
    boolean isSetRedshift();
    
    /**
     * Sets the "Redshift" element
     */
    void setRedshift(org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift redshift);
    
    /**
     * Appends and returns a new empty "Redshift" element
     */
    org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift addNewRedshift();
    
    /**
     * Unsets the "Redshift" element
     */
    void unsetRedshift();
    
    /**
     * Gets the "CoordFile" element
     */
    org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType getCoordFile();
    
    /**
     * True if has "CoordFile" element
     */
    boolean isSetCoordFile();
    
    /**
     * Sets the "CoordFile" element
     */
    void setCoordFile(org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType coordFile);
    
    /**
     * Appends and returns a new empty "CoordFile" element
     */
    org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType addNewCoordFile();
    
    /**
     * Unsets the "CoordFile" element
     */
    void unsetCoordFile();
    
    /**
     * An XML Spectral(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public interface Spectral extends org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Spectral.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("spectralc414elemtype");
        
        /**
         * Gets the "unit" attribute
         */
        org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType.Enum getUnit();
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType xgetUnit();
        
        /**
         * True if has "unit" attribute
         */
        boolean isSetUnit();
        
        /**
         * Sets the "unit" attribute
         */
        void setUnit(org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType.Enum unit);
        
        /**
         * Sets (as xml) the "unit" attribute
         */
        void xsetUnit(org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType unit);
        
        /**
         * Unsets the "unit" attribute
         */
        void unsetUnit();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral newInstance() {
              return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML Redshift(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public interface Redshift extends org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Redshift.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("redshift9481elemtype");
        
        /**
         * Gets the "unit" attribute
         */
        org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum getUnit();
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        org.astrogrid.stc.coords.v1_10.beans.PosUnitType xgetUnit();
        
        /**
         * True if has "unit" attribute
         */
        boolean isSetUnit();
        
        /**
         * Sets the "unit" attribute
         */
        void setUnit(org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum unit);
        
        /**
         * Sets (as xml) the "unit" attribute
         */
        void xsetUnit(org.astrogrid.stc.coords.v1_10.beans.PosUnitType unit);
        
        /**
         * Unsets the "unit" attribute
         */
        void unsetUnit();
        
        /**
         * Gets the "vel_time_unit" attribute
         */
        org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType.Enum getVelTimeUnit();
        
        /**
         * Gets (as xml) the "vel_time_unit" attribute
         */
        org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType xgetVelTimeUnit();
        
        /**
         * True if has "vel_time_unit" attribute
         */
        boolean isSetVelTimeUnit();
        
        /**
         * Sets the "vel_time_unit" attribute
         */
        void setVelTimeUnit(org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType.Enum velTimeUnit);
        
        /**
         * Sets (as xml) the "vel_time_unit" attribute
         */
        void xsetVelTimeUnit(org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType velTimeUnit);
        
        /**
         * Unsets the "vel_time_unit" attribute
         */
        void unsetVelTimeUnit();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift newInstance() {
              return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType newInstance() {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
