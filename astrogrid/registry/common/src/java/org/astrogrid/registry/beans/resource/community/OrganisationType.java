/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: OrganisationType.java,v 1.4 2004/03/09 09:45:24 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.community;

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
 * Class OrganisationType.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/09 09:45:24 $
 */
public class OrganisationType extends org.astrogrid.registry.beans.resource.ResourceType 
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

    public OrganisationType() {
        super();
        _facilityList = new ArrayList();
        _instrumentList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.community.OrganisationType()


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
     * Method unmarshalOrganisationType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.community.OrganisationType unmarshalOrganisationType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.community.OrganisationType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.community.OrganisationType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.community.OrganisationType unmarshalOrganisationType(java.io.Reader) 

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
