/**
 * ExecutionPhase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.types.v1.cea.axis;

public class ExecutionPhase implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ExecutionPhase(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _UNKNOWN = "UNKNOWN";
    public static final java.lang.String _INITIALIZING = "INITIALIZING";
    public static final java.lang.String _RUNNING = "RUNNING";
    public static final java.lang.String _COMPLETED = "COMPLETED";
    public static final java.lang.String _ERROR = "ERROR";
    public static final ExecutionPhase UNKNOWN = new ExecutionPhase(_UNKNOWN);
    public static final ExecutionPhase INITIALIZING = new ExecutionPhase(_INITIALIZING);
    public static final ExecutionPhase RUNNING = new ExecutionPhase(_RUNNING);
    public static final ExecutionPhase COMPLETED = new ExecutionPhase(_COMPLETED);
    public static final ExecutionPhase ERROR = new ExecutionPhase(_ERROR);
    public java.lang.String getValue() { return _value_;}
    public static ExecutionPhase fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        ExecutionPhase enum = (ExecutionPhase)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static ExecutionPhase fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
