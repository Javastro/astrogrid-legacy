/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Spatial.java,v 1.2 2007/01/04 16:26:24 clq2 Exp $
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Spatial.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:24 $
 */
public class Spatial extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a coverage area of the sky
     */
    private java.util.ArrayList _regionList;

    /**
     * The spatial (angular) resolution that is typical of the
     *  observations of interest, in decimal degrees.
     *  
     */
    private float _resolution;

    /**
     * keeps track of state for field: _resolution
     */
    private boolean _has_resolution;

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

    public Spatial() {
        super();
        _regionList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Spatial()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRegion
     * 
     * @param vRegion
     */
    public void addRegion(org.astrogrid.registry.beans.v10.resource.dataservice.Region vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        _regionList.add(vRegion);
    } //-- void addRegion(org.astrogrid.registry.beans.v10.resource.dataservice.Region) 

    /**
     * Method addRegion
     * 
     * @param index
     * @param vRegion
     */
    public void addRegion(int index, org.astrogrid.registry.beans.v10.resource.dataservice.Region vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        _regionList.add(index, vRegion);
    } //-- void addRegion(int, org.astrogrid.registry.beans.v10.resource.dataservice.Region) 

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
     * Method deleteResolution
     */
    public void deleteResolution()
    {
        this._has_resolution= false;
    } //-- void deleteResolution() 

    /**
     * Method enumerateRegion
     */
    public java.util.Enumeration enumerateRegion()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_regionList.iterator());
    } //-- java.util.Enumeration enumerateRegion() 

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
        
        if (obj instanceof Spatial) {
        
            Spatial temp = (Spatial)obj;
            if (this._regionList != null) {
                if (temp._regionList == null) return false;
                else if (!(this._regionList.equals(temp._regionList))) 
                    return false;
            }
            else if (temp._regionList != null)
                return false;
            if (this._resolution != temp._resolution)
                return false;
            if (this._has_resolution != temp._has_resolution)
                return false;
            if (this._regionOfRegard != temp._regionOfRegard)
                return false;
            if (this._has_regionOfRegard != temp._has_regionOfRegard)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getRegion
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Region getRegion(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _regionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Region) _regionList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Region getRegion(int) 

    /**
     * Method getRegion
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Region[] getRegion()
    {
        int size = _regionList.size();
        org.astrogrid.registry.beans.v10.resource.dataservice.Region[] mArray = new org.astrogrid.registry.beans.v10.resource.dataservice.Region[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.dataservice.Region) _regionList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Region[] getRegion() 

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
     * Returns the value of field 'resolution'. The field
     * 'resolution' has the following description: The spatial
     * (angular) resolution that is typical of the
     *  observations of interest, in decimal degrees.
     *  
     * 
     * @return the value of field 'resolution'.
     */
    public float getResolution()
    {
        return this._resolution;
    } //-- float getResolution() 

    /**
     * Method hasRegionOfRegard
     */
    public boolean hasRegionOfRegard()
    {
        return this._has_regionOfRegard;
    } //-- boolean hasRegionOfRegard() 

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
     * Method removeRegion
     * 
     * @param vRegion
     */
    public boolean removeRegion(org.astrogrid.registry.beans.v10.resource.dataservice.Region vRegion)
    {
        boolean removed = _regionList.remove(vRegion);
        return removed;
    } //-- boolean removeRegion(org.astrogrid.registry.beans.v10.resource.dataservice.Region) 

    /**
     * Method setRegion
     * 
     * @param index
     * @param vRegion
     */
    public void setRegion(int index, org.astrogrid.registry.beans.v10.resource.dataservice.Region vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _regionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _regionList.set(index, vRegion);
    } //-- void setRegion(int, org.astrogrid.registry.beans.v10.resource.dataservice.Region) 

    /**
     * Method setRegion
     * 
     * @param regionArray
     */
    public void setRegion(org.astrogrid.registry.beans.v10.resource.dataservice.Region[] regionArray)
    {
        //-- copy array
        _regionList.clear();
        for (int i = 0; i < regionArray.length; i++) {
            _regionList.add(regionArray[i]);
        }
    } //-- void setRegion(org.astrogrid.registry.beans.v10.resource.dataservice.Region) 

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
     * Sets the value of field 'resolution'. The field 'resolution'
     * has the following description: The spatial (angular)
     * resolution that is typical of the
     *  observations of interest, in decimal degrees.
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
     * Method unmarshalSpatial
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.Spatial unmarshalSpatial(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Spatial) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.Spatial.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Spatial unmarshalSpatial(java.io.Reader) 

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
