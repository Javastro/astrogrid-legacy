/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CoordSpectralValueType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CoordSpectralValueType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class CoordSpectralValueType extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _spectral_unit
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType _spectral_unit = org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType.valueOf("Hz");

    /**
     * Field _value
     */
    private double _value;

    /**
     * keeps track of state for field: _value
     */
    private boolean _has_value;

    /**
     * Field _reference
     */
    private java.lang.Object _reference;


      //----------------/
     //- Constructors -/
    //----------------/

    public CoordSpectralValueType() {
        super();
        setSpectral_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType.valueOf("Hz"));
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordSpectralValueType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'reference'.
     * 
     * @return the value of field 'reference'.
     */
    public java.lang.Object getReference()
    {
        return this._reference;
    } //-- java.lang.Object getReference() 

    /**
     * Returns the value of field 'spectral_unit'.
     * 
     * @return the value of field 'spectral_unit'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType getSpectral_unit()
    {
        return this._spectral_unit;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType getSpectral_unit() 

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public double getValue()
    {
        return this._value;
    } //-- double getValue() 

    /**
     * Method hasValue
     */
    public boolean hasValue()
    {
        return this._has_value;
    } //-- boolean hasValue() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'reference'.
     * 
     * @param reference the value of field 'reference'.
     */
    public void setReference(java.lang.Object reference)
    {
        this._reference = reference;
    } //-- void setReference(java.lang.Object) 

    /**
     * Sets the value of field 'spectral_unit'.
     * 
     * @param spectral_unit the value of field 'spectral_unit'.
     */
    public void setSpectral_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType spectral_unit)
    {
        this._spectral_unit = spectral_unit;
    } //-- void setSpectral_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType) 

    /**
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(double value)
    {
        this._value = value;
        this._has_value = true;
    } //-- void setValue(double) 

    /**
     * Method unmarshalCoordSpectralValueType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.CoordSpectralValueType unmarshalCoordSpectralValueType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.CoordSpectralValueType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.CoordSpectralValueType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordSpectralValueType unmarshalCoordSpectralValueType(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
