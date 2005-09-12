/**
 * AstronTimeTypeRelativeTimeUnit.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class AstronTimeTypeRelativeTimeUnit implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected AstronTimeTypeRelativeTimeUnit(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _s = "s";
    public static final java.lang.String _d = "d";
    public static final AstronTimeTypeRelativeTimeUnit s = new AstronTimeTypeRelativeTimeUnit(_s);
    public static final AstronTimeTypeRelativeTimeUnit d = new AstronTimeTypeRelativeTimeUnit(_d);
    public java.lang.String getValue() { return _value_;}
    public static AstronTimeTypeRelativeTimeUnit fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        AstronTimeTypeRelativeTimeUnit enum = (AstronTimeTypeRelativeTimeUnit)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static AstronTimeTypeRelativeTimeUnit fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
