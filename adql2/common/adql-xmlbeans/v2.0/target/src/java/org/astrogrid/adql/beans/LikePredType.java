/*
 * XML Type:  likePredType
 * Namespace: http://www.ivoa.net/xml/ADQL/v2.0/adql
 * Java type: org.astrogrid.adql.beans.LikePredType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans;


/**
 * An XML likePredType(@http://www.ivoa.net/xml/ADQL/v2.0/adql).
 *
 * This is a complex type.
 */
public interface LikePredType extends org.astrogrid.adql.beans.SearchType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(LikePredType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2359EC142DE5834D202372C3F9D3B794").resolveHandle("likepredtype2b33type");
    
    /**
     * Gets the "Arg" element
     */
    org.astrogrid.adql.beans.ScalarExpressionType getArg();
    
    /**
     * Sets the "Arg" element
     */
    void setArg(org.astrogrid.adql.beans.ScalarExpressionType arg);
    
    /**
     * Appends and returns a new empty "Arg" element
     */
    org.astrogrid.adql.beans.ScalarExpressionType addNewArg();
    
    /**
     * Gets the "Pattern" element
     */
    org.astrogrid.adql.beans.AtomType getPattern();
    
    /**
     * Sets the "Pattern" element
     */
    void setPattern(org.astrogrid.adql.beans.AtomType pattern);
    
    /**
     * Appends and returns a new empty "Pattern" element
     */
    org.astrogrid.adql.beans.AtomType addNewPattern();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.adql.beans.LikePredType newInstance() {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.adql.beans.LikePredType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.adql.beans.LikePredType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.adql.beans.LikePredType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.adql.beans.LikePredType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.adql.beans.LikePredType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.adql.beans.LikePredType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.beans.LikePredType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}