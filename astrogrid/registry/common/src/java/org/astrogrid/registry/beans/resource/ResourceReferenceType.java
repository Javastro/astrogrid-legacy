/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ResourceReferenceType.java,v 1.6 2004/03/19 08:16:47 KevinBenson Exp $
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
 * Class ResourceReferenceType.
 * 
 * @version $Revision: 1.6 $ $Date: 2004/03/19 08:16:47 $
 */
public class ResourceReferenceType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Unambiguous reference to the resource conforming to the IVOA
     *  standard for identifiers
     *  
     */
    private org.astrogrid.registry.beans.resource.IdentifierType _identifier;

    /**
     * the full name of a resource
     */
    private java.lang.String _title;

    /**
     * An account of the nature of the resource
     *  
     */
    private java.lang.String _description;

    /**
     * URL pointing to a human-readable document describing this 
     *  resource. 
     *  
     */
    private java.lang.String _referenceURL;


      //----------------/
     //- Constructors -/
    //----------------/

    public ResourceReferenceType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.ResourceReferenceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
     *  
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'identifier'. The field
     * 'identifier' has the following description: Unambiguous
     * reference to the resource conforming to the IVOA
     *  standard for identifiers
     *  
     * 
     * @return the value of field 'identifier'.
     */
    public org.astrogrid.registry.beans.resource.IdentifierType getIdentifier()
    {
        return this._identifier;
    } //-- org.astrogrid.registry.beans.resource.IdentifierType getIdentifier() 

    /**
     * Returns the value of field 'referenceURL'. The field
     * 'referenceURL' has the following description: URL pointing
     * to a human-readable document describing this 
     *  resource. 
     *  
     * 
     * @return the value of field 'referenceURL'.
     */
    public java.lang.String getReferenceURL()
    {
        return this._referenceURL;
    } //-- java.lang.String getReferenceURL() 

    /**
     * Returns the value of field 'title'. The field 'title' has
     * the following description: the full name of a resource
     * 
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

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
     * Sets the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
     *  
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'identifier'. The field 'identifier'
     * has the following description: Unambiguous reference to the
     * resource conforming to the IVOA
     *  standard for identifiers
     *  
     * 
     * @param identifier the value of field 'identifier'.
     */
    public void setIdentifier(org.astrogrid.registry.beans.resource.IdentifierType identifier)
    {
        this._identifier = identifier;
    } //-- void setIdentifier(org.astrogrid.registry.beans.resource.IdentifierType) 

    /**
     * Sets the value of field 'referenceURL'. The field
     * 'referenceURL' has the following description: URL pointing
     * to a human-readable document describing this 
     *  resource. 
     *  
     * 
     * @param referenceURL the value of field 'referenceURL'.
     */
    public void setReferenceURL(java.lang.String referenceURL)
    {
        this._referenceURL = referenceURL;
    } //-- void setReferenceURL(java.lang.String) 

    /**
     * Sets the value of field 'title'. The field 'title' has the
     * following description: the full name of a resource
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * Method unmarshalResourceReferenceType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.ResourceReferenceType unmarshalResourceReferenceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.ResourceReferenceType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.ResourceReferenceType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.ResourceReferenceType unmarshalResourceReferenceType(java.io.Reader) 

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
