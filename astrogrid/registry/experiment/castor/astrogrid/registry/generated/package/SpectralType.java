/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: SpectralType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.astrogrid.registry.generated.package.types.WavebandType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class SpectralType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class SpectralType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a named spectral region of the electro-magnetic spectrum.
     *  
     */
    private java.util.Vector _wavebandList;

    /**
     * a range of the electro-magnetic spectrum specified by 
     *  a lower and upper wavelength limit.
     *  
     */
    private org.astrogrid.registry.generated.package.WavelengthRange _wavelengthRange;

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
        _wavebandList = new Vector();
    } //-- org.astrogrid.registry.generated.package.SpectralType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addWaveband
     * 
     * @param vWaveband
     */
    public void addWaveband(org.astrogrid.registry.generated.package.types.WavebandType vWaveband)
        throws java.lang.IndexOutOfBoundsException
    {
        _wavebandList.addElement(vWaveband);
    } //-- void addWaveband(org.astrogrid.registry.generated.package.types.WavebandType) 

    /**
     * Method addWaveband
     * 
     * @param index
     * @param vWaveband
     */
    public void addWaveband(int index, org.astrogrid.registry.generated.package.types.WavebandType vWaveband)
        throws java.lang.IndexOutOfBoundsException
    {
        _wavebandList.insertElementAt(vWaveband, index);
    } //-- void addWaveband(int, org.astrogrid.registry.generated.package.types.WavebandType) 

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
        return _wavebandList.elements();
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
    public org.astrogrid.registry.generated.package.types.WavebandType getWaveband(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wavebandList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.types.WavebandType) _wavebandList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.types.WavebandType getWaveband(int) 

    /**
     * Method getWaveband
     */
    public org.astrogrid.registry.generated.package.types.WavebandType[] getWaveband()
    {
        int size = _wavebandList.size();
        org.astrogrid.registry.generated.package.types.WavebandType[] mArray = new org.astrogrid.registry.generated.package.types.WavebandType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.types.WavebandType) _wavebandList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.types.WavebandType[] getWaveband() 

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
    public org.astrogrid.registry.generated.package.WavelengthRange getWavelengthRange()
    {
        return this._wavelengthRange;
    } //-- org.astrogrid.registry.generated.package.WavelengthRange getWavelengthRange() 

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
     * Method removeAllWaveband
     */
    public void removeAllWaveband()
    {
        _wavebandList.removeAllElements();
    } //-- void removeAllWaveband() 

    /**
     * Method removeWaveband
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.types.WavebandType removeWaveband(int index)
    {
        java.lang.Object obj = _wavebandList.elementAt(index);
        _wavebandList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.types.WavebandType) obj;
    } //-- org.astrogrid.registry.generated.package.types.WavebandType removeWaveband(int) 

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
    public void setWaveband(int index, org.astrogrid.registry.generated.package.types.WavebandType vWaveband)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _wavebandList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _wavebandList.setElementAt(vWaveband, index);
    } //-- void setWaveband(int, org.astrogrid.registry.generated.package.types.WavebandType) 

    /**
     * Method setWaveband
     * 
     * @param wavebandArray
     */
    public void setWaveband(org.astrogrid.registry.generated.package.types.WavebandType[] wavebandArray)
    {
        //-- copy array
        _wavebandList.removeAllElements();
        for (int i = 0; i < wavebandArray.length; i++) {
            _wavebandList.addElement(wavebandArray[i]);
        }
    } //-- void setWaveband(org.astrogrid.registry.generated.package.types.WavebandType) 

    /**
     * Sets the value of field 'wavelengthRange'. The field
     * 'wavelengthRange' has the following description: a range of
     * the electro-magnetic spectrum specified by 
     *  a lower and upper wavelength limit.
     *  
     * 
     * @param wavelengthRange the value of field 'wavelengthRange'.
     */
    public void setWavelengthRange(org.astrogrid.registry.generated.package.WavelengthRange wavelengthRange)
    {
        this._wavelengthRange = wavelengthRange;
    } //-- void setWavelengthRange(org.astrogrid.registry.generated.package.WavelengthRange) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.SpectralType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.SpectralType.class, reader);
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
