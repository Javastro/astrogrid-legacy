/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: WavelengthRangeType.java,v 1.2 2005/07/05 08:26:57 clq2 Exp $
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
 * Class WavelengthRangeType.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:57 $
 */
public class WavelengthRangeType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The minimum wavelength
     *  
     */
    private float _min;

    /**
     * keeps track of state for field: _min
     */
    private boolean _has_min;

    /**
     * The maximum wavelength
     *  
     */
    private float _max;

    /**
     * keeps track of state for field: _max
     */
    private boolean _has_max;


      //----------------/
     //- Constructors -/
    //----------------/

    public WavelengthRangeType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.dataservice.WavelengthRangeType()


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
        
        if (obj instanceof WavelengthRangeType) {
        
            WavelengthRangeType temp = (WavelengthRangeType)obj;
            if (this._min != temp._min)
                return false;
            if (this._has_min != temp._has_min)
                return false;
            if (this._max != temp._max)
                return false;
            if (this._has_max != temp._has_max)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'max'. The field 'max' has the
     * following description: The maximum wavelength
     *  
     * 
     * @return the value of field 'max'.
     */
    public float getMax()
    {
        return this._max;
    } //-- float getMax() 

    /**
     * Returns the value of field 'min'. The field 'min' has the
     * following description: The minimum wavelength
     *  
     * 
     * @return the value of field 'min'.
     */
    public float getMin()
    {
        return this._min;
    } //-- float getMin() 

    /**
     * Method hasMax
     */
    public boolean hasMax()
    {
        return this._has_max;
    } //-- boolean hasMax() 

    /**
     * Method hasMin
     */
    public boolean hasMin()
    {
        return this._has_min;
    } //-- boolean hasMin() 

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
     * Sets the value of field 'max'. The field 'max' has the
     * following description: The maximum wavelength
     *  
     * 
     * @param max the value of field 'max'.
     */
    public void setMax(float max)
    {
        this._max = max;
        this._has_max = true;
    } //-- void setMax(float) 

    /**
     * Sets the value of field 'min'. The field 'min' has the
     * following description: The minimum wavelength
     *  
     * 
     * @param min the value of field 'min'.
     */
    public void setMin(float min)
    {
        this._min = min;
        this._has_min = true;
    } //-- void setMin(float) 

    /**
     * Method unmarshalWavelengthRangeType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.WavelengthRangeType unmarshalWavelengthRangeType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.WavelengthRangeType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.WavelengthRangeType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.WavelengthRangeType unmarshalWavelengthRangeType(java.io.Reader) 

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
