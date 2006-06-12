/*
 * XML Type:  archiveTableType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.ArchiveTableType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans;


/**
 * An XML archiveTableType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public interface ArchiveTableType extends org.astrogrid.adql.v1_0.beans.FromTableType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)schemaorg_apache_xmlbeans.system.sAD6892BF8B59ED2E8FBCAFA1D0210231.TypeSystemHolder.typeSystem.resolveHandle("archivetabletype29cdtype");
    
    /**
     * Gets the "Archive" attribute
     */
    java.lang.String getArchive();
    
    /**
     * Gets (as xml) the "Archive" attribute
     */
    org.apache.xmlbeans.XmlString xgetArchive();
    
    /**
     * Sets the "Archive" attribute
     */
    void setArchive(java.lang.String archive);
    
    /**
     * Sets (as xml) the "Archive" attribute
     */
    void xsetArchive(org.apache.xmlbeans.XmlString archive);
    
    /**
     * Gets the "Name" attribute
     */
    java.lang.String getName();
    
    /**
     * Gets (as xml) the "Name" attribute
     */
    org.apache.xmlbeans.XmlString xgetName();
    
    /**
     * Sets the "Name" attribute
     */
    void setName(java.lang.String name);
    
    /**
     * Sets (as xml) the "Name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlString name);
    
    /**
     * Gets the "Alias" attribute
     */
    java.lang.String getAlias();
    
    /**
     * Gets (as xml) the "Alias" attribute
     */
    org.apache.xmlbeans.XmlString xgetAlias();
    
    /**
     * True if has "Alias" attribute
     */
    boolean isSetAlias();
    
    /**
     * Sets the "Alias" attribute
     */
    void setAlias(java.lang.String alias);
    
    /**
     * Sets (as xml) the "Alias" attribute
     */
    void xsetAlias(org.apache.xmlbeans.XmlString alias);
    
    /**
     * Unsets the "Alias" attribute
     */
    void unsetAlias();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType newInstance() {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.astrogrid.adql.v1_0.beans.ArchiveTableType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.v1_0.beans.ArchiveTableType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
