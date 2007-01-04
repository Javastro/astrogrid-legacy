/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: RelatedResourceType.java,v 1.14 2007/01/04 16:26:22 clq2 Exp $
 */

package org.astrogrid.registry.beans.resource;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class RelatedResourceType.
 * 
 * @version $Revision: 1.14 $ $Date: 2007/01/04 16:26:22 $
 */
public class RelatedResourceType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a property that relates one resource to another.
     *  
     */
    private java.lang.String _relationship;

    /**
     * the resource that is related to the current one being
     * described.
     *  
     */
    private org.astrogrid.registry.beans.resource.ResourceReferenceType _relatedTo;


      //----------------/
     //- Constructors -/
    //----------------/

    public RelatedResourceType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.RelatedResourceType()


      //-----------/
     //- Methods -/
    //-----------/

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
        
        if (obj instanceof RelatedResourceType) {
        
            RelatedResourceType temp = (RelatedResourceType)obj;
            if (this._relationship != null) {
                if (temp._relationship == null) return false;
                else if (!(this._relationship.equals(temp._relationship))) 
                    return false;
            }
            else if (temp._relationship != null)
                return false;
            if (this._relatedTo != null) {
                if (temp._relatedTo == null) return false;
                else if (!(this._relatedTo.equals(temp._relatedTo))) 
                    return false;
            }
            else if (temp._relatedTo != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'relatedTo'. The field
     * 'relatedTo' has the following description: the resource that
     * is related to the current one being described.
     *  
     * 
     * @return the value of field 'relatedTo'.
     */
    public org.astrogrid.registry.beans.resource.ResourceReferenceType getRelatedTo()
    {
        return this._relatedTo;
    } //-- org.astrogrid.registry.beans.resource.ResourceReferenceType getRelatedTo() 

    /**
     * Returns the value of field 'relationship'. The field
     * 'relationship' has the following description: a property
     * that relates one resource to another.
     *  
     * 
     * @return the value of field 'relationship'.
     */
    public java.lang.String getRelationship()
    {
        return this._relationship;
    } //-- java.lang.String getRelationship() 

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
     * Sets the value of field 'relatedTo'. The field 'relatedTo'
     * has the following description: the resource that is related
     * to the current one being described.
     *  
     * 
     * @param relatedTo the value of field 'relatedTo'.
     */
    public void setRelatedTo(org.astrogrid.registry.beans.resource.ResourceReferenceType relatedTo)
    {
        this._relatedTo = relatedTo;
    } //-- void setRelatedTo(org.astrogrid.registry.beans.resource.ResourceReferenceType) 

    /**
     * Sets the value of field 'relationship'. The field
     * 'relationship' has the following description: a property
     * that relates one resource to another.
     *  
     * 
     * @param relationship the value of field 'relationship'.
     */
    public void setRelationship(java.lang.String relationship)
    {
        this._relationship = relationship;
    } //-- void setRelationship(java.lang.String) 

    /**
     * Method unmarshalRelatedResourceType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.RelatedResourceType unmarshalRelatedResourceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.RelatedResourceType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.RelatedResourceType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.RelatedResourceType unmarshalRelatedResourceType(java.io.Reader) 

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
