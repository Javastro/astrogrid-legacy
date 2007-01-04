/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CircleRegion.java,v 1.2 2007/01/04 16:26:25 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.v10.resource.dataservice.types.CoordFrame;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * a coverage area of the sky characterized by a central position
 *  and angular radius
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:25 $
 */
public class CircleRegion extends org.astrogrid.registry.beans.v10.resource.dataservice.Region 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a coordinate system frame
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.types.CoordFrame _coordFrame;

    /**
     * the position of the center of the circle/cone.
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.Position _center;

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

    public CircleRegion() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.CircleRegion()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Note: hashCode() has not been overriden
     * 
     * @param obj
     */
    public boolean equals(java.lang.Object obj)
    {
        if ( this == obj )
            return true;
        
        if (super.equals(obj)==false)
            return false;
        
        if (obj instanceof CircleRegion) {
        
            CircleRegion temp = (CircleRegion)obj;
            if (this._coordFrame != null) {
                if (temp._coordFrame == null) return false;
                else if (!(this._coordFrame.equals(temp._coordFrame))) 
                    return false;
            }
            else if (temp._coordFrame != null)
                return false;
            if (this._center != null) {
                if (temp._center == null) return false;
                else if (!(this._center.equals(temp._center))) 
                    return false;
            }
            else if (temp._center != null)
                return false;
            if (this._radius != temp._radius)
                return false;
            if (this._has_radius != temp._has_radius)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'center'. The field 'center' has
     * the following description: the position of the center of the
     * circle/cone.
     *  
     * 
     * @return the value of field 'center'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Position getCenter()
    {
        return this._center;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Position getCenter() 

    /**
     * Returns the value of field 'coordFrame'. The field
     * 'coordFrame' has the following description: a coordinate
     * system frame
     *  
     * 
     * @return the value of field 'coordFrame'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.types.CoordFrame getCoordFrame()
    {
        return this._coordFrame;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.CoordFrame getCoordFrame() 

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
     * Sets the value of field 'center'. The field 'center' has the
     * following description: the position of the center of the
     * circle/cone.
     *  
     * 
     * @param center the value of field 'center'.
     */
    public void setCenter(org.astrogrid.registry.beans.v10.resource.dataservice.Position center)
    {
        this._center = center;
    } //-- void setCenter(org.astrogrid.registry.beans.v10.resource.dataservice.Position) 

    /**
     * Sets the value of field 'coordFrame'. The field 'coordFrame'
     * has the following description: a coordinate system frame
     *  
     * 
     * @param coordFrame the value of field 'coordFrame'.
     */
    public void setCoordFrame(org.astrogrid.registry.beans.v10.resource.dataservice.types.CoordFrame coordFrame)
    {
        this._coordFrame = coordFrame;
    } //-- void setCoordFrame(org.astrogrid.registry.beans.v10.resource.dataservice.types.CoordFrame) 

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
     * Method unmarshalCircleRegion
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.CircleRegion unmarshalCircleRegion(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.CircleRegion) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.CircleRegion.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.CircleRegion unmarshalCircleRegion(java.io.Reader) 

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
