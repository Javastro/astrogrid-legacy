/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Service.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
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
 * a resource that can be invoked by a client to perform some
 * action
 *  on its behalf. 
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:56 $
 */
public class Service extends org.astrogrid.registry.beans.v10.resource.Resource 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a description of how a client invokes the service
     *  
     */
    private java.util.ArrayList _interfaceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Service() {
        super();
        _interfaceList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.resource.Service()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method add_interface
     * 
     * @param v_interface
     */
    public void add_interface(org.astrogrid.registry.beans.v10.resource.Interface v_interface)
        throws java.lang.IndexOutOfBoundsException
    {
        _interfaceList.add(v_interface);
    } //-- void add_interface(org.astrogrid.registry.beans.v10.resource.Interface) 

    /**
     * Method add_interface
     * 
     * @param index
     * @param v_interface
     */
    public void add_interface(int index, org.astrogrid.registry.beans.v10.resource.Interface v_interface)
        throws java.lang.IndexOutOfBoundsException
    {
        _interfaceList.add(index, v_interface);
    } //-- void add_interface(int, org.astrogrid.registry.beans.v10.resource.Interface) 

    /**
     * Method clear_interface
     */
    public void clear_interface()
    {
        _interfaceList.clear();
    } //-- void clear_interface() 

    /**
     * Method enumerate_interface
     */
    public java.util.Enumeration enumerate_interface()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_interfaceList.iterator());
    } //-- java.util.Enumeration enumerate_interface() 

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
        
        if (obj instanceof Service) {
        
            Service temp = (Service)obj;
            if (this._interfaceList != null) {
                if (temp._interfaceList == null) return false;
                else if (!(this._interfaceList.equals(temp._interfaceList))) 
                    return false;
            }
            else if (temp._interfaceList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method get_interface
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.Interface get_interface(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _interfaceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.Interface) _interfaceList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.Interface get_interface(int) 

    /**
     * Method get_interface
     */
    public org.astrogrid.registry.beans.v10.resource.Interface[] get_interface()
    {
        int size = _interfaceList.size();
        org.astrogrid.registry.beans.v10.resource.Interface[] mArray = new org.astrogrid.registry.beans.v10.resource.Interface[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.Interface) _interfaceList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.Interface[] get_interface() 

    /**
     * Method get_interfaceCount
     */
    public int get_interfaceCount()
    {
        return _interfaceList.size();
    } //-- int get_interfaceCount() 

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
     * Method remove_interface
     * 
     * @param v_interface
     */
    public boolean remove_interface(org.astrogrid.registry.beans.v10.resource.Interface v_interface)
    {
        boolean removed = _interfaceList.remove(v_interface);
        return removed;
    } //-- boolean remove_interface(org.astrogrid.registry.beans.v10.resource.Interface) 

    /**
     * Method set_interface
     * 
     * @param index
     * @param v_interface
     */
    public void set_interface(int index, org.astrogrid.registry.beans.v10.resource.Interface v_interface)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _interfaceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _interfaceList.set(index, v_interface);
    } //-- void set_interface(int, org.astrogrid.registry.beans.v10.resource.Interface) 

    /**
     * Method set_interface
     * 
     * @param _interfaceArray
     */
    public void set_interface(org.astrogrid.registry.beans.v10.resource.Interface[] _interfaceArray)
    {
        //-- copy array
        _interfaceList.clear();
        for (int i = 0; i < _interfaceArray.length; i++) {
            _interfaceList.add(_interfaceArray[i]);
        }
    } //-- void set_interface(org.astrogrid.registry.beans.v10.resource.Interface) 

    /**
     * Method unmarshalService
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.Service unmarshalService(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.Service) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.Service.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.Service unmarshalService(java.io.Reader) 

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
