/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Registry.java,v 1.2 2007/01/04 16:26:34 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.registry;

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
 * a service that provides access to descriptions of resources.
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:34 $
 */
public class Registry extends org.astrogrid.registry.beans.v10.resource.Service 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * an authority identifier managed by a registry.
     *  
     */
    private java.util.ArrayList _managedAuthorityList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Registry() {
        super();
        _managedAuthorityList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.resource.registry.Registry()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addManagedAuthority
     * 
     * @param vManagedAuthority
     */
    public void addManagedAuthority(java.lang.String vManagedAuthority)
        throws java.lang.IndexOutOfBoundsException
    {
        _managedAuthorityList.add(vManagedAuthority);
    } //-- void addManagedAuthority(java.lang.String) 

    /**
     * Method addManagedAuthority
     * 
     * @param index
     * @param vManagedAuthority
     */
    public void addManagedAuthority(int index, java.lang.String vManagedAuthority)
        throws java.lang.IndexOutOfBoundsException
    {
        _managedAuthorityList.add(index, vManagedAuthority);
    } //-- void addManagedAuthority(int, java.lang.String) 

    /**
     * Method clearManagedAuthority
     */
    public void clearManagedAuthority()
    {
        _managedAuthorityList.clear();
    } //-- void clearManagedAuthority() 

    /**
     * Method enumerateManagedAuthority
     */
    public java.util.Enumeration enumerateManagedAuthority()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_managedAuthorityList.iterator());
    } //-- java.util.Enumeration enumerateManagedAuthority() 

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
        
        if (obj instanceof Registry) {
        
            Registry temp = (Registry)obj;
            if (this._managedAuthorityList != null) {
                if (temp._managedAuthorityList == null) return false;
                else if (!(this._managedAuthorityList.equals(temp._managedAuthorityList))) 
                    return false;
            }
            else if (temp._managedAuthorityList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getManagedAuthority
     * 
     * @param index
     */
    public java.lang.String getManagedAuthority(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _managedAuthorityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_managedAuthorityList.get(index);
    } //-- java.lang.String getManagedAuthority(int) 

    /**
     * Method getManagedAuthority
     */
    public java.lang.String[] getManagedAuthority()
    {
        int size = _managedAuthorityList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_managedAuthorityList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getManagedAuthority() 

    /**
     * Method getManagedAuthorityCount
     */
    public int getManagedAuthorityCount()
    {
        return _managedAuthorityList.size();
    } //-- int getManagedAuthorityCount() 

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
     * Method removeManagedAuthority
     * 
     * @param vManagedAuthority
     */
    public boolean removeManagedAuthority(java.lang.String vManagedAuthority)
    {
        boolean removed = _managedAuthorityList.remove(vManagedAuthority);
        return removed;
    } //-- boolean removeManagedAuthority(java.lang.String) 

    /**
     * Method setManagedAuthority
     * 
     * @param index
     * @param vManagedAuthority
     */
    public void setManagedAuthority(int index, java.lang.String vManagedAuthority)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _managedAuthorityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _managedAuthorityList.set(index, vManagedAuthority);
    } //-- void setManagedAuthority(int, java.lang.String) 

    /**
     * Method setManagedAuthority
     * 
     * @param managedAuthorityArray
     */
    public void setManagedAuthority(java.lang.String[] managedAuthorityArray)
    {
        //-- copy array
        _managedAuthorityList.clear();
        for (int i = 0; i < managedAuthorityArray.length; i++) {
            _managedAuthorityList.add(managedAuthorityArray[i]);
        }
    } //-- void setManagedAuthority(java.lang.String) 

    /**
     * Method unmarshalRegistry
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.registry.Registry unmarshalRegistry(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.registry.Registry) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.registry.Registry.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.registry.Registry unmarshalRegistry(java.io.Reader) 

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
