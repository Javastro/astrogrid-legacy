/**
 * VelTimeUnitType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class VelTimeUnitType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected VelTimeUnitType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _value1 = "s";
    public static final java.lang.String _value2 = "h";
    public static final java.lang.String _value3 = "d";
    public static final java.lang.String _value4 = "a";
    public static final java.lang.String _value5 = "yr";
    public static final java.lang.String _value6 = "century";
    public static final java.lang.String _value7 = "";
    public static final VelTimeUnitType value1 = new VelTimeUnitType(_value1);
    public static final VelTimeUnitType value2 = new VelTimeUnitType(_value2);
    public static final VelTimeUnitType value3 = new VelTimeUnitType(_value3);
    public static final VelTimeUnitType value4 = new VelTimeUnitType(_value4);
    public static final VelTimeUnitType value5 = new VelTimeUnitType(_value5);
    public static final VelTimeUnitType value6 = new VelTimeUnitType(_value6);
    public static final VelTimeUnitType value7 = new VelTimeUnitType(_value7);
    public java.lang.String getValue() { return _value_;}
    public static VelTimeUnitType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        VelTimeUnitType enum = (VelTimeUnitType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static VelTimeUnitType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
