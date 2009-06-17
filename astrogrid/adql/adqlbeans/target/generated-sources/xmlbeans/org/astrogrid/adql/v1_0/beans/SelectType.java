/*
 * XML Type:  selectType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.SelectType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans;


/**
 * An XML selectType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public interface SelectType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SelectType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("selecttypec15dtype");
    
    /**
     * Gets the "Allow" element
     */
    org.astrogrid.adql.v1_0.beans.SelectionOptionType getAllow();
    
    /**
     * True if has "Allow" element
     */
    boolean isSetAllow();
    
    /**
     * Sets the "Allow" element
     */
    void setAllow(org.astrogrid.adql.v1_0.beans.SelectionOptionType allow);
    
    /**
     * Appends and returns a new empty "Allow" element
     */
    org.astrogrid.adql.v1_0.beans.SelectionOptionType addNewAllow();
    
    /**
     * Unsets the "Allow" element
     */
    void unsetAllow();
    
    /**
     * Gets the "Restrict" element
     */
    org.astrogrid.adql.v1_0.beans.SelectionLimitType getRestrict();
    
    /**
     * True if has "Restrict" element
     */
    boolean isSetRestrict();
    
    /**
     * Sets the "Restrict" element
     */
    void setRestrict(org.astrogrid.adql.v1_0.beans.SelectionLimitType restrict);
    
    /**
     * Appends and returns a new empty "Restrict" element
     */
    org.astrogrid.adql.v1_0.beans.SelectionLimitType addNewRestrict();
    
    /**
     * Unsets the "Restrict" element
     */
    void unsetRestrict();
    
    /**
     * Gets the "SelectionList" element
     */
    org.astrogrid.adql.v1_0.beans.SelectionListType getSelectionList();
    
    /**
     * Sets the "SelectionList" element
     */
    void setSelectionList(org.astrogrid.adql.v1_0.beans.SelectionListType selectionList);
    
    /**
     * Appends and returns a new empty "SelectionList" element
     */
    org.astrogrid.adql.v1_0.beans.SelectionListType addNewSelectionList();
    
    /**
     * Gets the "InTo" element
     */
    org.astrogrid.adql.v1_0.beans.IntoType getInTo();
    
    /**
     * True if has "InTo" element
     */
    boolean isSetInTo();
    
    /**
     * Sets the "InTo" element
     */
    void setInTo(org.astrogrid.adql.v1_0.beans.IntoType inTo);
    
    /**
     * Appends and returns a new empty "InTo" element
     */
    org.astrogrid.adql.v1_0.beans.IntoType addNewInTo();
    
    /**
     * Unsets the "InTo" element
     */
    void unsetInTo();
    
    /**
     * Gets the "From" element
     */
    org.astrogrid.adql.v1_0.beans.FromType getFrom();
    
    /**
     * True if has "From" element
     */
    boolean isSetFrom();
    
    /**
     * Sets the "From" element
     */
    void setFrom(org.astrogrid.adql.v1_0.beans.FromType from);
    
    /**
     * Appends and returns a new empty "From" element
     */
    org.astrogrid.adql.v1_0.beans.FromType addNewFrom();
    
    /**
     * Unsets the "From" element
     */
    void unsetFrom();
    
    /**
     * Gets the "Where" element
     */
    org.astrogrid.adql.v1_0.beans.WhereType getWhere();
    
    /**
     * True if has "Where" element
     */
    boolean isSetWhere();
    
    /**
     * Sets the "Where" element
     */
    void setWhere(org.astrogrid.adql.v1_0.beans.WhereType where);
    
    /**
     * Appends and returns a new empty "Where" element
     */
    org.astrogrid.adql.v1_0.beans.WhereType addNewWhere();
    
    /**
     * Unsets the "Where" element
     */
    void unsetWhere();
    
    /**
     * Gets the "GroupBy" element
     */
    org.astrogrid.adql.v1_0.beans.GroupByType getGroupBy();
    
    /**
     * True if has "GroupBy" element
     */
    boolean isSetGroupBy();
    
    /**
     * Sets the "GroupBy" element
     */
    void setGroupBy(org.astrogrid.adql.v1_0.beans.GroupByType groupBy);
    
    /**
     * Appends and returns a new empty "GroupBy" element
     */
    org.astrogrid.adql.v1_0.beans.GroupByType addNewGroupBy();
    
    /**
     * Unsets the "GroupBy" element
     */
    void unsetGroupBy();
    
    /**
     * Gets the "Having" element
     */
    org.astrogrid.adql.v1_0.beans.HavingType getHaving();
    
    /**
     * True if has "Having" element
     */
    boolean isSetHaving();
    
    /**
     * Sets the "Having" element
     */
    void setHaving(org.astrogrid.adql.v1_0.beans.HavingType having);
    
    /**
     * Appends and returns a new empty "Having" element
     */
    org.astrogrid.adql.v1_0.beans.HavingType addNewHaving();
    
    /**
     * Unsets the "Having" element
     */
    void unsetHaving();
    
    /**
     * Gets the "OrderBy" element
     */
    org.astrogrid.adql.v1_0.beans.OrderExpressionType getOrderBy();
    
    /**
     * True if has "OrderBy" element
     */
    boolean isSetOrderBy();
    
    /**
     * Sets the "OrderBy" element
     */
    void setOrderBy(org.astrogrid.adql.v1_0.beans.OrderExpressionType orderBy);
    
    /**
     * Appends and returns a new empty "OrderBy" element
     */
    org.astrogrid.adql.v1_0.beans.OrderExpressionType addNewOrderBy();
    
    /**
     * Unsets the "OrderBy" element
     */
    void unsetOrderBy();
    
    /**
     * Gets the "StartComment" element
     */
    java.lang.String getStartComment();
    
    /**
     * Gets (as xml) the "StartComment" element
     */
    org.apache.xmlbeans.XmlString xgetStartComment();
    
    /**
     * True if has "StartComment" element
     */
    boolean isSetStartComment();
    
    /**
     * Sets the "StartComment" element
     */
    void setStartComment(java.lang.String startComment);
    
    /**
     * Sets (as xml) the "StartComment" element
     */
    void xsetStartComment(org.apache.xmlbeans.XmlString startComment);
    
    /**
     * Unsets the "StartComment" element
     */
    void unsetStartComment();
    
    /**
     * Gets the "EndComment" element
     */
    java.lang.String getEndComment();
    
    /**
     * Gets (as xml) the "EndComment" element
     */
    org.apache.xmlbeans.XmlString xgetEndComment();
    
    /**
     * True if has "EndComment" element
     */
    boolean isSetEndComment();
    
    /**
     * Sets the "EndComment" element
     */
    void setEndComment(java.lang.String endComment);
    
    /**
     * Sets (as xml) the "EndComment" element
     */
    void xsetEndComment(org.apache.xmlbeans.XmlString endComment);
    
    /**
     * Unsets the "EndComment" element
     */
    void unsetEndComment();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.adql.v1_0.beans.SelectType newInstance() {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.adql.v1_0.beans.SelectType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.v1_0.beans.SelectType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
