/**
 * ContentLevelType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class ContentLevelType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ContentLevelType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _value1 = "General";
    public static final java.lang.String _value2 = "Elementary Education";
    public static final java.lang.String _value3 = "Middle School Education";
    public static final java.lang.String _value4 = "Secondary Education";
    public static final java.lang.String _value5 = "Community College";
    public static final java.lang.String _value6 = "University";
    public static final java.lang.String _value7 = "Research";
    public static final java.lang.String _value8 = "Amateur";
    public static final java.lang.String _value9 = "Informal Education";
    public static final ContentLevelType value1 = new ContentLevelType(_value1);
    public static final ContentLevelType value2 = new ContentLevelType(_value2);
    public static final ContentLevelType value3 = new ContentLevelType(_value3);
    public static final ContentLevelType value4 = new ContentLevelType(_value4);
    public static final ContentLevelType value5 = new ContentLevelType(_value5);
    public static final ContentLevelType value6 = new ContentLevelType(_value6);
    public static final ContentLevelType value7 = new ContentLevelType(_value7);
    public static final ContentLevelType value8 = new ContentLevelType(_value8);
    public static final ContentLevelType value9 = new ContentLevelType(_value9);
    public java.lang.String getValue() { return _value_;}
    public static ContentLevelType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        ContentLevelType enum = (ContentLevelType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static ContentLevelType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
