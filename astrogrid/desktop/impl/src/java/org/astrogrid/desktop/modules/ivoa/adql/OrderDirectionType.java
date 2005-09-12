/**
 * OrderDirectionType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class OrderDirectionType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected OrderDirectionType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _ASC = "ASC";
    public static final java.lang.String _DESC = "DESC";
    public static final OrderDirectionType ASC = new OrderDirectionType(_ASC);
    public static final OrderDirectionType DESC = new OrderDirectionType(_DESC);
    public java.lang.String getValue() { return _value_;}
    public static OrderDirectionType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        OrderDirectionType enum = (OrderDirectionType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static OrderDirectionType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
