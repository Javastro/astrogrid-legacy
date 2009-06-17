/*
 * An XML document type.
 * Localname: Velocity2D
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans;


/**
 * A document containing one Velocity2D(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public interface Velocity2DDocument extends org.astrogrid.stc.coords.v1_10.beans.VelocityDocument
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Velocity2DDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("velocity2d1baedoctype");
    
    /**
     * Gets the "Velocity2D" element
     */
    org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D getVelocity2D();
    
    /**
     * Sets the "Velocity2D" element
     */
    void setVelocity2D(org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D velocity2D);
    
    /**
     * Appends and returns a new empty "Velocity2D" element
     */
    org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D addNewVelocity2D();
    
    /**
     * An XML Velocity2D(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public interface Velocity2D extends org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Velocity2D.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("velocity2dc763elemtype");
        
        /**
         * Gets the "unit" attribute
         */
        org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum getUnit();
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        org.astrogrid.stc.coords.v1_10.beans.PosUnitType xgetUnit();
        
        /**
         * Sets the "unit" attribute
         */
        void setUnit(org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum unit);
        
        /**
         * Sets (as xml) the "unit" attribute
         */
        void xsetUnit(org.astrogrid.stc.coords.v1_10.beans.PosUnitType unit);
        
        /**
         * Gets the "vel_time_unit" attribute
         */
        org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType.Enum getVelTimeUnit();
        
        /**
         * Gets (as xml) the "vel_time_unit" attribute
         */
        org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType xgetVelTimeUnit();
        
        /**
         * Sets the "vel_time_unit" attribute
         */
        void setVelTimeUnit(org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType.Enum velTimeUnit);
        
        /**
         * Sets (as xml) the "vel_time_unit" attribute
         */
        void xsetVelTimeUnit(org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType velTimeUnit);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D newInstance() {
              return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument newInstance() {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
