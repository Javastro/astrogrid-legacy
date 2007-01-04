/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SIACapability.java,v 1.2 2007/01/04 16:26:30 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.sia;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.v10.resource.sia.types.ImageServiceType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * The capabilities of an SIA implementation. 
 *  
 *  Editor's Notes: 
 *  * This is a prototype definition to
 *  illustrate how to extend the schema to a specific 
 *  standard interface.
 *  * Staging information is not included yet. 
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:30 $
 */
public class SIACapability extends org.astrogrid.registry.beans.v10.resource.sia.SIACapRestriction 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The class of image service: Cutout, Mosaic, Atlas, Pointed
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.sia.types.ImageServiceType _imageServiceType;

    /**
     * The maximum image query region size, expressed in 
     *  decimal degrees. A value of 360 degrees indicates that 
     *  there is no limit and the entire data collection 
     *  (entire sky) can be queried. 
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.sia.SkySize _maxQueryRegionSize;

    /**
     * The maximum image query region size, expressed in 
     *  decimal degrees. A value of 360 degrees indicates 
     *  that there is no limit and the entire data collection 
     *  (entire sky) can be requested.
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.sia.SkySize _maxImageExtent;

    /**
     * The largest image (in terms of pixels) that can be 
     *  requested. 
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.sia.ImageSize _maxImageSize;

    /**
     * The maximum image file size in bytes.
     *  
     */
    private int _maxFileSize;

    /**
     * keeps track of state for field: _maxFileSize
     */
    private boolean _has_maxFileSize;

    /**
     * The largest number of records that the Image Query web
     *  method will return. 
     *  
     */
    private int _maxRecords;

    /**
     * keeps track of state for field: _maxRecords
     */
    private boolean _has_maxRecords;


      //----------------/
     //- Constructors -/
    //----------------/

    public SIACapability() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.sia.SIACapability()


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
        
        if (obj instanceof SIACapability) {
        
            SIACapability temp = (SIACapability)obj;
            if (this._imageServiceType != null) {
                if (temp._imageServiceType == null) return false;
                else if (!(this._imageServiceType.equals(temp._imageServiceType))) 
                    return false;
            }
            else if (temp._imageServiceType != null)
                return false;
            if (this._maxQueryRegionSize != null) {
                if (temp._maxQueryRegionSize == null) return false;
                else if (!(this._maxQueryRegionSize.equals(temp._maxQueryRegionSize))) 
                    return false;
            }
            else if (temp._maxQueryRegionSize != null)
                return false;
            if (this._maxImageExtent != null) {
                if (temp._maxImageExtent == null) return false;
                else if (!(this._maxImageExtent.equals(temp._maxImageExtent))) 
                    return false;
            }
            else if (temp._maxImageExtent != null)
                return false;
            if (this._maxImageSize != null) {
                if (temp._maxImageSize == null) return false;
                else if (!(this._maxImageSize.equals(temp._maxImageSize))) 
                    return false;
            }
            else if (temp._maxImageSize != null)
                return false;
            if (this._maxFileSize != temp._maxFileSize)
                return false;
            if (this._has_maxFileSize != temp._has_maxFileSize)
                return false;
            if (this._maxRecords != temp._maxRecords)
                return false;
            if (this._has_maxRecords != temp._has_maxRecords)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'imageServiceType'. The field
     * 'imageServiceType' has the following description: The class
     * of image service: Cutout, Mosaic, Atlas, Pointed
     *  
     * 
     * @return the value of field 'imageServiceType'.
     */
    public org.astrogrid.registry.beans.v10.resource.sia.types.ImageServiceType getImageServiceType()
    {
        return this._imageServiceType;
    } //-- org.astrogrid.registry.beans.v10.resource.sia.types.ImageServiceType getImageServiceType() 

    /**
     * Returns the value of field 'maxFileSize'. The field
     * 'maxFileSize' has the following description: The maximum
     * image file size in bytes.
     *  
     * 
     * @return the value of field 'maxFileSize'.
     */
    public int getMaxFileSize()
    {
        return this._maxFileSize;
    } //-- int getMaxFileSize() 

    /**
     * Returns the value of field 'maxImageExtent'. The field
     * 'maxImageExtent' has the following description: The maximum
     * image query region size, expressed in 
     *  decimal degrees. A value of 360 degrees indicates 
     *  that there is no limit and the entire data collection 
     *  (entire sky) can be requested.
     *  
     * 
     * @return the value of field 'maxImageExtent'.
     */
    public org.astrogrid.registry.beans.v10.resource.sia.SkySize getMaxImageExtent()
    {
        return this._maxImageExtent;
    } //-- org.astrogrid.registry.beans.v10.resource.sia.SkySize getMaxImageExtent() 

    /**
     * Returns the value of field 'maxImageSize'. The field
     * 'maxImageSize' has the following description: The largest
     * image (in terms of pixels) that can be 
     *  requested. 
     *  
     * 
     * @return the value of field 'maxImageSize'.
     */
    public org.astrogrid.registry.beans.v10.resource.sia.ImageSize getMaxImageSize()
    {
        return this._maxImageSize;
    } //-- org.astrogrid.registry.beans.v10.resource.sia.ImageSize getMaxImageSize() 

    /**
     * Returns the value of field 'maxQueryRegionSize'. The field
     * 'maxQueryRegionSize' has the following description: The
     * maximum image query region size, expressed in 
     *  decimal degrees. A value of 360 degrees indicates that 
     *  there is no limit and the entire data collection 
     *  (entire sky) can be queried. 
     *  
     * 
     * @return the value of field 'maxQueryRegionSize'.
     */
    public org.astrogrid.registry.beans.v10.resource.sia.SkySize getMaxQueryRegionSize()
    {
        return this._maxQueryRegionSize;
    } //-- org.astrogrid.registry.beans.v10.resource.sia.SkySize getMaxQueryRegionSize() 

    /**
     * Returns the value of field 'maxRecords'. The field
     * 'maxRecords' has the following description: The largest
     * number of records that the Image Query web
     *  method will return. 
     *  
     * 
     * @return the value of field 'maxRecords'.
     */
    public int getMaxRecords()
    {
        return this._maxRecords;
    } //-- int getMaxRecords() 

    /**
     * Method hasMaxFileSize
     */
    public boolean hasMaxFileSize()
    {
        return this._has_maxFileSize;
    } //-- boolean hasMaxFileSize() 

    /**
     * Method hasMaxRecords
     */
    public boolean hasMaxRecords()
    {
        return this._has_maxRecords;
    } //-- boolean hasMaxRecords() 

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
     * Sets the value of field 'imageServiceType'. The field
     * 'imageServiceType' has the following description: The class
     * of image service: Cutout, Mosaic, Atlas, Pointed
     *  
     * 
     * @param imageServiceType the value of field 'imageServiceType'
     */
    public void setImageServiceType(org.astrogrid.registry.beans.v10.resource.sia.types.ImageServiceType imageServiceType)
    {
        this._imageServiceType = imageServiceType;
    } //-- void setImageServiceType(org.astrogrid.registry.beans.v10.resource.sia.types.ImageServiceType) 

    /**
     * Sets the value of field 'maxFileSize'. The field
     * 'maxFileSize' has the following description: The maximum
     * image file size in bytes.
     *  
     * 
     * @param maxFileSize the value of field 'maxFileSize'.
     */
    public void setMaxFileSize(int maxFileSize)
    {
        this._maxFileSize = maxFileSize;
        this._has_maxFileSize = true;
    } //-- void setMaxFileSize(int) 

    /**
     * Sets the value of field 'maxImageExtent'. The field
     * 'maxImageExtent' has the following description: The maximum
     * image query region size, expressed in 
     *  decimal degrees. A value of 360 degrees indicates 
     *  that there is no limit and the entire data collection 
     *  (entire sky) can be requested.
     *  
     * 
     * @param maxImageExtent the value of field 'maxImageExtent'.
     */
    public void setMaxImageExtent(org.astrogrid.registry.beans.v10.resource.sia.SkySize maxImageExtent)
    {
        this._maxImageExtent = maxImageExtent;
    } //-- void setMaxImageExtent(org.astrogrid.registry.beans.v10.resource.sia.SkySize) 

    /**
     * Sets the value of field 'maxImageSize'. The field
     * 'maxImageSize' has the following description: The largest
     * image (in terms of pixels) that can be 
     *  requested. 
     *  
     * 
     * @param maxImageSize the value of field 'maxImageSize'.
     */
    public void setMaxImageSize(org.astrogrid.registry.beans.v10.resource.sia.ImageSize maxImageSize)
    {
        this._maxImageSize = maxImageSize;
    } //-- void setMaxImageSize(org.astrogrid.registry.beans.v10.resource.sia.ImageSize) 

    /**
     * Sets the value of field 'maxQueryRegionSize'. The field
     * 'maxQueryRegionSize' has the following description: The
     * maximum image query region size, expressed in 
     *  decimal degrees. A value of 360 degrees indicates that 
     *  there is no limit and the entire data collection 
     *  (entire sky) can be queried. 
     *  
     * 
     * @param maxQueryRegionSize the value of field
     * 'maxQueryRegionSize'.
     */
    public void setMaxQueryRegionSize(org.astrogrid.registry.beans.v10.resource.sia.SkySize maxQueryRegionSize)
    {
        this._maxQueryRegionSize = maxQueryRegionSize;
    } //-- void setMaxQueryRegionSize(org.astrogrid.registry.beans.v10.resource.sia.SkySize) 

    /**
     * Sets the value of field 'maxRecords'. The field 'maxRecords'
     * has the following description: The largest number of records
     * that the Image Query web
     *  method will return. 
     *  
     * 
     * @param maxRecords the value of field 'maxRecords'.
     */
    public void setMaxRecords(int maxRecords)
    {
        this._maxRecords = maxRecords;
        this._has_maxRecords = true;
    } //-- void setMaxRecords(int) 

    /**
     * Method unmarshalSIACapability
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.sia.SIACapability unmarshalSIACapability(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.sia.SIACapability) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.sia.SIACapability.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.sia.SIACapability unmarshalSIACapability(java.io.Reader) 

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
