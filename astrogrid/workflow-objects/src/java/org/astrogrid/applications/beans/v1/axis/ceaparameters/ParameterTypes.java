/**
 * ParameterTypes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.beans.v1.axis.ceaparameters;

public class ParameterTypes implements java.io.Serializable {
    private javax.xml.namespace.QName _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ParameterTypes(javax.xml.namespace.QName value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final javax.xml.namespace.QName _value1 = new javax.xml.namespace.QName("integer");
    public static final javax.xml.namespace.QName _value2 = new javax.xml.namespace.QName("real");
    public static final javax.xml.namespace.QName _value3 = new javax.xml.namespace.QName("double");
    public static final javax.xml.namespace.QName _value4 = new javax.xml.namespace.QName("string");
    public static final javax.xml.namespace.QName _value5 = new javax.xml.namespace.QName("boolean");
    public static final javax.xml.namespace.QName _value6 = new javax.xml.namespace.QName("anyURI");
    public static final javax.xml.namespace.QName _value7 = new javax.xml.namespace.QName("FileReference");
    public static final javax.xml.namespace.QName _value8 = new javax.xml.namespace.QName("MySpace_FileReference");
    public static final javax.xml.namespace.QName _value9 = new javax.xml.namespace.QName("MySpace_VOTableReference");
    public static final javax.xml.namespace.QName _value10 = new javax.xml.namespace.QName("RA");
    public static final javax.xml.namespace.QName _value11 = new javax.xml.namespace.QName("Dec");
    public static final javax.xml.namespace.QName _value12 = new javax.xml.namespace.QName("ADQL");
    public static final javax.xml.namespace.QName _value13 = new javax.xml.namespace.QName("IVORN");
    public static final ParameterTypes value1 = new ParameterTypes(_value1);
    public static final ParameterTypes value2 = new ParameterTypes(_value2);
    public static final ParameterTypes value3 = new ParameterTypes(_value3);
    public static final ParameterTypes value4 = new ParameterTypes(_value4);
    public static final ParameterTypes value5 = new ParameterTypes(_value5);
    public static final ParameterTypes value6 = new ParameterTypes(_value6);
    public static final ParameterTypes value7 = new ParameterTypes(_value7);
    public static final ParameterTypes value8 = new ParameterTypes(_value8);
    public static final ParameterTypes value9 = new ParameterTypes(_value9);
    public static final ParameterTypes value10 = new ParameterTypes(_value10);
    public static final ParameterTypes value11 = new ParameterTypes(_value11);
    public static final ParameterTypes value12 = new ParameterTypes(_value12);
    public static final ParameterTypes value13 = new ParameterTypes(_value13);
    public javax.xml.namespace.QName getValue() { return _value_;}
    public static ParameterTypes fromValue(javax.xml.namespace.QName value)
          throws java.lang.IllegalStateException {
        ParameterTypes enum = (ParameterTypes)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static ParameterTypes fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        try {
            return fromValue(new javax.xml.namespace.QName(value));
        } catch (Exception e) {
            throw new java.lang.IllegalStateException();
        }
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_.toString();}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
