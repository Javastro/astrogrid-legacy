/**
 * LINKContentrole.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package fr.u_strasbg.vizier.xml.VOTable_1_1_xsd;

public class LINKContentrole implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected LINKContentrole(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _query = "query";
    public static final java.lang.String _hints = "hints";
    public static final java.lang.String _doc = "doc";
    public static final java.lang.String _location = "location";
    public static final LINKContentrole query = new LINKContentrole(_query);
    public static final LINKContentrole hints = new LINKContentrole(_hints);
    public static final LINKContentrole doc = new LINKContentrole(_doc);
    public static final LINKContentrole location = new LINKContentrole(_location);
    public java.lang.String getValue() { return _value_;}
    public static LINKContentrole fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        LINKContentrole enum = (LINKContentrole)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static LINKContentrole fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
