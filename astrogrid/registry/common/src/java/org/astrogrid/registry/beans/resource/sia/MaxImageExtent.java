/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: MaxImageExtent.java,v 1.4 2004/03/09 09:45:24 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.sia;

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

/**
 * The maximum image query region size, expressed in decimal
 *  degrees. A value of 360 degrees indicates that there is no
 * limit
 *  and the entire data collection (entire sky) can be requested.
 *  
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/09 09:45:24 $
 */
public class MaxImageExtent extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The maximum size in the longitude (R.A.) direction 
     *  
     */
    private float _long;

    /**
     * keeps track of state for field: _long
     */
    private boolean _has_long;

    /**
     * The maximum size in the latitdude (Dec.) direction 
     *  
     */
    private float _lat;

    /**
     * keeps track of state for field: _lat
     */
    private boolean _has_lat;


      //----------------/
     //- Constructors -/
    //----------------/

    public MaxImageExtent() {
        super();
    } //-- org.astrogrid.registry.beans.resource.sia.MaxImageExtent()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'lat'. The field 'lat' has the
     * following description: The maximum size in the latitdude
     * (Dec.) direction 
     *  
     * 
     * @return the value of field 'lat'.
     */
    public float getLat()
    {
        return this._lat;
    } //-- float getLat() 

    /**
     * Returns the value of field 'long'. The field 'long' has the
     * following description: The maximum size in the longitude
     * (R.A.) direction 
     *  
     * 
     * @return the value of field 'long'.
     */
    public float getLong()
    {
        return this._long;
    } //-- float getLong() 

    /**
     * Method hasLat
     */
    public boolean hasLat()
    {
        return this._has_lat;
    } //-- boolean hasLat() 

    /**
     * Method hasLong
     */
    public boolean hasLong()
    {
        return this._has_long;
    } //-- boolean hasLong() 

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
     * Sets the value of field 'lat'. The field 'lat' has the
     * following description: The maximum size in the latitdude
     * (Dec.) direction 
     *  
     * 
     * @param lat the value of field 'lat'.
     */
    public void setLat(float lat)
    {
        this._lat = lat;
        this._has_lat = true;
    } //-- void setLat(float) 

    /**
     * Sets the value of field 'long'. The field 'long' has the
     * following description: The maximum size in the longitude
     * (R.A.) direction 
     *  
     * 
     * @param _long
     * @param long the value of field 'long'.
     */
    public void setLong(float _long)
    {
        this._long = _long;
        this._has_long = true;
    } //-- void setLong(float) 

    /**
     * Method unmarshalMaxImageExtent
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.sia.MaxImageExtent unmarshalMaxImageExtent(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.sia.MaxImageExtent) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.sia.MaxImageExtent.class, reader);
    } //-- org.astrogrid.registry.beans.resource.sia.MaxImageExtent unmarshalMaxImageExtent(java.io.Reader) 

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
