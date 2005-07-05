/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Spectral.java,v 1.2 2005/07/05 08:26:55 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Spectral.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:55 $
 */
public class Spectral extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a named spectral region of the electro-magnetic spectrum.
     *  
     */
    private java.util.ArrayList _wavebandList;

    /**
     * a range of the electro-magnetic spectrum specified by 
     *  a lower and upper wavelength limit.
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.WavelengthRange _range;

    /**
     * The spectral resolution that is typical of the
     *  observations of interest, given as a ratio of the
     * wavelength
     *  width (delta-lambda) to the observing wavelength (lambda).
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

    public Spectral() {
        super();
        _wavebandList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Spectral()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addWaveband
     * 
     * @param vWaveband
     */
    public void addWaveband(org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband vWaveband)
        throws java.lang.IndexOutOfBoundsException
    {
        _wavebandList.add(vWaveband);
    } //-- void addWaveband(org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband) 

    /**
     * Method addWaveband
     * 
     * @param index
     * @param vWaveband
     */
    public void addWaveband(int index, org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband vWaveband)
        throws java.lang.IndexOutOfBoundsException
    {
        _wavebandList.add(index, vWaveband);
    } //-- void addWaveband(int, org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband) 

    /**
     * Method clearWaveband
     */
    public void clearWaveband()
    {
        _wavebandList.clear();
    } //-- void clearWaveband() 

    /**
     * Method deleteResolution
     */
    public void deleteResolution()
    {
        this._has_resolution= false;
    } //-- void deleteResolution() 

    /**
     * Method enumerateWaveband
     */
    public java.util.Enumeration enumerateWaveband()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_wavebandList.iterator());
    } //-- java.util.Enumeration enumerateWaveband() 

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
        
        if (obj instanceof Spectral) {
        
            Spectral temp = (Spectral)obj;
            if (this._wavebandList != null) {
                if (temp._wavebandList == null) return false;
                else if (!(this._wavebandList.equals(temp._wavebandList))) 
                    return false;
            }
            else if (temp._wavebandList != null)
                return false;
            if (this._range != null) {
                if (temp._range == null) return false;
                else if (!(this._range.equals(temp._range))) 
                    return false;
            }
            else if (temp._range != null)
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
     * Returns the value of field 'range'. The field 'range' has
     * the following description: a range of the electro-magnetic
     * spectrum specified by 
     *  a lower and upper wavelength limit.
     *  
     * 
     * @return the value of field 'range'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.WavelengthRange getRange()
    {
        return this._range;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.WavelengthRange getRange() 

    /**
     * Returns the value of field 'resolution'. The field
     * 'resolution' has the following description: The spectral
     * resolution that is typical of the
     *  observations of interest, given as a ratio of the
     * wavelength
     *  width (delta-lambda) to the observing wavelength (lambda).
     *  
     * 
     * @return the value of field 'resolution'.
     */
    public float getResolution()
    {
        return this._resolution;
    } //-- float getResolution() 

    /**
     * Method getWaveband
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband getWaveband(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wavebandList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband) _wavebandList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband getWaveband(int) 

    /**
     * Method getWaveband
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband[] getWaveband()
    {
        int size = _wavebandList.size();
        org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband[] mArray = new org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband) _wavebandList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband[] getWaveband() 

    /**
     * Method getWavebandCount
     */
    public int getWavebandCount()
    {
        return _wavebandList.size();
    } //-- int getWavebandCount() 

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
     * Method removeWaveband
     * 
     * @param vWaveband
     */
    public boolean removeWaveband(org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband vWaveband)
    {
        boolean removed = _wavebandList.remove(vWaveband);
        return removed;
    } //-- boolean removeWaveband(org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband) 

    /**
     * Sets the value of field 'range'. The field 'range' has the
     * following description: a range of the electro-magnetic
     * spectrum specified by 
     *  a lower and upper wavelength limit.
     *  
     * 
     * @param range the value of field 'range'.
     */
    public void setRange(org.astrogrid.registry.beans.v10.resource.dataservice.WavelengthRange range)
    {
        this._range = range;
    } //-- void setRange(org.astrogrid.registry.beans.v10.resource.dataservice.WavelengthRange) 

    /**
     * Sets the value of field 'resolution'. The field 'resolution'
     * has the following description: The spectral resolution that
     * is typical of the
     *  observations of interest, given as a ratio of the
     * wavelength
     *  width (delta-lambda) to the observing wavelength (lambda).
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
     * Method setWaveband
     * 
     * @param index
     * @param vWaveband
     */
    public void setWaveband(int index, org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband vWaveband)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wavebandList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _wavebandList.set(index, vWaveband);
    } //-- void setWaveband(int, org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband) 

    /**
     * Method setWaveband
     * 
     * @param wavebandArray
     */
    public void setWaveband(org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband[] wavebandArray)
    {
        //-- copy array
        _wavebandList.clear();
        for (int i = 0; i < wavebandArray.length; i++) {
            _wavebandList.add(wavebandArray[i]);
        }
    } //-- void setWaveband(org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband) 

    /**
     * Method unmarshalSpectral
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.Spectral unmarshalSpectral(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Spectral) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.Spectral.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Spectral unmarshalSpectral(java.io.Reader) 

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
