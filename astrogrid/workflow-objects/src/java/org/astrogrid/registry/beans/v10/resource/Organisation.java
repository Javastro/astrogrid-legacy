/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Organisation.java,v 1.2 2007/01/04 16:26:24 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource;

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
 * A named group of one or more persons brought together to pursue 
 *  participation in VO applications. 
 *  
 *  According to the Resource Metadata Recommendation,
 * organisations 
 *  "can be hierarchical and range in size and scope. At a high
 * level, 
 *  an organisation could be a university, observatory, or
 * government
 *  agency. At a finer level, it could be a specific scientific 
 *  project, mission, or individual researcher." 
 *  
 *  The main purpose of an organisation as a registered resource is
 * 
 *  to serve as a publisher of other resources. 
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:24 $
 */
public class Organisation extends org.astrogrid.registry.beans.v10.resource.Resource 
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


      //----------------/
     //- Constructors -/
    //----------------/

    public Organisation() {
        super();
        _facilityList = new ArrayList();
        _instrumentList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.resource.Organisation()


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
        
        if (obj instanceof Organisation) {
        
            Organisation temp = (Organisation)obj;
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
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

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
     * Method unmarshalOrganisation
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.Organisation unmarshalOrganisation(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.Organisation) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.Organisation.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.Organisation unmarshalOrganisation(java.io.Reader) 

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
