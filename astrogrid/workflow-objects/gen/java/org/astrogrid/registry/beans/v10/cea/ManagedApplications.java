/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ManagedApplications.java,v 1.2 2005/07/05 08:26:57 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.cea;

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
 * Ths list of applications that a Common Execution Controller
 * Manages
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:57 $
 */
public class ManagedApplications extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * A reference to a CeaApplication
     */
    private java.util.ArrayList _applicationReferenceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ManagedApplications() {
        super();
        _applicationReferenceList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.cea.ManagedApplications()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addApplicationReference
     * 
     * @param vApplicationReference
     */
    public void addApplicationReference(java.lang.String vApplicationReference)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationReferenceList.add(vApplicationReference);
    } //-- void addApplicationReference(java.lang.String) 

    /**
     * Method addApplicationReference
     * 
     * @param index
     * @param vApplicationReference
     */
    public void addApplicationReference(int index, java.lang.String vApplicationReference)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationReferenceList.add(index, vApplicationReference);
    } //-- void addApplicationReference(int, java.lang.String) 

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
    public java.lang.String getApplicationReference(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationReferenceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_applicationReferenceList.get(index);
    } //-- java.lang.String getApplicationReference(int) 

    /**
     * Method getApplicationReference
     */
    public java.lang.String[] getApplicationReference()
    {
        int size = _applicationReferenceList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_applicationReferenceList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getApplicationReference() 

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
    public boolean removeApplicationReference(java.lang.String vApplicationReference)
    {
        boolean removed = _applicationReferenceList.remove(vApplicationReference);
        return removed;
    } //-- boolean removeApplicationReference(java.lang.String) 

    /**
     * Method setApplicationReference
     * 
     * @param index
     * @param vApplicationReference
     */
    public void setApplicationReference(int index, java.lang.String vApplicationReference)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationReferenceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _applicationReferenceList.set(index, vApplicationReference);
    } //-- void setApplicationReference(int, java.lang.String) 

    /**
     * Method setApplicationReference
     * 
     * @param applicationReferenceArray
     */
    public void setApplicationReference(java.lang.String[] applicationReferenceArray)
    {
        //-- copy array
        _applicationReferenceList.clear();
        for (int i = 0; i < applicationReferenceArray.length; i++) {
            _applicationReferenceList.add(applicationReferenceArray[i]);
        }
    } //-- void setApplicationReference(java.lang.String) 

    /**
     * Method unmarshalManagedApplications
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.cea.ManagedApplications unmarshalManagedApplications(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.cea.ManagedApplications) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.cea.ManagedApplications.class, reader);
    } //-- org.astrogrid.registry.beans.v10.cea.ManagedApplications unmarshalManagedApplications(java.io.Reader) 

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
