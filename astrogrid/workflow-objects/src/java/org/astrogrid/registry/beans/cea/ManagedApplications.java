/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ManagedApplications.java,v 1.3 2004/03/30 22:42:55 pah Exp $
 */

package org.astrogrid.registry.beans.cea;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.registry.beans.resource.IdentifierType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Ths list of applications that a Common Execution Controller
 * Manages
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/30 22:42:55 $
 */
public class ManagedApplications extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _applicationReferenceList
     */
    private java.util.ArrayList _applicationReferenceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ManagedApplications() {
        super();
        _applicationReferenceList = new ArrayList();
    } //-- org.astrogrid.registry.beans.cea.ManagedApplications()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addApplicationReference
     * 
     * @param vApplicationReference
     */
    public void addApplicationReference(org.astrogrid.registry.beans.resource.IdentifierType vApplicationReference)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationReferenceList.add(vApplicationReference);
    } //-- void addApplicationReference(org.astrogrid.registry.beans.resource.IdentifierType) 

    /**
     * Method addApplicationReference
     * 
     * @param index
     * @param vApplicationReference
     */
    public void addApplicationReference(int index, org.astrogrid.registry.beans.resource.IdentifierType vApplicationReference)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationReferenceList.add(index, vApplicationReference);
    } //-- void addApplicationReference(int, org.astrogrid.registry.beans.resource.IdentifierType) 

    /**
     * Method clearApplicationReference
     */
    public void clearApplicationReference()
    {
        _applicationReferenceList.clear();
    } //-- void clearApplicationReference() 

    /**
     * Method enumerateApplicationReference
     */
    public java.util.Enumeration enumerateApplicationReference()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_applicationReferenceList.iterator());
    } //-- java.util.Enumeration enumerateApplicationReference() 

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
        
        if (obj instanceof ManagedApplications) {
        
            ManagedApplications temp = (ManagedApplications)obj;
            if (this._applicationReferenceList != null) {
                if (temp._applicationReferenceList == null) return false;
                else if (!(this._applicationReferenceList.equals(temp._applicationReferenceList))) 
                    return false;
            }
            else if (temp._applicationReferenceList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getApplicationReference
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.IdentifierType getApplicationReference(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationReferenceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.IdentifierType) _applicationReferenceList.get(index);
    } //-- org.astrogrid.registry.beans.resource.IdentifierType getApplicationReference(int) 

    /**
     * Method getApplicationReference
     */
    public org.astrogrid.registry.beans.resource.IdentifierType[] getApplicationReference()
    {
        int size = _applicationReferenceList.size();
        org.astrogrid.registry.beans.resource.IdentifierType[] mArray = new org.astrogrid.registry.beans.resource.IdentifierType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.IdentifierType) _applicationReferenceList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.IdentifierType[] getApplicationReference() 

    /**
     * Method getApplicationReferenceCount
     */
    public int getApplicationReferenceCount()
    {
        return _applicationReferenceList.size();
    } //-- int getApplicationReferenceCount() 

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
     * Method removeApplicationReference
     * 
     * @param vApplicationReference
     */
    public boolean removeApplicationReference(org.astrogrid.registry.beans.resource.IdentifierType vApplicationReference)
    {
        boolean removed = _applicationReferenceList.remove(vApplicationReference);
        return removed;
    } //-- boolean removeApplicationReference(org.astrogrid.registry.beans.resource.IdentifierType) 

    /**
     * Method setApplicationReference
     * 
     * @param index
     * @param vApplicationReference
     */
    public void setApplicationReference(int index, org.astrogrid.registry.beans.resource.IdentifierType vApplicationReference)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationReferenceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _applicationReferenceList.set(index, vApplicationReference);
    } //-- void setApplicationReference(int, org.astrogrid.registry.beans.resource.IdentifierType) 

    /**
     * Method setApplicationReference
     * 
     * @param applicationReferenceArray
     */
    public void setApplicationReference(org.astrogrid.registry.beans.resource.IdentifierType[] applicationReferenceArray)
    {
        //-- copy array
        _applicationReferenceList.clear();
        for (int i = 0; i < applicationReferenceArray.length; i++) {
            _applicationReferenceList.add(applicationReferenceArray[i]);
        }
    } //-- void setApplicationReference(org.astrogrid.registry.beans.resource.IdentifierType) 

    /**
     * Method unmarshalManagedApplications
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.cea.ManagedApplications unmarshalManagedApplications(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.cea.ManagedApplications) Unmarshaller.unmarshal(org.astrogrid.registry.beans.cea.ManagedApplications.class, reader);
    } //-- org.astrogrid.registry.beans.cea.ManagedApplications unmarshalManagedApplications(java.io.Reader) 

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
