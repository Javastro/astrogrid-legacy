/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Position.java,v 1.2 2007/01/04 16:26:26 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.dataservice;

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
 * Class Position.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:26 $
 */
public class Position extends org.astrogrid.common.bean.BaseBean 
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

    public Position() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Position()


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
        
        if (obj instanceof Position) {
        
            Position temp = (Position)obj;
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
     * Method unmarshalPosition
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.Position unmarshalPosition(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Position) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.Position.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Position unmarshalPosition(java.io.Reader) 

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
