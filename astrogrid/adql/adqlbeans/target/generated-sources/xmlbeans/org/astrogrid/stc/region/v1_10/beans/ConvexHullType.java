/*
 * XML Type:  convexHullType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.ConvexHullType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans;


/**
 * An XML convexHullType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public interface ConvexHullType extends org.astrogrid.stc.region.v1_10.beans.ShapeType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ConvexHullType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("convexhulltype7513type");
    
    /**
     * Gets array of all "Point" elements
     */
    java.util.List[] getPointArray();
    
    /**
     * Gets ith "Point" element
     */
    java.util.List getPointArray(int i);
    
    /**
     * Gets (as xml) array of all "Point" elements
     */
    org.astrogrid.stc.coords.v1_10.beans.Double3Type[] xgetPointArray();
    
    /**
     * Gets (as xml) ith "Point" element
     */
    org.astrogrid.stc.coords.v1_10.beans.Double3Type xgetPointArray(int i);
    
    /**
     * Returns number of "Point" element
     */
    int sizeOfPointArray();
    
    /**
     * Sets array of all "Point" element
     */
    void setPointArray(java.util.List[] pointArray);
    
    /**
     * Sets ith "Point" element
     */
    void setPointArray(int i, java.util.List point);
    
    /**
     * Sets (as xml) array of all "Point" element
     */
    void xsetPointArray(org.astrogrid.stc.coords.v1_10.beans.Double3Type[] pointArray);
    
    /**
     * Sets (as xml) ith "Point" element
     */
    void xsetPointArray(int i, org.astrogrid.stc.coords.v1_10.beans.Double3Type point);
    
    /**
     * Inserts the value as the ith "Point" element
     */
    void insertPoint(int i, java.util.List point);
    
    /**
     * Appends the value as the last "Point" element
     */
    void addPoint(java.util.List point);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Point" element
     */
    org.astrogrid.stc.coords.v1_10.beans.Double3Type insertNewPoint(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Point" element
     */
    org.astrogrid.stc.coords.v1_10.beans.Double3Type addNewPoint();
    
    /**
     * Removes the ith "Point" element
     */
    void removePoint(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType newInstance() {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.stc.region.v1_10.beans.ConvexHullType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.stc.region.v1_10.beans.ConvexHullType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
