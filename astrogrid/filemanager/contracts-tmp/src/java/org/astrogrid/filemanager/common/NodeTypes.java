/**
 * NodeTypes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.filemanager.common;

public class NodeTypes implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected NodeTypes(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _FILE = "FILE";
    public static final java.lang.String _FOLDER = "FOLDER";
    public static final NodeTypes FILE = new NodeTypes(_FILE);
    public static final NodeTypes FOLDER = new NodeTypes(_FOLDER);
    public java.lang.String getValue() { return _value_;}
    public static NodeTypes fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        NodeTypes enum = (NodeTypes)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static NodeTypes fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
