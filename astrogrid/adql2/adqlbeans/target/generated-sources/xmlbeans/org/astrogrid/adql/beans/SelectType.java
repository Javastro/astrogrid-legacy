/*
 * XML Type:  selectType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.SelectType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans;


/**
 * An XML selectType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public interface SelectType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SelectType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s7E335E7860F285121806956A259B6A77").resolveHandle("selecttype059etype");
    
    /**
     * Gets the "Allow" element
     */
    org.astrogrid.adql.beans.SelectionOptionType getAllow();
    
    /**
     * True if has "Allow" element
     */
    boolean isSetAllow();
    
    /**
     * Sets the "Allow" element
     */
    void setAllow(org.astrogrid.adql.beans.SelectionOptionType allow);
    
    /**
     * Appends and returns a new empty "Allow" element
     */
    org.astrogrid.adql.beans.SelectionOptionType addNewAllow();
    
    /**
     * Unsets the "Allow" element
     */
    void unsetAllow();
    
    /**
     * Gets the "Restrict" element
     */
    org.astrogrid.adql.beans.SelectionLimitType getRestrict();
    
    /**
     * True if has "Restrict" element
     */
    boolean isSetRestrict();
    
    /**
     * Sets the "Restrict" element
     */
    void setRestrict(org.astrogrid.adql.beans.SelectionLimitType restrict);
    
    /**
     * Appends and returns a new empty "Restrict" element
     */
    org.astrogrid.adql.beans.SelectionLimitType addNewRestrict();
    
    /**
     * Unsets the "Restrict" element
     */
    void unsetRestrict();
    
    /**
     * Gets the "SelectionList" element
     */
    org.astrogrid.adql.beans.SelectionListType getSelectionList();
    
    /**
     * Sets the "SelectionList" element
     */
    void setSelectionList(org.astrogrid.adql.beans.SelectionListType selectionList);
    
    /**
     * Appends and returns a new empty "SelectionList" element
     */
    org.astrogrid.adql.beans.SelectionListType addNewSelectionList();
    
    /**
     * Gets the "InTo" element
     */
    org.astrogrid.adql.beans.IntoType getInTo();
    
    /**
     * True if has "InTo" element
     */
    boolean isSetInTo();
    
    /**
     * Sets the "InTo" element
     */
    void setInTo(org.astrogrid.adql.beans.IntoType inTo);
    
    /**
     * Appends and returns a new empty "InTo" element
     */
    org.astrogrid.adql.beans.IntoType addNewInTo();
    
    /**
     * Unsets the "InTo" element
     */
    void unsetInTo();
    
    /**
     * Gets the "From" element
     */
    org.astrogrid.adql.beans.FromType getFrom();
    
    /**
     * True if has "From" element
     */
    boolean isSetFrom();
    
    /**
     * Sets the "From" element
     */
    void setFrom(org.astrogrid.adql.beans.FromType from);
    
    /**
     * Appends and returns a new empty "From" element
     */
    org.astrogrid.adql.beans.FromType addNewFrom();
    
    /**
     * Unsets the "From" element
     */
    void unsetFrom();
    
    /**
     * Gets the "Where" element
     */
    org.astrogrid.adql.beans.WhereType getWhere();
    
    /**
     * True if has "Where" element
     */
    boolean isSetWhere();
    
    /**
     * Sets the "Where" element
     */
    void setWhere(org.astrogrid.adql.beans.WhereType where);
    
    /**
     * Appends and returns a new empty "Where" element
     */
    org.astrogrid.adql.beans.WhereType addNewWhere();
    
    /**
     * Unsets the "Where" element
     */
    void unsetWhere();
    
    /**
     * Gets the "GroupBy" element
     */
    org.astrogrid.adql.beans.GroupByType getGroupBy();
    
    /**
     * True if has "GroupBy" element
     */
    boolean isSetGroupBy();
    
    /**
     * Sets the "GroupBy" element
     */
    void setGroupBy(org.astrogrid.adql.beans.GroupByType groupBy);
    
    /**
     * Appends and returns a new empty "GroupBy" element
     */
    org.astrogrid.adql.beans.GroupByType addNewGroupBy();
    
    /**
     * Unsets the "GroupBy" element
     */
    void unsetGroupBy();
    
    /**
     * Gets the "Having" element
     */
    org.astrogrid.adql.beans.HavingType getHaving();
    
    /**
     * True if has "Having" element
     */
    boolean isSetHaving();
    
    /**
     * Sets the "Having" element
     */
    void setHaving(org.astrogrid.adql.beans.HavingType having);
    
    /**
     * Appends and returns a new empty "Having" element
     */
    org.astrogrid.adql.beans.HavingType addNewHaving();
    
    /**
     * Unsets the "Having" element
     */
    void unsetHaving();
    
    /**
     * Gets the "OrderBy" element
     */
    org.astrogrid.adql.beans.OrderExpressionType getOrderBy();
    
    /**
     * True if has "OrderBy" element
     */
    boolean isSetOrderBy();
    
    /**
     * Sets the "OrderBy" element
     */
    void setOrderBy(org.astrogrid.adql.beans.OrderExpressionType orderBy);
    
    /**
     * Appends and returns a new empty "OrderBy" element
     */
    org.astrogrid.adql.beans.OrderExpressionType addNewOrderBy();
    
    /**
     * Unsets the "OrderBy" element
     */
    void unsetOrderBy();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.adql.beans.SelectType newInstance() {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.adql.beans.SelectType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.adql.beans.SelectType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.adql.beans.SelectType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.adql.beans.SelectType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.adql.beans.SelectType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.adql.beans.SelectType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
