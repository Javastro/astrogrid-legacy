/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: FormatType.java,v 1.4 2004/03/09 09:45:23 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

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
 * Class FormatType.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/09 09:45:23 $
 */
public class FormatType extends org.astrogrid.common.bean.BaseBean 
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
     * if true, then the content is a MIME Type
     *  
     */
    private boolean _isMIMEType = false;

    /**
     * keeps track of state for field: _isMIMEType
     */
    private boolean _has_isMIMEType;


      //----------------/
     //- Constructors -/
    //----------------/

    public FormatType() {
        super();
        setContent("");
    } //-- org.astrogrid.registry.beans.resource.dataservice.FormatType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIsMIMEType
     */
    public void deleteIsMIMEType()
    {
        this._has_isMIMEType= false;
    } //-- void deleteIsMIMEType() 

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
     * Returns the value of field 'isMIMEType'. The field
     * 'isMIMEType' has the following description: if true, then
     * the content is a MIME Type
     *  
     * 
     * @return the value of field 'isMIMEType'.
     */
    public boolean getIsMIMEType()
    {
        return this._isMIMEType;
    } //-- boolean getIsMIMEType() 

    /**
     * Method hasIsMIMEType
     */
    public boolean hasIsMIMEType()
    {
        return this._has_isMIMEType;
    } //-- boolean hasIsMIMEType() 

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
     * Sets the value of field 'isMIMEType'. The field 'isMIMEType'
     * has the following description: if true, then the content is
     * a MIME Type
     *  
     * 
     * @param isMIMEType the value of field 'isMIMEType'.
     */
    public void setIsMIMEType(boolean isMIMEType)
    {
        this._isMIMEType = isMIMEType;
        this._has_isMIMEType = true;
    } //-- void setIsMIMEType(boolean) 

    /**
     * Method unmarshalFormatType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.FormatType unmarshalFormatType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.FormatType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.FormatType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.FormatType unmarshalFormatType(java.io.Reader) 

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
