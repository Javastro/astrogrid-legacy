/**
 * RightsType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.www.xml.VODataService.v0_4;

public class RightsType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected RightsType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _value1 = "public";
    public static final java.lang.String _value2 = "public";
    public static final java.lang.String _value3 = "secure";
    public static final java.lang.String _value4 = "proprietary";
    public static final RightsType value1 = new RightsType(_value1);
    public static final RightsType value2 = new RightsType(_value2);
    public static final RightsType value3 = new RightsType(_value3);
    public static final RightsType value4 = new RightsType(_value4);
    public java.lang.String getValue() { return _value_;}
    public static RightsType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        RightsType enum = (RightsType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static RightsType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
