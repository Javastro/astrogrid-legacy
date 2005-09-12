/**
 * BinaryOperatorType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class BinaryOperatorType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected BinaryOperatorType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _value1 = "+";
    public static final java.lang.String _value2 = "-";
    public static final java.lang.String _value3 = "*";
    public static final java.lang.String _value4 = "/";
    public static final BinaryOperatorType value1 = new BinaryOperatorType(_value1);
    public static final BinaryOperatorType value2 = new BinaryOperatorType(_value2);
    public static final BinaryOperatorType value3 = new BinaryOperatorType(_value3);
    public static final BinaryOperatorType value4 = new BinaryOperatorType(_value4);
    public java.lang.String getValue() { return _value_;}
    public static BinaryOperatorType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        BinaryOperatorType enum = (BinaryOperatorType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static BinaryOperatorType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
