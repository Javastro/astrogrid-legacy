/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SectorType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
import org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType;

/**
 * Class SectorType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class SectorType extends org.astrogrid.datacenter.adql.generated.ogsadai.ShapeType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pos_angle_unit
     */
    private PosUnitType _pos_angle_unit = PosUnitType.valueOf("deg");

    /**
     * Field _position
     */
    private CoordsType _position;

    /**
     * Field _posAngle1
     */
    private double _posAngle1;

    /**
     * keeps track of state for field: _posAngle1
     */
    private boolean _has_posAngle1;

    /**
     * Field _posAngle2
     */
    private double _posAngle2;

    /**
     * keeps track of state for field: _posAngle2
     */
    private boolean _has_posAngle2;


      //----------------/
     //- Constructors -/
    //----------------/

    public SectorType() {
        super();
        setPos_angle_unit(PosUnitType.valueOf("deg"));
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.SectorType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'posAngle1'.
     * 
     * @return the value of field 'posAngle1'.
     */
    public double getPosAngle1()
    {
        return this._posAngle1;
    } //-- double getPosAngle1() 

    /**
     * Returns the value of field 'posAngle2'.
     * 
     * @return the value of field 'posAngle2'.
     */
    public double getPosAngle2()
    {
        return this._posAngle2;
    } //-- double getPosAngle2() 

    /**
     * Returns the value of field 'pos_angle_unit'.
     * 
     * @return the value of field 'pos_angle_unit'.
     */
    public PosUnitType getPos_angle_unit()
    {
        return this._pos_angle_unit;
    } //-- PosUnitType getPos_angle_unit() 

    /**
     * Returns the value of field 'position'.
     * 
     * @return the value of field 'position'.
     */
    public CoordsType getPosition()
    {
        return this._position;
    } //-- CoordsType getPosition() 

    /**
     * Method hasPosAngle1
     */
    public boolean hasPosAngle1()
    {
        return this._has_posAngle1;
    } //-- boolean hasPosAngle1() 

    /**
     * Method hasPosAngle2
     */
    public boolean hasPosAngle2()
    {
        return this._has_posAngle2;
    } //-- boolean hasPosAngle2() 

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
     * Sets the value of field 'posAngle1'.
     * 
     * @param posAngle1 the value of field 'posAngle1'.
     */
    public void setPosAngle1(double posAngle1)
    {
        this._posAngle1 = posAngle1;
        this._has_posAngle1 = true;
    } //-- void setPosAngle1(double) 

    /**
     * Sets the value of field 'posAngle2'.
     * 
     * @param posAngle2 the value of field 'posAngle2'.
     */
    public void setPosAngle2(double posAngle2)
    {
        this._posAngle2 = posAngle2;
        this._has_posAngle2 = true;
    } //-- void setPosAngle2(double) 

    /**
     * Sets the value of field 'pos_angle_unit'.
     * 
     * @param pos_angle_unit the value of field 'pos_angle_unit'.
     */
    public void setPos_angle_unit(PosUnitType pos_angle_unit)
    {
        this._pos_angle_unit = pos_angle_unit;
    } //-- void setPos_angle_unit(PosUnitType) 

    /**
     * Sets the value of field 'position'.
     * 
     * @param position the value of field 'position'.
     */
    public void setPosition(CoordsType position)
    {
        this._position = position;
    } //-- void setPosition(CoordsType) 

    /**
     * Method unmarshalSectorType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.SectorType unmarshalSectorType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.SectorType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.SectorType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.SectorType unmarshalSectorType(java.io.Reader) 

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
