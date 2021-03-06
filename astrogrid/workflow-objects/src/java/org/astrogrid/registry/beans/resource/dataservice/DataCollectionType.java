/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DataCollectionType.java,v 1.14 2007/01/04 16:26:07 clq2 Exp $
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
import org.astrogrid.registry.beans.resource.ResourceReferenceType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class DataCollectionType.
 * 
 * @version $Revision: 1.14 $ $Date: 2007/01/04 16:26:07 $
 */
public class DataCollectionType extends org.astrogrid.registry.beans.resource.ResourceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the observatory or facility used to collect the data 
     *  contained or managed by this resource. 
     *  
     */
    private java.util.ArrayList _facilityList;

    /**
     * the Instrument used to collect the data contain or 
     *  managed by a resource. 
     *  
     */
    private java.util.ArrayList _instrumentList;

    /**
     * Extent of the content of the resource over space, time, and 
     *  frequency.
     *  
     */
    private org.astrogrid.registry.beans.resource.dataservice.CoverageType _coverage;

    /**
     * a description about how a data collection may be accessed
     *  
     */
    private org.astrogrid.registry.beans.resource.dataservice.AccessType _access;


      //----------------/
     //- Constructors -/
    //----------------/

    public DataCollectionType() {
        super();
        _facilityList = new ArrayList();
        _instrumentList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.dataservice.DataCollectionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFacility
     * 
     * @param vFacility
     */
    public void addFacility(org.astrogrid.registry.beans.resource.ResourceReferenceType vFacility)
        throws java.lang.IndexOutOfBoundsException
    {
        _facilityList.add(vFacility);
    } //-- void addFacility(org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Method addFacility
     * 
     * @param index
     * @param vFacility
     */
    public void addFacility(int index, org.astrogrid.registry.beans.resource.ResourceReferenceType vFacility)
        throws java.lang.IndexOutOfBoundsException
    {
        _facilityList.add(index, vFacility);
    } //-- void addFacility(int, org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Method addInstrument
     * 
     * @param vInstrument
     */
    public void addInstrument(org.astrogrid.registry.beans.resource.ResourceReferenceType vInstrument)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentList.add(vInstrument);
    } //-- void addInstrument(org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Method addInstrument
     * 
     * @param index
     * @param vInstrument
     */
    public void addInstrument(int index, org.astrogrid.registry.beans.resource.ResourceReferenceType vInstrument)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentList.add(index, vInstrument);
    } //-- void addInstrument(int, org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Method clearFacility
     */
    public void clearFacility()
    {
        _facilityList.clear();
    } //-- void clearFacility() 

    /**
     * Method clearInstrument
     */
    public void clearInstrument()
    {
        _instrumentList.clear();
    } //-- void clearInstrument() 

    /**
     * Method enumerateFacility
     */
    public java.util.Enumeration enumerateFacility()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_facilityList.iterator());
    } //-- java.util.Enumeration enumerateFacility() 

    /**
     * Method enumerateInstrument
     */
    public java.util.Enumeration enumerateInstrument()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_instrumentList.iterator());
    } //-- java.util.Enumeration enumerateInstrument() 

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
        
        if (obj instanceof DataCollectionType) {
        
            DataCollectionType temp = (DataCollectionType)obj;
            if (this._facilityList != null) {
                if (temp._facilityList == null) return false;
                else if (!(this._facilityList.equals(temp._facilityList))) 
                    return false;
            }
            else if (temp._facilityList != null)
                return false;
            if (this._instrumentList != null) {
                if (temp._instrumentList == null) return false;
                else if (!(this._instrumentList.equals(temp._instrumentList))) 
                    return false;
            }
            else if (temp._instrumentList != null)
                return false;
            if (this._coverage != null) {
                if (temp._coverage == null) return false;
                else if (!(this._coverage.equals(temp._coverage))) 
                    return false;
            }
            else if (temp._coverage != null)
                return false;
            if (this._access != null) {
                if (temp._access == null) return false;
                else if (!(this._access.equals(temp._access))) 
                    return false;
            }
            else if (temp._access != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'access'. The field 'access' has
     * the following description: a description about how a data
     * collection may be accessed
     *  
     * 
     * @return the value of field 'access'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.AccessType getAccess()
    {
        return this._access;
    } //-- org.astrogrid.registry.beans.resource.dataservice.AccessType getAccess() 

    /**
     * Returns the value of field 'coverage'. The field 'coverage'
     * has the following description: Extent of the content of the
     * resource over space, time, and 
     *  frequency.
     *  
     * 
     * @return the value of field 'coverage'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.CoverageType getCoverage()
    {
        return this._coverage;
    } //-- org.astrogrid.registry.beans.resource.dataservice.CoverageType getCoverage() 

    /**
     * Method getFacility
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.ResourceReferenceType getFacility(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _facilityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.ResourceReferenceType) _facilityList.get(index);
    } //-- org.astrogrid.registry.beans.resource.ResourceReferenceType getFacility(int) 

    /**
     * Method getFacility
     */
    public org.astrogrid.registry.beans.resource.ResourceReferenceType[] getFacility()
    {
        int size = _facilityList.size();
        org.astrogrid.registry.beans.resource.ResourceReferenceType[] mArray = new org.astrogrid.registry.beans.resource.ResourceReferenceType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.ResourceReferenceType) _facilityList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.ResourceReferenceType[] getFacility() 

    /**
     * Method getFacilityCount
     */
    public int getFacilityCount()
    {
        return _facilityList.size();
    } //-- int getFacilityCount() 

    /**
     * Method getInstrument
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.ResourceReferenceType getInstrument(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.ResourceReferenceType) _instrumentList.get(index);
    } //-- org.astrogrid.registry.beans.resource.ResourceReferenceType getInstrument(int) 

    /**
     * Method getInstrument
     */
    public org.astrogrid.registry.beans.resource.ResourceReferenceType[] getInstrument()
    {
        int size = _instrumentList.size();
        org.astrogrid.registry.beans.resource.ResourceReferenceType[] mArray = new org.astrogrid.registry.beans.resource.ResourceReferenceType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.ResourceReferenceType) _instrumentList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.ResourceReferenceType[] getInstrument() 

    /**
     * Method getInstrumentCount
     */
    public int getInstrumentCount()
    {
        return _instrumentList.size();
    } //-- int getInstrumentCount() 

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
     * Method removeFacility
     * 
     * @param vFacility
     */
    public boolean removeFacility(org.astrogrid.registry.beans.resource.ResourceReferenceType vFacility)
    {
        boolean removed = _facilityList.remove(vFacility);
        return removed;
    } //-- boolean removeFacility(org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Method removeInstrument
     * 
     * @param vInstrument
     */
    public boolean removeInstrument(org.astrogrid.registry.beans.resource.ResourceReferenceType vInstrument)
    {
        boolean removed = _instrumentList.remove(vInstrument);
        return removed;
    } //-- boolean removeInstrument(org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Sets the value of field 'access'. The field 'access' has the
     * following description: a description about how a data
     * collection may be accessed
     *  
     * 
     * @param access the value of field 'access'.
     */
    public void setAccess(org.astrogrid.registry.beans.resource.dataservice.AccessType access)
    {
        this._access = access;
    } //-- void setAccess(org.astrogrid.registry.beans.resource.dataservice.AccessType) 

    /**
     * Sets the value of field 'coverage'. The field 'coverage' has
     * the following description: Extent of the content of the
     * resource over space, time, and 
     *  frequency.
     *  
     * 
     * @param coverage the value of field 'coverage'.
     */
    public void setCoverage(org.astrogrid.registry.beans.resource.dataservice.CoverageType coverage)
    {
        this._coverage = coverage;
    } //-- void setCoverage(org.astrogrid.registry.beans.resource.dataservice.CoverageType) 

    /**
     * Method setFacility
     * 
     * @param index
     * @param vFacility
     */
    public void setFacility(int index, org.astrogrid.registry.beans.resource.ResourceReferenceType vFacility)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _facilityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _facilityList.set(index, vFacility);
    } //-- void setFacility(int, org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Method setFacility
     * 
     * @param facilityArray
     */
    public void setFacility(org.astrogrid.registry.beans.resource.ResourceReferenceType[] facilityArray)
    {
        //-- copy array
        _facilityList.clear();
        for (int i = 0; i < facilityArray.length; i++) {
            _facilityList.add(facilityArray[i]);
        }
    } //-- void setFacility(org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Method setInstrument
     * 
     * @param index
     * @param vInstrument
     */
    public void setInstrument(int index, org.astrogrid.registry.beans.resource.ResourceReferenceType vInstrument)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _instrumentList.set(index, vInstrument);
    } //-- void setInstrument(int, org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Method setInstrument
     * 
     * @param instrumentArray
     */
    public void setInstrument(org.astrogrid.registry.beans.resource.ResourceReferenceType[] instrumentArray)
    {
        //-- copy array
        _instrumentList.clear();
        for (int i = 0; i < instrumentArray.length; i++) {
            _instrumentList.add(instrumentArray[i]);
        }
    } //-- void setInstrument(org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Method unmarshalDataCollectionType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.DataCollectionType unmarshalDataCollectionType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.DataCollectionType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.DataCollectionType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.DataCollectionType unmarshalDataCollectionType(java.io.Reader) 

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
