/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: EllipseType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType;

/**
 * Class EllipseType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class EllipseType extends org.astrogrid.datacenter.adql.generated.ogsadai.CircleType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pos_angle_unit
     */
    private AngleUnitType _pos_angle_unit = AngleUnitType.valueOf("deg");

    /**
     * Field _minorRadius
     */
    private double _minorRadius;

    /**
     * keeps track of state for field: _minorRadius
     */
    private boolean _has_minorRadius;

    /**
     * Field _posAngle
     */
    private double _posAngle;

    /**
     * keeps track of state for field: _posAngle
     */
    private boolean _has_posAngle;


      //----------------/
     //- Constructors -/
    //----------------/

    public EllipseType() {
        super();
        setPos_angle_unit(AngleUnitType.valueOf("deg"));
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.EllipseType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'minorRadius'.
     * 
     * @return the value of field 'minorRadius'.
     */
    public double getMinorRadius()
    {
        return this._minorRadius;
    } //-- double getMinorRadius() 

    /**
     * Returns the value of field 'posAngle'.
     * 
     * @return the value of field 'posAngle'.
     */
    public double getPosAngle()
    {
        return this._posAngle;
    } //-- double getPosAngle() 

    /**
     * Returns the value of field 'pos_angle_unit'.
     * 
     * @return the value of field 'pos_angle_unit'.
     */
    public AngleUnitType getPos_angle_unit()
    {
        return this._pos_angle_unit;
    } //-- types.AngleUnitType getPos_angle_unit() 

    /**
     * Method hasMinorRadius
     */
    public boolean hasMinorRadius()
    {
        return this._has_minorRadius;
    } //-- boolean hasMinorRadius() 

    /**
     * Method hasPosAngle
     */
    public boolean hasPosAngle()
    {
        return this._has_posAngle;
    } //-- boolean hasPosAngle() 

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
     * Sets the value of field 'minorRadius'.
     * 
     * @param minorRadius the value of field 'minorRadius'.
     */
    public void setMinorRadius(double minorRadius)
    {
        this._minorRadius = minorRadius;
        this._has_minorRadius = true;
    } //-- void setMinorRadius(double) 

    /**
     * Sets the value of field 'posAngle'.
     * 
     * @param posAngle the value of field 'posAngle'.
     */
    public void setPosAngle(double posAngle)
    {
        this._posAngle = posAngle;
        this._has_posAngle = true;
    } //-- void setPosAngle(double) 

    /**
     * Sets the value of field 'pos_angle_unit'.
     * 
     * @param pos_angle_unit the value of field 'pos_angle_unit'.
     */
    public void setPos_angle_unit(AngleUnitType pos_angle_unit)
    {
        this._pos_angle_unit = pos_angle_unit;
    } //-- void setPos_angle_unit(types.AngleUnitType) 

    /**
     * Method unmarshalEllipseType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.EllipseType unmarshalEllipseType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.EllipseType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.EllipseType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.EllipseType unmarshalEllipseType(java.io.Reader) 

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
