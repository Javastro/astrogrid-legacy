/**
 * RelationshipType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class RelationshipType implements java.io.Serializable {
    private org.apache.axis.types.NMToken _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected RelationshipType(org.apache.axis.types.NMToken value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final org.apache.axis.types.NMToken _value1 = new org.apache.axis.types.NMToken("mirror-of");
    public static final org.apache.axis.types.NMToken _value2 = new org.apache.axis.types.NMToken("service-for");
    public static final org.apache.axis.types.NMToken _value3 = new org.apache.axis.types.NMToken("derived-from");
    public static final org.apache.axis.types.NMToken _value4 = new org.apache.axis.types.NMToken("related-to");
    public static final RelationshipType value1 = new RelationshipType(_value1);
    public static final RelationshipType value2 = new RelationshipType(_value2);
    public static final RelationshipType value3 = new RelationshipType(_value3);
    public static final RelationshipType value4 = new RelationshipType(_value4);
    public org.apache.axis.types.NMToken getValue() { return _value_;}
    public static RelationshipType fromValue(org.apache.axis.types.NMToken value)
          throws java.lang.IllegalStateException {
        RelationshipType enum = (RelationshipType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static RelationshipType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        try {
            return fromValue(new org.apache.axis.types.NMToken(value));
        } catch (Exception e) {
            throw new java.lang.IllegalStateException();
        }
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_.toString();}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
