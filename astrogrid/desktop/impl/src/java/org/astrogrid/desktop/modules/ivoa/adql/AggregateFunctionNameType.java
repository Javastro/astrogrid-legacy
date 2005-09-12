/**
 * AggregateFunctionNameType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class AggregateFunctionNameType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected AggregateFunctionNameType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _AVG = "AVG";
    public static final java.lang.String _MIN = "MIN";
    public static final java.lang.String _MAX = "MAX";
    public static final java.lang.String _SUM = "SUM";
    public static final java.lang.String _COUNT = "COUNT";
    public static final AggregateFunctionNameType AVG = new AggregateFunctionNameType(_AVG);
    public static final AggregateFunctionNameType MIN = new AggregateFunctionNameType(_MIN);
    public static final AggregateFunctionNameType MAX = new AggregateFunctionNameType(_MAX);
    public static final AggregateFunctionNameType SUM = new AggregateFunctionNameType(_SUM);
    public static final AggregateFunctionNameType COUNT = new AggregateFunctionNameType(_COUNT);
    public java.lang.String getValue() { return _value_;}
    public static AggregateFunctionNameType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        AggregateFunctionNameType enum = (AggregateFunctionNameType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static AggregateFunctionNameType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
