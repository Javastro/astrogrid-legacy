/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CircleRegionType.java,v 1.3 2004/03/05 09:51:59 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.resource.dataservice.types.CoordFrameType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CircleRegionType.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:51:59 $
 */
public class CircleRegionType extends org.astrogrid.registry.beans.resource.dataservice.RegionType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a coordinate system frame
     *  
     */
    private org.astrogrid.registry.beans.resource.dataservice.types.CoordFrameType _coordFrame;

    /**
     * the position of the center of some region of the sky.
     *  
     */
    private org.astrogrid.registry.beans.resource.dataservice.PositionType _centerPosition;

    /**
     * the radius of the circle in degrees.
     *  
     */
    private float _radius;

    /**
     * keeps track of state for field: _radius
     */
    private boolean _has_radius;


      //----------------/
     //- Constructors -/
    //----------------/

    public CircleRegionType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.dataservice.CircleRegionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'centerPosition'. The field
     * 'centerPosition' has the following description: the position
     * of the center of some region of the sky.
     *  
     * 
     * @return the value of field 'centerPosition'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.PositionType getCenterPosition()
    {
        return this._centerPosition;
    } //-- org.astrogrid.registry.beans.resource.dataservice.PositionType getCenterPosition() 

    /**
     * Returns the value of field 'coordFrame'. The field
     * 'coordFrame' has the following description: a coordinate
     * system frame
     *  
     * 
     * @return the value of field 'coordFrame'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.types.CoordFrameType getCoordFrame()
    {
        return this._coordFrame;
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.CoordFrameType getCoordFrame() 

    /**
     * Returns the value of field 'radius'. The field 'radius' has
     * the following description: the radius of the circle in
     * degrees.
     *  
     * 
     * @return the value of field 'radius'.
     */
    public float getRadius()
    {
        return this._radius;
    } //-- float getRadius() 

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
     * Sets the value of field 'centerPosition'. The field
     * 'centerPosition' has the following description: the position
     * of the center of some region of the sky.
     *  
     * 
     * @param centerPosition the value of field 'centerPosition'.
     */
    public void setCenterPosition(org.astrogrid.registry.beans.resource.dataservice.PositionType centerPosition)
    {
        this._centerPosition = centerPosition;
    } //-- void setCenterPosition(org.astrogrid.registry.beans.resource.dataservice.PositionType) 

    /**
     * Sets the value of field 'coordFrame'. The field 'coordFrame'
     * has the following description: a coordinate system frame
     *  
     * 
     * @param coordFrame the value of field 'coordFrame'.
     */
    public void setCoordFrame(org.astrogrid.registry.beans.resource.dataservice.types.CoordFrameType coordFrame)
    {
        this._coordFrame = coordFrame;
    } //-- void setCoordFrame(org.astrogrid.registry.beans.resource.dataservice.types.CoordFrameType) 

    /**
     * Sets the value of field 'radius'. The field 'radius' has the
     * following description: the radius of the circle in degrees.
     *  
     * 
     * @param radius the value of field 'radius'.
     */
    public void setRadius(float radius)
    {
        this._radius = radius;
        this._has_radius = true;
    } //-- void setRadius(float) 

    /**
     * Method unmarshalCircleRegionType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.CircleRegionType unmarshalCircleRegionType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.CircleRegionType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.CircleRegionType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.CircleRegionType unmarshalCircleRegionType(java.io.Reader) 

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
