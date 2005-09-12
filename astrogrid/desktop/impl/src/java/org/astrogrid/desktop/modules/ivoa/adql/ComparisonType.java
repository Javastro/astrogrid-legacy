/**
 * ComparisonType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class ComparisonType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ComparisonType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _value1 = "=";
    public static final java.lang.String _value2 = "<>";
    public static final java.lang.String _value3 = ">";
    public static final java.lang.String _value4 = ">=";
    public static final java.lang.String _value5 = "<";
    public static final java.lang.String _value6 = "<=";
    public static final ComparisonType value1 = new ComparisonType(_value1);
    public static final ComparisonType value2 = new ComparisonType(_value2);
    public static final ComparisonType value3 = new ComparisonType(_value3);
    public static final ComparisonType value4 = new ComparisonType(_value4);
    public static final ComparisonType value5 = new ComparisonType(_value5);
    public static final ComparisonType value6 = new ComparisonType(_value6);
    public java.lang.String getValue() { return _value_;}
    public static ComparisonType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        ComparisonType enum = (ComparisonType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static ComparisonType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
