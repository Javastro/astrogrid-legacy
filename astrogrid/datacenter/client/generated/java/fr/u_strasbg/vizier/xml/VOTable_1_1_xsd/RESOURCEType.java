/**
 * RESOURCEType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package fr.u_strasbg.vizier.xml.VOTable_1_1_xsd;

public class RESOURCEType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected RESOURCEType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _results = "results";
    public static final java.lang.String _meta = "meta";
    public static final RESOURCEType results = new RESOURCEType(_results);
    public static final RESOURCEType meta = new RESOURCEType(_meta);
    public java.lang.String getValue() { return _value_;}
    public static RESOURCEType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        RESOURCEType enum = (RESOURCEType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static RESOURCEType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
