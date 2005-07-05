/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CoordRange.java,v 1.2 2005/07/05 08:26:55 clq2 Exp $
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
 * a coverage area of the sky characterized by a range of
 *  longitude and latitude.
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:55 $
 */
public class CoordRange extends org.astrogrid.registry.beans.v10.resource.dataservice.Region 
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
     * The range in longitude
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.AngleRange _long;

    /**
     * The range in latitude
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.AngleRange _lat;


      //----------------/
     //- Constructors -/
    //----------------/

    public CoordRange() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.CoordRange()


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
        
        if (obj instanceof CoordRange) {
        
            CoordRange temp = (CoordRange)obj;
            if (this._coordFrame != null) {
                if (temp._coordFrame == null) return false;
                else if (!(this._coordFrame.equals(temp._coordFrame))) 
                    return false;
            }
            else if (temp._coordFrame != null)
                return false;
            if (this._long != null) {
                if (temp._long == null) return false;
                else if (!(this._long.equals(temp._long))) 
                    return false;
            }
            else if (temp._long != null)
                return false;
            if (this._lat != null) {
                if (temp._lat == null) return false;
                else if (!(this._lat.equals(temp._lat))) 
                    return false;
            }
            else if (temp._lat != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

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
     * Returns the value of field 'lat'. The field 'lat' has the
     * following description: The range in latitude
     * 
     * @return the value of field 'lat'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.AngleRange getLat()
    {
        return this._lat;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.AngleRange getLat() 

    /**
     * Returns the value of field 'long'. The field 'long' has the
     * following description: The range in longitude
     * 
     * @return the value of field 'long'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.AngleRange getLong()
    {
        return this._long;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.AngleRange getLong() 

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
     * Sets the value of field 'lat'. The field 'lat' has the
     * following description: The range in latitude
     * 
     * @param lat the value of field 'lat'.
     */
    public void setLat(org.astrogrid.registry.beans.v10.resource.dataservice.AngleRange lat)
    {
        this._lat = lat;
    } //-- void setLat(org.astrogrid.registry.beans.v10.resource.dataservice.AngleRange) 

    /**
     * Sets the value of field 'long'. The field 'long' has the
     * following description: The range in longitude
     * 
     * @param _long
     * @param long the value of field 'long'.
     */
    public void setLong(org.astrogrid.registry.beans.v10.resource.dataservice.AngleRange _long)
    {
        this._long = _long;
    } //-- void setLong(org.astrogrid.registry.beans.v10.resource.dataservice.AngleRange) 

    /**
     * Method unmarshalCoordRange
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.CoordRange unmarshalCoordRange(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.CoordRange) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.CoordRange.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.CoordRange unmarshalCoordRange(java.io.Reader) 

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
