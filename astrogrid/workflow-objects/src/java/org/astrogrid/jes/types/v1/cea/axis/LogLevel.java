/**
 * LogLevel.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.types.v1.cea.axis;

public class LogLevel implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected LogLevel(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _info = "info";
    public static final java.lang.String _warn = "warn";
    public static final java.lang.String _error = "error";
    public static final LogLevel info = new LogLevel(_info);
    public static final LogLevel warn = new LogLevel(_warn);
    public static final LogLevel error = new LogLevel(_error);
    public java.lang.String getValue() { return _value_;}
    public static LogLevel fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        LogLevel enum = (LogLevel)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static LogLevel fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
