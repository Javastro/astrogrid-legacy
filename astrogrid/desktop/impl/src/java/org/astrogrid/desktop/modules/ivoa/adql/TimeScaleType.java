/**
 * TimeScaleType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class TimeScaleType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TimeScaleType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _TT = "TT";
    public static final java.lang.String _TDT = "TDT";
    public static final java.lang.String _ET = "ET";
    public static final java.lang.String _TDB = "TDB";
    public static final java.lang.String _TCG = "TCG";
    public static final java.lang.String _TCB = "TCB";
    public static final java.lang.String _TAI = "TAI";
    public static final java.lang.String _IAT = "IAT";
    public static final java.lang.String _UTC = "UTC";
    public static final java.lang.String _LST = "LST";
    public static final TimeScaleType TT = new TimeScaleType(_TT);
    public static final TimeScaleType TDT = new TimeScaleType(_TDT);
    public static final TimeScaleType ET = new TimeScaleType(_ET);
    public static final TimeScaleType TDB = new TimeScaleType(_TDB);
    public static final TimeScaleType TCG = new TimeScaleType(_TCG);
    public static final TimeScaleType TCB = new TimeScaleType(_TCB);
    public static final TimeScaleType TAI = new TimeScaleType(_TAI);
    public static final TimeScaleType IAT = new TimeScaleType(_IAT);
    public static final TimeScaleType UTC = new TimeScaleType(_UTC);
    public static final TimeScaleType LST = new TimeScaleType(_LST);
    public java.lang.String getValue() { return _value_;}
    public static TimeScaleType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        TimeScaleType enum = (TimeScaleType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static TimeScaleType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
