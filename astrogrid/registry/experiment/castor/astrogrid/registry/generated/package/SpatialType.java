/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: SpatialType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class SpatialType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class SpatialType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Multiple occurances imply a coverage that is a union
     *  of the regions given (i.e. a logical "or", according
     *  to RM).
     *  
     */
    private java.util.Vector _regionList;

    /**
     * The spatial (angular) resolution that is typical of the
     *  observations of interest, in decimal degrees.
     *  
     */
    private float _spatialResolution;

    /**
     * keeps track of state for field: _spatialResolution
     */
    private boolean _has_spatialResolution;

    /**
     * The intrinsic size scale, given in arcseconds, associated
     *  with data items contained in a resource.
     *  
     */
    private float _regionOfRegard;

    /**
     * keeps track of state for field: _regionOfRegard
     */
    private boolean _has_regionOfRegard;


      //----------------/
     //- Constructors -/
    //----------------/

    public SpatialType() {
        super();
        _regionList = new Vector();
    } //-- org.astrogrid.registry.generated.package.SpatialType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRegion
     * 
     * @param vRegion
     */
    public void addRegion(org.astrogrid.registry.generated.package.Region vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        _regionList.addElement(vRegion);
    } //-- void addRegion(org.astrogrid.registry.generated.package.Region) 

    /**
     * Method addRegion
     * 
     * @param index
     * @param vRegion
     */
    public void addRegion(int index, org.astrogrid.registry.generated.package.Region vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        _regionList.insertElementAt(vRegion, index);
    } //-- void addRegion(int, org.astrogrid.registry.generated.package.Region) 

    /**
     * Method deleteRegionOfRegard
     */
    public void deleteRegionOfRegard()
    {
        this._has_regionOfRegard= false;
    } //-- void deleteRegionOfRegard() 

    /**
     * Method deleteSpatialResolution
     */
    public void deleteSpatialResolution()
    {
        this._has_spatialResolution= false;
    } //-- void deleteSpatialResolution() 

    /**
     * Method enumerateRegion
     */
    public java.util.Enumeration enumerateRegion()
    {
        return _regionList.elements();
    } //-- java.util.Enumeration enumerateRegion() 

    /**
     * Method getRegion
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Region getRegion(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _regionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.Region) _regionList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.Region getRegion(int) 

    /**
     * Method getRegion
     */
    public org.astrogrid.registry.generated.package.Region[] getRegion()
    {
        int size = _regionList.size();
        org.astrogrid.registry.generated.package.Region[] mArray = new org.astrogrid.registry.generated.package.Region[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.Region) _regionList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.Region[] getRegion() 

    /**
     * Method getRegionCount
     */
    public int getRegionCount()
    {
        return _regionList.size();
    } //-- int getRegionCount() 

    /**
     * Returns the value of field 'regionOfRegard'. The field
     * 'regionOfRegard' has the following description: The
     * intrinsic size scale, given in arcseconds, associated
     *  with data items contained in a resource.
     *  
     * 
     * @return the value of field 'regionOfRegard'.
     */
    public float getRegionOfRegard()
    {
        return this._regionOfRegard;
    } //-- float getRegionOfRegard() 

    /**
     * Returns the value of field 'spatialResolution'. The field
     * 'spatialResolution' has the following description: The
     * spatial (angular) resolution that is typical of the
     *  observations of interest, in decimal degrees.
     *  
     * 
     * @return the value of field 'spatialResolution'.
     */
    public float getSpatialResolution()
    {
        return this._spatialResolution;
    } //-- float getSpatialResolution() 

    /**
     * Method hasRegionOfRegard
     */
    public boolean hasRegionOfRegard()
    {
        return this._has_regionOfRegard;
    } //-- boolean hasRegionOfRegard() 

    /**
     * Method hasSpatialResolution
     */
    public boolean hasSpatialResolution()
    {
        return this._has_spatialResolution;
    } //-- boolean hasSpatialResolution() 

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
     * Method removeAllRegion
     */
    public void removeAllRegion()
    {
        _regionList.removeAllElements();
    } //-- void removeAllRegion() 

    /**
     * Method removeRegion
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Region removeRegion(int index)
    {
        java.lang.Object obj = _regionList.elementAt(index);
        _regionList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.Region) obj;
    } //-- org.astrogrid.registry.generated.package.Region removeRegion(int) 

    /**
     * Method setRegion
     * 
     * @param index
     * @param vRegion
     */
    public void setRegion(int index, org.astrogrid.registry.generated.package.Region vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _regionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _regionList.setElementAt(vRegion, index);
    } //-- void setRegion(int, org.astrogrid.registry.generated.package.Region) 

    /**
     * Method setRegion
     * 
     * @param regionArray
     */
    public void setRegion(org.astrogrid.registry.generated.package.Region[] regionArray)
    {
        //-- copy array
        _regionList.removeAllElements();
        for (int i = 0; i < regionArray.length; i++) {
            _regionList.addElement(regionArray[i]);
        }
    } //-- void setRegion(org.astrogrid.registry.generated.package.Region) 

    /**
     * Sets the value of field 'regionOfRegard'. The field
     * 'regionOfRegard' has the following description: The
     * intrinsic size scale, given in arcseconds, associated
     *  with data items contained in a resource.
     *  
     * 
     * @param regionOfRegard the value of field 'regionOfRegard'.
     */
    public void setRegionOfRegard(float regionOfRegard)
    {
        this._regionOfRegard = regionOfRegard;
        this._has_regionOfRegard = true;
    } //-- void setRegionOfRegard(float) 

    /**
     * Sets the value of field 'spatialResolution'. The field
     * 'spatialResolution' has the following description: The
     * spatial (angular) resolution that is typical of the
     *  observations of interest, in decimal degrees.
     *  
     * 
     * @param spatialResolution the value of field
     * 'spatialResolution'.
     */
    public void setSpatialResolution(float spatialResolution)
    {
        this._spatialResolution = spatialResolution;
        this._has_spatialResolution = true;
    } //-- void setSpatialResolution(float) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.SpatialType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.SpatialType.class, reader);
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
