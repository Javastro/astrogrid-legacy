/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: MaxImageSize.java,v 1.3 2004/03/05 09:52:02 KevinBenson Exp $
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
 * The largest image (in terms of pixels) that can be requested.
 *  
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:52:02 $
 */
public class MaxImageSize extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The maximum number of pixels in the longitude (R.A.) 
     *  direction 
     *  
     */
    private int _long;

    /**
     * keeps track of state for field: _long
     */
    private boolean _has_long;

    /**
     * The maximum number of pixels in the latitdude (Dec.) 
     *  direction 
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

    public MaxImageSize() {
        super();
    } //-- org.astrogrid.registry.beans.resource.sia.MaxImageSize()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'lat'. The field 'lat' has the
     * following description: The maximum number of pixels in the
     * latitdude (Dec.) 
     *  direction 
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
     * following description: The maximum number of pixels in the
     * longitude (R.A.) 
     *  direction 
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
     * following description: The maximum number of pixels in the
     * latitdude (Dec.) 
     *  direction 
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
     * following description: The maximum number of pixels in the
     * longitude (R.A.) 
     *  direction 
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
     * Method unmarshalMaxImageSize
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.sia.MaxImageSize unmarshalMaxImageSize(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.sia.MaxImageSize) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.sia.MaxImageSize.class, reader);
    } //-- org.astrogrid.registry.beans.resource.sia.MaxImageSize unmarshalMaxImageSize(java.io.Reader) 

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
