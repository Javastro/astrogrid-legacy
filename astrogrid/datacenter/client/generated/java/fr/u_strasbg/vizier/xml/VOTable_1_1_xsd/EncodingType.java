/**
 * EncodingType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package fr.u_strasbg.vizier.xml.VOTable_1_1_xsd;

public class EncodingType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected EncodingType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _gzip = "gzip";
    public static final java.lang.String _base64 = "base64";
    public static final java.lang.String _dynamic = "dynamic";
    public static final java.lang.String _none = "none";
    public static final EncodingType gzip = new EncodingType(_gzip);
    public static final EncodingType base64 = new EncodingType(_base64);
    public static final EncodingType dynamic = new EncodingType(_dynamic);
    public static final EncodingType none = new EncodingType(_none);
    public java.lang.String getValue() { return _value_;}
    public static EncodingType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        EncodingType enum = (EncodingType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static EncodingType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
