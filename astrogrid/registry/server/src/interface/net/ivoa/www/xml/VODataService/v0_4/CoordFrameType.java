/**
 * CoordFrameType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.www.xml.VODataService.v0_4;

public class CoordFrameType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected CoordFrameType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _ICRS = "ICRS";
    public static final java.lang.String _FK5 = "FK5";
    public static final java.lang.String _FK4 = "FK4";
    public static final java.lang.String _ECL = "ECL";
    public static final java.lang.String _GAL = "GAL";
    public static final java.lang.String _SGAL = "SGAL";
    public static final CoordFrameType ICRS = new CoordFrameType(_ICRS);
    public static final CoordFrameType FK5 = new CoordFrameType(_FK5);
    public static final CoordFrameType FK4 = new CoordFrameType(_FK4);
    public static final CoordFrameType ECL = new CoordFrameType(_ECL);
    public static final CoordFrameType GAL = new CoordFrameType(_GAL);
    public static final CoordFrameType SGAL = new CoordFrameType(_SGAL);
    public java.lang.String getValue() { return _value_;}
    public static CoordFrameType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        CoordFrameType enum = (CoordFrameType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static CoordFrameType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
