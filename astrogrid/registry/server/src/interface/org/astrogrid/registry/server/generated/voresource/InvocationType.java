/**
 * InvocationType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class InvocationType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected InvocationType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Custom = "Custom";
    public static final java.lang.String _Extended = "Extended";
    public static final java.lang.String _WebService = "WebService";
    public static final java.lang.String _WebBrowser = "WebBrowser";
    public static final java.lang.String _GLUService = "GLUService";
    public static final InvocationType Custom = new InvocationType(_Custom);
    public static final InvocationType Extended = new InvocationType(_Extended);
    public static final InvocationType WebService = new InvocationType(_WebService);
    public static final InvocationType WebBrowser = new InvocationType(_WebBrowser);
    public static final InvocationType GLUService = new InvocationType(_GLUService);
    public java.lang.String getValue() { return _value_;}
    public static InvocationType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        InvocationType enum = (InvocationType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static InvocationType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
