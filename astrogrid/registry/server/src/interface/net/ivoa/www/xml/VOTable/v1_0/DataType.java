/**
 * DataType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.www.xml.VOTable.v1_0;

public class DataType implements java.io.Serializable {
    private org.apache.axis.types.NMToken _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected DataType(org.apache.axis.types.NMToken value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final org.apache.axis.types.NMToken _value1 = new org.apache.axis.types.NMToken("boolean");
    public static final org.apache.axis.types.NMToken _value2 = new org.apache.axis.types.NMToken("bit");
    public static final org.apache.axis.types.NMToken _value3 = new org.apache.axis.types.NMToken("unsignedByte");
    public static final org.apache.axis.types.NMToken _value4 = new org.apache.axis.types.NMToken("short");
    public static final org.apache.axis.types.NMToken _value5 = new org.apache.axis.types.NMToken("int");
    public static final org.apache.axis.types.NMToken _value6 = new org.apache.axis.types.NMToken("long");
    public static final org.apache.axis.types.NMToken _value7 = new org.apache.axis.types.NMToken("char");
    public static final org.apache.axis.types.NMToken _value8 = new org.apache.axis.types.NMToken("unicodeChar");
    public static final org.apache.axis.types.NMToken _value9 = new org.apache.axis.types.NMToken("float");
    public static final org.apache.axis.types.NMToken _value10 = new org.apache.axis.types.NMToken("double");
    public static final org.apache.axis.types.NMToken _value11 = new org.apache.axis.types.NMToken("floatComplex");
    public static final org.apache.axis.types.NMToken _value12 = new org.apache.axis.types.NMToken("doubleComplex");
    public static final DataType value1 = new DataType(_value1);
    public static final DataType value2 = new DataType(_value2);
    public static final DataType value3 = new DataType(_value3);
    public static final DataType value4 = new DataType(_value4);
    public static final DataType value5 = new DataType(_value5);
    public static final DataType value6 = new DataType(_value6);
    public static final DataType value7 = new DataType(_value7);
    public static final DataType value8 = new DataType(_value8);
    public static final DataType value9 = new DataType(_value9);
    public static final DataType value10 = new DataType(_value10);
    public static final DataType value11 = new DataType(_value11);
    public static final DataType value12 = new DataType(_value12);
    public org.apache.axis.types.NMToken getValue() { return _value_;}
    public static DataType fromValue(org.apache.axis.types.NMToken value)
          throws java.lang.IllegalStateException {
        DataType enum = (DataType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static DataType fromString(java.lang.String value)
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
