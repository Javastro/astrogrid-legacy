/*
 * XML Type:  ArrayOfFromTableType
 * Namespace: urn:astrogrid:schema:ADQL:v2.0
 * Java type: org.astrogrid.adql.beans.ArrayOfFromTableType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans;


/**
 * An XML ArrayOfFromTableType(@urn:astrogrid:schema:ADQL:v2.0).
 *
 * This is a complex type.
 */
public interface ArrayOfFromTableType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)schemaorg_apache_xmlbeans.system.s00D95B496A788C4D65664035BC163E1A.TypeSystemHolder.typeSystem.resolveHandle("arrayoffromtabletype7f88type");
    
    /**
     * Gets array of all "fromTableType" elements
     */
    org.astrogrid.adql.beans.FromTableType[] getFromTableTypeArray();
    
    /**
     * Gets ith "fromTableType" element
     */
    org.astrogrid.adql.beans.FromTableType getFromTableTypeArray(int i);
    
    /**
     * Tests for nil ith "fromTableType" element
     */
    boolean isNilFromTableTypeArray(int i);
    
    /**
     * Returns number of "fromTableType" element
     */
    int sizeOfFromTableTypeArray();
    
    /**
     * Sets array of all "fromTableType" element
     */
    void setFromTableTypeArray(org.astrogrid.adql.beans.FromTableType[] fromTableTypeArray);
    
    /**
     * Sets ith "fromTableType" element
     */
    void setFromTableTypeArray(int i, org.astrogrid.adql.beans.FromTableType fromTableType);
    
    /**
     * Nils the ith "fromTableType" element
     */
    void setNilFromTableTypeArray(int i);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "fromTableType" element
     */
    org.astrogrid.adql.beans.FromTableType insertNewFromTableType(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "fromTableType" element
     */
    org.astrogrid.adql.beans.FromTableType addNewFromTableType();
    
    /**
     * Removes the ith "fromTableType" element
     */
    void removeFromTableType(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.adql.beans.ArrayOfFromTableType newInstance() {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.astrogrid.adql.beans.ArrayOfFromTableType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.beans.ArrayOfFromTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
