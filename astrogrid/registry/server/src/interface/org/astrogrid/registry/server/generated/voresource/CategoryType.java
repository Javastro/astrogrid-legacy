/**
 * CategoryType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class CategoryType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected CategoryType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Other = "Other";
    public static final java.lang.String _Archive = "Archive";
    public static final java.lang.String _Bibliography = "Bibliography";
    public static final java.lang.String _Catalog = "Catalog";
    public static final java.lang.String _Journal = "Journal";
    public static final java.lang.String _Library = "Library";
    public static final java.lang.String _Simulation = "Simulation";
    public static final java.lang.String _Survey = "Survey";
    public static final java.lang.String _Transformation = "Transformation";
    public static final java.lang.String _Education = "Education";
    public static final java.lang.String _Outreach = "Outreach";
    public static final java.lang.String _EPOResource = "EPOResource";
    public static final java.lang.String _Animation = "Animation";
    public static final java.lang.String _Artwork = "Artwork";
    public static final java.lang.String _Background = "Background";
    public static final java.lang.String _BasicData = "BasicData";
    public static final java.lang.String _Historical = "Historical";
    public static final java.lang.String _Photographic = "Photographic";
    public static final java.lang.String _Press = "Press";
    public static final java.lang.String _Organisation = "Organisation";
    public static final java.lang.String _Project = "Project";
    public static final java.lang.String _Person = "Person";
    public static final CategoryType Other = new CategoryType(_Other);
    public static final CategoryType Archive = new CategoryType(_Archive);
    public static final CategoryType Bibliography = new CategoryType(_Bibliography);
    public static final CategoryType Catalog = new CategoryType(_Catalog);
    public static final CategoryType Journal = new CategoryType(_Journal);
    public static final CategoryType Library = new CategoryType(_Library);
    public static final CategoryType Simulation = new CategoryType(_Simulation);
    public static final CategoryType Survey = new CategoryType(_Survey);
    public static final CategoryType Transformation = new CategoryType(_Transformation);
    public static final CategoryType Education = new CategoryType(_Education);
    public static final CategoryType Outreach = new CategoryType(_Outreach);
    public static final CategoryType EPOResource = new CategoryType(_EPOResource);
    public static final CategoryType Animation = new CategoryType(_Animation);
    public static final CategoryType Artwork = new CategoryType(_Artwork);
    public static final CategoryType Background = new CategoryType(_Background);
    public static final CategoryType BasicData = new CategoryType(_BasicData);
    public static final CategoryType Historical = new CategoryType(_Historical);
    public static final CategoryType Photographic = new CategoryType(_Photographic);
    public static final CategoryType Press = new CategoryType(_Press);
    public static final CategoryType Organisation = new CategoryType(_Organisation);
    public static final CategoryType Project = new CategoryType(_Project);
    public static final CategoryType Person = new CategoryType(_Person);
    public java.lang.String getValue() { return _value_;}
    public static CategoryType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        CategoryType enum = (CategoryType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static CategoryType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
