/**
 * SwitchTypes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.beans.v1.axis.ceabase;

public class SwitchTypes implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected SwitchTypes(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _normal = "normal";
    public static final java.lang.String _keyword = "keyword";
    public static final SwitchTypes normal = new SwitchTypes(_normal);
    public static final SwitchTypes keyword = new SwitchTypes(_keyword);
    public java.lang.String getValue() { return _value_;}
    public static SwitchTypes fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        SwitchTypes enum = (SwitchTypes)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static SwitchTypes fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
