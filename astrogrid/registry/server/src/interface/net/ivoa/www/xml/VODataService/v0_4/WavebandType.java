/**
 * WavebandType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.www.xml.VODataService.v0_4;

public class WavebandType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected WavebandType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _value1 = "Radio";
    public static final java.lang.String _value2 = "Millimeter";
    public static final java.lang.String _value3 = "Infrared";
    public static final java.lang.String _value4 = "Optical";
    public static final java.lang.String _value5 = "UV";
    public static final java.lang.String _value6 = "EUV";
    public static final java.lang.String _value7 = "X-ray";
    public static final java.lang.String _value8 = "Gamma-ray";
    public static final WavebandType value1 = new WavebandType(_value1);
    public static final WavebandType value2 = new WavebandType(_value2);
    public static final WavebandType value3 = new WavebandType(_value3);
    public static final WavebandType value4 = new WavebandType(_value4);
    public static final WavebandType value5 = new WavebandType(_value5);
    public static final WavebandType value6 = new WavebandType(_value6);
    public static final WavebandType value7 = new WavebandType(_value7);
    public static final WavebandType value8 = new WavebandType(_value8);
    public java.lang.String getValue() { return _value_;}
    public static WavebandType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        WavebandType enum = (WavebandType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static WavebandType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
