/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Temporal.java,v 1.2 2007/01/04 16:26:25 clq2 Exp $
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
 * Class Temporal.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:25 $
 */
public class Temporal extends org.astrogrid.common.bean.BaseBean 
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
    private float _resolution;

    /**
     * keeps track of state for field: _resolution
     */
    private boolean _has_resolution;


      //----------------/
     //- Constructors -/
    //----------------/

    public Temporal() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Temporal()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteResolution
     */
    public void deleteResolution()
    {
        this._has_resolution= false;
    } //-- void deleteResolution() 

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
        
        if (obj instanceof Temporal) {
        
            Temporal temp = (Temporal)obj;
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
            if (this._resolution != temp._resolution)
                return false;
            if (this._has_resolution != temp._has_resolution)
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
     * Returns the value of field 'resolution'. The field
     * 'resolution' has the following description: The temporal
     * resolution that is typical of the
     *  observations of interest, in seconds.
     *  
     * 
     * @return the value of field 'resolution'.
     */
    public float getResolution()
    {
        return this._resolution;
    } //-- float getResolution() 

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
     * Method hasResolution
     */
    public boolean hasResolution()
    {
        return this._has_resolution;
    } //-- boolean hasResolution() 

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
     * Sets the value of field 'resolution'. The field 'resolution'
     * has the following description: The temporal resolution that
     * is typical of the
     *  observations of interest, in seconds.
     *  
     * 
     * @param resolution the value of field 'resolution'.
     */
    public void setResolution(float resolution)
    {
        this._resolution = resolution;
        this._has_resolution = true;
    } //-- void setResolution(float) 

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
     * Method unmarshalTemporal
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.Temporal unmarshalTemporal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Temporal) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.Temporal.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Temporal unmarshalTemporal(java.io.Reader) 

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
