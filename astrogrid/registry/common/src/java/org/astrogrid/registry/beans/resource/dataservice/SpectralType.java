/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SpectralType.java,v 1.8 2004/04/05 14:36:11 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.registry.beans.resource.dataservice.types.WavebandType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class SpectralType.
 * 
 * @version $Revision: 1.8 $ $Date: 2004/04/05 14:36:11 $
 */
public class SpectralType extends org.astrogrid.common.bean.BaseBean 
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
    private org.astrogrid.registry.beans.resource.dataservice.WavelengthRangeType _wavelengthRange;

    /**
     * The spectral resolution that is typical of the
     *  observations of interest, given as a ratio of the
     * wavelength
     *  width (delta-lambda) to the observing wavelength (lambda).
     *  
     */
    private float _spectralResolution;

    /**
     * keeps track of state for field: _spectralResolution
     */
    private boolean _has_spectralResolution;


      //----------------/
     //- Constructors -/
    //----------------/

    public SpectralType() {
        super();
        _wavebandList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.dataservice.SpectralType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addWaveband
     * 
     * @param vWaveband
     */
    public void addWaveband(org.astrogrid.registry.beans.resource.dataservice.types.WavebandType vWaveband)
        throws java.lang.IndexOutOfBoundsException
    {
        _wavebandList.add(vWaveband);
    } //-- void addWaveband(org.astrogrid.registry.beans.resource.dataservice.types.WavebandType) 

    /**
     * Method addWaveband
     * 
     * @param index
     * @param vWaveband
     */
    public void addWaveband(int index, org.astrogrid.registry.beans.resource.dataservice.types.WavebandType vWaveband)
        throws java.lang.IndexOutOfBoundsException
    {
        _wavebandList.add(index, vWaveband);
    } //-- void addWaveband(int, org.astrogrid.registry.beans.resource.dataservice.types.WavebandType) 

    /**
     * Method clearWaveband
     */
    public void clearWaveband()
    {
        _wavebandList.clear();
    } //-- void clearWaveband() 

    /**
     * Method deleteSpectralResolution
     */
    public void deleteSpectralResolution()
    {
        this._has_spectralResolution= false;
    } //-- void deleteSpectralResolution() 

    /**
     * Method enumerateWaveband
     */
    public java.util.Enumeration enumerateWaveband()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_wavebandList.iterator());
    } //-- java.util.Enumeration enumerateWaveband() 

    /**
     * Returns the value of field 'spectralResolution'. The field
     * 'spectralResolution' has the following description: The
     * spectral resolution that is typical of the
     *  observations of interest, given as a ratio of the
     * wavelength
     *  width (delta-lambda) to the observing wavelength (lambda).
     *  
     * 
     * @return the value of field 'spectralResolution'.
     */
    public float getSpectralResolution()
    {
        return this._spectralResolution;
    } //-- float getSpectralResolution() 

    /**
     * Method getWaveband
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.dataservice.types.WavebandType getWaveband(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wavebandList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.dataservice.types.WavebandType) _wavebandList.get(index);
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.WavebandType getWaveband(int) 

    /**
     * Method getWaveband
     */
    public org.astrogrid.registry.beans.resource.dataservice.types.WavebandType[] getWaveband()
    {
        int size = _wavebandList.size();
        org.astrogrid.registry.beans.resource.dataservice.types.WavebandType[] mArray = new org.astrogrid.registry.beans.resource.dataservice.types.WavebandType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.dataservice.types.WavebandType) _wavebandList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.WavebandType[] getWaveband() 

    /**
     * Method getWavebandCount
     */
    public int getWavebandCount()
    {
        return _wavebandList.size();
    } //-- int getWavebandCount() 

    /**
     * Returns the value of field 'wavelengthRange'. The field
     * 'wavelengthRange' has the following description: a range of
     * the electro-magnetic spectrum specified by 
     *  a lower and upper wavelength limit.
     *  
     * 
     * @return the value of field 'wavelengthRange'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.WavelengthRangeType getWavelengthRange()
    {
        return this._wavelengthRange;
    } //-- org.astrogrid.registry.beans.resource.dataservice.WavelengthRangeType getWavelengthRange() 

    /**
     * Method hasSpectralResolution
     */
    public boolean hasSpectralResolution()
    {
        return this._has_spectralResolution;
    } //-- boolean hasSpectralResolution() 

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
    public boolean removeWaveband(org.astrogrid.registry.beans.resource.dataservice.types.WavebandType vWaveband)
    {
        boolean removed = _wavebandList.remove(vWaveband);
        return removed;
    } //-- boolean removeWaveband(org.astrogrid.registry.beans.resource.dataservice.types.WavebandType) 

    /**
     * Sets the value of field 'spectralResolution'. The field
     * 'spectralResolution' has the following description: The
     * spectral resolution that is typical of the
     *  observations of interest, given as a ratio of the
     * wavelength
     *  width (delta-lambda) to the observing wavelength (lambda).
     *  
     * 
     * @param spectralResolution the value of field
     * 'spectralResolution'.
     */
    public void setSpectralResolution(float spectralResolution)
    {
        this._spectralResolution = spectralResolution;
        this._has_spectralResolution = true;
    } //-- void setSpectralResolution(float) 

    /**
     * Method setWaveband
     * 
     * @param index
     * @param vWaveband
     */
    public void setWaveband(int index, org.astrogrid.registry.beans.resource.dataservice.types.WavebandType vWaveband)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wavebandList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _wavebandList.set(index, vWaveband);
    } //-- void setWaveband(int, org.astrogrid.registry.beans.resource.dataservice.types.WavebandType) 

    /**
     * Method setWaveband
     * 
     * @param wavebandArray
     */
    public void setWaveband(org.astrogrid.registry.beans.resource.dataservice.types.WavebandType[] wavebandArray)
    {
        //-- copy array
        _wavebandList.clear();
        for (int i = 0; i < wavebandArray.length; i++) {
            _wavebandList.add(wavebandArray[i]);
        }
    } //-- void setWaveband(org.astrogrid.registry.beans.resource.dataservice.types.WavebandType) 

    /**
     * Sets the value of field 'wavelengthRange'. The field
     * 'wavelengthRange' has the following description: a range of
     * the electro-magnetic spectrum specified by 
     *  a lower and upper wavelength limit.
     *  
     * 
     * @param wavelengthRange the value of field 'wavelengthRange'.
     */
    public void setWavelengthRange(org.astrogrid.registry.beans.resource.dataservice.WavelengthRangeType wavelengthRange)
    {
        this._wavelengthRange = wavelengthRange;
    } //-- void setWavelengthRange(org.astrogrid.registry.beans.resource.dataservice.WavelengthRangeType) 

    /**
     * Method unmarshalSpectralType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.SpectralType unmarshalSpectralType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.SpectralType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.SpectralType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.SpectralType unmarshalSpectralType(java.io.Reader) 

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
