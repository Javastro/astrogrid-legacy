/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: VODescription.java,v 1.2 2004/03/03 16:22:08 KevinBenson Exp $
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
 * A description of one or more VO Resources
 *  This element is used as a general container for multiple
 * resource
 *  descriptions and can be used as a root element.
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/03 16:22:08 $
 */
public class VODescription extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Any entity that is describable and identifiable by a IVOA
     * Identifier.
     *  
     */
    private java.util.ArrayList _resourceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VODescription() {
        super();
        _resourceList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.VODescription()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addResource
     * 
     * @param vResource
     */
    public void addResource(org.astrogrid.registry.beans.resource.ResourceType vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _resourceList.add(vResource);
    } //-- void addResource(org.astrogrid.registry.beans.resource.ResourceType) 

    /**
     * Method addResource
     * 
     * @param index
     * @param vResource
     */
    public void addResource(int index, org.astrogrid.registry.beans.resource.ResourceType vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _resourceList.add(index, vResource);
    } //-- void addResource(int, org.astrogrid.registry.beans.resource.ResourceType) 

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
     * Method getResource
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.ResourceType getResource(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.ResourceType) _resourceList.get(index);
    } //-- org.astrogrid.registry.beans.resource.ResourceType getResource(int) 

    /**
     * Method getResource
     */
    public org.astrogrid.registry.beans.resource.ResourceType[] getResource()
    {
        int size = _resourceList.size();
        org.astrogrid.registry.beans.resource.ResourceType[] mArray = new org.astrogrid.registry.beans.resource.ResourceType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.ResourceType) _resourceList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.ResourceType[] getResource() 

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
    public boolean removeResource(org.astrogrid.registry.beans.resource.ResourceType vResource)
    {
        boolean removed = _resourceList.remove(vResource);
        return removed;
    } //-- boolean removeResource(org.astrogrid.registry.beans.resource.ResourceType) 

    /**
     * Method setResource
     * 
     * @param index
     * @param vResource
     */
    public void setResource(int index, org.astrogrid.registry.beans.resource.ResourceType vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _resourceList.set(index, vResource);
    } //-- void setResource(int, org.astrogrid.registry.beans.resource.ResourceType) 

    /**
     * Method setResource
     * 
     * @param resourceArray
     */
    public void setResource(org.astrogrid.registry.beans.resource.ResourceType[] resourceArray)
    {
        //-- copy array
        _resourceList.clear();
        for (int i = 0; i < resourceArray.length; i++) {
            _resourceList.add(resourceArray[i]);
        }
    } //-- void setResource(org.astrogrid.registry.beans.resource.ResourceType) 

    /**
     * Method unmarshalVODescription
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.VODescription unmarshalVODescription(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.VODescription) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class, reader);
    } //-- org.astrogrid.registry.beans.resource.VODescription unmarshalVODescription(java.io.Reader) 

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
