/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CoordValueType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CoordValueType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class CoordValueType extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pos_unit
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType _pos_unit = org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType.valueOf("deg");

    /**
     * Field _value
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble _value;

    /**
     * Field _value60
     */
    private java.lang.String _value60;

    /**
     * Field _reference
     */
    private java.lang.Object _reference;


      //----------------/
     //- Constructors -/
    //----------------/

    public CoordValueType() {
        super();
        setPos_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType.valueOf("deg"));
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordValueType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'pos_unit'.
     * 
     * @return the value of field 'pos_unit'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType getPos_unit()
    {
        return this._pos_unit;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType getPos_unit() 

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
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble getValue()
    {
        return this._value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble getValue() 

    /**
     * Returns the value of field 'value60'.
     * 
     * @return the value of field 'value60'.
     */
    public java.lang.String getValue60()
    {
        return this._value60;
    } //-- java.lang.String getValue60() 

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
     * Sets the value of field 'pos_unit'.
     * 
     * @param pos_unit the value of field 'pos_unit'.
     */
    public void setPos_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType pos_unit)
    {
        this._pos_unit = pos_unit;
    } //-- void setPos_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType) 

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
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble value)
    {
        this._value = value;
    } //-- void setValue(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble) 

    /**
     * Sets the value of field 'value60'.
     * 
     * @param value60 the value of field 'value60'.
     */
    public void setValue60(java.lang.String value60)
    {
        this._value60 = value60;
    } //-- void setValue60(java.lang.String) 

    /**
     * Method unmarshalCoordValueType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.CoordValueType unmarshalCoordValueType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.CoordValueType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.CoordValueType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordValueType unmarshalCoordValueType(java.io.Reader) 

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
