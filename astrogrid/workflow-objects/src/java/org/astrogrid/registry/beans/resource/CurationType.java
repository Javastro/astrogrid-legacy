/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CurationType.java,v 1.2 2004/07/01 10:18:32 nw Exp $
 */

package org.astrogrid.registry.beans.resource;

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
 * Class CurationType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/07/01 10:18:32 $
 */
public class CurationType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Entity (e.g. person or organisation) responsible for making
     * the 
     *  resource available
     *  
     */
    private org.astrogrid.registry.beans.resource.ResourceReferenceType _publisher;

    /**
     * Information that can be used for contacting someone with
     *  regard to this resource.
     *  
     */
    private org.astrogrid.registry.beans.resource.ContactType _contact;

    /**
     * Date associated with an event in the life cycle of the
     *  resource. 
     *  
     */
    private java.util.ArrayList _dateList;

    /**
     * The entity (e.g. person or organisation) primarily
     * responsible for
     *  creating the content or constitution of the resource
     *  
     */
    private org.astrogrid.registry.beans.resource.CreatorType _creator;

    /**
     * Entity responsible for contributions to the content of the
     * resource
     *  
     */
    private java.util.ArrayList _contributorList;

    /**
     * Label associated with creation or availablilty of a version
     * of 
     *  a resource.
     *  
     */
    private java.lang.String _version;


      //----------------/
     //- Constructors -/
    //----------------/

    public CurationType() {
        super();
        _dateList = new ArrayList();
        _contributorList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.CurationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addContributor
     * 
     * @param vContributor
     */
    public void addContributor(org.astrogrid.registry.beans.resource.NameReferenceType vContributor)
        throws java.lang.IndexOutOfBoundsException
    {
        _contributorList.add(vContributor);
    } //-- void addContributor(org.astrogrid.registry.beans.resource.NameReferenceType) 

    /**
     * Method addContributor
     * 
     * @param index
     * @param vContributor
     */
    public void addContributor(int index, org.astrogrid.registry.beans.resource.NameReferenceType vContributor)
        throws java.lang.IndexOutOfBoundsException
    {
        _contributorList.add(index, vContributor);
    } //-- void addContributor(int, org.astrogrid.registry.beans.resource.NameReferenceType) 

    /**
     * Method addDate
     * 
     * @param vDate
     */
    public void addDate(org.astrogrid.registry.beans.resource.DateType vDate)
        throws java.lang.IndexOutOfBoundsException
    {
        _dateList.add(vDate);
    } //-- void addDate(org.astrogrid.registry.beans.resource.DateType) 

    /**
     * Method addDate
     * 
     * @param index
     * @param vDate
     */
    public void addDate(int index, org.astrogrid.registry.beans.resource.DateType vDate)
        throws java.lang.IndexOutOfBoundsException
    {
        _dateList.add(index, vDate);
    } //-- void addDate(int, org.astrogrid.registry.beans.resource.DateType) 

    /**
     * Method clearContributor
     */
    public void clearContributor()
    {
        _contributorList.clear();
    } //-- void clearContributor() 

    /**
     * Method clearDate
     */
    public void clearDate()
    {
        _dateList.clear();
    } //-- void clearDate() 

    /**
     * Method enumerateContributor
     */
    public java.util.Enumeration enumerateContributor()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_contributorList.iterator());
    } //-- java.util.Enumeration enumerateContributor() 

    /**
     * Method enumerateDate
     */
    public java.util.Enumeration enumerateDate()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_dateList.iterator());
    } //-- java.util.Enumeration enumerateDate() 

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
        
        if (obj instanceof CurationType) {
        
            CurationType temp = (CurationType)obj;
            if (this._publisher != null) {
                if (temp._publisher == null) return false;
                else if (!(this._publisher.equals(temp._publisher))) 
                    return false;
            }
            else if (temp._publisher != null)
                return false;
            if (this._contact != null) {
                if (temp._contact == null) return false;
                else if (!(this._contact.equals(temp._contact))) 
                    return false;
            }
            else if (temp._contact != null)
                return false;
            if (this._dateList != null) {
                if (temp._dateList == null) return false;
                else if (!(this._dateList.equals(temp._dateList))) 
                    return false;
            }
            else if (temp._dateList != null)
                return false;
            if (this._creator != null) {
                if (temp._creator == null) return false;
                else if (!(this._creator.equals(temp._creator))) 
                    return false;
            }
            else if (temp._creator != null)
                return false;
            if (this._contributorList != null) {
                if (temp._contributorList == null) return false;
                else if (!(this._contributorList.equals(temp._contributorList))) 
                    return false;
            }
            else if (temp._contributorList != null)
                return false;
            if (this._version != null) {
                if (temp._version == null) return false;
                else if (!(this._version.equals(temp._version))) 
                    return false;
            }
            else if (temp._version != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'contact'. The field 'contact'
     * has the following description: Information that can be used
     * for contacting someone with
     *  regard to this resource.
     *  
     * 
     * @return the value of field 'contact'.
     */
    public org.astrogrid.registry.beans.resource.ContactType getContact()
    {
        return this._contact;
    } //-- org.astrogrid.registry.beans.resource.ContactType getContact() 

    /**
     * Method getContributor
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.NameReferenceType getContributor(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contributorList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.NameReferenceType) _contributorList.get(index);
    } //-- org.astrogrid.registry.beans.resource.NameReferenceType getContributor(int) 

    /**
     * Method getContributor
     */
    public org.astrogrid.registry.beans.resource.NameReferenceType[] getContributor()
    {
        int size = _contributorList.size();
        org.astrogrid.registry.beans.resource.NameReferenceType[] mArray = new org.astrogrid.registry.beans.resource.NameReferenceType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.NameReferenceType) _contributorList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.NameReferenceType[] getContributor() 

    /**
     * Method getContributorCount
     */
    public int getContributorCount()
    {
        return _contributorList.size();
    } //-- int getContributorCount() 

    /**
     * Returns the value of field 'creator'. The field 'creator'
     * has the following description: The entity (e.g. person or
     * organisation) primarily responsible for
     *  creating the content or constitution of the resource
     *  
     * 
     * @return the value of field 'creator'.
     */
    public org.astrogrid.registry.beans.resource.CreatorType getCreator()
    {
        return this._creator;
    } //-- org.astrogrid.registry.beans.resource.CreatorType getCreator() 

    /**
     * Method getDate
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.DateType getDate(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dateList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.DateType) _dateList.get(index);
    } //-- org.astrogrid.registry.beans.resource.DateType getDate(int) 

    /**
     * Method getDate
     */
    public org.astrogrid.registry.beans.resource.DateType[] getDate()
    {
        int size = _dateList.size();
        org.astrogrid.registry.beans.resource.DateType[] mArray = new org.astrogrid.registry.beans.resource.DateType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.DateType) _dateList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.DateType[] getDate() 

    /**
     * Method getDateCount
     */
    public int getDateCount()
    {
        return _dateList.size();
    } //-- int getDateCount() 

    /**
     * Returns the value of field 'publisher'. The field
     * 'publisher' has the following description: Entity (e.g.
     * person or organisation) responsible for making the 
     *  resource available
     *  
     * 
     * @return the value of field 'publisher'.
     */
    public org.astrogrid.registry.beans.resource.ResourceReferenceType getPublisher()
    {
        return this._publisher;
    } //-- org.astrogrid.registry.beans.resource.ResourceReferenceType getPublisher() 

    /**
     * Returns the value of field 'version'. The field 'version'
     * has the following description: Label associated with
     * creation or availablilty of a version of 
     *  a resource.
     *  
     * 
     * @return the value of field 'version'.
     */
    public java.lang.String getVersion()
    {
        return this._version;
    } //-- java.lang.String getVersion() 

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
     * Method removeContributor
     * 
     * @param vContributor
     */
    public boolean removeContributor(org.astrogrid.registry.beans.resource.NameReferenceType vContributor)
    {
        boolean removed = _contributorList.remove(vContributor);
        return removed;
    } //-- boolean removeContributor(org.astrogrid.registry.beans.resource.NameReferenceType) 

    /**
     * Method removeDate
     * 
     * @param vDate
     */
    public boolean removeDate(org.astrogrid.registry.beans.resource.DateType vDate)
    {
        boolean removed = _dateList.remove(vDate);
        return removed;
    } //-- boolean removeDate(org.astrogrid.registry.beans.resource.DateType) 

    /**
     * Sets the value of field 'contact'. The field 'contact' has
     * the following description: Information that can be used for
     * contacting someone with
     *  regard to this resource.
     *  
     * 
     * @param contact the value of field 'contact'.
     */
    public void setContact(org.astrogrid.registry.beans.resource.ContactType contact)
    {
        this._contact = contact;
    } //-- void setContact(org.astrogrid.registry.beans.resource.ContactType) 

    /**
     * Method setContributor
     * 
     * @param index
     * @param vContributor
     */
    public void setContributor(int index, org.astrogrid.registry.beans.resource.NameReferenceType vContributor)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contributorList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _contributorList.set(index, vContributor);
    } //-- void setContributor(int, org.astrogrid.registry.beans.resource.NameReferenceType) 

    /**
     * Method setContributor
     * 
     * @param contributorArray
     */
    public void setContributor(org.astrogrid.registry.beans.resource.NameReferenceType[] contributorArray)
    {
        //-- copy array
        _contributorList.clear();
        for (int i = 0; i < contributorArray.length; i++) {
            _contributorList.add(contributorArray[i]);
        }
    } //-- void setContributor(org.astrogrid.registry.beans.resource.NameReferenceType) 

    /**
     * Sets the value of field 'creator'. The field 'creator' has
     * the following description: The entity (e.g. person or
     * organisation) primarily responsible for
     *  creating the content or constitution of the resource
     *  
     * 
     * @param creator the value of field 'creator'.
     */
    public void setCreator(org.astrogrid.registry.beans.resource.CreatorType creator)
    {
        this._creator = creator;
    } //-- void setCreator(org.astrogrid.registry.beans.resource.CreatorType) 

    /**
     * Method setDate
     * 
     * @param index
     * @param vDate
     */
    public void setDate(int index, org.astrogrid.registry.beans.resource.DateType vDate)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dateList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _dateList.set(index, vDate);
    } //-- void setDate(int, org.astrogrid.registry.beans.resource.DateType) 

    /**
     * Method setDate
     * 
     * @param dateArray
     */
    public void setDate(org.astrogrid.registry.beans.resource.DateType[] dateArray)
    {
        //-- copy array
        _dateList.clear();
        for (int i = 0; i < dateArray.length; i++) {
            _dateList.add(dateArray[i]);
        }
    } //-- void setDate(org.astrogrid.registry.beans.resource.DateType) 

    /**
     * Sets the value of field 'publisher'. The field 'publisher'
     * has the following description: Entity (e.g. person or
     * organisation) responsible for making the 
     *  resource available
     *  
     * 
     * @param publisher the value of field 'publisher'.
     */
    public void setPublisher(org.astrogrid.registry.beans.resource.ResourceReferenceType publisher)
    {
        this._publisher = publisher;
    } //-- void setPublisher(org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Sets the value of field 'version'. The field 'version' has
     * the following description: Label associated with creation or
     * availablilty of a version of 
     *  a resource.
     *  
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(java.lang.String version)
    {
        this._version = version;
    } //-- void setVersion(java.lang.String) 

    /**
     * Method unmarshalCurationType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.CurationType unmarshalCurationType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.CurationType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.CurationType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.CurationType unmarshalCurationType(java.io.Reader) 

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
