/**
 * AccessURLType_use.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class AccessURLType_use implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected AccessURLType_use(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _full = "full";
    public static final java.lang.String _base = "base";
    public static final java.lang.String _dir = "dir";
    public static final AccessURLType_use full = new AccessURLType_use(_full);
    public static final AccessURLType_use base = new AccessURLType_use(_base);
    public static final AccessURLType_use dir = new AccessURLType_use(_dir);
    public java.lang.String getValue() { return _value_;}
    public static AccessURLType_use fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        AccessURLType_use enum = (AccessURLType_use)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static AccessURLType_use fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
