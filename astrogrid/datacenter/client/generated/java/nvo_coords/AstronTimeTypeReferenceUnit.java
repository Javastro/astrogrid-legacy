/**
 * AstronTimeTypeReferenceUnit.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package nvo_coords;

public class AstronTimeTypeReferenceUnit implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected AstronTimeTypeReferenceUnit(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _s = "s";
    public static final java.lang.String _d = "d";
    public static final AstronTimeTypeReferenceUnit s = new AstronTimeTypeReferenceUnit(_s);
    public static final AstronTimeTypeReferenceUnit d = new AstronTimeTypeReferenceUnit(_d);
    public java.lang.String getValue() { return _value_;}
    public static AstronTimeTypeReferenceUnit fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        AstronTimeTypeReferenceUnit enum = (AstronTimeTypeReferenceUnit)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static AstronTimeTypeReferenceUnit fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
