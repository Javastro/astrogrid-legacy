/**
 * AstronTimeTypeReferenceTime_base.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class AstronTimeTypeReferenceTime_base implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected AstronTimeTypeReferenceTime_base(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _ISO8601 = "ISO8601";
    public static final java.lang.String _JD = "JD";
    public static final java.lang.String _MJD = "MJD";
    public static final java.lang.String _relative = "relative";
    public static final AstronTimeTypeReferenceTime_base ISO8601 = new AstronTimeTypeReferenceTime_base(_ISO8601);
    public static final AstronTimeTypeReferenceTime_base JD = new AstronTimeTypeReferenceTime_base(_JD);
    public static final AstronTimeTypeReferenceTime_base MJD = new AstronTimeTypeReferenceTime_base(_MJD);
    public static final AstronTimeTypeReferenceTime_base relative = new AstronTimeTypeReferenceTime_base(_relative);
    public java.lang.String getValue() { return _value_;}
    public static AstronTimeTypeReferenceTime_base fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        AstronTimeTypeReferenceTime_base enum = (AstronTimeTypeReferenceTime_base)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static AstronTimeTypeReferenceTime_base fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
