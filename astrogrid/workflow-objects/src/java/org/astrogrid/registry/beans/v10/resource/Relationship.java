/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Relationship.java,v 1.2 2007/01/04 16:26:24 clq2 Exp $
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
 * A description of the relationship between one resource and one
 * or
 *  more other resources.
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:24 $
 */
public class Relationship extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the named type of relationship
     *  
     */
    private java.lang.String _relationshipType;

    /**
     * the name of resource that this resource is related to.
     *  
     */
    private java.util.ArrayList _relatedResourceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Relationship() {
        super();
        _relatedResourceList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.resource.Relationship()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRelatedResource
     * 
     * @param vRelatedResource
     */
    public void addRelatedResource(org.astrogrid.registry.beans.v10.resource.ResourceName vRelatedResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _relatedResourceList.add(vRelatedResource);
    } //-- void addRelatedResource(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method addRelatedResource
     * 
     * @param index
     * @param vRelatedResource
     */
    public void addRelatedResource(int index, org.astrogrid.registry.beans.v10.resource.ResourceName vRelatedResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _relatedResourceList.add(index, vRelatedResource);
    } //-- void addRelatedResource(int, org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method clearRelatedResource
     */
    public void clearRelatedResource()
    {
        _relatedResourceList.clear();
    } //-- void clearRelatedResource() 

    /**
     * Method enumerateRelatedResource
     */
    public java.util.Enumeration enumerateRelatedResource()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_relatedResourceList.iterator());
    } //-- java.util.Enumeration enumerateRelatedResource() 

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
        
        if (obj instanceof Relationship) {
        
            Relationship temp = (Relationship)obj;
            if (this._relationshipType != null) {
                if (temp._relationshipType == null) return false;
                else if (!(this._relationshipType.equals(temp._relationshipType))) 
                    return false;
            }
            else if (temp._relationshipType != null)
                return false;
            if (this._relatedResourceList != null) {
                if (temp._relatedResourceList == null) return false;
                else if (!(this._relatedResourceList.equals(temp._relatedResourceList))) 
                    return false;
            }
            else if (temp._relatedResourceList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getRelatedResource
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.ResourceName getRelatedResource(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relatedResourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.ResourceName) _relatedResourceList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.ResourceName getRelatedResource(int) 

    /**
     * Method getRelatedResource
     */
    public org.astrogrid.registry.beans.v10.resource.ResourceName[] getRelatedResource()
    {
        int size = _relatedResourceList.size();
        org.astrogrid.registry.beans.v10.resource.ResourceName[] mArray = new org.astrogrid.registry.beans.v10.resource.ResourceName[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.ResourceName) _relatedResourceList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.ResourceName[] getRelatedResource() 

    /**
     * Method getRelatedResourceCount
     */
    public int getRelatedResourceCount()
    {
        return _relatedResourceList.size();
    } //-- int getRelatedResourceCount() 

    /**
     * Returns the value of field 'relationshipType'. The field
     * 'relationshipType' has the following description: the named
     * type of relationship
     *  
     * 
     * @return the value of field 'relationshipType'.
     */
    public java.lang.String getRelationshipType()
    {
        return this._relationshipType;
    } //-- java.lang.String getRelationshipType() 

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
     * Method removeRelatedResource
     * 
     * @param vRelatedResource
     */
    public boolean removeRelatedResource(org.astrogrid.registry.beans.v10.resource.ResourceName vRelatedResource)
    {
        boolean removed = _relatedResourceList.remove(vRelatedResource);
        return removed;
    } //-- boolean removeRelatedResource(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method setRelatedResource
     * 
     * @param index
     * @param vRelatedResource
     */
    public void setRelatedResource(int index, org.astrogrid.registry.beans.v10.resource.ResourceName vRelatedResource)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relatedResourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _relatedResourceList.set(index, vRelatedResource);
    } //-- void setRelatedResource(int, org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method setRelatedResource
     * 
     * @param relatedResourceArray
     */
    public void setRelatedResource(org.astrogrid.registry.beans.v10.resource.ResourceName[] relatedResourceArray)
    {
        //-- copy array
        _relatedResourceList.clear();
        for (int i = 0; i < relatedResourceArray.length; i++) {
            _relatedResourceList.add(relatedResourceArray[i]);
        }
    } //-- void setRelatedResource(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Sets the value of field 'relationshipType'. The field
     * 'relationshipType' has the following description: the named
     * type of relationship
     *  
     * 
     * @param relationshipType the value of field 'relationshipType'
     */
    public void setRelationshipType(java.lang.String relationshipType)
    {
        this._relationshipType = relationshipType;
    } //-- void setRelationshipType(java.lang.String) 

    /**
     * Method unmarshalRelationship
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.Relationship unmarshalRelationship(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.Relationship) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.Relationship.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.Relationship unmarshalRelationship(java.io.Reader) 

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
