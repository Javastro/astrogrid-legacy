/*
 * XML Type:  vector2CoordinateType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans;


/**
 * An XML vector2CoordinateType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public interface Vector2CoordinateType extends org.astrogrid.stc.coords.v1_10.beans.CoordinateType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Vector2CoordinateType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("vector2coordinatetype6d9etype");
    
    /**
     * Gets the "CValue2" element
     */
    org.apache.xmlbeans.XmlObject getCValue2();
    
    /**
     * True if has "CValue2" element
     */
    boolean isSetCValue2();
    
    /**
     * Sets the "CValue2" element
     */
    void setCValue2(org.apache.xmlbeans.XmlObject cValue2);
    
    /**
     * Appends and returns a new empty "CValue2" element
     */
    org.apache.xmlbeans.XmlObject addNewCValue2();
    
    /**
     * Unsets the "CValue2" element
     */
    void unsetCValue2();
    
    /**
     * Gets array of all "CError2" elements
     */
    org.apache.xmlbeans.XmlObject[] getCError2Array();
    
    /**
     * Gets ith "CError2" element
     */
    org.apache.xmlbeans.XmlObject getCError2Array(int i);
    
    /**
     * Returns number of "CError2" element
     */
    int sizeOfCError2Array();
    
    /**
     * Sets array of all "CError2" element
     */
    void setCError2Array(org.apache.xmlbeans.XmlObject[] cError2Array);
    
    /**
     * Sets ith "CError2" element
     */
    void setCError2Array(int i, org.apache.xmlbeans.XmlObject cError2);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CError2" element
     */
    org.apache.xmlbeans.XmlObject insertNewCError2(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CError2" element
     */
    org.apache.xmlbeans.XmlObject addNewCError2();
    
    /**
     * Removes the ith "CError2" element
     */
    void removeCError2(int i);
    
    /**
     * Gets array of all "CResolution2" elements
     */
    org.apache.xmlbeans.XmlObject[] getCResolution2Array();
    
    /**
     * Gets ith "CResolution2" element
     */
    org.apache.xmlbeans.XmlObject getCResolution2Array(int i);
    
    /**
     * Returns number of "CResolution2" element
     */
    int sizeOfCResolution2Array();
    
    /**
     * Sets array of all "CResolution2" element
     */
    void setCResolution2Array(org.apache.xmlbeans.XmlObject[] cResolution2Array);
    
    /**
     * Sets ith "CResolution2" element
     */
    void setCResolution2Array(int i, org.apache.xmlbeans.XmlObject cResolution2);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CResolution2" element
     */
    org.apache.xmlbeans.XmlObject insertNewCResolution2(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CResolution2" element
     */
    org.apache.xmlbeans.XmlObject addNewCResolution2();
    
    /**
     * Removes the ith "CResolution2" element
     */
    void removeCResolution2(int i);
    
    /**
     * Gets array of all "CSize2" elements
     */
    org.apache.xmlbeans.XmlObject[] getCSize2Array();
    
    /**
     * Gets ith "CSize2" element
     */
    org.apache.xmlbeans.XmlObject getCSize2Array(int i);
    
    /**
     * Returns number of "CSize2" element
     */
    int sizeOfCSize2Array();
    
    /**
     * Sets array of all "CSize2" element
     */
    void setCSize2Array(org.apache.xmlbeans.XmlObject[] cSize2Array);
    
    /**
     * Sets ith "CSize2" element
     */
    void setCSize2Array(int i, org.apache.xmlbeans.XmlObject cSize2);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CSize2" element
     */
    org.apache.xmlbeans.XmlObject insertNewCSize2(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CSize2" element
     */
    org.apache.xmlbeans.XmlObject addNewCSize2();
    
    /**
     * Removes the ith "CSize2" element
     */
    void removeCSize2(int i);
    
    /**
     * Gets array of all "CPixSize2" elements
     */
    org.apache.xmlbeans.XmlObject[] getCPixSize2Array();
    
    /**
     * Gets ith "CPixSize2" element
     */
    org.apache.xmlbeans.XmlObject getCPixSize2Array(int i);
    
    /**
     * Returns number of "CPixSize2" element
     */
    int sizeOfCPixSize2Array();
    
    /**
     * Sets array of all "CPixSize2" element
     */
    void setCPixSize2Array(org.apache.xmlbeans.XmlObject[] cPixSize2Array);
    
    /**
     * Sets ith "CPixSize2" element
     */
    void setCPixSize2Array(int i, org.apache.xmlbeans.XmlObject cPixSize2);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CPixSize2" element
     */
    org.apache.xmlbeans.XmlObject insertNewCPixSize2(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CPixSize2" element
     */
    org.apache.xmlbeans.XmlObject addNewCPixSize2();
    
    /**
     * Removes the ith "CPixSize2" element
     */
    void removeCPixSize2(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType newInstance() {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
