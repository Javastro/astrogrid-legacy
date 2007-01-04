/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: VOResources.java,v 1.2 2007/01/04 16:26:38 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.wsinterface;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.registry.beans.v10.resource.Resource;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class VOResources.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:38 $
 */
public class VOResources extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resourceList
     */
    private java.util.ArrayList _resourceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VOResources() {
        super();
        _resourceList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.wsinterface.VOResources()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addResource
     * 
     * @param vResource
     */
    public void addResource(org.astrogrid.registry.beans.v10.resource.Resource vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _resourceList.add(vResource);
    } //-- void addResource(org.astrogrid.registry.beans.v10.resource.Resource) 

    /**
     * Method addResource
     * 
     * @param index
     * @param vResource
     */
    public void addResource(int index, org.astrogrid.registry.beans.v10.resource.Resource vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _resourceList.add(index, vResource);
    } //-- void addResource(int, org.astrogrid.registry.beans.v10.resource.Resource) 

    /**
     * Method clearResource
     */
    public void clearResource()
    {
        _resourceList.clear();
    } //-- void clearResource() 

    /**
     * Method enumerateResource
     */
    public java.util.Enumeration enumerateResource()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_resourceList.iterator());
    } //-- java.util.Enumeration enumerateResource() 

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
        
        if (obj instanceof VOResources) {
        
            VOResources temp = (VOResources)obj;
            if (this._resourceList != null) {
                if (temp._resourceList == null) return false;
                else if (!(this._resourceList.equals(temp._resourceList))) 
                    return false;
            }
            else if (temp._resourceList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getResource
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.Resource getResource(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.Resource) _resourceList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.Resource getResource(int) 

    /**
     * Method getResource
     */
    public org.astrogrid.registry.beans.v10.resource.Resource[] getResource()
    {
        int size = _resourceList.size();
        org.astrogrid.registry.beans.v10.resource.Resource[] mArray = new org.astrogrid.registry.beans.v10.resource.Resource[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.Resource) _resourceList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.Resource[] getResource() 

    /**
     * Method getResourceCount
     */
    public int getResourceCount()
    {
        return _resourceList.size();
    } //-- int getResourceCount() 

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
     * Method removeResource
     * 
     * @param vResource
     */
    public boolean removeResource(org.astrogrid.registry.beans.v10.resource.Resource vResource)
    {
        boolean removed = _resourceList.remove(vResource);
        return removed;
    } //-- boolean removeResource(org.astrogrid.registry.beans.v10.resource.Resource) 

    /**
     * Method setResource
     * 
     * @param index
     * @param vResource
     */
    public void setResource(int index, org.astrogrid.registry.beans.v10.resource.Resource vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _resourceList.set(index, vResource);
    } //-- void setResource(int, org.astrogrid.registry.beans.v10.resource.Resource) 

    /**
     * Method setResource
     * 
     * @param resourceArray
     */
    public void setResource(org.astrogrid.registry.beans.v10.resource.Resource[] resourceArray)
    {
        //-- copy array
        _resourceList.clear();
        for (int i = 0; i < resourceArray.length; i++) {
            _resourceList.add(resourceArray[i]);
        }
    } //-- void setResource(org.astrogrid.registry.beans.v10.resource.Resource) 

    /**
     * Method unmarshalVOResources
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.wsinterface.VOResources unmarshalVOResources(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.wsinterface.VOResources) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.wsinterface.VOResources.class, reader);
    } //-- org.astrogrid.registry.beans.v10.wsinterface.VOResources unmarshalVOResources(java.io.Reader) 

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
