/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AccessURLType.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
 */

package org.astrogrid.registry.beans.resource;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AccessURLType.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:56 $
 */
public class AccessURLType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.String _content;

    /**
     * A flag indicating whether this should be interpreted as a
     * base
     *  URL, a full URL, or a URL to a directory that will produce
     * a 
     *  listing of files.
     *  
     */
    private org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType _use;


      //----------------/
     //- Constructors -/
    //----------------/

    public AccessURLType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.AccessURLType()


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
        
        if (obj instanceof AccessURLType) {
        
            AccessURLType temp = (AccessURLType)obj;
            if (this._content != null) {
                if (temp._content == null) return false;
                else if (!(this._content.equals(temp._content))) 
                    return false;
            }
            else if (temp._content != null)
                return false;
            if (this._use != null) {
                if (temp._use == null) return false;
                else if (!(this._use.equals(temp._use))) 
                    return false;
            }
            else if (temp._use != null)
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
     * Returns the value of field 'use'. The field 'use' has the
     * following description: A flag indicating whether this should
     * be interpreted as a base
     *  URL, a full URL, or a URL to a directory that will produce
     * a 
     *  listing of files.
     *  
     * 
     * @return the value of field 'use'.
     */
    public org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType getUse()
    {
        return this._use;
    } //-- org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType getUse() 

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
     * Sets the value of field 'use'. The field 'use' has the
     * following description: A flag indicating whether this should
     * be interpreted as a base
     *  URL, a full URL, or a URL to a directory that will produce
     * a 
     *  listing of files.
     *  
     * 
     * @param use the value of field 'use'.
     */
    public void setUse(org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType use)
    {
        this._use = use;
    } //-- void setUse(org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType) 

    /**
     * Method unmarshalAccessURLType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.AccessURLType unmarshalAccessURLType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.AccessURLType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.AccessURLType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.AccessURLType unmarshalAccessURLType(java.io.Reader) 

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
