/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: CoordRangeType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.generated.package.types.CoordFrameType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CoordRangeType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class CoordRangeType extends org.astrogrid.registry.generated.package.RegionType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a coordinate system frame
     *  
     */
    private org.astrogrid.registry.generated.package.types.CoordFrameType _coordFrame;

    /**
     * The range in longitude
     */
    private org.astrogrid.registry.generated.package.Long _long;

    /**
     * The range in latitude
     */
    private org.astrogrid.registry.generated.package.Lat _lat;


      //----------------/
     //- Constructors -/
    //----------------/

    public CoordRangeType() {
        super();
    } //-- org.astrogrid.registry.generated.package.CoordRangeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'coordFrame'. The field
     * 'coordFrame' has the following description: a coordinate
     * system frame
     *  
     * 
     * @return the value of field 'coordFrame'.
     */
    public org.astrogrid.registry.generated.package.types.CoordFrameType getCoordFrame()
    {
        return this._coordFrame;
    } //-- org.astrogrid.registry.generated.package.types.CoordFrameType getCoordFrame() 

    /**
     * Returns the value of field 'lat'. The field 'lat' has the
     * following description: The range in latitude
     * 
     * @return the value of field 'lat'.
     */
    public org.astrogrid.registry.generated.package.Lat getLat()
    {
        return this._lat;
    } //-- org.astrogrid.registry.generated.package.Lat getLat() 

    /**
     * Returns the value of field 'long'. The field 'long' has the
     * following description: The range in longitude
     * 
     * @return the value of field 'long'.
     */
    public org.astrogrid.registry.generated.package.Long getLong()
    {
        return this._long;
    } //-- org.astrogrid.registry.generated.package.Long getLong() 

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
    public void setCoordFrame(org.astrogrid.registry.generated.package.types.CoordFrameType coordFrame)
    {
        this._coordFrame = coordFrame;
    } //-- void setCoordFrame(org.astrogrid.registry.generated.package.types.CoordFrameType) 

    /**
     * Sets the value of field 'lat'. The field 'lat' has the
     * following description: The range in latitude
     * 
     * @param lat the value of field 'lat'.
     */
    public void setLat(org.astrogrid.registry.generated.package.Lat lat)
    {
        this._lat = lat;
    } //-- void setLat(org.astrogrid.registry.generated.package.Lat) 

    /**
     * Sets the value of field 'long'. The field 'long' has the
     * following description: The range in longitude
     * 
     * @param _long
     * @param long the value of field 'long'.
     */
    public void setLong(org.astrogrid.registry.generated.package.Long _long)
    {
        this._long = _long;
    } //-- void setLong(org.astrogrid.registry.generated.package.Long) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.CoordRangeType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.CoordRangeType.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
