/**
 * STREAMType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package fr.u_strasbg.vizier.xml.VOTable_1_1_xsd;

public class STREAMType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected STREAMType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _locator = "locator";
    public static final java.lang.String _other = "other";
    public static final STREAMType locator = new STREAMType(_locator);
    public static final STREAMType other = new STREAMType(_other);
    public java.lang.String getValue() { return _value_;}
    public static STREAMType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        STREAMType enum = (STREAMType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static STREAMType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
