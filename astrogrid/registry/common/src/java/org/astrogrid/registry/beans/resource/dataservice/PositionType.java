/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: PositionType.java,v 1.5 2004/03/11 14:08:05 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

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
 * Class PositionType.
 * 
 * @version $Revision: 1.5 $ $Date: 2004/03/11 14:08:05 $
 */
public class PositionType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the longitude component of the sky position
     *  
     */
    private double _long;

    /**
     * keeps track of state for field: _long
     */
    private boolean _has_long;

    /**
     * the latitude component of the sky position
     *  
     */
    private double _lat;

    /**
     * keeps track of state for field: _lat
     */
    private boolean _has_lat;


      //----------------/
     //- Constructors -/
    //----------------/

    public PositionType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.dataservice.PositionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'lat'. The field 'lat' has the
     * following description: the latitude component of the sky
     * position
     *  
     * 
     * @return the value of field 'lat'.
     */
    public double getLat()
    {
        return this._lat;
    } //-- double getLat() 

    /**
     * Returns the value of field 'long'. The field 'long' has the
     * following description: the longitude component of the sky
     * position
     *  
     * 
     * @return the value of field 'long'.
     */
    public double getLong()
    {
        return this._long;
    } //-- double getLong() 

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
     * following description: the latitude component of the sky
     * position
     *  
     * 
     * @param lat the value of field 'lat'.
     */
    public void setLat(double lat)
    {
        this._lat = lat;
        this._has_lat = true;
    } //-- void setLat(double) 

    /**
     * Sets the value of field 'long'. The field 'long' has the
     * following description: the longitude component of the sky
     * position
     *  
     * 
     * @param _long
     * @param long the value of field 'long'.
     */
    public void setLong(double _long)
    {
        this._long = _long;
        this._has_long = true;
    } //-- void setLong(double) 

    /**
     * Method unmarshalPositionType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.PositionType unmarshalPositionType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.PositionType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.PositionType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.PositionType unmarshalPositionType(java.io.Reader) 

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
