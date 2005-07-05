/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ResourceName.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource;

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
 * the name of a potentially registered resource. That is, the
 * entity
 *  referred to may have an associated identifier.
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:56 $
 */
public class ResourceName extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * The URI form of the IVOA identifier for the resource refered
     * to
     *  
     */
    private java.lang.String _ivoId;


      //----------------/
     //- Constructors -/
    //----------------/

    public ResourceName() {
        super();
        setContent("");
    } //-- org.astrogrid.registry.beans.v10.resource.ResourceName()


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
        
        if (obj instanceof ResourceName) {
        
            ResourceName temp = (ResourceName)obj;
            if (this._content != null) {
                if (temp._content == null) return false;
                else if (!(this._content.equals(temp._content))) 
                    return false;
            }
            else if (temp._content != null)
                return false;
            if (this._ivoId != null) {
                if (temp._ivoId == null) return false;
                else if (!(this._ivoId.equals(temp._ivoId))) 
                    return false;
            }
            else if (temp._ivoId != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
     */
    public java.lang.String getContent()
    {
        return this._content;
    } //-- java.lang.String getContent() 

    /**
     * Returns the value of field 'ivoId'. The field 'ivoId' has
     * the following description: The URI form of the IVOA
     * identifier for the resource refered to
     *  
     * 
     * @return the value of field 'ivoId'.
     */
    public java.lang.String getIvoId()
    {
        return this._ivoId;
    } //-- java.lang.String getIvoId() 

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
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content)
    {
        this._content = content;
    } //-- void setContent(java.lang.String) 

    /**
     * Sets the value of field 'ivoId'. The field 'ivoId' has the
     * following description: The URI form of the IVOA identifier
     * for the resource refered to
     *  
     * 
     * @param ivoId the value of field 'ivoId'.
     */
    public void setIvoId(java.lang.String ivoId)
    {
        this._ivoId = ivoId;
    } //-- void setIvoId(java.lang.String) 

    /**
     * Method unmarshalResourceName
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.ResourceName unmarshalResourceName(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.ResourceName) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.ResourceName.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.ResourceName unmarshalResourceName(java.io.Reader) 

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
