/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: HttpURLType.java,v 1.8 2007/01/04 16:26:34 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.applications.beans.v1.types.HttpMethodType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * The URL for an http get or post service
 * 
 * @version $Revision: 1.8 $ $Date: 2007/01/04 16:26:34 $
 */
public class HttpURLType extends org.astrogrid.common.bean.BaseBean 
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
     * Field _method
     */
    private org.astrogrid.applications.beans.v1.types.HttpMethodType _method = org.astrogrid.applications.beans.v1.types.HttpMethodType.valueOf("get");


      //----------------/
     //- Constructors -/
    //----------------/

    public HttpURLType() {
        super();
        setContent("");
        setMethod(org.astrogrid.applications.beans.v1.types.HttpMethodType.valueOf("get"));
    } //-- org.astrogrid.applications.beans.v1.HttpURLType()


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
        
        if (obj instanceof HttpURLType) {
        
            HttpURLType temp = (HttpURLType)obj;
            if (this._content != null) {
                if (temp._content == null) return false;
                else if (!(this._content.equals(temp._content))) 
                    return false;
            }
            else if (temp._content != null)
                return false;
            if (this._method != null) {
                if (temp._method == null) return false;
                else if (!(this._method.equals(temp._method))) 
                    return false;
            }
            else if (temp._method != null)
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
     * Returns the value of field 'method'.
     * 
     * @return the value of field 'method'.
     */
    public org.astrogrid.applications.beans.v1.types.HttpMethodType getMethod()
    {
        return this._method;
    } //-- org.astrogrid.applications.beans.v1.types.HttpMethodType getMethod() 

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
     * Sets the value of field 'method'.
     * 
     * @param method the value of field 'method'.
     */
    public void setMethod(org.astrogrid.applications.beans.v1.types.HttpMethodType method)
    {
        this._method = method;
    } //-- void setMethod(org.astrogrid.applications.beans.v1.types.HttpMethodType) 

    /**
     * Method unmarshalHttpURLType
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.HttpURLType unmarshalHttpURLType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.HttpURLType) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.HttpURLType.class, reader);
    } //-- org.astrogrid.applications.beans.v1.HttpURLType unmarshalHttpURLType(java.io.Reader) 

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
