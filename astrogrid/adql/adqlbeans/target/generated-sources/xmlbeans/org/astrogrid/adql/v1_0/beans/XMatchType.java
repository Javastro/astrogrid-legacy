/*
 * XML Type:  xMatchType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.XMatchType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans;


/**
 * An XML xMatchType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public interface XMatchType extends org.astrogrid.adql.v1_0.beans.SearchType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XMatchType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("xmatchtype13cetype");
    
    /**
     * Gets array of all "Table" elements
     */
    org.astrogrid.adql.v1_0.beans.XMatchTableAliasType[] getTableArray();
    
    /**
     * Gets ith "Table" element
     */
    org.astrogrid.adql.v1_0.beans.XMatchTableAliasType getTableArray(int i);
    
    /**
     * Returns number of "Table" element
     */
    int sizeOfTableArray();
    
    /**
     * Sets array of all "Table" element
     */
    void setTableArray(org.astrogrid.adql.v1_0.beans.XMatchTableAliasType[] tableArray);
    
    /**
     * Sets ith "Table" element
     */
    void setTableArray(int i, org.astrogrid.adql.v1_0.beans.XMatchTableAliasType table);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Table" element
     */
    org.astrogrid.adql.v1_0.beans.XMatchTableAliasType insertNewTable(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Table" element
     */
    org.astrogrid.adql.v1_0.beans.XMatchTableAliasType addNewTable();
    
    /**
     * Removes the ith "Table" element
     */
    void removeTable(int i);
    
    /**
     * Gets the "Nature" element
     */
    org.astrogrid.adql.v1_0.beans.ComparisonType.Enum getNature();
    
    /**
     * Gets (as xml) the "Nature" element
     */
    org.astrogrid.adql.v1_0.beans.ComparisonType xgetNature();
    
    /**
     * Sets the "Nature" element
     */
    void setNature(org.astrogrid.adql.v1_0.beans.ComparisonType.Enum nature);
    
    /**
     * Sets (as xml) the "Nature" element
     */
    void xsetNature(org.astrogrid.adql.v1_0.beans.ComparisonType nature);
    
    /**
     * Gets the "Sigma" element
     */
    org.astrogrid.adql.v1_0.beans.NumberType getSigma();
    
    /**
     * Sets the "Sigma" element
     */
    void setSigma(org.astrogrid.adql.v1_0.beans.NumberType sigma);
    
    /**
     * Appends and returns a new empty "Sigma" element
     */
    org.astrogrid.adql.v1_0.beans.NumberType addNewSigma();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.adql.v1_0.beans.XMatchType newInstance() {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.adql.v1_0.beans.XMatchType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.v1_0.beans.XMatchType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
