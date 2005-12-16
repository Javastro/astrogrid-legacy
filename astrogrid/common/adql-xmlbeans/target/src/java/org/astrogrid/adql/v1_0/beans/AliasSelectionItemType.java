/*
 * XML Type:  aliasSelectionItemType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.AliasSelectionItemType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans;


/**
 * An XML aliasSelectionItemType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public interface AliasSelectionItemType extends org.astrogrid.adql.v1_0.beans.SelectionItemType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)schemaorg_apache_xmlbeans.system.sA07F9A9BE4CFFAFC2C7FCE82C7F19589.TypeSystemHolder.typeSystem.resolveHandle("aliasselectionitemtypef6f0type");
    
    /**
     * Gets the "Expression" element
     */
    org.astrogrid.adql.v1_0.beans.ScalarExpressionType getExpression();
    
    /**
     * Sets the "Expression" element
     */
    void setExpression(org.astrogrid.adql.v1_0.beans.ScalarExpressionType expression);
    
    /**
     * Appends and returns a new empty "Expression" element
     */
    org.astrogrid.adql.v1_0.beans.ScalarExpressionType addNewExpression();
    
    /**
     * Gets the "As" attribute
     */
    java.lang.String getAs();
    
    /**
     * Gets (as xml) the "As" attribute
     */
    org.apache.xmlbeans.XmlString xgetAs();
    
    /**
     * True if has "As" attribute
     */
    boolean isSetAs();
    
    /**
     * Sets the "As" attribute
     */
    void setAs(java.lang.String as);
    
    /**
     * Sets (as xml) the "As" attribute
     */
    void xsetAs(org.apache.xmlbeans.XmlString as);
    
    /**
     * Unsets the "As" attribute
     */
    void unsetAs();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType newInstance() {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.astrogrid.adql.v1_0.beans.AliasSelectionItemType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.v1_0.beans.AliasSelectionItemType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
