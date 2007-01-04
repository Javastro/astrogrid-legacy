/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ImageSize.java,v 1.2 2007/01/04 16:26:30 clq2 Exp $
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
 * Class ImageSize.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:30 $
 */
public class ImageSize extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The image size in the longitude (R.A.) direction in pixels
     *  
     */
    private int _long;

    /**
     * keeps track of state for field: _long
     */
    private boolean _has_long;

    /**
     * The image size in the latitdude (Dec.) direction in pixels
     *  
     */
    private int _lat;

    /**
     * keeps track of state for field: _lat
     */
    private boolean _has_lat;


      //----------------/
     //- Constructors -/
    //----------------/

    public ImageSize() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.sia.ImageSize()


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
        
        if (obj instanceof ImageSize) {
        
            ImageSize temp = (ImageSize)obj;
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
     * following description: The image size in the latitdude
     * (Dec.) direction in pixels
     *  
     * 
     * @return the value of field 'lat'.
     */
    public int getLat()
    {
        return this._lat;
    } //-- int getLat() 

    /**
     * Returns the value of field 'long'. The field 'long' has the
     * following description: The image size in the longitude
     * (R.A.) direction in pixels
     *  
     * 
     * @return the value of field 'long'.
     */
    public int getLong()
    {
        return this._long;
    } //-- int getLong() 

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
     * following description: The image size in the latitdude
     * (Dec.) direction in pixels
     *  
     * 
     * @param lat the value of field 'lat'.
     */
    public void setLat(int lat)
    {
        this._lat = lat;
        this._has_lat = true;
    } //-- void setLat(int) 

    /**
     * Sets the value of field 'long'. The field 'long' has the
     * following description: The image size in the longitude
     * (R.A.) direction in pixels
     *  
     * 
     * @param _long
     * @param long the value of field 'long'.
     */
    public void setLong(int _long)
    {
        this._long = _long;
        this._has_long = true;
    } //-- void setLong(int) 

    /**
     * Method unmarshalImageSize
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.sia.ImageSize unmarshalImageSize(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.sia.ImageSize) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.sia.ImageSize.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.sia.ImageSize unmarshalImageSize(java.io.Reader) 

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
