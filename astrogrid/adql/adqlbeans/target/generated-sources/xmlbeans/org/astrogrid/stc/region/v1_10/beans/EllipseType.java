/*
 * XML Type:  ellipseType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.EllipseType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans;


/**
 * An XML ellipseType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public interface EllipseType extends org.astrogrid.stc.region.v1_10.beans.CircleType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(EllipseType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("ellipsetype506btype");
    
    /**
     * Gets the "MinorRadius" element
     */
    double getMinorRadius();
    
    /**
     * Gets (as xml) the "MinorRadius" element
     */
    org.apache.xmlbeans.XmlDouble xgetMinorRadius();
    
    /**
     * Sets the "MinorRadius" element
     */
    void setMinorRadius(double minorRadius);
    
    /**
     * Sets (as xml) the "MinorRadius" element
     */
    void xsetMinorRadius(org.apache.xmlbeans.XmlDouble minorRadius);
    
    /**
     * Gets the "PosAngle" element
     */
    double getPosAngle();
    
    /**
     * Gets (as xml) the "PosAngle" element
     */
    org.apache.xmlbeans.XmlDouble xgetPosAngle();
    
    /**
     * Sets the "PosAngle" element
     */
    void setPosAngle(double posAngle);
    
    /**
     * Sets (as xml) the "PosAngle" element
     */
    void xsetPosAngle(org.apache.xmlbeans.XmlDouble posAngle);
    
    /**
     * Gets the "pos_angle_unit" attribute
     */
    org.astrogrid.stc.coords.v1_10.beans.AngleUnitType.Enum getPosAngleUnit();
    
    /**
     * Gets (as xml) the "pos_angle_unit" attribute
     */
    org.astrogrid.stc.coords.v1_10.beans.AngleUnitType xgetPosAngleUnit();
    
    /**
     * True if has "pos_angle_unit" attribute
     */
    boolean isSetPosAngleUnit();
    
    /**
     * Sets the "pos_angle_unit" attribute
     */
    void setPosAngleUnit(org.astrogrid.stc.coords.v1_10.beans.AngleUnitType.Enum posAngleUnit);
    
    /**
     * Sets (as xml) the "pos_angle_unit" attribute
     */
    void xsetPosAngleUnit(org.astrogrid.stc.coords.v1_10.beans.AngleUnitType posAngleUnit);
    
    /**
     * Unsets the "pos_angle_unit" attribute
     */
    void unsetPosAngleUnit();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType newInstance() {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.region.v1_10.beans.EllipseType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.region.v1_10.beans.EllipseType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
