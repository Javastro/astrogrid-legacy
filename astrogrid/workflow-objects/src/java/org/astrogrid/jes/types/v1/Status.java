/**
 * Status.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.types.v1;

public class Status implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected Status(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _INITIALIZED = "INITIALIZED";
    public static final java.lang.String _RUNNING = "RUNNING";
    public static final java.lang.String _COMPLETED = "COMPLETED";
    public static final java.lang.String _ERROR = "ERROR";
    public static final Status INITIALIZED = new Status(_INITIALIZED);
    public static final Status RUNNING = new Status(_RUNNING);
    public static final Status COMPLETED = new Status(_COMPLETED);
    public static final Status ERROR = new Status(_ERROR);
    public java.lang.String getValue() { return _value_;}
    public static Status fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        Status enum = (Status)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static Status fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
