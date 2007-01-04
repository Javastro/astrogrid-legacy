/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SkySize.java,v 1.2 2007/01/04 16:26:30 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.sia;

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
 * Class SkySize.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:30 $
 */
public class SkySize extends org.astrogrid.common.bean.BaseBean 
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

    public SkySize() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.sia.SkySize()


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
        
        if (obj instanceof SkySize) {
        
            SkySize temp = (SkySize)obj;
            if (this._long != temp._long)
                return false;
            if (this._has_long != temp._has_long)
                return false;
            if (this._lat != temp._lat)
                return false;
            if (this._has_lat != temp._has_lat)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

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
     * Method unmarshalSkySize
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.sia.SkySize unmarshalSkySize(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.sia.SkySize) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.sia.SkySize.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.sia.SkySize unmarshalSkySize(java.io.Reader) 

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
