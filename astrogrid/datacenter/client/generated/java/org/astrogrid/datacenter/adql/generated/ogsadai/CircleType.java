/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CircleType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
//import types.PosUnitType;

/**
 * Class CircleType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class CircleType extends org.astrogrid.datacenter.adql.generated.ogsadai.ShapeType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _radius_unit
     */
    private PosUnitType _radius_unit = PosUnitType.valueOf("deg");

    /**
     * Field _center
     */
    private CoordsType _center;

    /**
     * Field _radius
     */
    private double _radius;

    /**
     * keeps track of state for field: _radius
     */
    private boolean _has_radius;


      //----------------/
     //- Constructors -/
    //----------------/

    public CircleType() {
        super();
        setRadius_unit(PosUnitType.valueOf("deg"));
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CircleType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'center'.
     * 
     * @return the value of field 'center'.
     */
    public CoordsType getCenter()
    {
        return this._center;
    } //-- CoordsType getCenter() 

    /**
     * Returns the value of field 'radius'.
     * 
     * @return the value of field 'radius'.
     */
    public double getRadius()
    {
        return this._radius;
    } //-- double getRadius() 

    /**
     * Returns the value of field 'radius_unit'.
     * 
     * @return the value of field 'radius_unit'.
     */
    public PosUnitType getRadius_unit()
    {
        return this._radius_unit;
    } //-- PosUnitType getRadius_unit() 

    /**
     * Method hasRadius
     */
    public boolean hasRadius()
    {
        return this._has_radius;
    } //-- boolean hasRadius() 

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
     * Sets the value of field 'center'.
     * 
     * @param center the value of field 'center'.
     */
    public void setCenter(CoordsType center)
    {
        this._center = center;
    } //-- void setCenter(CoordsType) 

    /**
     * Sets the value of field 'radius'.
     * 
     * @param radius the value of field 'radius'.
     */
    public void setRadius(double radius)
    {
        this._radius = radius;
        this._has_radius = true;
    } //-- void setRadius(double) 

    /**
     * Sets the value of field 'radius_unit'.
     * 
     * @param radius_unit the value of field 'radius_unit'.
     */
    public void setRadius_unit(PosUnitType radius_unit)
    {
        this._radius_unit = radius_unit;
    } //-- void setRadius_unit(PosUnitType) 

    /**
     * Method unmarshalCircleType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.CircleType unmarshalCircleType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.CircleType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.CircleType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CircleType unmarshalCircleType(java.io.Reader) 

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
