/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: IdentifierType.java,v 1.2 2004/07/01 10:18:31 nw Exp $
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
 * Class IdentifierType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/07/01 10:18:31 $
 */
public class IdentifierType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the identifier of a namespace under the control of
     *  a single naming authority
     */
    private java.lang.String _authorityID;

    /**
     * a name for a resource that is unique within an 
     *  authority's namespace
     */
    private java.lang.String _resourceKey;


      //----------------/
     //- Constructors -/
    //----------------/

    public IdentifierType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.IdentifierType()


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
        
        if (obj instanceof IdentifierType) {
        
            IdentifierType temp = (IdentifierType)obj;
            if (this._authorityID != null) {
                if (temp._authorityID == null) return false;
                else if (!(this._authorityID.equals(temp._authorityID))) 
                    return false;
            }
            else if (temp._authorityID != null)
                return false;
            if (this._resourceKey != null) {
                if (temp._resourceKey == null) return false;
                else if (!(this._resourceKey.equals(temp._resourceKey))) 
                    return false;
            }
            else if (temp._resourceKey != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'authorityID'. The field
     * 'authorityID' has the following description: the identifier
     * of a namespace under the control of
     *  a single naming authority
     * 
     * @return the value of field 'authorityID'.
     */
    public java.lang.String getAuthorityID()
    {
        return this._authorityID;
    } //-- java.lang.String getAuthorityID() 

    /**
     * Returns the value of field 'resourceKey'. The field
     * 'resourceKey' has the following description: a name for a
     * resource that is unique within an 
     *  authority's namespace
     * 
     * @return the value of field 'resourceKey'.
     */
    public java.lang.String getResourceKey()
    {
        return this._resourceKey;
    } //-- java.lang.String getResourceKey() 

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
     * Sets the value of field 'authorityID'. The field
     * 'authorityID' has the following description: the identifier
     * of a namespace under the control of
     *  a single naming authority
     * 
     * @param authorityID the value of field 'authorityID'.
     */
    public void setAuthorityID(java.lang.String authorityID)
    {
        this._authorityID = authorityID;
    } //-- void setAuthorityID(java.lang.String) 

    /**
     * Sets the value of field 'resourceKey'. The field
     * 'resourceKey' has the following description: a name for a
     * resource that is unique within an 
     *  authority's namespace
     * 
     * @param resourceKey the value of field 'resourceKey'.
     */
    public void setResourceKey(java.lang.String resourceKey)
    {
        this._resourceKey = resourceKey;
    } //-- void setResourceKey(java.lang.String) 

    /**
     * Method unmarshalIdentifierType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.IdentifierType unmarshalIdentifierType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.IdentifierType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.IdentifierType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.IdentifierType unmarshalIdentifierType(java.io.Reader) 

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
