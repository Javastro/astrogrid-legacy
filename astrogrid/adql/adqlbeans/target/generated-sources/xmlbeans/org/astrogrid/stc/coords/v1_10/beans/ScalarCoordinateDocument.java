/*
 * An XML document type.
 * Localname: ScalarCoordinate
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans;


/**
 * A document containing one ScalarCoordinate(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public interface ScalarCoordinateDocument extends org.astrogrid.stc.coords.v1_10.beans.CoordinateDocument
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ScalarCoordinateDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("scalarcoordinate4403doctype");
    
    /**
     * Gets the "ScalarCoordinate" element
     */
    org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate getScalarCoordinate();
    
    /**
     * Sets the "ScalarCoordinate" element
     */
    void setScalarCoordinate(org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate scalarCoordinate);
    
    /**
     * Appends and returns a new empty "ScalarCoordinate" element
     */
    org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate addNewScalarCoordinate();
    
    /**
     * An XML ScalarCoordinate(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public interface ScalarCoordinate extends org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ScalarCoordinate.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("scalarcoordinatec4c3elemtype");
        
        /**
         * Gets the "unit" attribute
         */
        java.lang.String getUnit();
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        org.apache.xmlbeans.XmlString xgetUnit();
        
        /**
         * True if has "unit" attribute
         */
        boolean isSetUnit();
        
        /**
         * Sets the "unit" attribute
         */
        void setUnit(java.lang.String unit);
        
        /**
         * Sets (as xml) the "unit" attribute
         */
        void xsetUnit(org.apache.xmlbeans.XmlString unit);
        
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
            public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate newInstance() {
              return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument newInstance() {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
