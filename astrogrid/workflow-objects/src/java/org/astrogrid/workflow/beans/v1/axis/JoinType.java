/**
 * JoinType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.workflow.beans.v1.axis;

public class JoinType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected JoinType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _value1 = "true";
    public static final java.lang.String _value2 = "false";
    public static final java.lang.String _value3 = "any";
    public static final JoinType value1 = new JoinType(_value1);
    public static final JoinType value2 = new JoinType(_value2);
    public static final JoinType value3 = new JoinType(_value3);
    public java.lang.String getValue() { return _value_;}
    public static JoinType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        JoinType enum = (JoinType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static JoinType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
