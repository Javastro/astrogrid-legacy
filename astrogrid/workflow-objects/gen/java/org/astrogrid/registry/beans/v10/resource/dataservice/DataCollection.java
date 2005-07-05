/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DataCollection.java,v 1.2 2005/07/05 08:26:55 clq2 Exp $
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
import org.astrogrid.registry.beans.v10.resource.AccessURL;
import org.astrogrid.registry.beans.v10.resource.ResourceName;
import org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * A logical grouping of data which, in general, is composed of one
 * 
 *  or more accessible datasets.
 *  
 *  (A dataset is a collection of digitally-encoded data with a
 * that 
 *  is normally accessible as a single unit, e.g. a file.)
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:55 $
 */
public class DataCollection extends org.astrogrid.registry.beans.v10.resource.Resource 
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
     * Extent of the content of the resource over space, time, 
     *  and frequency.
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.Coverage _coverage;

    /**
     * The physical or digital manifestation of the information 
     *  supported by a resource.
     *  
     */
    private java.util.ArrayList _formatList;

    /**
     * Information about rights held in and over the resource.
     *  
     */
    private java.util.ArrayList _rightsList;

    /**
     * The URL that can be used to download the data contained in 
     *  this data collection.
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.AccessURL _accessURL;


      //----------------/
     //- Constructors -/
    //----------------/

    public DataCollection() {
        super();
        _facilityList = new ArrayList();
        _instrumentList = new ArrayList();
        _formatList = new ArrayList();
        _rightsList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.DataCollection()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFacility
     * 
     * @param vFacility
     */
    public void addFacility(org.astrogrid.registry.beans.v10.resource.ResourceName vFacility)
        throws java.lang.IndexOutOfBoundsException
    {
        _facilityList.add(vFacility);
    } //-- void addFacility(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method addFacility
     * 
     * @param index
     * @param vFacility
     */
    public void addFacility(int index, org.astrogrid.registry.beans.v10.resource.ResourceName vFacility)
        throws java.lang.IndexOutOfBoundsException
    {
        _facilityList.add(index, vFacility);
    } //-- void addFacility(int, org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method addFormat
     * 
     * @param vFormat
     */
    public void addFormat(org.astrogrid.registry.beans.v10.resource.dataservice.Format vFormat)
        throws java.lang.IndexOutOfBoundsException
    {
        _formatList.add(vFormat);
    } //-- void addFormat(org.astrogrid.registry.beans.v10.resource.dataservice.Format) 

    /**
     * Method addFormat
     * 
     * @param index
     * @param vFormat
     */
    public void addFormat(int index, org.astrogrid.registry.beans.v10.resource.dataservice.Format vFormat)
        throws java.lang.IndexOutOfBoundsException
    {
        _formatList.add(index, vFormat);
    } //-- void addFormat(int, org.astrogrid.registry.beans.v10.resource.dataservice.Format) 

    /**
     * Method addInstrument
     * 
     * @param vInstrument
     */
    public void addInstrument(org.astrogrid.registry.beans.v10.resource.ResourceName vInstrument)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentList.add(vInstrument);
    } //-- void addInstrument(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method addInstrument
     * 
     * @param index
     * @param vInstrument
     */
    public void addInstrument(int index, org.astrogrid.registry.beans.v10.resource.ResourceName vInstrument)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentList.add(index, vInstrument);
    } //-- void addInstrument(int, org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method addRights
     * 
     * @param vRights
     */
    public void addRights(org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights vRights)
        throws java.lang.IndexOutOfBoundsException
    {
        _rightsList.add(vRights);
    } //-- void addRights(org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights) 

    /**
     * Method addRights
     * 
     * @param index
     * @param vRights
     */
    public void addRights(int index, org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights vRights)
        throws java.lang.IndexOutOfBoundsException
    {
        _rightsList.add(index, vRights);
    } //-- void addRights(int, org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights) 

    /**
     * Method clearFacility
     */
    public void clearFacility()
    {
        _facilityList.clear();
    } //-- void clearFacility() 

    /**
     * Method clearFormat
     */
    public void clearFormat()
    {
        _formatList.clear();
    } //-- void clearFormat() 

    /**
     * Method clearInstrument
     */
    public void clearInstrument()
    {
        _instrumentList.clear();
    } //-- void clearInstrument() 

    /**
     * Method clearRights
     */
    public void clearRights()
    {
        _rightsList.clear();
    } //-- void clearRights() 

    /**
     * Method enumerateFacility
     */
    public java.util.Enumeration enumerateFacility()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_facilityList.iterator());
    } //-- java.util.Enumeration enumerateFacility() 

    /**
     * Method enumerateFormat
     */
    public java.util.Enumeration enumerateFormat()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_formatList.iterator());
    } //-- java.util.Enumeration enumerateFormat() 

    /**
     * Method enumerateInstrument
     */
    public java.util.Enumeration enumerateInstrument()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_instrumentList.iterator());
    } //-- java.util.Enumeration enumerateInstrument() 

    /**
     * Method enumerateRights
     */
    public java.util.Enumeration enumerateRights()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_rightsList.iterator());
    } //-- java.util.Enumeration enumerateRights() 

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
        
        if (obj instanceof DataCollection) {
        
            DataCollection temp = (DataCollection)obj;
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
            if (this._formatList != null) {
                if (temp._formatList == null) return false;
                else if (!(this._formatList.equals(temp._formatList))) 
                    return false;
            }
            else if (temp._formatList != null)
                return false;
            if (this._rightsList != null) {
                if (temp._rightsList == null) return false;
                else if (!(this._rightsList.equals(temp._rightsList))) 
                    return false;
            }
            else if (temp._rightsList != null)
                return false;
            if (this._accessURL != null) {
                if (temp._accessURL == null) return false;
                else if (!(this._accessURL.equals(temp._accessURL))) 
                    return false;
            }
            else if (temp._accessURL != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'accessURL'. The field
     * 'accessURL' has the following description: The URL that can
     * be used to download the data contained in 
     *  this data collection.
     *  
     * 
     * @return the value of field 'accessURL'.
     */
    public org.astrogrid.registry.beans.v10.resource.AccessURL getAccessURL()
    {
        return this._accessURL;
    } //-- org.astrogrid.registry.beans.v10.resource.AccessURL getAccessURL() 

    /**
     * Returns the value of field 'coverage'. The field 'coverage'
     * has the following description: Extent of the content of the
     * resource over space, time, 
     *  and frequency.
     *  
     * 
     * @return the value of field 'coverage'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Coverage getCoverage()
    {
        return this._coverage;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Coverage getCoverage() 

    /**
     * Method getFacility
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.ResourceName getFacility(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _facilityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.ResourceName) _facilityList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.ResourceName getFacility(int) 

    /**
     * Method getFacility
     */
    public org.astrogrid.registry.beans.v10.resource.ResourceName[] getFacility()
    {
        int size = _facilityList.size();
        org.astrogrid.registry.beans.v10.resource.ResourceName[] mArray = new org.astrogrid.registry.beans.v10.resource.ResourceName[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.ResourceName) _facilityList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.ResourceName[] getFacility() 

    /**
     * Method getFacilityCount
     */
    public int getFacilityCount()
    {
        return _facilityList.size();
    } //-- int getFacilityCount() 

    /**
     * Method getFormat
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Format getFormat(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formatList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Format) _formatList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Format getFormat(int) 

    /**
     * Method getFormat
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Format[] getFormat()
    {
        int size = _formatList.size();
        org.astrogrid.registry.beans.v10.resource.dataservice.Format[] mArray = new org.astrogrid.registry.beans.v10.resource.dataservice.Format[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.dataservice.Format) _formatList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Format[] getFormat() 

    /**
     * Method getFormatCount
     */
    public int getFormatCount()
    {
        return _formatList.size();
    } //-- int getFormatCount() 

    /**
     * Method getInstrument
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.ResourceName getInstrument(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.ResourceName) _instrumentList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.ResourceName getInstrument(int) 

    /**
     * Method getInstrument
     */
    public org.astrogrid.registry.beans.v10.resource.ResourceName[] getInstrument()
    {
        int size = _instrumentList.size();
        org.astrogrid.registry.beans.v10.resource.ResourceName[] mArray = new org.astrogrid.registry.beans.v10.resource.ResourceName[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.ResourceName) _instrumentList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.ResourceName[] getInstrument() 

    /**
     * Method getInstrumentCount
     */
    public int getInstrumentCount()
    {
        return _instrumentList.size();
    } //-- int getInstrumentCount() 

    /**
     * Method getRights
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights getRights(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rightsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights) _rightsList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights getRights(int) 

    /**
     * Method getRights
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights[] getRights()
    {
        int size = _rightsList.size();
        org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights[] mArray = new org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights) _rightsList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights[] getRights() 

    /**
     * Method getRightsCount
     */
    public int getRightsCount()
    {
        return _rightsList.size();
    } //-- int getRightsCount() 

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
    public boolean removeFacility(org.astrogrid.registry.beans.v10.resource.ResourceName vFacility)
    {
        boolean removed = _facilityList.remove(vFacility);
        return removed;
    } //-- boolean removeFacility(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method removeFormat
     * 
     * @param vFormat
     */
    public boolean removeFormat(org.astrogrid.registry.beans.v10.resource.dataservice.Format vFormat)
    {
        boolean removed = _formatList.remove(vFormat);
        return removed;
    } //-- boolean removeFormat(org.astrogrid.registry.beans.v10.resource.dataservice.Format) 

    /**
     * Method removeInstrument
     * 
     * @param vInstrument
     */
    public boolean removeInstrument(org.astrogrid.registry.beans.v10.resource.ResourceName vInstrument)
    {
        boolean removed = _instrumentList.remove(vInstrument);
        return removed;
    } //-- boolean removeInstrument(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method removeRights
     * 
     * @param vRights
     */
    public boolean removeRights(org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights vRights)
    {
        boolean removed = _rightsList.remove(vRights);
        return removed;
    } //-- boolean removeRights(org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights) 

    /**
     * Sets the value of field 'accessURL'. The field 'accessURL'
     * has the following description: The URL that can be used to
     * download the data contained in 
     *  this data collection.
     *  
     * 
     * @param accessURL the value of field 'accessURL'.
     */
    public void setAccessURL(org.astrogrid.registry.beans.v10.resource.AccessURL accessURL)
    {
        this._accessURL = accessURL;
    } //-- void setAccessURL(org.astrogrid.registry.beans.v10.resource.AccessURL) 

    /**
     * Sets the value of field 'coverage'. The field 'coverage' has
     * the following description: Extent of the content of the
     * resource over space, time, 
     *  and frequency.
     *  
     * 
     * @param coverage the value of field 'coverage'.
     */
    public void setCoverage(org.astrogrid.registry.beans.v10.resource.dataservice.Coverage coverage)
    {
        this._coverage = coverage;
    } //-- void setCoverage(org.astrogrid.registry.beans.v10.resource.dataservice.Coverage) 

    /**
     * Method setFacility
     * 
     * @param index
     * @param vFacility
     */
    public void setFacility(int index, org.astrogrid.registry.beans.v10.resource.ResourceName vFacility)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _facilityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _facilityList.set(index, vFacility);
    } //-- void setFacility(int, org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method setFacility
     * 
     * @param facilityArray
     */
    public void setFacility(org.astrogrid.registry.beans.v10.resource.ResourceName[] facilityArray)
    {
        //-- copy array
        _facilityList.clear();
        for (int i = 0; i < facilityArray.length; i++) {
            _facilityList.add(facilityArray[i]);
        }
    } //-- void setFacility(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method setFormat
     * 
     * @param index
     * @param vFormat
     */
    public void setFormat(int index, org.astrogrid.registry.beans.v10.resource.dataservice.Format vFormat)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formatList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _formatList.set(index, vFormat);
    } //-- void setFormat(int, org.astrogrid.registry.beans.v10.resource.dataservice.Format) 

    /**
     * Method setFormat
     * 
     * @param formatArray
     */
    public void setFormat(org.astrogrid.registry.beans.v10.resource.dataservice.Format[] formatArray)
    {
        //-- copy array
        _formatList.clear();
        for (int i = 0; i < formatArray.length; i++) {
            _formatList.add(formatArray[i]);
        }
    } //-- void setFormat(org.astrogrid.registry.beans.v10.resource.dataservice.Format) 

    /**
     * Method setInstrument
     * 
     * @param index
     * @param vInstrument
     */
    public void setInstrument(int index, org.astrogrid.registry.beans.v10.resource.ResourceName vInstrument)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _instrumentList.set(index, vInstrument);
    } //-- void setInstrument(int, org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method setInstrument
     * 
     * @param instrumentArray
     */
    public void setInstrument(org.astrogrid.registry.beans.v10.resource.ResourceName[] instrumentArray)
    {
        //-- copy array
        _instrumentList.clear();
        for (int i = 0; i < instrumentArray.length; i++) {
            _instrumentList.add(instrumentArray[i]);
        }
    } //-- void setInstrument(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method setRights
     * 
     * @param index
     * @param vRights
     */
    public void setRights(int index, org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights vRights)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rightsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _rightsList.set(index, vRights);
    } //-- void setRights(int, org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights) 

    /**
     * Method setRights
     * 
     * @param rightsArray
     */
    public void setRights(org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights[] rightsArray)
    {
        //-- copy array
        _rightsList.clear();
        for (int i = 0; i < rightsArray.length; i++) {
            _rightsList.add(rightsArray[i]);
        }
    } //-- void setRights(org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights) 

    /**
     * Method unmarshalDataCollection
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.DataCollection unmarshalDataCollection(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.DataCollection) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.DataCollection.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.DataCollection unmarshalDataCollection(java.io.Reader) 

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
