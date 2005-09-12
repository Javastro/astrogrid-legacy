/**
 * JointTableQualifierType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class JointTableQualifierType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected JointTableQualifierType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _LEFT_OUTER = "LEFT_OUTER";
    public static final java.lang.String _RIGHT_OUTER = "RIGHT_OUTER";
    public static final java.lang.String _FULL_OUTER = "FULL_OUTER";
    public static final java.lang.String _INNER = "INNER";
    public static final java.lang.String _CROSS = "CROSS";
    public static final JointTableQualifierType LEFT_OUTER = new JointTableQualifierType(_LEFT_OUTER);
    public static final JointTableQualifierType RIGHT_OUTER = new JointTableQualifierType(_RIGHT_OUTER);
    public static final JointTableQualifierType FULL_OUTER = new JointTableQualifierType(_FULL_OUTER);
    public static final JointTableQualifierType INNER = new JointTableQualifierType(_INNER);
    public static final JointTableQualifierType CROSS = new JointTableQualifierType(_CROSS);
    public java.lang.String getValue() { return _value_;}
    public static JointTableQualifierType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        JointTableQualifierType enum = (JointTableQualifierType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static JointTableQualifierType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
