/*
 * XML Type:  vector3CoordinateType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans;


/**
 * An XML vector3CoordinateType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public interface Vector3CoordinateType extends org.astrogrid.stc.coords.v1_10.beans.CoordinateType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Vector3CoordinateType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("vector3coordinatetype3a5ftype");
    
    /**
     * Gets the "CValue3" element
     */
    org.apache.xmlbeans.XmlObject getCValue3();
    
    /**
     * True if has "CValue3" element
     */
    boolean isSetCValue3();
    
    /**
     * Sets the "CValue3" element
     */
    void setCValue3(org.apache.xmlbeans.XmlObject cValue3);
    
    /**
     * Appends and returns a new empty "CValue3" element
     */
    org.apache.xmlbeans.XmlObject addNewCValue3();
    
    /**
     * Unsets the "CValue3" element
     */
    void unsetCValue3();
    
    /**
     * Gets array of all "CError3" elements
     */
    org.apache.xmlbeans.XmlObject[] getCError3Array();
    
    /**
     * Gets ith "CError3" element
     */
    org.apache.xmlbeans.XmlObject getCError3Array(int i);
    
    /**
     * Returns number of "CError3" element
     */
    int sizeOfCError3Array();
    
    /**
     * Sets array of all "CError3" element
     */
    void setCError3Array(org.apache.xmlbeans.XmlObject[] cError3Array);
    
    /**
     * Sets ith "CError3" element
     */
    void setCError3Array(int i, org.apache.xmlbeans.XmlObject cError3);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CError3" element
     */
    org.apache.xmlbeans.XmlObject insertNewCError3(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CError3" element
     */
    org.apache.xmlbeans.XmlObject addNewCError3();
    
    /**
     * Removes the ith "CError3" element
     */
    void removeCError3(int i);
    
    /**
     * Gets array of all "CResolution3" elements
     */
    org.apache.xmlbeans.XmlObject[] getCResolution3Array();
    
    /**
     * Gets ith "CResolution3" element
     */
    org.apache.xmlbeans.XmlObject getCResolution3Array(int i);
    
    /**
     * Returns number of "CResolution3" element
     */
    int sizeOfCResolution3Array();
    
    /**
     * Sets array of all "CResolution3" element
     */
    void setCResolution3Array(org.apache.xmlbeans.XmlObject[] cResolution3Array);
    
    /**
     * Sets ith "CResolution3" element
     */
    void setCResolution3Array(int i, org.apache.xmlbeans.XmlObject cResolution3);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CResolution3" element
     */
    org.apache.xmlbeans.XmlObject insertNewCResolution3(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CResolution3" element
     */
    org.apache.xmlbeans.XmlObject addNewCResolution3();
    
    /**
     * Removes the ith "CResolution3" element
     */
    void removeCResolution3(int i);
    
    /**
     * Gets array of all "CSize3" elements
     */
    org.apache.xmlbeans.XmlObject[] getCSize3Array();
    
    /**
     * Gets ith "CSize3" element
     */
    org.apache.xmlbeans.XmlObject getCSize3Array(int i);
    
    /**
     * Returns number of "CSize3" element
     */
    int sizeOfCSize3Array();
    
    /**
     * Sets array of all "CSize3" element
     */
    void setCSize3Array(org.apache.xmlbeans.XmlObject[] cSize3Array);
    
    /**
     * Sets ith "CSize3" element
     */
    void setCSize3Array(int i, org.apache.xmlbeans.XmlObject cSize3);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CSize3" element
     */
    org.apache.xmlbeans.XmlObject insertNewCSize3(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CSize3" element
     */
    org.apache.xmlbeans.XmlObject addNewCSize3();
    
    /**
     * Removes the ith "CSize3" element
     */
    void removeCSize3(int i);
    
    /**
     * Gets array of all "CPixSize3" elements
     */
    org.apache.xmlbeans.XmlObject[] getCPixSize3Array();
    
    /**
     * Gets ith "CPixSize3" element
     */
    org.apache.xmlbeans.XmlObject getCPixSize3Array(int i);
    
    /**
     * Returns number of "CPixSize3" element
     */
    int sizeOfCPixSize3Array();
    
    /**
     * Sets array of all "CPixSize3" element
     */
    void setCPixSize3Array(org.apache.xmlbeans.XmlObject[] cPixSize3Array);
    
    /**
     * Sets ith "CPixSize3" element
     */
    void setCPixSize3Array(int i, org.apache.xmlbeans.XmlObject cPixSize3);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CPixSize3" element
     */
    org.apache.xmlbeans.XmlObject insertNewCPixSize3(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CPixSize3" element
     */
    org.apache.xmlbeans.XmlObject addNewCPixSize3();
    
    /**
     * Removes the ith "CPixSize3" element
     */
    void removeCPixSize3(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType newInstance() {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
