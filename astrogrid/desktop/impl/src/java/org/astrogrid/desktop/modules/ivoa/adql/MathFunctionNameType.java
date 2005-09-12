/**
 * MathFunctionNameType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class MathFunctionNameType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected MathFunctionNameType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _ABS = "ABS";
    public static final java.lang.String _CEILING = "CEILING";
    public static final java.lang.String _DEGREES = "DEGREES";
    public static final java.lang.String _EXP = "EXP";
    public static final java.lang.String _FLOOR = "FLOOR";
    public static final java.lang.String _LOG = "LOG";
    public static final java.lang.String _PI = "PI";
    public static final java.lang.String _POWER = "POWER";
    public static final java.lang.String _RADIANS = "RADIANS";
    public static final java.lang.String _SQRT = "SQRT";
    public static final java.lang.String _SQUARE = "SQUARE";
    public static final java.lang.String _LOG10 = "LOG10";
    public static final java.lang.String _RAND = "RAND";
    public static final java.lang.String _ROUND = "ROUND";
    public static final java.lang.String _TRUNCATE = "TRUNCATE";
    public static final MathFunctionNameType ABS = new MathFunctionNameType(_ABS);
    public static final MathFunctionNameType CEILING = new MathFunctionNameType(_CEILING);
    public static final MathFunctionNameType DEGREES = new MathFunctionNameType(_DEGREES);
    public static final MathFunctionNameType EXP = new MathFunctionNameType(_EXP);
    public static final MathFunctionNameType FLOOR = new MathFunctionNameType(_FLOOR);
    public static final MathFunctionNameType LOG = new MathFunctionNameType(_LOG);
    public static final MathFunctionNameType PI = new MathFunctionNameType(_PI);
    public static final MathFunctionNameType POWER = new MathFunctionNameType(_POWER);
    public static final MathFunctionNameType RADIANS = new MathFunctionNameType(_RADIANS);
    public static final MathFunctionNameType SQRT = new MathFunctionNameType(_SQRT);
    public static final MathFunctionNameType SQUARE = new MathFunctionNameType(_SQUARE);
    public static final MathFunctionNameType LOG10 = new MathFunctionNameType(_LOG10);
    public static final MathFunctionNameType RAND = new MathFunctionNameType(_RAND);
    public static final MathFunctionNameType ROUND = new MathFunctionNameType(_ROUND);
    public static final MathFunctionNameType TRUNCATE = new MathFunctionNameType(_TRUNCATE);
    public java.lang.String getValue() { return _value_;}
    public static MathFunctionNameType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        MathFunctionNameType enum = (MathFunctionNameType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static MathFunctionNameType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
