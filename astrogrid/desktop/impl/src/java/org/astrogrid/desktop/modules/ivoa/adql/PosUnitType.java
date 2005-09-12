/**
 * PosUnitType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class PosUnitType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected PosUnitType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _value1 = "deg";
    public static final java.lang.String _value2 = "rad";
    public static final java.lang.String _value3 = "h";
    public static final java.lang.String _value4 = "arcmin";
    public static final java.lang.String _value5 = "arcsec";
    public static final java.lang.String _value6 = "m";
    public static final java.lang.String _value7 = "km";
    public static final java.lang.String _value8 = "mm";
    public static final java.lang.String _value9 = "au";
    public static final java.lang.String _value10 = "pc";
    public static final java.lang.String _value11 = "kpc";
    public static final java.lang.String _value12 = "Mpc";
    public static final java.lang.String _value13 = "lyr";
    public static final java.lang.String _value14 = "";
    public static final PosUnitType value1 = new PosUnitType(_value1);
    public static final PosUnitType value2 = new PosUnitType(_value2);
    public static final PosUnitType value3 = new PosUnitType(_value3);
    public static final PosUnitType value4 = new PosUnitType(_value4);
    public static final PosUnitType value5 = new PosUnitType(_value5);
    public static final PosUnitType value6 = new PosUnitType(_value6);
    public static final PosUnitType value7 = new PosUnitType(_value7);
    public static final PosUnitType value8 = new PosUnitType(_value8);
    public static final PosUnitType value9 = new PosUnitType(_value9);
    public static final PosUnitType value10 = new PosUnitType(_value10);
    public static final PosUnitType value11 = new PosUnitType(_value11);
    public static final PosUnitType value12 = new PosUnitType(_value12);
    public static final PosUnitType value13 = new PosUnitType(_value13);
    public static final PosUnitType value14 = new PosUnitType(_value14);
    public java.lang.String getValue() { return _value_;}
    public static PosUnitType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        PosUnitType enum = (PosUnitType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static PosUnitType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
