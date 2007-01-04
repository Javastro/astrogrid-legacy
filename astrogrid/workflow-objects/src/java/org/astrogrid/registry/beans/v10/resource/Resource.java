/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Resource.java,v 1.2 2007/01/04 16:26:24 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Any entity that is describable and identifiable by a IVOA
 * Identifier.
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:24 $
 */
public class Resource extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The date this resource metadata description was created.
     *  
     */
    private org.exolab.castor.types.Date _created;

    /**
     * The date this resource metadata description was last
     * updated.
     *  
     */
    private org.exolab.castor.types.Date _updated;

    /**
     * a tag indicating whether this resource is believed to be
     * still
     *  actively maintained.
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType _status = org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType.valueOf("active");

    /**
     * the full name given to the resource
     *  
     */
    private java.lang.String _title;

    /**
     * a short name or abbreviation given to the resource.
     *  
     */
    private java.lang.String _shortName;

    /**
     * Unambiguous reference to the resource conforming to the IVOA
     *  standard for identifiers
     *  
     */
    private java.lang.String _identifier;

    /**
     * Information regarding the general curation of the resource
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.Curation _curation;

    /**
     * Information regarding the general content of the resource
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.Content _content;


      //----------------/
     //- Constructors -/
    //----------------/

    public Resource() {
        super();
        setStatus(org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType.valueOf("active"));
    } //-- org.astrogrid.registry.beans.v10.resource.Resource()


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
        
        if (obj instanceof Resource) {
        
            Resource temp = (Resource)obj;
            if (this._created != null) {
                if (temp._created == null) return false;
                else if (!(this._created.equals(temp._created))) 
                    return false;
            }
            else if (temp._created != null)
                return false;
            if (this._updated != null) {
                if (temp._updated == null) return false;
                else if (!(this._updated.equals(temp._updated))) 
                    return false;
            }
            else if (temp._updated != null)
                return false;
            if (this._status != null) {
                if (temp._status == null) return false;
                else if (!(this._status.equals(temp._status))) 
                    return false;
            }
            else if (temp._status != null)
                return false;
            if (this._title != null) {
                if (temp._title == null) return false;
                else if (!(this._title.equals(temp._title))) 
                    return false;
            }
            else if (temp._title != null)
                return false;
            if (this._shortName != null) {
                if (temp._shortName == null) return false;
                else if (!(this._shortName.equals(temp._shortName))) 
                    return false;
            }
            else if (temp._shortName != null)
                return false;
            if (this._identifier != null) {
                if (temp._identifier == null) return false;
                else if (!(this._identifier.equals(temp._identifier))) 
                    return false;
            }
            else if (temp._identifier != null)
                return false;
            if (this._curation != null) {
                if (temp._curation == null) return false;
                else if (!(this._curation.equals(temp._curation))) 
                    return false;
            }
            else if (temp._curation != null)
                return false;
            if (this._content != null) {
                if (temp._content == null) return false;
                else if (!(this._content.equals(temp._content))) 
                    return false;
            }
            else if (temp._content != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: Information regarding the
     * general content of the resource
     *  
     * 
     * @return the value of field 'content'.
     */
    public org.astrogrid.registry.beans.v10.resource.Content getContent()
    {
        return this._content;
    } //-- org.astrogrid.registry.beans.v10.resource.Content getContent() 

    /**
     * Returns the value of field 'created'. The field 'created'
     * has the following description: The date this resource
     * metadata description was created.
     *  
     * 
     * @return the value of field 'created'.
     */
    public org.exolab.castor.types.Date getCreated()
    {
        return this._created;
    } //-- org.exolab.castor.types.Date getCreated() 

    /**
     * Returns the value of field 'curation'. The field 'curation'
     * has the following description: Information regarding the
     * general curation of the resource
     *  
     * 
     * @return the value of field 'curation'.
     */
    public org.astrogrid.registry.beans.v10.resource.Curation getCuration()
    {
        return this._curation;
    } //-- org.astrogrid.registry.beans.v10.resource.Curation getCuration() 

    /**
     * Returns the value of field 'identifier'. The field
     * 'identifier' has the following description: Unambiguous
     * reference to the resource conforming to the IVOA
     *  standard for identifiers
     *  
     * 
     * @return the value of field 'identifier'.
     */
    public java.lang.String getIdentifier()
    {
        return this._identifier;
    } //-- java.lang.String getIdentifier() 

    /**
     * Returns the value of field 'shortName'. The field
     * 'shortName' has the following description: a short name or
     * abbreviation given to the resource.
     *  
     * 
     * @return the value of field 'shortName'.
     */
    public java.lang.String getShortName()
    {
        return this._shortName;
    } //-- java.lang.String getShortName() 

    /**
     * Returns the value of field 'status'. The field 'status' has
     * the following description: a tag indicating whether this
     * resource is believed to be still
     *  actively maintained.
     *  
     * 
     * @return the value of field 'status'.
     */
    public org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType getStatus()
    {
        return this._status;
    } //-- org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType getStatus() 

    /**
     * Returns the value of field 'title'. The field 'title' has
     * the following description: the full name given to the
     * resource
     *  
     * 
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

    /**
     * Returns the value of field 'updated'. The field 'updated'
     * has the following description: The date this resource
     * metadata description was last updated.
     *  
     * 
     * @return the value of field 'updated'.
     */
    public org.exolab.castor.types.Date getUpdated()
    {
        return this._updated;
    } //-- org.exolab.castor.types.Date getUpdated() 

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
     * Sets the value of field 'content'. The field 'content' has
     * the following description: Information regarding the general
     * content of the resource
     *  
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(org.astrogrid.registry.beans.v10.resource.Content content)
    {
        this._content = content;
    } //-- void setContent(org.astrogrid.registry.beans.v10.resource.Content) 

    /**
     * Sets the value of field 'created'. The field 'created' has
     * the following description: The date this resource metadata
     * description was created.
     *  
     * 
     * @param created the value of field 'created'.
     */
    public void setCreated(org.exolab.castor.types.Date created)
    {
        this._created = created;
    } //-- void setCreated(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'curation'. The field 'curation' has
     * the following description: Information regarding the general
     * curation of the resource
     *  
     * 
     * @param curation the value of field 'curation'.
     */
    public void setCuration(org.astrogrid.registry.beans.v10.resource.Curation curation)
    {
        this._curation = curation;
    } //-- void setCuration(org.astrogrid.registry.beans.v10.resource.Curation) 

    /**
     * Sets the value of field 'identifier'. The field 'identifier'
     * has the following description: Unambiguous reference to the
     * resource conforming to the IVOA
     *  standard for identifiers
     *  
     * 
     * @param identifier the value of field 'identifier'.
     */
    public void setIdentifier(java.lang.String identifier)
    {
        this._identifier = identifier;
    } //-- void setIdentifier(java.lang.String) 

    /**
     * Sets the value of field 'shortName'. The field 'shortName'
     * has the following description: a short name or abbreviation
     * given to the resource.
     *  
     * 
     * @param shortName the value of field 'shortName'.
     */
    public void setShortName(java.lang.String shortName)
    {
        this._shortName = shortName;
    } //-- void setShortName(java.lang.String) 

    /**
     * Sets the value of field 'status'. The field 'status' has the
     * following description: a tag indicating whether this
     * resource is believed to be still
     *  actively maintained.
     *  
     * 
     * @param status the value of field 'status'.
     */
    public void setStatus(org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType status)
    {
        this._status = status;
    } //-- void setStatus(org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType) 

    /**
     * Sets the value of field 'title'. The field 'title' has the
     * following description: the full name given to the resource
     *  
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * Sets the value of field 'updated'. The field 'updated' has
     * the following description: The date this resource metadata
     * description was last updated.
     *  
     * 
     * @param updated the value of field 'updated'.
     */
    public void setUpdated(org.exolab.castor.types.Date updated)
    {
        this._updated = updated;
    } //-- void setUpdated(org.exolab.castor.types.Date) 

    /**
     * Method unmarshalResource
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.Resource unmarshalResource(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.Resource) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.Resource.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.Resource unmarshalResource(java.io.Reader) 

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
