/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SpatialType.java,v 1.3 2004/03/05 09:51:59 KevinBenson Exp $
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class SpatialType.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:51:59 $
 */
public class SpatialType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Multiple occurances imply a coverage that is a union
     *  of the regions given (i.e. a logical "or", according
     *  to RM).
     *  
     */
    private java.util.ArrayList _regionList;

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
        _regionList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.dataservice.SpatialType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRegion
     * 
     * @param vRegion
     */
    public void addRegion(org.astrogrid.registry.beans.resource.dataservice.RegionType vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        _regionList.add(vRegion);
    } //-- void addRegion(org.astrogrid.registry.beans.resource.dataservice.RegionType) 

    /**
     * Method addRegion
     * 
     * @param index
     * @param vRegion
     */
    public void addRegion(int index, org.astrogrid.registry.beans.resource.dataservice.RegionType vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        _regionList.add(index, vRegion);
    } //-- void addRegion(int, org.astrogrid.registry.beans.resource.dataservice.RegionType) 

    /**
     * Method clearRegion
     */
    public void clearRegion()
    {
        _regionList.clear();
    } //-- void clearRegion() 

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
        return new org.exolab.castor.util.IteratorEnumeration(_regionList.iterator());
    } //-- java.util.Enumeration enumerateRegion() 

    /**
     * Method getRegion
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.dataservice.RegionType getRegion(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _regionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.dataservice.RegionType) _regionList.get(index);
    } //-- org.astrogrid.registry.beans.resource.dataservice.RegionType getRegion(int) 

    /**
     * Method getRegion
     */
    public org.astrogrid.registry.beans.resource.dataservice.RegionType[] getRegion()
    {
        int size = _regionList.size();
        org.astrogrid.registry.beans.resource.dataservice.RegionType[] mArray = new org.astrogrid.registry.beans.resource.dataservice.RegionType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.dataservice.RegionType) _regionList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.dataservice.RegionType[] getRegion() 

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
     * Method removeRegion
     * 
     * @param vRegion
     */
    public boolean removeRegion(org.astrogrid.registry.beans.resource.dataservice.RegionType vRegion)
    {
        boolean removed = _regionList.remove(vRegion);
        return removed;
    } //-- boolean removeRegion(org.astrogrid.registry.beans.resource.dataservice.RegionType) 

    /**
     * Method setRegion
     * 
     * @param index
     * @param vRegion
     */
    public void setRegion(int index, org.astrogrid.registry.beans.resource.dataservice.RegionType vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _regionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _regionList.set(index, vRegion);
    } //-- void setRegion(int, org.astrogrid.registry.beans.resource.dataservice.RegionType) 

    /**
     * Method setRegion
     * 
     * @param regionArray
     */
    public void setRegion(org.astrogrid.registry.beans.resource.dataservice.RegionType[] regionArray)
    {
        //-- copy array
        _regionList.clear();
        for (int i = 0; i < regionArray.length; i++) {
            _regionList.add(regionArray[i]);
        }
    } //-- void setRegion(org.astrogrid.registry.beans.resource.dataservice.RegionType) 

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
     * Method unmarshalSpatialType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.SpatialType unmarshalSpatialType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.SpatialType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.SpatialType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.SpatialType unmarshalSpatialType(java.io.Reader) 

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
