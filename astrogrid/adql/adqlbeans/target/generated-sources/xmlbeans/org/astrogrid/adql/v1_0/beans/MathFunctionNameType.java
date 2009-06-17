/*
 * XML Type:  mathFunctionNameType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.MathFunctionNameType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans;


/**
 * An XML mathFunctionNameType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is an atomic type that is a restriction of org.astrogrid.adql.v1_0.beans.MathFunctionNameType.
 */
public interface MathFunctionNameType extends org.apache.xmlbeans.XmlString
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MathFunctionNameType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2AEA7D228DB83D6EA12A6A0F2F278CBB").resolveHandle("mathfunctionnametype078ctype");
    
    org.apache.xmlbeans.StringEnumAbstractBase enumValue();
    void set(org.apache.xmlbeans.StringEnumAbstractBase e);
    
    static final Enum ABS = Enum.forString("ABS");
    static final Enum CEILING = Enum.forString("CEILING");
    static final Enum DEGREES = Enum.forString("DEGREES");
    static final Enum EXP = Enum.forString("EXP");
    static final Enum FLOOR = Enum.forString("FLOOR");
    static final Enum LOG = Enum.forString("LOG");
    static final Enum PI = Enum.forString("PI");
    static final Enum POWER = Enum.forString("POWER");
    static final Enum RADIANS = Enum.forString("RADIANS");
    static final Enum SQRT = Enum.forString("SQRT");
    static final Enum SQUARE = Enum.forString("SQUARE");
    static final Enum LOG_10 = Enum.forString("LOG10");
    static final Enum RAND = Enum.forString("RAND");
    static final Enum ROUND = Enum.forString("ROUND");
    static final Enum TRUNCATE = Enum.forString("TRUNCATE");
    
    static final int INT_ABS = Enum.INT_ABS;
    static final int INT_CEILING = Enum.INT_CEILING;
    static final int INT_DEGREES = Enum.INT_DEGREES;
    static final int INT_EXP = Enum.INT_EXP;
    static final int INT_FLOOR = Enum.INT_FLOOR;
    static final int INT_LOG = Enum.INT_LOG;
    static final int INT_PI = Enum.INT_PI;
    static final int INT_POWER = Enum.INT_POWER;
    static final int INT_RADIANS = Enum.INT_RADIANS;
    static final int INT_SQRT = Enum.INT_SQRT;
    static final int INT_SQUARE = Enum.INT_SQUARE;
    static final int INT_LOG_10 = Enum.INT_LOG_10;
    static final int INT_RAND = Enum.INT_RAND;
    static final int INT_ROUND = Enum.INT_ROUND;
    static final int INT_TRUNCATE = Enum.INT_TRUNCATE;
    
    /**
     * Enumeration value class for org.astrogrid.adql.v1_0.beans.MathFunctionNameType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_ABS
     * Enum.forString(s); // returns the enum value for a string
     * Enum.forInt(i); // returns the enum value for an int
     * </pre>
     * Enumeration objects are immutable singleton objects that
     * can be compared using == object equality. They have no
     * public constructor. See the constants defined within this
     * class for all the valid values.
     */
    static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase
    {
        /**
         * Returns the enum value for a string, or null if none.
         */
        public static Enum forString(java.lang.String s)
            { return (Enum)table.forString(s); }
        /**
         * Returns the enum value corresponding to an int, or null if none.
         */
        public static Enum forInt(int i)
            { return (Enum)table.forInt(i); }
        
        private Enum(java.lang.String s, int i)
            { super(s, i); }
        
        static final int INT_ABS = 1;
        static final int INT_CEILING = 2;
        static final int INT_DEGREES = 3;
        static final int INT_EXP = 4;
        static final int INT_FLOOR = 5;
        static final int INT_LOG = 6;
        static final int INT_PI = 7;
        static final int INT_POWER = 8;
        static final int INT_RADIANS = 9;
        static final int INT_SQRT = 10;
        static final int INT_SQUARE = 11;
        static final int INT_LOG_10 = 12;
        static final int INT_RAND = 13;
        static final int INT_ROUND = 14;
        static final int INT_TRUNCATE = 15;
        
        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table
        (
            new Enum[]
            {
                new Enum("ABS", INT_ABS),
                new Enum("CEILING", INT_CEILING),
                new Enum("DEGREES", INT_DEGREES),
                new Enum("EXP", INT_EXP),
                new Enum("FLOOR", INT_FLOOR),
                new Enum("LOG", INT_LOG),
                new Enum("PI", INT_PI),
                new Enum("POWER", INT_POWER),
                new Enum("RADIANS", INT_RADIANS),
                new Enum("SQRT", INT_SQRT),
                new Enum("SQUARE", INT_SQUARE),
                new Enum("LOG10", INT_LOG_10),
                new Enum("RAND", INT_RAND),
                new Enum("ROUND", INT_ROUND),
                new Enum("TRUNCATE", INT_TRUNCATE),
            }
        );
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() { return forInt(intValue()); } 
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType newValue(java.lang.Object obj) {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) type.newValue( obj ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType newInstance() {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.astrogrid.adql.v1_0.beans.MathFunctionNameType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.astrogrid.adql.v1_0.beans.MathFunctionNameType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
