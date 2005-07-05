/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TemporalType.java,v 1.2 2005/07/05 08:26:57 clq2 Exp $
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
 * Class TemporalType.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:57 $
 */
public class TemporalType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The earliest temporal coverage of the resource.
     *  
     */
    private org.exolab.castor.types.Date _startTime;

    /**
     * The latest temporal coverage of the resource.
     *  
     */
    private org.exolab.castor.types.Date _endTime;

    /**
     * The temporal resolution that is typical of the
     *  observations of interest, in seconds.
     *  
     */
    private float _temporalResolution;

    /**
     * keeps track of state for field: _temporalResolution
     */
    private boolean _has_temporalResolution;


      //----------------/
     //- Constructors -/
    //----------------/

    public TemporalType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.dataservice.TemporalType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteTemporalResolution
     */
    public void deleteTemporalResolution()
    {
        this._has_temporalResolution= false;
    } //-- void deleteTemporalResolution() 

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
        
        if (obj instanceof TemporalType) {
        
            TemporalType temp = (TemporalType)obj;
            if (this._startTime != null) {
                if (temp._startTime == null) return false;
                else if (!(this._startTime.equals(temp._startTime))) 
                    return false;
            }
            else if (temp._startTime != null)
                return false;
            if (this._endTime != null) {
                if (temp._endTime == null) return false;
                else if (!(this._endTime.equals(temp._endTime))) 
                    return false;
            }
            else if (temp._endTime != null)
                return false;
            if (this._temporalResolution != temp._temporalResolution)
                return false;
            if (this._has_temporalResolution != temp._has_temporalResolution)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'endTime'. The field 'endTime'
     * has the following description: The latest temporal coverage
     * of the resource.
     *  
     * 
     * @return the value of field 'endTime'.
     */
    public org.exolab.castor.types.Date getEndTime()
    {
        return this._endTime;
    } //-- org.exolab.castor.types.Date getEndTime() 

    /**
     * Returns the value of field 'startTime'. The field
     * 'startTime' has the following description: The earliest
     * temporal coverage of the resource.
     *  
     * 
     * @return the value of field 'startTime'.
     */
    public org.exolab.castor.types.Date getStartTime()
    {
        return this._startTime;
    } //-- org.exolab.castor.types.Date getStartTime() 

    /**
     * Returns the value of field 'temporalResolution'. The field
     * 'temporalResolution' has the following description: The
     * temporal resolution that is typical of the
     *  observations of interest, in seconds.
     *  
     * 
     * @return the value of field 'temporalResolution'.
     */
    public float getTemporalResolution()
    {
        return this._temporalResolution;
    } //-- float getTemporalResolution() 

    /**
     * Method hasTemporalResolution
     */
    public boolean hasTemporalResolution()
    {
        return this._has_temporalResolution;
    } //-- boolean hasTemporalResolution() 

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
     * Sets the value of field 'endTime'. The field 'endTime' has
     * the following description: The latest temporal coverage of
     * the resource.
     *  
     * 
     * @param endTime the value of field 'endTime'.
     */
    public void setEndTime(org.exolab.castor.types.Date endTime)
    {
        this._endTime = endTime;
    } //-- void setEndTime(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'startTime'. The field 'startTime'
     * has the following description: The earliest temporal
     * coverage of the resource.
     *  
     * 
     * @param startTime the value of field 'startTime'.
     */
    public void setStartTime(org.exolab.castor.types.Date startTime)
    {
        this._startTime = startTime;
    } //-- void setStartTime(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'temporalResolution'. The field
     * 'temporalResolution' has the following description: The
     * temporal resolution that is typical of the
     *  observations of interest, in seconds.
     *  
     * 
     * @param temporalResolution the value of field
     * 'temporalResolution'.
     */
    public void setTemporalResolution(float temporalResolution)
    {
        this._temporalResolution = temporalResolution;
        this._has_temporalResolution = true;
    } //-- void setTemporalResolution(float) 

    /**
     * Method unmarshalTemporalType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.TemporalType unmarshalTemporalType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.TemporalType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.TemporalType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.TemporalType unmarshalTemporalType(java.io.Reader) 

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
