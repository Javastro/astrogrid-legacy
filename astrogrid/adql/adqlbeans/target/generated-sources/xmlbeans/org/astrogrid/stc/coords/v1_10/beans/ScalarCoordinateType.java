/*
 * XML Type:  scalarCoordinateType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans;


/**
 * An XML scalarCoordinateType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public interface ScalarCoordinateType extends org.astrogrid.stc.coords.v1_10.beans.CoordinateType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ScalarCoordinateType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("scalarcoordinatetype0fadtype");
    
    /**
     * Gets the "CValue" element
     */
    org.apache.xmlbeans.XmlObject getCValue();
    
    /**
     * True if has "CValue" element
     */
    boolean isSetCValue();
    
    /**
     * Sets the "CValue" element
     */
    void setCValue(org.apache.xmlbeans.XmlObject cValue);
    
    /**
     * Appends and returns a new empty "CValue" element
     */
    org.apache.xmlbeans.XmlObject addNewCValue();
    
    /**
     * Unsets the "CValue" element
     */
    void unsetCValue();
    
    /**
     * Gets array of all "CError" elements
     */
    org.apache.xmlbeans.XmlObject[] getCErrorArray();
    
    /**
     * Gets ith "CError" element
     */
    org.apache.xmlbeans.XmlObject getCErrorArray(int i);
    
    /**
     * Returns number of "CError" element
     */
    int sizeOfCErrorArray();
    
    /**
     * Sets array of all "CError" element
     */
    void setCErrorArray(org.apache.xmlbeans.XmlObject[] cErrorArray);
    
    /**
     * Sets ith "CError" element
     */
    void setCErrorArray(int i, org.apache.xmlbeans.XmlObject cError);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CError" element
     */
    org.apache.xmlbeans.XmlObject insertNewCError(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CError" element
     */
    org.apache.xmlbeans.XmlObject addNewCError();
    
    /**
     * Removes the ith "CError" element
     */
    void removeCError(int i);
    
    /**
     * Gets array of all "CResolution" elements
     */
    org.apache.xmlbeans.XmlObject[] getCResolutionArray();
    
    /**
     * Gets ith "CResolution" element
     */
    org.apache.xmlbeans.XmlObject getCResolutionArray(int i);
    
    /**
     * Returns number of "CResolution" element
     */
    int sizeOfCResolutionArray();
    
    /**
     * Sets array of all "CResolution" element
     */
    void setCResolutionArray(org.apache.xmlbeans.XmlObject[] cResolutionArray);
    
    /**
     * Sets ith "CResolution" element
     */
    void setCResolutionArray(int i, org.apache.xmlbeans.XmlObject cResolution);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CResolution" element
     */
    org.apache.xmlbeans.XmlObject insertNewCResolution(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CResolution" element
     */
    org.apache.xmlbeans.XmlObject addNewCResolution();
    
    /**
     * Removes the ith "CResolution" element
     */
    void removeCResolution(int i);
    
    /**
     * Gets array of all "CSize" elements
     */
    org.apache.xmlbeans.XmlObject[] getCSizeArray();
    
    /**
     * Gets ith "CSize" element
     */
    org.apache.xmlbeans.XmlObject getCSizeArray(int i);
    
    /**
     * Returns number of "CSize" element
     */
    int sizeOfCSizeArray();
    
    /**
     * Sets array of all "CSize" element
     */
    void setCSizeArray(org.apache.xmlbeans.XmlObject[] cSizeArray);
    
    /**
     * Sets ith "CSize" element
     */
    void setCSizeArray(int i, org.apache.xmlbeans.XmlObject cSize);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CSize" element
     */
    org.apache.xmlbeans.XmlObject insertNewCSize(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CSize" element
     */
    org.apache.xmlbeans.XmlObject addNewCSize();
    
    /**
     * Removes the ith "CSize" element
     */
    void removeCSize(int i);
    
    /**
     * Gets array of all "CPixSize" elements
     */
    org.apache.xmlbeans.XmlObject[] getCPixSizeArray();
    
    /**
     * Gets ith "CPixSize" element
     */
    org.apache.xmlbeans.XmlObject getCPixSizeArray(int i);
    
    /**
     * Returns number of "CPixSize" element
     */
    int sizeOfCPixSizeArray();
    
    /**
     * Sets array of all "CPixSize" element
     */
    void setCPixSizeArray(org.apache.xmlbeans.XmlObject[] cPixSizeArray);
    
    /**
     * Sets ith "CPixSize" element
     */
    void setCPixSizeArray(int i, org.apache.xmlbeans.XmlObject cPixSize);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CPixSize" element
     */
    org.apache.xmlbeans.XmlObject insertNewCPixSize(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CPixSize" element
     */
    org.apache.xmlbeans.XmlObject addNewCPixSize();
    
    /**
     * Removes the ith "CPixSize" element
     */
    void removeCPixSize(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType newInstance() {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
