/**
 * COOSYSSystem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package fr.u_strasbg.vizier.xml.VOTable_1_1_xsd;

public class COOSYSSystem implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected COOSYSSystem(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _eq_FK4 = "eq_FK4";
    public static final java.lang.String _eq_FK5 = "eq_FK5";
    public static final java.lang.String _ICRS = "ICRS";
    public static final java.lang.String _ecl_FK4 = "ecl_FK4";
    public static final java.lang.String _ecl_FK5 = "ecl_FK5";
    public static final java.lang.String _galactic = "galactic";
    public static final java.lang.String _supergalactic = "supergalactic";
    public static final java.lang.String _xy = "xy";
    public static final java.lang.String _barycentric = "barycentric";
    public static final java.lang.String _geo_app = "geo_app";
    public static final COOSYSSystem eq_FK4 = new COOSYSSystem(_eq_FK4);
    public static final COOSYSSystem eq_FK5 = new COOSYSSystem(_eq_FK5);
    public static final COOSYSSystem ICRS = new COOSYSSystem(_ICRS);
    public static final COOSYSSystem ecl_FK4 = new COOSYSSystem(_ecl_FK4);
    public static final COOSYSSystem ecl_FK5 = new COOSYSSystem(_ecl_FK5);
    public static final COOSYSSystem galactic = new COOSYSSystem(_galactic);
    public static final COOSYSSystem supergalactic = new COOSYSSystem(_supergalactic);
    public static final COOSYSSystem xy = new COOSYSSystem(_xy);
    public static final COOSYSSystem barycentric = new COOSYSSystem(_barycentric);
    public static final COOSYSSystem geo_app = new COOSYSSystem(_geo_app);
    public java.lang.String getValue() { return _value_;}
    public static COOSYSSystem fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        COOSYSSystem enum = (COOSYSSystem)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static COOSYSSystem fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
