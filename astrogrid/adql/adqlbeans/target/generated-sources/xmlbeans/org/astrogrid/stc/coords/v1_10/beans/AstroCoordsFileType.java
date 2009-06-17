/*
 * XML Type:  astroCoordsFileType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans;


/**
 * An XML astroCoordsFileType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public interface AstroCoordsFileType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AstroCoordsFileType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("astrocoordsfiletyped990type");
    
    /**
     * Gets the "FITSFile" element
     */
    org.astrogrid.stc.coords.v1_10.beans.FitsType getFITSFile();
    
    /**
     * Sets the "FITSFile" element
     */
    void setFITSFile(org.astrogrid.stc.coords.v1_10.beans.FitsType fitsFile);
    
    /**
     * Appends and returns a new empty "FITSFile" element
     */
    org.astrogrid.stc.coords.v1_10.beans.FitsType addNewFITSFile();
    
    /**
     * Gets the "FITSTime" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType getFITSTime();
    
    /**
     * True if has "FITSTime" element
     */
    boolean isSetFITSTime();
    
    /**
     * Sets the "FITSTime" element
     */
    void setFITSTime(org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType fitsTime);
    
    /**
     * Appends and returns a new empty "FITSTime" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType addNewFITSTime();
    
    /**
     * Unsets the "FITSTime" element
     */
    void unsetFITSTime();
    
    /**
     * Gets the "FITSPosition" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType getFITSPosition();
    
    /**
     * True if has "FITSPosition" element
     */
    boolean isSetFITSPosition();
    
    /**
     * Sets the "FITSPosition" element
     */
    void setFITSPosition(org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType fitsPosition);
    
    /**
     * Appends and returns a new empty "FITSPosition" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType addNewFITSPosition();
    
    /**
     * Unsets the "FITSPosition" element
     */
    void unsetFITSPosition();
    
    /**
     * Gets the "FITSVelocity" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType getFITSVelocity();
    
    /**
     * True if has "FITSVelocity" element
     */
    boolean isSetFITSVelocity();
    
    /**
     * Sets the "FITSVelocity" element
     */
    void setFITSVelocity(org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType fitsVelocity);
    
    /**
     * Appends and returns a new empty "FITSVelocity" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType addNewFITSVelocity();
    
    /**
     * Unsets the "FITSVelocity" element
     */
    void unsetFITSVelocity();
    
    /**
     * Gets the "FITSSpectral" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType getFITSSpectral();
    
    /**
     * True if has "FITSSpectral" element
     */
    boolean isSetFITSSpectral();
    
    /**
     * Sets the "FITSSpectral" element
     */
    void setFITSSpectral(org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType fitsSpectral);
    
    /**
     * Appends and returns a new empty "FITSSpectral" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType addNewFITSSpectral();
    
    /**
     * Unsets the "FITSSpectral" element
     */
    void unsetFITSSpectral();
    
    /**
     * Gets the "FITSRedshift" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType getFITSRedshift();
    
    /**
     * True if has "FITSRedshift" element
     */
    boolean isSetFITSRedshift();
    
    /**
     * Sets the "FITSRedshift" element
     */
    void setFITSRedshift(org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType fitsRedshift);
    
    /**
     * Appends and returns a new empty "FITSRedshift" element
     */
    org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType addNewFITSRedshift();
    
    /**
     * Unsets the "FITSRedshift" element
     */
    void unsetFITSRedshift();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType newInstance() {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
