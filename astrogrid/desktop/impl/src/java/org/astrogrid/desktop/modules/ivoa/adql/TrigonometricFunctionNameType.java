/**
 * TrigonometricFunctionNameType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class TrigonometricFunctionNameType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TrigonometricFunctionNameType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _SIN = "SIN";
    public static final java.lang.String _COS = "COS";
    public static final java.lang.String _TAN = "TAN";
    public static final java.lang.String _COT = "COT";
    public static final java.lang.String _ASIN = "ASIN";
    public static final java.lang.String _ACOS = "ACOS";
    public static final java.lang.String _ATAN = "ATAN";
    public static final java.lang.String _ATAN2 = "ATAN2";
    public static final TrigonometricFunctionNameType SIN = new TrigonometricFunctionNameType(_SIN);
    public static final TrigonometricFunctionNameType COS = new TrigonometricFunctionNameType(_COS);
    public static final TrigonometricFunctionNameType TAN = new TrigonometricFunctionNameType(_TAN);
    public static final TrigonometricFunctionNameType COT = new TrigonometricFunctionNameType(_COT);
    public static final TrigonometricFunctionNameType ASIN = new TrigonometricFunctionNameType(_ASIN);
    public static final TrigonometricFunctionNameType ACOS = new TrigonometricFunctionNameType(_ACOS);
    public static final TrigonometricFunctionNameType ATAN = new TrigonometricFunctionNameType(_ATAN);
    public static final TrigonometricFunctionNameType ATAN2 = new TrigonometricFunctionNameType(_ATAN2);
    public java.lang.String getValue() { return _value_;}
    public static TrigonometricFunctionNameType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        TrigonometricFunctionNameType enum = (TrigonometricFunctionNameType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static TrigonometricFunctionNameType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
